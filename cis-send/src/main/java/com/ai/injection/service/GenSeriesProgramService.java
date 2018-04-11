//package com.ai.injection.service;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.ai.cms.injection.bean.ADI;
//import com.ai.cms.injection.bean.MappingBean;
//import com.ai.cms.injection.entity.InjectionPlatform;
//import com.ai.cms.injection.entity.SendEvent;
//import com.ai.cms.injection.entity.SendTask;
//import com.ai.cms.injection.enums.InjectionItemTypeEnum;
//import com.ai.cms.injection.enums.InjectionObjectTypeEnum;
//import com.ai.cms.injection.enums.SendTaskStatusEnum;
//import com.ai.cms.media.entity.Program;
//
//@Service
//@Transactional(value = "slaveTransactionManager", readOnly = false)
//public class GenSeriesProgramService extends GenCommonService {
//
//	/**
//	 * 生成剧头节目映射任务
//	 * 
//	 * @param sendEvent
//	 * @param program
//	 * @return
//	 */
//	public SendTask genSeriesProgramTask(SendEvent sendEvent, Program program) {
//		// 1. 生成任务
//		SendTask sendTask = new SendTask(sendEvent);
//		sendTask.setType(InjectionObjectTypeEnum.MAPPING.getKey());
//		sendTask.setItemType(InjectionItemTypeEnum.PROGRAM.getKey());
//		sendTask.setItemId(sendEvent.getItemId());
//		sendTask.setStatus(SendTaskStatusEnum.WAIT.getKey());
//		InjectionPlatform platform = injectionPlatformRepository.findOne(sendEvent.getPlatformId());
//		sendTask.setPlatform(platform);
//		sendTaskRepository.save(sendTask);
//		String correlateId = StringUtils.trimToEmpty(sendTask.getPlatform()
//				.getCorrelatePrefix()) + sendTask.getId();
//		sendTask.setCorrelateId(correlateId);
//		sendTask.setPriority(sendEvent.getPriority());
//
//		// 2.生成剧头节目映射
//		ADI adi = new ADI();
//		MappingBean mappingBean = genMappingBean(sendEvent);
//		if (program != null && program.getEpisodeIndex() != null) {
//			mappingBean.setSequence("" + program.getEpisodeIndex());
//		}
//		adi.getMappings().add(mappingBean);
//
//		// 3.生成xml
//		genXML(sendTask, adi);
//
//		// 4.保存任务
//		sendTaskRepository.save(sendTask);
//
//		return sendTask;
//	}
//}