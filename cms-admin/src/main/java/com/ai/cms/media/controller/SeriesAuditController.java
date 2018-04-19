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
import org.springframework.web.multipart.MultipartFile;

import com.ai.cms.media.bean.BatchAuditBean;
import com.ai.cms.media.bean.BatchBean;
import com.ai.cms.media.bean.BatchMetadataBean;
import com.ai.cms.media.bean.BatchStatusBean;
import com.ai.cms.media.bean.BatchTypeBean;
import com.ai.cms.media.bean.BatchUploadResult;
import com.ai.cms.media.bean.ImageBean;
import com.ai.cms.media.bean.UploadImageBean;
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
@RequestMapping(value = { "/media/seriesAudit" })
public class SeriesAuditController extends SeriesController {

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}

		List<PropertyFilter> filters = getPropertyFilters(request);
		Specification<Series> specification = SpecificationUtils
				.getSpecification(filters);
		Page<Series> page = find(specification, pageInfo, seriesRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "media/seriesAudit/list";
	}

	@RequestMapping(value = { "selectItem" })
	public String selectItem(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		list(model, request, pageInfo);

		return "media/seriesAudit/selectItem";
	}

	@RequestMapping(value = { "selectSeries" })
	public String selectSeries(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		list(model, request, pageInfo);

		return "media/seriesAudit/selectSeries";
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

		return "media/seriesAudit/listBySeriesId";
	}

	@RequiresPermissions("media:seriesAudit:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model) {
		super.toAdd(model);
		return "media/seriesAudit/edit";
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "剧头审核", action = "增加", message = "增加剧头")
	@RequiresPermissions("media:seriesAudit:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody ImageBean<Series> imageBean) {
		return super.edit(imageBean, null, false, true);
	}

	@RequiresPermissions("media:seriesAudit:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		super.toEdit(model, id);
		return "media/seriesAudit/edit";
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "剧头审核", action = "修改", message = "修改剧头")
	@RequiresPermissions("media:seriesAudit:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody ImageBean<Series> imageBean,
			@PathVariable("id") Long id, boolean notAutoCreate,
			boolean notAutoAudit) {
		return super.edit(imageBean, id, false, true);
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "剧头审核", action = "删除", message = "删除剧头")
	@RequiresPermissions("media:seriesAudit:delete")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		return super.delete(id);
	}

	@RequiresPermissions("media:seriesAudit:view")
	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable("id") Long id) {
		return super.detail(model, id);
	}

	@RequiresPermissions("media:seriesAudit:batchEdit")
	@RequestMapping(value = { "batchType" }, method = RequestMethod.GET)
	public String batchType(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		setModel(model);

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		return "media/seriesAudit/batchType";
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "剧头审核", action = "批量修改", message = "批量修改剧头")
	@RequiresPermissions("media:seriesAudit:batchEdit")
	@RequestMapping(value = { "batchType" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchType(@RequestBody BatchTypeBean batchBean) {
		return super.batchType(batchBean);
	}

	@RequiresPermissions("media:seriesAudit:batchEdit")
	@RequestMapping(value = { "batchChangeMetadata" }, method = RequestMethod.GET)
	public String batchChangeMetadata(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		setModel(model);

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		return "media/seriesAudit/batchChangeMetadata";
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "剧头审核", action = "批量修改", message = "批量修改剧头")
	@RequiresPermissions("media:seriesAudit:batchEdit")
	@RequestMapping(value = { "batchChangeMetadata" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchChangeMetadata(
			@RequestBody BatchMetadataBean batchBean) {
		return super.batchChangeMetadata(batchBean);
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "剧头审核", action = "批量删除", message = "批量删除剧头")
	@RequiresPermissions("media:seriesAudit:batchDelete")
	@RequestMapping(value = { "batchDelete" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchDelete(@RequestBody BatchBean batchBean) {
		return super.batchDelete(batchBean);
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "剧头审核", action = "送审", message = "送审剧头")
	@RequiresPermissions("media:seriesAudit:submit")
	@RequestMapping(value = { "{id}/submit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult submit(@RequestBody ImageBean<Series> imageBean,
			@PathVariable("id") Long id) {
		// 1.保存
		BaseResult result = super.edit(imageBean, id, true, true);
		// 2.送审
		String message = result.getMessage();
		Series operationObjectList = null;
		Series seriesInfo = seriesRepository.findOne(id);
		if (seriesInfo != null) {
			seriesInfo.setAuditStatus(AuditStatusEnum.AUDIT_WAIT.getKey());
			seriesInfo.setSubmitUser(SecurityUtils.getUserName());
			seriesInfo.setSubmitTime(new Date());
			seriesInfo.setAuditComment(imageBean.getData().getAuditComment());
			mediaService.saveSeries(seriesInfo);
			operationObjectList = seriesInfo;
		} else {
			message = "剧头不存在！";
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformSeriesOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "剧头审核", action = "批量送审", message = "批量送审剧头")
	@RequiresPermissions("media:seriesAudit:batchSubmit")
	@RequestMapping(value = { "batchSubmit" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchSubmit(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<Series> operationObjectList = new ArrayList<Series>();
		Date currentTime = new Date();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Series seriesInfo = seriesRepository.findOne(itemId);
			if (seriesInfo != null) {
				seriesInfo.setAuditStatus(AuditStatusEnum.AUDIT_WAIT.getKey());
				seriesInfo.setSubmitUser(SecurityUtils.getUserName());
				seriesInfo.setSubmitTime(currentTime);
				mediaService.saveSeries(seriesInfo);
				operationObjectList.add(seriesInfo);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformSeriesOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "剧头审核", action = "审核", message = "审核剧头")
	@RequiresPermissions("media:seriesAudit:audit")
	@RequestMapping(value = { "{id}/audit/{auditStatus}" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult audit(@RequestBody ImageBean<Series> imageBean,
			@PathVariable("id") Long id,
			@PathVariable("auditStatus") Integer auditStatus) {
		// 1.保存
		BaseResult result = super.edit(imageBean, id, true, true);
		// 2.审核
		String message = result.getMessage();
		Series operationObjectList = null;
		Date currentTime = new Date();
		Series seriesInfo = seriesRepository.findOne(id);
		if (seriesInfo != null
				&& (auditStatus == AuditStatusEnum.AUDIT_FIRST_PASS.getKey() || auditStatus == AuditStatusEnum.AUDIT_NOT_PASS
						.getKey())) {
			seriesInfo.setAuditStatus(auditStatus);
			seriesInfo.setAuditUser(SecurityUtils.getUserName());
			seriesInfo.setAuditTime(currentTime);
			seriesInfo.setAuditComment(imageBean.getData().getAuditComment());
			if (seriesInfo.getAuditStatus() == AuditStatusEnum.AUDIT_FIRST_PASS
					.getKey()) {
				seriesInfo.setStorageTime(currentTime);
			}
			mediaService.saveSeriesAndAuditStatus(seriesInfo);
			operationObjectList = seriesInfo;
		} else {
			message = "剧头不存在或状态不正确！";
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformSeriesOperationObject(operationObjectList));
	}

	@RequiresPermissions("media:seriesAudit:batchAudit")
	@RequestMapping(value = { "batchAudit" }, method = RequestMethod.GET)
	public String batchAudit(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		setModel(model);

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		return "media/seriesAudit/batchAudit";
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "剧头审核", action = "批量审核", message = "批量审核剧头")
	@RequiresPermissions("media:seriesAudit:batchAudit")
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
		List<Series> operationObjectList = new ArrayList<Series>();
		Date currentTime = new Date();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Series seriesInfo = seriesRepository.findOne(itemId);
			if (seriesInfo != null
					&& (auditStatus == AuditStatusEnum.AUDIT_FIRST_PASS
							.getKey() || auditStatus == AuditStatusEnum.AUDIT_NOT_PASS
							.getKey())) {
				seriesInfo.setAuditStatus(auditStatus);
				seriesInfo.setAuditUser(SecurityUtils.getUserName());
				seriesInfo.setAuditTime(currentTime);
				seriesInfo.setAuditComment(batchBean.getAuditComment());
				if (seriesInfo.getAuditStatus() == AuditStatusEnum.AUDIT_FIRST_PASS
						.getKey()) {
					seriesInfo.setStorageTime(currentTime);
				}
				mediaService.saveSeriesAndAuditStatus(seriesInfo);
				operationObjectList.add(seriesInfo);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformSeriesOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "剧头审核", action = "批量上传", message = "批量上传剧照")
	@RequiresPermissions("media:seriesAudit:edit")
	@RequestMapping(value = { "{id}/batchUploadStills" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BatchUploadResult batchUploadStills(Model model,
			@PathVariable("id") Long id,
			@RequestParam(value = "files[]") MultipartFile[] files) {
		BatchUploadResult result = super.batchUploadStills(model, id, files);
		for (UploadImageBean file : result.getFiles()) {
			file.setDeleteUrl(file.getDeleteUrl().replace("/media/series/",
					"/media/seriesAudit/"));
		}
		return result;
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "剧头审核", action = "删除", message = "删除剧照")
	@RequiresPermissions("media:seriesAudit:edit")
	@RequestMapping(value = { "{id}/deleteImage/{imageId}" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BatchUploadResult deleteImage(Model model,
			@PathVariable("id") Long id, @PathVariable("imageId") Long imageId) {
		return super.deleteImage(model, id, imageId);
	}
}
