package com.ai.cms.config.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import com.ai.cms.config.entity.MediaTemplate;
import com.ai.cms.config.repository.MediaTemplateRepository;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.OperationObject;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractController;
import com.ai.common.enums.TranscodeModeEnum;
import com.ai.common.enums.ValidStatusEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;

@Controller
@RequestMapping(value = { "/config/mediaTemplate" })
public class MediaTemplateController extends AbstractController {

	@Autowired
	private MediaTemplateRepository mediaTemplateRepository;

	private void setModel(Model model) {
		model.addAttribute("statusEnum", ValidStatusEnum.values());

		model.addAttribute("yesNoEnum", YesNoEnum.values());
		model.addAttribute("transcodeModeEnum", TranscodeModeEnum.values());
	}

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		Page<MediaTemplate> page = find(request, pageInfo,
				mediaTemplateRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "config/mediaTemplate/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model) {
		MediaTemplate mediaTemplate = new MediaTemplate();
		model.addAttribute("mediaTemplate", mediaTemplate);

		setModel(model);

		return "config/mediaTemplate/edit";
	}

	@OperationLogAnnotation(module = "配置管理", subModule = "码率模板管理", action = "增加", message = "增加码率模板")
	@RequiresPermissions("config:mediaTemplate:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody MediaTemplate mediaTemplate) {
		return edit(mediaTemplate, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		MediaTemplate mediaTemplate = mediaTemplateRepository.findOne(id);
		model.addAttribute("mediaTemplate", mediaTemplate);

		setModel(model);

		return "config/mediaTemplate/edit";
	}

	@OperationLogAnnotation(module = "配置管理", subModule = "码率模板管理", action = "修改", message = "修改码率模板")
	@RequiresPermissions("config:mediaTemplate:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody MediaTemplate mediaTemplate,
			@PathVariable("id") Long id) {
		String message = "";
		MediaTemplate operationObjectList = null;

		MediaTemplate mediaTemplateInfo = null;
		if (id == null) {
			mediaTemplateInfo = mediaTemplate;
		} else {
			mediaTemplateInfo = mediaTemplateRepository.findOne(mediaTemplate
					.getId());
			BeanInfoUtil
					.bean2bean(
							mediaTemplate,
							mediaTemplateInfo,
							"code,title,vCodec,vBitrateMode,vResolution,definition,"
									+ "vFormat,vBitrate,vMaxBitrate,vMinBitrate,"
									+ "vFramerate,vGop,v2pass,vProfile,vProfileLevel,aCodec,aBitrate,"
									+ "transcodeMode,externalCode,status");
		}
		// TS-CBR-H264-8000-1080P-25-MP2-128
		String mediaSpec = mediaTemplate.getvFormat() + "-"
				+ mediaTemplate.getvBitrateMode() + "-"
				+ mediaTemplate.getvCodec() + "-" + mediaTemplate.getvBitrate()
				+ "-" + mediaTemplate.getvResolution() + "-"
				+ mediaTemplate.getvFramerate() + "-"
				+ mediaTemplate.getaCodec() + "-" + mediaTemplate.getaBitrate();
		mediaTemplateInfo.setMediaSpec(mediaSpec);
		mediaTemplateRepository.save(mediaTemplateInfo);
		operationObjectList = mediaTemplateInfo;
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "配置管理", subModule = "码率模板管理", action = "删除", message = "删除码率模板")
	@RequiresPermissions("config:mediaTemplate:delete")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		String message = "";
		MediaTemplate operationObjectList = null;

		MediaTemplate mediaTemplate = mediaTemplateRepository.findOne(id);
		if (mediaTemplate != null) {
			mediaTemplateRepository.delete(mediaTemplate);
			operationObjectList = mediaTemplate;
		} else {
			message = "码率模板不存在！";
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@RequestMapping(value = { "check" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Object[] check(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "fieldId") String fieldId,
			@RequestParam(value = "fieldValue") String fieldValue) {
		boolean exist = checkCode(id, fieldValue);

		Object[] jsonValidateReturn = new Object[3];
		jsonValidateReturn[0] = fieldId;
		jsonValidateReturn[1] = !exist;
		if (!exist) {
			jsonValidateReturn[2] = "可以使用!";
		} else {
			jsonValidateReturn[2] = "代码" + StringUtils.trim(fieldValue)
					+ "已使用!";
		}
		return jsonValidateReturn;
	}

	private boolean checkCode(Long id, String code) {
		boolean exist = false;
		MediaTemplate mediaTemplate = null;
		if (StringUtils.isNotEmpty(code)) {
			mediaTemplate = mediaTemplateRepository.findOneByCode(code);
		}
		if (mediaTemplate != null) {
			if (id == null || id == -1
					|| mediaTemplate.getId().longValue() != id) {
				exist = true;
			}
		}
		return exist;
	}

	public OperationObject transformOperationObject(MediaTemplate mediaTemplate) {
		if (mediaTemplate == null) {
			return null;
		}
		OperationObject operationObject = new OperationObject();
		operationObject.setId(mediaTemplate.getId());
		operationObject.setName(mediaTemplate.getTitle());
		return operationObject;
	}

}
