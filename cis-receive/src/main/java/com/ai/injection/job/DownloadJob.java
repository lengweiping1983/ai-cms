package com.ai.injection.job;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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

import com.ai.AdminGlobal;
import com.ai.cms.injection.entity.DownloadTask;
import com.ai.cms.injection.enums.DownloadTaskStatusEnum;
import com.ai.cms.injection.repository.DownloadTaskRepository;
import com.ai.cms.injection.service.DownloadService;
import com.ai.common.transfer.FTPTransferMode;
import com.ai.common.transfer.ServerToClient;
import com.ai.common.transfer.TaskCallback;
import com.ai.common.utils.MD5Util;

@Service
public class DownloadJob {
	private static final Log logger = LogFactory.getLog(DownloadJob.class);

	public static java.text.DecimalFormat df = new java.text.DecimalFormat(
			"#.#");
	public static double SIZE_KB = 1024;
	public static double SIZE_MB = 1024 * 1024;
	public static double SIZE_GB = 1024 * 1024 * 1024;

	public static String getFileSizeS(java.lang.Long fileSize) {
		if (fileSize == null) {
			return "";
		}
		if (fileSize > SIZE_GB) {
			return (df.format(fileSize / SIZE_GB) + "GB");
		} else if (fileSize > SIZE_MB) {
			return (df.format(fileSize / SIZE_MB) + "MB");
		} else {
			return (df.format(fileSize / SIZE_KB) + "KB");
		}
	}

	public static String formatSpeed(long s) {
		if (s > 0.1 * 1024 * 1024 * 1024) {
			float f = s / 1024f / 1024f / 1024f;
			return String.format("%.1f GB", f);
		} else if (s > 0.1 * 1024 * 1024) {
			float f = s / 1024f / 1024f;
			return String.format("%.1f MB", f);
		} else {
			float f = s / 1024f;
			return String.format("%.1f kb", f);
		}
	}

	@Value("${download.enabled:false}")
	private boolean downloadEnabled = false;

	@Value("${download.task.maxNum:5}")
	private int taskMaxNum;

	@Value("${download.task.maxRequestTimes:3}")
	private int taskMaxRequestTimes;

	@Value("${download.task.timeout:12}")
	private int taskTimeout;

	@Autowired
	private DownloadTaskRepository downloadTaskRepository;

	@Autowired
	private DownloadService downloadService;

	public static boolean restart = true;

