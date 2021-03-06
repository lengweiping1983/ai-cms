package com.ai.cms.transcode.controller;

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

import com.ai.cms.config.service.ConfigService;
import com.ai.cms.media.bean.BatchPriorityBean;
import com.ai.cms.transcode.entity.TranscodeRequest;
import com.ai.cms.transcode.entity.TranscodeTask;
import com.ai.cms.transcode.enums.TranscodeRequestTypeEnum;
import com.ai.cms.transcode.enums.TranscodeTaskStatusEnum;
import com.ai.cms.transcode.enums.TranscodeTaskTypeEnum;
import com.ai.cms.transcode.repository.TranscodeRequestRepository;
import com.ai.cms.transcode.repository.TranscodeTaskRepository;
import com.ai.cms.transcode.service.TranscodeService;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.OperationObject;
import com.ai.common.bean.PageInfo;
import com.ai.common.bean.ResultCode;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;
import com.ai.sys.security.SecurityUtils;

@Controller
@RequestMapping(value = { "/transcode/transcodeTask" })
public class TranscodeTaskController extends AbstractImageController {

	@Autowired
	private TranscodeService transcodeService;

	@Autowired
	private TranscodeRequestRepository transcodeRequestRepository;

	@Autowired
	private TranscodeTaskRepository transcodeTaskRepository;

	@Autowired
	private ConfigService configService;

