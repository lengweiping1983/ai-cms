package com.ai.cms.transcode.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.cms.config.entity.MediaTemplate;
import com.ai.cms.config.repository.MediaTemplateRepository;
import com.ai.cms.media.entity.MediaFile;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.Series;
import com.ai.cms.media.repository.MediaFileRepository;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.cms.media.repository.SeriesRepository;
import com.ai.cms.media.service.MediaService;
import com.ai.cms.transcode.entity.TranscodeRequest;
import com.ai.cms.transcode.entity.TranscodeRequestFile;
import com.ai.cms.transcode.entity.TranscodeTask;
import com.ai.cms.transcode.enums.GenProgramNameRuleEnum;
import com.ai.cms.transcode.enums.GenTaskModeStatusEnum;
import com.ai.cms.transcode.enums.NeedSnapshotEnum;
import com.ai.cms.transcode.enums.TranscodeRequestStatusEnum;
import com.ai.cms.transcode.enums.TranscodeRequestTypeEnum;
import com.ai.cms.transcode.enums.TranscodeTaskStatusEnum;
import com.ai.cms.transcode.enums.TranscodeTaskTypeEnum;
import com.ai.cms.transcode.repository.TranscodeRequestFileRepository;
import com.ai.cms.transcode.repository.TranscodeRequestRepository;
import com.ai.cms.transcode.repository.TranscodeTaskRepository;
import com.ai.cms.transcode.utils.ShaTaClient;
import com.ai.common.enums.MediaFileTypeEnum;
import com.ai.common.enums.MediaStatusEnum;
import com.ai.common.enums.ProgramTypeEnum;
import com.ai.common.enums.SourceEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.exception.ServiceException;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.common.utils.PathUtils;

@Service
@Transactional(value = "slaveTransactionManager", readOnly = false)
public class TranscodeService extends AbstractService<TranscodeRequest, Long> {
	private static final Log logger = LogFactory.getLog(TranscodeService.class);

	@Value("${transcode.output.path:/transcode}")
	private String transcodeOutputPath;

	@Value("${transcode.callback.offlineUpload.url:}")
	private String offlineUploadCallBack;

	@Value("${transcode.callback.encode.url:}")
	private String encodeCallBack;

	@Value("${transcode.callback.image.url:}")
	private String imageCallBack;

	@Autowired
	private MediaTemplateRepository mediaTemplateRepository;

	@Autowired
	private TranscodeRequestRepository transcodeRequestRepository;

	@Autowired
	private TranscodeRequestFileRepository transcodeRequestFileRepository;

	@Autowired
	private TranscodeTaskRepository transcodeTaskRepository;

	@Autowired
	private SeriesRepository seriesRepository;

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private MediaFileRepository mediaFileRepository;

	@Autowired
	private MediaService mediaService;

	@Override
	public AbstractRepository<TranscodeRequest, Long> getRepository() {
		return transcodeRequestRepository;
	}

	public void deleteTranscodeRequest(TranscodeRequest transcodeRequest) {
		if (transcodeRequest != null) {
			transcodeTaskRepository.deleteByRequestId(transcodeRequest.getId());
			transcodeRequestFileRepository.deleteByRequestId(transcodeRequest
					.getId());
			transcodeRequestRepository.delete(transcodeRequest);
		}
	}

	public void deleteTranscodeTask(TranscodeTask transcodeTask) {
		if (transcodeTask != null) {
			transcodeTaskRepository.delete(transcodeTask);
		}
	}

