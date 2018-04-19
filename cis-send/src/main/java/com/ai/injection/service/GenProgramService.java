package com.ai.injection.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.AdminGlobal;
import com.ai.cms.injection.bean.ADI;
import com.ai.cms.injection.bean.MappingBean;
import com.ai.cms.injection.bean.PictureBean;
import com.ai.cms.injection.bean.ProgramBean;
import com.ai.cms.injection.bean.SeriesBean;
import com.ai.cms.injection.entity.InjectionObject;
import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.entity.SendTask;
import com.ai.cms.injection.enums.InjectionActionTypeEnum;
import com.ai.cms.injection.enums.InjectionDirectionEnum;
import com.ai.cms.injection.enums.InjectionItemTypeEnum;
import com.ai.cms.injection.enums.InjectionStatusEnum;
import com.ai.cms.injection.enums.PlatformTypeEnum;
import com.ai.cms.injection.enums.SendTaskStatusEnum;
import com.ai.cms.media.entity.MediaFile;
import com.ai.cms.media.entity.MediaImage;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.Series;
import com.ai.common.enums.ContentTypeEnum;
import com.ai.common.enums.MediaImageTypeEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.ProgramTypeEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.exception.DataException;
import com.ai.common.utils.BeanInfoUtil;

@Service
@Transactional(value = "slaveTransactionManager", readOnly = false)
public class GenProgramService extends GenCommonService {

	@Autowired
	private GenSeriesService genSeriesService;

	/**
	 * 生成节目任务
	 * 
	 * @param sendTask
	 * @param platform
	 * @return
	 */
	public boolean genTask(SendTask sendTask, InjectionPlatform platform) {
		// 1.获取对象,检查状态
		Program program = programRepository.findOne(sendTask.getItemId());
		if (program == null) {
			throw new DataException("节目不存在！");
		}
		InjectionObject programInjectionObject = injectionService
				.getAndNewInjectionObject(program, sendTask.getPlatformId(),
						sendTask.getCategory());

		// 如是业务系统，需要检查依赖平台是否都分发成功，如都分发成功才向业务系统发送
		if ((sendTask.getAction() == InjectionActionTypeEnum.CREATE.getKey() || sendTask
				.getAction() == InjectionActionTypeEnum.UPDATE.getKey())
				&& platform.getDirection() == InjectionDirectionEnum.SEND
						.getKey()
				&& platform.getType() == PlatformTypeEnum.APP_SYSTEM.getKey()
				&& StringUtils.isNotEmpty(platform.getDependPlatformId())) {
			List<InjectionObject> injectionObjectListAll = injectionService
					.findInjectionObjectList(InjectionItemTypeEnum.PROGRAM,
							program.getId());
			for (String dependPlatformId : platform.getDependPlatformId()
					.split(",")) {
				for (InjectionObject injectionObject : injectionObjectListAll) {
					if (dependPlatformId.equals(""
							+ injectionObject.getPlatformId())
							&& injectionObject.getInjectionStatus() != InjectionStatusEnum.INJECTION_SUCCESS
									.getKey()) {
						// 该依赖平台没有分发成功
						// 等待5分钟
						sendTask.setNextCheckTime(DateUtils.addMinutes(
								new Date(), 5));
						return false;
					}
				}
			}
			if (StringUtils.isNotEmpty(sendTask.getSubItemId())) {// 节目依赖平台都分发成功，检查媒体内容是否都分发成功
				for (String subItemId : sendTask.getSubItemId().split(",")) {
					injectionObjectListAll = injectionService
							.findInjectionObjectList(
									InjectionItemTypeEnum.MOVIE,
									Long.valueOf(subItemId));
					for (String dependPlatformId : platform
							.getDependPlatformId().split(",")) {
						for (InjectionObject injectionObject : injectionObjectListAll) {
							if (dependPlatformId.equals(""
									+ injectionObject.getPlatformId())
									&& injectionObject.getInjectionStatus() != InjectionStatusEnum.INJECTION_SUCCESS
											.getKey()) {
								// 该依赖平台没有分发成功
								// 等待5分钟
								sendTask.setNextCheckTime(DateUtils.addMinutes(
										new Date(), 5));
								return false;
							}
						}
					}
				}
			}
		}
		// 2.设置基本属性
		String correlateId = StringUtils.trimToEmpty(platform
				.getCorrelatePrefix()) + sendTask.getId();
		sendTask.setCorrelateId(correlateId);

		// 3.生成节目任务
		ADI adi = new ADI();
		genProgramTask(sendTask, platform, adi, program, programInjectionObject);

		// 4.生成xml
		genXML(sendTask, platform, adi);

		// 5.保存任务
		sendTask.setStatus(SendTaskStatusEnum.WAIT.getKey());
		sendTaskRepository.save(sendTask);
		return true;
	}

