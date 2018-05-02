package com.ai.cms.injection.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.cms.config.service.ConfigService;
import com.ai.cms.injection.entity.DownloadTask;
import com.ai.cms.injection.entity.InjectionObject;
import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.entity.ReceiveObject;
import com.ai.cms.injection.entity.ReceiveTask;
import com.ai.cms.injection.enums.DownloadModuleEnum;
import com.ai.cms.injection.enums.DownloadTaskStatusEnum;
import com.ai.cms.injection.enums.InjectionItemTypeEnum;
import com.ai.cms.injection.enums.InjectionStatusEnum;
import com.ai.cms.injection.enums.ReceiveResponseStatusEnum;
import com.ai.cms.injection.enums.ReceiveTaskStatusEnum;
import com.ai.cms.injection.repository.DownloadTaskRepository;
import com.ai.cms.injection.repository.InjectionPlatformRepository;
import com.ai.cms.injection.repository.ReceiveObjectRepository;
import com.ai.cms.injection.repository.ReceiveTaskRepository;
import com.ai.cms.media.entity.MediaFile;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.Series;
import com.ai.cms.media.service.MediaService;
import com.ai.common.enums.MediaStatusEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.exception.ServiceException;
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
	private InjectionService injectionService;

	@Autowired
	private InjectionPlatformRepository injectionPlatformRepository;

	@Autowired
	private ReceiveTaskRepository receiveTaskRepository;

	@Autowired
	private DownloadTaskRepository downloadTaskRepository;

	@Autowired
	private ReceiveObjectRepository receiveObjectRepository;

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

		checkTaskStatus(receiveTask);

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

		checkTaskStatus(receiveTask);

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

	public void checkTaskStatus(ReceiveTask receiveTask) {
		if (receiveTask.getDownloadFail() > 0) {
			receiveTask.setStatus(ReceiveTaskStatusEnum.FAIL.getKey());
		} else if (receiveTask.getDownloadTotal() == receiveTask
				.getDownloadSuccess()) {
			List<ReceiveObject> receiveObjectList = receiveObjectRepository
					.findByModuleAndPid(DownloadModuleEnum.RECEIVE.getKey(),
							receiveTask.getId());
			if (receiveObjectList.size() > 0) {// 接收到有剧头或节目对象
				InjectionPlatform injectionPlatform = injectionPlatformRepository
						.findOne(receiveTask.getPlatformId());
				if (injectionPlatform != null
						&& StringUtils.isNotEmpty(injectionPlatform
								.getDependPlatformId())) {// 有依赖平台
					receiveTask.setStatus(ReceiveTaskStatusEnum.WAIT_INJECTION
							.getKey());
					// 等待1分钟
					receiveTask.setNextCheckTime(DateUtils.addMinutes(
							new Date(), 1));
				} else {
					receiveTask.setStatus(ReceiveTaskStatusEnum.SUCCESS
							.getKey());
				}
			} else {
				receiveTask.setStatus(ReceiveTaskStatusEnum.SUCCESS.getKey());
			}
		} else {
			receiveTask.setStatus(ReceiveTaskStatusEnum.PROCESSING.getKey());
		}
	}

	public boolean autoInjectionTask(ReceiveTask receiveTask,
			InjectionPlatform injectionPlatform) {
		List<ReceiveObject> receiveObjectList = receiveObjectRepository
				.findByModuleAndPidAndStatus(
						DownloadModuleEnum.RECEIVE.getKey(),
						receiveTask.getId(),
						InjectionStatusEnum.DEFAULT.getKey());
		if (receiveObjectList.size() > 0) {// 接收到有剧头或节目对象
			if (injectionPlatform != null
					&& injectionPlatform.getNeedInjection() == YesNoEnum.YES
							.getKey()
					&& StringUtils.isNotEmpty(injectionPlatform
							.getInjectionPlatformId())) {// 需要自动分发
				String[] platformIdString = injectionPlatform
						.getInjectionPlatformId().split(",");
				Long[] platformIds = new Long[platformIdString.length];
				String[] templateIds = new String[platformIdString.length];
				Integer[] prioritys = new Integer[platformIdString.length];
				for (int i = 0; i < platformIdString.length; i++) {
					platformIds[i] = Long.valueOf(platformIdString[i]);
					prioritys[i] = receiveTask.getPriority();
				}
				for (ReceiveObject receiveObject : receiveObjectList) {// 先分发剧头
					if (receiveObject.getItemType() == InjectionItemTypeEnum.SERIES
							.getKey()) {
						Series series = mediaService
								.findSeriesById(receiveObject.getItemId());
						if (series == null) {
							continue;
						}
						for (int i = 0; i < platformIdString.length; i++) {
							templateIds[i] = series.getTemplateId();
						}
						injectionService.inInjection(series, platformIds,
								templateIds, prioritys,
								receiveTask.getCpCode(), false);
						receiveObject
								.setStatus(InjectionStatusEnum.INJECTION_ING
										.getKey());
						receiveObjectRepository.save(receiveObject);
					}
				}
				for (ReceiveObject receiveObject : receiveObjectList) {// 分发节目
					if (receiveObject.getItemType() == InjectionItemTypeEnum.PROGRAM
							.getKey()) {
						Program program = mediaService
								.findProgramById(receiveObject.getItemId());
						if (program == null) {
							continue;
						}
						for (int i = 0; i < platformIdString.length; i++) {
							templateIds[i] = program.getTemplateId();
						}
						injectionService.inInjection(program.getSeries(),
								program, platformIds, templateIds, prioritys,
								receiveTask.getCpCode());
						receiveObject
								.setStatus(InjectionStatusEnum.INJECTION_ING
										.getKey());
						receiveObjectRepository.save(receiveObject);
					}
				}
				// 等待5分钟
				receiveTask.setNextCheckTime(DateUtils
						.addMinutes(new Date(), 5));
				return true;
			}
		}
		return false;
	}

	public void checkInjectionTask(ReceiveTask receiveTask,
			InjectionPlatform injectionPlatform) {
		List<ReceiveObject> receiveObjectList = receiveObjectRepository
				.findByModuleAndPid(DownloadModuleEnum.RECEIVE.getKey(),
						receiveTask.getId());
		if (receiveObjectList.size() > 0) {// 接收到有剧头或节目对象
			if (injectionPlatform != null
					&& StringUtils.isNotEmpty(injectionPlatform
							.getDependPlatformId())) {// 有剧头或节目对象,有依赖平台
				for (ReceiveObject receiveObject : receiveObjectList) {
					if (receiveObject.getStatus() == InjectionStatusEnum.INJECTION_SUCCESS
							.getKey()) {
						continue;
					}
					List<InjectionObject> injectionObjectListAll = null;
					if (receiveObject.getItemType() == InjectionItemTypeEnum.SERIES
							.getKey()) {
						injectionObjectListAll = injectionService
								.findInjectionObjectList(
										InjectionItemTypeEnum.SERIES,
										receiveObject.getItemId());
					} else if (receiveObject.getItemType() == InjectionItemTypeEnum.PROGRAM
							.getKey()) {
						injectionObjectListAll = injectionService
								.findInjectionObjectList(
										InjectionItemTypeEnum.PROGRAM,
										receiveObject.getItemId());
					}
					if (injectionObjectListAll != null
							&& injectionObjectListAll.size() > 0) {
						for (String dependPlatformId : injectionPlatform
								.getDependPlatformId().split(",")) {
							boolean found = false;
							for (InjectionObject injectionObject : injectionObjectListAll) {
								if (dependPlatformId.equals(""
										+ injectionObject.getPlatformId())) {
									found = true;
									if (injectionObject.getInjectionStatus() != InjectionStatusEnum.INJECTION_SUCCESS
											.getKey()) {
										throw new ServiceException(
												"依赖平台没有分发成功！请耐心等待,5分钟后再试！");
									}
								}
							}
							if (!found) {
								throw new ServiceException(
										"依赖平台暂还没有分发！请耐心等待,5分钟后再试！");
							}
						}
						receiveObject
								.setStatus(InjectionStatusEnum.INJECTION_SUCCESS
										.getKey());
						receiveObjectRepository.save(receiveObject);
					}
				}
				boolean allInjectionSuccess = true;
				for (ReceiveObject receiveObject : receiveObjectList) {
					if (receiveObject.getStatus() != InjectionStatusEnum.INJECTION_SUCCESS
							.getKey()) {
						allInjectionSuccess = false;
						break;
					}
				}
				if (allInjectionSuccess) {
					receiveTask.setStatus(ReceiveTaskStatusEnum.SUCCESS
							.getKey());
				}
			} else {
				receiveTask.setStatus(ReceiveTaskStatusEnum.SUCCESS.getKey());
			}
		} else {
			receiveTask.setStatus(ReceiveTaskStatusEnum.SUCCESS.getKey());
		}
	}
}