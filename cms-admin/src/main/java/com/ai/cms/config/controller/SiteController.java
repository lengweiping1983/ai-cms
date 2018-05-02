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

import com.ai.cms.config.entity.Site;
import com.ai.cms.config.repository.SiteRepository;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.OperationObject;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractController;
import com.ai.common.enums.ValidStatusEnum;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;

@Controller
@RequestMapping(value = { "/config/site" })
public class SiteController extends AbstractController {

	@Autowired
	private SiteRepository siteRepository;

	private void setModel(Model model) {
		model.addAttribute("statusEnum", ValidStatusEnum.values());
	}

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		Page<Site> page = find(request, pageInfo, siteRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "config/site/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model) {
		Site site = new Site();
		model.addAttribute("site", site);

		setModel(model);

		return "config/site/edit";
	}

	@OperationLogAnnotation(module = "配置管理", subModule = "渠道管理", action = "增加", message = "增加渠道")
	@RequiresPermissions("config:site:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody Site site) {
		return edit(site, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		Site site = siteRepository.findOne(id);
		model.addAttribute("site", site);

		setModel(model);

		return "config/site/edit";
	}

	@OperationLogAnnotation(module = "配置管理", subModule = "渠道管理", action = "修改", message = "修改渠道")
	@RequiresPermissions("config:site:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody Site site, @PathVariable("id") Long id) {
		String message = "";
		Site operationObjectList = null;

		if (id == null) {
			siteRepository.save(site);
			operationObjectList = site;
		} else {
			Site siteInfo = siteRepository.findOne(site.getId());
			if (siteInfo != null) {
				BeanInfoUtil.bean2bean(site, siteInfo,
						"name,code,status,description");
				siteRepository.save(siteInfo);
				operationObjectList = siteInfo;
			} else {
				message = "渠道不存在！";
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "配置管理", subModule = "渠道管理", action = "删除", message = "删除渠道")
	@RequiresPermissions("config:site:delete")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		String message = "";
		Site operationObjectList = null;

		Site site = siteRepository.findOne(id);
		if (site != null) {
			siteRepository.delete(site);
			operationObjectList = site;
		} else {
			message = "渠道不存在！";
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
		Site site = null;
		if (StringUtils.isNotEmpty(code)) {
			site = siteRepository.findOneByCode(code);
		}
		if (site != null) {
			if (id == null || id == -1 || site.getId().longValue() != id) {
				exist = true;
			}
		}
		return exist;
	}

	public OperationObject transformOperationObject(Site site) {
		if (site == null) {
			return null;
		}
		OperationObject operationObject = new OperationObject();
		operationObject.setId(site.getId());
		operationObject.setName(site.getName());
		return operationObject;
	}
}