	public void genProgramTask(SendTask sendTask, InjectionPlatform platform,
			ADI adi, Program program, InjectionObject programInjectionObject) {
		if (sendTask.getAction() == InjectionActionTypeEnum.CREATE.getKey()) {// 发送增加命令
			genCreateProgramTask(sendTask, platform, adi, program,
					programInjectionObject);
		} else if (sendTask.getAction() == InjectionActionTypeEnum.UPDATE
				.getKey()) {// 发送修改命令
			genUpdateProgramTask(sendTask, platform, adi, program,
					programInjectionObject);
		} else if (sendTask.getAction() == InjectionActionTypeEnum.DELETE
				.getKey()) {// 发送删除命令
			genDeleteProgramTask(sendTask, platform, adi, program,
					programInjectionObject);
		}
	}

	private void genCreateProgramTask(SendTask sendTask,
			InjectionPlatform platform, ADI adi, Program program,
			InjectionObject programInjectionObject) {
		// 1.生成节目操作对象
		ProgramBean programBean = genProgramBean(sendTask, platform, program,
				programInjectionObject);
		programBean.setAction(InjectionActionTypeEnum.CREATE.getValue());
		adi.getObjects().add(programBean);

		if (programInjectionObject.getInjectionStatus() != InjectionStatusEnum.INJECTION_ING
				.getKey()) {
			// 设置对象分发状态为分发中
			programInjectionObject
					.setInjectionStatus(InjectionStatusEnum.INJECTION_ING
							.getKey());
			injectionObjectRepository.save(programInjectionObject);
			injectionService.updateInjectionStatus(platform, program, false);
		}

		// 2.判断单片是否要包装成剧头
		// 如果推送的是单片（比如电影），那还是要把此program分装到某个series下
		if (program.getType() == ProgramTypeEnum.MOVIE.getKey()
				&& platform.getNeedPackingProgram() == YesNoEnum.YES.getKey()) {// 单集类型
			// a. 根据节目虚拟剧头
			Series virtualSeries = new Series();
			BeanInfoUtil.bean2bean(program, virtualSeries, Program.METADATA
					+ "," + Program.POSTER + "," + Series.METADATA_OTHER);
			virtualSeries.setId(program.getId() + 9000000000l);
			InjectionObject virtualSeriesInjectionObject = new InjectionObject(
					null, null);

			// b.生成剧头操作对象
			SeriesBean seriesBean = genSeriesService.genSeriesBean(sendTask,
					platform, virtualSeries, virtualSeriesInjectionObject);
			seriesBean.setVolumnCount("" + 1);
			seriesBean.setAction(InjectionActionTypeEnum.CREATE.getValue());
			adi.getObjects().add(0, seriesBean);

			// c. 生成剧头与节目映像
			MappingBean mappingBean = genCreateSeriesMappingBean(virtualSeries,
					virtualSeriesInjectionObject, program,
					programInjectionObject);
			adi.getMappings().add(mappingBean);
		}

		// 3.生成海报操作对象
		if (platform.getNeedImageObject() == YesNoEnum.YES.getKey()) {
			if (StringUtils.isNotEmpty(program.getImage1())) {
				// a.生成海报操作对象
				PictureBean pictureBean = genCreatePictureBean(program,
						programInjectionObject, program.getImage1(),
						1000000000l);
				adi.getObjects().add(pictureBean);

				// b.生成海报映射对象
				MappingBean mappingBean = genCreatePictureMappingBean(program,
						programInjectionObject, 1000000000l, 1, 1);
				adi.getMappings().add(mappingBean);
			}
			if (StringUtils.isNotEmpty(program.getImage2())) {
				// a.生成海报操作对象
				PictureBean pictureBean = genCreatePictureBean(program,
						programInjectionObject, program.getImage2(),
						2000000000l);
				adi.getObjects().add(pictureBean);

				// b.生成海报映射对象
				MappingBean mappingBean = genCreatePictureMappingBean(program,
						programInjectionObject, 2000000000l, 1, 2);
				adi.getMappings().add(mappingBean);
			}

			if (StringUtils.isNotEmpty(program.getImage3())) {
				// a.生成海报操作对象
				PictureBean pictureBean = genCreatePictureBean(program,
						programInjectionObject, program.getImage3(),
						3000000000l);
				adi.getObjects().add(pictureBean);

				// b.生成海报映射对象
				MappingBean mappingBean = genCreatePictureMappingBean(program,
						programInjectionObject, 3000000000l, 1, 3);
				adi.getMappings().add(mappingBean);
			}

			if (StringUtils.isNotEmpty(program.getImage4())) {
				// a.生成海报操作对象
				PictureBean pictureBean = genCreatePictureBean(program,
						programInjectionObject, program.getImage4(),
						4000000000l);
				adi.getObjects().add(pictureBean);

				// b.生成海报映射对象
				MappingBean mappingBean = genCreatePictureMappingBean(program,
						programInjectionObject, 4000000000l, 1, 4);
				adi.getMappings().add(mappingBean);
			}

			// 生成剧照
			List<MediaImage> mediaImageList = mediaImageRepository
					.findByProgramIdAndType(program.getId(),
							MediaImageTypeEnum.STILLS.getKey());
			for (MediaImage mediaImage : mediaImageList) {
				if (StringUtils.isNotEmpty(mediaImage.getFilePath())) {
					// a.生成剧照操作对象
					PictureBean pictureBean = genCreatePictureBean(mediaImage);
					adi.getObjects().add(pictureBean);

					// b.生成剧照映射对象
					MappingBean mappingBean = genCreatePictureMappingBean(
							program, programInjectionObject, mediaImage);
					adi.getMappings().add(mappingBean);
				}
			}
		}

		// 4.获取所有的媒体内容
		genMediaFileData(sendTask, platform, adi, program,
				programInjectionObject);

		// 5.生成剧头节目映射
		if (program.getType() == ProgramTypeEnum.TV.getKey()
				&& program.getSeriesId() != null) {// 多集类型
			Series series = seriesRepository.findOne(program.getSeriesId());
			if (series != null) {
				InjectionObject seriesInjectionObject = injectionService
						.getAndNewInjectionObject(series,
								sendTask.getPlatformId(),
								sendTask.getCategory());
				MappingBean mappingBean = genCreateSeriesMappingBean(series,
						seriesInjectionObject, program, programInjectionObject);
				adi.getMappings().add(mappingBean);
			}
		}
	}

