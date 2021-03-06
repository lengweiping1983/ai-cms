package com.ai.cms.injection.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.cms.config.service.ConfigService;
import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.enums.InjectionDirectionEnum;
import com.ai.cms.injection.enums.PlatformTypeEnum;
import com.ai.cms.injection.enums.ProviderInterfaceModeEnum;
import com.ai.cms.injection.enums.ProviderTypeEnum;
import com.ai.cms.injection.repository.InjectionPlatformRepository;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.OperationObject;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractController;
import com.ai.common.enums.ValidStatusEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;

@Controller
@RequestMapping(value = { "/injection/platform" })
public class InjectionPlatformController extends AbstractController {

	@Autowired
	private InjectionPlatformRepository injectionPlatformRepository;

	@Autowired
	private ConfigService configService;

	private void setModel(Model model) {
		model.addAttribute("typeEnum", PlatformTypeEnum.values());
		model.addAttribute("statusEnum", ValidStatusEnum.values());
		model.addAttribute("yesNoEnum", YesNoEnum.values());
		model.addAttribute("providerTypeEnum", ProviderTypeEnum.values());
		model.addAttribute("providerInterfaceModeEnum",
				ProviderInterfaceModeEnum.values());
		model.addAttribute("injectionDirectionEnum",
				InjectionDirectionEnum.values());

		model.addAttribute("siteList", configService.findAllSite());
		model.addAttribute("mediaTemplateList",
				configService.findAllMediaTemplate());

		model.addAttribute("injectionPlatformList", injectionPlatformRepository
				.findAllByDirection(InjectionDirectionEnum.SEND.getKey()));
		model.addAttribute("indirectPlatformList", injectionPlatformRepository
				.findAllByDirection(InjectionDirectionEnum.INDIRECT.getKey()));
	}

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		Page<InjectionPlatform> page = find(request, pageInfo,
				injectionPlatformRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "injection/platform/list";
	}

	@RequestMapping(value = { "add/{direction}" }, method = RequestMethod.GET)
	public String toAdd(Model model,
			@PathVariable("direction") Integer direction) {
		InjectionPlatform injectionPlatform = new InjectionPlatform();
		model.addAttribute("injectionPlatform", injectionPlatform);

		setModel(model);

		injectionPlatform.setDirection(direction);
		InjectionDirectionEnum directionEnum = InjectionDirectionEnum
				.getEnumByKey(direction);
		switch (directionEnum) {
		case RECEIVE:
			return "injection/platform/editReceive";
		case INDIRECT:
			return "injection/platform/editIndirect";
		default:
			return "injection/platform/editSend";
		}
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "分发平台管理", action = "增加", message = "增加分发平台")
	@RequiresPermissions("injection:platform:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody InjectionPlatform injectionPlatform) {
		return edit(injectionPlatform, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		InjectionPlatform injectionPlatform = injectionPlatformRepository
				.findOne(id);
		model.addAttribute("injectionPlatform", injectionPlatform);

		setModel(model);

		InjectionDirectionEnum directionEnum = InjectionDirectionEnum
				.getEnumByKey(injectionPlatform.getDirection());
		switch (directionEnum) {
		case RECEIVE:
			return "injection/platform/editReceive";
		case INDIRECT:
			return "injection/platform/editIndirect";
		default:
			return "injection/platform/editSend";
		}
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "分发平台管理", action = "修改", message = "修改分发平台")
	@RequiresPermissions("injection:platform:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody InjectionPlatform injectionPlatform,
			@PathVariable("id") Long id) {
		String message = "";
		InjectionPlatform operationObjectList = null;

		if (id == null) {
			injectionPlatformRepository.save(injectionPlatform);
			operationObjectList = injectionPlatform;
		} else {
			InjectionPlatform injectionPlatformInfo = injectionPlatformRepository
					.findOne(injectionPlatform.getId());
			if (injectionPlatformInfo != null) {
				BeanInfoUtil
						.bean2bean(
								injectionPlatform,
								injectionPlatformInfo,
								"siteCode,name,type,dependPlatformId,provider,interfaceMode,needDownloadVideo,needAudit"
										+ ",needInjection,injectionPlatformId,indirectPlatformId,platformCode"
										+ ",cspId,lspId,serviceUrl,liveServiceUrl,isWSDL,namespace"
										+ ",templateId,description,direction,isCallback,status"
										+ ",templateCustom,templateFilename,useGlobalCode,playCodeCustom,codePrefix,correlatePrefix"
										+ ",needImageObject,needPackingProgram,needDeleteMediaFile,separateChar");
				injectionPlatformRepository.save(injectionPlatformInfo);
				operationObjectList = injectionPlatformInfo;
			} else {
				message = "分发平台不存在！";
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资分发", subModule = "分发平台管理", action = "删除", message = "删除分发平台")
	@RequiresPermissions("injection:platform:delete")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		String message = "";
		InjectionPlatform operationObjectList = null;

		InjectionPlatform injectionPlatform = injectionPlatformRepository
				.findOne(id);
		if (injectionPlatform != null) {
			injectionPlatformRepository.delete(injectionPlatform);
			operationObjectList = injectionPlatform;
		} else {
			message = "分发平台不存在！";
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	public OperationObject transformOperationObject(
			InjectionPlatform injectionPlatform) {
		if (injectionPlatform == null) {
			return null;
		}
		OperationObject operationObject = new OperationObject();
		operationObject.setId(injectionPlatform.getId());
		operationObject.setName(injectionPlatform.getName());
		return operationObject;
	}
}
