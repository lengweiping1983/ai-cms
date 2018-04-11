package com.ai.cms.media.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.cms.media.bean.BatchBean;
import com.ai.cms.media.bean.BatchStatusBean;
import com.ai.cms.media.entity.MediaFile;
import com.ai.cms.media.entity.Program;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.env.handler.OperationLogAnnotation;

@Controller
@RequestMapping(value = { "/media/mediaFileAudit" })
public class MediaFileAuditController extends MediaFileController {

	@RequiresPermissions("media:mediaFileAudit:view")
	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		Page<MediaFile> page = find(request, pageInfo, mediaFileRepository);
		model.addAttribute("page", page);

		String search_programId__EQ_L = request
				.getParameter("search_programId__EQ_L");
		if (StringUtils.isNotEmpty(search_programId__EQ_L)) {
			Program program = programRepository.findOne(Long
					.valueOf(search_programId__EQ_L));
			model.addAttribute("program", program);
		}

		setModel(model);

		return "media/mediaFileAudit/list";
	}

	@RequiresPermissions("media:mediaFileAudit:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model,
			@RequestParam(value = "programId", required = false) Long programId) {
		return super.toAdd(model, programId);
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "媒体内容审核", action = "增加", message = "增加媒体内容")
	@RequiresPermissions("media:mediaFileAudit:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody MediaFile mediaFile) {
		return edit(mediaFile, null);
	}

	@RequiresPermissions("media:mediaFileAudit:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		return super.toEdit(model, id);
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "媒体内容审核", action = "修改", message = "修改媒体内容")
	@RequiresPermissions("media:mediaFileAudit:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody MediaFile mediaFile,
			@PathVariable("id") Long id) {
		return super.edit(mediaFile, id);
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "媒体内容审核", action = "删除", message = "删除媒体内容")
	@RequiresPermissions("media:mediaFileAudit:delete")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		return super.delete(id);
	}

	@RequiresPermissions("media:mediaFileAudit:view")
	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable("id") Long id) {
		return super.detail(model, id);
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "媒体内容审核", action = "批量删除", message = "批量删除媒体内容")
	@RequiresPermissions("media:mediaFileAudit:batchDelete")
	@RequestMapping(value = { "batchDelete" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchDelete(@RequestBody BatchBean batchBean) {
		return super.batchDelete(batchBean);
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "媒体内容审核", action = "上线", message = "上线媒体内容")
	@RequiresPermissions("media:mediaFileAudit:online")
	@RequestMapping(value = { "{id}/online" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult online(@PathVariable("id") Long id) {
		return super.online(id);
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "媒体内容审核", action = "下线", message = "下线媒体内容")
	@RequiresPermissions("media:mediaFileAudit:offline")
	@RequestMapping(value = { "{id}/offline" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult offline(@PathVariable("id") Long id) {
		return super.offline(id);
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "媒体内容审核", action = "批量上线", message = "批量上线媒体内容")
	@RequiresPermissions("media:mediaFileAudit:batchOnline")
	@RequestMapping(value = { "batchOnline" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchOnline(@RequestBody BatchStatusBean batchBean) {
		return super.batchOnline(batchBean);
	}

	@OperationLogAnnotation(module = "媒资审核", subModule = "媒体内容审核", action = "批量下线", message = "批量下线媒体内容")
	@RequiresPermissions("media:mediaFileAudit:batchOffline")
	@RequestMapping(value = { "batchOffline" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchOffline(@RequestBody BatchStatusBean batchBean) {
		return super.batchOffline(batchBean);
	}
}
