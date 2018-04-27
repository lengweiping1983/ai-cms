package com.ai.cms.media.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ai.cms.config.entity.Cp;
import com.ai.cms.config.service.ConfigService;
import com.ai.cms.media.bean.ProgramImportLog;
import com.ai.cms.media.entity.MediaImport;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.Series;
import com.ai.cms.media.repository.MediaImportRepository;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.cms.media.repository.SeriesRepository;
import com.ai.common.enums.AuditStatusEnum;
import com.ai.common.enums.ContentTypeEnum;
import com.ai.common.enums.ProgramTypeEnum;
import com.ai.common.enums.SourceEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.exception.ServiceException;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;

@Service
@Transactional(value = "slaveTransactionManager", readOnly = false)
public class ProgramImportService extends AbstractService<MediaImport, Long> {
	private static final Log logger = LogFactory
			.getLog(ProgramImportService.class);

	@Autowired
	private SeriesRepository seriesRepository;

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private MediaImportRepository mediaImportRepository;

	@Autowired
	private ConfigService configService;

	@Override
	public AbstractRepository<MediaImport, Long> getRepository() {
		return mediaImportRepository;
	}

	public synchronized void importMediaData(MediaImport mediaImport,
			List<ProgramImportLog> logList, int startRow) {
		if (logList == null || logList.size() <= 1) {
			throw new ServiceException("数据为空!");
		}

		logger.info("importMediaData totalNum=" + logList.size() + " begin...");
		Map<String, Cp> cpMap = configService.findAllCpNameMap();

		for (int i = 0; i < logList.size(); i++) {
			int row = startRow + i + 1;
			ProgramImportLog log = logList.get(i);

			if (log.getId() == null && StringUtils.isEmpty(log.getName())) {
				continue;
			}

			if (StringUtils.isEmpty(log.getName())) {
				throw new ServiceException("第" + row + "行，节目名称为空!");
			}
			if (StringUtils.isNotEmpty(log.getCpName())) {
				String cpCode = configService.getCpCodeByCpName(cpMap,
						StringUtils.trimToEmpty(log.getCpName()));
				if (StringUtils.isEmpty(cpCode)) {
					throw new ServiceException("第" + row + "行，内容提供商["
							+ log.getCpName() + "]不存在!");
				}
			}
			if (StringUtils.isNotEmpty(log.getDuration())) {
				try {
					Integer.valueOf(log.getDuration());
				} catch (Exception e) {
					throw new ServiceException("第" + row + "行，节目时长["
							+ log.getDuration() + "]不正确!");
				}
			}
			if (StringUtils.isNotEmpty(log.getEpisodeIndex())) {
				try {
					Integer.valueOf(log.getEpisodeIndex());
				} catch (Exception e) {
					throw new ServiceException("第" + row + "行，第几集["
							+ log.getEpisodeIndex() + "]不正确!");
				}
			}
		}

		for (int i = 0; i < logList.size(); i++) {
			int row = startRow + i + 1;
			ProgramImportLog log = logList.get(i);

			if (log.getId() == null && StringUtils.isEmpty(log.getName())) {
				continue;
			}

			Integer contentType = null;
			String cpCode = null;
			if (StringUtils.isNotEmpty(log.getContentType())) {
				contentType = ContentTypeEnum.getKeyByValue(log
						.getContentType());
			}
			if (StringUtils.isNotEmpty(log.getCpName())) {
				cpCode = configService.getCpCodeByCpName(cpMap,
						StringUtils.trimToEmpty(log.getCpName()));
			}
			importProgram(mediaImport, row, log, contentType, cpCode);
		}
		mediaImport.setImportTime(new Date());
		mediaImportRepository.save(mediaImport);
		logger.info("importMediaData totalNum=" + logList.size() + " end.");
	}

	public synchronized void importProgram(MediaImport mediaImport, int row,
			ProgramImportLog log, Integer contentType, String cpCode) {
		if (log.getId() != null) {
			Program program = programRepository.findOne(log.getId());
			updateProgram(mediaImport, row, log, contentType, cpCode, program);
		} else {
			Integer episodeIndex = 1;
			if (StringUtils.isNotEmpty(log.getEpisodeIndex())) {
				try {
					episodeIndex = Integer.valueOf(log.getEpisodeIndex());
				} catch (Exception e) {

				}
			}
			List<Program> programList = programRepository
					.findByNameAndEpisodeIndex(log.getName(), episodeIndex);
			if (programList == null || programList.size() == 0) {
				updateProgram(mediaImport, row, log, contentType, cpCode, null);
			}
			for (Program program : programList) {
				updateProgram(mediaImport, row, log, contentType, cpCode,
						program);
			}
		}
	}

