package com.ai.cms.injection.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ai.cms.injection.entity.InjectionObject;
import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.entity.SendTask;
import com.ai.cms.injection.enums.InjectionActionTypeEnum;
import com.ai.cms.injection.enums.InjectionDirectionEnum;
import com.ai.cms.injection.enums.InjectionItemTypeEnum;
import com.ai.cms.injection.enums.InjectionObjectTypeEnum;
import com.ai.cms.injection.enums.InjectionStatusEnum;
import com.ai.cms.injection.enums.PlayCodeStatusEnum;
import com.ai.cms.injection.enums.SendTaskStatusEnum;
import com.ai.cms.injection.repository.InjectionObjectRepository;
import com.ai.cms.injection.repository.InjectionPlatformRepository;
import com.ai.cms.injection.repository.SendTaskRepository;
import com.ai.cms.media.entity.MediaFile;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.Series;
import com.ai.cms.media.repository.MediaFileRepository;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.cms.media.repository.SeriesRepository;
import com.ai.common.enums.MediaStatusEnum;
import com.ai.common.enums.ProgramTypeEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.exception.ServiceException;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;

@org.springframework.stereotype.Service
@Transactional(value = "slaveTransactionManager", readOnly = false)
public class InjectionService extends AbstractService<SendTask, Long> {

	@Autowired
	private InjectionPlatformRepository injectionPlatformRepository;

	@Autowired
	private InjectionObjectRepository injectionObjectRepository;

	@Autowired
	private SendTaskRepository sendTaskRepository;

	@Autowired
	private SeriesRepository seriesRepository;

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private MediaFileRepository mediaFileRepository;

	@Override
	public AbstractRepository<SendTask, Long> getRepository() {
		return sendTaskRepository;
	}

