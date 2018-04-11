package com.ai.epg.template.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.AppGlobal;
import com.ai.cms.media.bean.ImageBean;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.CategoryItemTypeEnum;
import com.ai.common.enums.TemplateParamCategoryEnum;
import com.ai.common.enums.TemplateParamModeEnum;
import com.ai.common.enums.TemplateParamTypeEnum;
import com.ai.common.enums.WidgetItemTypeEnum;
import com.ai.common.enums.WidgetTypeEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.exception.ServiceException;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.epg.template.entity.Template;
import com.ai.epg.template.entity.TemplateParam;
import com.ai.epg.template.repository.TemplateParamRepository;
import com.ai.epg.template.repository.TemplateRepository;
import com.ai.epg.template.service.TemplateService;

@Controller
@RequestMapping(value = { "/template/{templateId}/templateParam" })
public class TemplateParamController extends AbstractImageController {

	@Autowired
	private TemplateRepository templateRepository;

	@Autowired
	private TemplateParamRepository templateParamRepository;

	@Autowired
	private TemplateService templateService;

	private void setModel(Model model) {
		model.addAttribute("yesNoEnum", YesNoEnum.values());
		model.addAttribute("typeEnum", TemplateParamTypeEnum.values());
		model.addAttribute("widgetTypeEnum", WidgetTypeEnum.values());
		model.addAttribute("widgetItemTypeEnum", WidgetItemTypeEnum.values());
		model.addAttribute("categoryItemTypeEnum",
				CategoryItemTypeEnum.values());
		model.addAttribute("templateParamModeEnum",
				TemplateParamModeEnum.values());
		model.addAttribute("templateParamCategoryEnum",
				TemplateParamCategoryEnum.values());
	}

	@RequestMapping(value = { "" })
	public String list(@PathVariable("templateId") Long templateId,
			Model model, HttpServletRequest request, PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("sortIndex");
		}

		Template template = templateRepository.findOne(templateId);
		model.addAttribute("template", template);

		Page<TemplateParam> page = find(request, pageInfo,
				templateParamRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "template/templateParam/list";
	}

	@RequestMapping(value = { "listByPosition" })
	public String listByPosition(@PathVariable("templateId") Long templateId,
			Model model, HttpServletRequest request) {
		Template template = templateRepository.findOne(templateId);
		model.addAttribute("template", template);

		String positionStr = "";
		List<TemplateParam> list = templateParamRepository
				.findAllByTemplateId(templateId);
		for (TemplateParam templateParam : list) {
			if (StringUtils.isNotEmpty(templateParam.getPosition())) {
				positionStr += StringUtils.trimToEmpty(templateParam
						.getPosition()) + ",";
			}
		}
		model.addAttribute("positionStr", positionStr);

		setModel(model);

		return "template/templateParam/listByPosition";
	}

	@RequestMapping(value = { "editByPosition" }, method = RequestMethod.GET)
	public String toEditByPosition(Model model, HttpServletRequest request,
			@PathVariable("templateId") Long templateId) {
		String positionId = request.getParameter("positionId");

		TemplateParam templateParam = templateParamRepository
				.findOneByTemplateIdAndPositionId(templateId, positionId);
		if (templateParam == null) {
			templateParam = new TemplateParam();
			templateParam.setPositionId(positionId);
		}

		model.addAttribute("templateParam", templateParam);

		Template template = templateRepository.findOne(templateId);
		model.addAttribute("template", template);

		setModel(model);
		return "template/templateParam/editByPosition";
	}