	@Scheduled(cron = "${download.task.schedule:0 0/5 * * * ?}")
	public void executeDownload() {
		if (!downloadEnabled) {
			return;
		}
		if (restart) {
			downloadService.resetDownloadingStatus();
			restart = false;
		}
		logger.info("download begin.");
		long startTime = System.currentTimeMillis();
		try {
			downloadTask();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("download end run("
				+ (System.currentTimeMillis() - startTime) / 1000 + ")s.");
	}

	private void downloadTask() {
		int newTaskMaxNum = taskMaxNum;
		if (newTaskMaxNum <= 0) {
			return;
		}

		PageRequest pageRequest = new PageRequest(0, newTaskMaxNum);
		Page<DownloadTask> page = downloadTaskRepository.findByStatus(
				DownloadTaskStatusEnum.DOWNLOADING.getKey(), pageRequest);
		List<DownloadTask> runTaskList = page.getContent();
		newTaskMaxNum = newTaskMaxNum - runTaskList.size();
		Date currentTime = new Date();
		for (DownloadTask downloadTask : runTaskList) {
			if (downloadTask.getLastRequestTime() == null) {
				downloadTask.setLastRequestTime(currentTime);
				downloadService.saveDownloadTask(downloadTask);
			}

			if ((currentTime.getTime() - downloadTask.getLastRequestTime()
					.getTime()) > taskTimeout * 3600 * 1000l) {
				downloadTask.setResponseCode("-1");
				downloadTask.setResponseMsg("系统置为超时");
				downloadTask.setResponseTime(currentTime);
				downloadTask.setStatus(DownloadTaskStatusEnum.TIMEOUT.getKey()); // 超时
				downloadService.saveDownloadTask(downloadTask);

				downloadService.calculateFailDownloadTask(downloadTask);

				newTaskMaxNum++;
				logger.info("download find timeout task！");
			}
		}

		if (newTaskMaxNum <= 0) {
			logger.info("download task pool is full, wait next time！");
			return;
		}

		pageRequest = new PageRequest(0, newTaskMaxNum);
		page = downloadTaskRepository.findByRequestTimes(taskMaxRequestTimes,
				pageRequest);
		List<DownloadTask> taskList = page.getContent();
		for (DownloadTask downloadTask : taskList) {
			if (downloadTask.getFirstRequestTime() == null) {
				downloadTask.setFirstRequestTime(currentTime);
			}
			downloadTask.setLastRequestTime(currentTime);

			downloadTask.setRequestTimes(downloadTask.getRequestTimes() + 1);
			downloadTask.setRequestTotalTimes(downloadTask
					.getRequestTotalTimes() + 1);
			boolean resumeBroken = true;
			if (downloadTask.getStatus() == DownloadTaskStatusEnum.REDOWNLOAD
					.getKey()) {
				resumeBroken = false;
			}
			downloadTask.setStatus(DownloadTaskStatusEnum.DOWNLOADING.getKey());
			downloadService.saveDownloadTask(downloadTask);
			DownloadThread downloadThread = new DownloadThread(downloadTask,
					resumeBroken);
			downloadThread.start();
		}
	}

	TaskCallback callback = new TaskCallback() {

		@Override
		public synchronized boolean begining(long taskId, long totalSize,
				long beginSize) {
			DownloadTask downloadTask = downloadTaskRepository.findOne(taskId);
			if (downloadTask == null) {
				return false;
			}
			downloadTask.setFileSize(totalSize);
			downloadTask.setStatus(DownloadTaskStatusEnum.DOWNLOADING.getKey());
			downloadService.saveDownloadTask(downloadTask);
			logger.info("ThreadId=" + Thread.currentThread().getId()
					+ " DownloadTaskId=" + taskId + ",totalSize=" + totalSize);
			return true;
		}

		@Override
		public synchronized boolean success(long taskId) {
			DownloadTask downloadTask = downloadTaskRepository.findOne(taskId);
			if (downloadTask == null) {
				return false;
			}
			downloadTask.setResponseCode("0");
			downloadTask.setResponseMsg("下载成功！");
			downloadTask.setResponseTime(new Date());
			downloadTask.setPercent(100);
			downloadTask.setStatus(DownloadTaskStatusEnum.SUCCESS.getKey());

			try {
				String fileMd5 = MD5Util.getMd5ByFile(new File(AdminGlobal
						.getVideoUploadPath(downloadTask.getOutputFilePath())));
				downloadTask.setFileMd5(fileMd5);
			} catch (NoSuchAlgorithmException e) {
				logger.error(e.getMessage(), e);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}

			downloadService.saveDownloadTask(downloadTask);
			downloadService.calculateSuccessDownloadTask(downloadTask);
			logger.info("ThreadId=" + Thread.currentThread().getId()
					+ " DownloadTaskId=" + taskId + ",success");
			return true;
		}

		@Override
		public synchronized boolean fail(long taskId, int times, String message) {
			if (times >= taskMaxRequestTimes) {
				DownloadTask downloadTask = downloadTaskRepository
						.findOne(taskId);
				if (downloadTask == null) {
					return false;
				}
				downloadTask.setStatus(DownloadTaskStatusEnum.FAIL.getKey());
				downloadService.saveDownloadTask(downloadTask);
				downloadService.calculateFailDownloadTask(downloadTask);
			}
			logger.info("ThreadId=" + Thread.currentThread().getId()
					+ " DownloadTaskId=" + taskId + ",fail,times=" + times
					+ ",message=" + message);
			return true;
		}

		@Override
		public boolean percent(long taskId, double percent) {
			DownloadTask downloadTask = downloadTaskRepository.findOne(taskId);
			if (downloadTask == null) {
				return false;
			}
			downloadTask.setPercent((int) (percent * 100));
			downloadService.saveDownloadTask(downloadTask);
			logger.info("ThreadId=" + Thread.currentThread().getId()
					+ " DownloadTaskId=" + taskId + ",percent=" + percent);
			if (downloadTask.getStatus() == DownloadTaskStatusEnum.PAUSE
					.getKey()
					|| downloadTask.getStatus() == DownloadTaskStatusEnum.STOP
							.getKey()) {
				return false;
			}
			return true;
		}
	};

	class DownloadThread extends Thread {
		private Long downloadTaskId;
		private String inputFilePath;
		private String outputFilePath;
		private boolean resumeBroken;

		public DownloadThread(DownloadTask downloadTask, boolean resumeBroken) {
			downloadTaskId = downloadTask.getId();

			inputFilePath = downloadTask.getInputFilePath();
			outputFilePath = AdminGlobal.getVideoUploadPath(downloadTask
					.getOutputFilePath());
			this.resumeBroken = resumeBroken;
		}

		@Override
		public void run() {
			if (downloadTaskId == null) {
				return;
			}
			try {
				logger.info("ThreadId=" + Thread.currentThread().getId()
						+ " DownloadTaskId=" + downloadTaskId + " filePath{"
						+ inputFilePath + "} download begin...");
				ServerToClient stc = new ServerToClient();
				stc.setResumeBroken(resumeBroken);
				stc.setRetryTimes(taskMaxRequestTimes);
				stc.setRetryWaitTime(1L);
				stc.setTransferMode(FTPTransferMode.DOWNLOAD);
				if (stc.transfer(inputFilePath, outputFilePath, downloadTaskId,
						callback)) {
					logger.info("ThreadId=" + Thread.currentThread().getId()
							+ " DownloadTaskId=" + downloadTaskId
							+ " filePath{" + inputFilePath
							+ "} download finish.");
				} else {
					logger.info("ThreadId=" + Thread.currentThread().getId()
							+ " DownloadTaskId=" + downloadTaskId
							+ " filePath{" + inputFilePath + "} download end.");
				}
			} catch (Exception e) {
				logger.error("ThreadId=" + Thread.currentThread().getId()
						+ " DownloadTaskId=" + downloadTaskId + " filePath{"
						+ inputFilePath + "} download error:", e);
			}
		}
	}

}
