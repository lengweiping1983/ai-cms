package com.ai.cms.injection.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.entity.SendTask;
import com.ai.cms.injection.enums.InjectionActionTypeEnum;
import com.ai.cms.injection.enums.InjectionItemTypeEnum;
import com.ai.cms.injection.enums.SendTaskStatusEnum;
import com.ai.cms.injection.repository.SendTaskRepository;
import com.ai.cms.injection.service.InjectionService;
import com.ai.cms.media.bean.BatchPriorityBean;
import com.ai.cms.media.bean.BatchStatusBean;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.OperationObject;
import com.ai.common.bean.PageInfo;
import com.ai.common.bean.ResultCode;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;
import com.ai.sys.security.SecurityUtils;

@Controller
@RequestMapping(value = { "/injection/sendTask" })
public class SendTaskController extends AbstractImageController {

	@Autowired
	private SendTaskRepository sendTaskRepository;

	@Autowired
	private InjectionService injectionService;

	private void setModel(Model model) {
		model.addAttribute("statusEnum", SendTaskStatusEnum.values());
		model.addAttribute("yesNoEnum", YesNoEnum.values());

		model.addAttribute("injectionItemTypeEnum",
				InjectionItemTypeEnum.values());
		model.addAttribute("injectionActionTypeEnum",
				InjectionActionTypeEnum.values());

		List<InjectionPlatform> injectionPlatformList = injectionService
				.findAllSendInjectionPlatform();
		model.addAttribute("injectionPlatformList", injectionPlatformList);
		model.addAttribute("injectionPlatformMap", injectionService
				.findAllInjectionPlatformMap(injectionPlatformList));
	}

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-id");
		}
		List<PropertyFilter> filters = getPropertyFilters(request);
		if (SecurityUtils.getCpCode() != null) {
			filters.add(new PropertyFilter("cpCode__INMASK_S", ""
					+ SecurityUtils.getCpCode()));
		}
		Specification<SendTask> specification = SpecificationUtils
				.getSpecification(filters);
		Page<SendTask> page = find(specification, pageInfo, sendTaskRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "injection/sendTask/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model) {
		SendTask sendTask = new SendTask();
		model.addAttribute("sendTask", sendTask);

		setModel(model);

		return "injection/sendTask/edit";
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "分发工单查询", action = "增加", message = "增加分发工单")
	@RequiresPermissions("injection:sendTask:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody SendTask sendTask) {
		return edit(sendTask, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		SendTask sendTask = sendTaskRepository.findOne(id);
		model.addAttribute("sendTask", sendTask);

		if (sendTask.getPreTaskId() != null && sendTask.getPreTaskId() > 0) {
			SendTask preSendTask = sendTaskRepository.findOne(sendTask
					.getPreTaskId());
			model.addAttribute("preSendTask", preSendTask);
		}

		setModel(model);

		return "injection/sendTask/edit";
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "分发工单查询", action = "修改", message = "修改分发工单")
	@RequiresPermissions("injection:sendTask:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody SendTask sendTask,
			@PathVariable("id") Long id) {
		String message = "";
		SendTask operationObjectList = null;

		SendTask sendTaskInfo = null;
		if (id == null) {
			sendTaskInfo = sendTask;
		} else {
			sendTaskInfo = sendTaskRepository.findOne(sendTask.getId());
			BeanInfoUtil.bean2bean(sendTask, sendTaskInfo, "priority");
		}
		sendTaskRepository.save(sendTaskInfo);
		operationObjectList = sendTaskInfo;
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "分发工单查询", action = "删除", message = "删除分发工单")
	@RequiresPermissions("injection:sendTask:delete")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		String message = "";
		SendTask operationObjectList = null;

		SendTask sendTaskInfo = sendTaskRepository.findOne(id);
		if (sendTaskInfo != null) {
			sendTaskRepository.delete(sendTaskInfo);
			operationObjectList = sendTaskInfo;
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable("id") Long id) {
		model.addAttribute("readOnly", "true");
		toEdit(model, id);
		return "injection/sendTask/detail";
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "分发工单查询", action = "重发", message = "重发工单")
	@RequiresPermissions("injection:sendTask:reset")
	@RequestMapping(value = { "{id}/reset" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult reset(@PathVariable("id") Long id) {
		String message = "";
		SendTask operationObjectList = null;

		SendTask sendTask = sendTaskRepository.findOne(id);
		if (sendTask != null) {
			sendTask.setRequestTimes(0);
			sendTask.setStatus(SendTaskStatusEnum.WAIT.getKey());
			sendTaskRepository.save(sendTask);
			operationObjectList = sendTask;
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "分发工单查询", action = "暂停", message = "暂停工单")
	@RequiresPermissions("injection:sendTask:pause")
	@RequestMapping(value = { "{id}/pause" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult pause(@PathVariable("id") Long id) {
		String message = "";
		SendTask operationObjectList = null;

		SendTask sendTask = sendTaskRepository.findOne(id);
		if (sendTask != null
				&& (SendTaskStatusEnum.WAIT.getKey() == sendTask.getStatus())) {
			sendTask.setStatus(SendTaskStatusEnum.PAUSE.getKey());
			sendTaskRepository.save(sendTask);
			operationObjectList = sendTask;
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "分发工单查询", action = "停止", message = "停止工单")
	@RequiresPermissions("injection:sendTask:stop")
	@RequestMapping(value = { "{id}/stop" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult stop(@PathVariable("id") Long id) {
		String message = "";
		SendTask operationObjectList = null;

		SendTask sendTask = sendTaskRepository.findOne(id);
		if (sendTask != null
				&& (SendTaskStatusEnum.WAIT.getKey() == sendTask.getStatus())) {
			sendTask.setStatus(SendTaskStatusEnum.STOP.getKey());
			sendTaskRepository.save(sendTask);
			operationObjectList = sendTask;
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "分发工单查询", action = "批量重发", message = "批量重发工单")
	@RequiresPermissions("injection:sendTask:batchReset")
	@RequestMapping(value = { "batchReset" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchReset(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}

		String message = "";
		List<SendTask> operationObjectList = new ArrayList<SendTask>();

		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			SendTask sendTask = sendTaskRepository.findOne(itemId);
			if (sendTask != null) {
				sendTask.setRequestTimes(0);
				sendTask.setStatus(SendTaskStatusEnum.WAIT.getKey());
				sendTaskRepository.save(sendTask);
				operationObjectList.add(sendTask);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "分发工单查询", action = "批量暂停", message = "批量暂停工单")
	@RequiresPermissions("injection:sendTask:batchPause")
	@RequestMapping(value = { "batchPause" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchPause(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}

		String message = "";
		List<SendTask> operationObjectList = new ArrayList<SendTask>();

		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			SendTask sendTask = sendTaskRepository.findOne(itemId);
			if (sendTask != null
					&& (SendTaskStatusEnum.WAIT.getKey() == sendTask
							.getStatus())) {
				sendTask.setStatus(SendTaskStatusEnum.PAUSE.getKey());
				sendTaskRepository.save(sendTask);
				operationObjectList.add(sendTask);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "分发工单查询", action = "批量停止", message = "批量停止工单")
	@RequiresPermissions("injection:sendTask:batchStop")
	@RequestMapping(value = { "batchStop" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchStop(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}

		String message = "";
		List<SendTask> operationObjectList = new ArrayList<SendTask>();

		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			SendTask sendTask = sendTaskRepository.findOne(itemId);
			if (sendTask != null
					&& (SendTaskStatusEnum.WAIT.getKey() == sendTask
							.getStatus())) {
				sendTask.setStatus(SendTaskStatusEnum.STOP.getKey());
				sendTaskRepository.save(sendTask);
				operationObjectList.add(sendTask);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@RequestMapping(value = { "batchChangePriority" }, method = RequestMethod.GET)
	public String tobatchChangeStatus(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		return "injection/sendTask/batchChangePriority";
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "分发工单查询", action = "批量修改", message = "批量调整优先级")
	@RequiresPermissions("injection:sendTask:batchChangePriority")
	@RequestMapping(value = { "batchChangePriority" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchChangePriority(
			@RequestBody BatchPriorityBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}

		String message = "";
		List<SendTask> operationObjectList = new ArrayList<SendTask>();

		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			SendTask sendTask = sendTaskRepository.findOne(itemId);
			if (sendTask != null
					&& (SendTaskStatusEnum.SUCCESS.getKey() != sendTask
							.getStatus() && SendTaskStatusEnum.FAIL.getKey() != sendTask
							.getStatus())) {
				sendTask.setPriority(batchBean.getPriority());
				sendTaskRepository.save(sendTask);
				operationObjectList.add(sendTask);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	public List<OperationObject> transformOperationObject(
			List<SendTask> sendTaskList) {
		if (sendTaskList == null || sendTaskList.size() <= 0) {
			return null;
		}
		List<OperationObject> list = new ArrayList<OperationObject>();
		for (SendTask sendTask : sendTaskList) {
			OperationObject operationObject = new OperationObject();
			operationObject.setId(sendTask.getId());
			operationObject.setName(sendTask.getName());
			list.add(operationObject);
		}
		return list;
	}

	public OperationObject transformOperationObject(SendTask sendTask) {
		if (sendTask == null) {
			return null;
		}
		OperationObject operationObject = new OperationObject();
		operationObject.setId(sendTask.getId());
		operationObject.setName(sendTask.getName());
		return operationObject;
	}
}
