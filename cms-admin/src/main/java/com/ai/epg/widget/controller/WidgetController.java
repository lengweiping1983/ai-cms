package com.ai.epg.widget.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ai.AppGlobal;
import com.ai.cms.media.bean.ImageBean;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.*;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;
import com.ai.epg.config.entity.App;
import com.ai.epg.config.repository.AppRepository;
import com.ai.epg.widget.entity.Widget;
import com.ai.epg.widget.repository.WidgetItemRepository;
import com.ai.epg.widget.repository.WidgetRepository;
import com.ai.epg.widget.service.WidgetService;
import com.ai.AdminConstants;
import com.ai.AdminGlobal;

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
@RequestMapping(value = { "/widget/widget" })
public class WidgetController extends AbstractImageController {

	@Autowired
	private WidgetRepository widgetRepository;

	@Autowired
	private WidgetItemRepository widgetItemRepository;

	@Autowired
	private WidgetService widgetService;

	@Autowired
	private AppRepository appRepository;

	private void setModel(Model model, HttpServletRequest request) {
		model.addAttribute("statusEnum", OnlineStatusEnum.values());
		model.addAttribute("yesNoEnum", YesNoEnum.values());
		model.addAttribute("typeEnum", WidgetTypeEnum.values());
		model.addAttribute("itemTypeEnum", WidgetItemTypeEnum.values());

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

		Specification<Widget> specification = SpecificationUtils
				.getSpecification(filters);
		Page<Widget> page = find(specification, pageInfo, widgetRepository);
		model.addAttribute("page", page);

		setModel(model, request);
	}

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		setSession(request);

		setContentPage(model, request, pageInfo);

		setModel(model, request);

		return "widget/widget/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model, HttpServletRequest request) {
		Widget widget = new Widget();
		model.addAttribute("widget", widget);

		setModel(model, request);

