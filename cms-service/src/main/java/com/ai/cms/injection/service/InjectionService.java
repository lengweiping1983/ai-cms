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
import com.ai.cms.injection.enums.PlatformTypeEnum;
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

	public void saveInjectionObject(InjectionObject injectionObject) {
		injectionObjectRepository.save(injectionObject);
	}

	public void updateMediaFileInjectionObjectAndInjectionStatus(
			InjectionPlatform injectionPlatform, String category,
			Long mediaFileId) {
		if (injectionPlatform == null) {
			return;
		}
		if (mediaFileId == null) {
			return;
		}
		MediaFile mediaFile = mediaFileRepository.findOne(mediaFileId);
		if (mediaFile == null) {
			return;
		}
		updateInjectionStatus(injectionPlatform, mediaFile);

		updateProgramInjectionObjectAndInjectionStatus(injectionPlatform,
				category, mediaFile.getProgramId());
	}

	public void updateProgramInjectionObjectAndInjectionStatus(
			InjectionPlatform injectionPlatform, String category, Long programId) {
		if (injectionPlatform == null) {
			return;
		}
		if (programId == null) {
			return;
		}
		Program program = programRepository.findOne(programId);
		if (program == null) {
			return;
		}

		List<InjectionObject> subInjectionObjectList = injectionObjectRepository
				.findByPlatformIdAndCategoryAndItemTypeAndItemParentId(
						injectionPlatform.getId(), category,
						InjectionItemTypeEnum.MOVIE.getKey(), programId);

		Integer injectionStatus = getInjectionObjectInjectionStatus(subInjectionObjectList);

		InjectionObject programInjectionObject = getAndNewInjectionObject(
				program, injectionPlatform, category);
		programInjectionObject.setInjectionStatus(injectionStatus);
		injectionObjectRepository.save(programInjectionObject);

		updateInjectionStatus(injectionPlatform, program, false);

		updateSeriesInjectionObjectAndInjectionStatus(injectionPlatform,
				category, program.getSeriesId());
	}

	public void updateSeriesInjectionObjectAndInjectionStatus(
			InjectionPlatform injectionPlatform, String category, Long seriesId) {
		if (injectionPlatform == null) {
			return;
		}
		if (seriesId == null) {
			return;
		}
		Series series = seriesRepository.findOne(seriesId);
		if (series == null) {
			return;
		}

		List<InjectionObject> subInjectionObjectList = injectionObjectRepository
				.findByPlatformIdAndCategoryAndItemTypeAndItemParentId(
						injectionPlatform.getId(), category,
						InjectionItemTypeEnum.PROGRAM.getKey(), seriesId);

		Integer injectionStatus = getInjectionObjectInjectionStatus(subInjectionObjectList);

		InjectionObject seriesInjectionObject = getAndNewInjectionObject(
				series, injectionPlatform, category);
		seriesInjectionObject.setInjectionStatus(injectionStatus);
		injectionObjectRepository.save(seriesInjectionObject);

		updateInjectionStatus(injectionPlatform, series, false);
	}

	public Integer getInjectionObjectInjectionStatus(
			List<InjectionObject> subInjectionObjectList) {
		boolean allInjectionSuccess = true;
		boolean hasInjectionIng = false;
		boolean hasInjectionSuccess = false;
		boolean hasInjectionFail = false;

		boolean allRecoverySuccess = true;
		boolean hasRecoveryIng = false;
		boolean hasRecoverySuccess = false;
		boolean hasRecoveryFail = false;

		for (InjectionObject subInjectionObject : subInjectionObjectList) {
			if (subInjectionObject.getInjectionStatus() != InjectionStatusEnum.INJECTION_SUCCESS
					.getKey()) {
				allInjectionSuccess = false;
			}
			if (subInjectionObject.getInjectionStatus() != InjectionStatusEnum.RECOVERY_SUCCESS
					.getKey()) {
				allRecoverySuccess = false;
			}
			if (subInjectionObject.getInjectionStatus() == InjectionStatusEnum.INJECTION_ING
					.getKey()) {
				hasInjectionIng = true;
			} else if (subInjectionObject.getInjectionStatus() == InjectionStatusEnum.INJECTION_SUCCESS
					.getKey()) {
				hasInjectionSuccess = true;
			} else if (subInjectionObject.getInjectionStatus() == InjectionStatusEnum.INJECTION_FAIL
					.getKey()) {
				hasInjectionFail = true;
			} else if (subInjectionObject.getInjectionStatus() == InjectionStatusEnum.RECOVERY_ING
					.getKey()) {
				hasRecoveryIng = true;
			} else if (subInjectionObject.getInjectionStatus() == InjectionStatusEnum.RECOVERY_SUCCESS
					.getKey()) {
				hasRecoverySuccess = true;
			} else if (subInjectionObject.getInjectionStatus() == InjectionStatusEnum.RECOVERY_FAIL
					.getKey()) {
				hasRecoveryFail = true;
			}
		}
		Integer injectionStatus = InjectionStatusEnum.INJECTION_WAIT.getKey();
		if (hasInjectionFail) {
			injectionStatus = InjectionStatusEnum.INJECTION_FAIL.getKey();
		} else if (hasRecoveryFail) {
			injectionStatus = InjectionStatusEnum.RECOVERY_FAIL.getKey();
		} else if (hasRecoverySuccess || hasRecoveryIng) {
			if (allRecoverySuccess) {
				injectionStatus = InjectionStatusEnum.RECOVERY_SUCCESS.getKey();
			} else {
				injectionStatus = InjectionStatusEnum.RECOVERY_ING.getKey();
			}
		} else if (hasInjectionSuccess || hasInjectionIng) {
			if (allInjectionSuccess) {
				injectionStatus = InjectionStatusEnum.INJECTION_SUCCESS
						.getKey();
			} else {
				injectionStatus = InjectionStatusEnum.INJECTION_ING.getKey();
			}
		}
		return injectionStatus;
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
			updateInjectionStatus(injectionPlatform, series, false);
		} else if (sendTask.getItemId() != null
				&& sendTask.getType() == InjectionObjectTypeEnum.OBJECT
						.getKey()
				&& sendTask.getItemType() == InjectionItemTypeEnum.PROGRAM
						.getKey()) {
			Program program = programRepository.findOne(sendTask.getItemId());
			updateInjectionStatus(injectionPlatform, program, false);
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
					// if (platform.getIsCallback() == YesNoEnum.YES.getKey()
					// && result == 0) {
					// // 需要回写的等待回写成功后，再更新状态为已分发(在回写接口中实现)
					// injectionObjectRepository.updateInjectionCode(
					// platformId, category,
					// InjectionItemTypeEnum.PROGRAM.getKey(), id,
					// currentTime, partnerItemCode);
					// break;
					// }
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
					// if (platform.getIsCallback() == YesNoEnum.YES.getKey()
					// && result == 0) {
					// // 需要回写的等待回写成功后，再更新状态为已分发(在回写接口中实现)
					// injectionObjectRepository.updateInjectionCode(
					// platformId, category,
					// InjectionItemTypeEnum.MOVIE.getKey(), id,
					// currentTime, partnerItemCode);
					// break;
					// }
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

	public List<InjectionPlatform> findAllInjectionPlatform(List<Long> ids) {
		return injectionPlatformRepository.findAll(ids);
	}

	public List<InjectionPlatform> findAllSendInjectionPlatform() {
		return injectionPlatformRepository
				.findAllByDirection(InjectionDirectionEnum.SEND.getKey());
	}

	public List<InjectionPlatform> findAllReceiveInjectionPlatform() {
		return injectionPlatformRepository
				.findAllByDirection(InjectionDirectionEnum.RECEIVE.getKey());
	}

	public List<InjectionPlatform> findAllIndirectInjectionPlatform() {
		return injectionPlatformRepository
				.findAllByDirection(InjectionDirectionEnum.INDIRECT.getKey());
	}

	public List<InjectionPlatform> findAllInjectionPlatform() {
		return injectionPlatformRepository.findAllByValid();
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

	public void updateInjectionStatus(InjectionPlatform injectionPlatform,
			Series series, boolean deliver) {
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
				&& injectionPlatform.getDirection() == InjectionDirectionEnum.SEND
						.getKey()
				&& injectionPlatform.getType() == PlatformTypeEnum.BUSINESS_SYSTEM
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

		if (deliver) {
			List<Program> programList = programRepository.findBySeriesId(series
					.getId());
			for (Program program : programList) {
				updateInjectionStatus(injectionPlatform, program, deliver);
			}
		}
	}

	public void updateInjectionStatus(InjectionPlatform injectionPlatform,
			Program program, boolean deliver) {
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
				&& injectionPlatform.getDirection() == InjectionDirectionEnum.SEND
						.getKey()
				&& injectionPlatform.getType() == PlatformTypeEnum.BUSINESS_SYSTEM
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

		if (deliver) {
			List<MediaFile> mediaFileList = mediaFileRepository
					.findByProgramId(program.getId());
			for (MediaFile mediaFile : mediaFileList) {
				updateInjectionStatus(injectionPlatform, mediaFile);
			}
		}
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
				&& injectionPlatform.getDirection() == InjectionDirectionEnum.SEND
						.getKey()
				&& injectionPlatform.getType() == PlatformTypeEnum.BUSINESS_SYSTEM
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
			InjectionPlatform platform, String category) {
		InjectionObject injectionObject = injectionObjectRepository
				.findOneByPlatformIdAndCategoryAndItemTypeAndItemId(
						platform.getId(), category,
						InjectionItemTypeEnum.SERIES.getKey(), series.getId());
		if (injectionObject == null) {
			injectionObject = new InjectionObject(platform.getId(), category);
			injectionObject.setItemType(InjectionItemTypeEnum.SERIES.getKey());
			injectionObject.setItemId(series.getId());
		}
		if (platform.getUseGlobalCode() == YesNoEnum.YES.getKey()
				&& StringUtils.isNotEmpty(series.getPlayCode())) {
			injectionObject.setPartnerItemCode(series.getPlayCode());
		}
		return injectionObject;
	}

	public InjectionObject getAndNewInjectionObject(Program program,
			InjectionPlatform platform, String category) {
		InjectionObject injectionObject = injectionObjectRepository
				.findOneByPlatformIdAndCategoryAndItemTypeAndItemId(
						platform.getId(), category,
						InjectionItemTypeEnum.PROGRAM.getKey(), program.getId());
		if (injectionObject == null) {
			injectionObject = new InjectionObject(platform.getId(), category);
			injectionObject.setItemType(InjectionItemTypeEnum.PROGRAM.getKey());
			injectionObject.setItemId(program.getId());
			injectionObject.setItemParentId(program.getSeriesId());
		}
		if (platform.getUseGlobalCode() == YesNoEnum.YES.getKey()
				&& StringUtils.isNotEmpty(program.getPlayCode())) {
			injectionObject.setPartnerItemCode(program.getPlayCode());
		}
		return injectionObject;
	}

	public InjectionObject getAndNewInjectionObject(MediaFile mediaFile,
			InjectionPlatform platform, String category) {
		InjectionObject injectionObject = injectionObjectRepository
				.findOneByPlatformIdAndCategoryAndItemTypeAndItemId(
						platform.getId(), category,
						InjectionItemTypeEnum.MOVIE.getKey(), mediaFile.getId());
		if (injectionObject == null) {
			injectionObject = new InjectionObject(platform.getId(), category);
			injectionObject.setItemType(InjectionItemTypeEnum.MOVIE.getKey());
			injectionObject.setItemId(mediaFile.getId());
			injectionObject.setItemParentId(mediaFile.getProgramId());
		}
		if (platform.getUseGlobalCode() == YesNoEnum.YES.getKey()
				&& StringUtils.isNotEmpty(mediaFile.getPlayCode())) {
			injectionObject.setPartnerItemCode(mediaFile.getPlayCode());
		}
		return injectionObject;
	}

	public InjectionObject findByPlatformIdAndItemTypeAndPartnerItemCode(
			Long platformId, Integer itemType, String partnerItemCode) {
		List<InjectionObject> injectionObjectList = injectionObjectRepository
				.findByPlatformIdAndItemTypeAndPartnerItemCode(platformId,
						itemType, partnerItemCode);
		if (injectionObjectList != null && injectionObjectList.size() > 0) {
			return injectionObjectList.get(0);
		}
		return null;
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

	public String getNameAdditionalSuffix(Series series, String category) {
		return series.getName();
	}

	public String getNameAdditionalSuffix(Series series, Program program,
			String category) {
		if (series == null) {
			return program.getName();
		}
		return series.getName() + " " + program.getName();
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
		if (injectionStatus == InjectionStatusEnum.INJECTION_SUCCESS.getKey()
				|| injectionStatus == InjectionStatusEnum.RECOVERY_FAIL
						.getKey()) {
			return true;
		}
		return false;
	}

	/**
	 * 分发剧头
	 */
	public synchronized void inInjection(Series series, Long[] platformIds,
			String[] templateIds, Integer[] prioritys, String cpCode) {
		if (series == null || platformIds == null || templateIds == null) {
			return;
		}
		for (int i = 0; i < platformIds.length; i++) {
			InjectionPlatform injectionPlatform = injectionPlatformRepository
					.getOne(platformIds[i]);
			if (injectionPlatform == null) {
				continue;
			}
			Map<String, String> categoryMap = getCategoryMapByTemplateIds(templateIds[i]);
			for (String category : categoryMap.keySet()) {
				InjectionObject seriesInjectionObject = getAndNewInjectionObject(
						series, injectionPlatform, category);
				inInjection(injectionPlatform, series, seriesInjectionObject,
						category, categoryMap.get(category), prioritys[i],
						cpCode);
			}
		}
		updateInjectionStatus(null, series, true);
	}

	private synchronized Long inInjection(InjectionPlatform injectionPlatform,
			Series series, InjectionObject seriesInjectionObject,
			String category, String templateId, Integer priority, String cpCode) {
		Long taskId = null;
		if (!isInjection(seriesInjectionObject.getInjectionStatus())) {
			SendTask sendTask = new SendTask(seriesInjectionObject);
			sendTask.setCpCode(cpCode);
			sendTask.setType(InjectionObjectTypeEnum.OBJECT.getKey());
			if (isInjected(seriesInjectionObject.getInjectionStatus())) {
				sendTask.setAction(InjectionActionTypeEnum.UPDATE.getKey());
			} else {
				sendTask.setAction(InjectionActionTypeEnum.CREATE.getKey());
			}
			sendTask.setItemType(InjectionItemTypeEnum.SERIES.getKey());
			sendTask.setItemId(series.getId());
			String name = getNameAdditionalSuffix(series, category)
					+ " "
					+ InjectionActionTypeEnum
							.getEnumByKey(sendTask.getAction()).getName();
			sendTask.setName(name);
			sendTask.setPriority(priority);
			sendTask.setStatus(SendTaskStatusEnum.DEFAULT.getKey());
			sendTaskRepository.save(sendTask);
			taskId = sendTask.getId();

			seriesInjectionObject
					.setInjectionStatus(InjectionStatusEnum.INJECTION_WAIT
							.getKey());
			injectionObjectRepository.save(seriesInjectionObject);

			// 生成间接平台分发对象
			if (injectionPlatform.getIsCallback() == YesNoEnum.YES.getKey()
					&& StringUtils.isNotEmpty(injectionPlatform
							.getIndirectPlatformId())) {
				for (String indirectPlatformId : injectionPlatform
						.getIndirectPlatformId().split(",")) {
					InjectionPlatform indirectPlatform = injectionPlatformRepository
							.findOne(Long.valueOf(indirectPlatformId));
					if (indirectPlatform != null) {
						InjectionObject tempInjectionObject = getAndNewInjectionObject(
								series, indirectPlatform,
								seriesInjectionObject.getCategory());
						tempInjectionObject
								.setInjectionStatus(InjectionStatusEnum.INJECTION_WAIT
										.getKey());
						injectionObjectRepository.save(tempInjectionObject);
					}
				}
			}
		}

		// 节目后分发
		List<Program> programList = programRepository.findBySeriesId(series
				.getId());
		for (Program program : programList) {
			InjectionObject programInjectionObject = getAndNewInjectionObject(
					program, injectionPlatform,
					seriesInjectionObject.getCategory());
			inInjection(injectionPlatform, series, seriesInjectionObject,
					program, programInjectionObject, category, templateId,
					priority, cpCode, taskId);
		}
		return taskId;
	}

	/**
	 * 分发节目
	 */
	public synchronized void inInjection(Series series, Program program,
			Long[] platformIds, String[] templateIds, Integer[] prioritys,
			String cpCode) {
		if (program == null || platformIds == null || templateIds == null) {
			return;
		}
		for (int i = 0; i < platformIds.length; i++) {
			InjectionPlatform injectionPlatform = injectionPlatformRepository
					.getOne(platformIds[i]);
			if (injectionPlatform == null) {
				continue;
			}
			Map<String, String> categoryMap = getCategoryMapByTemplateIds(templateIds[i]);
			for (String category : categoryMap.keySet()) {
				InjectionObject seriesInjectionObject = null;
				if (series != null) {
					seriesInjectionObject = getAndNewInjectionObject(series,
							injectionPlatform, category);
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
					preTaskId = inInjection(injectionPlatform, series,
							seriesInjectionObject, category,
							categoryMap.get(category), prioritys[i], cpCode);
				}

				InjectionObject programInjectionObject = getAndNewInjectionObject(
						program, injectionPlatform, category);
				inInjection(injectionPlatform, series, seriesInjectionObject,
						program, programInjectionObject, category,
						categoryMap.get(category), prioritys[i], cpCode,
						preTaskId);
			}
		}
		updateInjectionStatus(null, program, true);
	}

	private synchronized Long inInjection(InjectionPlatform injectionPlatform,
			Series series, InjectionObject seriesInjectionObject,
			Program program, InjectionObject programInjectionObject,
			String category, String templateId, Integer priority,
			String cpCode, Long preTaskId) {
		if (program.getMediaStatus() != MediaStatusEnum.OK.getKey()
				&& program.getMediaStatus() != MediaStatusEnum.MISS_IMAGE
						.getKey()) {
			throw new ServiceException("节目[" + program.getName()
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
			sendTask.setCpCode(cpCode);
			sendTask.setType(InjectionObjectTypeEnum.OBJECT.getKey());
			if (isInjected(programInjectionObject.getInjectionStatus())) {
				sendTask.setAction(InjectionActionTypeEnum.UPDATE.getKey());
			} else {
				sendTask.setAction(InjectionActionTypeEnum.CREATE.getKey());
			}
			sendTask.setItemType(InjectionItemTypeEnum.PROGRAM.getKey());
			sendTask.setItemId(program.getId());
			String name = getNameAdditionalSuffix(series, program, category)
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
			taskId = sendTask.getId();

			programInjectionObject
					.setInjectionStatus(InjectionStatusEnum.INJECTION_WAIT
							.getKey());
			injectionObjectRepository.save(programInjectionObject);

			// 生成间接平台分发对象
			if (injectionPlatform.getIsCallback() == YesNoEnum.YES.getKey()
					&& StringUtils.isNotEmpty(injectionPlatform
							.getIndirectPlatformId())) {
				for (String indirectPlatformId : injectionPlatform
						.getIndirectPlatformId().split(",")) {
					InjectionPlatform indirectPlatform = injectionPlatformRepository
							.findOne(Long.valueOf(indirectPlatformId));
					if (indirectPlatform != null) {
						InjectionObject tempInjectionObject = getAndNewInjectionObject(
								program, indirectPlatform,
								programInjectionObject.getCategory());
						tempInjectionObject
								.setInjectionStatus(InjectionStatusEnum.INJECTION_WAIT
										.getKey());
						injectionObjectRepository.save(tempInjectionObject);
					}
				}
			}

		}

		for (MediaFile mediaFile : mediaFileList) {
			InjectionObject mediaFileInjectionObject = getAndNewInjectionObject(
					mediaFile, injectionPlatform,
					programInjectionObject.getCategory());
			inInjection(injectionPlatform, series, program,
					programInjectionObject, mediaFile,
					mediaFileInjectionObject, category, priority, cpCode,
					taskId);
		}
		return taskId;
	}

	private synchronized void inInjection(InjectionPlatform injectionPlatform,
			Series series, Program program,
			InjectionObject programInjectionObject, MediaFile mediaFile,
			InjectionObject mediaFileInjectionObject, String category,
			Integer priority, String cpCode, Long preTaskId) {
		if (StringUtils.isEmpty(mediaFile.getFilePath())) {
			throw new ServiceException("节目[" + program.getName()
					+ "]媒体文件路径为空，不能分发!");
		}
		if (isContainChinese(mediaFile.getFilePath())) {
			throw new ServiceException("节目[" + program.getName()
					+ "]媒体文件路径包括中文，不能分发!");
		}
		if (!isInjection(mediaFileInjectionObject.getInjectionStatus())) {
			SendTask sendTask = new SendTask(mediaFileInjectionObject);
			sendTask.setCpCode(cpCode);
			sendTask.setType(InjectionObjectTypeEnum.OBJECT.getKey());
			if (isInjected(mediaFileInjectionObject.getInjectionStatus())) {
				sendTask.setAction(InjectionActionTypeEnum.UPDATE.getKey());
			} else {
				sendTask.setAction(InjectionActionTypeEnum.CREATE.getKey());
			}
			sendTask.setItemType(InjectionItemTypeEnum.MOVIE.getKey());
			sendTask.setItemId(mediaFile.getId());
			String name = getNameAdditionalSuffix(series, program, category)
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
			mediaFileInjectionObject
					.setInjectionStatus(InjectionStatusEnum.INJECTION_WAIT
							.getKey());
			injectionObjectRepository.save(mediaFileInjectionObject);

			// 生成间接平台分发对象
			if (injectionPlatform.getIsCallback() == YesNoEnum.YES.getKey()
					&& StringUtils.isNotEmpty(injectionPlatform
							.getIndirectPlatformId())) {
				for (String indirectPlatformId : injectionPlatform
						.getIndirectPlatformId().split(",")) {
					InjectionPlatform indirectPlatform = injectionPlatformRepository
							.findOne(Long.valueOf(indirectPlatformId));
					if (indirectPlatform != null) {
						InjectionObject tempInjectionObject = getAndNewInjectionObject(
								mediaFile, indirectPlatform,
								mediaFileInjectionObject.getCategory());
						tempInjectionObject
								.setInjectionStatus(InjectionStatusEnum.INJECTION_WAIT
										.getKey());
						injectionObjectRepository.save(tempInjectionObject);
					}
				}
			}
		}
	}

	/**
	 * 回收剧头
	 */
	public synchronized void outInjection(Series series, Long[] platformIds,
			String[] templateIds, Integer[] prioritys, String cpCode) {
		if (series == null || platformIds == null || templateIds == null) {
			return;
		}
		for (int i = 0; i < platformIds.length; i++) {
			InjectionPlatform injectionPlatform = injectionPlatformRepository
					.getOne(platformIds[i]);
			if (injectionPlatform == null) {
				continue;
			}
			Map<String, String> categoryMap = getCategoryMapByTemplateIds(templateIds[i]);
			for (String category : categoryMap.keySet()) {
				InjectionObject seriesInjectionObject = getAndNewInjectionObject(
						series, injectionPlatform, category);
				outInjection(injectionPlatform, series, seriesInjectionObject,
						category, categoryMap.get(category), prioritys[i],
						cpCode);
			}
		}
		updateInjectionStatus(null, series, true);
	}

	private synchronized void outInjection(InjectionPlatform injectionPlatform,
			Series series, InjectionObject seriesInjectionObject,
			String category, String templateId, Integer priority, String cpCode) {
		// 节目先回收
		List<Program> programList = programRepository.findBySeriesId(series
				.getId());
		for (Program program : programList) {
			InjectionObject programInjectionObject = getAndNewInjectionObject(
					program, injectionPlatform,
					seriesInjectionObject.getCategory());
			outInjection(injectionPlatform, series, seriesInjectionObject,
					program, programInjectionObject, category, templateId,
					priority, cpCode);
		}

		if (isInjected(seriesInjectionObject.getInjectionStatus())) {
			SendTask sendTask = new SendTask(seriesInjectionObject);
			sendTask.setCpCode(cpCode);
			sendTask.setType(InjectionObjectTypeEnum.OBJECT.getKey());
			sendTask.setAction(InjectionActionTypeEnum.DELETE.getKey());
			sendTask.setItemType(InjectionItemTypeEnum.SERIES.getKey());
			sendTask.setItemId(series.getId());
			String name = getNameAdditionalSuffix(series, category)
					+ " "
					+ InjectionActionTypeEnum
							.getEnumByKey(sendTask.getAction()).getName();
			sendTask.setName(name);
			sendTask.setPriority(priority);
			sendTask.setStatus(SendTaskStatusEnum.DEFAULT.getKey());
			sendTaskRepository.save(sendTask);

			seriesInjectionObject
					.setInjectionStatus(InjectionStatusEnum.RECOVERY_WAIT
							.getKey());
			injectionObjectRepository.save(seriesInjectionObject);
			// 生成间接平台分发对象
			if (injectionPlatform.getIsCallback() == YesNoEnum.YES.getKey()
					&& StringUtils.isNotEmpty(injectionPlatform
							.getIndirectPlatformId())) {
				for (String indirectPlatformId : injectionPlatform
						.getIndirectPlatformId().split(",")) {
					InjectionPlatform indirectPlatform = injectionPlatformRepository
							.findOne(Long.valueOf(indirectPlatformId));
					if (indirectPlatform != null) {
						InjectionObject tempInjectionObject = getAndNewInjectionObject(
								series, indirectPlatform,
								seriesInjectionObject.getCategory());
						tempInjectionObject
								.setInjectionStatus(InjectionStatusEnum.RECOVERY_WAIT
										.getKey());
						injectionObjectRepository.save(tempInjectionObject);
					}
				}
			}
		}
	}

	/**
	 * 回收节目
	 */
	public synchronized void outInjection(Series series, Program program,
			Long[] platformIds, String[] templateIds, Integer[] prioritys,
			String cpCode) {
		if (program == null || platformIds == null || templateIds == null) {
			return;
		}
		for (int i = 0; i < platformIds.length; i++) {
			InjectionPlatform injectionPlatform = injectionPlatformRepository
					.getOne(platformIds[i]);
			if (injectionPlatform == null) {
				continue;
			}
			Map<String, String> categoryMap = getCategoryMapByTemplateIds(templateIds[i]);
			for (String category : categoryMap.keySet()) {
				InjectionObject seriesInjectionObject = null;
				if (series != null) {
					seriesInjectionObject = getAndNewInjectionObject(series,
							injectionPlatform, category);
				}

				InjectionObject programInjectionObject = getAndNewInjectionObject(
						program, injectionPlatform, category);

				outInjection(injectionPlatform, series, seriesInjectionObject,
						program, programInjectionObject, category,
						categoryMap.get(category), prioritys[i], cpCode);
			}
		}
		updateInjectionStatus(null, program, true);
	}

	private synchronized void outInjection(InjectionPlatform injectionPlatform,
			Series series, InjectionObject seriesInjectionObject,
			Program program, InjectionObject programInjectionObject,
			String category, String templateId, Integer priority, String cpCode) {
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
					mediaFile, injectionPlatform,
					programInjectionObject.getCategory());
			outInjection(injectionPlatform, series, program,
					programInjectionObject, mediaFile,
					mediaFileInjectionObject, category, priority, cpCode);
		}

		if (isInjected(programInjectionObject.getInjectionStatus())) {
			SendTask sendTask = new SendTask(programInjectionObject);
			sendTask.setCpCode(cpCode);
			sendTask.setType(InjectionObjectTypeEnum.OBJECT.getKey());
			sendTask.setAction(InjectionActionTypeEnum.DELETE.getKey());
			sendTask.setItemType(InjectionItemTypeEnum.PROGRAM.getKey());
			sendTask.setItemId(program.getId());
			String name = getNameAdditionalSuffix(series, program, category)
					+ " "
					+ InjectionActionTypeEnum
							.getEnumByKey(sendTask.getAction()).getName();
			sendTask.setName(name);
			sendTask.setPriority(priority);
			sendTask.setStatus(SendTaskStatusEnum.DEFAULT.getKey());

			// 记录媒体内容的Id
			sendTask.setSubItemId(subItemId);
			sendTaskRepository.save(sendTask);

			programInjectionObject
					.setInjectionStatus(InjectionStatusEnum.RECOVERY_WAIT
							.getKey());
			injectionObjectRepository.save(programInjectionObject);
			// 生成间接平台分发对象
			if (injectionPlatform.getIsCallback() == YesNoEnum.YES.getKey()
					&& StringUtils.isNotEmpty(injectionPlatform
							.getIndirectPlatformId())) {
				for (String indirectPlatformId : injectionPlatform
						.getIndirectPlatformId().split(",")) {
					InjectionPlatform indirectPlatform = injectionPlatformRepository
							.findOne(Long.valueOf(indirectPlatformId));
					if (indirectPlatform != null) {
						InjectionObject tempInjectionObject = getAndNewInjectionObject(
								program, indirectPlatform,
								programInjectionObject.getCategory());
						tempInjectionObject
								.setInjectionStatus(InjectionStatusEnum.RECOVERY_WAIT
										.getKey());
						injectionObjectRepository.save(tempInjectionObject);
					}
				}
			}
		}
	}

	private synchronized void outInjection(InjectionPlatform injectionPlatform,
			Series series, Program program,
			InjectionObject programInjectionObject, MediaFile mediaFile,
			InjectionObject mediaFileInjectionObject, String category,
			Integer priority, String cpCode) {
		if (isInjected(mediaFileInjectionObject.getInjectionStatus())) {
			SendTask sendTask = new SendTask(mediaFileInjectionObject);
			sendTask.setCpCode(cpCode);
			sendTask.setType(InjectionObjectTypeEnum.OBJECT.getKey());
			sendTask.setAction(InjectionActionTypeEnum.DELETE.getKey());
			sendTask.setItemType(InjectionItemTypeEnum.MOVIE.getKey());
			sendTask.setItemId(mediaFile.getId());
			String name = getNameAdditionalSuffix(series, program, category)
					+ " "
					+ InjectionActionTypeEnum
							.getEnumByKey(sendTask.getAction()).getName();
			sendTask.setName(name);
			sendTask.setPriority(priority);
			sendTask.setStatus(SendTaskStatusEnum.DEFAULT.getKey());
			// 媒体内容暂不需要生成任务,会同节目一起分发
			// sendTaskRepository.save(sendTask);

			mediaFileInjectionObject
					.setInjectionStatus(InjectionStatusEnum.RECOVERY_WAIT
							.getKey());
			injectionObjectRepository.save(mediaFileInjectionObject);

			// 生成间接平台分发对象
			if (injectionPlatform.getIsCallback() == YesNoEnum.YES.getKey()
					&& StringUtils.isNotEmpty(injectionPlatform
							.getIndirectPlatformId())) {
				for (String indirectPlatformId : injectionPlatform
						.getIndirectPlatformId().split(",")) {
					InjectionPlatform indirectPlatform = injectionPlatformRepository
							.findOne(Long.valueOf(indirectPlatformId));
					if (indirectPlatform != null) {
						InjectionObject tempInjectionObject = getAndNewInjectionObject(
								mediaFile, indirectPlatform,
								mediaFileInjectionObject.getCategory());
						tempInjectionObject
								.setInjectionStatus(InjectionStatusEnum.RECOVERY_WAIT
										.getKey());
						injectionObjectRepository.save(tempInjectionObject);
					}
				}
			}
		}
	}

}