	private void genUpdateProgramTask(SendTask sendTask,
			InjectionPlatform platform, ADI adi, Program program,
			InjectionObject programInjectionObject) {
		// 1.生成节目操作对象
		ProgramBean programBean = genProgramBean(sendTask, platform, program,
				programInjectionObject);
		programBean.setAction(InjectionActionTypeEnum.UPDATE.getValue());
		adi.getObjects().add(programBean);

		if (programInjectionObject.getInjectionStatus() != InjectionStatusEnum.INJECTION_ING
				.getKey()) {
			// 设置对象分发状态为分发中
			programInjectionObject
					.setInjectionStatus(InjectionStatusEnum.INJECTION_ING
							.getKey());
			injectionObjectRepository.save(programInjectionObject);
			injectionService.updateInjectionStatus(platform, program, false);
		}

		// 2.判断单片是否要包装成剧头
		// 如果推送的是单片（比如电影），那还是要把此program分装到某个series下
		if (program.getType() == ProgramTypeEnum.MOVIE.getKey()
				&& platform.getNeedPackingProgram() == YesNoEnum.YES.getKey()) {// 单集类型
			// 1. 根据节目虚拟剧头
			Series virtualSeries = new Series();
			BeanInfoUtil.bean2bean(program, virtualSeries, Program.METADATA
					+ "," + Program.POSTER + "," + Series.METADATA_OTHER);
			virtualSeries.setId(program.getId() + 9000000000l);
			InjectionObject virtualSeriesInjectionObject = new InjectionObject(
					null, null);

			// 2.生成剧头操作对象
			SeriesBean seriesBean = genSeriesService.genSeriesBean(sendTask,
					platform, virtualSeries, virtualSeriesInjectionObject);
			seriesBean.setVolumnCount("" + 1);
			seriesBean.setAction(InjectionActionTypeEnum.UPDATE.getValue());
			adi.getObjects().add(0, seriesBean);

			// 3. 生成剧头与节目映像
			MappingBean mappingBean = genCreateSeriesMappingBean(virtualSeries,
					virtualSeriesInjectionObject, program,
					programInjectionObject);
			adi.getMappings().add(mappingBean);
		}

		// 3.生成海报操作对象
		if (platform.getNeedImageObject() == YesNoEnum.YES.getKey()) {
			if (StringUtils.isNotEmpty(program.getImage1())) {
				// a.生成海报操作对象
				PictureBean pictureBean = genCreatePictureBean(program,
						programInjectionObject, program.getImage1(),
						1000000000l);
				adi.getObjects().add(pictureBean);

				// b.生成海报映射对象
				MappingBean mappingBean = genCreatePictureMappingBean(program,
						programInjectionObject, 1000000000l, 1, 1);
				adi.getMappings().add(mappingBean);
			}

			if (StringUtils.isNotEmpty(program.getImage2())) {
				// a.生成海报操作对象
				PictureBean pictureBean = genCreatePictureBean(program,
						programInjectionObject, program.getImage2(),
						2000000000l);
				adi.getObjects().add(pictureBean);

				// b.生成海报映射对象
				MappingBean mappingBean = genCreatePictureMappingBean(program,
						programInjectionObject, 2000000000l, 1, 2);
				adi.getMappings().add(mappingBean);
			}

			if (StringUtils.isNotEmpty(program.getImage3())) {
				// a.生成海报操作对象
				PictureBean pictureBean = genCreatePictureBean(program,
						programInjectionObject, program.getImage3(),
						3000000000l);
				adi.getObjects().add(pictureBean);

				// b.生成海报映射对象
				MappingBean mappingBean = genCreatePictureMappingBean(program,
						programInjectionObject, 3000000000l, 1, 3);
				adi.getMappings().add(mappingBean);
			}

			if (StringUtils.isNotEmpty(program.getImage4())) {
				// a.生成海报操作对象
				PictureBean pictureBean = genCreatePictureBean(program,
						programInjectionObject, program.getImage4(),
						4000000000l);
				adi.getObjects().add(pictureBean);

				// b.生成海报映射对象
				MappingBean mappingBean = genCreatePictureMappingBean(program,
						programInjectionObject, 4000000000l, 1, 4);
				adi.getMappings().add(mappingBean);
			}

			// 生成剧照
			List<MediaImage> mediaImageList = mediaImageRepository
					.findByProgramIdAndType(program.getId(),
							MediaImageTypeEnum.STILLS.getKey());
			for (MediaImage mediaImage : mediaImageList) {
				if (StringUtils.isNotEmpty(mediaImage.getFilePath())) {
					// a.生成剧照操作对象
					PictureBean pictureBean = genCreatePictureBean(mediaImage);
					adi.getObjects().add(pictureBean);

					// b.生成剧照映射对象
					MappingBean mappingBean = genCreatePictureMappingBean(
							program, programInjectionObject, mediaImage);
					adi.getMappings().add(mappingBean);
				}
			}
		}

		// 4.获取所有的媒体内容
		genMediaFileData(sendTask, platform, adi, program,
				programInjectionObject);

		// 5.生成剧头节目映射
		if (program.getType() == ProgramTypeEnum.TV.getKey()
				&& program.getSeriesId() != null) {// 多集类型
			Series series = seriesRepository.findOne(program.getSeriesId());
			if (series != null) {
				InjectionObject seriesInjectionObject = injectionService
						.getAndNewInjectionObject(series,
								sendTask.getPlatformId(),
								sendTask.getCategory());
				MappingBean mappingBean = genCreateSeriesMappingBean(series,
						seriesInjectionObject, program, programInjectionObject);
				adi.getMappings().add(mappingBean);
			}
		}
	}

