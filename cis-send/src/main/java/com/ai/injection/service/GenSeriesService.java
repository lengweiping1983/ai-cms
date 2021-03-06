package com.ai.injection.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.AdminGlobal;
import com.ai.cms.injection.bean.ADI;
import com.ai.cms.injection.bean.MappingBean;
import com.ai.cms.injection.bean.PictureBean;
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
import com.ai.cms.media.entity.MediaImage;
import com.ai.cms.media.entity.Series;
import com.ai.common.enums.ContentTypeEnum;
import com.ai.common.enums.MediaImageTypeEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.SeriesTypeEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.exception.DataException;

@Service
@Transactional(value = "slaveTransactionManager", readOnly = false)
public class GenSeriesService extends GenCommonService {

	/**
	 * 生成剧头任务
	 * 
	 * @param sendTask
	 * @param platform
	 * @return
	 */
	public boolean genTask(SendTask sendTask, InjectionPlatform platform) {
		// 1.获取对象,检查状态
		Series series = seriesRepository.findOne(sendTask.getItemId());
		if (series == null) {
			throw new DataException("剧头不存在！");
		}
		InjectionObject seriesInjectionObject = injectionService
				.getAndNewInjectionObject(series, platform,
						sendTask.getCategory());

		// 如是业务系统，需要检查依赖平台是否都分发成功，如都分发成功才向业务系统发送
		if ((sendTask.getAction() == InjectionActionTypeEnum.CREATE.getKey() || sendTask
				.getAction() == InjectionActionTypeEnum.UPDATE.getKey())
				&& platform.getDirection() == InjectionDirectionEnum.SEND
						.getKey()
				&& platform.getType() == PlatformTypeEnum.APP_SYSTEM.getKey()
				&& StringUtils.isNotEmpty(platform.getDependPlatformId())) {
			List<InjectionObject> injectionObjectListAll = injectionService
					.findInjectionObjectList(InjectionItemTypeEnum.SERIES,
							series.getId());
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
		}

		// 2.设置基本属性
		String correlateId = StringUtils.trimToEmpty(platform
				.getCorrelatePrefix()) + sendTask.getId();
		sendTask.setCorrelateId(correlateId);

		// 3.生成剧头任务
		ADI adi = new ADI();
		genSeriesTask(sendTask, platform, adi, series, seriesInjectionObject);

		// 4.生成xml
		genXML(sendTask, platform, adi);

		// 5.保存任务
		sendTask.setStatus(SendTaskStatusEnum.WAIT.getKey());
		sendTaskRepository.save(sendTask);
		return true;
	}

	public void genSeriesTask(SendTask sendTask, InjectionPlatform platform,
			ADI adi, Series series, InjectionObject seriesInjectionObject) {
		if (sendTask.getAction() == InjectionActionTypeEnum.CREATE.getKey()) {// 发送增加命令
			genCreateSeriesTask(sendTask, platform, adi, series,
					seriesInjectionObject);
		} else if (sendTask.getAction() == InjectionActionTypeEnum.UPDATE
				.getKey()) {// 发送修改命令
			genUpdateSeriesTask(sendTask, platform, adi, series,
					seriesInjectionObject);
		} else if (sendTask.getAction() == InjectionActionTypeEnum.DELETE
				.getKey()) {// 发送删除命令
			genDeleteSeriesTask(sendTask, platform, adi, series,
					seriesInjectionObject);
		}
	}

