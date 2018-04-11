package com.ai.epg.category.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.enums.InjectionStatusEnum;
import com.ai.cms.injection.repository.InjectionPlatformRepository;
import com.ai.cms.injection.service.InjectionService;
import com.ai.cms.media.bean.BatchAddBean;
import com.ai.cms.media.bean.BatchBean;
import com.ai.cms.media.bean.BatchInjectionBean;
import com.ai.cms.media.bean.BatchStatusBean;
import com.ai.cms.media.bean.BatchTitleBean;
import com.ai.cms.media.bean.ImageBean;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.bean.ResultCode;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.CategoryItemTypeEnum;
import com.ai.common.enums.ItemTypeEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.exception.ServiceException;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;
import com.ai.epg.category.entity.Category;
import com.ai.epg.category.entity.CategoryItem;
import com.ai.epg.category.entity.CategoryItemView;
import com.ai.epg.category.repository.CategoryItemRepository;
import com.ai.epg.category.repository.CategoryItemViewRepository;
import com.ai.epg.category.repository.CategoryRepository;
import com.ai.epg.category.service.CategoryService;
import com.ai.epg.subscriber.entity.SubscriberGroup;
import com.ai.epg.subscriber.repository.SubscriberGroupRepository;

import org.apache.commons.lang3.StringUtils;
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

import com.ai.AdminConstants;
import com.ai.AdminGlobal;

@Controller
@RequestMapping(value = { "/category/{categoryId}/categoryItem" })
public class CategoryItemController extends AbstractImageController {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryItemRepository categoryItemRepository;

	@Autowired
	private CategoryItemViewRepository categoryItemViewRepository;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private InjectionPlatformRepository injectionPlatformRepository;
	@Autowired
	private InjectionService injectionService;

	@Autowired
	private SubscriberGroupRepository subscriberGroupRepository;

	private void setModel(Model model) {
		model.addAttribute("typeEnum", CategoryItemTypeEnum.values());
		model.addAttribute("statusEnum", OnlineStatusEnum.values());
		model.addAttribute("injectionStatusEnum", InjectionStatusEnum.values());
		
		List<SubscriberGroup> subscriberGroupList = subscriberGroupRepository
				.findAll();
		model.addAttribute("subscriberGroupList", subscriberGroupList);
	}

	@RequestMapping(value = { "" })
	public String list(@PathVariable("categoryId") Long categoryId,
			Model model, HttpServletRequest request, PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("sortIndex");
		}
		
		categoryItemRepository.updateSortIndex("SortIndex");
		categoryItemRepository.updateSeqSortIndexByCategoryId(categoryId);

		Category category = categoryRepository.findOne(categoryId);
		model.addAttribute("category", category);

