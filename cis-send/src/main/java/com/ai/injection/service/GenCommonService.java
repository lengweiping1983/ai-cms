package com.ai.injection.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.AdminGlobal;
import com.ai.AppGlobal;
import com.ai.cms.config.entity.MediaTemplate;
import com.ai.cms.injection.bean.ADI;
import com.ai.cms.injection.bean.CategoryBean;
import com.ai.cms.injection.bean.MappingBean;
import com.ai.cms.injection.bean.MovieBean;
import com.ai.cms.injection.bean.ObjectBean;
import com.ai.cms.injection.bean.ScheduleBean;
import com.ai.cms.injection.entity.InjectionObject;
import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.entity.SendTask;
import com.ai.cms.injection.enums.InjectionActionTypeEnum;
import com.ai.cms.injection.enums.InjectionItemTypeEnum;
import com.ai.cms.injection.enums.InjectionStatusEnum;
import com.ai.cms.injection.repository.InjectionObjectRepository;
import com.ai.cms.injection.repository.InjectionPlatformRepository;
import com.ai.cms.injection.repository.SendTaskRepository;
import com.ai.cms.injection.service.InjectionService;
import com.ai.cms.media.entity.MediaFile;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.repository.MediaFileRepository;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.cms.media.repository.SeriesRepository;
import com.ai.cms.media.service.MediaService;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;

@Service
@Transactional(value = "slaveTransactionManager", readOnly = false)
public class GenCommonService extends AbstractService<SendTask, Long> {
	protected static final Log logger = LogFactory
			.getLog(GenCommonService.class);

	@Autowired
	protected InjectionService injectionService;

	@Autowired
	protected InjectionPlatformRepository injectionPlatformRepository;

	@Autowired
	protected InjectionObjectRepository injectionObjectRepository;

	@Autowired
	protected SendTaskRepository sendTaskRepository;

	@Autowired
	protected MediaService mediaService;

	@Autowired
	protected SeriesRepository seriesRepository;

	@Autowired
	protected ProgramRepository programRepository;

	@Autowired
	protected MediaFileRepository mediaFileRepository;

	@Autowired
	protected XMLGenerator xmlGenerator;

	@Value("${spring.profiles.active:dev}")
	protected String profiles;

	@Value("${injection.suffix.lowercase:false}")
	protected boolean suffixLowercase;

	@Value("${injection.separate.char:}")
	protected String separateChar;

	@Value("${site.platformId:}")
	protected String platformId;

	@Override
	public AbstractRepository<SendTask, Long> getRepository() {
		return sendTaskRepository;
	}

	public boolean genCreateMediaFileTask(ADI adi, Program program,
			InjectionObject programInjectionObject, MediaFile mediaFile,
			InjectionObject mediaFileInjectionObject) {
		// 1.生成媒体内容操作对象
		MovieBean mediaFileBean = genMovieBean(program, mediaFile,
				mediaFileInjectionObject);
		mediaFileBean.setAction(InjectionActionTypeEnum.CREATE.getValue());
		adi.getObjects().add(mediaFileBean);

		if (mediaFileInjectionObject.getInjectionStatus() != InjectionStatusEnum.INJECTION_ING
				.getKey()) {
			// 设置对象分发状态为分发中
			mediaFileInjectionObject
					.setInjectionStatus(InjectionStatusEnum.INJECTION_ING
							.getKey());
			injectionObjectRepository.save(mediaFileInjectionObject);
			injectionService.updateInjectionStatus(mediaFile);
		}

		// 2.生成媒体内容映射对象
		MappingBean mappingBean = genMappingBean(program,
				programInjectionObject, mediaFile, mediaFileInjectionObject);
		mappingBean.setAction(InjectionActionTypeEnum.CREATE.getValue());
		adi.getMappings().add(mappingBean);

		return true;
	}

	public boolean genUpdateMediaFileTask(ADI adi, Program program,
			InjectionObject programInjectionObject, MediaFile mediaFile,
			InjectionObject mediaFileInjectionObject) {
		// 1.生成媒体内容操作对象
		MovieBean mediaFileBean = genMovieBean(program, mediaFile,
				mediaFileInjectionObject);
		mediaFileBean.setAction(InjectionActionTypeEnum.UPDATE.getValue());
		adi.getObjects().add(mediaFileBean);

		if (mediaFileInjectionObject.getInjectionStatus() != InjectionStatusEnum.INJECTION_ING
				.getKey()) {
			// 设置对象分发状态为分发中
			mediaFileInjectionObject
					.setInjectionStatus(InjectionStatusEnum.INJECTION_ING
							.getKey());
			injectionObjectRepository.save(mediaFileInjectionObject);
			injectionService.updateInjectionStatus(mediaFile);
		}

		// 2.生成媒体内容映射对象
		MappingBean mappingBean = genMappingBean(program,
				programInjectionObject, mediaFile, mediaFileInjectionObject);
		mappingBean.setAction(InjectionActionTypeEnum.UPDATE.getValue());
		adi.getMappings().add(mappingBean);

		return true;
	}

