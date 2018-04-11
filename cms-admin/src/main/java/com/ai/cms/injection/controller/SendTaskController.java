package com.ai.cms.injection.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.ai.common.bean.PageInfo;
import com.ai.common.bean.ResultCode;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.utils.BeanInfoUtil;

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
		Page<SendTask> page = find(request, pageInfo, sendTaskRepository);
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

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody SendTask sendTask,
			@PathVariable("id") Long id) {
		SendTask sendTaskInfo = null;
		if (id == null) {
			sendTaskInfo = sendTask;
		} else {
			sendTaskInfo = sendTaskRepository.findOne(sendTask.getId());
			BeanInfoUtil.bean2bean(sendTask, sendTaskInfo, "priority");
		}
		sendTaskRepository.save(sendTaskInfo);
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		SendTask sendTaskInfo = sendTaskRepository.findOne(id);
		sendTaskRepository.delete(sendTaskInfo);
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable("id") Long id) {
		model.addAttribute("readOnly", "true");
		toEdit(model, id);
		return "injection/sendTask/detail";
	}

	@RequestMapping(value = { "{id}/reset" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult reset(@PathVariable("id") Long id) {
		SendTask sendTask = sendTaskRepository.findOne(id);
		sendTask.setRequestTimes(0);
		sendTask.setStatus(SendTaskStatusEnum.WAIT.getKey());
		sendTaskRepository.save(sendTask);
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/pause" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult pause(@PathVariable("id") Long id) {
		SendTask sendTask = sendTaskRepository.findOne(id);
		if (sendTask != null
				&& (SendTaskStatusEnum.WAIT.getKey() == sendTask.getStatus())) {
			sendTask.setStatus(SendTaskStatusEnum.PAUSE.getKey());
			sendTaskRepository.save(sendTask);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/stop" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult stop(@PathVariable("id") Long id) {
		SendTask sendTask = sendTaskRepository.findOne(id);
		if (sendTask != null
				&& (SendTaskStatusEnum.WAIT.getKey() == sendTask.getStatus())) {
			sendTask.setStatus(SendTaskStatusEnum.STOP.getKey());
			sendTaskRepository.save(sendTask);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "batchReset" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchReset(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			SendTask sendTask = sendTaskRepository.findOne(itemId);
			if (sendTask != null) {
				sendTask.setRequestTimes(0);
				sendTask.setStatus(SendTaskStatusEnum.WAIT.getKey());
				sendTaskRepository.save(sendTask);
			}
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "batchPause" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchPause(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			SendTask sendTask = sendTaskRepository.findOne(itemId);
			if (sendTask != null
					&& (SendTaskStatusEnum.WAIT.getKey() == sendTask
							.getStatus())) {
				sendTask.setStatus(SendTaskStatusEnum.PAUSE.getKey());
				sendTaskRepository.save(sendTask);
			}
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "batchStop" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchStop(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			SendTask sendTask = sendTaskRepository.findOne(itemId);
			if (sendTask != null
					&& (SendTaskStatusEnum.WAIT.getKey() == sendTask
							.getStatus())) {
				sendTask.setStatus(SendTaskStatusEnum.STOP.getKey());
				sendTaskRepository.save(sendTask);
			}
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "batchChangePriority" }, method = RequestMethod.GET)
	public String tobatchChangeStatus(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		return "injection/sendTask/batchChangePriority";
	}

	@RequestMapping(value = { "batchChangePriority" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchChangePriority(
			@RequestBody BatchPriorityBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
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
			}
		}
		return new BaseResult();
	}

}