		Page<CategoryItemView> page = find(request, pageInfo,
				categoryItemViewRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "category/categoryItem/list";
	}
	
	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model, @PathVariable("categoryId") Long categoryId) {
		CategoryItemView categoryItemView = new CategoryItemView();
		model.addAttribute("categoryItem", categoryItemView);

		Category category = categoryRepository.findOne(categoryId);
		model.addAttribute("category", category);

		setModel(model);

		return "category/categoryItem/edit";
	}

	@OperationLogAnnotation(module = "栏目管理", subModule = "栏目项管理", action = "增加", message = "增加栏目项")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@PathVariable("categoryId") Long categoryId,
			@RequestBody ImageBean<CategoryItem> imageBean) {
		return edit(categoryId, imageBean, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model,
			@PathVariable("categoryId") Long categoryId,
			@PathVariable("id") Long id) {
		CategoryItemView categoryItemView = categoryItemViewRepository
				.findOne(id);
		model.addAttribute("categoryItem", categoryItemView);

		Category category = categoryRepository.findOne(categoryId);
		model.addAttribute("category", category);

		setModel(model);

		return "category/categoryItem/edit";
	}

	@OperationLogAnnotation(module = "栏目管理", subModule = "栏目项管理", action = "修改", message = "修改栏目项")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@PathVariable("categoryId") Long categoryId,
			@RequestBody ImageBean<CategoryItem> imageBean,
			@PathVariable("id") Long id) {
		CategoryItem categoryItem = imageBean.getData();
		String image1Data = imageBean.getImage1Data();
		String image2Data = imageBean.getImage2Data();

		CategoryItem categoryItemInfo = null;
		String image1Old = "";
		String image2Old = "";
		if (id == null) {
			categoryItemInfo = categoryItem;
			categoryItemInfo.setSiteCode(AdminGlobal.getSiteCode());
			if (categoryItem.getItemType() != ItemTypeEnum.URI.getKey()) {
				CategoryItem existCategoryItem = categoryService.getCategoryItem(
						categoryId, categoryItem.getItemType(),
						categoryItem.getItemId());
				if (existCategoryItem != null) {
					throw new ServiceException("该元素已存在！");
				}
			}
		} else {
			categoryItemInfo = categoryItemRepository.findOne(id);
			if (categoryItem != null && !categoryItemInfo.getTitle().equals(categoryItem.getTitle())) {
				AdminGlobal.operationLogMessage.set("栏目项修改标题：<br/>" + categoryItemInfo.getTitle() + "  ==>  " + categoryItem.getTitle());
			} else {
				AdminGlobal.operationLogAction.set("增加");
				AdminGlobal.operationLogMessage.set("增加栏目项");
			}
			image1Old = categoryItemInfo.getImage1();
			image2Old = categoryItemInfo.getImage2();
			BeanInfoUtil.bean2bean(categoryItem, categoryItemInfo,
					"title,image1,image2,sortIndex,status,itemStatus,groupCodes,validTime,expiredTime");
		}

		String image1 = "";
		String image2 = "";
		if (StringUtils.isNotEmpty(image1Data)) {
			image1 = upload(AdminConstants.MODULE_RESOURCE_CATEGORY,
					AdminConstants.RESOURCE_TYPE_POSTER, image1Data);
			categoryItemInfo.setImage1(image1);
		}
		if (StringUtils.isNotEmpty(image2Data)) {
			image2 = upload(AdminConstants.MODULE_RESOURCE_CATEGORY,
					AdminConstants.RESOURCE_TYPE_POSTER, image2Data);
			categoryItemInfo.setImage2(image2);
		}
		categoryService.saveCategoryItem(categoryItemInfo);

		// Date currentTime = new Date();
		// if (categoryItemInfo.getInjectionStatus() ==
		// InjectionStatusEnum.INJECTED
		// .getKey()) {
		// injectionService.inInjection(
		// categoryItemViewRepository.findOne(id), 1, currentTime,
		// currentTime);
		// }

		if (!StringUtils.trimToEmpty(image1Old).equals(
				StringUtils.trimToEmpty(categoryItem.getImage1()))) {
			// deleteOldResource(image1Old);
		}

		if (!StringUtils.trimToEmpty(image2Old).equals(
				StringUtils.trimToEmpty(categoryItem.getImage2()))) {
			// deleteOldResource(image2Old);
		}
		AdminGlobal.operationLogTypeAndId.set(categoryItem.getItemType()+"$"+categoryItemInfo.getItemId());
		AdminGlobal.operationLogMessage.set("修改栏目项");
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "栏目管理", subModule = "栏目项管理", action = "删除", message = "删除栏目项")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("categoryId") Long categoryId,
			@PathVariable("id") Long id) {
		CategoryItem categoryItemInfo = categoryItemRepository.findOne(id);
		if (categoryItemInfo != null) {
			// String image1Old = categoryItemInfo.getImage1();
			// String image2Old = categoryItemInfo.getImage2();
//			if (categoryItemInfo != null) {
//				Date currentTime = new Date();
//				injectionService.outInjection(categoryItemInfo, categoryItemInfo.getPlatformId(), 1,
//						currentTime, currentTime);
//			}
			categoryItemRepository.delete(categoryItemInfo);

			// deleteOldResource(image1Old);
			// deleteOldResource(image2Old);
			AdminGlobal.operationLogTypeAndId.set(categoryItemInfo.getItemType()+"$"+categoryItemInfo.getItemId());
			AdminGlobal.operationLogMessage.set("删除栏目项");
		}
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "栏目管理", subModule = "栏目项管理", action = "上下线", message = "上下线栏目项")
	@RequestMapping(value = { "{id}/status" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult status(@PathVariable("id") Long id) {
		CategoryItem categoryItemInfo = categoryItemRepository.findOne(id);
		if (categoryItemInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			categoryItemInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
		} else {
			categoryItemInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
		}
		categoryItemRepository.save(categoryItemInfo);
		// Date currentTime = new Date();
		// if (categoryItemInfo.getInjectionStatus() ==
		// InjectionStatusEnum.INJECTED
		// .getKey()) {
		// injectionService.inInjection(categoryItemViewRepository
		// .findOne(categoryItemInfo.getId()), 1, currentTime,
		// currentTime);
		// }
		AdminGlobal.operationLogTypeAndId.set(categoryItemInfo.getItemType()+"$"+categoryItemInfo.getItemId());

		if (categoryItemInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			AdminGlobal.operationLogAction.set("上线");
			AdminGlobal.operationLogMessage.set("上线栏目项");
		} else if (categoryItemInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			AdminGlobal.operationLogAction.set("下线");
			AdminGlobal.operationLogMessage.set("下线栏目项");
		}
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "栏目管理", subModule = "栏目项管理", action = "批量修改", message = "批量上下线推荐位项")
	@RequestMapping(value = { "batchChangeStatus" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchChangeStatus(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		// Date currentTime = new Date();
		String[] itemIdArr = itemIds.split(",");
		String typeAndIdStr = "";
		if (null != itemIdArr) {
			for (String itemIdStr : itemIdArr) {
				Long itemId = Long.valueOf(itemIdStr);
				CategoryItem categoryItemInfo = categoryItemRepository
						.findOne(itemId);
				categoryItemInfo.setStatus(batchBean.getStatus());
				categoryItemRepository.save(categoryItemInfo);

				// if (categoryItemInfo.getInjectionStatus() ==
				// InjectionStatusEnum.INJECTED
				// .getKey()) {
				// injectionService.inInjection(categoryItemViewRepository
				// .findOne(categoryItemInfo.getId()), 1, currentTime,
				// currentTime);
				// }
				typeAndIdStr += (categoryItemInfo.getItemType()+"$"+categoryItemInfo.getItemId()+",");
			}
			AdminGlobal.operationLogTypeAndId.set(typeAndIdStr);
			if (batchBean.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
				AdminGlobal.operationLogAction.set("批量上线");
				AdminGlobal.operationLogMessage.set("批量上线栏目项");
			} else if (batchBean.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
				AdminGlobal.operationLogAction.set("批量下线");
				AdminGlobal.operationLogMessage.set("批量下线栏目项");
			}
		}
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "栏目管理", subModule = "栏目项管理", action = "批量修改", message = "批量删除栏目项")
	@RequestMapping(value = { "batchDelete" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchDelete(@RequestBody BatchBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		Date currentTime = new Date();
		String[] itemIdArr = itemIds.split(",");
		String typeAndIdStr = "";
		if (null != itemIdArr) {
			for (String itemIdStr : itemIdArr) {
				Long itemId = Long.valueOf(itemIdStr);
				CategoryItem categoryItemInfo = categoryItemRepository
						.findOne(itemId);
				if (categoryItemInfo != null
						&& categoryItemInfo.getStatus() == OnlineStatusEnum.OFFLINE
								.getKey()) {

//					injectionService.outInjection(categoryItemInfo, categoryItemInfo.getPlatformId(), 1, currentTime,
//							currentTime);

					categoryItemRepository.delete(categoryItemInfo);
					typeAndIdStr += (categoryItemInfo.getItemType()+"$"+categoryItemInfo.getItemId()+",");
				}
			}
			AdminGlobal.operationLogTypeAndId.set(typeAndIdStr);
			AdminGlobal.operationLogMessage.set("删除栏目项");
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "batchChangeTitle" }, method = RequestMethod.GET)
	public String batchChangeTitle(Model model,
			@PathVariable("categoryId") Long categoryId,
			@RequestParam(value = "itemIds") String itemIds) {
		Category category = categoryRepository.findOne(categoryId);
		model.addAttribute("category", category);

		List<Long> idList = new ArrayList<Long>();

		String[] itemIdArr = itemIds.split(",");
		if (null != itemIdArr) {
			for (String itemIdStr : itemIdArr) {
				Long itemId = Long.valueOf(itemIdStr);
				idList.add(itemId);
			}
		}

		if (idList.size() > 0) {
			List<CategoryItemView> categoryItemViewList = categoryItemViewRepository
					.findByIdIn(idList);

			List<CategoryItemView> categoryItemList = new ArrayList<CategoryItemView>();
			for (Long id : idList) {
				for (CategoryItemView categoryItemView : categoryItemViewList) {
					if (id.longValue() == categoryItemView.getId().longValue()) {
						categoryItemList.add(categoryItemView);
						break;
					}
				}
			}
			model.addAttribute("categoryItemList", categoryItemList);
		}

		setModel(model);

		return "category/categoryItem/batchChangeTitle";
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "栏目管理", action = "批量修改", message = "批量修改栏目项定制名称/排序值")
	@RequestMapping(value = { "batchChangeTitle" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchChangeTitle(@RequestBody BatchTitleBean batchBean) {
		// Date currentTime = new Date();
		Long[] ids = batchBean.getIds();
		String typeAndIdStr = "";
		if (null != ids) {
			for (int i = 0; i < ids.length; i++) {
				CategoryItem categoryItemInfo = categoryItemRepository
						.findOne(ids[i]);
				if (categoryItemInfo != null) {
					categoryItemInfo.setTitle(batchBean.getTitles()[i]);
					categoryItemInfo.setSortIndex(batchBean.getSortIndexs()[i]);
					categoryItemRepository.save(categoryItemInfo);

					if (categoryItemInfo.getInjectionStatus() == InjectionStatusEnum.INJECTION_SUCCESS
							.getKey()) {
						// injectionService.inInjection(categoryItemViewRepository
						// .findOne(categoryItemInfo.getId()), 1,
						// currentTime, currentTime);
					}
					typeAndIdStr += (categoryItemInfo.getItemType()+"$"+categoryItemInfo.getItemId()+",");
				}
			}
			AdminGlobal.operationLogTypeAndId.set(typeAndIdStr);
			AdminGlobal.operationLogMessage.set("批量修改栏目项定制名称/排序值");
		}
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

		return "category/categoryItem/batchInjection";
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
				CategoryItem categoryItemInfo = categoryItemRepository
						.findOne(itemId);
//				if (categoryItemInfo != null) {
//					injectionService.inInjection(categoryItemInfo, batchBean.getPlatformId(),
//							batchBean.getPriority(), currentTime, currentTime);
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
		
		return "category/categoryItem/batchOutInjection";
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
				CategoryItem categoryItemInfo = categoryItemRepository
						.findOne(itemId);
//				if (categoryItemInfo != null) {
//					injectionService.outInjection(categoryItemInfo, categoryItemInfo.getPlatformId(),
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
				CategoryItem categoryItem = categoryItemRepository
						.findOne(itemId);
				if (categoryItem != null) {
					categoryItem.setInjectionStatus(InjectionStatusEnum.DEFAULT
							.getKey());
					categoryItemRepository.save(categoryItem);
				}
			}
		}
		return new BaseResult();
	}
	
	@RequestMapping(value = { "batchAdd" }, method = RequestMethod.GET)
	public String toBatchAdd(Model model,
			@PathVariable("categoryId") Long categoryId) {
		Category category = categoryRepository.findOne(categoryId);
		model.addAttribute("category", category);

		setModel(model);

		return "category/categoryItem/batchAdd";
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "栏目管理", action = "批量增加栏目项", message = "批量增加推荐位项")
	@RequestMapping(value = { "batchAdd" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchAdd(@PathVariable("categoryId") Long categoryId,
			@RequestBody BatchAddBean batchAddBean) {
		String typeAndIdStr = "";
		if (batchAddBean.getItemId() != null
				&& batchAddBean.getItemId().length > 0) {
			int minSortIndex = 999;
			int maxSortIndex = -1;
			for (int i = 0; i < batchAddBean.getItemId().length; i++) {
				if (batchAddBean.getSortIndex()[i] != null
						&& batchAddBean.getSortIndex()[i] < minSortIndex) {
					minSortIndex = batchAddBean.getSortIndex()[i];
				}
				if (batchAddBean.getSortIndex()[i] != null
						&& batchAddBean.getSortIndex()[i] > maxSortIndex) {
					maxSortIndex = batchAddBean.getSortIndex()[i];
				}
			}
			if (minSortIndex > 0 && maxSortIndex > 0) {
				Integer step = maxSortIndex - minSortIndex + 1;
				categoryService.batchUpdateMoveByAfter(categoryId, step,
						minSortIndex);
			}
			for (int i = 0; i < batchAddBean.getItemId().length; i++) {
				CategoryItem categoryItem = categoryService.getCategoryItem(categoryId,
						batchAddBean.getItemType()[i],
						batchAddBean.getItemId()[i]);
				if (categoryItem == null) {
					categoryItem = new CategoryItem();
					categoryItem.setCategoryId(categoryId);
					categoryItem.setItemType(batchAddBean.getItemType()[i]);
					categoryItem.setItemId(batchAddBean.getItemId()[i]);
					categoryItem.setSiteCode(AdminGlobal.getSiteCode());
				}
				categoryItem.setTitle(batchAddBean.getTitle()[i]);
				categoryItem.setStatus(batchAddBean.getStatus()[i]);
				categoryItem.setItemStatus(batchAddBean.getItemStatus()[i]);
				categoryItem.setSortIndex(batchAddBean.getSortIndex()[i]);
				categoryItemRepository.save(categoryItem);
				typeAndIdStr += (categoryItem.getItemType()+"$"+categoryItem.getItemId()+",");
			}
			AdminGlobal.operationLogTypeAndId.set(typeAndIdStr);
			AdminGlobal.operationLogMessage.set("批量增加栏目项定制名称/排序值");
		}
		return new BaseResult();
	}
}
