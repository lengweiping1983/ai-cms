package com.ai.injection.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.AdminGlobal;
import com.ai.cms.config.entity.Cp;
import com.ai.cms.config.entity.MediaTemplate;
import com.ai.cms.config.service.ConfigService;
import com.ai.cms.injection.bean.ADI;
import com.ai.cms.injection.bean.MappingBean;
import com.ai.cms.injection.bean.MovieBean;
import com.ai.cms.injection.bean.ObjectBean;
import com.ai.cms.injection.bean.PictureBean;
import com.ai.cms.injection.bean.ProgramBean;
import com.ai.cms.injection.bean.SeriesBean;
import com.ai.cms.injection.entity.DownloadTask;
import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.entity.ReceiveTask;
import com.ai.cms.injection.enums.DownloadModuleEnum;
import com.ai.cms.injection.enums.DownloadTaskStatusEnum;
import com.ai.cms.injection.enums.InjectionDirectionEnum;
import com.ai.cms.injection.repository.DownloadTaskRepository;
import com.ai.cms.injection.repository.InjectionPlatformRepository;
import com.ai.cms.injection.repository.ReceiveTaskRepository;
import com.ai.cms.media.entity.MediaFile;
import com.ai.cms.media.entity.MediaImage;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.Series;
import com.ai.cms.media.service.MediaService;
import com.ai.common.enums.AuditStatusEnum;
import com.ai.common.enums.ContentTypeEnum;
import com.ai.common.enums.MediaFileTypeEnum;
import com.ai.common.enums.MediaImageTypeEnum;
import com.ai.common.enums.MediaStatusEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.ProgramTypeEnum;
import com.ai.common.enums.SourceEnum;
import com.ai.common.enums.ValidStatusEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.exception.DataException;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;
import com.ai.common.utils.FtpUtils;
import com.github.axet.wget.WGet;

@Service
@Transactional(value = "slaveTransactionManager", readOnly = false)
public class ReceiveService extends AbstractService<ReceiveTask, Long> {

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
	public AbstractRepository<ReceiveTask, Long> getRepository() {
		return receiveTaskRepository;
	}