		return "widget/widget/edit";
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "推荐位管理", action = "增加", message = "增加推荐位")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody ImageBean<Widget> imageBean) {
		return edit(imageBean, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, HttpServletRequest request,
			@PathVariable("id") Long id) {
		Widget widget = widgetRepository.findOne(id);
		model.addAttribute("widget", widget);

		setModel(model, request);

		return "widget/widget/edit";
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "推荐位管理", action = "修改", message = "增加推荐位")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody ImageBean<Widget> imageBean,
			@PathVariable("id") Long id) {
		Widget widget = imageBean.getData();
		widget.setCode(StringUtils.upperCase(widget.getCode()));
		// String image1Data = imageBean.getImage1Data();
		// String image2Data = imageBean.getImage2Data();
		String backgroundImageData = imageBean.getBackgroundImageData();

		Widget widgetInfo = null;
		// String image1Old = "";
		// String image2Old = "";
		String backgroundImageOld = "";
		if (id == null) {
			widgetInfo = widget;
		} else {
			widgetInfo = widgetRepository.findOne(id);
			if (widget != null && !widgetInfo.getTitle().equals(widget.getTitle())) {
				AdminGlobal.operationLogMessage.set("推荐位修改标题：<br/>" + widgetInfo.getTitle() + "  ==>  " + widget.getTitle());
			} else {
				AdminGlobal.operationLogAction.set("增加");
				AdminGlobal.operationLogMessage.set("增加推荐位");
			}
			// image1Old = widgetInfo.getImage1();
			// image2Old = widgetInfo.getImage2();
			backgroundImageOld = widgetInfo.getBackgroundImage();
			BeanInfoUtil
					.bean2bean(
							widget,
							widgetInfo,
							"name,code,title,type,appCode,itemNum,sortIndex,status,"
									+ "configItemTypes,"
									+ "configImage1,configImage1Width,configImage1Height,"
									+ "configImage2,configImage2Width,configImage2Height,"
									+ "description,backgroundImage");
		}

		// String image1 = "";
		// String image2 = "";
		String backgroundImage = "";
		// if (StringUtils.isNotEmpty(image1Data)) {
		// image1 = upload(AdminConstants.MODULE_RESOURCE_WIDGET,
		// AdminConstants.RESOURCE_TYPE_POSTER, image1Data);
		// widgetInfo.setImage1(image1);
		// }
		// if (StringUtils.isNotEmpty(image2Data)) {
		// image2 = upload(AdminConstants.MODULE_RESOURCE_WIDGET,
		// AdminConstants.RESOURCE_TYPE_POSTER, image2Data);
		// widgetInfo.setImage2(image2);
		// }
		if (StringUtils.isNotEmpty(backgroundImageData)) {
			backgroundImage = upload(AdminConstants.MODULE_RESOURCE_WIDGET,
					AdminConstants.RESOURCE_TYPE_BACKGROUND,
					backgroundImageData);
			widgetInfo.setBackgroundImage(backgroundImage);
		}
		widgetRepository.save(widgetInfo);

		// if
		// (!StringUtils.trimToEmpty(image1Old).equals(StringUtils.trimToEmpty(widget.getImage1())))
		// {
		// deleteOldResource(image1Old);
		// }

		// if
		// (!StringUtils.trimToEmpty(image2Old).equals(StringUtils.trimToEmpty(widget.getImage2())))
		// {
		// deleteOldResource(image2Old);
		// }

		if (!StringUtils.trimToEmpty(backgroundImageOld).equals(
				StringUtils.trimToEmpty(widget.getBackgroundImage()))) {
			deleteOldResource(backgroundImageOld);
		}
		AdminGlobal.operationLogTypeAndId.set(ItemTypeEnum.WIDGET.getKey()+"$"+widgetInfo.getId());
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "推荐位管理", action = "删除", message = "删除推荐位")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		Widget widgetInfo = widgetRepository.findOne(id);
		if (widgetInfo != null) {
			// String image1Old = widgetInfo.getImage1();
			// String image2Old = widgetInfo.getImage2();
			String backgroundImageOld = widgetInfo.getBackgroundImage();

			widgetService.deleteWidget(widgetInfo);

			// deleteOldResource(image1Old);
			// deleteOldResource(image2Old);
			deleteOldResource(backgroundImageOld);
			AdminGlobal.operationLogTypeAndId.set(ItemTypeEnum.WIDGET.getKey()+"$"+widgetInfo.getId());
			AdminGlobal.operationLogMessage.set("删除推荐位");
		}
		return new BaseResult();
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
			jsonValidateReturn[2] = "推荐位代码不能为空!";
		} else if (StringUtils.upperCase(fieldValue).indexOf(
				AppGlobal.WIDGET_CODE_PRE) != 0) {
			jsonValidateReturn[2] = "推荐位代码[" + fieldValue + "]需要以"
					+ AppGlobal.WIDGET_CODE_PRE + "开头!";
		} else if (AppGlobal.WIDGET_CODE_PRE.equalsIgnoreCase(fieldValue)) {
			jsonValidateReturn[2] = "推荐位代码[" + fieldValue + "]不正确!";
		} else {
			boolean exist = checkCode(id, currentAppCode, fieldValue);
			if (!exist) {
				jsonValidateReturn[1] = true;
				jsonValidateReturn[2] = "可以使用!";
			} else {
				jsonValidateReturn[2] = "推荐位代码" + StringUtils.trim(fieldValue)
						+ "已使用!";
			}
		}
		return jsonValidateReturn;
	}

	private boolean checkCode(Long id, String appCode, String code) {
		boolean exist = false;
		Widget widget = null;
		if (StringUtils.isNotEmpty(code)) {
			widget = widgetRepository.findOneByAppCodeAndCode(appCode, code);
		}
		if (widget != null) {
			if (id == null || id == -1 || widget.getId().longValue() != id) {
				exist = true;
			}
		}
		return exist;
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "推荐位管理", action = "上下线", message = "上下线推荐位")
	@RequestMapping(value = { "{id}/status" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult status(@PathVariable("id") Long id) {
		Widget widgetInfo = widgetRepository.findOne(id);
		if (widgetInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			widgetInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
		} else {
			widgetInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
		}
		if (widgetInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			widgetInfo.setOnlineTime(new Date());
			widgetInfo.setOfflineTime(null);
		} else if (widgetInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			widgetInfo.setOfflineTime(new Date());
		}
		widgetRepository.save(widgetInfo);
		AdminGlobal.operationLogTypeAndId.set(ItemTypeEnum.WIDGET.getKey()+"$"+widgetInfo.getId());
		if (widgetInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			AdminGlobal.operationLogAction.set("上线");
			AdminGlobal.operationLogMessage.set("上线推荐位");
		} else if (widgetInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			AdminGlobal.operationLogAction.set("下线");
			AdminGlobal.operationLogMessage.set("下线推荐位");
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "batchToWidget" }, method = RequestMethod.GET)
	public String toBatchToWidget(Model model, HttpServletRequest request,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		setModel(model, request);

		return "widget/widget/batchToWidget";
	}

	@OperationLogAnnotation(module = "内容管理", subModule = "节目/剧集管理", action = "批量编排", message = "批量编排到推荐位")
	@RequestMapping(value = { "batchToWidget" }, method = { RequestMethod.POST }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchToWidget(
			@RequestParam(value = "selectWidgetId") String selectWidgetId,
			@RequestParam(value = "status") Integer status,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds,
			@RequestParam(value = "sortIndex") Integer sortIndex) {
		if (selectWidgetId == null || itemType == null
				|| StringUtils.isEmpty(itemIds)) {
			return new BaseResult();
		}
		widgetService.batchUpdate(selectWidgetId, status, itemType, itemIds, sortIndex);
		String typeAndIdStr = "";
		for (String s : itemIds.split(",")) {
			typeAndIdStr += (itemType+"$"+s+",");
		}
		AdminGlobal.operationLogTypeAndId.set(typeAndIdStr);
		Widget widget = widgetRepository.findOne(Long.valueOf(selectWidgetId));
		AdminGlobal.operationLogMessage.set("编排到推荐位: <br/>" + widget.getAppCode() + "  ==>  " + widget.getTitle());
		return new BaseResult();
	}

	@RequestMapping(value = { "selectWidget" })
	public String selectWidget(Model model, HttpServletRequest request,
			PageInfo pageInfo) {

		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("sortIndex");
		}

		List<PropertyFilter> filters = getPropertyFilters(request);

		String appCode = (String) request.getParameter("appCode");
		filters.add(new PropertyFilter("appCode__EQ_S", appCode));

		Specification<Widget> specification = SpecificationUtils
				.getSpecification(filters);
		Page<Widget> page = find(specification, pageInfo, widgetRepository);
		model.addAttribute("page", page);

		setModel(model, request);

		setModel(model, request);

		return "widget/widget/selectWidget";
	}
	
	@RequestMapping(value = { "selectItem" })
	public String selectItem(Model model, HttpServletRequest request,
			PageInfo pageInfo) {

		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}

		List<PropertyFilter> filters = getPropertyFilters(request);

		String appCode = (String) request.getParameter("search_appCode__EQ_S");
		if (appCode == null) {
			appCode = (String) request.getSession().getAttribute(
					"currentAppCode");
			filters.add(new PropertyFilter("appCode__EQ_S", appCode));
			request.setAttribute("search_appCode__EQ_S", appCode);
		}

		Specification<Widget> specification = SpecificationUtils
				.getSpecification(filters);
		Page<Widget> page = find(specification, pageInfo, widgetRepository);
		model.addAttribute("page", page);

		setModel(model, request);

		return "widget/widget/selectItem";
	}

}
