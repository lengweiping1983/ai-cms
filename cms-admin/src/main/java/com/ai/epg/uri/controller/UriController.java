package com.ai.epg.uri.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ai.AdminConstants;
import com.ai.AdminGlobal;
import com.ai.AppGlobal;
import com.ai.cms.star.entity.Star;
import com.ai.cms.star.repository.StarRepository;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.*;
import com.ai.common.exception.ServiceException;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;
import com.ai.epg.album.service.AlbumService;
import com.ai.epg.category.entity.Category;
import com.ai.epg.category.repository.CategoryRepository;
import com.ai.epg.category.service.CategoryService;
import com.ai.epg.config.entity.App;
import com.ai.epg.config.repository.AppRepository;
import com.ai.epg.template.entity.Template;
import com.ai.epg.template.entity.TemplateParam;
import com.ai.epg.template.repository.TemplateParamRepository;
import com.ai.epg.template.repository.TemplateRepository;
import com.ai.epg.template.service.TemplateService;
import com.ai.epg.uri.bean.UriImageBean;
import com.ai.epg.uri.bean.UriParamBean;
import com.ai.epg.uri.bean.UriTemplateBean;
import com.ai.epg.uri.entity.Uri;
import com.ai.epg.uri.entity.UriParam;
import com.ai.epg.uri.repository.UriParamRepository;
import com.ai.epg.uri.repository.UriRepository;
import com.ai.epg.uri.service.UriService;
import com.ai.epg.widget.entity.Widget;
import com.ai.epg.widget.repository.WidgetRepository;
import com.ai.epg.widget.service.WidgetService;

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

@Controller
@RequestMapping(value = { "/uri/uri" })
public class UriController extends AbstractImageController {

	@Autowired
	private UriRepository uriRepository;

	@Autowired
	private UriParamRepository uriParamRepository;

	@Autowired
	private UriService uriService;

	@Autowired
	private AppRepository appRepository;

	@Autowired
	private TemplateRepository templateRepository;

	@Autowired
	private TemplateParamRepository templateParamRepository;
	
	@Autowired
	private TemplateService templateService;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private WidgetRepository widgetRepository;

	@Autowired
	protected WidgetService widgetService;
	
	@Autowired
	protected CategoryService categoryService;

	@Autowired
	protected AlbumService albumService;
	
	@Autowired
	private StarRepository starRepository;

	private void setModel(Model model, HttpServletRequest request) {
		model.addAttribute("statusEnum", OnlineStatusEnum.values());

		model.addAttribute("uriTypeEnum", UriTypeEnum.values());
		model.addAttribute("yesNoEnum", YesNoEnum.values());

		String currentAppCode = (String) request.getSession().getAttribute(
				"currentAppCode");
		List<Template> templateList = templateRepository
				.findByAppCodeIncludeShare(currentAppCode);
		model.addAttribute("templateList", templateList);

		List<App> appList = appRepository.findAll();
		model.addAttribute("appList", appList);
	}

	private void setSession(HttpServletRequest request) {
		String currentAppCode = request.getParameter("appCode");
		if (StringUtils.isNotEmpty(currentAppCode)) {
			request.getSession().setAttribute("currentAppCode", currentAppCode);
		}
	}

	private void setContentPage(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("sortIndex");
		}

		List<PropertyFilter> filters = getPropertyFilters(request);

		String currentAppCode = (String) request.getSession().getAttribute(
				"currentAppCode");
		filters.add(new PropertyFilter("appCode__EQ_S", currentAppCode));