	public TranscodeTask findTranscodeTaskByCloudTaskId(String cloudTaskId) {
		List<TranscodeTask> list = transcodeTaskRepository
				.findByCloudTaskId(cloudTaskId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	private void startTask(TranscodeTask transcodeTask) {
		Date currentTime = new Date();
		if (transcodeTask.getFirstRequestTime() == null) {
			transcodeTask.setFirstRequestTime(currentTime);
		}
		transcodeTask.setLastRequestTime(currentTime);

		transcodeTask.setRequestTimes(transcodeTask.getRequestTimes() + 1);
		transcodeTask
				.setRequestTotalTimes(transcodeTask.getRequestTotalTimes() + 1);
	}

	public void offlineUpload(TranscodeTask transcodeTask) {
		startTask(transcodeTask);

		ShaTaClient stClient = new ShaTaClient();
		String cloudTaskId = "";
		String responseMsg = "";
		try {
			responseMsg = stClient.offlineUpload(
					transcodeTask.getInputFilePath(),
					transcodeTask.getOutputFilePath(), offlineUploadCallBack);
			JSONObject json = new JSONObject(responseMsg);
			try {
				cloudTaskId = json.getString("path");
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			transcodeTask
					.setStatus(TranscodeTaskStatusEnum.PROCESSING.getKey());
			transcodeTask.setCloudTaskId(cloudTaskId);
			transcodeTaskRepository.save(transcodeTask);
			logger.info("offlineUpload task append new offlineUpload task!");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			transcodeTask.setStatus(TranscodeTaskStatusEnum.FAIL.getKey());
			transcodeTask.setResponseMsg(responseMsg);
			transcodeTaskRepository.save(transcodeTask);
		}
	}

	public void transcode(TranscodeTask transcodeTask) {
		startTask(transcodeTask);

		ShaTaClient stClient = new ShaTaClient();
		String cloudTaskId = "";
		String responseMsg = "";
		try {
			responseMsg = stClient.encode(transcodeTask.getInputFilePath(),
					transcodeTask.getProfile(),
					transcodeTask.getOutputFilePath(), encodeCallBack);
			JSONObject json = new JSONObject(responseMsg);
			try {
				if (json.has("task_id")) {
					cloudTaskId = json.getString("task_id");
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			if (StringUtils.isNotEmpty(cloudTaskId)) {
				transcodeTask.setStatus(TranscodeTaskStatusEnum.PROCESSING
						.getKey());
				transcodeTask.setCloudTaskId(cloudTaskId);
				transcodeTaskRepository.save(transcodeTask);
				logger.info("transcode task append new encode task!");
			} else {
				String error_code = null;
				String message = null;
				try {
					error_code = json.getString("error_code");
					message = json.getString("message");
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				transcodeTask.setStatus(TranscodeTaskStatusEnum.FILE_NOT_FUOUND
						.getKey());
				transcodeTask.setResponseTime(new Date());
				transcodeTask.setResponseMsg(message);
				transcodeTask.setResponseCode(error_code);
				transcodeTaskRepository.save(transcodeTask);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			transcodeTask.setStatus(TranscodeTaskStatusEnum.FAIL.getKey());
			transcodeTask.setResponseTime(new Date());
			transcodeTask.setResponseMsg(responseMsg);
			transcodeTaskRepository.save(transcodeTask);
		}
	}

	public void image(TranscodeTask transcodeTask) {
		startTask(transcodeTask);

		ShaTaClient stClient = new ShaTaClient();
		String cloudTaskId = "";
		String responseMsg = "";
		try {
			responseMsg = stClient.image(transcodeTask.getInputFilePath(),
					transcodeTask.getProfile(), transcodeTask.getTimePoints(),
					transcodeTask.getOutputFilePath(), imageCallBack);
			JSONObject json = new JSONObject(responseMsg);
			try {
				cloudTaskId = json.getString("task_id");
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			transcodeTask
					.setStatus(TranscodeTaskStatusEnum.PROCESSING.getKey());
			transcodeTask.setCloudTaskId(cloudTaskId);
			transcodeTaskRepository.save(transcodeTask);
			logger.info("image task append new image task!");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			transcodeTask.setStatus(TranscodeTaskStatusEnum.FAIL.getKey());
			transcodeTask.setResponseMsg(responseMsg);
			transcodeTaskRepository.save(transcodeTask);
		}
	}

	/**
	 * 任务处理返回结果
	 * 
	 * @param cloudTaskId
	 * @param result
	 * @param message
	 * @return
	 */
	public boolean updateTranscodeTaskFromCallback(String cloudTaskId,
			int result, String message) {
		if (StringUtils.isEmpty(cloudTaskId)) {
			return false;
		}
		TranscodeTask transcodeTask = findTranscodeTaskByCloudTaskId(cloudTaskId);
		if (transcodeTask == null) {
			return false;
		}
		if (result == 0) {
			transcodeTask.setStatus(TranscodeTaskStatusEnum.SUCCESS.getKey());
		} else {
			transcodeTask.setStatus(TranscodeTaskStatusEnum.FAIL.getKey());
		}
		transcodeTask.setResponseCode(String.valueOf(result));
		transcodeTask.setResponseMsg(message);
		transcodeTask.setResponseTime(new Date());
		transcodeTask.setIsCallback(YesNoEnum.YES.getKey());
		transcodeTaskRepository.save(transcodeTask);
		return true;
	}

	/**
	 * 根据转码任务更新媒资状态
	 * 
	 * @param transcodeTask
	 */
	public void updateMediaStatusByTranscodeTask(TranscodeTask transcodeTask) {
		Integer isCallback = transcodeTask.getIsCallback();
		transcodeTask.setIsCallback(YesNoEnum.NO.getKey());
		transcodeTaskRepository.save(transcodeTask);
		if (isCallback == YesNoEnum.YES.getKey()) {
			if (TranscodeTaskStatusEnum.SUCCESS.getKey() == transcodeTask
					.getStatus()) {
				calculateSuccessTranscodeTask(transcodeTask);

				List<TranscodeTask> preTaskList = transcodeTaskRepository
						.findByPreTaskId(transcodeTask.getId());
				if (preTaskList != null) {
					for (TranscodeTask preTask : preTaskList) {
						preTask.setPreTaskStatus(YesNoEnum.YES.getKey());
						transcodeTaskRepository.save(preTask);
					}
				}
			} else if (TranscodeTaskStatusEnum.FAIL.getKey() == transcodeTask
					.getStatus()) {
				calculateFailTranscodeTask(transcodeTask);
			}
		}
	}

	public void calculateSuccessTranscodeTask(TranscodeTask transcodeTask) {
		TranscodeRequest transcodeRequest = transcodeRequestRepository
				.findOne(transcodeTask.getRequestId());

		long taskSuccess = transcodeTaskRepository.countByRequestIdAndStatus(
				transcodeRequest.getId(),
				TranscodeTaskStatusEnum.SUCCESS.getKey());
		transcodeRequest.setTaskSuccess(taskSuccess);

		long taskFail = transcodeTaskRepository
				.countTaskFailNumByRequestId(transcodeRequest.getId());
		transcodeRequest.setTaskFail(taskFail);

		if (transcodeRequest.getTaskTotal() <= taskSuccess) {
			transcodeRequest.setStatus(TranscodeRequestStatusEnum.SUCCESS
					.getKey());
		}

		transcodeRequestRepository.save(transcodeRequest);

		if (transcodeTask.getMediaFileId() != null) {
			mediaService.updateMediaFileMediaStatus(
					transcodeTask.getMediaFileId(),
					MediaStatusEnum.OK.getKey(),
					transcodeTask.getOutputFilePath(), true);
		}
	}

	public void calculateFailTranscodeTask(TranscodeTask transcodeTask) {
		TranscodeRequest transcodeRequest = transcodeRequestRepository
				.findOne(transcodeTask.getRequestId());

		long taskFail = transcodeTaskRepository
				.countTaskFailNumByRequestId(transcodeRequest.getId());
		transcodeRequest.setTaskFail(taskFail);

		if (transcodeRequest.getTaskTotal() <= taskFail) {
			transcodeRequest
					.setStatus(TranscodeRequestStatusEnum.FAIL.getKey());
		} else {
			transcodeRequest.setStatus(TranscodeRequestStatusEnum.PART_FAIL
					.getKey());
		}

		transcodeRequestRepository.save(transcodeRequest);

		if (transcodeTask.getMediaFileId() != null) {
			mediaService.updateMediaFileMediaStatus(
					transcodeTask.getMediaFileId(),
					MediaStatusEnum.TRANSCODE_FAIL.getKey(), null, true);
		}
	}

	public void resetTranscodeTask(TranscodeTask transcodeTask) {
		transcodeTask.setRequestTimes(0);
		transcodeTask.setStatus(TranscodeTaskStatusEnum.WAIT.getKey());
		transcodeTaskRepository.save(transcodeTask);

		TranscodeRequest transcodeRequest = transcodeRequestRepository
				.findOne(transcodeTask.getRequestId());
		transcodeRequest.setStatus(TranscodeRequestStatusEnum.RUNNING.getKey());

		transcodeRequestRepository.save(transcodeRequest);

		if (transcodeTask.getMediaFileId() != null) {
			mediaService.updateMediaFileMediaStatus(
					transcodeTask.getMediaFileId(),
					MediaStatusEnum.TRANSCODE.getKey(), null, true);
		}
	}

	/**
	 * 复制工单
	 * 
	 * @param transcodeRequest
	 * @param templateId
	 */
	public void copyTranscodeRequest(TranscodeRequest transcodeRequest,
			String templateId) {
		if (transcodeRequest == null) {
			throw new ServiceException("工单不存在!");
		}
		TranscodeRequest transcodeRequestInfo = new TranscodeRequest();
		BeanInfoUtil.bean2bean(transcodeRequest, transcodeRequestInfo,
				TranscodeRequest.METADATA);
		transcodeRequestInfo.setTemplateId(templateId);
		transcodeRequestInfo.setName(transcodeRequest.getName() + " 复制");

		List<TranscodeRequestFile> fileList = new ArrayList<TranscodeRequestFile>();
		List<TranscodeRequestFile> sourceFileList = transcodeRequest
				.getFileList();
		for (TranscodeRequestFile sourceFile : sourceFileList) {
			TranscodeRequestFile file = new TranscodeRequestFile();
			BeanInfoUtil.bean2bean(sourceFile, file,
					TranscodeRequestFile.METADATA);
			fileList.add(file);
		}

		transcodeRequestInfo.setFileList(fileList);
		transcodeRequestRepository.save(transcodeRequestInfo);
	}

	/**
	 * 更新或执行工单
	 * 
	 * @param transcodeRequest
	 * @param produce
	 */
	public synchronized void updateTranscodeRequest(
			TranscodeRequest transcodeRequest, boolean produce) {
		if (transcodeRequest == null) {
			throw new ServiceException("工单不存在!");
		}
		if (transcodeRequest.getStatus() != TranscodeRequestStatusEnum.EDIT
				.getKey()) {
			return;
		}
		if (transcodeRequest.getGenTask() == GenTaskModeStatusEnum.LOCAL
				.getKey()) {
			// 本地已转码，判断路径是否包括中文字符
			for (TranscodeRequestFile newFile : transcodeRequest.getFileList()) {
				if (isContainChinese(newFile.getFilePath())) {
					throw new ServiceException("媒体文件路径["
							+ newFile.getFilePath() + "]包括中文字符!");
				}
			}
		}

		TranscodeRequest transcodeRequestInfo = null;
		if (transcodeRequest.getId() != null) {
			transcodeRequestInfo = transcodeRequestRepository
					.findOne(transcodeRequest.getId());
		}
		if (transcodeRequestInfo != null) {
			BeanInfoUtil.bean2bean(transcodeRequest, transcodeRequestInfo,
					TranscodeRequest.METADATA);

			List<TranscodeRequestFile> oldFiles = transcodeRequestInfo
					.getFileList();
			for (TranscodeRequestFile oldFile : oldFiles) {
				boolean exist = false;
				for (TranscodeRequestFile newFile : transcodeRequest
						.getFileList()) {
					if (newFile.getId() != null
							&& oldFile.getId() != null
							&& newFile.getId().longValue() == oldFile.getId()
									.longValue()) {
						exist = true;
						break;
					}
				}
				if (!exist) {
					transcodeRequestFileRepository.delete(oldFile);
				}
			}
			transcodeRequestInfo.setFileList(transcodeRequest.getFileList());
		} else {
			transcodeRequestInfo = transcodeRequest;
		}
		transcodeRequestRepository.save(transcodeRequestInfo);

		if (produce) {
			if (transcodeRequestInfo.getFileList() == null
					|| transcodeRequestInfo.getFileList().size() <= 0) {
				throw new ServiceException("工单["
						+ transcodeRequestInfo.getName() + "],请增加媒体文件!");
			}
			if (StringUtils.isEmpty(transcodeRequestInfo.getTemplateId())) {
				throw new ServiceException("工单["
						+ transcodeRequestInfo.getName() + "],请选择码率模板!");
			}
			if (transcodeRequestInfo.getType() == TranscodeRequestTypeEnum.BATCH_MOVIE
					.getKey()) {
				// 批量单集工单,每个文件为一个单集
				for (TranscodeRequestFile file : transcodeRequestInfo
						.getFileList()) {
					file.setMediaFileType(MediaFileTypeEnum.DEFAULT.getKey());
					List<TranscodeRequestFile> fileList = new ArrayList<TranscodeRequestFile>();
					fileList.add(file);
					produceTranscodeRequest(transcodeRequestInfo, fileList,
							file.getMediaId(), file.getMediaName(),
							file.getMediaFilename());
				}
			} else {
				produceTranscodeRequest(transcodeRequestInfo,
						transcodeRequestInfo.getFileList(),
						transcodeRequestInfo.getMediaId(),
						transcodeRequestInfo.getMediaName(),
						transcodeRequestInfo.getMediaFilename());
			}

			long taskTotal = transcodeTaskRepository
					.countByRequestId(transcodeRequestInfo.getId());
			transcodeRequestInfo.setTaskTotal(taskTotal);
			if (taskTotal <= 0) {
				transcodeRequestInfo
						.setStatus(TranscodeRequestStatusEnum.SUCCESS.getKey());
			} else {
				transcodeRequestInfo
						.setStatus(TranscodeRequestStatusEnum.RUNNING.getKey());
			}
			transcodeRequestRepository.save(transcodeRequestInfo);
		}
	}

	private void produceTranscodeRequest(TranscodeRequest transcodeRequest,
			List<TranscodeRequestFile> fileList, Long mediaId,
			String mediaName, String mediaFilename) {
		TranscodeRequestTypeEnum typeEnum = TranscodeRequestTypeEnum
				.getEnumByKey(transcodeRequest.getType());
		switch (typeEnum) {
		case MOVIE:
		case BATCH_MOVIE:
			produceMovie(transcodeRequest, fileList, mediaId, mediaName,
					mediaFilename);
			return;
		case TV:
			produceTv(transcodeRequest, mediaId, mediaName, mediaFilename);
			return;
		default:
			return;
		}
	}

	private void produceMovie(TranscodeRequest transcodeRequest,
			List<TranscodeRequestFile> fileList, Long mediaId,
			String mediaName, String mediaFilename) {
		// 1.生成节目
		Program program = null;
		if (mediaId != null) {
			program = programRepository.findOne(mediaId);
		}
		if (program == null) {
			program = new Program();

			program.setType(ProgramTypeEnum.MOVIE.getKey());
			program.setName(mediaName);
			program.setTitle(getTitleByMediaName(mediaName));
			program.setContentType(transcodeRequest.getContentType());
			program.setEpisodeIndex(1);
			program.setTag(transcodeRequest.getTag());
			program.setInternalTag(transcodeRequest.getInternalTag());
			program.setCpId(transcodeRequest.getCpId());

			program.setSource(SourceEnum.TRANSCODE.getKey());

			mediaService.saveProgram(program);

			if (transcodeRequest.getType() == TranscodeRequestTypeEnum.BATCH_MOVIE
					.getKey()) {
				// 每个文件为一个单集
				for (TranscodeRequestFile file : fileList) {
					file.setNewMedia(YesNoEnum.YES.getKey());
					file.setMediaId(program.getId());
				}
			} else {
				transcodeRequest.setNewMedia(YesNoEnum.YES.getKey());
				transcodeRequest.setMediaId(program.getId());
			}
		}
		program.setFilename(StringUtils.trimToEmpty(mediaFilename));
		program.setStoragePath(getStoragePath(transcodeRequest, mediaName,
				mediaFilename));

		// 2.生成媒体内容,每个文件生成对应的MediaFile
		String[] templateIdArr = transcodeRequest.getTemplateId().split(",");
		for (String templateIdS : templateIdArr) {
			Long templateId = Long.valueOf(templateIdS);
			MediaTemplate mediaTemplate = mediaTemplateRepository
					.findOne(templateId);
			for (TranscodeRequestFile file : fileList) {
				produceMediaFile(transcodeRequest, file, mediaTemplate, program);
			}
		}
		// 更新节目的媒资状态
		mediaService.saveProgramAndMediaStatus(program);
	}

	private void produceMediaFile(TranscodeRequest transcodeRequest,
			TranscodeRequestFile file, MediaTemplate mediaTemplate,
			Program program) {
		if (file.getEpisodeIndex() != null
				&& program.getEpisodeIndex() != null
				&& file.getEpisodeIndex().intValue() != program
						.getEpisodeIndex().intValue()) {
			return;
		}
		// 1.生成媒体内容
		MediaFile mediaFile = null;
		if (file.getMediaFileType() == MediaFileTypeEnum.DEFAULT.getKey()
				&& mediaTemplate.getId() != -1) {
			List<MediaFile> mediaFileList = mediaFileRepository
					.findByProgramIdAndTemplateIdAndType(program.getId(),
							mediaTemplate.getId(), file.getMediaFileType());
			if (mediaFileList != null && mediaFileList.size() > 0) {
				mediaFile = mediaFileList.get(0);
			}
			if (mediaFile != null
					&& transcodeRequest.getCoverFile() != YesNoEnum.YES
							.getKey()) {
				throw new ServiceException("工单[" + transcodeRequest.getName()
						+ "],格式为[" + mediaTemplate.getTitle() + "]的节目["
						+ program.getTitle() + "]已存在正片!");
			}
		}
		if (mediaFile == null) {
			mediaFile = new MediaFile();

			mediaFile.setSeriesId(program.getSeriesId());
			mediaFile.setEpisodeIndex(program.getEpisodeIndex());
			mediaFile.setProgramId(program.getId());

			mediaFile.setType(file.getMediaFileType());
			mediaFile.setTemplateId(mediaTemplate.getId());

			mediaFile.setSource(SourceEnum.TRANSCODE.getKey());
			mediaFile.setMediaStatus(MediaStatusEnum.TRANSCODE.getKey());

			mediaService.saveMediaFile(mediaFile);
		}

		// 2.生成转码任务
		if (transcodeRequest.getGenTask() == GenTaskModeStatusEnum.DEFAULT
				.getKey()) {
			// 正常转码
			TranscodeTask transcodeTask = produceTranscodeTask(
					transcodeRequest, file, mediaTemplate, program, mediaFile);
			transcodeTaskRepository.save(transcodeTask);

			if (transcodeRequest.getNeedSnapshot() == NeedSnapshotEnum.YES
					.getKey()) {
				TranscodeTask imageTask = produceImageTask(transcodeRequest,
						file, mediaTemplate, program, mediaFile);
				imageTask.setPreTaskId(transcodeTask.getId());
				imageTask.setPreTaskStatus(YesNoEnum.NO.getKey());
				imageTask.setTimePoints(transcodeRequest.getTimePoints());
				transcodeTaskRepository.save(imageTask);
			}
		} else if (transcodeRequest.getGenTask() == GenTaskModeStatusEnum.LOCAL
				.getKey()) {
			// 本地已转码，标记转码成功
			mediaFile.setFilePath(file.getFilePath());
			mediaFile.setMediaStatus(MediaStatusEnum.OK.getKey());
			mediaService.saveMediaFile(mediaFile);
		}
	}

	private void produceTv(TranscodeRequest transcodeRequest, Long mediaId,
			String mediaName, String mediaFilename) {
		// 1.生成剧头
		Series series = null;
		if (mediaId != null) {
			series = seriesRepository.findOne(mediaId);
		}
		if (series == null) {
			series = new Series();

			series.setName(mediaName);
			series.setTitle(getTitleByMediaName(mediaName));
			series.setContentType(transcodeRequest.getContentType());
			series.setEpisodeTotal(transcodeRequest.getEpisodeTotal());
			series.setTag(transcodeRequest.getTag());
			series.setInternalTag(transcodeRequest.getInternalTag());
			series.setCpId(transcodeRequest.getCpId());

			series.setSource(SourceEnum.TRANSCODE.getKey());

			mediaService.saveSeries(series);

			transcodeRequest.setNewMedia(YesNoEnum.YES.getKey());
			transcodeRequest.setMediaId(series.getId());
		}

		series.setFilename(StringUtils.trimToEmpty(mediaFilename));
		series.setStoragePath(getStoragePath(transcodeRequest, mediaName,
				mediaFilename));

		// 2.生成节目和媒体内容
		String[] templateIdArr = transcodeRequest.getTemplateId().split(",");
		for (String templateIdS : templateIdArr) {
			Long templateId = Long.valueOf(templateIdS);
			MediaTemplate mediaTemplate = mediaTemplateRepository
					.findOne(templateId);
			produceProgram(transcodeRequest, mediaName, mediaFilename,
					mediaTemplate, series);
		}
		// 更新剧头的媒资状态
		mediaService.saveSeriesAndMediaStatus(series);
	}

	private void produceProgram(TranscodeRequest transcodeRequest,
			String mediaName, String mediaFilename,
			MediaTemplate mediaTemplate, Series series) {
		// 1.多个文件生成多个节目
		for (TranscodeRequestFile file : transcodeRequest.getFileList()) {
			Program program = programRepository
					.findOneBySeriesIdAndEpisodeIndex(series.getId(),
							file.getEpisodeIndex());
			if (program == null) {
				program = new Program();

				program.setType(ProgramTypeEnum.TV.getKey());
				program.setSeries(series);
				program.setSeriesId(series.getId());

				program.setName(series.getTitle()
						+ " "
						+ String.format(
								GenProgramNameRuleEnum.EPISODE.getValue(),
								file.getEpisodeIndex()));
				if (transcodeRequest.getGenProgramNameRule() == GenProgramNameRuleEnum.FILENAME
						.getKey()) {
					program.setTitle(getTitleByFilePath(file.getFilePath()));
				} else if (transcodeRequest.getGenProgramNameRule() == GenProgramNameRuleEnum.EPISODE
						.getKey()) {
					program.setTitle(String.format(
							GenProgramNameRuleEnum.EPISODE.getValue(),
							file.getEpisodeIndex()));
				} else if (transcodeRequest.getGenProgramNameRule() == GenProgramNameRuleEnum.COURSE
						.getKey()) {
					program.setTitle(String.format(
							GenProgramNameRuleEnum.COURSE.getValue(),
							file.getEpisodeIndex()));
				} else if (transcodeRequest.getGenProgramNameRule() == GenProgramNameRuleEnum.SITE
						.getKey()) {
					program.setTitle(String.format(
							GenProgramNameRuleEnum.SITE.getValue(),
							file.getEpisodeIndex()));
				} else if (transcodeRequest.getGenProgramNameRule() == GenProgramNameRuleEnum.SECTION
						.getKey()) {
					program.setTitle(String.format(
							GenProgramNameRuleEnum.SECTION.getValue(),
							file.getEpisodeIndex()));
				} else {
					program.setTitle(String.format(
							GenProgramNameRuleEnum.EPISODE.getValue(),
							file.getEpisodeIndex()));
				}
				program.setContentType(transcodeRequest.getContentType());
				program.setEpisodeIndex(file.getEpisodeIndex());
				program.setTag(transcodeRequest.getTag());
				program.setInternalTag(transcodeRequest.getInternalTag());
				program.setCpId(transcodeRequest.getCpId());

				program.setSource(SourceEnum.TRANSCODE.getKey());

				mediaService.saveProgram(program);
			}

			program.setFilename(StringUtils.trimToEmpty(mediaFilename));
			program.setStoragePath(getStoragePath(transcodeRequest, mediaName,
					mediaFilename));

			// 2.生成媒体内容
			produceMediaFile(transcodeRequest, file, mediaTemplate, program);

			// 更新节目的媒资状态
			mediaService.saveProgramAndMediaStatus(program);
		}
	}

	private TranscodeTask produceTranscodeTask(
			TranscodeRequest transcodeRequest, TranscodeRequestFile file,
			MediaTemplate mediaTemplate, Program program, MediaFile mediaFile) {
		TranscodeTask transcodeTask = new TranscodeTask();
		transcodeTask.setCpId(transcodeRequest.getCpId());

		transcodeTask.setType(TranscodeTaskTypeEnum.TRANSCODE.getKey());

		transcodeTask.setRequestId(transcodeRequest.getId());
		transcodeTask.setRequestFileId(file.getId());

		transcodeTask.setProgramId(program.getId());
		transcodeTask.setMediaFileId(mediaFile.getId());

		transcodeTask.setTemplateId(mediaTemplate.getId());
		transcodeTask.setProfile(mediaTemplate.getExternalCode());

		transcodeTask.setInputFilePath(file.getFilePath());
		transcodeTask.setInputSubtitleFilePath(file.getSubtitleFilePath());

		if (YesNoEnum.YES.getKey() == transcodeRequest.getCoverFile()
				&& StringUtils.isNotEmpty(mediaFile.getFilePath())) {
			transcodeTask.setOutputFilePath(mediaFile.getFilePath());
		} else {
			String storagePath = program.getStoragePath();
			String mediaFilename = program.getFilename();
			String suffix = "ts";
			if (StringUtils.isNotEmpty(mediaTemplate.getvFormat())) {
				suffix = mediaTemplate.getvFormat().toLowerCase();
			}
			String rootPath = PathUtils.joinPath(
					getTranscodeOutputPath(transcodeTask.getCpId()),
					mediaTemplate.getCode() + "/" + storagePath);
			String filename = StringUtils.trimToEmpty(mediaFilename) + "_"
					+ program.getEpisodeIndex() + "_" + mediaFile.getId() + "_"
					+ mediaTemplate.getCode() + "." + suffix;
			transcodeTask.setOutputFilePath(rootPath + "/" + filename);
		}

		transcodeTask.setPriority(transcodeRequest.getPriority());
		transcodeTask.setStatus(TranscodeTaskStatusEnum.WAIT.getKey());

		genTranscodeTaskName(transcodeTask, file, program);
		return transcodeTask;
	}

	private TranscodeTask produceImageTask(TranscodeRequest transcodeRequest,
			TranscodeRequestFile file, MediaTemplate mediaTemplate,
			Program program, MediaFile mediaFile) {
		TranscodeTask transcodeTask = new TranscodeTask();
		transcodeTask.setCpId(transcodeRequest.getCpId());

		transcodeTask.setType(TranscodeTaskTypeEnum.IMAGE.getKey());

		transcodeTask.setRequestId(transcodeRequest.getId());
		transcodeTask.setRequestFileId(file.getId());

		transcodeTask.setProgramId(program.getId());
		transcodeTask.setMediaFileId(mediaFile.getId());

		transcodeTask.setTemplateId(mediaTemplate.getId());
		transcodeTask.setProfile(mediaTemplate.getExternalCode());
		transcodeTask.setTimePoints(transcodeRequest.getTimePoints());

		transcodeTask.setInputFilePath(file.getFilePath());
		transcodeTask.setInputSubtitleFilePath(file.getSubtitleFilePath());

		String storagePath = program.getStoragePath();
		String mediaFilename = program.getFilename();
		String suffix = "jpg";
		String rootPath = PathUtils.joinPath(
				getTranscodeOutputPath(transcodeTask.getCpId()),
				mediaTemplate.getCode() + "/" + storagePath);
		String filename = StringUtils.trimToEmpty(mediaFilename) + "_"
				+ program.getEpisodeIndex() + "_" + mediaFile.getId() + "_"
				+ mediaTemplate.getCode() + "." + suffix;
		transcodeTask.setOutputFilePath(rootPath + "/" + filename);

		transcodeTask.setPriority(transcodeRequest.getPriority());
		transcodeTask.setStatus(TranscodeTaskStatusEnum.WAIT.getKey());

		genTranscodeTaskName(transcodeTask, file, program);
		return transcodeTask;
	}

	private String getTranscodeOutputPath(String cpId) {
		if (StringUtils.isNotEmpty(cpId)) {
			return transcodeOutputPath + "/" + StringUtils.trimToEmpty(cpId);
		}
		return transcodeOutputPath;
	}

	private void genTranscodeTaskName(TranscodeTask transcodeTask,
			TranscodeRequestFile file, Program program) {
		MediaFileTypeEnum mediaFileTypeEnum = MediaFileTypeEnum
				.getEnumByKey(file.getMediaFileType());
		TranscodeTaskTypeEnum transcodeTaskTypeEnum = TranscodeTaskTypeEnum
				.getEnumByKey(transcodeTask.getType());
		transcodeTask.setName(program.getName() + " "
				+ mediaFileTypeEnum.getValue()
				+ transcodeTaskTypeEnum.getValue());
	}

	private String getStoragePath(TranscodeRequest transcodeRequest,
			String mediaName, String mediaFilename) {
		String storagePath = DateFormatUtils.format(transcodeRequest
				.getCreateTime() != null ? transcodeRequest.getCreateTime()
				: new Date(), "yyyyMM")
				+ "/" + StringUtils.trimToEmpty(mediaFilename);
		return storagePath;
	}

	public static String getTitleByMediaName(String source) {
		String str = source;
		if (str.indexOf("_") >= 0) {
			return str.substring(0, str.indexOf("_"));
		}
		if (str.indexOf(".") >= 0) {
			return str = StringUtils.substringBeforeLast(str, ".");
		}
		return str;
	}

	public static String getTitleByFilePath(String source) {
		String str = source;
		if (str.indexOf("/") >= 0) {
			str = StringUtils.substringAfterLast(str, "/");
		}
		if (str.indexOf("_") >= 0) {
			return str.substring(0, str.indexOf("_"));
		}
		if (str.indexOf(".") >= 0) {
			return str = StringUtils.substringBeforeLast(str, ".");
		}
		return str;
	}

	/**
	 * 判断字符串是否包含中文字符
	 * 
	 * @param source
	 * @return
	 */
	public static boolean isContainChinese(String source) {
		if (StringUtils.isEmpty(source)) {
			return false;
		}
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(source);
		if (m.find()) {
			return true;
		}
		return false;
	}

}
