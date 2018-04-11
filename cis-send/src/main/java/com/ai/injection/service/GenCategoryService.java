//package com.ai.injection.service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.ai.cms.injection.bean.ADI;
//import com.ai.cms.injection.bean.CategoryBean;
//import com.ai.cms.injection.entity.InjectionPlatform;
//import com.ai.cms.injection.entity.SendEvent;
//import com.ai.cms.injection.entity.SendTask;
//import com.ai.cms.injection.enums.InjectionActionTypeEnum;
//import com.ai.cms.injection.enums.InjectionItemTypeEnum;
//import com.ai.cms.injection.enums.InjectionObjectTypeEnum;
//import com.ai.cms.injection.enums.InjectionStatusEnum;
//import com.ai.cms.injection.enums.SendTaskStatusEnum;
//import com.ai.common.enums.OnlineStatusEnum;
//import com.ai.epg.category.entity.Category;
//import com.ai.epg.category.repository.CategoryRepository;
//
//@Service
//@Transactional(readOnly = true)
//public class GenCategoryService extends GenCommonService {
//
//	@Autowired
//	private CategoryRepository categoryRepository;
//	
//	@Value("${injection.rootCategory.id:}")
//	private String rootCategoryId;
//	
//	@Value("${injection.rootCategory.code:}")
//	private String rootCategoryCode;
//
//	/**
//	 * 生成栏目任务，同时需要合并栏目相关事件
//	 * 
//	 * @param sendEvent
//	 * @param category
//	 * @param priority
//	 * @return
//	 */
//	@Transactional(readOnly = false)
//	public SendTask genCategoryTask(SendEvent sendEvent, Category category) {
//		Long itemId = category.getId();
//
//		// 1. 生成任务
//		SendTask sendTask = new SendTask(sendEvent);
//		sendTask.setType(InjectionObjectTypeEnum.OBJECT.getKey());
//		sendTask.setItemType(InjectionItemTypeEnum.CATEGORY.getKey());
//		sendTask.setItemId(itemId);
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
//		// 4.生成栏目任务
//		ADI adi = new ADI();
//		genCategoryTask(adi, sendEvent, eventList, category, sendTask);
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
//	public boolean genCategoryTask(ADI adi, SendEvent sendEvent,
//			List<SendEvent> eventList, Category category, SendTask sendTask) {
//		if (category != null) {
//			for (SendEvent objectEvent : eventList) {
//				if (objectEvent.getType() == InjectionObjectTypeEnum.OBJECT
//						.getKey()
//						&& objectEvent.getItemType() == InjectionItemTypeEnum.CATEGORY
//								.getKey()
//						&& objectEvent.getItemId() != null
//						&& objectEvent.getItemId().longValue() == category
//								.getId().longValue()
//						&& objectEvent.getAction() == InjectionActionTypeEnum.DELETE
//								.getKey()) {
//					// 有删除操作，只需要发送删除命令
//					return genDeleteCategoryTask(adi, objectEvent, eventList,
//							category, sendTask);
//				}
//			}
//			if (sendEvent.getAction() == InjectionActionTypeEnum.CREATE
//					.getKey()) {// 发送增加命令
//				return genCreateCategoryTask(adi, category, sendTask);
//			} else if (sendEvent.getAction() == InjectionActionTypeEnum.DELETE
//					.getKey()) {// 发送删除命令
//				return genDeleteCategoryTask(adi, sendEvent, eventList,
//						category, sendTask);
//			} else {// 发送修改命令
//				return genUpdateCategoryTask(adi, eventList, category, sendTask);
//			}
//		} else {// 栏目不存在了，只需要发送删除命令
//			return genDeleteCategoryTask(adi, sendEvent, eventList, category,
//					sendTask);
//		}
//	}
//
//	@Transactional(readOnly = false)
//	private boolean genCreateCategoryTask(ADI adi, Category category,
//			SendTask sendTask) {
//		// 1.生成栏目操作对象
//		CategoryBean categoryBean = genCategoryBean(category);
//		categoryBean.setAction(InjectionActionTypeEnum.CREATE.getValue());
//		adi.getObjects().add(categoryBean);
//
//		if (category.getInjectionStatus() != InjectionStatusEnum.INJECTION
//				.getKey()
//				&& category.getInjectionStatus() != InjectionStatusEnum.REINJECTION
//						.getKey()) {
//			// 设置对象分发状态为分发中
//			category.setInjectionStatus(InjectionStatusEnum.INJECTION.getKey());
//			categoryRepository.save(category);
//		}
//
//		sendTask.setAction(InjectionActionTypeEnum.CREATE.getKey());
//		sendTask.setName(category.getTitle() + " "
//				+ InjectionActionTypeEnum.CREATE.getName());
//		return true;
//	}
//
//	@Transactional(readOnly = false)
//	private boolean genUpdateCategoryTask(ADI adi, List<SendEvent> eventList,
//			Category category, SendTask sendTask) {
//		// 1.生成栏目操作对象
//		CategoryBean categoryBean = genCategoryBean(category);
//		categoryBean.setAction(InjectionActionTypeEnum.UPDATE.getValue());
//		adi.getObjects().add(categoryBean);
//
//		if (category.getInjectionStatus() != InjectionStatusEnum.INJECTION
//				.getKey()
//				&& category.getInjectionStatus() != InjectionStatusEnum.REINJECTION
//						.getKey()) {
//			// 设置对象分发状态为分发中
//			category.setInjectionStatus(InjectionStatusEnum.INJECTION.getKey());
//			categoryRepository.save(category);
//		}
//
//		sendTask.setAction(InjectionActionTypeEnum.UPDATE.getKey());
//		sendTask.setName(category.getTitle() + " "
//				+ InjectionActionTypeEnum.UPDATE.getName());
//		return true;
//	}
//
//	@Transactional(readOnly = false)
//	private boolean genDeleteCategoryTask(ADI adi, SendEvent sendEvent,
//			List<SendEvent> eventList, Category category, SendTask sendTask) {
//		// 1.生成栏目操作对象
//		CategoryBean categoryBean = genCategoryBean(sendEvent, category);
//		categoryBean.setName(sendEvent.getItemName());// 删除需要名称
//		categoryBean.setAction(InjectionActionTypeEnum.DELETE.getValue());
//		adi.getObjects().add(categoryBean);
//
//		sendTask.setAction(InjectionActionTypeEnum.DELETE.getKey());
//		sendTask.setName(sendEvent.getItemName() + " "
//				+ InjectionActionTypeEnum.DELETE.getName());
//		return true;
//	}
//
//	private CategoryBean genCategoryBean(Category category) {
//		CategoryBean categoryBean = new CategoryBean();
//		categoryBean.setID("" + category.getId());
//		categoryBean.setCode("" + category.getId());
//		categoryBean.setPartnerItemCode(category.getPartnerItemCode());
//
//		if (category.getParentId() != null) {
//			Category parentCategory = categoryRepository.findOne(category
//					.getParentId());
//			if (parentCategory != null) {
//				categoryBean.setParentID("" + parentCategory.getId());
//				categoryBean.setParentCode("" + parentCategory.getId());
//				categoryBean.setParentPartnerItemCode(parentCategory.getPartnerItemCode());
//			} else {
//				categoryBean.setParentID(rootCategoryId);
//				categoryBean.setParentCode(rootCategoryCode);
//				categoryBean.setParentPartnerItemCode(rootCategoryCode);
//			}
//		} else {
//			categoryBean.setParentID(rootCategoryId);
//			categoryBean.setParentCode(rootCategoryCode);
//			categoryBean.setParentPartnerItemCode(rootCategoryCode);
//		}
//		if (StringUtils.isNotEmpty(category.getTitle())) {
//			categoryBean.setName(category.getTitle());
//		}
//		if (category.getSortIndex() != null) {
//			categoryBean.setSequence("" + category.getSortIndex());
//		}
//		categoryBean.setStatus(category.getStatus() == OnlineStatusEnum.OFFLINE
//				.getKey() ? "0" : "1");
//		return categoryBean;
//	}
//
//	private CategoryBean genCategoryBean(SendEvent sendEvent, Category category) {
//		CategoryBean categoryBean = new CategoryBean();
//		categoryBean.setID("" + sendEvent.getItemId());
//		categoryBean.setCode(sendEvent.getItemCode());
//		categoryBean.setPartnerItemCode(category.getPartnerItemCode());
//
//		if (category != null && category.getParentId() != null) {
//			Category parentCategory = categoryRepository.findOne(category
//					.getParentId());
//			if (parentCategory != null) {
//				categoryBean.setParentID("" + parentCategory.getId());
//				categoryBean.setParentCode("" + parentCategory.getId());
//				categoryBean.setParentPartnerItemCode(parentCategory.getPartnerItemCode());
//			} else {
//				categoryBean.setParentID(rootCategoryId);
//				categoryBean.setParentCode(rootCategoryCode);
//				categoryBean.setParentPartnerItemCode(rootCategoryCode);
//			}
//		} else {
//			categoryBean.setParentID(rootCategoryId);
//			categoryBean.setParentCode(rootCategoryCode);
//			categoryBean.setParentPartnerItemCode(rootCategoryCode);
//		}
//		return categoryBean;
//	}
//
//}