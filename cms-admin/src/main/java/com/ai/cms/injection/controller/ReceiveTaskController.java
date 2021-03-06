package com.ai.cms.injection.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.entity.ReceiveTask;
import com.ai.cms.injection.enums.InjectionActionTypeEnum;
import com.ai.cms.injection.enums.InjectionItemTypeEnum;
import com.ai.cms.injection.enums.ReceiveTaskStatusEnum;
import com.ai.cms.injection.repository.ReceiveTaskRepository;
import com.ai.cms.injection.service.InjectionService;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.OperationObject;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.sys.security.SecurityUtils;

@Controller
@RequestMapping(value = { "/injection/receiveTask" })
public class ReceiveTaskController extends AbstractImageController {

	@Autowired
	private ReceiveTaskRepository receiveTaskRepository;

	@Autowired
	private InjectionService injectionService;

	private void setModel(Model model) {
		model.addAttribute("statusEnum", ReceiveTaskStatusEnum.values());

		model.addAttribute("injectionItemTypeEnum",
				InjectionItemTypeEnum.values());
		model.addAttribute("injectionActionTypeEnum",
				InjectionActionTypeEnum.values());

		List<InjectionPlatform> injectionPlatformList = injectionService
				.findAllReceiveInjectionPlatform();
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
		Specification<ReceiveTask> specification = SpecificationUtils
				.getSpecification(filters);
		Page<ReceiveTask> page = find(specification, pageInfo,
				receiveTaskRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "injection/receiveTask/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model) {
		ReceiveTask receiveTask = new ReceiveTask();
		model.addAttribute("receiveTask", receiveTask);

		setModel(model);

		return "injection/receiveTask/edit";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody ReceiveTask receiveTask) {
		return edit(receiveTask, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		ReceiveTask receiveTask = receiveTaskRepository.findOne(id);
		model.addAttribute("receiveTask", receiveTask);

		setModel(model);

		return "injection/receiveTask/edit";
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody ReceiveTask receiveTask,
			@PathVariable("id") Long id) {
		String message = "";
		ReceiveTask operationObjectList = null;

		ReceiveTask receiveTaskInfo = null;
		if (id == null) {
			receiveTaskInfo = receiveTask;
		} else {
			receiveTaskInfo = receiveTaskRepository
					.findOne(receiveTask.getId());
			BeanInfoUtil.bean2bean(receiveTask, receiveTaskInfo, "priority");
		}
		receiveTaskRepository.save(receiveTaskInfo);
		operationObjectList = receiveTaskInfo;
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		String message = "";
		ReceiveTask operationObjectList = null;

		ReceiveTask receiveTaskInfo = receiveTaskRepository.findOne(id);
		if (receiveTaskInfo != null) {
			receiveTaskRepository.delete(receiveTaskInfo);
			operationObjectList = receiveTaskInfo;
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable("id") Long id) {
		ReceiveTask receiveTask = receiveTaskRepository.findOne(id);
		model.addAttribute("receiveTask", receiveTask);

		setModel(model);

		return "injection/receiveTask/detail";
	}

	public List<OperationObject> transformOperationObject(
			List<ReceiveTask> receiveTaskList) {
		if (receiveTaskList == null || receiveTaskList.size() <= 0) {
			return null;
		}
		List<OperationObject> list = new ArrayList<OperationObject>();
		for (ReceiveTask receiveTask : receiveTaskList) {
			OperationObject operationObject = new OperationObject();
			operationObject.setId(receiveTask.getId());
			operationObject.setName("" + receiveTask.getId());
			list.add(operationObject);
		}
		return list;
	}

	public OperationObject transformOperationObject(ReceiveTask receiveTask) {
		if (receiveTask == null) {
			return null;
		}
		OperationObject operationObject = new OperationObject();
		operationObject.setId(receiveTask.getId());
		operationObject.setName("" + receiveTask.getId());
		return operationObject;
	}

}
