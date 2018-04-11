package com.ai.cms.injection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.cms.config.service.ConfigService;
import com.ai.cms.injection.entity.DownloadTask;
import com.ai.cms.injection.entity.ReceiveTask;
import com.ai.cms.injection.enums.DownloadModuleEnum;
import com.ai.cms.injection.enums.DownloadTaskStatusEnum;
import com.ai.cms.injection.enums.ReceiveResponseStatusEnum;
import com.ai.cms.injection.enums.ReceiveTaskStatusEnum;
import com.ai.cms.injection.repository.DownloadTaskRepository;
import com.ai.cms.injection.repository.InjectionPlatformRepository;
import com.ai.cms.injection.repository.ReceiveTaskRepository;
import com.ai.cms.media.entity.MediaFile;
import com.ai.cms.media.service.MediaService;
import com.ai.common.enums.MediaStatusEnum;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;

@Service
@Transactional(value = "slaveTransactionManager", readOnly = false)
public class DownloadService extends AbstractService<DownloadTask, Long> {

	@Autowired
	private ConfigService configService;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private InjectionPlatformRepository injectionPlatformRepository;

	@Autowired
	private ReceiveTaskRepository receiveTaskRepository;

	@Autowired
	private DownloadTaskRepository downloadTaskRepository;

	@Override
	public AbstractRepository<DownloadTask, Long> getRepository() {
		return downloadTaskRepository;
	}

	public void saveDownloadTask(DownloadTask downloadTask) {
		downloadTaskRepository.save(downloadTask);
	}

	public void resetDownloadingStatus() {
		downloadTaskRepository.resetDownloadingStatus();
	}

	/**
	 * 根据下载任务更新媒资状态
	 * 
	 * @param downloadTask
	 */
	public void updateMediaStatusByDownloadTask(DownloadTask downloadTask) {
		if (DownloadTaskStatusEnum.SUCCESS.getKey() == downloadTask.getStatus()) {
			calculateSuccessDownloadTask(downloadTask);
		} else if (DownloadTaskStatusEnum.FAIL.getKey() == downloadTask
				.getStatus()) {
			calculateFailDownloadTask(downloadTask);
		}
	}

	public void calculateSuccessDownloadTask(DownloadTask downloadTask) {
		logger.info("ThreadId=" + Thread.currentThread().getId()
				+ " calculateSuccessDownloadTask " + downloadTask.getId()
				+ " begin.");
		ReceiveTask receiveTask = receiveTaskRepository.findOne(downloadTask
				.getPid());

		long downloadSuccess = downloadTaskRepository
				.countByModuleAndPidAndStatus(
						DownloadModuleEnum.RECEIVE.getKey(),
						receiveTask.getId(),
						DownloadTaskStatusEnum.SUCCESS.getKey());
		receiveTask.setDownloadSuccess(downloadSuccess);

		long downloadFail = downloadTaskRepository.countFailNumByModuleAndPid(
				DownloadModuleEnum.RECEIVE.getKey(), receiveTask.getId());
		receiveTask.setDownloadFail(downloadFail);

		if (receiveTask.getDownloadFail() > 0) {
			receiveTask.setStatus(ReceiveTaskStatusEnum.FAIL.getKey());
		} else if (receiveTask.getDownloadTotal() == receiveTask
				.getDownloadSuccess()) {
			receiveTask.setStatus(ReceiveTaskStatusEnum.SUCCESS.getKey());
		} else {
			receiveTask.setStatus(ReceiveTaskStatusEnum.PROCESSING.getKey());
		}
		receiveTask.setResponseStatus(ReceiveResponseStatusEnum.DEFAULT
				.getKey());
		receiveTaskRepository.save(receiveTask);

		if (downloadTask.getMediaFileId() != null) {
			MediaFile mediaFile = mediaService.findMediaFileById(downloadTask
					.getMediaFileId());
			mediaFile.setFileSize(downloadTask.getFileSize());
			mediaFile.setFileMd5(downloadTask.getFileMd5());
			mediaService.saveMediaFile(mediaFile);

			mediaService.updateMediaFileMediaStatus(
					downloadTask.getMediaFileId(), MediaStatusEnum.OK.getKey(),
					downloadTask.getOutputFilePath(), true);
		}
		logger.info("ThreadId=" + Thread.currentThread().getId()
				+ " calculateSuccessDownloadTask " + downloadTask.getId()
				+ " end.");
	}

	public void calculateFailDownloadTask(DownloadTask downloadTask) {
		ReceiveTask receiveTask = receiveTaskRepository.findOne(downloadTask
				.getPid());

		long downloadFail = downloadTaskRepository.countFailNumByModuleAndPid(
				DownloadModuleEnum.RECEIVE.getKey(), receiveTask.getId());
		receiveTask.setDownloadFail(downloadFail);

		if (receiveTask.getDownloadFail() > 0) {
			receiveTask.setStatus(ReceiveTaskStatusEnum.FAIL.getKey());
		} else if (receiveTask.getDownloadTotal() == receiveTask
				.getDownloadSuccess()) {
			receiveTask.setStatus(ReceiveTaskStatusEnum.SUCCESS.getKey());
		} else {
			receiveTask.setStatus(ReceiveTaskStatusEnum.PROCESSING.getKey());
		}
		receiveTask.setResponseStatus(ReceiveResponseStatusEnum.DEFAULT
				.getKey());
		receiveTaskRepository.save(receiveTask);

		if (downloadTask.getMediaFileId() != null) {
			mediaService.updateMediaFileMediaStatus(
					downloadTask.getMediaFileId(),
					MediaStatusEnum.DOWNLOAD_FAIL.getKey(), null, true);
		}
	}

	public void resetDownloadTask(DownloadTask downloadTask) {
		downloadTask.setRequestTimes(0);
		downloadTask.setStatus(DownloadTaskStatusEnum.WAIT.getKey());
		downloadTaskRepository.save(downloadTask);

		ReceiveTask receiveTask = receiveTaskRepository.findOne(downloadTask
				.getPid());
		receiveTask.setStatus(ReceiveTaskStatusEnum.PROCESSING.getKey());
		receiveTaskRepository.save(receiveTask);

		if (downloadTask.getMediaFileId() != null) {
			mediaService.updateMediaFileMediaStatus(
					downloadTask.getMediaFileId(),
					MediaStatusEnum.DOWNLOAD.getKey(), null, true);
		}
	}
}