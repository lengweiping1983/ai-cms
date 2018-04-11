package com.ai.cms.transcode.job;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ai.cms.transcode.entity.TranscodeTask;
import com.ai.cms.transcode.enums.TranscodeTaskStatusEnum;
import com.ai.cms.transcode.enums.TranscodeTaskTypeEnum;
import com.ai.cms.transcode.repository.TranscodeTaskRepository;
import com.ai.cms.transcode.service.TranscodeService;
import com.ai.common.enums.YesNoEnum;

@Service
public class TranscodeTaskJob {
	private static final Log logger = LogFactory.getLog(TranscodeTaskJob.class);

	@Value("${transcode.enabled:false}")
	private boolean transcodeEnabled;

	@Value("${transcode.mode:'local'}")
	private String transcodeMode;

	@Value("${transcode.task.maxNum:20}")
	private int taskMaxNum;

	@Value("${transcode.task.maxRequestTimes:3}")
	private int taskMaxRequestTimes;

	@Value("${transcode.task.timeout:12}")
	private int taskTimeout;

	@Autowired
	private TranscodeService transcodeService;

	@Autowired
	private TranscodeTaskRepository transcodeTaskRepository;

	@Scheduled(cron = "${transcode.task.schedule:0 0/5 * * * ?}")
	public void execute() {
		if (!transcodeEnabled) {
			return;
		}
		logger.info("TranscodeTaskJob begin.");
		long startTime = System.currentTimeMillis();
		try {
			if ("local".equalsIgnoreCase(transcodeMode)) {
				syncLocalTask();
			} else if ("online".equalsIgnoreCase(transcodeMode)) {
				sendOnlineTask();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("TranscodeTaskJob end run("
				+ (System.currentTimeMillis() - startTime) / 1000 + ")s.");
	}

	private void syncLocalTask() {
		int newTaskMaxNum = taskMaxNum;
		if (newTaskMaxNum <= 0) {
			return;
		}

		// 获取已回写的任务
		PageRequest pageRequest = new PageRequest(0, newTaskMaxNum);
		Page<TranscodeTask> page = transcodeTaskRepository.findByIsCallback(
				YesNoEnum.YES.getKey(), pageRequest);
		List<TranscodeTask> runTaskList = page.getContent();
		if (runTaskList != null) {
			for (TranscodeTask transcodeTask : runTaskList) {
				transcodeService
						.updateMediaStatusByTranscodeTask(transcodeTask);
			}
		}
	}

	private void sendOnlineTask() {
		int newTaskMaxNum = taskMaxNum;
		if (newTaskMaxNum <= 0) {
			return;
		}

		// 获取正在运行的Task，根据数量要进行后面新增Task的数据
		PageRequest pageRequest = new PageRequest(0, newTaskMaxNum);
		Page<TranscodeTask> page = transcodeTaskRepository.findByTypeAndStatus(
				TranscodeTaskTypeEnum.TRANSCODE.getKey(),
				TranscodeTaskStatusEnum.PROCESSING.getKey(), pageRequest);
		List<TranscodeTask> runTaskList = page.getContent();
		if (runTaskList != null) {
			newTaskMaxNum = newTaskMaxNum - runTaskList.size();
			Date currentTime = new Date();
			for (TranscodeTask transcodeTask : runTaskList) {
				if (transcodeTask.getLastRequestTime() == null) {
					transcodeTask.setLastRequestTime(currentTime);
					transcodeTaskRepository.save(transcodeTask);
				}

				// 如果任务执行超过12个小时，置为超时。
				if ((currentTime.getTime() - transcodeTask.getLastRequestTime()
						.getTime()) > taskTimeout * 3600 * 1000l) {
					transcodeTask.setStatus(TranscodeTaskStatusEnum.TIMEOUT
							.getKey()); // 超时
					transcodeTask.setResponseMsg("系统置为超时");
					transcodeTask.setResponseTime(currentTime);
					transcodeTaskRepository.save(transcodeTask);
					transcodeService.calculateFailTranscodeTask(transcodeTask);

					newTaskMaxNum++;
					logger.info("transcode task find timeout!");
				}
			}
		}

		if (newTaskMaxNum <= 0) {
			logger.info("transcode task pool is full, wait next time!");
			return;
		}

		// 获取最多可以执行的任务列表
		pageRequest = new PageRequest(0, newTaskMaxNum);
		page = transcodeTaskRepository.findByRequestTimes(taskMaxRequestTimes,
				pageRequest);
		List<TranscodeTask> taskList = page.getContent();
		for (TranscodeTask transcodeTask : taskList) {
			int taskType = transcodeTask.getType();
			TranscodeTaskTypeEnum taskTypeEnum = TranscodeTaskTypeEnum
					.getEnumByKey(taskType);
			switch (taskTypeEnum) {
			case OFFLINE_UPLOAD:
				transcodeService.offlineUpload(transcodeTask);
				break;
			case TRANSCODE:
				if (newTaskMaxNum <= 0) {
					continue;
				}
				newTaskMaxNum--;
				transcodeService.transcode(transcodeTask);
				break;
			case IMAGE:
				transcodeService.image(transcodeTask);
				break;
			case AFTER:
				// 后续添加后处理的逻辑，暂时不需要
				break;
			default:
				break;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
