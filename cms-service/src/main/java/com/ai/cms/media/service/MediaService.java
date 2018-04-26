package com.ai.cms.media.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.AppGlobal;
import com.ai.cms.config.entity.MediaTemplate;
import com.ai.cms.config.repository.MediaTemplateRepository;
import com.ai.cms.injection.enums.PlayCodeStatusEnum;
import com.ai.cms.media.entity.MediaFile;
import com.ai.cms.media.entity.MediaImage;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.Series;
import com.ai.cms.media.repository.MediaFileRepository;
import com.ai.cms.media.repository.MediaImageRepository;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.cms.media.repository.SeriesRepository;
import com.ai.common.bean.OperationObject;
import com.ai.common.enums.AuditStatusEnum;
import com.ai.common.enums.MediaFileTypeEnum;
import com.ai.common.enums.MediaStatusEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;

@Service
@Transactional(value = "slaveTransactionManager", readOnly = true)
public class MediaService extends AbstractService<Series, Long> {

	@Autowired
	private MediaTemplateRepository mediaTemplateRepository;

	@Autowired
	private SeriesRepository seriesRepository;

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private MediaFileRepository mediaFileRepository;
	
	@Autowired
	private MediaImageRepository mediaImageRepository;

	@Override
	public AbstractRepository<Series, Long> getRepository() {
		return seriesRepository;
	}

	public Series findSeriesById(Long id) {
		return seriesRepository.findOne(id);
	}

	public Program findProgramById(Long id) {
		return programRepository.findOne(id);
	}

	public MediaFile findMediaFileById(Long id) {
		return mediaFileRepository.findOne(id);
	}