	public SendTask findSendTaskByCorrelateId(String correlateId) {
		List<SendTask> list = sendTaskRepository.findByCorrelateId(correlateId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public void saveSendTask(SendTask sendTask) {
		sendTaskRepository.save(sendTask);
	}

	public void resetSendTask(SendTask sendTask) {
		sendTask.setRequestTimes(0);
		sendTask.setStatus(SendTaskStatusEnum.WAIT.getKey());
		sendTaskRepository.save(sendTask);
	}

	public void saveSendTaskAndInjectionStatus(SendTask sendTask, int result) {
		if (result == 0) {
			sendTask.setStatus(SendTaskStatusEnum.SUCCESS.getKey());
			sendTaskRepository.save(sendTask);

			sendTaskRepository.updatePreTaskStatusByPreTaskId(
					YesNoEnum.YES.getKey(), sendTask.getId());
		} else {
			sendTask.setStatus(SendTaskStatusEnum.FAIL.getKey());
			sendTaskRepository.save(sendTask);
		}

		InjectionPlatform injectionPlatform = injectionPlatformRepository
				.findOne(sendTask.getPlatformId());
		updateInjectionStatus(injectionPlatform, sendTask.getCategory(),
				sendTask.getOperationObjectIds(), result);

		if (sendTask.getItemId() != null
				&& sendTask.getType() == InjectionObjectTypeEnum.OBJECT
						.getKey()
				&& sendTask.getItemType() == InjectionItemTypeEnum.SERIES
						.getKey()) {
			Series series = seriesRepository.findOne(sendTask.getItemId());
			updateInjectionStatus(injectionPlatform, series);
		} else if (sendTask.getItemId() != null
				&& sendTask.getType() == InjectionObjectTypeEnum.OBJECT
						.getKey()
				&& sendTask.getItemType() == InjectionItemTypeEnum.PROGRAM
						.getKey()) {
			Program program = programRepository.findOne(sendTask.getItemId());
			updateInjectionStatus(injectionPlatform, program);
		}
	}

	public void updateInjectionStatus(InjectionPlatform platform,
			String category, String allOperationObjectIds, int result) {
		if (platform == null) {
			return;
		}
		if (StringUtils.isNotEmpty(allOperationObjectIds)) {
			// 格式如o:Program:REGIST:288872:00000001000000010000000000288872;o:Picture:REGIST:1000288872:00000001000000080000001000288872;o:Movie:REGIST:285303:00000001000000020000000000285303;
			String[] allIdArray = allOperationObjectIds.split(";");
			for (String operationObjectIds : allIdArray) {
				if (StringUtils.isNotEmpty(operationObjectIds)) {
					String[] idArray = operationObjectIds.split(":");
					updateInjectionStatus(platform, category, idArray, result);
				}
			}
		}
	}

	public void updateInjectionStatus(InjectionPlatform platform,
			String category, String[] idArray, int result) {
		if (idArray == null || idArray.length < 1) {
			return;
		}
		if ("o".equalsIgnoreCase(idArray[0])) {
			updateObjectInjectionStatus(platform, category, idArray, result);
		} else if ("m".equalsIgnoreCase(idArray[0])) {
			updateMappingInjectionStatus(platform, category, idArray, result);
		}
	}

	public void updateObjectInjectionStatus(InjectionPlatform platform,
			String category, String[] idArray, int result) {
		// 格式o:Program:REGIST:288872:00000001000000010000000000288872;
		if (idArray != null && idArray.length == 5
				&& StringUtils.isNotEmpty(idArray[1])
				&& StringUtils.isNotEmpty(idArray[2])
				&& StringUtils.isNotEmpty(idArray[3])
				&& StringUtils.isNotEmpty(idArray[4])) {
			InjectionItemTypeEnum typeEnum = InjectionItemTypeEnum
					.getEnumByValue(idArray[1]);
			InjectionActionTypeEnum actionEnum = InjectionActionTypeEnum
					.getEnumByValue(idArray[2]);
			if (typeEnum == null || actionEnum == null) {
				return;
			}
			int status = 0;
			if (result == 0) {
				if (actionEnum.getKey() == InjectionActionTypeEnum.DELETE
						.getKey()) {
					status = InjectionStatusEnum.RECOVERY_SUCCESS.getKey();
				} else {
					status = InjectionStatusEnum.INJECTION_SUCCESS.getKey();
				}
			} else {
				if (actionEnum.getKey() == InjectionActionTypeEnum.DELETE
						.getKey()) {
					status = InjectionStatusEnum.RECOVERY_FAIL.getKey();
				} else {
					status = InjectionStatusEnum.INJECTION_FAIL.getKey();
				}
			}
			try {
				Long platformId = platform.getId();
				Date currentTime = new Date();
				Long id = Long.valueOf(idArray[3]);
				String partnerItemCode = StringUtils.trimToEmpty(idArray[4]);
				switch (typeEnum) {
				case PROGRAM:
					if (platform.getIsCallback() == YesNoEnum.YES.getKey()
							&& result == 0) {
						// 需要回写的等待回写成功后，再更新状态为已分发(在回写接口中实现)
						injectionObjectRepository.updateInjectionCode(
								platformId, category,
								InjectionItemTypeEnum.PROGRAM.getKey(), id,
								currentTime, partnerItemCode);
						break;
					}
					injectionObjectRepository.updateInjectionStatus(platformId,
							category, InjectionItemTypeEnum.PROGRAM.getKey(),
							id, status, currentTime, partnerItemCode);
					break;
				case SERIES:
					injectionObjectRepository.updateInjectionStatus(platformId,
							category, InjectionItemTypeEnum.SERIES.getKey(),
							id, status, currentTime, partnerItemCode);
					break;
				case MOVIE:
					if (platform.getIsCallback() == YesNoEnum.YES.getKey()
							&& result == 0) {
						// 需要回写的等待回写成功后，再更新状态为已分发(在回写接口中实现)
						injectionObjectRepository.updateInjectionCode(
								platformId, category,
								InjectionItemTypeEnum.MOVIE.getKey(), id,
								currentTime, partnerItemCode);
						break;
					}
					injectionObjectRepository.updateInjectionStatus(platformId,
							category, InjectionItemTypeEnum.MOVIE.getKey(), id,
							status, currentTime, partnerItemCode);
					break;
				case CATEGORY:
					break;
				case PACKAGE:
					break;
				case SCHEDULE:
					injectionObjectRepository.updateInjectionStatus(platformId,
							category, InjectionItemTypeEnum.SCHEDULE.getKey(),
							id, status, currentTime, partnerItemCode);
					break;
				default:
					break;
				}
			} catch (Exception e) {
			}
		}
	}

	public void updateMappingInjectionStatus(InjectionPlatform platform,
			String category, String[] idArray, int result) {
		if (idArray != null && idArray.length == 8
				&& StringUtils.isNotEmpty(idArray[1])
				&& StringUtils.isNotEmpty(idArray[2])
				&& StringUtils.isNotEmpty(idArray[7])) {
			InjectionItemTypeEnum typeEnum = InjectionItemTypeEnum
					.getEnumByValue(idArray[1]);
			InjectionActionTypeEnum actionEnum = InjectionActionTypeEnum
					.getEnumByValue(idArray[2]);
			if (typeEnum == null || actionEnum == null) {
				return;
			}
			// int status = 0;
			// if (result == 0) {
			// if (actionEnum.getKey() == InjectionActionTypeEnum.DELETE
			// .getKey()) {
			// status = InjectionStatusEnum.RECOVERED.getKey();
			// } else {
			// status = InjectionStatusEnum.INJECTED.getKey();
			// }
			// } else {
			// if (actionEnum.getKey() == InjectionActionTypeEnum.DELETE
			// .getKey()) {
			// status = InjectionStatusEnum.RECOVERY_FAIL.getKey();
			// } else {
			// status = InjectionStatusEnum.INJECTION_FAIL.getKey();
			// }
			// }
			// try {
			// Long platformId = platform.getId();
			// Date currentTime = new Date();
			// Long rId = Long.valueOf(idArray[7]);
			// switch (typeEnum) {
			// case CATEGORY:
			// break;
			// case PACKAGE:
			// break;
			// default:
			// break;
			// }
			// } catch (Exception e) {
			// }
		}
	}

	public List<InjectionObject> findInjectionObjectList(
			InjectionItemTypeEnum injectionItemTypeEnum, Long itemId) {
		List<InjectionObject> injectionObjectList = injectionObjectRepository
				.findByItemTypeAndItemId(injectionItemTypeEnum.getKey(), itemId);
		return injectionObjectList;
	}

	public List<InjectionObject> findInjectionObjectList(
			InjectionItemTypeEnum injectionItemTypeEnum, List<Long> itemIdList) {
		List<InjectionObject> injectionObjectList = injectionObjectRepository
				.findByItemTypeAndItemIdIn(injectionItemTypeEnum.getKey(),
						itemIdList);
		return injectionObjectList;
	}

	public InjectionPlatform findOneInjectionPlatform(Long id) {
		return injectionPlatformRepository.findOne(id);
	}

	public List<InjectionPlatform> findAllReceiveInjectionPlatform() {
		return injectionPlatformRepository
				.findAllByDirection(InjectionDirectionEnum.RECEIVE.getKey());
	}

	public List<InjectionPlatform> findAllSendInjectionPlatform() {
		return injectionPlatformRepository
				.findAllByDirection(InjectionDirectionEnum.SEND.getKey());
	}

	public Map<Long, InjectionPlatform> findAllInjectionPlatformMap(
			List<InjectionPlatform> injectionPlatformList) {
		Map<Long, InjectionPlatform> injectionPlatformMap = new HashMap<Long, InjectionPlatform>();
		for (InjectionPlatform injectionPlatform : injectionPlatformList) {
			injectionPlatformMap.put(injectionPlatform.getId(),
					injectionPlatform);
		}
		return injectionPlatformMap;
	}

	public void updateInjectionStatus(Series series) {
		updateInjectionStatus(null, series);
	}

	public void updateInjectionStatus(InjectionPlatform injectionPlatform,
			Series series) {
		if (series == null) {
			return;
		}
		// 分发状态
		List<InjectionObject> injectionObjectList = injectionObjectRepository
				.findByItemTypeAndItemId(InjectionItemTypeEnum.SERIES.getKey(),
						series.getId());
		String injectionStatus = getInjectionStatus(injectionObjectList);
		if (StringUtils.isEmpty(injectionStatus)) {
			injectionStatus = "" + InjectionStatusEnum.DEFAULT.getKey();
		}
		series.setInjectionStatus(injectionStatus);

		// 播放代码自定义
		if (injectionPlatform != null
				&& injectionPlatform.getPlayCodeCustom() == YesNoEnum.NO
						.getKey()
				&& injectionStatus.equals(""
						+ InjectionStatusEnum.INJECTION_SUCCESS.getKey())) {
			if (injectionObjectList != null
					&& StringUtils.isEmpty(series.getPlayCode())) {
				series.setPlayCode(injectionObjectList.get(0)
						.getPartnerItemCode());
			}
			if (StringUtils.isNotEmpty(series.getPlayCode())) {
				series.setPlayCodeStatus(PlayCodeStatusEnum.INPUT.getKey());
			}
		}

		seriesRepository.save(series);
		List<Program> programList = programRepository.findBySeriesId(series
				.getId());
		for (Program program : programList) {
			updateInjectionStatus(injectionPlatform, program);
		}
	}

	public void updateInjectionStatus(Program program) {
		updateInjectionStatus(null, program);
	}

	public void updateInjectionStatus(InjectionPlatform injectionPlatform,
			Program program) {
		if (program == null) {
			return;
		}
		// 分发状态
		List<InjectionObject> injectionObjectList = injectionObjectRepository
				.findByItemTypeAndItemId(
						InjectionItemTypeEnum.PROGRAM.getKey(), program.getId());
		String injectionStatus = getInjectionStatus(injectionObjectList);
		if (StringUtils.isEmpty(injectionStatus)) {
			injectionStatus = "" + InjectionStatusEnum.DEFAULT.getKey();
		}
		program.setInjectionStatus(injectionStatus);

		// 播放代码自定义
		if (injectionPlatform != null
				&& injectionPlatform.getPlayCodeCustom() == YesNoEnum.NO
						.getKey()
				&& injectionStatus.equals(""
						+ InjectionStatusEnum.INJECTION_SUCCESS.getKey())) {
			if (injectionObjectList != null
					&& StringUtils.isEmpty(program.getPlayCode())) {
				program.setPlayCode(injectionObjectList.get(0)
						.getPartnerItemCode());
			}
			if (StringUtils.isNotEmpty(program.getPlayCode())) {
				program.setPlayCodeStatus(PlayCodeStatusEnum.INPUT.getKey());
			}
		}

		programRepository.save(program);
		List<MediaFile> mediaFileList = mediaFileRepository
				.findByProgramId(program.getId());
		for (MediaFile mediaFile : mediaFileList) {
			updateInjectionStatus(injectionPlatform, mediaFile);
		}
	}

	public void updateInjectionStatus(MediaFile mediaFile) {
		updateInjectionStatus(null, mediaFile);
	}

	public void updateInjectionStatus(InjectionPlatform injectionPlatform,
			MediaFile mediaFile) {
		if (mediaFile == null) {
			return;
		}
		// 分发状态
		List<InjectionObject> injectionObjectList = injectionObjectRepository
				.findByItemTypeAndItemId(InjectionItemTypeEnum.MOVIE.getKey(),
						mediaFile.getId());
		String injectionStatus = getInjectionStatus(injectionObjectList);
		if (StringUtils.isEmpty(injectionStatus)) {
			injectionStatus = "" + InjectionStatusEnum.DEFAULT.getKey();
		}
		mediaFile.setInjectionStatus(injectionStatus);

		// 播放代码自定义
		if (injectionPlatform != null
				&& injectionPlatform.getPlayCodeCustom() == YesNoEnum.NO
						.getKey()
				&& injectionStatus.equals(""
						+ InjectionStatusEnum.INJECTION_SUCCESS.getKey())) {
			if (injectionObjectList != null
					&& StringUtils.isEmpty(mediaFile.getPlayCode())) {
				mediaFile.setPlayCode(injectionObjectList.get(0)
						.getPartnerItemCode());
			}
			if (StringUtils.isNotEmpty(mediaFile.getPlayCode())) {
				mediaFile.setPlayCodeStatus(PlayCodeStatusEnum.INPUT.getKey());
			}
		}
		mediaFileRepository.save(mediaFile);
	}

	private String getInjectionStatus(List<InjectionObject> injectionObjectList) {
		List<Integer> injectionStatusList = new ArrayList<Integer>();
		for (InjectionObject injectionObject : injectionObjectList) {
			if (injectionObject.getInjectionStatus() != null
					&& !injectionStatusList.contains(injectionObject
							.getInjectionStatus())) {
				injectionStatusList.add(injectionObject.getInjectionStatus());
			}
		}
		return StringUtils.join(injectionStatusList.toArray(), ",");
	}

	public void resetInjectionStatus(Series series) {
		if (series == null) {
			return;
		}
		injectionObjectRepository.deleteByItemTypeAndItemId(
				InjectionItemTypeEnum.SERIES.getKey(), series.getId());
		series.setInjectionStatus("" + InjectionStatusEnum.DEFAULT.getKey());
		seriesRepository.save(series);

		List<Program> programList = programRepository.findBySeriesId(series
				.getId());
		for (Program program : programList) {
			resetInjectionStatus(program);
		}
	}

	public void resetInjectionStatus(Program program) {
		if (program == null) {
			return;
		}
		injectionObjectRepository.deleteByItemTypeAndItemId(
				InjectionItemTypeEnum.PROGRAM.getKey(), program.getId());
		program.setInjectionStatus("" + InjectionStatusEnum.DEFAULT.getKey());
		programRepository.save(program);

		List<MediaFile> mediaFileList = mediaFileRepository
				.findByProgramId(program.getId());
		for (MediaFile mediaFile : mediaFileList) {
			resetInjectionStatus(mediaFile);
		}
	}

	public void resetInjectionStatus(MediaFile mediaFile) {
		if (mediaFile == null) {
			return;
		}
		injectionObjectRepository.deleteByItemTypeAndItemId(
				InjectionItemTypeEnum.MOVIE.getKey(), mediaFile.getId());
		mediaFile.setInjectionStatus("" + InjectionStatusEnum.DEFAULT.getKey());
		mediaFileRepository.save(mediaFile);
	}

	public InjectionObject getAndNewInjectionObject(Series series,
			Long platformId, String category) {
		InjectionObject seriesInjectionObject = injectionObjectRepository
				.findOneByPlatformIdAndCategoryAndItemTypeAndItemId(platformId,
						category, InjectionItemTypeEnum.SERIES.getKey(),
						series.getId());
		if (seriesInjectionObject == null) {
			seriesInjectionObject = new InjectionObject(platformId, category);
			seriesInjectionObject.setItemType(InjectionItemTypeEnum.SERIES
					.getKey());
			seriesInjectionObject.setItemId(series.getId());
		}
		return seriesInjectionObject;
	}

	public InjectionObject getAndNewInjectionObject(Program program,
			Long platformId, String category) {
		InjectionObject programInjectionObject = injectionObjectRepository
				.findOneByPlatformIdAndCategoryAndItemTypeAndItemId(platformId,
						category, InjectionItemTypeEnum.PROGRAM.getKey(),
						program.getId());
		if (programInjectionObject == null) {
			programInjectionObject = new InjectionObject(platformId, category);
			programInjectionObject.setItemType(InjectionItemTypeEnum.PROGRAM
					.getKey());
			programInjectionObject.setItemId(program.getId());
		}
		return programInjectionObject;
	}

	public InjectionObject getAndNewInjectionObject(MediaFile mediaFile,
			Long platformId, String category) {
		InjectionObject mediaFileInjectionObject = injectionObjectRepository
				.findOneByPlatformIdAndCategoryAndItemTypeAndItemId(platformId,
						category, InjectionItemTypeEnum.MOVIE.getKey(),
						mediaFile.getId());
		if (mediaFileInjectionObject == null) {
			mediaFileInjectionObject = new InjectionObject(platformId, category);
			mediaFileInjectionObject.setItemType(InjectionItemTypeEnum.MOVIE
					.getKey());
			mediaFileInjectionObject.setItemId(mediaFile.getId());
		}
		return mediaFileInjectionObject;
	}

	public static boolean isContainChinese(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 根据码率模板获取对应的分组代码
	 * 
	 * @param templateIds
	 * @return
	 */
	public Map<String, String> getCategoryMapByTemplateIds(String templateIds) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("default", templateIds);
		return map;
	}

	public String getTitleAdditionalSuffix(Series series, String category) {
		return series.getTitle();
	}

	public String getTitleAdditionalSuffix(Series series, Program program,
			String category) {
		if (series == null) {
			return program.getTitle();
		}
		return series.getTitle() + " " + program.getTitle();
	}

	/**
	 * 分发中
	 * 
	 * @param injectionStatus
	 * @return
	 */
	public boolean isInjection(Integer injectionStatus) {
		if (injectionStatus == InjectionStatusEnum.INJECTION_WAIT.getKey()
				|| injectionStatus == InjectionStatusEnum.INJECTION_ING
						.getKey()) {
			return true;
		}
		return false;
	}

	/**
	 * 已分发
	 * 
	 * @param injectionStatus
	 * @return
	 */
	public boolean isInjected(Integer injectionStatus) {
		if (injectionStatus == InjectionStatusEnum.INJECTION_SUCCESS.getKey()) {
			return true;
		}
		return false;
	}

	/**
	 * 分发剧头
	 */
	public synchronized void inInjection(Series series, Long[] platformIds,
			String[] templateIds, Integer[] prioritys) {
		if (series == null || platformIds == null || templateIds == null) {
			return;
		}
		for (int i = 0; i < platformIds.length; i++) {
			Map<String, String> categoryMap = getCategoryMapByTemplateIds(templateIds[i]);
			for (String category : categoryMap.keySet()) {
				InjectionObject seriesInjectionObject = getAndNewInjectionObject(
						series, platformIds[i], category);
				inInjection(series, seriesInjectionObject, category,
						categoryMap.get(category), prioritys[i]);
			}
		}
		updateInjectionStatus(series);
	}

	private synchronized Long inInjection(Series series,
			InjectionObject seriesInjectionObject, String category,
			String templateId, Integer priority) {
		Long taskId = null;
		if (!isInjection(seriesInjectionObject.getInjectionStatus())) {
			SendTask sendTask = new SendTask(seriesInjectionObject);
			sendTask.setType(InjectionObjectTypeEnum.OBJECT.getKey());
			if (isInjected(seriesInjectionObject.getInjectionStatus())) {
				sendTask.setAction(InjectionActionTypeEnum.UPDATE.getKey());
				seriesInjectionObject
						.setInjectionStatus(InjectionStatusEnum.INJECTION_WAIT
								.getKey());
			} else {
				sendTask.setAction(InjectionActionTypeEnum.CREATE.getKey());
				seriesInjectionObject
						.setInjectionStatus(InjectionStatusEnum.INJECTION_WAIT
								.getKey());
			}
			sendTask.setItemType(InjectionItemTypeEnum.SERIES.getKey());
			sendTask.setItemId(series.getId());
			String name = getTitleAdditionalSuffix(series, category)
					+ " "
					+ InjectionActionTypeEnum
							.getEnumByKey(sendTask.getAction()).getName();
			sendTask.setName(name);
			sendTask.setPriority(priority);
			sendTask.setStatus(SendTaskStatusEnum.DEFAULT.getKey());
			sendTaskRepository.save(sendTask);
			injectionObjectRepository.save(seriesInjectionObject);
			taskId = sendTask.getId();
		}

		// 节目后分发
		List<Program> programList = programRepository.findBySeriesId(series
				.getId());
		for (Program program : programList) {
			InjectionObject programInjectionObject = getAndNewInjectionObject(
					program, seriesInjectionObject.getPlatformId(),
					seriesInjectionObject.getCategory());
			inInjection(series, seriesInjectionObject, program,
					programInjectionObject, category, templateId, priority,
					taskId);
		}
		return taskId;
	}

	/**
	 * 分发节目
	 */
	public synchronized void inInjection(Series series, Program program,
			Long[] platformIds, String[] templateIds, Integer[] prioritys) {
		if (program == null || platformIds == null || templateIds == null) {
			return;
		}
		for (int i = 0; i < platformIds.length; i++) {
			Map<String, String> categoryMap = getCategoryMapByTemplateIds(templateIds[i]);
			for (String category : categoryMap.keySet()) {
				InjectionObject seriesInjectionObject = null;
				if (series != null) {
					seriesInjectionObject = getAndNewInjectionObject(series,
							platformIds[i], category);
				}
				Long preTaskId = null;
				// 判断剧头是否有分发
				if (program.getType() == ProgramTypeEnum.TV.getKey()
						&& program.getSeriesId() != null
						&& series != null
						&& seriesInjectionObject != null
						&& !isInjection(seriesInjectionObject
								.getInjectionStatus())
						&& !isInjected(seriesInjectionObject
								.getInjectionStatus())) {
					// 剧头没有分发，先分发剧头
					preTaskId = inInjection(series, seriesInjectionObject,
							category, categoryMap.get(category), prioritys[i]);
				}

				InjectionObject programInjectionObject = getAndNewInjectionObject(
						program, platformIds[i], category);
				inInjection(series, seriesInjectionObject, program,
						programInjectionObject, category,
						categoryMap.get(category), prioritys[i], preTaskId);
			}
		}
		updateInjectionStatus(program);
	}

	private synchronized Long inInjection(Series series,
			InjectionObject seriesInjectionObject, Program program,
			InjectionObject programInjectionObject, String category,
			String templateId, Integer priority, Long preTaskId) {
		if (program.getMediaStatus() != MediaStatusEnum.OK.getKey()
				&& program.getMediaStatus() != MediaStatusEnum.MISS_IMAGE
						.getKey()) {
			throw new ServiceException("节目[" + program.getTitle()
					+ "]媒体内容未准备好，不能分发!");
		}
		// 媒体内容一起分发
		List<Long> templateIdList = new ArrayList<Long>();
		for (String tempId : templateId.split(",")) {
			templateIdList.add(Long.valueOf(tempId));
		}
		List<MediaFile> mediaFileList = mediaFileRepository
				.findByProgramIdAndTemplateIdIn(program.getId(), templateIdList);

		List<Long> itemIdList = mediaFileList.stream().map(s -> s.getId())
				.collect(Collectors.toList());
		String subItemId = StringUtils.join(itemIdList.toArray(), ",");

		Long taskId = null;
		if (!isInjection(programInjectionObject.getInjectionStatus())) {
			SendTask sendTask = new SendTask(programInjectionObject);
			sendTask.setType(InjectionObjectTypeEnum.OBJECT.getKey());
			if (isInjected(programInjectionObject.getInjectionStatus())) {
				sendTask.setAction(InjectionActionTypeEnum.UPDATE.getKey());
				programInjectionObject
						.setInjectionStatus(InjectionStatusEnum.INJECTION_WAIT
								.getKey());
			} else {
				sendTask.setAction(InjectionActionTypeEnum.CREATE.getKey());
				programInjectionObject
						.setInjectionStatus(InjectionStatusEnum.INJECTION_WAIT
								.getKey());
			}
			sendTask.setItemType(InjectionItemTypeEnum.PROGRAM.getKey());
			sendTask.setItemId(program.getId());
			String name = getTitleAdditionalSuffix(series, program, category)
					+ " "
					+ InjectionActionTypeEnum
							.getEnumByKey(sendTask.getAction()).getName();
			sendTask.setName(name);
			sendTask.setPriority(priority);
			sendTask.setStatus(SendTaskStatusEnum.DEFAULT.getKey());
			if (preTaskId != null) {
				sendTask.setPreTaskId(preTaskId);
				sendTask.setPreTaskStatus(YesNoEnum.NO.getKey());
			}

			// 记录媒体内容的Id
			sendTask.setSubItemId(subItemId);
			sendTaskRepository.save(sendTask);
			injectionObjectRepository.save(programInjectionObject);
			taskId = sendTask.getId();
		}

		for (MediaFile mediaFile : mediaFileList) {
			InjectionObject mediaFileInjectionObject = getAndNewInjectionObject(
					mediaFile, programInjectionObject.getPlatformId(),
					programInjectionObject.getCategory());
			inInjection(series, program, programInjectionObject, mediaFile,
					mediaFileInjectionObject, category, priority, taskId);
		}
		return taskId;
	}

	private synchronized void inInjection(Series series, Program program,
			InjectionObject programInjectionObject, MediaFile mediaFile,
			InjectionObject mediaFileInjectionObject, String category,
			Integer priority, Long preTaskId) {
		if (StringUtils.isEmpty(mediaFile.getFilePath())) {
			throw new ServiceException("节目[" + program.getTitle()
					+ "]媒体文件路径为空，不能分发!");
		}
		if (isContainChinese(mediaFile.getFilePath())) {
			throw new ServiceException("节目[" + program.getTitle()
					+ "]媒体文件路径包括中文，不能分发!");
		}
		if (!isInjection(mediaFileInjectionObject.getInjectionStatus())) {
			SendTask sendTask = new SendTask(mediaFileInjectionObject);
			sendTask.setType(InjectionObjectTypeEnum.OBJECT.getKey());
			if (isInjected(mediaFileInjectionObject.getInjectionStatus())) {
				sendTask.setAction(InjectionActionTypeEnum.UPDATE.getKey());
				mediaFileInjectionObject
						.setInjectionStatus(InjectionStatusEnum.INJECTION_WAIT
								.getKey());
			} else {
				sendTask.setAction(InjectionActionTypeEnum.CREATE.getKey());
				mediaFileInjectionObject
						.setInjectionStatus(InjectionStatusEnum.INJECTION_WAIT
								.getKey());
			}
			sendTask.setItemType(InjectionItemTypeEnum.MOVIE.getKey());
			sendTask.setItemId(mediaFile.getId());
			String name = getTitleAdditionalSuffix(series, program, category)
					+ " "
					+ InjectionActionTypeEnum
							.getEnumByKey(sendTask.getAction()).getName();
			sendTask.setName(name);
			sendTask.setPriority(priority);
			sendTask.setStatus(SendTaskStatusEnum.DEFAULT.getKey());
			if (preTaskId != null) {
				sendTask.setPreTaskId(preTaskId);
				sendTask.setPreTaskStatus(YesNoEnum.NO.getKey());
			}
			// 媒体内容暂不需要生成任务,会同节目一起分发
			// sendTaskRepository.save(sendTask);
			injectionObjectRepository.save(mediaFileInjectionObject);
		}
	}

	/**
	 * 回收剧头
	 */
	public synchronized void outInjection(Series series, Long[] platformIds,
			String[] templateIds, Integer[] prioritys) {
		if (series == null || platformIds == null || templateIds == null) {
			return;
		}
		for (int i = 0; i < platformIds.length; i++) {
			Map<String, String> categoryMap = getCategoryMapByTemplateIds(templateIds[i]);
			for (String category : categoryMap.keySet()) {
				InjectionObject seriesInjectionObject = getAndNewInjectionObject(
						series, platformIds[i], category);
				outInjection(series, seriesInjectionObject, category,
						categoryMap.get(category), prioritys[i]);
			}
		}
		updateInjectionStatus(series);
	}

	private synchronized void outInjection(Series series,
			InjectionObject seriesInjectionObject, String category,
			String templateId, Integer priority) {
		// 节目先回收
		List<Program> programList = programRepository.findBySeriesId(series
				.getId());
		for (Program program : programList) {
			InjectionObject programInjectionObject = getAndNewInjectionObject(
					program, seriesInjectionObject.getPlatformId(),
					seriesInjectionObject.getCategory());
			outInjection(series, seriesInjectionObject, program,
					programInjectionObject, category, templateId, priority);
		}

		if (seriesInjectionObject.getInjectionStatus() == InjectionStatusEnum.INJECTION_SUCCESS
				.getKey()
				|| seriesInjectionObject.getInjectionStatus() == InjectionStatusEnum.RECOVERY_FAIL
						.getKey()) {
			seriesInjectionObject
					.setInjectionStatus(InjectionStatusEnum.RECOVERY_WAIT
							.getKey());

			SendTask sendTask = new SendTask(seriesInjectionObject);
			sendTask.setType(InjectionObjectTypeEnum.OBJECT.getKey());
			sendTask.setAction(InjectionActionTypeEnum.DELETE.getKey());
			sendTask.setItemType(InjectionItemTypeEnum.SERIES.getKey());
			sendTask.setItemId(series.getId());
			String name = getTitleAdditionalSuffix(series, category)
					+ " "
					+ InjectionActionTypeEnum
							.getEnumByKey(sendTask.getAction()).getName();
			sendTask.setName(name);
			sendTask.setPriority(priority);
			sendTask.setStatus(SendTaskStatusEnum.DEFAULT.getKey());
			sendTaskRepository.save(sendTask);
			injectionObjectRepository.save(seriesInjectionObject);
		}
	}

	/**
	 * 回收节目
	 */
	public synchronized void outInjection(Series series, Program program,
			Long[] platformIds, String[] templateIds, Integer[] prioritys) {
		if (program == null || platformIds == null || templateIds == null) {
			return;
		}
		for (int i = 0; i < platformIds.length; i++) {
			Map<String, String> categoryMap = getCategoryMapByTemplateIds(templateIds[i]);
			for (String category : categoryMap.keySet()) {
				InjectionObject seriesInjectionObject = null;
				if (series != null) {
					seriesInjectionObject = getAndNewInjectionObject(series,
							platformIds[i], category);
				}

				InjectionObject programInjectionObject = getAndNewInjectionObject(
						program, platformIds[i], category);

				outInjection(series, seriesInjectionObject, program,
						programInjectionObject, category,
						categoryMap.get(category), prioritys[i]);
			}
		}
		updateInjectionStatus(program);
	}

	private synchronized void outInjection(Series series,
			InjectionObject seriesInjectionObject, Program program,
			InjectionObject programInjectionObject, String category,
			String templateId, Integer priority) {
		// 媒体内容一起回收
		List<Long> templateIdList = new ArrayList<Long>();
		for (String tempId : templateId.split(",")) {
			templateIdList.add(Long.valueOf(tempId));
		}
		List<MediaFile> mediaFileList = mediaFileRepository
				.findByProgramIdAndTemplateIdIn(program.getId(), templateIdList);

		List<Long> itemIdList = mediaFileList.stream().map(s -> s.getId())
				.collect(Collectors.toList());
		String subItemId = StringUtils.join(itemIdList.toArray(), ",");

		for (MediaFile mediaFile : mediaFileList) {
			InjectionObject mediaFileInjectionObject = getAndNewInjectionObject(
					mediaFile, programInjectionObject.getPlatformId(),
					programInjectionObject.getCategory());
			outInjection(series, program, programInjectionObject, mediaFile,
					mediaFileInjectionObject, category, priority);
		}

		if (programInjectionObject.getInjectionStatus() == InjectionStatusEnum.INJECTION_SUCCESS
				.getKey()
				|| programInjectionObject.getInjectionStatus() == InjectionStatusEnum.RECOVERY_FAIL
						.getKey()) {
			programInjectionObject
					.setInjectionStatus(InjectionStatusEnum.RECOVERY_WAIT
							.getKey());

			SendTask sendTask = new SendTask(programInjectionObject);
			sendTask.setType(InjectionObjectTypeEnum.OBJECT.getKey());
			sendTask.setAction(InjectionActionTypeEnum.DELETE.getKey());
			sendTask.setItemType(InjectionItemTypeEnum.PROGRAM.getKey());
			sendTask.setItemId(program.getId());
			String name = getTitleAdditionalSuffix(series, program, category)
					+ " "
					+ InjectionActionTypeEnum
							.getEnumByKey(sendTask.getAction()).getName();
			sendTask.setName(name);
			sendTask.setPriority(priority);
			sendTask.setStatus(SendTaskStatusEnum.DEFAULT.getKey());

			// 记录媒体内容的Id
			sendTask.setSubItemId(subItemId);
			sendTaskRepository.save(sendTask);
			injectionObjectRepository.save(programInjectionObject);
		}
	}

	private synchronized void outInjection(Series series, Program program,
			InjectionObject programInjectionObject, MediaFile mediaFile,
			InjectionObject mediaFileInjectionObject, String category,
			Integer priority) {
		if (mediaFileInjectionObject.getInjectionStatus() == InjectionStatusEnum.INJECTION_SUCCESS
				.getKey()
				|| mediaFileInjectionObject.getInjectionStatus() == InjectionStatusEnum.RECOVERY_FAIL
						.getKey()) {
			mediaFileInjectionObject
					.setInjectionStatus(InjectionStatusEnum.RECOVERY_WAIT
							.getKey());
			SendTask sendTask = new SendTask(mediaFileInjectionObject);
			sendTask.setType(InjectionObjectTypeEnum.OBJECT.getKey());
			sendTask.setAction(InjectionActionTypeEnum.DELETE.getKey());
			sendTask.setItemType(InjectionItemTypeEnum.MOVIE.getKey());
			sendTask.setItemId(mediaFile.getId());
			String name = getTitleAdditionalSuffix(series, program, category)
					+ " "
					+ InjectionActionTypeEnum
							.getEnumByKey(sendTask.getAction()).getName();
			sendTask.setName(name);
			sendTask.setPriority(priority);
			sendTask.setStatus(SendTaskStatusEnum.DEFAULT.getKey());
			// 媒体内容暂不需要生成任务,会同节目一起分发
			// sendTaskRepository.save(sendTask);
			injectionObjectRepository.save(mediaFileInjectionObject);
		}
	}

}
