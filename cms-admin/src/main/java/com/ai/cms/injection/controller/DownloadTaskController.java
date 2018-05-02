package com.ai.cms.injection.controller;

import java.io.File;
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

import com.ai.AdminGlobal;
import com.ai.cms.injection.entity.DownloadTask;
import com.ai.cms.injection.enums.DownloadTaskStatusEnum;
import com.ai.cms.injection.repository.DownloadTaskRepository;
import com.ai.cms.injection.service.DownloadService;
import com.ai.cms.media.bean.BatchPriorityBean;
import com.ai.cms.media.bean.BatchStatusBean;
import com.ai.cms.media.entity.Program;
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
@RequestMapping(value = { "/injection/downloadTask" })
public class DownloadTaskController extends AbstractImageController {

	@Autowired
	private DownloadTaskRepository downloadTaskRepository;

	@Autowired
	private DownloadService downloadService;

	private void setModel(Model model) {
		model.addAttribute("statusEnum", DownloadTaskStatusEnum.values());
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
		Specification<Program> specification = SpecificationUtils
				.getSpecification(filters);
		Page<Program> page = find(specification, pageInfo,
				downloadTaskRepository);

		model.addAttribute("page", page);

		setModel(model);

		return "injection/downloadTask/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model) {
		DownloadTask downloadTask = new DownloadTask();
		model.addAttribute("downloadTask", downloadTask);

		setModel(model);

		return "injection/downloadTask/edit";
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "下载任务管理", action = "增加", message = "增加下载任务")
	@RequiresPermissions("injection:downloadTask:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody DownloadTask downloadTask) {
		return edit(downloadTask, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		DownloadTask downloadTask = downloadTaskRepository.findOne(id);
		model.addAttribute("downloadTask", downloadTask);

		setModel(model);

		return "injection/downloadTask/edit";
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "下载任务管理", action = "修改", message = "修改下载任务")
	@RequiresPermissions("injection:downloadTask:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody DownloadTask downloadTask,
			@PathVariable("id") Long id) {
		String message = "";
		DownloadTask operationObjectList = null;

		DownloadTask downloadTaskInfo = null;
		if (id == null) {
			downloadTaskInfo = downloadTask;
		} else {
			downloadTaskInfo = downloadTaskRepository.findOne(downloadTask
					.getId());
			BeanInfoUtil.bean2bean(downloadTask, downloadTaskInfo, "priority");
		}
		downloadTaskRepository.save(downloadTaskInfo);
		operationObjectList = downloadTaskInfo;
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "下载任务管理", action = "删除", message = "删除下载任务")
	@RequiresPermissions("injection:downloadTask:delete")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		String message = "";
		DownloadTask operationObjectList = null;

		DownloadTask downloadTaskInfo = downloadTaskRepository.findOne(id);
		downloadTaskRepository.delete(downloadTaskInfo);
		operationObjectList = downloadTaskInfo;
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable("id") Long id) {
		DownloadTask downloadTask = downloadTaskRepository.findOne(id);
		model.addAttribute("downloadTask", downloadTask);

		setModel(model);

		return "injection/downloadTask/detail";
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "下载任务管理", action = "继续", message = "继续下载")
	@RequiresPermissions("injection:downloadTask:reset")
	@RequestMapping(value = { "{id}/reset" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult reset(@PathVariable("id") Long id) {
		String message = "";
		DownloadTask operationObjectList = null;

		DownloadTask downloadTask = downloadTaskRepository.findOne(id);
		if (downloadTask != null
				&& (DownloadTaskStatusEnum.SUCCESS.getKey() != downloadTask
						.getStatus())) {
			downloadService.resetDownloadTask(downloadTask);
			operationObjectList = downloadTask;
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "下载任务管理", action = "暂停", message = "暂停下载")
	@RequiresPermissions("injection:downloadTask:pause")
	@RequestMapping(value = { "{id}/pause" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult pause(@PathVariable("id") Long id) {
		String message = "";
		DownloadTask operationObjectList = null;

		DownloadTask downloadTask = downloadTaskRepository.findOne(id);
		if (downloadTask != null
				&& (DownloadTaskStatusEnum.SUCCESS.getKey() != downloadTask
						.getStatus())) {
			downloadTask.setStatus(DownloadTaskStatusEnum.PAUSE.getKey());
			downloadTaskRepository.save(downloadTask);
			operationObjectList = downloadTask;
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "下载任务管理", action = "停止", message = "停止下载")
	@RequiresPermissions("injection:downloadTask:stop")
	@RequestMapping(value = { "{id}/stop" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult stop(@PathVariable("id") Long id) {
		String message = "";
		DownloadTask operationObjectList = null;

		DownloadTask downloadTask = downloadTaskRepository.findOne(id);
		if (downloadTask != null
				&& (DownloadTaskStatusEnum.SUCCESS.getKey() != downloadTask
						.getStatus())) {
			downloadTask.setStatus(DownloadTaskStatusEnum.STOP.getKey());
			downloadTaskRepository.save(downloadTask);
			downloadService.calculateFailDownloadTask(downloadTask);

			String outputFilePath = AdminGlobal.getVideoUploadPath(downloadTask
					.getOutputFilePath());
			if (StringUtils.isNotEmpty(outputFilePath)) {
				File file = new File(outputFilePath);
				if (file.exists()) {
					file.delete();
				}
			}
			operationObjectList = downloadTask;
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "下载任务管理", action = "重新", message = "重新下载")
	@RequiresPermissions("injection:downloadTask:renew")
	@RequestMapping(value = { "{id}/renew" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult renew(@PathVariable("id") Long id) {
		String message = "";
		DownloadTask operationObjectList = null;

		DownloadTask downloadTask = downloadTaskRepository.findOne(id);
		if (downloadTask != null
				&& (DownloadTaskStatusEnum.DEFAULT.getKey() != downloadTask
						.getStatus() && DownloadTaskStatusEnum.WAIT.getKey() != downloadTask
						.getStatus())) {
			downloadTask.setRequestTimes(0);
			downloadTask.setStatus(DownloadTaskStatusEnum.REDOWNLOAD.getKey());
			downloadTaskRepository.save(downloadTask);
			operationObjectList = downloadTask;
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "下载任务管理", action = "批量继续", message = "批量继续下载")
	@RequiresPermissions("injection:downloadTask:batchReset")
	@RequestMapping(value = { "batchReset" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchReset(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}

		String message = "";
		List<DownloadTask> operationObjectList = new ArrayList<DownloadTask>();

		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			DownloadTask downloadTask = downloadTaskRepository.findOne(itemId);
			if (downloadTask != null
					&& (DownloadTaskStatusEnum.SUCCESS.getKey() != downloadTask
							.getStatus())) {
				downloadService.resetDownloadTask(downloadTask);
				operationObjectList.add(downloadTask);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "下载任务管理", action = "批量暂停", message = "批量暂停下载")
	@RequiresPermissions("injection:downloadTask:batchPause")
	@RequestMapping(value = { "batchPause" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchPause(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}

		String message = "";
		List<DownloadTask> operationObjectList = new ArrayList<DownloadTask>();

		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			DownloadTask downloadTask = downloadTaskRepository.findOne(itemId);
			if (downloadTask != null
					&& (DownloadTaskStatusEnum.SUCCESS.getKey() != downloadTask
							.getStatus())) {
				downloadTask.setStatus(DownloadTaskStatusEnum.PAUSE.getKey());
				downloadTaskRepository.save(downloadTask);
				operationObjectList.add(downloadTask);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "下载任务管理", action = "批量停止", message = "批量停止下载")
	@RequiresPermissions("injection:downloadTask:batchStop")
	@RequestMapping(value = { "batchStop" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchStop(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}

		String message = "";
		List<DownloadTask> operationObjectList = new ArrayList<DownloadTask>();

		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			DownloadTask downloadTask = downloadTaskRepository.findOne(itemId);
			if (downloadTask != null
					&& (DownloadTaskStatusEnum.SUCCESS.getKey() != downloadTask
							.getStatus())) {
				downloadTask.setStatus(DownloadTaskStatusEnum.STOP.getKey());
				downloadTaskRepository.save(downloadTask);
				downloadService.calculateFailDownloadTask(downloadTask);

				String outputFilePath = AdminGlobal
						.getVideoUploadPath(downloadTask.getOutputFilePath());
				if (StringUtils.isNotEmpty(outputFilePath)) {
					File file = new File(outputFilePath);
					if (file.exists()) {
						file.delete();
					}
				}
				operationObjectList.add(downloadTask);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "下载任务管理", action = "批量重新", message = "批量重新下载")
	@RequiresPermissions("injection:downloadTask:batchRenew")
	@RequestMapping(value = { "batchRenew" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchRenew(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}

		String message = "";
		List<DownloadTask> operationObjectList = new ArrayList<DownloadTask>();

		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			DownloadTask downloadTask = downloadTaskRepository.findOne(itemId);
			if (downloadTask != null
					&& (DownloadTaskStatusEnum.DEFAULT.getKey() != downloadTask
							.getStatus() && DownloadTaskStatusEnum.WAIT
							.getKey() != downloadTask.getStatus())) {
				downloadTask.setRequestTimes(0);
				downloadTask.setStatus(DownloadTaskStatusEnum.REDOWNLOAD
						.getKey());
				downloadTaskRepository.save(downloadTask);
				operationObjectList.add(downloadTask);
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

		return "injection/downloadTask/batchChangePriority";
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "下载任务管理", action = "批量修改", message = "批量调整优先级")
	@RequiresPermissions("injection:downloadTask:batchChangePriority")
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
		List<DownloadTask> operationObjectList = new ArrayList<DownloadTask>();

		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			DownloadTask downloadTask = downloadTaskRepository.findOne(itemId);
			if (downloadTask != null
					&& (DownloadTaskStatusEnum.SUCCESS.getKey() != downloadTask
							.getStatus() && DownloadTaskStatusEnum.DOWNLOADING
							.getKey() != downloadTask.getStatus())) {
				downloadTask.setPriority(batchBean.getPriority());
				downloadTaskRepository.save(downloadTask);
				operationObjectList.add(downloadTask);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	public List<OperationObject> transformOperationObject(
			List<DownloadTask> downloadTaskList) {
		if (downloadTaskList == null || downloadTaskList.size() <= 0) {
			return null;
		}
		List<OperationObject> list = new ArrayList<OperationObject>();
		for (DownloadTask downloadTask : downloadTaskList) {
			OperationObject operationObject = new OperationObject();
			operationObject.setId(downloadTask.getId());
			operationObject.setName(downloadTask.getName());
			list.add(operationObject);
		}
		return list;
	}

	public OperationObject transformOperationObject(DownloadTask downloadTask) {
		if (downloadTask == null) {
			return null;
		}
		OperationObject operationObject = new OperationObject();
		operationObject.setId(downloadTask.getId());
		operationObject.setName(downloadTask.getName());
		return operationObject;
	}

}