		Specification<Uri> specification = SpecificationUtils
				.getSpecification(filters);
		Page<Uri> page = find(specification, pageInfo, uriRepository);
		model.addAttribute("page", page);
	}

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		setSession(request);

		setContentPage(model, request, pageInfo);

		setModel(model, request);

		return "uri/uri/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model, HttpServletRequest request) {
		Uri uri = new Uri();
		model.addAttribute("uri", uri);

		setModel(model, request);

		return "uri/uri/edit";
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "页面管理", action = "增加", message = "增加页面")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(HttpServletRequest request,
			@RequestBody UriImageBean<Uri> imageBean) {
		return edit(request, imageBean, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, HttpServletRequest request,
			@PathVariable("id") Long id) {
		Uri uri = uriRepository.findOne(id);
		model.addAttribute("uri", uri);

		if (uri != null && StringUtils.isNotEmpty(uri.getTemplateCode())) {
			Template template = templateService.getTemplate(uri.getAppCode(),
					uri.getTemplateCode());
			model.addAttribute("template", template);
		}

		setModel(model, request);

		return "uri/uri/edit";
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "页面管理", action = "修改", message = "修改页面")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(HttpServletRequest request,
			@RequestBody UriImageBean<Uri> imageBean,
			@PathVariable("id") Long id) {
		String appCode = (String) request.getSession().getAttribute(
				"currentAppCode");
		Uri uri = imageBean.getData();
		uri.setCode(StringUtils.upperCase(uri.getCode()));
		List<UriParamBean> params = imageBean.getParams();
		String image1Data = imageBean.getImage1Data();
		String image2Data = imageBean.getImage2Data();
		String backgroundImageData = imageBean.getBackgroundImageData();
		String logoImageData = imageBean.getLogoImageData();

		Uri uriInfo = null;
		String image1Old = "";
		String image2Old = "";
		String backgroundImageOld = "";
		String logoImageOld = "";
		if (id == null) {
			uriInfo = uri;
			uriInfo.setSiteCode(AdminGlobal.getSiteCode());
		} else {
			uriInfo = uriRepository.findOne(id);
			if (uri != null && !uriInfo.getTitle().equals(uri.getTitle())) {
				AdminGlobal.operationLogMessage.set("页面修改标题：<br/>" + uriInfo.getTitle() + "  ==>  " + uri.getTitle());
			} else {
				AdminGlobal.operationLogAction.set("增加");
				AdminGlobal.operationLogMessage.set("增加页面");
			}
			image1Old = uriInfo.getImage1();
			image2Old = uriInfo.getImage2();
			backgroundImageOld = uriInfo.getBackgroundImage();
			logoImageOld = uriInfo.getLogoImage();
			BeanInfoUtil
					.bean2bean(
							uri,
							uriInfo,
							"type,internal,code,name,title,url,sortIndex,status,templateId,templateCode,"
							+ "backgroundImage,logoImage,tag,keyword,description,appCode,image1,image2");
		}
		if (uri.getInternal() != null
				&& uri.getInternal() == YesNoEnum.NO.getKey()) {
			uriInfo.setTemplateCode(null);
		} else if (uri.getInternal() != null
				&& uri.getInternal() == YesNoEnum.YES.getKey()) {
			uriInfo.setUrl(null);
		}

		String image1 = "";
		String image2 = "";
		if (StringUtils.isNotEmpty(image1Data)) {
			image1 = upload(AdminConstants.MODULE_RESOURCE_URI,
					AdminConstants.RESOURCE_TYPE_POSTER, image1Data);
			uriInfo.setImage1(image1);
		}
		if (StringUtils.isNotEmpty(image2Data)) {
			image2 = upload(AdminConstants.MODULE_RESOURCE_URI,
					AdminConstants.RESOURCE_TYPE_POSTER, image2Data);
			uriInfo.setImage2(image2);
		}
		String backgroundImage = "";
		if (StringUtils.isNotEmpty(backgroundImageData)) {
			backgroundImage = upload(AdminConstants.MODULE_RESOURCE_TEMPLATE,
					AdminConstants.RESOURCE_TYPE_BACKGROUND,
					backgroundImageData);
			uriInfo.setBackgroundImage(backgroundImage);
		}
		String logoImage = "";
		if (StringUtils.isNotEmpty(logoImageData)) {
			logoImage = upload(AdminConstants.MODULE_RESOURCE_TEMPLATE,
					AdminConstants.RESOURCE_TYPE_POSTER, logoImageData);
			uriInfo.setLogoImage(logoImage);
		}
		try {
			uriService.saveUri(uriInfo, params, appCode);
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			throw new ServiceException("保存失败，可能参数有重复！");
		}

		if (!StringUtils.trimToEmpty(image1Old).equals(
				StringUtils.trimToEmpty(uri.getImage1()))) {
			deleteOldResource(image1Old);
		}

		if (!StringUtils.trimToEmpty(image2Old).equals(
				StringUtils.trimToEmpty(uri.getImage2()))) {
			deleteOldResource(image2Old);
		}
		if (!StringUtils.trimToEmpty(backgroundImageOld).equals(
				StringUtils.trimToEmpty(uri.getBackgroundImage()))) {
			deleteOldResource(backgroundImageOld);
		}
		if (!StringUtils.trimToEmpty(logoImageOld).equals(
				StringUtils.trimToEmpty(uri.getLogoImage()))) {
			deleteOldResource(logoImageOld);
		}
		AdminGlobal.operationLogTypeAndId.set(ItemTypeEnum.URI.getKey()+"$"+uriInfo.getId());
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "页面管理", action = "删除", message = "删除页面")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		Uri uriInfo = uriRepository.findOne(id);
		if (uriInfo != null) {
			String image1Old = uriInfo.getImage1();
			String image2Old = uriInfo.getImage2();
			String backgroundImageOld = uriInfo.getBackgroundImage();
			String logoImageOld = uriInfo.getLogoImage();
			uriService.deleteUri(uriInfo);

			deleteOldResource(image1Old);
			deleteOldResource(image2Old);
			deleteOldResource(backgroundImageOld);
			deleteOldResource(logoImageOld);
			AdminGlobal.operationLogTypeAndId.set(ItemTypeEnum.URI.getKey()+"$"+uriInfo.getId());
			AdminGlobal.operationLogMessage.set("删除页面");
		}

		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, HttpServletRequest request,
			@PathVariable("id") Long id) {
		Uri uri = uriRepository.findOne(id);
		model.addAttribute("uri", uri);
		
		if (uri != null && StringUtils.isNotEmpty(uri.getTemplateCode())) {
			Template template = templateService.getTemplate(uri.getAppCode(),
					uri.getTemplateCode());
			model.addAttribute("template", template);
		}

		setModel(model, request);

		return "uri/uri/detail";
	}

	@RequestMapping(value = { "check" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Object[] check(HttpServletRequest request,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "fieldId") String fieldId,
			@RequestParam(value = "fieldValue") String fieldValue) {
		String currentAppCode = (String) request.getSession().getAttribute(
				"currentAppCode");

		Object[] jsonValidateReturn = new Object[3];
		jsonValidateReturn[0] = fieldId;
		jsonValidateReturn[1] = false;
		if (StringUtils.isEmpty(fieldValue)) {
			jsonValidateReturn[2] = "页面代码不能为空!";
		} else if (StringUtils.upperCase(fieldValue).indexOf(
				AppGlobal.URI_CODE_PRE) != 0) {
			jsonValidateReturn[2] = "页面代码[" + fieldValue + "]需要以"
					+ AppGlobal.URI_CODE_PRE + "开头!";
		} else if (AppGlobal.URI_CODE_PRE.equalsIgnoreCase(fieldValue)) {
			jsonValidateReturn[2] = "页面代码[" + fieldValue + "]不正确!";
		} else {
			boolean exist = checkCode(id, currentAppCode, fieldValue);
			if (!exist) {
				jsonValidateReturn[1] = true;
				jsonValidateReturn[2] = "可以使用!";
			} else {
				jsonValidateReturn[2] = "页面代码" + StringUtils.trim(fieldValue)
						+ "已使用!";
			}
		}
		return jsonValidateReturn;
	}

	private boolean checkCode(Long id, String appCode, String code) {
		boolean exist = false;
		Uri uri = null;
		if (StringUtils.isNotEmpty(code)) {
			uri = uriRepository.findOneByAppCodeAndCode(appCode, code);
		}
		if (uri != null) {
			if (id == null || id == -1 || uri.getId().longValue() != id) {
				exist = true;
			}
		}
		return exist;
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "页面管理", action = "上下线", message = "上下线页面")
	@RequestMapping(value = { "{id}/status" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult status(@PathVariable("id") Long id) {
		Uri uriInfo = uriRepository.findOne(id);
		if (uriInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			uriInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
		} else {
			uriInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
		}
		if (uriInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			uriInfo.setOnlineTime(new Date());
			uriInfo.setOfflineTime(null);
		} else if (uriInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			uriInfo.setOfflineTime(new Date());
		}
		uriService.saveUri(uriInfo);
		AdminGlobal.operationLogTypeAndId.set(ItemTypeEnum.URI.getKey()+"$"+uriInfo.getId());
		if (uriInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			AdminGlobal.operationLogAction.set("上线");
			AdminGlobal.operationLogMessage.set("上线页面");
		} else if (uriInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			AdminGlobal.operationLogAction.set("下线");
			AdminGlobal.operationLogMessage.set("下线页面");
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "selectUri" })
	public String selectUri(Model model, HttpServletRequest request,
			PageInfo pageInfo) {

		setContentPage(model, request, pageInfo);

		setModel(model, request);

		return "uri/uri/selectUri";
	}

	@RequestMapping(value = { "selectItem" })
	public String selectItem(Model model, HttpServletRequest request,
			PageInfo pageInfo) {

		setContentPage(model, request, pageInfo);

		setModel(model, request);

		return "uri/uri/selectItem";
	}

	@RequestMapping(value = { "loadUriConfigById" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public UriTemplateBean loadUriConfigById(
			HttpServletRequest request,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "internalParam", required = false) String internalParam,
			@RequestParam(value = "internalParamCategory", required = false) Integer internalParamCategory,
			@RequestParam(value = "itemType", required = false) Integer itemType,
			@RequestParam(value = "itemId", required = false) Long itemId) {
		UriTemplateBean result = new UriTemplateBean();
		if (id == null) {
			return result;
		}
		Uri uri = uriRepository.findOne(id);
		if (uri == null) {
			return result;
		}
		return loadUriConfig(request, uri.getId(), uri.getTemplateId(),
				internalParam, internalParamCategory, itemType, itemId);
	}

	@RequestMapping(value = { "loadUriConfig" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public UriTemplateBean loadUriConfig(
			HttpServletRequest request,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "templateId", required = true) Long templateId,
			@RequestParam(value = "internalParam", required = false) String internalParam,
			@RequestParam(value = "internalParamCategory", required = false) Integer internalParamCategory,
			@RequestParam(value = "itemType", required = false) Integer itemType,
			@RequestParam(value = "itemId", required = false) Long itemId) {
		UriTemplateBean result = new UriTemplateBean();
		if (templateId == null) {
			return result;
		}
		Template template = templateRepository.findOne(templateId);
		if (template == null) {
			return result;
		}
		BeanInfoUtil.bean2bean(template, result,
				"id,code,name,configBackgroundImage,backgroundImage");
		result.setCode(template.getCode() + "@" + template.getAppCode());
		List<UriParam> uriParamList = null;
		if (id != null) {
			uriParamList = uriParamRepository.findAllByUriId(id);
		}

		boolean dynamicCategory = false;
		List<TemplateParam> templateParamListAll = templateParamRepository
				.findAllByTemplateId(templateId);
		List<TemplateParam> templateParamList = new ArrayList<TemplateParam>();
		for (TemplateParam templateParam : templateParamListAll) {
			if (templateParam.getInternalParamCategory() == internalParamCategory) {
				templateParamList.add(templateParam);
			}
			if (templateParam.getInternalParamCategory() == TemplateParamCategoryEnum.DYNAMIC
					.getKey()) {
				dynamicCategory = true;
			}
		}
		result.setDynamicCategory(dynamicCategory);
		result.setInternalParamCategory(internalParamCategory);
		
		for (TemplateParam templateParam : templateParamList) {
			UriParamBean uriParamBean = null;
			boolean exist = false;
			if (itemType != null && itemId != null) {
				if (templateParam.getInternalParamCategory() == TemplateParamCategoryEnum.DYNAMIC
						.getKey() && templateParam.getType() == itemType) {// 动态连动参数
					exist = true;
					uriParamBean = loadUriParamConfig(request, templateParam,
							null, templateParam.getCode(), itemId, null);
				}
			}

			if (!exist && StringUtils.isNotEmpty(internalParam)) {
				String[] internalParamArr = internalParam.split("&amp;");
				for (String param : internalParamArr) {
					String[] value = param.split("\\$");
					if (value != null && value.length >= 3
							&& StringUtils.isNotEmpty(value[0])
							&& StringUtils.isNotEmpty(value[1])
							&& StringUtils.isNotEmpty(value[2])) {
						String numberParam = StringUtils.trimToEmpty(value[0]);
						// String typeParam = StringUtils.trimToEmpty(value[1]);
						String codeParam = StringUtils.trimToEmpty(value[2]);
						String styleParam = null;
						if (value.length >= 4) {
							styleParam = StringUtils.trimToEmpty(value[3]);
						}
						if (templateParam.getNumber().equals(numberParam)) {
							exist = true;
							uriParamBean = loadUriParamConfig(request,
									templateParam, null, codeParam, null,
									styleParam);
						}
					}
				}
			}
			if (!exist && uriParamList != null) {
				for (UriParam uriParam : uriParamList) {
					if (templateParam.getNumber().equals(uriParam.getNumber())) {
						exist = true;
						uriParamBean = loadUriParamConfig(request,
								templateParam, uriParam,
								templateParam.getCode(), uriParam.getItemId(),
								uriParam.getStyle());
						break;
					}
				}
			}
			if (!exist) {
				uriParamBean = loadUriParamConfig(request, templateParam, null,
						templateParam.getCode(), null, null);
			}
			result.getParams().add(uriParamBean);
		}
		return result;
	}

	@RequestMapping(value = { "loadUriParamConfig" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public UriParamBean loadUriParamConfig(
			HttpServletRequest request,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "templateId", required = true) Long templateId,
			@RequestParam(value = "type", required = true) Integer type,
			@RequestParam(value = "number", required = true) String number,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "itemId", required = false) Long itemId) {
		if (templateId == null || type == null) {
			return null;
		}
		TemplateParam templateParam = templateParamRepository
				.findOneByTemplateIdAndNumber(templateId, number);
		if (templateParam == null) {
			return null;
		}
		UriParamBean uriParamBean = loadUriParamConfig(request, templateParam,
				null, code, itemId, null);
		return uriParamBean;
	}

	public UriParamBean loadUriParamConfig(HttpServletRequest request,
			TemplateParam templateParam, UriParam uriParam, String searchCode,
			Long itemId, String style) {
		UriParamBean uriParamBean = new UriParamBean();
		BeanInfoUtil.bean2bean(templateParam, uriParamBean,
				"number,type,mode,name,styleConfig,description,configCode,configItemTypes,"
						+ "configImage1,configImage1Width,configImage1Height,"
						+ "configImage2,configImage2Width,configImage2Height");
		String appCode = (String) request.getSession().getAttribute(
				"currentAppCode");
		TemplateParamTypeEnum templateParamTypeEnum = TemplateParamTypeEnum
				.getEnumByKey(uriParamBean.getType());
		TemplateParamModeEnum templateParamModeEnum = TemplateParamModeEnum
				.getEnumByKey(uriParamBean.getMode());
		uriParamBean.setTypeName(templateParamTypeEnum.getValue());
		uriParamBean.setModeName(templateParamModeEnum.getValue());
		uriParamBean.setStyle(style);
		String targetAppCode = appCode;
		String targetCode = searchCode;
		if (StringUtils.isNotEmpty(searchCode) && searchCode.contains("@")) {
			targetAppCode = StringUtils.upperCase(StringUtils
					.substringAfterLast(searchCode, "@"));
			targetCode = StringUtils.upperCase(StringUtils.substringBefore(
					searchCode, "@"));
		}
		if (templateParam.getType() == TemplateParamTypeEnum.WIDGET.getKey()) {
			Widget widget = null;
			if (itemId != null) {
				widget = widgetRepository.findOne(itemId);
			}
			if (widget == null && StringUtils.isNotEmpty(targetCode)) {
				widget = widgetRepository.findOneByAppCodeAndCode(
						targetAppCode, targetCode);
			}
			if (widget != null) {
				uriParamBean.setExist(true);
				uriParamBean.setCode(widget.getCode() + "@"
						+ widget.getAppCode());
				uriParamBean.setName(widget.getName());

				uriParamBean.setItemId(widget.getId());
				if (uriParam != null) {
					uriParamBean.setId(uriParam.getId());
				}
			} else {
				if (StringUtils.isNotEmpty(targetCode)) {
					uriParamBean.setCode(targetCode + "@" + targetAppCode);
				}
			}
		} else if (templateParam.getType() == TemplateParamTypeEnum.CATEGORY
				.getKey()) {
			Category category = null;
			if (itemId != null) {
				category = categoryRepository.findOne(itemId);
			}
			if (category == null && StringUtils.isNotEmpty(targetCode)) {
				category = categoryRepository.findOneByAppCodeAndCode(
						targetAppCode, targetCode);
			}
			if (category != null) {
				uriParamBean.setExist(true);
				uriParamBean.setCode(category.getCode() + "@"
						+ category.getAppCode());
				uriParamBean.setName(category.getName());

				uriParamBean.setItemId(category.getId());
				if (uriParam != null) {
					uriParamBean.setId(uriParam.getId());
				}
			} else {
				if (StringUtils.isNotEmpty(targetCode)) {
					uriParamBean.setCode(targetCode + "@" + targetAppCode);
				}
			}
		} else if (templateParam.getType() == TemplateParamTypeEnum.STAR
				.getKey()) {
			Star star = null;
			if (itemId != null) {
				star = starRepository.findOne(itemId);
			}
			if (star == null && StringUtils.isNotEmpty(targetCode)) {
				try {
					star = starRepository.findOne(Long.valueOf(targetCode));
				} catch (Exception e) {

				}
			}
			if (star != null) {
				uriParamBean.setExist(true);
				uriParamBean.setCode("" + star.getId());
				uriParamBean.setName(star.getName());

				uriParamBean.setItemId(star.getId());
				if (uriParam != null) {
					uriParamBean.setId(uriParam.getId());
				}
			}
		}
		if (StringUtils.isEmpty(uriParamBean.getCode())) {
			uriParamBean.setCode("");
		}
		if (StringUtils.isEmpty(uriParamBean.getName())) {
			uriParamBean.setName("");
		}
		return uriParamBean;
	}

	@RequestMapping(value = { "{id}/listByPosition" })
	public String listByPosition(@PathVariable("id") Long id, Model model,
			HttpServletRequest request) {
		Uri uri = uriRepository.findOne(id);
		model.addAttribute("uri", uri);

		Long templateId = uri.getTemplateId();

		if (templateId != null) {
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
		}

		setModel(model, request);

		return "uri/uri/listByPosition";
	}

	@RequestMapping(value = { "{id}/getUriParam" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public UriParamBean getUriParam(HttpServletRequest request,
			@PathVariable("id") Long id) {
		UriParamBean uriParamBean = new UriParamBean();
		String positionId = request.getParameter("positionId");
		String internalParam = request.getParameter("internalParam");
		Uri uri = uriRepository.findOne(id);

		Long templateId = uri.getTemplateId();

		if (templateId != null) {
			TemplateParam templateParam = templateParamRepository
					.findOneByTemplateIdAndPositionId(templateId, positionId);
			if (templateParam != null) {
				uriParamBean.setType(templateParam.getType());
				uriParamBean.setMode(templateParam.getMode());
				uriParamBean.setInternalParamCategory(templateParam.getInternalParamCategory());
				UriParam uriParam = uriParamRepository
						.findOneByUriIdAndItemTypeAndNumber(id,
								templateParam.getType(),
								templateParam.getNumber());
				if (uriParam != null) {
					uriParamBean.setItemId(uriParam.getItemId());
				}
				
				if (StringUtils.isNotEmpty(internalParam)) {
					String[] internalParamArr = internalParam.split("&amp;");
					for (String param : internalParamArr) {
						String[] value = param.split("\\$");
						if (value != null && value.length >= 3
								&& StringUtils.isNotEmpty(value[0])
								&& StringUtils.isNotEmpty(value[1])
								&& StringUtils.isNotEmpty(value[2])) {
							String numberParam = StringUtils
									.trimToEmpty(value[0]);
							String typeParam = StringUtils.trimToEmpty(value[1]);
							String codeParam = StringUtils
									.trimToEmpty(value[2]);
							if (templateParam.getNumber().equals(numberParam)) {
								if (typeParam
										.equals(""
												+ TemplateParamTypeEnum.WIDGET
														.getKey())) {
									Widget widget = widgetService.getWidget("",
											codeParam);
									if (widget != null) {
										uriParamBean.setItemId(widget.getId());
									}
								} else if (typeParam.equals(""
										+ TemplateParamTypeEnum.CATEGORY
												.getKey())) {
									Category category = categoryService
											.getCategory("", codeParam);
									if (category != null) {
										uriParamBean
												.setItemId(category.getId());
									}
								}
							}
						}
					}
				}
			}
		}
		return uriParamBean;
	}

}
