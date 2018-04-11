package com.ai.epg.category.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ai.AppGlobal;
import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.enums.InjectionStatusEnum;
import com.ai.cms.injection.repository.InjectionPlatformRepository;
import com.ai.cms.injection.service.InjectionService;
import com.ai.cms.media.bean.BatchInjectionBean;
import com.ai.cms.media.bean.BatchStatusBean;
import com.ai.cms.media.bean.ImageBean;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.bean.ResultCode;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.*;
import com.ai.common.exception.ServiceException;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;
import com.ai.epg.category.entity.Category;
import com.ai.epg.category.repository.CategoryItemRepository;
import com.ai.epg.category.repository.CategoryRepository;
import com.ai.epg.category.service.CategoryService;
import com.ai.epg.config.entity.App;
import com.ai.epg.config.repository.AppRepository;

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

import com.ai.AdminGlobal;

@Controller
@RequestMapping(value = { "/category/category/" })
public class CategoryController extends AbstractImageController {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryItemRepository categoryItemRepository;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private AppRepository appRepository;

	@Autowired
	private InjectionPlatformRepository injectionPlatformRepository;
	
	@Autowired
	private InjectionService injectionService;

	private void setModel(Model model, HttpServletRequest request) {
		model.addAttribute("statusEnum", OnlineStatusEnum.values());
		model.addAttribute("typeEnum", CategoryTypeEnum.values());
		model.addAttribute("itemTypeEnum", CategoryItemTypeEnum.values());
		
		List<App> appList = appRepository.findAll();
		model.addAttribute("appList", appList);

		model.addAttribute("yesNoEnum", YesNoEnum.values());

		model.addAttribute("injectionStatusEnum", InjectionStatusEnum.values());
	}

	private void setContentPage(Model model, HttpServletRequest request,
			PageInfo pageInfo) {

	}

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {

		setContentPage(model, request, pageInfo);

		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("sortIndex");
		}

		List<PropertyFilter> filters = getPropertyFilters(request);
		String search_parentId__EQ_L = request
				.getParameter("search_parentId__EQ_L");
		if (StringUtils.isNotEmpty(search_parentId__EQ_L)) {
			Category parent = categoryRepository.findOne(Long
					.valueOf(search_parentId__EQ_L));
			model.addAttribute("parent", parent);
		} else {
			filters.add(new PropertyFilter("parentId__ISNULL_L", null));
		}

		String currentAppCode = (String) request.getSession().getAttribute(
				"currentAppCode");
		filters.add(new PropertyFilter("appCode__EQ_S", currentAppCode));

		Specification<Category> specification = SpecificationUtils
				.getSpecification(filters);
		Page<Category> page = find(specification, pageInfo, categoryRepository);

		model.addAttribute("page", page);

		setModel(model, request);

