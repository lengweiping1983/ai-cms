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
import com.ai.cms.media.bean.SeriesImportLog;
import com.ai.cms.media.entity.MediaImport;
import com.ai.cms.media.entity.Series;
import com.ai.cms.media.repository.MediaImportRepository;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.cms.media.repository.SeriesRepository;
import com.ai.common.enums.AuditStatusEnum;
import com.ai.common.enums.ContentTypeEnum;
import com.ai.common.enums.SeriesTypeEnum;
import com.ai.common.enums.SourceEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.exception.ServiceException;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;

@Service
@Transactional(value = "slaveTransactionManager", readOnly = false)
public class SeriesImportService extends AbstractService<MediaImport, Long> {
	private static final Log logger = LogFactory
			.getLog(SeriesImportService.class);

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
			List<SeriesImportLog> logList, int startRow) {
		if (logList == null || logList.size() <= 1) {
			throw new ServiceException("数据为空!");
		}

		logger.info("importMediaData totalNum=" + logList.size() + " begin...");
		Map<String, Cp> cpMap = configService.findAllCpNameMap();

		for (int i = 0; i < logList.size(); i++) {
			int row = startRow + i + 1;
			SeriesImportLog log = logList.get(i);

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
			if (StringUtils.isNotEmpty(log.getEpisodeTotal())) {
				try {
					Integer.valueOf(log.getEpisodeTotal());
				} catch (Exception e) {
					throw new ServiceException("第" + row + "行，总集数["
							+ log.getEpisodeTotal() + "]不正确!");
				}
			}
		}

		for (int i = 0; i < logList.size(); i++) {
			int row = startRow + i + 1;
			SeriesImportLog log = logList.get(i);

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
			importSeries(mediaImport, row, log, contentType, cpCode);
		}
		mediaImport.setImportTime(new Date());
		mediaImportRepository.save(mediaImport);
		logger.info("importMediaData totalNum=" + logList.size() + " end.");
	}

	public synchronized void importSeries(MediaImport mediaImport, int row,
			SeriesImportLog log, Integer contentType, String cpCode) {
		logger.info("importMediaData row=" + row + " begin...");
		if (log.getId() != null) {
			Series series = seriesRepository.findOne(log.getId());
			updateSeries(mediaImport, row, log, contentType, cpCode, series);
		} else {
			List<Series> seriesList = seriesRepository
					.findByName(log.getName());
			if (seriesList == null || seriesList.size() == 0) {
				updateSeries(mediaImport, row, log, contentType, cpCode, null);
			}
			for (Series series : seriesList) {
				updateSeries(mediaImport, row, log, contentType, cpCode, series);
			}
		}
	}

	@Transactional(value = "slaveTransactionManager", propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void updateSeries(MediaImport mediaImport, int row,
			SeriesImportLog log, Integer contentType, String cpCode,
			Series series) {
		if (series == null) {
			if (mediaImport.getCreateMetadata() == YesNoEnum.YES.getKey()) {
				long existNum = seriesRepository.countByName(log.getName());
				if (existNum > 0) {
					throw new ServiceException("剧头[" + log.getName() + "]已存在！");
				}
				series = new Series();
				series.setSource(SourceEnum.BATCH_IMPORT.getKey());
				series.setCpCode(cpCode);
				if (mediaImport.getAuditStatus().equals(
						"" + AuditStatusEnum.AUDIT_FIRST_PASS.getKey())) {
					series.setAuditStatus(AuditStatusEnum.AUDIT_FIRST_PASS
							.getKey());
					series.setStorageTime(new Date());
				}
			} else {
				return;
			}
		} else {
			long existNum = seriesRepository.countByNameAndNotId(log.getName(),
					series.getId());
			if (existNum > 0) {
				throw new ServiceException("剧头[" + log.getName() + "]已存在！");
			}
		}
		List<String> auditStatusList = Arrays.asList(mediaImport
				.getAuditStatus().split(","));
		if (!auditStatusList.contains("" + series.getAuditStatus())) {
			throw new ServiceException("剧头[" + log.getName() + "]不能导入！审核状态不一致！");
		}
		if (StringUtils.isNotEmpty(mediaImport.getCpCode())) {
			if (StringUtils.isEmpty(series.getCpCode())) {
				throw new ServiceException("剧头[" + log.getName()
						+ "]不能导入！内容不属于该提供商！");
			}
			List<String> cpCodeList = Arrays.asList(series.getCpCode().split(
					","));
			if (!cpCodeList.contains(mediaImport.getCpCode())) {
				throw new ServiceException("剧头[" + log.getName()
						+ "]不能导入！内容不属于该提供商！");
			}
		}

		if (StringUtils.isNotEmpty(log.getType())) {
			series.setType(SeriesTypeEnum.getKeyByValue(log.getType()));
		}
		if (StringUtils.isNotEmpty(log.getEpisodeTotal())) {
			try {
				series.setEpisodeTotal(Integer.valueOf(log.getEpisodeTotal()));
			} catch (Exception e) {

			}
		}
		if (StringUtils.isNotEmpty(cpCode)) {
			series.setCpCode(cpCode);
		}
		if (StringUtils.isNotEmpty(log.getName())) {
			series.setName(log.getName());
		}
		if (StringUtils.isNotEmpty(log.getTitle())) {
			series.setTitle(log.getTitle());
		}
		if (StringUtils.isNotEmpty(log.getCaption())) {
			series.setCaption(log.getCaption());
		}
		if (contentType != null) {
			series.setContentType(contentType);
		}

		if (StringUtils.isNotEmpty(log.getDirector())) {
			series.setDirector(log.getDirector());
		}
		if (StringUtils.isNotEmpty(log.getActor())) {
			series.setActor(log.getActor());
		}

		if (StringUtils.isNotEmpty(log.getYear())) {
			series.setYear(log.getYear());
		}
		if (StringUtils.isNotEmpty(log.getArea())) {
			series.setArea(log.getArea());
		}
		if (StringUtils.isNotEmpty(log.getLanguage())) {
			series.setLanguage(log.getLanguage());
		}

		if (log.getRating() != null) {
			try {
				float rating = Float.valueOf(log.getRating());
				BigDecimal b = new BigDecimal(rating);
				float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
				series.setRating(f1);
			} catch (Exception e) {

			}
		}
		if (StringUtils.isNotEmpty(log.getDuration())) {
			try {
				series.setDuration(Integer.valueOf(log.getDuration()));
			} catch (Exception e) {

			}
		}
		if (StringUtils.isNotEmpty(log.getSubtitle())) {
			series.setSubtitle(YesNoEnum.getKeyByValue(log.getSubtitle()));
		}
		if (StringUtils.isNotEmpty(log.getTag())) {
			series.setTag(log.getTag());
		}
		if (StringUtils.isNotEmpty(log.getInternalTag())) {
			series.setInternalTag(log.getInternalTag());
		}
		if (StringUtils.isNotEmpty(log.getKeyword())) {
			series.setKeyword(log.getKeyword());
		}

		if (StringUtils.isNotEmpty(log.getViewpoint())) {
			series.setViewpoint(log.getViewpoint());
		}
		if (StringUtils.isNotEmpty(log.getInfo())) {
			series.setInfo(log.getInfo());
		}

		if (log.getOrgAirDate() != null) {
			series.setOrgAirDate(log.getOrgAirDate());
		}
		if (StringUtils.isNotEmpty(log.getBroadcastLicense())) {
			series.setBroadcastLicense(log.getBroadcastLicense());
		}
		if (StringUtils.isNotEmpty(log.getAuthorizeInfo())) {
			series.setAuthorizeInfo(log.getAuthorizeInfo());
		}
		if (log.getAuthorizeAddress() != null) {
			series.setAuthorizeAddress(log.getAuthorizeAddress());
		}
		if (log.getLicensingWindowStart() != null) {
			series.setLicensingWindowStart(log.getLicensingWindowStart());
		}
		if (log.getLicensingWindowEnd() != null) {
			series.setLicensingWindowEnd(log.getLicensingWindowEnd());
		}
		mediaService.saveSeries(series);
	}

}