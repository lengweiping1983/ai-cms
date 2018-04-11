package com.ai.cms.media.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ai.CloudStorage;
import com.ai.cms.injection.enums.PlayCodeStatusEnum;
import com.ai.cms.media.bean.MediaFileExportLog;
import com.ai.cms.media.entity.MediaFile;
import com.ai.cms.media.entity.MediaImport;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.repository.MediaFileRepository;
import com.ai.cms.media.repository.MediaImportRepository;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.common.enums.ContentTypeEnum;
import com.ai.common.enums.MediaFileTypeEnum;
import com.ai.common.exception.ServiceException;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;

@Service
@Transactional(value = "slaveTransactionManager", readOnly = false)
public class MediaFileImportService extends AbstractService<MediaImport, Long> {
	private static final Log logger = LogFactory
			.getLog(MediaFileImportService.class);

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private MediaFileRepository mediaFileRepository;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private MediaImportRepository mediaImportRepository;

	@Override
	public AbstractRepository<MediaImport, Long> getRepository() {
		return mediaImportRepository;
	}

	public synchronized void importMediaFile(MediaImport mediaImport,
			List<MediaFileExportLog> logList, int startRow) {
		if (logList == null || logList.size() <= 1) {
			throw new ServiceException("数据为空!");
		}

		logger.info("importMediaFile totalNum=" + logList.size() + " begin...");
		for (int i = 0; i < logList.size(); i++) {
			int row = startRow + i + 1;
			MediaFileExportLog log = logList.get(i);

			if (log.getId() == null && StringUtils.isEmpty(log.getName())) {
				continue;
			}

			if (StringUtils.isNotEmpty(log.getEpisodeIndex())) {
				try {
					Integer.valueOf(log.getEpisodeIndex());
				} catch (Exception e) {
					throw new ServiceException("第" + row + "行，第几集["
							+ log.getEpisodeIndex() + "]不正确!");
				}
			}

			if (StringUtils.isNotEmpty(log.getDuration())) {
				try {
					Integer.valueOf(log.getDuration());
				} catch (Exception e) {
					throw new ServiceException("第" + row + "行，播放时长["
							+ log.getDuration() + "]不正确!");
				}
			}

			if (StringUtils.isNotEmpty(log.getFileSize())) {
				try {
					Long.valueOf(log.getFileSize());
				} catch (Exception e) {
					throw new ServiceException("第" + row + "行，文件大小["
							+ log.getFileSize() + "]不正确!");
				}
			}

			if (StringUtils.isNotEmpty(log.getBitrate())) {
				try {
					Integer.valueOf(log.getBitrate());
				} catch (Exception e) {
					throw new ServiceException("第" + row + "行，码率["
							+ log.getBitrate() + "]不正确!");
				}
			}
		}

		for (int i = 0; i < logList.size(); i++) {
			int row = startRow + i + 1;
			MediaFileExportLog log = logList.get(i);

			if (log.getId() == null && StringUtils.isEmpty(log.getName())) {
				continue;
			}

			Integer contentType = null;
			if (StringUtils.isNotEmpty(log.getContentType())) {
				contentType = ContentTypeEnum.getKeyByValue(log
						.getContentType());
			}
			importMediaFile(mediaImport, row, log, contentType);
		}
		mediaImport.setImportTime(new Date());
		mediaImportRepository.save(mediaImport);
		logger.info("importMediaFile totalNum=" + logList.size() + " end.");
	}

	public synchronized void importMediaFile(MediaImport mediaImport, int row,
			MediaFileExportLog log, Integer contentType) {
		logger.info("importMediaFile row=" + row + " begin...");

		if (log.getId() != null) {
			MediaFile mediaFile = mediaFileRepository.findOneById(log.getId());
			if (mediaFile != null) {
				Program program = programRepository.findOneById(mediaFile
						.getProgramId());
				updateMediaFile(mediaImport, row, log, program, mediaFile);
				return;
			}
		}

		Integer episodeIndex = 1;
		if (StringUtils.isNotEmpty(log.getEpisodeIndex())) {
			try {
				episodeIndex = Integer.valueOf(log.getEpisodeIndex());
			} catch (Exception e) {

			}
		}
		List<Program> programList = null;
		if (contentType != null) {
			programList = programRepository
					.findByContentTypeAndNameAndEpisodeIndex(contentType,
							log.getName(), episodeIndex);
		} else {
			programList = programRepository.findByNameAndEpisodeIndex(
					log.getName(), episodeIndex);
		}
		if (programList != null) {
			for (Program program : programList) {
				updateMediaFile(mediaImport, row, log, program);
			}
		}
	}

	@Transactional(value = "slaveTransactionManager", propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void updateMediaFile(MediaImport mediaImport, int row,
			MediaFileExportLog log, Program program) {
		if (program == null) {
			return;
		}
		Integer mediaFileType = MediaFileTypeEnum.getKeyByValue(log
				.getMediaFileType());

		List<MediaFile> mediaFileList = mediaFileRepository.findByProgramId(
				program.getId(), mediaFileType);
		if (mediaFileList != null) {
			for (MediaFile mediaFile : mediaFileList) {
				updateMediaFile(mediaImport, row, log, program, mediaFile);
			}
		}
	}

	public void updateMediaFile(MediaImport mediaImport, int row,
			MediaFileExportLog log, Program program, MediaFile mediaFile) {
		if (program == null || mediaFile == null) {
			return;
		}
		if (StringUtils.isNotEmpty(log.getDuration())) {
			try {
				mediaFile.setDuration(Integer.valueOf(log.getDuration()));
			} catch (Exception e) {
			}
		}
		if (StringUtils.isNotEmpty(log.getFileMd5())) {
			mediaFile.setFileMd5(StringUtils.trimToEmpty(log.getFileMd5()));
		}
		if (StringUtils.isNotEmpty(log.getFileSize())) {
			try {
				mediaFile.setFileSize(Long.valueOf(log.getFileSize()));
			} catch (Exception e) {
			}
		}
		if (StringUtils.isNotEmpty(log.getBitrate())) {
			try {
				mediaFile.setBitrate(Integer.valueOf(log.getBitrate()));
			} catch (Exception e) {
			}
		}
		if (StringUtils.isNotEmpty(log.getProgramPlayCode())) {
			program.setPlayCode(StringUtils.trimToEmpty(log
					.getProgramPlayCode()));
			program.setPlayCodeStatus(PlayCodeStatusEnum.INPUT.getKey());
			mediaService.saveProgram(program);
		}
		if (StringUtils.isNotEmpty(log.getPlayCode())) {
			mediaFile.setPlayCode(StringUtils.trimToEmpty(log.getPlayCode()));
			mediaFile.setPlayCodeStatus(PlayCodeStatusEnum.INPUT.getKey());
		}
		if (StringUtils.isNotEmpty(log.getFilePath())) {
			String filePath = StringUtils.trimToEmpty(log.getFilePath())
					.replace(CloudStorage.getVideoDownloadUrl(), "");

			mediaService.saveMediaFileAndMediaStatus(mediaFile, filePath,
					mediaFile.getTemplateId());
		} else {
			mediaService.saveMediaFile(mediaFile);
		}
	}
}