package com.ai.cms.media.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import com.ai.cms.media.bean.BatchAuditBean;
import com.ai.cms.media.bean.BatchBean;
import com.ai.cms.media.bean.BatchMetadataBean;
import com.ai.cms.media.bean.BatchStatusBean;
import com.ai.cms.media.bean.BatchTypeBean;
import com.ai.cms.media.bean.ImageBean;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.Series;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.bean.ResultCode;
import com.ai.common.enums.AuditStatusEnum;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.env.handler.OperationLogAnnotation;
import com.ai.sys.security.SecurityUtils;

@Controller
@RequestMapping(value = { "/media/programAudit" })
public class ProgramAuditController extends ProgramController {

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}

		List<PropertyFilter> filters = getPropertyFilters(request);
		Specification<Program> specification = SpecificationUtils
				.getSpecification(filters);
		Page<Program> page = find(specification, pageInfo, programRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "media/programAudit/list";
	}

	@RequestMapping(value = { "selectItem" })
	public String selectItem(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		list(model, request, pageInfo);

		return "media/programAudit/selectItem";
	}

	@RequestMapping(value = { "selectProgram" })
	public String selectProgram(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		list(model, request, pageInfo);

		return "media/programAudit/selectProgram";
	}

	@RequestMapping(value = { "listBySeriesId" })
	public String listBySeriesId(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("episodeIndex");
		}
		list(model, request, pageInfo);

		String search_seriesId__EQ_L = request
				.getParameter("search_seriesId__EQ_L");
		if (StringUtils.isNotEmpty(search_seriesId__EQ_L)) {
			Series series = seriesRepository.findOne(Long
					.valueOf(search_seriesId__EQ_L));
			model.addAttribute("series", series);
		}

		return "media/programAudit/listBySeriesId";
	}

	@RequiresPermissions("media:programAudit:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model,
			@RequestParam(value = "seriesId", required = false) Long seriesId) {
		super.toAdd(model, seriesId);
		return "media/programAudit/edit";
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "节目审核", action = "增加", message = "增加节目")
	@RequiresPermissions("media:programAudit:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody ImageBean<Program> imageBean) {
		return super.edit(imageBean, null, false, true);
	}

	@RequiresPermissions("media:programAudit:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		super.toEdit(model, id);
		return "media/programAudit/edit";
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "节目审核", action = "修改", message = "修改节目")
	@RequiresPermissions("media:programAudit:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody ImageBean<Program> imageBean,
			@PathVariable("id") Long id, boolean notAutoCreate,
			boolean notAutoAudit) {
		return super.edit(imageBean, id, false, true);
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "节目审核", action = "删除", message = "删除节目")
	@RequiresPermissions("media:programAudit:delete")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		return super.delete(id);
	}

	@RequiresPermissions("media:programAudit:view")
	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable("id") Long id) {
		return super.detail(model, id);
	}

	@RequiresPermissions("media:programAudit:batchEdit")
	@RequestMapping(value = { "batchType" }, method = RequestMethod.GET)
	public String batchType(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		setModel(model);

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		return "media/programAudit/batchType";
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "节目审核", action = "批量修改", message = "批量修改节目")
	@RequiresPermissions("media:programAudit:batchEdit")
	@RequestMapping(value = { "batchType" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchType(@RequestBody BatchTypeBean batchBean) {
		return super.batchType(batchBean);
	}

	@RequiresPermissions("media:programAudit:batchEdit")
	@RequestMapping(value = { "batchChangeMetadata" }, method = RequestMethod.GET)
	public String batchChangeMetadata(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		setModel(model);

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		return "media/programAudit/batchChangeMetadata";
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "节目审核", action = "批量修改", message = "批量修改节目")
	@RequiresPermissions("media:programAudit:batchEdit")
	@RequestMapping(value = { "batchChangeMetadata" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchChangeMetadata(
			@RequestBody BatchMetadataBean batchBean) {
		return super.batchChangeMetadata(batchBean);
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "节目审核", action = "批量删除", message = "批量删除节目")
	@RequiresPermissions("media:programAudit:batchDelete")
	@RequestMapping(value = { "batchDelete" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchDelete(@RequestBody BatchBean batchBean) {
		return super.batchDelete(batchBean);
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "节目审核", action = "送审", message = "送审节目")
	@RequiresPermissions("media:programAudit:submit")
	@RequestMapping(value = { "{id}/submit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult submit(@RequestBody ImageBean<Program> imageBean,
			@PathVariable("id") Long id) {
		// 1.保存
		BaseResult result = super.edit(imageBean, id, true, true);
		// 2.送审
		String message = result.getMessage();
		Program operationObjectList = null;
		Program programInfo = programRepository.findOne(id);
		if (programInfo != null) {
			programInfo.setAuditStatus(AuditStatusEnum.AUDIT_WAIT.getKey());
			programInfo.setSubmitUser(SecurityUtils.getUserName());
			programInfo.setSubmitTime(new Date());
			programInfo.setAuditComment(imageBean.getData().getAuditComment());
			mediaService.saveProgram(programInfo);
			operationObjectList = programInfo;
		} else {
			message = "节目不存在！";
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformProgramOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "节目审核", action = "批量送审", message = "批量送审节目")
	@RequiresPermissions("media:programAudit:batchSubmit")
	@RequestMapping(value = { "batchSubmit" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchSubmit(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<Program> operationObjectList = new ArrayList<Program>();
		Date currentTime = new Date();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Program programInfo = programRepository.findOne(itemId);
			if (programInfo != null) {
				programInfo.setAuditStatus(AuditStatusEnum.AUDIT_WAIT.getKey());
				programInfo.setSubmitUser(SecurityUtils.getUserName());
				programInfo.setSubmitTime(currentTime);
				mediaService.saveProgram(programInfo);
				operationObjectList.add(programInfo);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformProgramOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "节目审核", action = "审核", message = "审核节目")
	@RequiresPermissions("media:programAudit:audit")
	@RequestMapping(value = { "{id}/audit/{auditStatus}" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult audit(@RequestBody ImageBean<Program> imageBean,
			@PathVariable("id") Long id,
			@PathVariable("auditStatus") Integer auditStatus) {
		// 1.保存
		BaseResult result = super.edit(imageBean, id, true, true);
		// 2.审核
		String message = result.getMessage();
		Program operationObjectList = null;
		Date currentTime = new Date();
		Program programInfo = programRepository.findOne(id);
		if (programInfo != null
				&& (auditStatus == AuditStatusEnum.AUDIT_FIRST_PASS.getKey() || auditStatus == AuditStatusEnum.AUDIT_NOT_PASS
						.getKey())) {
			programInfo.setAuditStatus(auditStatus);
			programInfo.setAuditUser(SecurityUtils.getUserName());
			programInfo.setAuditTime(currentTime);
			programInfo.setAuditComment(imageBean.getData().getAuditComment());
			if (programInfo.getAuditStatus() == AuditStatusEnum.AUDIT_FIRST_PASS
					.getKey()) {
				programInfo.setStorageTime(currentTime);
			}
			mediaService.saveProgram(programInfo);
			operationObjectList = programInfo;
		} else {
			message = "节目不存在或状态不正确！";
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformProgramOperationObject(operationObjectList));
	}

	@RequiresPermissions("media:programAudit:batchAudit")
	@RequestMapping(value = { "batchAudit" }, method = RequestMethod.GET)
	public String batchAudit(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		setModel(model);

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		return "media/programAudit/batchAudit";
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "节目审核", action = "批量审核", message = "批量审核节目")
	@RequiresPermissions("media:programAudit:batchAudit")
	@RequestMapping(value = { "batchAudit/{auditStatus}" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchAudit(@RequestBody BatchAuditBean batchBean,
			@PathVariable("auditStatus") Integer auditStatus) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<Program> operationObjectList = new ArrayList<Program>();
		Date currentTime = new Date();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Program programInfo = programRepository.findOne(itemId);
			if (programInfo != null
					&& (auditStatus == AuditStatusEnum.AUDIT_FIRST_PASS
							.getKey() || auditStatus == AuditStatusEnum.AUDIT_NOT_PASS
							.getKey())) {
				programInfo.setAuditStatus(auditStatus);
				programInfo.setAuditUser(SecurityUtils.getUserName());
				programInfo.setAuditTime(currentTime);
				programInfo.setAuditComment(batchBean.getAuditComment());
				if (programInfo.getAuditStatus() == AuditStatusEnum.AUDIT_FIRST_PASS
						.getKey()) {
					programInfo.setStorageTime(currentTime);
				}
				mediaService.saveProgram(programInfo);
				operationObjectList.add(programInfo);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformProgramOperationObject(operationObjectList));
	}
}
