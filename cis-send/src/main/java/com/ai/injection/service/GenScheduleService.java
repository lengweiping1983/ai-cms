//package com.ai.injection.service;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.ai.cms.injection.bean.ADI;
//import com.ai.cms.injection.bean.ScheduleBean;
//import com.ai.cms.injection.entity.InjectionPlatform;
//import com.ai.cms.injection.entity.SendEvent;
//import com.ai.cms.injection.entity.SendTask;
//import com.ai.cms.injection.enums.InjectionActionTypeEnum;
//import com.ai.cms.injection.enums.InjectionItemTypeEnum;
//import com.ai.cms.injection.enums.InjectionObjectTypeEnum;
//import com.ai.cms.injection.enums.SendTaskStatusEnum;
//import com.ai.cms.injection.repository.InjectionObjectRepository;
//import com.ai.cms.live.entity.ChannelConfig;
//import com.ai.cms.live.entity.Schedule;
//import com.ai.cms.live.repository.ChannelConfigRepository;
//import com.ai.cms.live.repository.ChannelRepository;
//import com.ai.cms.live.repository.ScheduleRepository;
//import com.ai.common.enums.OnlineStatusEnum;
//
//@Service
//@Transactional(readOnly = true)
//public class GenScheduleService extends GenCommonService {
//
//	@Autowired
//	private ChannelRepository channelRepository;
//
//	@Autowired
//	private ScheduleRepository scheduleRepository;
//
//	@Autowired
//	private ChannelConfigRepository channelConfigRepository;
//
//	@Autowired
//	private InjectionObjectRepository injectionObjectRepository;
//
//	public ChannelConfig getChannelConfig(Long channelId) {
//		List<ChannelConfig> channelConfigList = channelConfigRepository
//				.findByChannelId(channelId);
//		if (channelConfigList != null && channelConfigList.size() > 0) {
//			for (ChannelConfig channelConfig : channelConfigList) {
//				if (channelConfig.getStatus() == OnlineStatusEnum.ONLINE
//						.getKey()) {
//					return channelConfig;
//				}
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * 生成时间表任务，同时需要合并时间表相关事件
//	 * 
//	 * @param sendEvent
//	 * @param priority
//	 * @return
//	 */
//	@Transactional(readOnly = false)
//	public SendTask genScheduleTask(SendEvent sendEvent) {
//		// 1. 生成任务
//		SendTask sendTask = new SendTask(sendEvent);
//		sendTask.setType(sendEvent.getType());
//		sendTask.setAction(sendEvent.getAction());
//
//		InjectionActionTypeEnum actionEnum = InjectionActionTypeEnum
//				.getEnumByKey(sendEvent.getAction());
//
//		ChannelConfig channelConfig = getChannelConfig(sendEvent
//				.getParentItemId());
//		if (channelConfig != null) {
//			sendTask.setName(channelConfig.getPartnerChannelName() + " "
//					+ "时间表" + " "
//					+ ((actionEnum != null) ? actionEnum.getName() : ""));
//		} else {
//			sendTask.setName("时间表" + " "
//					+ ((actionEnum != null) ? actionEnum.getName() : ""));
//		}
//
//		sendTask.setItemType(sendEvent.getItemType());
//		sendTask.setItemId(sendEvent.getItemId());
//		sendTask.setStatus(SendTaskStatusEnum.WAIT.getKey());
//		InjectionPlatform platform = injectionPlatformRepository.findOne(sendEvent.getPlatformId());
//		sendTask.setPlatform(platform);
//		sendTaskRepository.save(sendTask);
//		String correlateId = StringUtils.trimToEmpty(sendTask.getPlatform()
//				.getCorrelatePrefix()) + sendTask.getId();
//		sendTask.setCorrelateId(correlateId);
//
//		// 2.获取所有相关的事件列表
//		List<SendEvent> eventList = null;
//		if (sendEvent.getParentItemId() != null) {
//			eventList = sendEventRepository
//					.findByTypeAndParentItemTypeAndParentItemId(
//							sendEvent.getPlatformId(), sendEvent.getCategory(),
//							InjectionObjectTypeEnum.OBJECT.getKey(),
//							InjectionItemTypeEnum.CHANNEL.getKey(),
//							sendEvent.getParentItemId());
//		} else {
//			eventList = new ArrayList<SendEvent>();
//			eventList.add(sendEvent);
//		}
//
//		// 3.设置优先级
//		sendTask.setPriority(getPriority(eventList, sendEvent.getPriority()));
//
//		// 4.生成任务
//		ADI adi = new ADI();
//		for (SendEvent objectEvent : eventList) {
//			ScheduleBean scheduleBean = genScheduleBean(objectEvent,
//					channelConfig);
//			adi.getObjects().add(scheduleBean);
//		}
//
//		// 5.生成xml
//		genXML(sendTask, adi);
//
//		// 6.合并相关事件
//		mergeEvent(eventList, correlateId);
//
//		// 7.保存任务
//		sendTaskRepository.save(sendTask);
//
//		return sendTask;
//	}
//
//	public ScheduleBean genScheduleBean(SendEvent sendEvent,
//			ChannelConfig channelConfig) {
//		ScheduleBean scheduleBean = new ScheduleBean();
//
//		if (sendEvent.getAction() == InjectionActionTypeEnum.CREATE.getKey()) {
//			scheduleBean.setAction(InjectionActionTypeEnum.CREATE.getValue());
//		} else if (sendEvent.getAction() == InjectionActionTypeEnum.UPDATE
//				.getKey()) {
//			scheduleBean.setAction(InjectionActionTypeEnum.UPDATE.getValue());
//		} else if (sendEvent.getAction() == InjectionActionTypeEnum.DELETE
//				.getKey()) {
//			scheduleBean.setAction(InjectionActionTypeEnum.DELETE.getValue());
//		}
//
//		Schedule schedule = scheduleRepository.findOne(sendEvent.getItemId());
//		if (schedule != null) {
//			SimpleDateFormat timedf = new SimpleDateFormat("HHmmss");
//			Date currentTime = new Date();
//			currentTime.setTime(schedule.getDuration() * 60 * 1000 - 8 * 3600
//					* 1000);
//			scheduleBean.setDuration(timedf.format(currentTime));
//			scheduleBean.setGenre(schedule.getTag());
//			scheduleBean.setProgramName(schedule.getProgramName());
//			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//			String beginTime = df.format(schedule.getBeginTime());
//			scheduleBean.setStartDate(beginTime.substring(0, 8));
//			scheduleBean.setStartTime(beginTime.substring(8, 14));
//			scheduleBean
//					.setStatus(schedule.getStatus() == OnlineStatusEnum.OFFLINE
//							.getKey() ? "0" : "1");
//		}
//
//		scheduleBean.setID("" + sendEvent.getItemId());
//		scheduleBean.setCode(sendEvent.getItemCode());
//		scheduleBean.setPartnerItemCode(sendEvent.getPartnerItemCode());
//
//		scheduleBean.setChannelID("" + sendEvent.getParentItemId());
//		scheduleBean.setChannelCode(sendEvent.getParentItemCode());
//		scheduleBean.setParentPartnerItemCode(sendEvent
//				.getParentPartnerItemCode());
//
//		return scheduleBean;
//	}
//
//}