	private void genDeleteProgramTask(SendTask sendTask,
			InjectionPlatform platform, ADI adi, Program program,
			InjectionObject programInjectionObject) {
		// 1.生成节目操作对象
		ProgramBean programBean = genProgramBean(sendTask, platform, program,
				programInjectionObject);
		programBean.setAction(InjectionActionTypeEnum.DELETE.getValue());
		adi.getObjects().add(programBean);

		if (programInjectionObject.getInjectionStatus() != InjectionStatusEnum.RECOVERY_ING
				.getKey()) {
			// 设置对象分发状态为回收中
			programInjectionObject
					.setInjectionStatus(InjectionStatusEnum.RECOVERY_ING
							.getKey());
			injectionObjectRepository.save(programInjectionObject);
			injectionService.updateInjectionStatus(platform, program, false);
		}

		// 2.判断单片是否要包装成剧头
		// 如果推送的是单片（比如电影），那还是要把此program分装到某个series下
		if (program.getType() == ProgramTypeEnum.MOVIE.getKey()
				&& platform.getNeedPackingProgram() == YesNoEnum.YES.getKey()) {// 单集类型
			// 1. 根据节目虚拟剧头
			Series virtualSeries = new Series();
			BeanInfoUtil.bean2bean(program, virtualSeries, Program.METADATA
					+ "," + Program.POSTER + "," + Series.METADATA_OTHER);
			virtualSeries.setId(program.getId() + 9000000000l);
			InjectionObject virtualSeriesInjectionObject = new InjectionObject(
					null, null);

			// 2.生成剧头操作对象
			SeriesBean seriesBean = genSeriesService.genSeriesBean(sendTask,
					platform, virtualSeries, virtualSeriesInjectionObject);
			seriesBean.setAction(InjectionActionTypeEnum.DELETE.getValue());
			adi.getObjects().add(seriesBean);
		}

		// 3.获取所有的媒体内容
		genMediaFileData(sendTask, platform, adi, program,
				programInjectionObject);
	}