	public boolean genDeleteMediaFileTask(ADI adi, Program program,
			InjectionObject programInjectionObject, MediaFile mediaFile,
			InjectionObject mediaFileInjectionObject) {
		// 1.生成媒体内容操作对象
		MovieBean mediaFileBean = genMovieBean(program, mediaFile,
				mediaFileInjectionObject);
		mediaFileBean.setAction(InjectionActionTypeEnum.DELETE.getValue());
		adi.getObjects().add(mediaFileBean);

		if (mediaFileInjectionObject.getInjectionStatus() != InjectionStatusEnum.RECOVERY_ING
				.getKey()) {
			// 设置对象分发状态为回收中
			mediaFileInjectionObject
					.setInjectionStatus(InjectionStatusEnum.RECOVERY_ING
							.getKey());
			injectionObjectRepository.save(mediaFileInjectionObject);
			injectionService.updateInjectionStatus(mediaFile);
		}
		return true;
	}

	public MovieBean genMovieBean(Program program, MediaFile mediaFile,
			InjectionObject mediaFileInjectionObject) {
		MovieBean movieBean = new MovieBean();
		movieBean.setID("" + mediaFile.getId());
		movieBean.setCode("" + mediaFile.getId());
		movieBean.setPartnerItemCode(mediaFileInjectionObject
				.getPartnerItemCode());
		movieBean.setType(String.valueOf(mediaFile.getType()));
		if (StringUtils.isNotEmpty(mediaFile.getFilePath())) {
			if (suffixLowercase) {
				movieBean.setFileURL(AdminGlobal
						.getVideoFtpPath(getOutputFilePath(mediaFile
								.getFilePath())));
			} else {
				movieBean.setFileURL(AdminGlobal.getVideoFtpPath(mediaFile
						.getFilePath()));
			}
			movieBean.setPlayURL(AdminGlobal.getVideoWebPath(mediaFile
					.getFilePath()));
		}
		movieBean.setFileMd5(StringUtils.trimToEmpty(mediaFile.getFileMd5()));
		if (mediaFile.getFileSize() != null) {
			movieBean.setFileSize(String.valueOf(mediaFile.getFileSize()));
		}
		if (program != null) {
			movieBean.setName(program.getName());

			if (program.getDuration() != null) {
				movieBean.setDuration(String.valueOf(program.getDuration()));
			}
		}
		movieBean.setDefinitionFlag("HD");
		movieBean.setMediaSpec(mediaFile.getMediaSpec());
		if (mediaFile.getTemplateId() != null && mediaFile.getTemplateId() > 0) {
			MediaTemplate mediaTemplate = AppGlobal.mediaTemplateMap
					.get(mediaFile.getTemplateId());
			if (mediaTemplate != null) {
				// TS-CBR-H264-8000-1080P-25-MP2-128
				String mediaSpec = mediaTemplate.getvFormat() + "-"
						+ mediaTemplate.getvBitrateMode() + "-"
						+ mediaTemplate.getvCodec() + "-"
						+ mediaTemplate.getvBitrate() + "-"
						+ mediaTemplate.getvResolution() + "-"
						+ mediaTemplate.getvFramerate() + "-"
						+ mediaTemplate.getaCodec() + "-"
						+ mediaTemplate.getaBitrate();
				movieBean.setMediaSpec(mediaSpec);
				movieBean.setvBitrate(mediaTemplate.getvBitrateMode());
				movieBean.setDefinitionFlag(mediaTemplate.getDefinition());
			}
		}
		// jsyd易视腾需要知道分发的平台Id(平台Id易视腾提供)
		movieBean.setPlatformId(platformId);

		return movieBean;
	}

