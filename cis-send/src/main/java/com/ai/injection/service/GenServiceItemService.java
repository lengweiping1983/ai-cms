//package com.ai.injection.service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.time.DateFormatUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.ai.cms.injection.bean.ADI;
//import com.ai.cms.injection.bean.MappingBean;
//import com.ai.cms.injection.entity.InjectionPlatform;
//import com.ai.cms.injection.entity.SendEvent;
//import com.ai.cms.injection.entity.SendTask;
//import com.ai.cms.injection.enums.InjectionActionTypeEnum;
//import com.ai.cms.injection.enums.InjectionItemTypeEnum;
//import com.ai.cms.injection.enums.InjectionObjectTypeEnum;
//import com.ai.cms.injection.enums.SendTaskStatusEnum;
//import com.ai.epg.product.entity.Service;
//import com.ai.epg.product.entity.ServiceItem;
//import com.ai.epg.product.repository.ServiceItemRepository;
//import com.ai.epg.product.repository.ServiceRepository;
//
//@org.springframework.stereotype.Service
//@Transactional(readOnly = true)
//public class GenServiceItemService extends GenCommonService {
//
//	@Autowired
//	private ServiceRepository serviceRepository;
//	
//	@Autowired
//	private ServiceItemRepository serviceItemRepository;
//
//	/**
//	 * 生成服务项任务，同时需要合并服务项相关事件
//	 * 
//	 * @param sendEvent
//	 * @param service
//	 * @param priority
//	 * @return
//	 */
//	@Transactional(readOnly = false)
//	public SendTask genServiceItemTask(SendEvent sendEvent, Service service) {
//		// 1. 生成任务
//		SendTask sendTask = new SendTask(sendEvent);
//		sendTask.setType(sendEvent.getType());
//		sendTask.setAction(sendEvent.getAction());
//
//		InjectionActionTypeEnum actionEnum = InjectionActionTypeEnum
//				.getEnumByKey(sendEvent.getAction());
//
//		sendTask.setName(sendEvent.getItemName() + " " + "打包" + " "
//				+ ((actionEnum != null) ? actionEnum.getName() : ""));
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
//					.findByTypeAndParentItemTypeAndParentItemId(sendEvent.getPlatformId(),sendEvent.getCategory(),
//							InjectionObjectTypeEnum.MAPPING.getKey(),
//							InjectionItemTypeEnum.PACKAGE.getKey(),
//							sendEvent.getParentItemId());
//		} else {
//			eventList = new ArrayList<SendEvent>();
//			eventList.add(sendEvent);
//		}
//
//		// 3.设置优先级
//		sendTask.setPriority(getPriority(eventList, sendEvent.getPriority()));
//
//		// 4.生成服务任务
//		ADI adi = new ADI();
//		for (SendEvent objectEvent : eventList) {
//			MappingBean mappingBean = genServiceItemMappingBean(objectEvent);
//			adi.getMappings().add(mappingBean);
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
//	/**
//	 * 根据事件生成映射对象
//	 * 
//	 * @param sendEvent
//	 * @return
//	 */
//	public MappingBean genServiceItemMappingBean(SendEvent sendEvent) {
//		MappingBean mappingBean = new MappingBean();
//
//		if (sendEvent.getAction() == InjectionActionTypeEnum.CREATE.getKey()) {
//			mappingBean.setAction(InjectionActionTypeEnum.CREATE.getValue());
//		} else if (sendEvent.getAction() == InjectionActionTypeEnum.UPDATE
//				.getKey()) {
//			mappingBean.setAction(InjectionActionTypeEnum.UPDATE.getValue());
//		} else if (sendEvent.getAction() == InjectionActionTypeEnum.DELETE
//				.getKey()) {
//			mappingBean.setAction(InjectionActionTypeEnum.DELETE.getValue());
//		}
//
//		if (sendEvent.getRelId() != null
//				&& sendEvent.getParentItemId() != null) {
//			Service service = serviceRepository.findOne(sendEvent
//					.getParentItemId());
//			if (service != null) {
//				if (service.getType() != null) {
//					mappingBean.setParentSubType("" + service.getType());
//				}
//			}
//			ServiceItem serviceItem = serviceItemRepository.findOne(sendEvent.getRelId());
//
//			if (serviceItem != null) {
//				if (serviceItem.getValidTime() != null) {
//					try {
//						String date = DateFormatUtils.format(
//								serviceItem.getValidTime(), "yyyyMMddHHmmss");
//						mappingBean.setValidStart(date);
//					} catch (Exception e) {
//						log.error(e.getMessage(), e);
//					}
//				}
//				if (serviceItem.getExpiredTime() != null) {
//					try {
//						String date = DateFormatUtils.format(
//								serviceItem.getExpiredTime(), "yyyyMMddHHmmss");
//						mappingBean.setValidEnd(date);
//					} catch (Exception e) {
//						log.error(e.getMessage(), e);
//					}
//				}
//			}
//		}
//
//		InjectionItemTypeEnum itemTypeEnum = InjectionItemTypeEnum
//				.getEnumByKey(sendEvent.getItemType());
//		InjectionItemTypeEnum parentItemTypeEnum = InjectionItemTypeEnum
//				.getEnumByKey(sendEvent.getParentItemType());
//
//		mappingBean.setElementType(itemTypeEnum.getValue());
//		mappingBean.setElementID("" + sendEvent.getItemId());
//		mappingBean.setElementCode(sendEvent.getItemCode());
//		mappingBean.setPartnerItemCode(sendEvent.getPartnerItemCode());
//
//		mappingBean.setParentType(parentItemTypeEnum.getValue());
//		mappingBean.setParentID("" + sendEvent.getParentItemId());
//		mappingBean.setParentCode(sendEvent.getParentItemCode());
//		mappingBean.setParentPartnerItemCode(sendEvent
//				.getParentPartnerItemCode());
//
//		mappingBean.setElementName(sendEvent.getItemName());
//		mappingBean.setParentName(sendEvent.getParentItemName());
//		if (sendEvent.getSortIndex() != null) {
//			mappingBean.setSequence("" + sendEvent.getSortIndex());
//		}
//		mappingBean.setRelId(sendEvent.getRelId());
//
//		return mappingBean;
//	}
//}