	private void genCreateSeriesTask(SendTask sendTask,
			InjectionPlatform platform, ADI adi, Series series,
			InjectionObject seriesInjectionObject) {
		// 1.生成剧头操作对象
		SeriesBean seriesBean = genSeriesBean(sendTask, platform, series,
				seriesInjectionObject);
		seriesBean.setAction(InjectionActionTypeEnum.CREATE.getValue());
		adi.getObjects().add(seriesBean);

		if (seriesInjectionObject.getInjectionStatus() != InjectionStatusEnum.INJECTION_ING
				.getKey()) {
			// 设置对象分发状态为分发中
			seriesInjectionObject
					.setInjectionStatus(InjectionStatusEnum.INJECTION_ING
							.getKey());
			injectionObjectRepository.save(seriesInjectionObject);
			injectionService.updateInjectionStatus(platform, series, false);
		}

		// 2.生成海报操作对象
		if (platform.getNeedImageObject() == YesNoEnum.YES.getKey()) {
			if (StringUtils.isNotEmpty(series.getImage1())) {
				// a.生成海报操作对象
				PictureBean pictureBean = genCreatePictureBean(series,
						seriesInjectionObject, series.getImage1(), 1000000000l);
				adi.getObjects().add(pictureBean);

				// b.生成海报映射对象
				MappingBean mappingBean = genCreatePictureMappingBean(series,
						seriesInjectionObject, 1000000000l, 1, 1);
				adi.getMappings().add(mappingBean);
			}

			if (StringUtils.isNotEmpty(series.getImage2())) {
				// a.生成海报操作对象
				PictureBean pictureBean = genCreatePictureBean(series,
						seriesInjectionObject, series.getImage2(), 2000000000l);
				adi.getObjects().add(pictureBean);

				// b.生成海报映射对象
				MappingBean mappingBean = genCreatePictureMappingBean(series,
						seriesInjectionObject, 2000000000l, 1, 2);
				adi.getMappings().add(mappingBean);
			}

			if (StringUtils.isNotEmpty(series.getImage3())) {
				// a.生成海报操作对象
				PictureBean pictureBean = genCreatePictureBean(series,
						seriesInjectionObject, series.getImage3(), 3000000000l);
				adi.getObjects().add(pictureBean);

				// b.生成海报映射对象
				MappingBean mappingBean = genCreatePictureMappingBean(series,
						seriesInjectionObject, 3000000000l, 1, 3);
				adi.getMappings().add(mappingBean);
			}

			if (StringUtils.isNotEmpty(series.getImage4())) {
				// a.生成海报操作对象
				PictureBean pictureBean = genCreatePictureBean(series,
						seriesInjectionObject, series.getImage4(), 4000000000l);
				adi.getObjects().add(pictureBean);

				// b.生成海报映射对象
				MappingBean mappingBean = genCreatePictureMappingBean(series,
						seriesInjectionObject, 4000000000l, 1, 4);
				adi.getMappings().add(mappingBean);
			}

			// 生成剧照
			List<MediaImage> mediaImageList = mediaImageRepository
					.findBySeriesIdAndType(series.getId(),
							MediaImageTypeEnum.STILLS.getKey());
			for (MediaImage mediaImage : mediaImageList) {
				if (StringUtils.isNotEmpty(mediaImage.getFilePath())) {
					// a.生成剧照操作对象
					PictureBean pictureBean = genCreatePictureBean(mediaImage);
					adi.getObjects().add(pictureBean);

					// b.生成剧照映射对象
					MappingBean mappingBean = genCreatePictureMappingBean(
							series, seriesInjectionObject, mediaImage);
					adi.getMappings().add(mappingBean);
				}
			}
		}
	}

	private void genUpdateSeriesTask(SendTask sendTask,
			InjectionPlatform platform, ADI adi, Series series,
			InjectionObject seriesInjectionObject) {
		// 1.生成剧头操作对象
		SeriesBean seriesBean = genSeriesBean(sendTask, platform, series,
				seriesInjectionObject);
		seriesBean.setAction(InjectionActionTypeEnum.UPDATE.getValue());
		adi.getObjects().add(seriesBean);

		if (seriesInjectionObject.getInjectionStatus() != InjectionStatusEnum.INJECTION_ING
				.getKey()) {
			// 设置对象分发状态为分发中
			seriesInjectionObject
					.setInjectionStatus(InjectionStatusEnum.INJECTION_ING
							.getKey());
			injectionObjectRepository.save(seriesInjectionObject);
			injectionService.updateInjectionStatus(platform, series, false);
		}

		// 2.生成海报操作对象
		if (platform.getNeedImageObject() == YesNoEnum.YES.getKey()) {
			if (StringUtils.isNotEmpty(series.getImage1())) {
				// a.生成海报操作对象
				PictureBean pictureBean = genCreatePictureBean(series,
						seriesInjectionObject, series.getImage1(), 1000000000l);
				adi.getObjects().add(pictureBean);

				// b.生成海报映射对象
				MappingBean mappingBean = genCreatePictureMappingBean(series,
						seriesInjectionObject, 1000000000l, 1, 1);
				adi.getMappings().add(mappingBean);
			}

			if (StringUtils.isNotEmpty(series.getImage2())) {
				// a.生成海报操作对象
				PictureBean pictureBean = genCreatePictureBean(series,
						seriesInjectionObject, series.getImage2(), 2000000000l);
				adi.getObjects().add(pictureBean);

				// b.生成海报映射对象
				MappingBean mappingBean = genCreatePictureMappingBean(series,
						seriesInjectionObject, 2000000000l, 1, 2);
				adi.getMappings().add(mappingBean);
			}

			if (StringUtils.isNotEmpty(series.getImage3())) {
				// a.生成海报操作对象
				PictureBean pictureBean = genCreatePictureBean(series,
						seriesInjectionObject, series.getImage3(), 3000000000l);
				adi.getObjects().add(pictureBean);

				// b.生成海报映射对象
				MappingBean mappingBean = genCreatePictureMappingBean(series,
						seriesInjectionObject, 3000000000l, 1, 3);
				adi.getMappings().add(mappingBean);
			}

			if (StringUtils.isNotEmpty(series.getImage4())) {
				// a.生成海报操作对象
				PictureBean pictureBean = genCreatePictureBean(series,
						seriesInjectionObject, series.getImage4(), 4000000000l);
				adi.getObjects().add(pictureBean);

				// b.生成海报映射对象
				MappingBean mappingBean = genCreatePictureMappingBean(series,
						seriesInjectionObject, 4000000000l, 1, 4);
				adi.getMappings().add(mappingBean);
			}

			// 生成剧照
			List<MediaImage> mediaImageList = mediaImageRepository
					.findBySeriesIdAndType(series.getId(),
							MediaImageTypeEnum.STILLS.getKey());
			for (MediaImage mediaImage : mediaImageList) {
				if (StringUtils.isNotEmpty(mediaImage.getFilePath())) {
					// a.生成剧照操作对象
					PictureBean pictureBean = genCreatePictureBean(mediaImage);
					adi.getObjects().add(pictureBean);

					// b.生成剧照映射对象
					MappingBean mappingBean = genCreatePictureMappingBean(
							series, seriesInjectionObject, mediaImage);
					adi.getMappings().add(mappingBean);
				}
			}
		}
	}