	private void setModel(Model model) {
		model.addAttribute("statusEnum", TranscodeTaskStatusEnum.values());
		model.addAttribute("typeEnum", TranscodeTaskTypeEnum.values());

		model.addAttribute("transcodeRequestTypeEnum",
				TranscodeRequestTypeEnum.values());

		model.addAttribute("mediaTemplateList",
				configService.findAllMediaTemplate());
	}

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}

		List<PropertyFilter> filters = getPropertyFilters(request);
		if (SecurityUtils.getCpCode() != null) {
			filters.add(new PropertyFilter("cpCode__INMASK_S", ""
					+ SecurityUtils.getCpCode()));
		}
		Specification<TranscodeTask> specification = SpecificationUtils
				.getSpecification(filters);
		Page<TranscodeTask> page = find(specification, pageInfo,
				transcodeTaskRepository);
		// Page<TranscodeTask> page = find(request, pageInfo,
		// transcodeTaskRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "transcode/transcodeTask/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model) {
		TranscodeTask transcodeTask = new TranscodeTask();
		model.addAttribute("transcodeTask", transcodeTask);

		setModel(model);

		return "transcode/transcodeTask/edit";
	}

	@OperationLogAnnotation(module = "媒资生产", subModule = "转码任务管理", action = "增加", message = "增加任务")
	@RequiresPermissions("transcode:transcodeTask:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody TranscodeTask transcodeTask) {
		return edit(transcodeTask, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		TranscodeTask transcodeTask = transcodeTaskRepository.findOne(id);
		model.addAttribute("transcodeTask", transcodeTask);

		setModel(model);

		return "transcode/transcodeTask/edit";
	}

	@OperationLogAnnotation(module = "媒资生产", subModule = "转码任务管理", action = "修改", message = "修改任务")
	@RequiresPermissions("transcode:transcodeTask:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody TranscodeTask transcodeTask,
			@PathVariable("id") Long id) {
		String message = "";
		TranscodeTask operationObjectList = null;

		TranscodeTask transcodeTaskInfo = null;
		if (id == null) {
			transcodeTaskInfo = transcodeTask;
		} else {
			transcodeTaskInfo = transcodeTaskRepository.findOne(transcodeTask
					.getId());
			BeanInfoUtil
					.bean2bean(transcodeTask, transcodeTaskInfo, "priority");
		}
		transcodeTaskRepository.save(transcodeTaskInfo);
		operationObjectList = transcodeTaskInfo;
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资生产", subModule = "转码任务管理", action = "删除", message = "删除任务")
	@RequiresPermissions("transcode:transcodeTask:delete")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		String message = "";
		TranscodeTask operationObjectList = null;

		TranscodeTask transcodeTask = transcodeTaskRepository.findOne(id);
		if (transcodeTask != null) {
			transcodeService.deleteTranscodeTask(transcodeTask);
			operationObjectList = transcodeTask;
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable("id") Long id) {
		TranscodeTask transcodeTask = transcodeTaskRepository.findOne(id);
		model.addAttribute("transcodeTask", transcodeTask);

		TranscodeRequest transcodeRequest = transcodeRequestRepository
				.findOne(transcodeTask.getRequestId());
		model.addAttribute("transcodeRequest", transcodeRequest);

		if (transcodeTask.getPreTaskId() != null
				&& transcodeTask.getPreTaskId() > 0) {
			TranscodeTask preTranscodeTask = transcodeTaskRepository
					.findOne(transcodeTask.getPreTaskId());
			model.addAttribute("preTranscodeTask", preTranscodeTask);
		}

		setModel(model);

		return "transcode/transcodeTask/detail";
	}

	@OperationLogAnnotation(module = "媒资生产", subModule = "转码任务管理", action = "重发", message = "重发指令")
	@RequiresPermissions("transcode:transcodeTask:reset")
	@RequestMapping(value = { "{id}/reset" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult reset(@PathVariable("id") Long id) {
		String message = "";
		TranscodeTask operationObjectList = null;

		TranscodeTask transcodeTask = transcodeTaskRepository.findOne(id);
		if (transcodeTask.getStatus() == TranscodeTaskStatusEnum.PROCESSING
				.getKey()
				|| transcodeTask.getStatus() == TranscodeTaskStatusEnum.SUCCESS
						.getKey()
				|| transcodeTask.getStatus() == TranscodeTaskStatusEnum.FAIL
						.getKey()
				|| transcodeTask.getStatus() == TranscodeTaskStatusEnum.TIMEOUT
						.getKey()) {
			transcodeService.resetTranscodeTask(transcodeTask);
			operationObjectList = transcodeTask;
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资生产", subModule = "转码任务管理", action = "发送", message = "发送指令")
	@RequiresPermissions("transcode:transcodeTask:send")
	@RequestMapping(value = { "{id}/send" }, method = { RequestMethod.GET })
	@ResponseBody
	public BaseResult send(@PathVariable("id") Long id) {
		String message = "";
		TranscodeTask operationObjectList = null;

		TranscodeTask transcodeTask = transcodeTaskRepository.findOne(id);
		if (transcodeTask.getStatus() == TranscodeTaskStatusEnum.WAIT.getKey()) {
			int taskType = transcodeTask.getType();
			TranscodeTaskTypeEnum taskTypeEnum = TranscodeTaskTypeEnum
					.getEnumByKey(taskType);
			switch (taskTypeEnum) {
			case OFFLINE_UPLOAD:
				transcodeService.offlineUpload(transcodeTask);
				operationObjectList = transcodeTask;
				break;
			case TRANSCODE:
				transcodeService.transcode(transcodeTask);
				operationObjectList = transcodeTask;
				break;
			case IMAGE:
				transcodeService.image(transcodeTask);
				operationObjectList = transcodeTask;
				break;
			case AFTER:
				// 后续添加后处理的逻辑，暂时不需要
				break;
			default:
				break;
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

		return "transcode/transcodeTask/batchChangePriority";
	}

	@OperationLogAnnotation(module = "媒资生产", subModule = "转码任务管理", action = "批量修改", message = "批量调整优先级")
	@RequiresPermissions("transcode:transcodeTask:batchChangePriority")
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
		List<TranscodeTask> operationObjectList = new ArrayList<TranscodeTask>();

		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			TranscodeTask transcodeTask = transcodeTaskRepository
					.findOne(itemId);
			if (transcodeTask != null
					&& (TranscodeTaskStatusEnum.PROCESSING.getKey() != transcodeTask
							.getStatus())) {
				transcodeTask.setPriority(batchBean.getPriority());
				transcodeTaskRepository.save(transcodeTask);
				operationObjectList.add(transcodeTask);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	public List<OperationObject> transformOperationObject(
			List<TranscodeTask> transcodeTaskList) {
		if (transcodeTaskList == null || transcodeTaskList.size() <= 0) {
			return null;
		}
		List<OperationObject> list = new ArrayList<OperationObject>();
		for (TranscodeTask transcodeTask : transcodeTaskList) {
			OperationObject operationObject = new OperationObject();
			operationObject.setId(transcodeTask.getId());
			operationObject.setName(transcodeTask.getName());
			list.add(operationObject);
		}
		return list;
	}

	public OperationObject transformOperationObject(TranscodeTask transcodeTask) {
		if (transcodeTask == null) {
			return null;
		}
		OperationObject operationObject = new OperationObject();
		operationObject.setId(transcodeTask.getId());
		operationObject.setName(transcodeTask.getName());
		return operationObject;
	}
}