	private void genMediaFileData(SendTask sendTask,
			InjectionPlatform platform, ADI adi, Program program,
			InjectionObject programInjectionObject) {
		if (StringUtils.isEmpty(sendTask.getSubItemId())) {
			return;
		}
		for (String id : sendTask.getSubItemId().split(",")) {
			MediaFile mediaFile = mediaFileRepository.findOne(Long.valueOf(id));
			if (mediaFile != null) {
				InjectionObject mediaFileInjectionObject = injectionService
						.getAndNewInjectionObject(mediaFile,
								programInjectionObject.getPlatformId(),
								programInjectionObject.getCategory());
				if (sendTask.getAction() == InjectionActionTypeEnum.CREATE
						.getKey()) {
					genCreateMediaFileTask(sendTask, platform, adi, program,
							programInjectionObject, mediaFile,
							mediaFileInjectionObject);
				} else if (sendTask.getAction() == InjectionActionTypeEnum.UPDATE
						.getKey()) {
					genUpdateMediaFileTask(sendTask, platform, adi, program,
							programInjectionObject, mediaFile,
							mediaFileInjectionObject);
				} else if (sendTask.getAction() == InjectionActionTypeEnum.DELETE
						.getKey()) {
					genDeleteMediaFileTask(sendTask, platform, adi, program,
							programInjectionObject, mediaFile,
							mediaFileInjectionObject);
				}
			}
		}
	}