	public ReceiveTask findByPlatformIdAndCorrelateId(Long platformId,
			String correlateId) {
		List<ReceiveTask> list = receiveTaskRepository
				.findByPlatformIdAndCorrelateId(platformId, correlateId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public void saveReceiveTask(ReceiveTask receiveTask) {
		receiveTaskRepository.save(receiveTask);
	}

	public InjectionPlatform findReceiveInjectionPlatform(String CSPID,
			String LSPID) {
		List<InjectionPlatform> injectionPlatformList = injectionPlatformRepository
				.findByCspIdAndLspIdAndDirection(CSPID, LSPID,
						InjectionDirectionEnum.RECEIVE.getKey());
		if (injectionPlatformList != null && injectionPlatformList.size() > 0) {
			return injectionPlatformList.get(0);
		}
		return null;
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false, rollbackFor = DataException.class)
	public void handleTask(ReceiveTask receiveTask, InjectionPlatform platform,
			ADI adi) throws DataException {
		// 4.1获取对象
		Cp cp = configService.findOneCpByCode(platform.getCspId());
		String currentCpId = (cp != null) ? "" + cp.getId() : "";

		List<ObjectBean> objects = adi.getObjects();
		List<MappingBean> mappings = adi.getMappings();

		Map<String, ObjectBean> objectMap = new HashMap<String, ObjectBean>();
		for (ObjectBean objectBean : objects) {
			objectMap.put(objectBean.getCode(), objectBean);
		}

		Map<String, List<MappingBean>> mappingMap = new HashMap<String, List<MappingBean>>();
		for (MappingBean mappingBean : mappings) {
			List<MappingBean> mappingBeanList = mappingMap.get(mappingBean
					.getElementCode());
			if (mappingBeanList == null) {
				mappingBeanList = new ArrayList<MappingBean>();
				mappingMap.put(mappingBean.getElementCode(), mappingBeanList);
			}
			mappingBeanList.add(mappingBean);
		}

		// 4.2保存剧头
		for (ObjectBean objectBean : objects) {
			if (objectBean instanceof SeriesBean) {
				SeriesBean seriesBean = (SeriesBean) objectBean;
				saveSeries(seriesBean, receiveTask, platform, currentCpId);
			}
		}

		// 4.3保存节目
		for (ObjectBean objectBean : objects) {
			if (objectBean instanceof ProgramBean) {
				ProgramBean programBean = (ProgramBean) objectBean;
				saveProgram(programBean, receiveTask, platform, currentCpId);
			}
		}

		// 4.4保存剧头与节目Mapping
		for (MappingBean mappingBean : mappings) {
			if ("Series".equalsIgnoreCase(mappingBean.getParentType())
					&& "Program".equalsIgnoreCase(mappingBean.getElementType())) {
				// 当Mapping关系涉及Series和Program间绑定时，Sequence必须填写；
				saveSeriesProgramMapping(mappingBean, receiveTask, platform,
						currentCpId);
			}
		}

		// 4.5保存Movie
		for (ObjectBean objectBean : objects) {
			if (objectBean instanceof MovieBean) {
				MovieBean movieBean = (MovieBean) objectBean;
				saveMediaFile(movieBean, receiveTask, platform, currentCpId);
			}
		}

		// 4.6保存节目与Movie Mapping
		for (MappingBean mappingBean : mappings) {
			if ("Program".equalsIgnoreCase(mappingBean.getParentType())
					&& "Movie".equalsIgnoreCase(mappingBean.getElementType())) {
				saveProgramMediaFileMapping(mappingBean, receiveTask, platform,
						currentCpId);
			}
		}

		// 4.7保存海报Mapping
		for (MappingBean mappingBean : mappings) {
			if ("Picture".equalsIgnoreCase(mappingBean.getElementType())) {
				if (StringUtils.isNotEmpty(mappingBean.getType())
						&& "1".equals(mappingBean.getType())) {
					// 只处理海报
					// Type当Mapping关系为Picture时，此字段为必填；当Mapping关系为片花时，此字段为必填；
					// Sequence当Mapping关系涉及Picture时，此字段为必填，展示顺序由上有平台保证；
					ObjectBean objectBean = objectMap.get(mappingBean
							.getElementCode());
					if (objectBean instanceof PictureBean) {
						PictureBean pictureBean = (PictureBean) objectBean;
						if ("Program".equalsIgnoreCase(mappingBean
								.getParentType())) {
							saveProgramPosterMapping(mappingBean, pictureBean,
									receiveTask, platform, currentCpId);
						} else if ("Series".equalsIgnoreCase(mappingBean
								.getParentType())) {
							saveSeriesPosterMapping(mappingBean, pictureBean,
									receiveTask, platform, currentCpId);
						}
					}
				} else if (StringUtils.isNotEmpty(mappingBean.getType())) {
					// Type当Mapping关系为Picture时，此字段为必填；当Mapping关系为片花时，此字段为必填；
					// Sequence当Mapping关系涉及Picture时，此字段为必填，展示顺序由上有平台保证；
					ObjectBean objectBean = objectMap.get(mappingBean
							.getElementCode());
					if (objectBean instanceof PictureBean) {
						PictureBean pictureBean = (PictureBean) objectBean;
						if ("Program".equalsIgnoreCase(mappingBean
								.getParentType())) {
							saveProgramImageMapping(mappingBean, pictureBean,
									receiveTask, platform, currentCpId);
						} else if ("Series".equalsIgnoreCase(mappingBean
								.getParentType())) {
							saveSeriesImageMapping(mappingBean, pictureBean,
									receiveTask, platform, currentCpId);
						}
					}
				}
			}
		}

		// 4.8计算任务总数
		long downloadTotal = downloadTaskRepository.countByModuleAndPid(
				DownloadModuleEnum.RECEIVE.getKey(), receiveTask.getId());
		receiveTask.setDownloadTotal(downloadTotal);
	}

	private void saveSeries(SeriesBean seriesBean, ReceiveTask receiveTask,
			InjectionPlatform platform, String currentCpId) {
		Series series = mediaService.findSeriesByCloudCode(platform.getCspId()
				+ "_" + seriesBean.getCode());
		// 检查该媒资是否属于该CP
		if (series != null) {
		}
		if ("DELETE".equalsIgnoreCase(seriesBean.getAction())) {
			mediaService.deleteSeries(series);
			return;
		}
		if (series == null) {
			series = new Series();
			series.setSource(SourceEnum.INJECTION.getKey());
			series.setCpId(currentCpId);
			series.setCloudId(platform.getCspId() + "_" + seriesBean.getID());
			series.setCloudCode(platform.getCspId() + "_"
					+ seriesBean.getCode());
		}
		if (StringUtils.isNotEmpty(seriesBean.getName())) {
			series.setName(seriesBean.getName());
			series.setTitle(seriesBean.getName());
		}
		if (StringUtils.isNotEmpty(seriesBean.getOriginalName())) {
			series.setOriginalName(seriesBean.getOriginalName());
		}
		if (StringUtils.isNotEmpty(seriesBean.getOrderNumber())) {
			series.setOrderNumber(seriesBean.getOrderNumber());
		}
		if (StringUtils.isNotEmpty(seriesBean.getSortName())) {
			series.setSortName(seriesBean.getSortName());
		}
		if (StringUtils.isNotEmpty(seriesBean.getSearchName())) {
			series.setSearchName(seriesBean.getSearchName());
		}

		if (StringUtils.isNotEmpty(seriesBean.getReleaseYear())) {
			series.setYear(seriesBean.getReleaseYear());
		}
		if (StringUtils.isNotEmpty(seriesBean.getOriginalCountry())) {
			series.setArea(seriesBean.getOriginalCountry());
		}
		if (StringUtils.isNotEmpty(seriesBean.getLanguage())) {
			series.setLanguage(seriesBean.getLanguage());
		}

		if (StringUtils.isNotEmpty(seriesBean.getDirector())) {
			series.setDirector(seriesBean.getDirector());
		}
		if (StringUtils.isNotEmpty(seriesBean.getActorDisplay())) {
			series.setActor(seriesBean.getActorDisplay());
		}
		if (StringUtils.isNotEmpty(seriesBean.getCompere())) {
			series.setCompere(seriesBean.getCompere());
		}
		if (StringUtils.isNotEmpty(seriesBean.getGuest())) {
			series.setGuest(seriesBean.getGuest());
		}
		if (StringUtils.isNotEmpty(seriesBean.getKeywords())) {
			series.setKeyword(seriesBean.getKeywords());
		}
		if (StringUtils.isNotEmpty(seriesBean.getTags())) {
			series.setTag(seriesBean.getTags());
		}

		if (StringUtils.isNotEmpty(seriesBean.getGenre())) {
			// 已丢去该字段
		}
		if (StringUtils.isNotEmpty(seriesBean.getKpeople())) {
			series.setKpeople(seriesBean.getKpeople());
		}
		if (StringUtils.isNotEmpty(seriesBean.getScriptWriter())) {
			series.setScriptWriter(seriesBean.getScriptWriter());
		}
		if (StringUtils.isNotEmpty(seriesBean.getReporter())) {
			series.setReporter(seriesBean.getReporter());
		}
		if (StringUtils.isNotEmpty(seriesBean.getOPIncharge())) {
			series.setIncharge(seriesBean.getOPIncharge());
		}

		if (StringUtils.isNotEmpty(seriesBean.getRMediaCode())) {
		}
		if (StringUtils.isNotEmpty(seriesBean.getVSPCode())) {
			series.setVspCode(seriesBean.getVSPCode());
		}
		if (StringUtils.isNotEmpty(seriesBean.getSPCode())) {
			series.setSpCode(seriesBean.getSPCode());
		}
		if (StringUtils.isNotEmpty(seriesBean.getContentProvider())) {
			series.setCpCode(seriesBean.getContentProvider());
		}
		if (StringUtils.isNotEmpty(seriesBean.getCopyRight())) {
			series.setCopyRight(seriesBean.getCopyRight());
		}

		if (StringUtils.isNotEmpty(seriesBean.getType())) {
			series.setContentType(ContentTypeEnum.getKeyByValue(seriesBean
					.getType()));
		} else {
			series.setContentType(ContentTypeEnum.TV.getKey());
		}
		if (StringUtils.isNotEmpty(seriesBean.getVolumnCount())) {
			series.setEpisodeTotal(NumberUtils.toInt(seriesBean
					.getVolumnCount()));// 总集数
		}
		if (StringUtils.isNotEmpty(seriesBean.getSeriesType())) {
			int type = NumberUtils.toInt(seriesBean.getSeriesType(), 1);// 1：连续剧，2：系列片
			series.setType(type);
		}
		if (StringUtils.isNotEmpty(seriesBean.getDescription())) {
			series.setInfo(seriesBean.getDescription());
		}
		if (StringUtils.isNotEmpty(seriesBean.getStatus())) {
			int status = NumberUtils.toInt(seriesBean.getStatus(), 1);// 0:失效
																		// 1:生效
			if (status == 0) { // 0:失效
				series.setStatus(OnlineStatusEnum.OFFLINE.getKey());
			} else if (status == 1
					&& series.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {// 1:生效
				series.setStatus(OnlineStatusEnum.ONLINE.getKey());
			}
		}

		if (StringUtils.isNotEmpty(seriesBean.getOrgAirDate())) {
			try {
				Date date = DateUtils.parseDate(seriesBean.getOrgAirDate(),
						"yyyyMMdd");
				series.setOrgAirDate(date);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (StringUtils.isNotEmpty(seriesBean.getLicensingWindowStart())) {
			try {
				Date date = DateUtils.parseDate(
						seriesBean.getLicensingWindowStart(), "yyyyMMddHHmmss");
				series.setLicensingWindowStart(date);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (StringUtils.isNotEmpty(seriesBean.getLicensingWindowEnd())) {
			try {
				Date date = DateUtils.parseDate(
						seriesBean.getLicensingWindowEnd(), "yyyyMMddHHmmss");
				series.setLicensingWindowEnd(date);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (platform.getNeedAudit() == YesNoEnum.NO.getKey()) {
			series.setAuditStatus(AuditStatusEnum.AUDIT_FIRST_PASS.getKey());
			series.setStorageTime(new Date());
		}
		if (platform.getPlayCodeCustom() == YesNoEnum.NO.getKey()) {
			series.setPlayCode(seriesBean.getCode());
		}
		try {
			mediaService.saveSeries(series);
		} catch (Exception e) {
			throw new DataException("Series[" + seriesBean.getCode() + "]数据错误！");
		}
	}

	private void saveProgram(ProgramBean programBean, ReceiveTask receiveTask,
			InjectionPlatform platform, String currentCpId) {
		Program program = mediaService.findProgramByCloudCode(platform
				.getCspId() + "_" + programBean.getCode());
		// 检查该媒资是否属于该CP
		if (program != null) {
		}
		if ("DELETE".equalsIgnoreCase(programBean.getAction())) {
			mediaService.deleteProgram(program);
			return;
		}
		if (program == null) {
			program = new Program();
			program.setSource(SourceEnum.INJECTION.getKey());
			program.setCpId(currentCpId);
			program.setCloudId(platform.getCspId() + "_" + programBean.getID());
			program.setCloudCode(platform.getCspId() + "_"
					+ programBean.getCode());
		}
		if (StringUtils.isNotEmpty(programBean.getName())) {
			program.setName(programBean.getName());
			program.setTitle(programBean.getName());
		}
		if (StringUtils.isNotEmpty(programBean.getOriginalName())) {
			program.setOriginalName(programBean.getOriginalName());
		}
		if (StringUtils.isNotEmpty(programBean.getOrderNumber())) {
			program.setOrderNumber(programBean.getOrderNumber());
		}
		if (StringUtils.isNotEmpty(programBean.getSortName())) {
			program.setSortName(programBean.getSortName());
		}
		if (StringUtils.isNotEmpty(programBean.getSearchName())) {
			program.setSearchName(programBean.getSearchName());
		}

		if (StringUtils.isNotEmpty(programBean.getReleaseYear())) {
			program.setYear(programBean.getReleaseYear());
		}
		if (StringUtils.isNotEmpty(programBean.getOriginalCountry())) {
			program.setArea(programBean.getOriginalCountry());
		}
		if (StringUtils.isNotEmpty(programBean.getLanguage())) {
			program.setLanguage(programBean.getLanguage());
		}

		if (StringUtils.isNotEmpty(programBean.getDirector())) {
			program.setDirector(programBean.getDirector());
		}
		if (StringUtils.isNotEmpty(programBean.getActorDisplay())) {
			program.setActor(programBean.getActorDisplay());
		}
		if (StringUtils.isNotEmpty(programBean.getCompere())) {
			program.setCompere(programBean.getCompere());
		}
		if (StringUtils.isNotEmpty(programBean.getGuest())) {
			program.setGuest(programBean.getGuest());
		}
		if (StringUtils.isNotEmpty(programBean.getKeywords())) {
			program.setKeyword(programBean.getKeywords());
		}
		if (StringUtils.isNotEmpty(programBean.getTags())) {
			program.setTag(programBean.getTags());
		}

		if (StringUtils.isNotEmpty(programBean.getGenre())) {
			// 已丢去该字段
		}
		if (StringUtils.isNotEmpty(programBean.getKpeople())) {
			program.setKpeople(programBean.getKpeople());
		}
		if (StringUtils.isNotEmpty(programBean.getScriptWriter())) {
			program.setScriptWriter(programBean.getScriptWriter());
		}
		if (StringUtils.isNotEmpty(programBean.getReporter())) {
			program.setReporter(programBean.getReporter());
		}
		if (StringUtils.isNotEmpty(programBean.getOPIncharge())) {
			program.setIncharge(programBean.getOPIncharge());
		}

		if (StringUtils.isNotEmpty(programBean.getRMediaCode())) {
		}
		if (StringUtils.isNotEmpty(programBean.getVSPCode())) {
			program.setVspCode(programBean.getVSPCode());
		}
		if (StringUtils.isNotEmpty(programBean.getSPCode())) {
			program.setSpCode(programBean.getSPCode());
		}
		if (StringUtils.isNotEmpty(programBean.getContentProvider())) {
			program.setCpCode(programBean.getContentProvider());
		}
		if (StringUtils.isNotEmpty(programBean.getCopyRight())) {
			program.setCopyRight(programBean.getCopyRight());
		}

		if (StringUtils.isNotEmpty(programBean.getType())) {
			if ("VOD".equalsIgnoreCase(programBean.getType())) {
				program.setContentType(ContentTypeEnum.MOVIE.getKey());
			} else {
				program.setContentType(ContentTypeEnum
						.getKeyByValue(programBean.getType()));
			}
		} else {
			program.setContentType(ContentTypeEnum.MOVIE.getKey());
		}
		if (StringUtils.isNotEmpty(programBean.getSeriesFlag())) {
			int type = NumberUtils.toInt(programBean.getSeriesFlag(), 0);// 0:普通VOD
																			// 1:连续剧剧集
			if (type == 1) {// 1:连续剧剧集
				program.setType(ProgramTypeEnum.TV.getKey());
			}
		}
		if (StringUtils.isNotEmpty(programBean.getSourceType())) {
			int sourceType = NumberUtils.toInt(programBean.getSourceType(), 1);// 1:VOD,5:Advertisement
			program.setSourceType(sourceType);
		}
		if (StringUtils.isNotEmpty(programBean.getDuration())) {
			program.setDuration(NumberUtils.toInt(programBean.getDuration()));
		}

		if (StringUtils.isNotEmpty(programBean.getDescription())) {
			program.setInfo(programBean.getDescription());
		}
		if (StringUtils.isNotEmpty(programBean.getStatus())) {
			int status = NumberUtils.toInt(programBean.getStatus(), 1);// 0:失效
																		// 1:生效
			if (status == 0) { // 0:失效
				program.setStatus(OnlineStatusEnum.OFFLINE.getKey());
			} else if (status == 1
					&& program.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {// 1:生效
				program.setStatus(OnlineStatusEnum.ONLINE.getKey());
			}
		}

		if (StringUtils.isNotEmpty(programBean.getOrgAirDate())) {
			try {
				Date date = DateUtils.parseDate(programBean.getOrgAirDate(),
						"yyyyMMdd");
				program.setOrgAirDate(date);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (StringUtils.isNotEmpty(programBean.getLicensingWindowStart())) {
			try {
				Date date = DateUtils
						.parseDate(programBean.getLicensingWindowStart(),
								"yyyyMMddHHmmss");
				program.setLicensingWindowStart(date);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (StringUtils.isNotEmpty(programBean.getLicensingWindowEnd())) {
			try {
				Date date = DateUtils.parseDate(
						programBean.getLicensingWindowEnd(), "yyyyMMddHHmmss");
				program.setLicensingWindowEnd(date);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (platform.getNeedAudit() == YesNoEnum.NO.getKey()) {
			program.setAuditStatus(AuditStatusEnum.AUDIT_FIRST_PASS.getKey());
			program.setStorageTime(new Date());
		}

		if (platform.getPlayCodeCustom() == YesNoEnum.NO.getKey()) {
			program.setPlayCode(programBean.getCode());
		}
		try {
			mediaService.saveProgram(program);
		} catch (Exception e) {
			throw new DataException("Program[" + programBean.getCode()
					+ "]数据错误！");
		}
	}

	private void saveMediaFile(MovieBean movieBean, ReceiveTask receiveTask,
			InjectionPlatform platform, String currentCpId) {
		MediaFile mediaFile = mediaService.findMediaFileByCloudCode(platform
				.getCspId() + "_" + movieBean.getCode());
		if (mediaFile != null) {
			Program program = mediaService.findProgramById(mediaFile
					.getProgramId());
			// 检查该媒资是否属于该CP
			if (program != null) {
			}
		}
		if ("DELETE".equalsIgnoreCase(movieBean.getAction())) {
			mediaService.deleteMediaFile(mediaFile);
			return;
		}
		if (mediaFile == null) {
			mediaFile = new MediaFile();
			mediaFile.setSource(SourceEnum.INJECTION.getKey());
			mediaFile.setCloudId(platform.getCspId() + "_" + movieBean.getID());
			mediaFile.setCloudCode(platform.getCspId() + "_"
					+ movieBean.getCode());
		}
		if (StringUtils.isNotEmpty(movieBean.getType())) {
			int type = NumberUtils.toInt(movieBean.getType(),
					MediaFileTypeEnum.DEFAULT.getKey());// 媒体类型1:正片,2:预览片
			mediaFile.setType(type);
		}

		String sourceFilePath = StringUtils.trimToEmpty(mediaFile
				.getSourceFilePath());
		String inputFilePath = StringUtils.trimToEmpty(movieBean.getFileURL());
		if (StringUtils.isNotEmpty(inputFilePath)) {
			mediaFile.setSourceFilePath(inputFilePath);
			if (platform.getNeedDownloadVideo() == YesNoEnum.NO.getKey()) {// 不需要下载视频文件
				mediaFile.setFilePath(inputFilePath);
				mediaFile.setMediaStatus(MediaStatusEnum.OK.getKey());
			} else if (platform.getNeedDownloadVideo() == YesNoEnum.YES
					.getKey() && !sourceFilePath.equals(inputFilePath)) {// 需要下载视频文件
				mediaFile.setMediaStatus(MediaStatusEnum.DOWNLOAD.getKey());
			}
		} else {
			throw new DataException("Movie[" + movieBean.getCode() + "]没有视频地址！");
		}
		if (StringUtils.isNotEmpty(movieBean.getOCSURL())) {
		}
		if (StringUtils.isNotEmpty(movieBean.getClosedCaptioning())) {
			// 字幕标志:0:无字幕,1:有字幕
			int subtitle = NumberUtils
					.toInt(movieBean.getClosedCaptioning(), 1);
			mediaFile.setSubtitle(subtitle);
		}
		if (StringUtils.isNotEmpty(movieBean.getDuration())) {
			mediaFile
					.setDuration(NumberUtils.toInt(movieBean.getDuration()) * 60);
		}
		if (StringUtils.isNotEmpty(movieBean.getFileSize())) {
			mediaFile.setFileSize(NumberUtils.toLong(movieBean.getFileSize()));
		}
		if (StringUtils.isNotEmpty(movieBean.getMediaSpec())) {
			mediaFile.setMediaSpec(movieBean.getMediaSpec());

			List<MediaTemplate> mediaTemplateList = configService
					.findAllMediaTemplate();
			for (MediaTemplate mediaTemplate : mediaTemplateList) {
				if (mediaTemplate.getStatus() == ValidStatusEnum.VALID.getKey()
						&& StringUtils.trimToEmpty(movieBean.getMediaSpec())
								.equalsIgnoreCase(mediaTemplate.getMediaSpec())) {
					mediaFile.setTemplateId(mediaTemplate.getId());
					break;
				}
			}
			if (mediaFile.getTemplateId() != null
					&& mediaFile.getTemplateId() <= 0) {
				for (MediaTemplate mediaTemplate : mediaTemplateList) {
					if (StringUtils.trimToEmpty(movieBean.getMediaSpec())
							.equalsIgnoreCase(mediaTemplate.getMediaSpec())) {
						mediaFile.setTemplateId(mediaTemplate.getId());
						break;
					}
				}
			}
		}
		if (platform.getPlayCodeCustom() == YesNoEnum.NO.getKey()) {
			mediaFile.setPlayCode(movieBean.getCode());
		}
		mediaService.saveMediaFile(mediaFile);

		if (platform.getNeedDownloadVideo() == YesNoEnum.YES.getKey()
				&& !sourceFilePath.equals(inputFilePath)) {// 需要下载视频文件
			DownloadTask downloadTask = new DownloadTask();

			downloadTask.setModule(DownloadModuleEnum.RECEIVE.getKey());
			downloadTask.setPid(receiveTask.getId());

			downloadTask.setProgramId(mediaFile.getProgramId());
			downloadTask.setMediaFileId(mediaFile.getId());
			downloadTask.setInputFilePath(inputFilePath);
			downloadTask.setSourceFileMd5(mediaFile.getSourceFileMd5());
			downloadTask.setSourceFileSize(mediaFile.getSourceFileSize());
			String dirPath = "/receive/"
					+ platform.getCspId()
					+ DateFormatUtils.format(receiveTask.getCreateTime(),
							"/yyyyMM");
			String fileName = StringUtils
					.substringAfterLast(inputFilePath, "/");
			String suffix = StringUtils.substringAfterLast(inputFilePath, ".");
			String outputFilePath = dirPath + "/" + mediaFile.getId() + "_"
					+ fileName.replace(suffix, suffix.toLowerCase());
			downloadTask.setOutputFilePath(outputFilePath);

			downloadTask.setPriority(receiveTask.getPriority());
			downloadTask.setStatus(DownloadTaskStatusEnum.DEFAULT.getKey());
			String name = StringUtils.trimToEmpty(movieBean.getName());
			if (StringUtils.isEmpty(name)) {
				name = fileName;
			}
			downloadTask.setName(name);
			downloadTaskRepository.save(downloadTask);
		}
	}

	private void saveSeriesProgramMapping(MappingBean mappingBean,
			ReceiveTask receiveTask, InjectionPlatform platform,
			String currentCpId) {
		Series series = mediaService.findSeriesByCloudCode(platform.getCspId()
				+ "_" + mappingBean.getParentCode());
		Program program = mediaService.findProgramByCloudCode(platform
				.getCspId() + "_" + mappingBean.getElementCode());
		if (series == null) {
			throw new DataException("Series[" + mappingBean.getParentCode()
					+ "]不存在！");
		}
		if (program == null) {
			throw new DataException("Program[" + mappingBean.getElementCode()
					+ "]不存在！");
		}
		// 检查该媒资是否属于该CP
		if (series != null) {
		}
		// 检查该媒资是否属于该CP
		if (program != null) {
		}
		if ("DELETE".equalsIgnoreCase(mappingBean.getAction())) {
			program.setSeries(null);
			program.setSeriesId(null);
			program.setEpisodeIndex(1);
			mediaService.saveProgram(program);
			return;
		}
		program.setType(ProgramTypeEnum.TV.getKey());
		program.setSeries(series);
		program.setSeriesId(series.getId());
		if (StringUtils.isNotEmpty(mappingBean.getSequence())) {
			program.setEpisodeIndex(NumberUtils.toInt(
					mappingBean.getSequence(), 1));
		} else {
			throw new DataException("Mapping[" + mappingBean.getElementCode()
					+ "]不正确！");
		}
		try {
			mediaService.saveProgramAndMediaStatus(program, true);
		} catch (Exception e) {
			throw new DataException("Program[" + mappingBean.getElementCode()
					+ "]数据错误！");
		}
	}

	private void saveProgramMediaFileMapping(MappingBean mappingBean,
			ReceiveTask receiveTask, InjectionPlatform platform,
			String currentCpId) {
		Program program = mediaService.findProgramByCloudCode(platform
				.getCspId() + "_" + mappingBean.getParentCode());
		MediaFile mediaFile = mediaService.findMediaFileByCloudCode(platform
				.getCspId() + "_" + mappingBean.getElementCode());
		if (program == null) {
			throw new DataException("Program[" + mappingBean.getParentCode()
					+ "]不存在！");
		}
		if (mediaFile == null) {
			throw new DataException("Movie[" + mappingBean.getElementCode()
					+ "]不存在！");
		}
		// 检查该媒资是否属于该CP
		if (program != null) {
		}
		if ("DELETE".equalsIgnoreCase(mappingBean.getAction())) {
			mediaFile.setProgramId(null);
			mediaFile.setSeriesId(null);
			mediaFile.setEpisodeIndex(null);
			mediaService.saveMediaFile(mediaFile);
			return;
		}
		if (platform.getNeedDownloadVideo() == YesNoEnum.YES.getKey()
				&& StringUtils.isNotEmpty(mediaFile.getSourceFilePath())) {// 需要下载视频文件,更新下载任务名称
			List<DownloadTask> downloadTaskList = downloadTaskRepository
					.findByModuleAndPidAndStatusAndMediaFileIdAndInputFilePath(
							DownloadModuleEnum.RECEIVE.getKey(),
							receiveTask.getId(),
							DownloadTaskStatusEnum.DEFAULT.getKey(),
							mediaFile.getId(), mediaFile.getSourceFilePath());
			if (downloadTaskList != null && downloadTaskList.size() > 0) {
				DownloadTask downloadTask = downloadTaskList.get(0);
				downloadTask.setName(program.getName());
				downloadTask.setProgramId(program.getId());
				downloadTask.setStatus(DownloadTaskStatusEnum.WAIT.getKey());
				downloadTaskRepository.save(downloadTask);
			}
		}

		mediaFile.setProgramId(program.getId());
		mediaFile.setSeriesId(program.getSeriesId());
		mediaFile.setEpisodeIndex(program.getEpisodeIndex());
		mediaService.saveMediaFileAndMediaStatus(mediaFile,
				mediaFile.getFilePath(), mediaFile.getTemplateId());
	}

	private void saveSeriesPosterMapping(MappingBean mappingBean,
			PictureBean pictureBean, ReceiveTask receiveTask,
			InjectionPlatform platform, String currentCpId) {
		Series series = mediaService.findSeriesByCloudCode(platform.getCspId()
				+ "_" + mappingBean.getParentCode());
		if (series == null) {
			throw new DataException("Series[" + mappingBean.getParentCode()
					+ "]不存在！");
		}
		// 检查该媒资是否属于该CP
		if (series != null) {
		}
		if (StringUtils.isEmpty(mappingBean.getSequence())
				|| "1".equals(mappingBean.getSequence())) {
			if ("DELETE".equalsIgnoreCase(mappingBean.getAction())
					&& (StringUtils.isEmpty(series.getImage1Code()) || series
							.getImage1Code().equals(pictureBean.getCode()))) {
				series.setImage1(null);
				series.setImage1Code(null);
				mediaService.saveSeries(series);
				return;
			}

			if (StringUtils.isNotEmpty(pictureBean.getFileURL())
					&& (StringUtils.isEmpty(series.getImage1Code()) || series
							.getImage1Code().equals(pictureBean.getCode()))) {
				series.setImage1(getFilePath(pictureBean.getFileURL(),
						receiveTask, platform));
				series.setImage1Code(pictureBean.getCode());
				mediaService.saveSeries(series);
			}
		}

		if ("2".equals(mappingBean.getSequence())) {
			if ("DELETE".equalsIgnoreCase(mappingBean.getAction())
					&& (StringUtils.isEmpty(series.getImage2Code()) || series
							.getImage2Code().equals(pictureBean.getCode()))) {
				series.setImage2(null);
				series.setImage2Code(null);
				mediaService.saveSeries(series);
				return;
			}

			if (StringUtils.isNotEmpty(pictureBean.getFileURL())
					&& (StringUtils.isEmpty(series.getImage2Code()) || series
							.getImage2Code().equals(pictureBean.getCode()))) {
				series.setImage2(getFilePath(pictureBean.getFileURL(),
						receiveTask, platform));
				series.setImage2Code(pictureBean.getCode());
				mediaService.saveSeries(series);
			}
		}

		if ("3".equals(mappingBean.getSequence())) {
			if ("DELETE".equalsIgnoreCase(mappingBean.getAction())
					&& (StringUtils.isEmpty(series.getImage3Code()) || series
							.getImage3Code().equals(pictureBean.getCode()))) {
				series.setImage3(null);
				series.setImage3Code(null);
				mediaService.saveSeries(series);
				return;
			}

			if (StringUtils.isNotEmpty(pictureBean.getFileURL())
					&& (StringUtils.isEmpty(series.getImage3Code()) || series
							.getImage3Code().equals(pictureBean.getCode()))) {
				series.setImage3(getFilePath(pictureBean.getFileURL(),
						receiveTask, platform));
				series.setImage3Code(pictureBean.getCode());
				mediaService.saveSeries(series);
			}
		}

		if ("4".equals(mappingBean.getSequence())) {
			if ("DELETE".equalsIgnoreCase(mappingBean.getAction())
					&& (StringUtils.isEmpty(series.getImage4Code()) || series
							.getImage4Code().equals(pictureBean.getCode()))) {
				series.setImage4(null);
				series.setImage4Code(null);
				mediaService.saveSeries(series);
				return;
			}

			if (StringUtils.isNotEmpty(pictureBean.getFileURL())
					&& (StringUtils.isEmpty(series.getImage4Code()) || series
							.getImage4Code().equals(pictureBean.getCode()))) {
				series.setImage4(getFilePath(pictureBean.getFileURL(),
						receiveTask, platform));
				series.setImage4Code(pictureBean.getCode());
				mediaService.saveSeries(series);
			}
		}
	}

	private void saveProgramPosterMapping(MappingBean mappingBean,
			PictureBean pictureBean, ReceiveTask receiveTask,
			InjectionPlatform platform, String currentCpId) {
		Program program = mediaService.findProgramByCloudCode(platform
				.getCspId() + "_" + mappingBean.getParentCode());
		if (program == null) {
			throw new DataException("Program[" + mappingBean.getParentCode()
					+ "]不存在！");
		}
		// 检查该媒资是否属于该CP
		if (program != null) {
		}
		if (StringUtils.isEmpty(mappingBean.getSequence())
				|| "1".equals(mappingBean.getSequence())) {
			if ("DELETE".equalsIgnoreCase(mappingBean.getAction())
					&& (StringUtils.isEmpty(program.getImage1Code()) || program
							.getImage1Code().equals(pictureBean.getCode()))) {
				program.setImage1(null);
				program.setImage1Code(null);
				mediaService.saveProgram(program);
				return;
			}

			if (StringUtils.isNotEmpty(pictureBean.getFileURL())
					&& (StringUtils.isEmpty(program.getImage1Code()) || program
							.getImage1Code().equals(pictureBean.getCode()))) {
				program.setImage1(getFilePath(pictureBean.getFileURL(),
						receiveTask, platform));
				program.setImage1Code(pictureBean.getCode());
				mediaService.saveProgram(program);
			}
		}
		if ("2".equals(mappingBean.getSequence())) {
			if ("DELETE".equalsIgnoreCase(mappingBean.getAction())
					&& (StringUtils.isEmpty(program.getImage2Code()) || program
							.getImage2Code().equals(pictureBean.getCode()))) {
				program.setImage2(null);
				program.setImage2Code(null);
				mediaService.saveProgram(program);
				return;
			}

			if (StringUtils.isNotEmpty(pictureBean.getFileURL())
					&& (StringUtils.isEmpty(program.getImage2Code()) || program
							.getImage2Code().equals(pictureBean.getCode()))) {
				program.setImage2(getFilePath(pictureBean.getFileURL(),
						receiveTask, platform));
				program.setImage2Code(pictureBean.getCode());
				mediaService.saveProgram(program);
			}
		}

		if ("3".equals(mappingBean.getSequence())) {
			if ("DELETE".equalsIgnoreCase(mappingBean.getAction())
					&& (StringUtils.isEmpty(program.getImage3Code()) || program
							.getImage3Code().equals(pictureBean.getCode()))) {
				program.setImage3(null);
				program.setImage3Code(null);
				mediaService.saveProgram(program);
				return;
			}

			if (StringUtils.isNotEmpty(pictureBean.getFileURL())
					&& (StringUtils.isEmpty(program.getImage3Code()) || program
							.getImage3Code().equals(pictureBean.getCode()))) {
				program.setImage3(getFilePath(pictureBean.getFileURL(),
						receiveTask, platform));
				program.setImage3Code(pictureBean.getCode());
				mediaService.saveProgram(program);
			}
		}

		if ("4".equals(mappingBean.getSequence())) {
			if ("DELETE".equalsIgnoreCase(mappingBean.getAction())
					&& (StringUtils.isEmpty(program.getImage4Code()) || program
							.getImage4Code().equals(pictureBean.getCode()))) {
				program.setImage4(null);
				program.setImage4Code(null);
				mediaService.saveProgram(program);
				return;
			}

			if (StringUtils.isNotEmpty(pictureBean.getFileURL())
					&& (StringUtils.isEmpty(program.getImage4Code()) || program
							.getImage4Code().equals(pictureBean.getCode()))) {
				program.setImage4(getFilePath(pictureBean.getFileURL(),
						receiveTask, platform));
				program.setImage4Code(pictureBean.getCode());
				mediaService.saveProgram(program);
			}
		}
	}

	private void saveSeriesImageMapping(MappingBean mappingBean,
			PictureBean pictureBean, ReceiveTask receiveTask,
			InjectionPlatform platform, String currentCpId) {
		Series series = mediaService.findSeriesByCloudCode(platform.getCspId()
				+ "_" + mappingBean.getParentCode());
		if (series == null) {
			throw new DataException("Series[" + mappingBean.getParentCode()
					+ "]不存在！");
		}
		// 检查该媒资是否属于该CP
		if (series != null) {
		}
		MediaImage mediaImage = mediaService.findMediaImageByCloudCode(platform
				.getCspId() + "_" + mappingBean.getElementCode());
		if ("DELETE".equalsIgnoreCase(mappingBean.getAction())) {
			mediaService.deleteMediaImage(mediaImage);
			return;
		}
		if (mediaImage == null) {
			mediaImage = new MediaImage();
			mediaImage.setSource(SourceEnum.INJECTION.getKey());
			mediaImage.setCloudId(platform.getCspId() + "_"
					+ pictureBean.getID());
			mediaImage.setCloudCode(platform.getCspId() + "_"
					+ pictureBean.getCode());
		}
		mediaImage.setSeriesId(series.getId());
		if (StringUtils.isNotEmpty(mappingBean.getType())) {
			int type = NumberUtils.toInt(mappingBean.getType(),
					MediaImageTypeEnum.STILLS.getKey());
			mediaImage.setType(type);
		} else {
			throw new DataException("Mapping[" + mappingBean.getElementCode()
					+ "]不正确！");
		}
		if (StringUtils.isNotEmpty(mappingBean.getSequence())) {
			mediaImage.setSortIndex(NumberUtils.toInt(
					mappingBean.getSequence(), 1));
		} else {
			throw new DataException("Mapping[" + mappingBean.getElementCode()
					+ "]不正确！");
		}
		mediaImage.setFilePath(getFilePath(pictureBean.getFileURL(),
				receiveTask, platform));
		if (StringUtils.isNotEmpty(mediaImage.getFilePath())) {
			File file = new File(AdminGlobal.getImageUploadPath(mediaImage
					.getFilePath()));
			if (file.exists()) {
				mediaImage.setFileSize(file.length());
			}
			String fileName = StringUtils.substringAfterLast(
					mediaImage.getFilePath(), "/");
			mediaImage.setSourceFileName(fileName);
		}
		mediaService.saveMediaImage(mediaImage);
	}

	private void saveProgramImageMapping(MappingBean mappingBean,
			PictureBean pictureBean, ReceiveTask receiveTask,
			InjectionPlatform platform, String currentCpId) {
		Program program = mediaService.findProgramByCloudCode(platform
				.getCspId() + "_" + mappingBean.getParentCode());
		if (program == null) {
			throw new DataException("Program[" + mappingBean.getParentCode()
					+ "]不存在！");
		}
		// 检查该媒资是否属于该CP
		if (program != null) {
		}
		MediaImage mediaImage = mediaService.findMediaImageByCloudCode(platform
				.getCspId() + "_" + mappingBean.getElementCode());
		if ("DELETE".equalsIgnoreCase(mappingBean.getAction())) {
			mediaService.deleteMediaImage(mediaImage);
			return;
		}
		if (mediaImage == null) {
			mediaImage = new MediaImage();
			mediaImage.setSource(SourceEnum.INJECTION.getKey());
			mediaImage.setCloudId(platform.getCspId() + "_"
					+ pictureBean.getID());
			mediaImage.setCloudCode(platform.getCspId() + "_"
					+ pictureBean.getCode());
		}
		mediaImage.setProgramId(program.getId());

		if (StringUtils.isNotEmpty(mappingBean.getType())) {
			int type = NumberUtils.toInt(mappingBean.getType(),
					MediaImageTypeEnum.STILLS.getKey());
			mediaImage.setType(type);
		} else {
			throw new DataException("Mapping[" + mappingBean.getElementCode()
					+ "]不正确！");
		}
		if (StringUtils.isNotEmpty(mappingBean.getSequence())) {
			mediaImage.setSortIndex(NumberUtils.toInt(
					mappingBean.getSequence(), 1));
		} else {
			throw new DataException("Mapping[" + mappingBean.getElementCode()
					+ "]不正确！");
		}
		mediaImage.setFilePath(getFilePath(pictureBean.getFileURL(),
				receiveTask, platform));
		if (StringUtils.isNotEmpty(mediaImage.getFilePath())) {
			File file = new File(AdminGlobal.getImageUploadPath(mediaImage
					.getFilePath()));
			if (file.exists()) {
				mediaImage.setFileSize(file.length());
			}
			String fileName = StringUtils.substringAfterLast(
					mediaImage.getFilePath(), "/");
			mediaImage.setSourceFileName(fileName);
		}
		mediaService.saveMediaImage(mediaImage);
	}

	/**
	 * 下载海报
	 * 
	 * @param source
	 * @param receiveTask
	 * @param platform
	 * @return
	 */
	public String getFilePath(String source, ReceiveTask receiveTask,
			InjectionPlatform platform) {
		String suffix = StringUtils.substringAfterLast(source, ".")
				.toLowerCase();
		String dirPath = "/receive/"
				+ platform.getCspId()
				+ DateFormatUtils
						.format(receiveTask.getCreateTime(), "/yyyyMM");
		String fileName = System.currentTimeMillis() + "." + suffix;
		String filePath = dirPath + "/" + fileName;
		if (source.indexOf("http") == 0) {
			try {
				downloadFile(source, AdminGlobal.getImageUploadPath(filePath));
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new DataException("下载[" + source + "]错误！");
			}
		} else if (source.indexOf("ftp") == 0) {
			try {
				FtpUtils.downloadFileByFtpPath(source,
						AdminGlobal.getImageUploadPath(filePath), true);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new DataException("下载[" + source + "]错误！");
			}
		}
		return filePath;
	}

	public static boolean downloadFile(String downloadUrl, String uploadUrl)
			throws MalformedURLException {
		URL url = new URL(downloadUrl);
		File targetFile = new File(uploadUrl);
		WGet w = new WGet(url, targetFile);
		w.download();
		return true;
	}

}