	@RequestMapping(value = { "editByPosition" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult editByPosition(Model model, HttpServletRequest request,
			@PathVariable("templateId") Long templateId,
			@RequestBody ImageBean<TemplateParam> imageBean) {
		TemplateParam templateParam = imageBean.getData();
		templateParam
				.setNumber(StringUtils.upperCase(templateParam.getNumber()));
		templateParam.setCode(StringUtils.upperCase(templateParam.getCode()));

		TemplateParam templateParamInfo = templateParamRepository
				.findOneByTemplateIdAndPositionId(templateId,
						templateParam.getPositionId());
		if (templateParamInfo != null) {
			BeanInfoUtil
					.bean2bean(templateParam, templateParamInfo, "position");
			templateParamRepository.save(templateParamInfo);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model, HttpServletRequest request,
			@PathVariable("templateId") Long templateId) {
		TemplateParam templateParam = new TemplateParam();
		model.addAttribute("templateParam", templateParam);

		Template template = templateRepository.findOne(templateId);
		model.addAttribute("template", template);

		setModel(model);

		return "template/templateParam/edit";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@PathVariable("templateId") Long templateId,
			@RequestBody ImageBean<TemplateParam> imageBean) {
		return edit(templateId, imageBean, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model,
			@PathVariable("templateId") Long templateId,
			@PathVariable("id") Long id) {
		TemplateParam templateParam = templateParamRepository.findOne(id);
		model.addAttribute("templateParam", templateParam);

		Template template = templateRepository.findOne(templateId);
		model.addAttribute("template", template);

		setModel(model);

		return "template/templateParam/edit";
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@PathVariable("templateId") Long templateId,
			@RequestBody ImageBean<TemplateParam> imageBean,
			@PathVariable("id") Long id) {
		TemplateParam templateParam = imageBean.getData();
		if (StringUtils.isEmpty(templateParam.getNumber())) {
			throw new ServiceException("参数编号不能为空!");
		} else if (StringUtils.upperCase(templateParam.getNumber()).indexOf(
				AppGlobal.TEMPLATE_PARAM_NUMBER_PRE) != 0) {
			throw new ServiceException("参数编号[" + templateParam.getNumber()
					+ "]需要以" + AppGlobal.TEMPLATE_PARAM_NUMBER_PRE + "开头!");
		} else if (AppGlobal.TEMPLATE_PARAM_NUMBER_PRE
				.equalsIgnoreCase(templateParam.getNumber())) {
			throw new ServiceException("参数编号[" + templateParam.getNumber()
					+ "]不正确!");
		}
		templateParam
				.setNumber(StringUtils.upperCase(templateParam.getNumber()));
		templateParam.setCode(StringUtils.upperCase(templateParam.getCode()));

		templateParam.setStyleConfig(StringUtils.trimToEmpty(templateParam
				.getStyleConfig()));
		if (StringUtils.isNotEmpty(templateParam.getStyleConfig())) {
			String[] styleArrAll = templateParam.getStyleConfig().split("\\$");
			for (String styleStr : styleArrAll) {
				String[] styleArr = styleStr.split("=");
				if (styleArr.length != 2) {
					throw new ServiceException("样式配置格式不正确!");
				}
			}
		}

		TemplateParam templateParamInfo = null;
		if (id == null) {
			TemplateParam existTemplateParam = null;
			if (StringUtils.isNotEmpty(templateParam.getCode())) {
				existTemplateParam = templateParamRepository
						.findOneByTemplateIdAndTypeAndCode(templateId,
								templateParam.getType(),
								templateParam.getCode());
				if (existTemplateParam != null) {
					throw new ServiceException("该参数[参数值:"
							+ templateParam.getCode() + "]已存在！");
				}
			}
			existTemplateParam = templateParamRepository
					.findOneByTemplateIdAndNumber(templateId,
							templateParam.getNumber());
			if (existTemplateParam != null) {
				throw new ServiceException("该参数[参数编号:"
						+ templateParam.getNumber() + "]已存在！");
			}
			templateParamInfo = templateParam;
		} else {
			TemplateParam existTemplateParam = null;
			if (StringUtils.isNotEmpty(templateParam.getCode())) {
				existTemplateParam = templateParamRepository
						.findOneByTemplateIdAndTypeAndCode(templateId,
								templateParam.getType(),
								templateParam.getCode());
				if (existTemplateParam != null
						&& id.longValue() != existTemplateParam.getId()) {
					throw new ServiceException("该参数[参数值:"
							+ templateParam.getCode() + "]已存在！");
				}
			}
			existTemplateParam = templateParamRepository
					.findOneByTemplateIdAndNumber(templateId,
							templateParam.getNumber());
			if (existTemplateParam != null
					&& id.longValue() != existTemplateParam.getId()) {
				throw new ServiceException("该参数[参数编号:"
						+ templateParam.getNumber() + "]已存在！");
			}
			templateParamInfo = templateParamRepository.findOne(id);
			BeanInfoUtil
					.bean2bean(
							templateParam,
							templateParamInfo,
							"number,type,code,name,mode,internalParamCategory,styleConfig,description,"
									+ "configCode,widgetType,widgetItemNum,configItemTypes,"
									+ "configImage1,configImage1Width,configImage1Height,"
									+ "configImage2,configImage2Width,configImage2Height,"
									+ "sortIndex,positionId,position");
		}

		templateParamRepository.save(templateParamInfo);

		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("templateId") Long templateId,
			@PathVariable("id") Long id) {
		TemplateParam templateParamInfo = templateParamRepository.findOne(id);
		if (templateParamInfo != null) {
			templateParamRepository.delete(templateParamInfo);
		}
		return new BaseResult();
	}
}
