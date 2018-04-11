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
//import com.ai.cms.injection.bean.ServiceBean;
//import com.ai.cms.injection.entity.InjectionPlatform;
//import com.ai.cms.injection.entity.SendEvent;
//import com.ai.cms.injection.entity.SendTask;
//import com.ai.cms.injection.enums.InjectionActionTypeEnum;
//import com.ai.cms.injection.enums.InjectionItemTypeEnum;
//import com.ai.cms.injection.enums.InjectionObjectTypeEnum;
//import com.ai.cms.injection.enums.InjectionStatusEnum;
//import com.ai.cms.injection.enums.SendTaskStatusEnum;
//import com.ai.common.enums.OnlineStatusEnum;
//import com.ai.epg.product.entity.Service;
//import com.ai.epg.product.repository.ServiceRepository;
//
//@org.springframework.stereotype.Service
//@Transactional(readOnly = true)
//public class GenServiceService extends GenCommonService {
//
//	@Autowired
//	private ServiceRepository serviceRepository;
//
//	/**
//	 * 生成服务任务，同时需要合并服务相关事件
//	 * 
//	 * @param sendEvent
//	 * @param service
//	 * @param priority
//	 * @return
//	 */
//	@Transactional(readOnly = false)
//	public SendTask genServiceTask(SendEvent sendEvent, Service service) {
//		// 1. 生成任务
//		SendTask sendTask = new SendTask(sendEvent);
//		sendTask.setType(InjectionObjectTypeEnum.OBJECT.getKey());
//		sendTask.setItemType(InjectionItemTypeEnum.PACKAGE.getKey());
//		sendTask.setItemId(service.getId());
//		sendTask.setStatus(SendTaskStatusEnum.WAIT.getKey());
//		InjectionPlatform platform = injectionPlatformRepository.findOne(sendEvent.getPlatformId());
//		sendTask.setPlatform(platform);
//		sendTaskRepository.save(sendTask);
//		String correlateId = StringUtils.trimToEmpty(sendTask.getPlatform()
//				.getCorrelatePrefix()) + sendTask.getId();
//		sendTask.setCorrelateId(correlateId);
//
//		// 2.获取所有相关的事件列表
//		List<SendEvent> eventList = new ArrayList<SendEvent>();
//		eventList.add(sendEvent);
//
//		// 3.设置优先级
//		sendTask.setPriority(getPriority(eventList, sendEvent.getPriority()));
//
//		// 4.生成服务任务
//		ADI adi = new ADI();
//		genServiceTask(adi, sendEvent, eventList, service, sendTask);
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
//	@Transactional(readOnly = false)
//	public boolean genServiceTask(ADI adi, SendEvent sendEvent,
//			List<SendEvent> eventList, Service service, SendTask sendTask) {
//		if (service != null) {
//			for (SendEvent objectEvent : eventList) {
//				if (objectEvent.getType() == InjectionObjectTypeEnum.OBJECT
//						.getKey()
//						&& objectEvent.getItemType() == InjectionItemTypeEnum.PACKAGE
//								.getKey()
//						&& objectEvent.getItemId() != null
//						&& objectEvent.getItemId().longValue() == service
//								.getId().longValue()
//						&& objectEvent.getAction() == InjectionActionTypeEnum.DELETE
//								.getKey()) {
//					// 有删除操作，只需要发送删除命令
//					return genDeleteServiceTask(adi, objectEvent, eventList,
//							service, sendTask);
//				}
//			}
//			if (sendEvent.getAction() == InjectionActionTypeEnum.CREATE
//					.getKey()) {// 发送增加命令
//				return genCreateServiceTask(adi, service, sendTask);
//			} else if (sendEvent.getAction() == InjectionActionTypeEnum.DELETE
//					.getKey()) {// 发送删除命令
//				return genDeleteServiceTask(adi, sendEvent, eventList, service,
//						sendTask);
//			} else {// 发送修改命令
//				return genUpdateServiceTask(adi, eventList, service, sendTask);
//			}
//		} else {// 服务不存在了，只需要发送删除命令
//			return genDeleteServiceTask(adi, sendEvent, eventList, service,
//					sendTask);
//		}
//	}
//
//	@Transactional(readOnly = false)
//	private boolean genCreateServiceTask(ADI adi, Service service,
//			SendTask sendTask) {
//		// 1.生成服务操作对象
//		ServiceBean serviceBean = genServiceBean(service);
//		serviceBean.setAction(InjectionActionTypeEnum.CREATE.getValue());
//		adi.getObjects().add(serviceBean);
//
//		if (service.getInjectionStatus() != InjectionStatusEnum.INJECTION
//				.getKey()
//				&& service.getInjectionStatus() != InjectionStatusEnum.REINJECTION
//						.getKey()) {
//			// 设置对象分发状态为分发中
//			service.setInjectionStatus(InjectionStatusEnum.INJECTION.getKey());
//			serviceRepository.save(service);
//		}
//
//		sendTask.setAction(InjectionActionTypeEnum.CREATE.getKey());
//		sendTask.setName(service.getName() + " "
//				+ InjectionActionTypeEnum.CREATE.getName());
//		return true;
//	}
//
//	@Transactional(readOnly = false)
//	private boolean genUpdateServiceTask(ADI adi, List<SendEvent> eventList,
//			Service service, SendTask sendTask) {
//		// 1.生成服务操作对象
//		ServiceBean serviceBean = genServiceBean(service);
//		serviceBean.setAction(InjectionActionTypeEnum.UPDATE.getValue());
//		adi.getObjects().add(serviceBean);
//
//		if (service.getInjectionStatus() != InjectionStatusEnum.INJECTION
//				.getKey()
//				&& service.getInjectionStatus() != InjectionStatusEnum.REINJECTION
//						.getKey()) {
//			// 设置对象分发状态为分发中
//			service.setInjectionStatus(InjectionStatusEnum.INJECTION.getKey());
//			serviceRepository.save(service);
//		}
//
//		sendTask.setAction(InjectionActionTypeEnum.UPDATE.getKey());
//		sendTask.setName(service.getName() + " "
//				+ InjectionActionTypeEnum.UPDATE.getName());
//		return true;
//	}
//
//	@Transactional(readOnly = false)
//	private boolean genDeleteServiceTask(ADI adi, SendEvent sendEvent,
//			List<SendEvent> eventList, Service service, SendTask sendTask) {
//		// 1.生成服务操作对象
//		ServiceBean serviceBean = genServiceBean(sendEvent, service);
//		serviceBean.setName(sendEvent.getItemName());// 删除需要名称
//		serviceBean.setAction(InjectionActionTypeEnum.DELETE.getValue());
//		adi.getObjects().add(serviceBean);
//
//		sendTask.setAction(InjectionActionTypeEnum.DELETE.getKey());
//		sendTask.setName(sendEvent.getItemName() + " "
//				+ InjectionActionTypeEnum.DELETE.getName());
//		return true;
//	}
//
//	private ServiceBean genServiceBean(Service service) {
//		ServiceBean serviceBean = new ServiceBean();
//		serviceBean.setID("" + service.getId());
//		serviceBean.setCode("" + service.getId());
//		serviceBean.setPartnerItemCode(service.getPartnerItemCode());
//		serviceBean.setName(service.getName());
//		if (service.getType() != null) {
//			serviceBean.setType("" + service.getType());
//		}
//		if (service.getLicensingWindowStart() != null) {
//			try {
//				String date = DateFormatUtils.format(
//						service.getLicensingWindowStart(), "yyyyMMddHHmmss");
//				serviceBean.setLicensingWindowStart(date);
//			} catch (Exception e) {
//				log.error(e.getMessage(), e);
//			}
//		}
//		if (service.getLicensingWindowEnd() != null) {
//			try {
//				String date = DateFormatUtils.format(
//						service.getLicensingWindowEnd(), "yyyyMMddHHmmss");
//				serviceBean.setLicensingWindowEnd(date);
//			} catch (Exception e) {
//				log.error(e.getMessage(), e);
//			}
//		}
//		if (service.getPrice() != null) {
//			serviceBean.setPrice("" + service.getPrice());
//		}
//		serviceBean.setStatus(service.getStatus() == OnlineStatusEnum.OFFLINE
//				.getKey() ? "0" : "1");
//		return serviceBean;
//	}
//
//	private ServiceBean genServiceBean(SendEvent sendEvent, Service service) {
//		ServiceBean serviceBean = new ServiceBean();
//		serviceBean.setID("" + sendEvent.getItemId());
//		serviceBean.setCode(sendEvent.getItemCode());
//		serviceBean.setPartnerItemCode(service.getPartnerItemCode());
//		return serviceBean;
//	}
//
//}