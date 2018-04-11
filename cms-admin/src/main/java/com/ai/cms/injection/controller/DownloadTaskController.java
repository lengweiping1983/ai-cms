package com.ai.cms.injection.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.ai.common.bean.PageInfo;
import com.ai.common.bean.ResultCode;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.common.utils.BeanInfoUtil;

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

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody DownloadTask downloadTask,
			@PathVariable("id") Long id) {
		DownloadTask downloadTaskInfo = null;
		if (id == null) {
			downloadTaskInfo = downloadTask;
		} else {
			downloadTaskInfo = downloadTaskRepository.findOne(downloadTask
					.getId());
			BeanInfoUtil.bean2bean(downloadTask, downloadTaskInfo, "priority");
		}
		downloadTaskRepository.save(downloadTaskInfo);
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		DownloadTask downloadTaskInfo = downloadTaskRepository.findOne(id);
		downloadTaskRepository.delete(downloadTaskInfo);
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable("id") Long id) {
		DownloadTask downloadTask = downloadTaskRepository.findOne(id);
		model.addAttribute("downloadTask", downloadTask);

		setModel(model);

		return "injection/downloadTask/detail";
	}

	@RequestMapping(value = { "{id}/reset" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult reset(@PathVariable("id") Long id) {
		DownloadTask downloadTask = downloadTaskRepository.findOne(id);
		if (downloadTask != null
				&& (DownloadTaskStatusEnum.SUCCESS.getKey() != downloadTask
						.getStatus())) {
			downloadService.resetDownloadTask(downloadTask);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/pause" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult pause(@PathVariable("id") Long id) {
		DownloadTask downloadTask = downloadTaskRepository.findOne(id);
		if (downloadTask != null
				&& (DownloadTaskStatusEnum.SUCCESS.getKey() != downloadTask
						.getStatus())) {
			downloadTask.setStatus(DownloadTaskStatusEnum.PAUSE.getKey());
			downloadTaskRepository.save(downloadTask);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/stop" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult stop(@PathVariable("id") Long id) {
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
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/renew" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult renew(@PathVariable("id") Long id) {
		DownloadTask downloadTask = downloadTaskRepository.findOne(id);
		if (downloadTask != null
				&& (DownloadTaskStatusEnum.DEFAULT.getKey() != downloadTask
						.getStatus() && DownloadTaskStatusEnum.WAIT.getKey() != downloadTask
						.getStatus())) {
			downloadTask.setRequestTimes(0);
			downloadTask.setStatus(DownloadTaskStatusEnum.REDOWNLOAD.getKey());
			downloadTaskRepository.save(downloadTask);
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
			DownloadTask downloadTask = downloadTaskRepository.findOne(itemId);
			if (downloadTask != null
					&& (DownloadTaskStatusEnum.SUCCESS.getKey() != downloadTask
							.getStatus())) {
				downloadService.resetDownloadTask(downloadTask);
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
			DownloadTask downloadTask = downloadTaskRepository.findOne(itemId);
			if (downloadTask != null
					&& (DownloadTaskStatusEnum.SUCCESS.getKey() != downloadTask
							.getStatus())) {
				downloadTask.setStatus(DownloadTaskStatusEnum.PAUSE.getKey());
				downloadTaskRepository.save(downloadTask);
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
			}
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "batchRenew" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchRenew(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
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

		return "injection/downloadTask/batchChangePriority";
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
			DownloadTask downloadTask = downloadTaskRepository.findOne(itemId);
			if (downloadTask != null
					&& (DownloadTaskStatusEnum.SUCCESS.getKey() != downloadTask
							.getStatus() && DownloadTaskStatusEnum.DOWNLOADING
							.getKey() != downloadTask.getStatus())) {
				downloadTask.setPriority(batchBean.getPriority());
				downloadTaskRepository.save(downloadTask);
			}
		}
		return new BaseResult();
	}
}