	@Transactional(value = "slaveTransactionManager", propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void updateProgram(MediaImport mediaImport, int row,
			ProgramImportLog log, Integer contentType, String cpCode,
			Program program) {
		if (program == null) {
			if (mediaImport.getCreateMetadata() == YesNoEnum.YES.getKey()) {
				if (StringUtils.isNotEmpty(log.getType())
						&& ProgramTypeEnum.getKeyByValue(log.getType()) == ProgramTypeEnum.MOVIE
								.getKey()) {
					long existNum = programRepository
							.countByName(log.getName());
					if (existNum > 0) {
						throw new ServiceException("节目[" + log.getName()
								+ "]已存在！");
					}
				}
				program = new Program();
				program.setSource(SourceEnum.BATCH_IMPORT.getKey());
				program.setCpCode(cpCode);
				if (mediaImport.getAuditStatus().equals(
						"" + AuditStatusEnum.AUDIT_FIRST_PASS.getKey())) {
					program.setAuditStatus(AuditStatusEnum.AUDIT_FIRST_PASS
							.getKey());
					program.setStorageTime(new Date());
				}
				if (StringUtils.isNotEmpty(log.getType())
						&& ProgramTypeEnum.getKeyByValue(log.getType()) == ProgramTypeEnum.TV
								.getKey()
						&& StringUtils.isNotEmpty(log.getSeriesName())) {
					List<Series> seriesList = seriesRepository.findByName(log
							.getSeriesName());
					if (seriesList != null && seriesList.size() > 0) {
						program.setSeries(seriesList.get(0));
						program.setSeriesId(seriesList.get(0).getId());
					}
				}
			} else {
				return;
			}
		} else {
			if (StringUtils.isNotEmpty(log.getType())
					&& ProgramTypeEnum.getKeyByValue(log.getType()) == ProgramTypeEnum.MOVIE
							.getKey()) {
				long existNum = programRepository.countByNameAndNotId(
						log.getName(), program.getId());
				if (existNum > 0) {
					throw new ServiceException("节目[" + log.getName() + "]已存在！");
				}
			}
		}
		List<String> auditStatusList = Arrays.asList(mediaImport
				.getAuditStatus().split(","));
		if (!auditStatusList.contains("" + program.getAuditStatus())) {
			throw new ServiceException("节目[" + log.getName() + "]不能导入！审核状态不一致！");
		}
		if (StringUtils.isNotEmpty(mediaImport.getCpCode())) {
			if (StringUtils.isEmpty(program.getCpCode())) {
				throw new ServiceException("节目[" + log.getName()
						+ "]不能导入！内容不属于该提供商！");
			}
			List<String> cpCodeList = Arrays.asList(program.getCpCode().split(
					","));
			if (!cpCodeList.contains(mediaImport.getCpCode())) {
				throw new ServiceException("节目[" + log.getName()
						+ "]不能导入！内容不属于该提供商！");
			}
		}

		if (StringUtils.isNotEmpty(log.getType())) {
			program.setType(ProgramTypeEnum.getKeyByValue(log.getType()));
		}
		if (StringUtils.isNotEmpty(log.getEpisodeIndex())) {
			try {
				program.setEpisodeIndex(Integer.valueOf(log.getEpisodeIndex()));
			} catch (Exception e) {

			}
		}
		if (StringUtils.isNotEmpty(cpCode)) {
			program.setCpCode(cpCode);
		}
		if (StringUtils.isNotEmpty(log.getName())) {
			program.setName(log.getName());
		}
		if (StringUtils.isNotEmpty(log.getTitle())) {
			program.setTitle(log.getTitle());
		}
		if (StringUtils.isNotEmpty(log.getCaption())) {
			program.setCaption(log.getCaption());
		}
		if (contentType != null) {
			program.setContentType(contentType);
		}

		if (StringUtils.isNotEmpty(log.getDirector())) {
			program.setDirector(log.getDirector());
		}
		if (StringUtils.isNotEmpty(log.getActor())) {
			program.setActor(log.getActor());
		}

		if (StringUtils.isNotEmpty(log.getYear())) {
			program.setYear(log.getYear());
		}
		if (StringUtils.isNotEmpty(log.getArea())) {
			program.setArea(log.getArea());
		}
		if (StringUtils.isNotEmpty(log.getLanguage())) {
			program.setLanguage(log.getLanguage());
		}

		if (log.getRating() != null) {
			try {
				float rating = Float.valueOf(log.getRating());
				BigDecimal b = new BigDecimal(rating);
				float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
				program.setRating(f1);
			} catch (Exception e) {

			}
		}
		if (StringUtils.isNotEmpty(log.getDuration())) {
			try {
				program.setDuration(Integer.valueOf(log.getDuration()));
			} catch (Exception e) {

			}
		}
		if (StringUtils.isNotEmpty(log.getSubtitle())) {
			program.setSubtitle(YesNoEnum.getKeyByValue(log.getSubtitle()));
		}
		if (StringUtils.isNotEmpty(log.getTag())) {
			program.setTag(log.getTag());
		}
		if (StringUtils.isNotEmpty(log.getInternalTag())) {
			program.setInternalTag(log.getInternalTag());
		}
		if (StringUtils.isNotEmpty(log.getKeyword())) {
			program.setKeyword(log.getKeyword());
		}

		if (StringUtils.isNotEmpty(log.getViewpoint())) {
			program.setViewpoint(log.getViewpoint());
		}
		if (StringUtils.isNotEmpty(log.getInfo())) {
			program.setInfo(log.getInfo());
		}

		if (log.getOrgAirDate() != null) {
			program.setOrgAirDate(log.getOrgAirDate());
		}
		if (StringUtils.isNotEmpty(log.getBroadcastLicense())) {
			program.setBroadcastLicense(log.getBroadcastLicense());
		}
		if (StringUtils.isNotEmpty(log.getAuthorizeInfo())) {
			program.setAuthorizeInfo(log.getAuthorizeInfo());
		}
		if (log.getAuthorizeAddress() != null) {
			program.setAuthorizeAddress(log.getAuthorizeAddress());
		}
		if (log.getLicensingWindowStart() != null) {
			program.setLicensingWindowStart(log.getLicensingWindowStart());
		}
		if (log.getLicensingWindowEnd() != null) {
			program.setLicensingWindowEnd(log.getLicensingWindowEnd());
		}
		mediaService.saveProgram(program);
	}
}