	public Series findSeriesByCloudId(String cloudId) {
		List<Series> list = seriesRepository.findByCloudId(cloudId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Program findProgramByCloudId(String cloudId) {
		List<Program> list = programRepository.findByCloudId(cloudId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public MediaFile findMediaFileByCloudId(String cloudId) {
		List<MediaFile> list = mediaFileRepository.findByCloudId(cloudId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public MediaImage findMediaImageByCloudId(String cloudId) {
		List<MediaImage> list = mediaImageRepository.findByCloudId(cloudId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Series findSeriesByCloudCode(String cloudCode) {
		List<Series> list = seriesRepository.findByCloudCode(cloudCode);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Program findProgramByCloudCode(String cloudCode) {
		List<Program> list = programRepository.findByCloudCode(cloudCode);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public MediaFile findMediaFileByCloudCode(String cloudCode) {
		List<MediaFile> list = mediaFileRepository.findByCloudCode(cloudCode);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public MediaImage findMediaImageByCloudCode(String cloudCode) {
		List<MediaImage> list = mediaImageRepository.findByCloudCode(cloudCode);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void saveSeriesAndAuditStatus(Series series) {
		setSeriesImageStatus(series);
		seriesRepository.save(series);
		if (series.getAuditStatus() == AuditStatusEnum.AUDIT_FIRST_PASS
				.getKey()) {
			programRepository.updateAuditStatusBySeriesId(series.getId(),
					series.getAuditStatus(), series.getAuditUser(),
					series.getAuditTime(), series.getStorageTime());
		} else {
			programRepository.updateAuditStatusBySeriesId(series.getId(),
					series.getAuditStatus(), series.getAuditUser(),
					series.getAuditTime());
		}
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void updateCpCodeBySeriesId(Series series) {
		programRepository
				.updateCpCodeBySeriesId(series.getId(), series.getCpCode());
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void saveSeries(Series series) {
		setSeriesImageStatus(series);
		seriesRepository.save(series);
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void saveSeriesAndMediaStatus(Series series) {
		setSeriesImageStatus(series);
		seriesRepository.save(series);
		updateSeriesMediaStatus(series);
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void saveProgram(Program program) {
		setProgramImageStatus(program);
		programRepository.save(program);
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void saveProgramAndMediaStatus(Program program) {
		saveProgramAndMediaStatus(program, false);
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void saveProgramAndMediaStatus(Program program, boolean deliver) {
		setProgramImageStatus(program);
		programRepository.save(program);
		updateProgramMediaStatus(program, deliver);
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void saveMediaFile(MediaFile mediaFile) {
		setMediaFileSpec(mediaFile, mediaFile.getTemplateId());
		mediaFileRepository.save(mediaFile);
	}
	
	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void saveMediaImage(MediaImage mediaImage) {
		mediaImageRepository.save(mediaImage);
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void saveMediaFileAndMediaStatus(MediaFile mediaFile,
			String newFilePath, Long newTemplateId) {
		setMediaFileSpec(mediaFile, mediaFile.getTemplateId());
		mediaFileRepository.save(mediaFile);
		if (StringUtils.isNotEmpty(newFilePath)
				|| mediaFile.getPlayCodeStatus() == PlayCodeStatusEnum.INPUT
						.getKey()) {
			updateMediaFileMediaStatus(mediaFile, MediaStatusEnum.OK.getKey(),
					newFilePath, newTemplateId, true);
		} else if (mediaFile.getMediaStatus() == MediaStatusEnum.OK.getKey()) {
			updateMediaFileMediaStatus(mediaFile,
					MediaStatusEnum.MISS_VIDEO.getKey(), newFilePath,
					newTemplateId, true);
		} else if (newTemplateId != null) {
			updateMediaFileMediaStatus(mediaFile, null, newFilePath,
					newTemplateId, true);
		}
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void deleteSeries(Series series) {
		if (series != null) {
			mediaImageRepository.deleteBySeriesId(series.getId());
			mediaFileRepository.deleteBySeriesId(series.getId());
			programRepository.deleteBySeriesId(series.getId());
			seriesRepository.delete(series);
		}
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void deleteProgram(Program program) {
		if (program != null) {
			mediaImageRepository.deleteByProgramId(program.getId());
			mediaFileRepository.deleteByProgramId(program.getId());
			programRepository.delete(program);
			// 更新剧头的媒资状态
			updateSeriesMediaStatus(program.getSeriesId());
		}
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void deleteMediaFile(MediaFile mediaFile) {
		if (mediaFile != null) {
			mediaFileRepository.delete(mediaFile);
			// 更新节目的媒资状态
			updateProgramMediaStatus(mediaFile.getProgramId(), true);
		}
	}
	
	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void deleteMediaImage(MediaImage mediaImage) {
		if (mediaImage != null) {
			mediaImageRepository.delete(mediaImage);
		}
	}

	/**
	 * 设置节目的海报状态
	 * 
	 * @param program
	 * @return
	 */
	private void setProgramImageStatus(Program program) {
		int newMediaStatus = program.getMediaStatus();
		if (StringUtils.isNotEmpty(program.getImage1())
				|| StringUtils.isNotEmpty(program.getImage2())
				|| StringUtils.isNotEmpty(program.getImage3())
				|| StringUtils.isNotEmpty(program.getImage4())) {
			newMediaStatus = AppGlobal.setMediaStatusClose(newMediaStatus,
					MediaStatusEnum.MISS_IMAGE.getKey());
		} else if (StringUtils.isEmpty(program.getImage1())
				&& StringUtils.isEmpty(program.getImage2())
				&& StringUtils.isEmpty(program.getImage3())
				&& StringUtils.isEmpty(program.getImage4())) {
			newMediaStatus = AppGlobal.setMediaStatusOpen(newMediaStatus,
					MediaStatusEnum.MISS_IMAGE.getKey());
		}
		program.setMediaStatus(newMediaStatus);
	}

	/**
	 * 设置剧头的海报状态
	 * 
	 * @param series
	 * @return
	 */
	private void setSeriesImageStatus(Series series) {
		int newMediaStatus = series.getMediaStatus();
		if (StringUtils.isNotEmpty(series.getImage1())
				|| StringUtils.isNotEmpty(series.getImage2())
				|| StringUtils.isNotEmpty(series.getImage3())
				|| StringUtils.isNotEmpty(series.getImage4())) {
			newMediaStatus = AppGlobal.setMediaStatusClose(newMediaStatus,
					MediaStatusEnum.MISS_IMAGE.getKey());
		} else if (StringUtils.isEmpty(series.getImage1())
				&& StringUtils.isEmpty(series.getImage2())
				&& StringUtils.isEmpty(series.getImage3())
				&& StringUtils.isEmpty(series.getImage4())) {
			newMediaStatus = AppGlobal.setMediaStatusOpen(newMediaStatus,
					MediaStatusEnum.MISS_IMAGE.getKey());
		}
		series.setMediaStatus(newMediaStatus);
	}

	/**
	 * 计算的媒资状态
	 * 
	 * @param oldMediaStatus
	 * @param mediaFileList
	 * @return
	 */
	private int calculateMediaStatus(int oldMediaStatus,
			List<MediaFile> mediaFileList) {
		int newMediaStatus = oldMediaStatus;
		long totalNum = mediaFileList.size();
		long missVideoNum = 0;
		long waitNumTranscode = 0;
		long failNumTranscode = 0;
		long waitNumDownload = 0;
		long failNumDownload = 0;
		for (MediaFile mediaFile : mediaFileList) {
			if (mediaFile.getMediaStatus() == MediaStatusEnum.MISS_VIDEO
					.getKey()) {
				missVideoNum++;
			} else if (mediaFile.getMediaStatus() == MediaStatusEnum.TRANSCODE
					.getKey()) {
				waitNumTranscode++;
			} else if (mediaFile.getMediaStatus() == MediaStatusEnum.TRANSCODE_FAIL
					.getKey()) {
				failNumTranscode++;
			} else if (mediaFile.getMediaStatus() == MediaStatusEnum.DOWNLOAD
					.getKey()) {
				waitNumDownload++;
			} else if (mediaFile.getMediaStatus() == MediaStatusEnum.DOWNLOAD_FAIL
					.getKey()) {
				failNumDownload++;
			}
		}

		if (totalNum == 0 || missVideoNum > 0) {// 缺视频
			newMediaStatus = AppGlobal.setMediaStatusOpen(newMediaStatus,
					MediaStatusEnum.MISS_VIDEO.getKey());
		} else {
			newMediaStatus = AppGlobal.setMediaStatusClose(newMediaStatus,
					MediaStatusEnum.MISS_VIDEO.getKey());
		}

		if (waitNumTranscode > 0) {// 正在转码
			newMediaStatus = AppGlobal.setMediaStatusOpen(newMediaStatus,
					MediaStatusEnum.TRANSCODE.getKey());
		} else {
			newMediaStatus = AppGlobal.setMediaStatusClose(newMediaStatus,
					MediaStatusEnum.TRANSCODE.getKey());
		}

		if (failNumTranscode > 0 && failNumTranscode >= totalNum) {// 转码失败
			newMediaStatus = AppGlobal.setMediaStatusOpen(newMediaStatus,
					MediaStatusEnum.TRANSCODE_FAIL.getKey());
			newMediaStatus = AppGlobal.setMediaStatusClose(newMediaStatus,
					MediaStatusEnum.TRANSCODE_PART_FAIL.getKey());
		} else if (failNumTranscode > 0) {// 部分转码失败
			newMediaStatus = AppGlobal.setMediaStatusOpen(newMediaStatus,
					MediaStatusEnum.TRANSCODE_PART_FAIL.getKey());
			newMediaStatus = AppGlobal.setMediaStatusClose(newMediaStatus,
					MediaStatusEnum.TRANSCODE_FAIL.getKey());
		} else {
			newMediaStatus = AppGlobal.setMediaStatusClose(newMediaStatus,
					MediaStatusEnum.TRANSCODE_FAIL.getKey(),
					MediaStatusEnum.TRANSCODE_PART_FAIL.getKey());
		}

		if (waitNumDownload > 0) {// 正在转码
			newMediaStatus = AppGlobal.setMediaStatusOpen(newMediaStatus,
					MediaStatusEnum.DOWNLOAD.getKey());
		} else {
			newMediaStatus = AppGlobal.setMediaStatusClose(newMediaStatus,
					MediaStatusEnum.DOWNLOAD.getKey());
		}

		if (failNumDownload > 0 && failNumDownload >= totalNum) {// 转码失败
			newMediaStatus = AppGlobal.setMediaStatusOpen(newMediaStatus,
					MediaStatusEnum.DOWNLOAD_FAIL.getKey());
			newMediaStatus = AppGlobal.setMediaStatusClose(newMediaStatus,
					MediaStatusEnum.DOWNLOAD_PART_FAIL.getKey());
		} else if (failNumDownload > 0) {// 部分转码失败
			newMediaStatus = AppGlobal.setMediaStatusOpen(newMediaStatus,
					MediaStatusEnum.DOWNLOAD_PART_FAIL.getKey());
			newMediaStatus = AppGlobal.setMediaStatusClose(newMediaStatus,
					MediaStatusEnum.DOWNLOAD_FAIL.getKey());
		} else {
			newMediaStatus = AppGlobal.setMediaStatusClose(newMediaStatus,
					MediaStatusEnum.DOWNLOAD_FAIL.getKey(),
					MediaStatusEnum.DOWNLOAD_PART_FAIL.getKey());
		}
		return newMediaStatus;
	}

	/**
	 * 更新剧头的媒资状态
	 * 
	 * @param seriesId
	 */
	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void updateSeriesMediaStatus(Long seriesId) {
		if (seriesId == null) {
			return;
		}
		Series series = seriesRepository.findOne(seriesId);
		updateSeriesMediaStatus(series);
	}

	/**
	 * 更新剧头的媒资状态
	 * 
	 * @param series
	 */
	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void updateSeriesMediaStatus(Series series) {
		if (series == null) {
			return;
		}
		boolean updateFlag = false;
		int oldMediaStatus = series.getMediaStatus();
		List<MediaFile> mediaFileList = mediaFileRepository
				.findBySeriesId(series.getId());
		int newMediaStatus = calculateMediaStatus(oldMediaStatus, mediaFileList);
		if (newMediaStatus != oldMediaStatus) {
			series.setMediaStatus(newMediaStatus);
			updateFlag = true;
		}
		String oldTemplateId = series.getTemplateId();
		String newTemplateId = getSeriesTemplateId(series.getId());
		if (!StringUtils.trimToEmpty(oldTemplateId).equals(
				StringUtils.trimToEmpty(newTemplateId))) {
			series.setTemplateId(newTemplateId);
			updateFlag = true;
		}
		if (updateFlag) {
			seriesRepository.save(series);
		}
	}

	/**
	 * 更新节目的媒资状态
	 * 
	 * @param programId
	 */
	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void updateProgramMediaStatus(Long programId, boolean deliver) {
		if (programId == null) {
			return;
		}
		Program program = programRepository.findOne(programId);
		updateProgramMediaStatus(program, deliver);
	}

	/**
	 * 更新节目的媒资状态
	 * 
	 * @param program
	 */
	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void updateProgramMediaStatus(Program program, boolean deliver) {
		if (program == null) {
			return;
		}
		boolean updateFlag = false;
		int oldMediaStatus = program.getMediaStatus();
		List<MediaFile> mediaFileList = mediaFileRepository
				.findByProgramId(program.getId());
		int newMediaStatus = calculateMediaStatus(oldMediaStatus, mediaFileList);
		if (newMediaStatus != oldMediaStatus) {
			program.setMediaStatus(newMediaStatus);
			updateFlag = true;
		}
		String oldTemplateId = program.getTemplateId();
		String newTemplateId = getProgramTemplateId(program.getId());
		if (!StringUtils.trimToEmpty(oldTemplateId).equals(
				StringUtils.trimToEmpty(newTemplateId))) {
			program.setTemplateId(newTemplateId);
			updateFlag = true;
		}
		if (updateFlag) {
			programRepository.save(program);
			if (deliver) {
				// 节目的媒资状态变化了，需要同步更新剧头的媒资状态
				updateSeriesMediaStatus(program.getSeriesId());
			}
		}
	}

	/**
	 * 更新媒体内容的媒资状态
	 * 
	 * @param mediaFileId
	 * @param newMediaStatus
	 * @param newFilePath
	 * @param deliver
	 */
	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void updateMediaFileMediaStatus(Long mediaFileId,
			int newMediaStatus, String newFilePath, boolean deliver) {
		MediaFile mediaFile = mediaFileRepository.findOne(mediaFileId);
		updateMediaFileMediaStatus(mediaFile, newMediaStatus, newFilePath,
				null, deliver);
	}

	/**
	 * 更新媒体内容的媒资状态
	 * 
	 * @param mediaFile
	 * @param newMediaStatus
	 * @param newFilePath
	 * @param newTemplateId
	 * @param deliver
	 */
	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void updateMediaFileMediaStatus(MediaFile mediaFile,
			Integer newMediaStatus, String newFilePath, Long newTemplateId,
			boolean deliver) {
		if (mediaFile == null) {
			return;
		}
		boolean updateFlag = false;

		if (newMediaStatus != null) {
			if (newMediaStatus != mediaFile.getMediaStatus()) {
				mediaFile.setMediaStatus(newMediaStatus);
				// 更新媒体内容的上线状态
				updateMediaFileOnlineStatus(mediaFile, newMediaStatus);
			}
			updateFlag = true;
		}
		if (newFilePath != null) {
			if (!StringUtils.trimToEmpty(newFilePath).equals(
					StringUtils.trimToEmpty(mediaFile.getFilePath()))) {
				mediaFile.setFilePath(newFilePath);
				updateFlag = true;
			}
		}
		if (newTemplateId != null) {
			if (mediaFile.getTemplateId() == null
					|| newTemplateId != mediaFile.getTemplateId()) {
				mediaFile.setTemplateId(newTemplateId);
				updateFlag = true;
			}
		}
		if (updateFlag) {
			mediaFileRepository.save(mediaFile);
			if (deliver) {
				// 媒体内容的媒资状态变化，需要同步更新节目的媒资状态
				updateProgramMediaStatus(mediaFile.getProgramId(), deliver);
			}
		}
	}

	/**
	 * 更新媒体内容的上线状态
	 * 
	 * @param mediaFile
	 * @param newMediaStatus
	 */
	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void updateMediaFileOnlineStatus(MediaFile mediaFile,
			int newMediaStatus) {
		if (newMediaStatus == MediaStatusEnum.OK.getKey()
				&& mediaFile.getStatus() != OnlineStatusEnum.ONLINE.getKey()) {
			// 自动上线
			mediaFile.setStatus(OnlineStatusEnum.ONLINE.getKey());
			mediaFile.setOnlineTime(new Date());
			mediaFile.setOfflineTime(null);
		} else if (newMediaStatus != MediaStatusEnum.OK.getKey()
				&& mediaFile.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			// 自动下线
			mediaFile.setStatus(OnlineStatusEnum.OFFLINE.getKey());
			mediaFile.setOfflineTime(new Date());
		}
	}

	/**
	 * 设置媒件文件的码率相关数据
	 * 
	 * @param mediaFile
	 * @param templateId
	 */
	private void setMediaFileSpec(MediaFile mediaFile, Long templateId) {
		if (templateId == null || templateId <= 0) {
			return;
		}
		MediaTemplate mediaTemplate = mediaTemplateRepository
				.findOne(templateId);
		if (mediaTemplate != null) {
			mediaFile.setTemplateId(mediaTemplate.getId());
			mediaFile.setBitrate(mediaTemplate.getvBitrate());
			mediaFile.setResolution(mediaTemplate.getvResolution());
			mediaFile.setDefinition(mediaTemplate.getDefinition());
			mediaFile.setFormat(mediaTemplate.getvFormat());
			// TS-CBR-H264-8000-1080P-25-MP2-128
			String mediaSpec = mediaTemplate.getvFormat() + "-"
					+ mediaTemplate.getvBitrateMode() + "-"
					+ mediaTemplate.getvCodec() + "-"
					+ mediaTemplate.getvBitrate() + "-"
					+ mediaTemplate.getvResolution() + "-"
					+ mediaTemplate.getvFramerate() + "-"
					+ mediaTemplate.getaCodec() + "-"
					+ mediaTemplate.getaBitrate();
			mediaFile.setMediaSpec(mediaSpec);
		}
	}

	/**
	 * 获取媒体内容的码率模板Id
	 * 
	 * @param mediaFileList
	 * @param filterRepeat
	 * @return
	 */
	private String getTemplateId(List<MediaFile> mediaFileList,
			boolean filterRepeat) {
		List<Long> templateIdList = new ArrayList<Long>();
		for (MediaFile mediaFile : mediaFileList) {
			if (mediaFile.getTemplateId() != null) {
				if (!filterRepeat
						|| !templateIdList.contains(mediaFile.getTemplateId())) {
					templateIdList.add(mediaFile.getTemplateId());
				}
			}
		}
		return StringUtils.join(templateIdList.toArray(), ",");
	}

	/**
	 * 获取节目的码率模板
	 * 
	 * @param programId
	 */
	private String getProgramTemplateId(Long programId) {
		List<MediaFile> mediaFileList = mediaFileRepository.findByProgramId(
				programId, MediaFileTypeEnum.DEFAULT.getKey());
		return getTemplateId(mediaFileList, false);
	}

	/**
	 * 更新节目的码率模板
	 * 
	 * @param programId
	 */
	@Transactional(value = "slaveTransactionManager", readOnly = false)
	private void updateProgramTemplateId(Long programId) {
		String templateId = getProgramTemplateId(programId);
		programRepository.updateTemplateIdById(programId, templateId);
	}

	/**
	 * 更新剧头的码率模板
	 * 
	 * @param seriesId
	 */
	private String getSeriesTemplateId(Long seriesId) {
		List<MediaFile> mediaFileList = mediaFileRepository.findBySeriesId(
				seriesId, MediaFileTypeEnum.DEFAULT.getKey());
		return getTemplateId(mediaFileList, true);
	}

	/**
	 * 更新剧头的码率模板
	 * 
	 * @param seriesId
	 */
	@Transactional(value = "slaveTransactionManager", readOnly = false)
	private void updateSeriesTemplateId(Long seriesId) {
		String templateId = getSeriesTemplateId(seriesId);
		seriesRepository.updateTemplateIdById(seriesId, templateId);
	}

	public OperationObject transformSeriesOperationObject(Series series) {
		if (series == null) {
			return null;
		}
		OperationObject operationObject = new OperationObject();
		operationObject.setId(series.getId());
		operationObject.setName(series.getName());
		return operationObject;
	}

	public List<OperationObject> transformSeriesOperationObject(
			List<Series> seriesList) {
		if (seriesList == null || seriesList.size() <= 0) {
			return null;
		}
		List<OperationObject> list = new ArrayList<OperationObject>();
		for (Series series : seriesList) {
			OperationObject operationObject = new OperationObject();
			operationObject.setId(series.getId());
			operationObject.setName(series.getName());
			list.add(operationObject);
		}
		return list;
	}

	public OperationObject transformProgramOperationObject(Program program) {
		if (program == null) {
			return null;
		}
		OperationObject operationObject = new OperationObject();
		operationObject.setId(program.getId());
		operationObject.setName(program.getName());
		return operationObject;
	}

	public List<OperationObject> transformProgramOperationObject(
			List<Program> programList) {
		if (programList == null || programList.size() <= 0) {
			return null;
		}
		List<OperationObject> list = new ArrayList<OperationObject>();
		for (Program program : programList) {
			OperationObject operationObject = new OperationObject();
			operationObject.setId(program.getId());
			operationObject.setName(program.getName());
			list.add(operationObject);
		}
		return list;
	}

	public OperationObject transformMediaFileOperationObject(MediaFile mediaFile) {
		if (mediaFile == null) {
			return null;
		}
		OperationObject operationObject = new OperationObject();
		operationObject.setId(mediaFile.getId());
		operationObject.setName(mediaFile.getFilePath());
		return operationObject;
	}

	public List<OperationObject> transformMediaFileOperationObject(
			List<MediaFile> mediaFileList) {
		if (mediaFileList == null || mediaFileList.size() <= 0) {
			return null;
		}
		List<OperationObject> list = new ArrayList<OperationObject>();
		for (MediaFile mediaFile : mediaFileList) {
			OperationObject operationObject = new OperationObject();
			operationObject.setId(mediaFile.getId());
			operationObject.setName(mediaFile.getFilePath());
			list.add(operationObject);
		}
		return list;
	}
}