	private void genDeleteSeriesTask(SendTask sendTask,
			InjectionPlatform platform, ADI adi, Series series,
			InjectionObject seriesInjectionObject) {
		// 1.生成剧头操作对象
		SeriesBean seriesBean = genSeriesBean(sendTask, platform, series,
				seriesInjectionObject);
		seriesBean.setAction(InjectionActionTypeEnum.DELETE.getValue());
		adi.getObjects().add(seriesBean);

		if (seriesInjectionObject.getInjectionStatus() != InjectionStatusEnum.RECOVERY_ING
				.getKey()) {
			// 设置对象分发状态为回收中
			seriesInjectionObject
					.setInjectionStatus(InjectionStatusEnum.RECOVERY_ING
							.getKey());
			injectionObjectRepository.save(seriesInjectionObject);
			injectionService.updateInjectionStatus(platform, series, false);
		}
	}

	public SeriesBean genSeriesBean(SendTask sendTask,
			InjectionPlatform platform, Series series,
			InjectionObject seriesInjectionObject) {
		SeriesBean seriesBean = new SeriesBean();

		seriesBean.setID("" + series.getId());
		seriesBean.setCode("" + series.getId());
		seriesBean.setPartnerItemCode(seriesInjectionObject
				.getPartnerItemCode());

		seriesBean.setName(series.getName());

		seriesBean.setOriginalName(series.getName());

		if (StringUtils.isNotEmpty(series.getOrderNumber())) {
			seriesBean.setOrderNumber(series.getOrderNumber());
		}
		if (StringUtils.isNotEmpty(series.getSortName())) {
			seriesBean.setSortName(series.getSortName());
		}
		if (StringUtils.isNotEmpty(series.getSearchName())) {
			seriesBean.setSearchName(series.getSearchName());
		}

		if (StringUtils.isNotEmpty(series.getDirector())) {
			seriesBean.setDirector(getSeparateString(sendTask, platform,
					series.getDirector()));
		}
		if (StringUtils.isNotEmpty(series.getActor())) {
			seriesBean.setActorDisplay(getSeparateString(sendTask, platform,
					series.getActor()));
		}
		if (StringUtils.isNotEmpty(series.getYear())) {
			seriesBean.setReleaseYear(series.getYear());
		}
		if (StringUtils.isNotEmpty(series.getArea())) {
			seriesBean.setOriginalCountry(series.getArea());
		}
		if (StringUtils.isNotEmpty(series.getLanguage())) {
			seriesBean.setLanguage(series.getLanguage());
		}

		if (series.getOrgAirDate() != null) {
			try {
				String date = DateFormatUtils.format(series.getOrgAirDate(),
						"yyyyMMdd");
				seriesBean.setOrgAirDate(date);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (series.getLicensingWindowStart() != null) {
			try {
				String date = DateFormatUtils.format(
						series.getLicensingWindowStart(), "yyyyMMddHHmmss");
				seriesBean.setLicensingWindowStart(date);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (series.getLicensingWindowEnd() != null) {
			try {
				String date = DateFormatUtils.format(
						series.getLicensingWindowEnd(), "yyyyMMddHHmmss");
				seriesBean.setLicensingWindowEnd(date);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

		if (series.getEpisodeTotal() != null) {
			seriesBean.setVolumnCount(String.valueOf(series.getEpisodeTotal()));
		}
		if (series.getDuration() != null) {
			seriesBean.setDuration(String.valueOf(series.getDuration()));
		}
		if (series.getRating() != null) {
			seriesBean.setRating(String.valueOf(series.getRating()));
		}

		seriesBean
				.setSeriesType((series.getType() == null || series.getType() == SeriesTypeEnum.TV
						.getKey()) ? "1" : "2");
		if (series.getContentType() != null) {
			seriesBean.setType(ContentTypeEnum.getEnumByKey(
					series.getContentType()).getValue());
		}
		if (StringUtils.isNotEmpty(series.getTag())) {
			seriesBean.setTags(getSeparateString(sendTask, platform,
					series.getTag()));
		}

		if (StringUtils.isNotEmpty(series.getViewpoint())) {
			seriesBean.setViewpoint(series.getViewpoint());
		}
		if (StringUtils.isNotEmpty(series.getInfo())) {
			seriesBean.setDescription(series.getInfo());
		}

		seriesBean.setStatus(series.getStatus() == OnlineStatusEnum.OFFLINE
				.getKey() ? "0" : "1");

		if (StringUtils.isNotEmpty(series.getImage1())) {
			seriesBean
					.setImage1(AdminGlobal.getImageWebPath(series.getImage1()));
		}
		if (StringUtils.isNotEmpty(series.getImage2())) {
			seriesBean
					.setImage2(AdminGlobal.getImageWebPath(series.getImage2()));
		}

		if (StringUtils.isNotEmpty(series.getKpeople())) {
			seriesBean.setKpeople(getSeparateString(sendTask, platform,
					series.getKpeople()));
		}
		if (StringUtils.isNotEmpty(series.getScriptWriter())) {
			seriesBean.setScriptWriter(getSeparateString(sendTask, platform,
					series.getScriptWriter()));
		}
		if (StringUtils.isNotEmpty(series.getCompere())) {
			seriesBean.setCompere(getSeparateString(sendTask, platform,
					series.getCompere()));
		}
		if (StringUtils.isNotEmpty(series.getGuest())) {
			seriesBean.setGuest(getSeparateString(sendTask, platform,
					series.getGuest()));
		}
		if (StringUtils.isNotEmpty(series.getReporter())) {
			seriesBean.setReporter(getSeparateString(sendTask, platform,
					series.getReporter()));
		}
		if (StringUtils.isNotEmpty(series.getIncharge())) {
			seriesBean.setOPIncharge(series.getIncharge());
		}
		if (StringUtils.isNotEmpty(series.getCopyRight())) {
			seriesBean.setCopyRight(series.getCopyRight());
		}
		if (StringUtils.isNotEmpty(series.getCpCode())) {
			seriesBean.setContentProvider(series.getCpCode());
		}

		if (series.getCreateTime() != null) {
			try {
				String date = DateFormatUtils.format(series.getCreateTime(),
						"yyyy-MM-dd HH:mm:ss");
				seriesBean.setOnlineTime(date);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (series.getOnlineTime() != null) {
			try {
				String date = DateFormatUtils.format(series.getOnlineTime(),
						"yyyy-MM-dd HH:mm:ss");
				seriesBean.setOnlineTime(date);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

		if (series.getOrgAirDate() != null) {
			try {
				String date = DateFormatUtils.format(series.getOrgAirDate(),
						"yyyy-MM-dd HH:mm:ss");
				seriesBean.setOrgAirDate(date);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		seriesBean.setDefinition("HD");

		return seriesBean;
	}

	private PictureBean genCreatePictureBean(Series series,
			InjectionObject seriesInjectionObject, String imagePath,
			long idOffset) {
		PictureBean pictureBean = new PictureBean();

		pictureBean.setAction(InjectionActionTypeEnum.CREATE.getValue());

		String id = "" + (series.getId() + idOffset);
		pictureBean.setID(id);
		pictureBean.setCode(id);

		pictureBean.setFileURL(AdminGlobal.getImageFtpPath(imagePath));

		return pictureBean;
	}

	private MappingBean genCreatePictureMappingBean(Series series,
			InjectionObject seriesInjectionObject, long idOffset, int type,
			int sequence) {
		MappingBean mappingBean = new MappingBean();

		mappingBean.setAction(InjectionActionTypeEnum.CREATE.getValue());

		mappingBean.setElementType(InjectionItemTypeEnum.SERIES.getValue());
		mappingBean.setElementID("" + series.getId());
		mappingBean.setElementCode("" + series.getId());
		mappingBean.setPartnerItemCode(seriesInjectionObject
				.getPartnerItemCode());

		String id = "" + (series.getId() + idOffset);
		mappingBean.setParentType(InjectionItemTypeEnum.PICTURE.getValue());
		mappingBean.setParentID(id);
		mappingBean.setParentCode(id);

		mappingBean.setType("" + type);
		mappingBean.setSequence("" + sequence);

		return mappingBean;
	}

}