	public MappingBean genMappingBean(Program program,
			InjectionObject programInjectionObject, MediaFile mediaFile,
			InjectionObject mediaFileInjectionObject) {
		MappingBean mappingBean = new MappingBean();

		mappingBean.setElementType(InjectionItemTypeEnum.MOVIE.getValue());
		mappingBean.setElementID("" + mediaFile.getId());
		mappingBean.setElementCode("" + mediaFile.getId());
		mappingBean.setPartnerItemCode(mediaFileInjectionObject
				.getPartnerItemCode());

		mappingBean.setParentType(InjectionItemTypeEnum.PROGRAM.getValue());
		mappingBean.setParentID("" + program.getId());
		mappingBean.setParentCode("" + program.getId());
		mappingBean.setParentPartnerItemCode(programInjectionObject
				.getPartnerItemCode());

		return mappingBean;
	}

	public static String getOutputFilePath(String source) {
		String str = StringUtils.substringAfterLast(source, ".");
		return source.replace("." + str, "." + str.toLowerCase());
	}

	/**
	 * 生成xml
	 * 
	 * @param sendTask
	 * @param platform
	 * @param adi
	 */
	public void genXML(SendTask sendTask, InjectionPlatform platform, ADI adi) {
		try {
			getTransformCode(sendTask, platform, adi);

			String operationObjectIds = getOperationObjectIds(adi);
			sendTask.setOperationObjectIds(operationObjectIds);

			String filePath = xmlGenerator.genXML(sendTask, platform, adi);
			sendTask.setCmdFileURL(AdminGlobal.getXmlFtpPath(filePath));
			sendTask.setRequestXmlFilePath(filePath);
			// sendTask.setRequestXmlFileContent(IOUtils.readTxtFile(AdminGlobal
			// .getXmlUploadPath() + filePath));
			sendTask.setRequestXmlFileContent(getXmlFileContent(adi));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("Generate XML error.");
		}
	}

	public String getOperationObjectIds(ADI adi) {
		StringBuffer sb = new StringBuffer();
		if (adi != null && adi.getObjects() != null) {
			for (ObjectBean objectBean : adi.getObjects()) {
				InjectionItemTypeEnum typeEnum = InjectionItemTypeEnum
						.getEnumByValue(objectBean.getElementType());
				InjectionActionTypeEnum actionEnum = InjectionActionTypeEnum
						.getEnumByValue(objectBean.getAction());
				if (typeEnum == null || actionEnum == null) {
					continue;
				}
				sb.append("o:" + typeEnum.getValue() + ":"
						+ actionEnum.getValue() + ":" + objectBean.getID()
						+ ":" + objectBean.getCode() + ";");
			}
			for (MappingBean mappingBean : adi.getMappings()) {
				InjectionItemTypeEnum typeEnum = InjectionItemTypeEnum
						.getEnumByValue(mappingBean.getParentType());
				InjectionActionTypeEnum actionEnum = InjectionActionTypeEnum
						.getEnumByValue(mappingBean.getAction());
				if (typeEnum == null || actionEnum == null) {
					continue;
				}
				if (mappingBean.getRelId() != null) {
					sb.append("m:" + typeEnum.getValue() + ":"
							+ actionEnum.getValue() + ":"
							+ mappingBean.getElementID() + ":"
							+ mappingBean.getElementCode() + ":"
							+ mappingBean.getParentID() + ":"
							+ mappingBean.getParentCode() + ":"
							+ mappingBean.getRelId() + ";");
				}
			}
		}
		return sb.toString();
	}

	public String getXmlFileContent(ADI adi) {
		StringBuffer sb = new StringBuffer();
		if (adi != null && adi.getObjects() != null) {
			for (ObjectBean objectBean : adi.getObjects()) {
				InjectionItemTypeEnum typeEnum = InjectionItemTypeEnum
						.getEnumByValue(objectBean.getElementType());
				InjectionActionTypeEnum actionEnum = InjectionActionTypeEnum
						.getEnumByValue(objectBean.getAction());
				if (typeEnum == null || actionEnum == null) {
					continue;
				}
				sb.append("ElementType=\"" + typeEnum.getValue() + "\"" + ":"
						+ "Action=\"" + actionEnum.getValue() + "\"" + ":"
						+ objectBean.getID() + ":" + objectBean.getCode() + ":"
						+ objectBean.getName() + ";");
			}
		}
		return sb.toString();
	}