		return "category/category/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model, HttpServletRequest request,
			@RequestParam(value = "parentId", required = false) Long parentId) {
		Category category = new Category();
		model.addAttribute("category", category);

		if (parentId != null) {
			Category parent = categoryRepository.findOne(parentId);
			category.setParent(parent);
			model.addAttribute("parent", parent);
		}

		setModel(model, request);

		return "category/category/edit";
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "栏目管理", action = "增加", message = "增加栏目")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody ImageBean<Category> imageBean) {
		return edit(imageBean, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, HttpServletRequest request,
			@PathVariable("id") Long id) {
		Category category = categoryRepository.findOne(id);
		model.addAttribute("category", category);
		if (category.getParentId() != null) {
			model.addAttribute("parent",
					categoryRepository.findOne(category.getParentId()));
		}

		setModel(model, request);

		return "category/category/edit";
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "栏目管理", action = "修改", message = "修改栏目")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody ImageBean<Category> imageBean,
			@PathVariable("id") Long id) {
		Category category = imageBean.getData();
		category.setCode(StringUtils.upperCase(category.getCode()));

		Category categoryInfo = null;
		if (id == null) {
			categoryInfo = category;
			categoryInfo.setSiteCode(AdminGlobal.getSiteCode());
		} else {
			categoryInfo = categoryRepository.findOne(id);
			if (category != null && !categoryInfo.getTitle().equals(category.getTitle())) {
				AdminGlobal.operationLogMessage.set("栏目修改标题：<br/>" + categoryInfo.getTitle() + "  ==>  " + category.getTitle());
			} else {
				AdminGlobal.operationLogAction.set("增加");
				AdminGlobal.operationLogMessage.set("增加栏目");
			}
			BeanInfoUtil
					.bean2bean(
							category,
							categoryInfo,
							"parentId,type,code,partnerItemCode,name,title,description,sortIndex,status,"
									+ "configItemTypes,"
									+ "configImage1,configImage1Width,configImage1Height,"
									+ "configImage2,configImage2Width,configImage2Height");
		}

		categoryRepository.save(categoryInfo);

		AdminGlobal.operationLogTypeAndId.set(ItemTypeEnum.CATEGORY.getKey()+"$"+categoryInfo.getId());
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "栏目管理", action = "删除", message = "删除栏目")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		Category categoryInfo = categoryRepository.findOne(id);
		if (categoryInfo != null) {
			List<Category> categoryList = categoryRepository.findByParentId(id);
			if (categoryList != null && categoryList.size() > 0) {
				throw new ServiceException("栏目[" + categoryInfo.getName()
						+ "]下存在子栏目，不能删除！");
			}
//			if (categoryInfo != null) {
//				Date currentTime = new Date();
//				injectionService.outInjection(categoryInfo, categoryInfo.getPlatformId(), 1, currentTime,
//						currentTime);
//			}
			categoryService.deleteCategory(categoryInfo);

			AdminGlobal.operationLogTypeAndId.set(ItemTypeEnum.CATEGORY.getKey()+"$"+categoryInfo.getId());
			AdminGlobal.operationLogMessage.set("删除栏目");
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, HttpServletRequest request,
			@PathVariable("id") Long id) {
		Category category = categoryRepository.findOne(id);
		model.addAttribute("category", category);
		if (category.getParentId() != null) {
			model.addAttribute("parent",
					categoryRepository.findOne(category.getParentId()));
		}

		setModel(model, request);

		return "category/category/detail";
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
			jsonValidateReturn[2] = "栏目代码不能为空!";
		} else if (StringUtils.upperCase(fieldValue).indexOf(
				AppGlobal.CATEGORY_CODE_PRE) != 0) {
			jsonValidateReturn[2] = "栏目代码[" + fieldValue + "]需要以"
					+ AppGlobal.CATEGORY_CODE_PRE + "开头!";
		} else if (AppGlobal.CATEGORY_CODE_PRE.equalsIgnoreCase(fieldValue)) {
			jsonValidateReturn[2] = "栏目代码[" + fieldValue + "]不正确!";
		} else {
			boolean exist = checkCode(id, currentAppCode, fieldValue);
			if (!exist) {
				jsonValidateReturn[1] = true;
				jsonValidateReturn[2] = "可以使用!";
			} else {
				jsonValidateReturn[2] = "栏目代码" + StringUtils.trim(fieldValue)
						+ "已使用!";
			}
		}
		return jsonValidateReturn;
	}

	private boolean checkCode(Long id, String appCode, String code) {
		boolean exist = false;
		Category category = null;
		if (StringUtils.isNotEmpty(code)) {
			category = categoryRepository
					.findOneByAppCodeAndCode(appCode, code);
		}
		if (category != null) {
			if (id == null || id == -1 || category.getId().longValue() != id) {
				exist = true;
			}
		}
		return exist;
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "栏目管理", action = "上下线", message = "上下线栏目")
	@RequestMapping(value = { "{id}/status" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult status(@PathVariable("id") Long id) {
		Category categoryInfo = categoryRepository.findOne(id);
		if (categoryInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			categoryInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
		} else {
			categoryInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
		}
		if (categoryInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			categoryInfo.setOnlineTime(new Date());
			categoryInfo.setOfflineTime(null);
		} else if (categoryInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			categoryInfo.setOfflineTime(new Date());
		}
		categoryRepository.save(categoryInfo);
		AdminGlobal.operationLogTypeAndId.set(ItemTypeEnum.CATEGORY.getKey()+"$"+categoryInfo.getId());
		if (categoryInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			AdminGlobal.operationLogAction.set("上线");
			AdminGlobal.operationLogMessage.set("上线栏目");
		} else if (categoryInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			AdminGlobal.operationLogAction.set("下线");
			AdminGlobal.operationLogMessage.set("下线栏目");
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "batchToCategory" }, method = RequestMethod.GET)
	public String toBatchToCategory(Model model, HttpServletRequest request,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		setModel(model, request);

		return "category/category/batchToCategory";
	}

	@OperationLogAnnotation(module = "内容管理", subModule = "节目/剧头管理", action = "批量编排", message = "批量编排到栏目")
	@RequestMapping(value = { "batchToCategory" }, method = { RequestMethod.POST }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchToCategory(
			@RequestParam(value = "selectCategoryId") Long selectCategoryId,
			@RequestParam(value = "status") Integer status,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds,
			@RequestParam(value = "sortIndex") Integer sortIndex) {
		if (selectCategoryId == null || itemType == null
				|| StringUtils.isEmpty(itemIds)) {
			return new BaseResult();
		}
		categoryService.batchUpdate("" + selectCategoryId, status, itemType,
				itemIds, sortIndex);

		String typeAndIdStr = "";
		for (String s : itemIds.split(",")) {
			typeAndIdStr += (itemType+"$"+s+",");
		}
		AdminGlobal.operationLogTypeAndId.set(typeAndIdStr);
		Category category = categoryRepository.findOne(Long.valueOf(selectCategoryId));
		AdminGlobal.operationLogMessage.set("编排到栏目: <br/>" + category.getAppCode() + "  ==>  " + category.getTitle());
		return new BaseResult();
	}

	@RequestMapping(value = { "batchInjection" }, method = RequestMethod.GET)
	public String tobatchInjection(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);
		
		List<InjectionPlatform> injectionPlatformList = injectionPlatformRepository.findAll();
		model.addAttribute("injectionPlatformList", injectionPlatformList);

		return "category/category/batchInjection";
	}

	@RequestMapping(value = { "batchInjection" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchInjection(@RequestBody BatchInjectionBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		Date currentTime = new Date();
		String[] itemIdArr = itemIds.split(",");
		if (null != itemIdArr) {
			for (String itemIdStr : itemIdArr) {
				Long itemId = Long.valueOf(itemIdStr);
				Category category = categoryRepository.findOne(itemId);
//				if (category != null) {
//					injectionService.inInjection(category, batchBean.getPlatformId(),
//							batchBean.getPriority(), currentTime, currentTime,
//							true);
//				}
			}
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "batchOutInjection" }, method = RequestMethod.GET)
	public String batchOutInjection(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		return "category/category/batchOutInjection";
	}

	@RequestMapping(value = { "batchOutInjection" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchOutInjection(@RequestBody BatchInjectionBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		Date currentTime = new Date();
		String[] itemIdArr = itemIds.split(",");
		if (null != itemIdArr) {
			for (String itemIdStr : itemIdArr) {
				Long itemId = Long.valueOf(itemIdStr);
				Category category = categoryRepository.findOne(itemId);
//				if (category != null) {
//					injectionService.outInjection(category, category.getPlatformId(),
//							batchBean.getPriority(), currentTime, currentTime);
//				}
			}
		}
		return new BaseResult();
	}
	
	@RequestMapping(value = { "resetInjectionStatus" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult resetInjectionStatus(
			@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String[] itemIdArr = itemIds.split(",");
		if (null != itemIdArr) {
			for (String itemIdStr : itemIdArr) {
				Long itemId = Long.valueOf(itemIdStr);
				Category category = categoryRepository.findOne(itemId);
				if (category != null) {
					category.setInjectionStatus(InjectionStatusEnum.DEFAULT
							.getKey());
					categoryRepository.save(category);
				}
			}
		}
		return new BaseResult();
	}

}