	private ProgramBean genProgramBean(SendTask sendTask,
			InjectionPlatform platform, Program program,
			InjectionObject programInjectionObject) {
		ProgramBean programBean = new ProgramBean();

		programBean.setID("" + program.getId());
		programBean.setCode("" + program.getId());
		programBean.setPartnerItemCode(programInjectionObject
				.getPartnerItemCode());

		programBean.setName(program.getName());

		programBean.setOriginalName(program.getName());

		if (StringUtils.isNotEmpty(program.getOrderNumber())) {
			programBean.setOrderNumber(program.getOrderNumber());
		}
		if (StringUtils.isNotEmpty(program.getSortName())) {
			programBean.setSortName(program.getSortName());
		}
		if (StringUtils.isNotEmpty(program.getSearchName())) {
			programBean.setSearchName(program.getSearchName());
		}

		if (StringUtils.isNotEmpty(program.getDirector())) {
			programBean.setDirector(getSeparateString(sendTask, platform,
					program.getDirector()));
		}
		if (StringUtils.isNotEmpty(program.getActor())) {
			programBean.setActorDisplay(getSeparateString(sendTask, platform,
					program.getActor()));
		}
		if (StringUtils.isNotEmpty(program.getYear())) {
			programBean.setReleaseYear(program.getYear());
		}
		if (StringUtils.isNotEmpty(program.getArea())) {
			programBean.setOriginalCountry(program.getArea());
		}
		if (StringUtils.isNotEmpty(program.getLanguage())) {
			programBean.setLanguage(program.getLanguage());
		}

		if (program.getOrgAirDate() != null) {
			try {
				String date = DateFormatUtils.format(program.getOrgAirDate(),
						"yyyyMMdd");
				programBean.setOrgAirDate(date);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (program.getLicensingWindowStart() != null) {
			try {
				String date = DateFormatUtils.format(
						program.getLicensingWindowStart(), "yyyyMMddHHmmss");
				programBean.setLicensingWindowStart(date);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (program.getLicensingWindowEnd() != null) {
			try {
				String date = DateFormatUtils.format(
						program.getLicensingWindowEnd(), "yyyyMMddHHmmss");
				programBean.setLicensingWindowEnd(date);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

		if (program.getEpisodeIndex() != null) {
			programBean.setEpisodeIndex("" + program.getEpisodeIndex());
		}
		if (program.getDuration() != null) {
			programBean.setDuration("" + program.getDuration());
		}
		if (program.getRating() != null) {
			programBean.setRating(String.valueOf(program.getRating()));
		}
		programBean.setSeriesFlag(program.getType() == ProgramTypeEnum.MOVIE
				.getKey() ? "0" : "1");
		programBean.setSourceType(""
				+ (program.getSourceType() == null ? "1" : program
						.getSourceType()));

		if (program.getContentType() != null) {
			programBean.setType(ContentTypeEnum.getEnumByKey(
					program.getContentType()).getValue());
		}
		if (StringUtils.isNotEmpty(program.getTag())) {
			programBean.setTags(getSeparateString(sendTask, platform,
					program.getTag()));
		}
		if (StringUtils.isNotEmpty(program.getViewpoint())) {
			programBean.setViewpoint(program.getViewpoint());
		}
		if (StringUtils.isNotEmpty(program.getInfo())) {
			programBean.setDescription(program.getInfo());
		}
		programBean.setStatus(program.getStatus() == OnlineStatusEnum.OFFLINE
				.getKey() ? "0" : "1");

		if (StringUtils.isNotEmpty(program.getImage1())) {
			programBean.setImage1(AdminGlobal.getImageWebPath(program
					.getImage1()));
		}
		if (StringUtils.isNotEmpty(program.getImage2())) {
			programBean.setImage2(AdminGlobal.getImageWebPath(program
					.getImage2()));
		}

		if (StringUtils.isNotEmpty(program.getKpeople())) {
			programBean.setKpeople(getSeparateString(sendTask, platform,
					program.getKpeople()));
		}
		if (StringUtils.isNotEmpty(program.getScriptWriter())) {
			programBean.setScriptWriter(getSeparateString(sendTask, platform,
					program.getScriptWriter()));
		}
		if (StringUtils.isNotEmpty(program.getCompere())) {
			programBean.setCompere(getSeparateString(sendTask, platform,
					program.getCompere()));
		}
		if (StringUtils.isNotEmpty(program.getGuest())) {
			programBean.setGuest(getSeparateString(sendTask, platform,
					program.getGuest()));
		}
		if (StringUtils.isNotEmpty(program.getReporter())) {
			programBean.setReporter(getSeparateString(sendTask, platform,
					program.getReporter()));
		}
		if (StringUtils.isNotEmpty(program.getIncharge())) {
			programBean.setOPIncharge(program.getIncharge());
		}
		if (StringUtils.isNotEmpty(program.getCopyRight())) {
			programBean.setCopyRight(program.getCopyRight());
		}
		if (StringUtils.isNotEmpty(program.getCpCode())) {
			programBean.setContentProvider(program.getCpCode());
		}

		if (program.getCreateTime() != null) {
			try {
				String date = DateFormatUtils.format(program.getCreateTime(),
						"yyyy-MM-dd HH:mm:ss");
				programBean.setOnlineTime(date);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (program.getOnlineTime() != null) {
			try {
				String date = DateFormatUtils.format(program.getOnlineTime(),
						"yyyy-MM-dd HH:mm:ss");
				programBean.setOnlineTime(date);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

		if (program.getOrgAirDate() != null) {
			try {
				String date = DateFormatUtils.format(program.getOrgAirDate(),
						"yyyy-MM-dd HH:mm:ss");
				programBean.setOrgAirDate(date);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		programBean.setDefinition("HD");
		return programBean;
	}

	private PictureBean genCreatePictureBean(Program program,
			InjectionObject programInjectionObject, String imagePath,
			long idOffset) {
		PictureBean pictureBean = new PictureBean();

		pictureBean.setAction(InjectionActionTypeEnum.CREATE.getValue());

		String id = "" + (program.getId() + idOffset);
		pictureBean.setID(id);
		pictureBean.setCode(id);

		pictureBean.setFileURL(AdminGlobal.getImageFtpPath(imagePath));

		return pictureBean;
	}

	private MappingBean genCreatePictureMappingBean(Program program,
			InjectionObject programInjectionObject, long idOffset, int type,
			int sequence) {
		MappingBean mappingBean = new MappingBean();

		mappingBean.setAction(InjectionActionTypeEnum.CREATE.getValue());

		mappingBean.setElementType(InjectionItemTypeEnum.PROGRAM.getValue());
		mappingBean.setElementID("" + program.getId());
		mappingBean.setElementCode("" + program.getId());
		mappingBean.setPartnerItemCode(programInjectionObject
				.getPartnerItemCode());

		String id = "" + (program.getId() + idOffset);
		mappingBean.setParentType(InjectionItemTypeEnum.PICTURE.getValue());
		mappingBean.setParentID(id);
		mappingBean.setParentCode(id);

		mappingBean.setType("" + type);
		mappingBean.setSequence("" + sequence);

		return mappingBean;
	}

	public MappingBean genCreateSeriesMappingBean(Series series,
			InjectionObject seriesInjectionObject, Program program,
			InjectionObject programInjectionObject) {
		MappingBean mappingBean = new MappingBean();

		mappingBean.setAction(InjectionActionTypeEnum.CREATE.getValue());

		mappingBean.setElementType(InjectionItemTypeEnum.PROGRAM.getValue());
		mappingBean.setElementID("" + program.getId());
		mappingBean.setElementCode("" + program.getId());
		mappingBean.setPartnerItemCode(programInjectionObject
				.getPartnerItemCode());

		mappingBean.setParentType(InjectionItemTypeEnum.SERIES.getValue());
		mappingBean.setParentID("" + series.getId());
		mappingBean.setParentCode("" + series.getId());
		mappingBean.setParentPartnerItemCode(seriesInjectionObject
				.getPartnerItemCode());

		mappingBean.setSequence("" + program.getEpisodeIndex());
		return mappingBean;
	}

}