	public void getTransformCode(SendTask sendTask, InjectionPlatform platform,
			ADI adi) {
		if (adi == null) {
			return;
		}
		List<ObjectBean> objects = adi.getObjects();
		if (objects != null) {
			for (ObjectBean objectBean : objects) {
				if (StringUtils.isNotEmpty(objectBean.getPartnerItemCode())) {
					objectBean.setCode(objectBean.getPartnerItemCode());
				} else {
					objectBean.setCode(fill(sendTask, platform,
							objectBean.getElementType(), objectBean.getCode()));
				}
				if (objectBean instanceof CategoryBean) {
					CategoryBean categoryBean = (CategoryBean) objectBean;
					if (StringUtils.isNotEmpty(categoryBean
							.getParentPartnerItemCode())) {
						categoryBean.setParentCode(categoryBean
								.getParentPartnerItemCode());
					} else {
						categoryBean.setParentCode(fill(sendTask, platform,
								objectBean.getElementType(),
								categoryBean.getParentCode()));
					}
				} else if (objectBean instanceof ScheduleBean) {
					ScheduleBean scheduleBean = (ScheduleBean) objectBean;
					if (StringUtils.isNotEmpty(scheduleBean
							.getParentPartnerItemCode())) {
						scheduleBean.setChannelCode(scheduleBean
								.getParentPartnerItemCode());
					} else {
						scheduleBean.setChannelCode(fill(sendTask, platform,
								objectBean.getElementType(),
								scheduleBean.getChannelCode()));
					}
				}
			}
		}

		List<MappingBean> mapping = adi.getMappings();
		if (mapping != null) {
			for (MappingBean mappingBean : mapping) {
				if (StringUtils.isNotEmpty(mappingBean.getPartnerItemCode())) {
					mappingBean
							.setElementCode(mappingBean.getPartnerItemCode());
				} else {
					mappingBean.setElementCode(fill(sendTask, platform,
							mappingBean.getElementType(),
							mappingBean.getElementCode()));
				}
				if (StringUtils.isNotEmpty(mappingBean
						.getParentPartnerItemCode())) {
					mappingBean.setParentCode(mappingBean
							.getParentPartnerItemCode());
				} else {
					mappingBean.setParentCode(fill(sendTask, platform,
							mappingBean.getParentType(),
							mappingBean.getParentCode()));
				}
			}
		}
	}

	public String fill(SendTask sendTask, InjectionPlatform platform,
			String elementType, String code) {
		if (StringUtils.isEmpty(code)) {
			return "";
		}
		for (InjectionItemTypeEnum objEnum : InjectionItemTypeEnum.values()) {
			if (objEnum.getValue().equalsIgnoreCase(elementType)) {
				return fill(sendTask, platform, objEnum.getKey(), code);
			}
		}
		return code;
	}

	public String fill(SendTask sendTask, InjectionPlatform platform, int type,
			String code) {
		return StringUtils.trimToEmpty(platform.getCodePrefix()) + ""
				+ fillType(type) + fillCode(code);
	}

	public String fillType(int type) {
		String str = "00000000" + type;
		return str.substring(str.length() - 8, str.length());
	}

	public String fillCode(String code) {
		String str = "0000000000000000" + code;
		return str.substring(str.length() - 16, str.length());
	}

	public final static String[] DEFAULT_SEPARATE_CHAR_ARR = { ";", ",", "，",
			"\\|", "｜", " " };

	/**
	 * 获取以逗号分隔的字符串
	 * 
	 * @param source
	 * @return
	 */
	public String getSeparateString(final String source) {
		if (StringUtils.isEmpty(separateChar) || StringUtils.isEmpty(source)) {
			return source;
		}
		String a = getSeparateString(source, DEFAULT_SEPARATE_CHAR_ARR, ",");
		String b = a.replaceAll(",", separateChar);
		return b;
	}

	/**
	 * 获取以逗号分隔的字符串
	 * 
	 * @param source
	 * @return
	 */
	public static String getCommaSeparateString(final String source) {
		return getSeparateString(source, DEFAULT_SEPARATE_CHAR_ARR, ",");
	}

	public static String getSeparateString(final String source,
			final String[] separateCharArr, String separateChar) {
		if (StringUtils.isEmpty(source)) {
			return source;
		}
		String result = source;
		for (String c : separateCharArr) {
			result = result.replaceAll(c, separateChar);
		}
		StringBuffer br = new StringBuffer();
		String arr[] = result.split(separateChar);
		for (int i = 0; i < arr.length; i++) {
			if (StringUtils.isNotEmpty(arr[i])) {
				br.append(arr[i]);
				if (i < arr.length - 1) {
					br.append(separateChar);
				}
			}
		}
		return br.toString();
	}
}