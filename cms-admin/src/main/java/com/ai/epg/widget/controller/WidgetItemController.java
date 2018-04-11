package com.ai.epg.widget.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ai.cms.media.bean.BatchAddBean;
import com.ai.cms.media.bean.BatchBean;
import com.ai.cms.media.bean.BatchStatusBean;
import com.ai.cms.media.bean.BatchTitleBean;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.Series;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.cms.media.repository.SeriesRepository;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.bean.ResultCode;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.*;
import com.ai.common.exception.ServiceException;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;
import com.ai.epg.album.entity.Album;
import com.ai.epg.album.repository.AlbumRepository;
import com.ai.epg.category.repository.CategoryRepository;
import com.ai.epg.subscriber.entity.SubscriberGroup;
import com.ai.epg.subscriber.repository.SubscriberGroupRepository;
import com.ai.epg.uri.bean.UriImageBean;
import com.ai.epg.uri.bean.UriParamBean;
import com.ai.epg.uri.entity.Uri;
import com.ai.epg.uri.repository.UriRepository;
import com.ai.epg.uri.service.UriService;
import com.ai.epg.widget.entity.Widget;
import com.ai.epg.widget.entity.WidgetItem;
import com.ai.epg.widget.entity.WidgetItemView;
import com.ai.epg.widget.repository.WidgetItemRepository;
import com.ai.epg.widget.repository.WidgetItemViewRepository;
import com.ai.epg.widget.repository.WidgetRepository;
import com.ai.epg.widget.service.WidgetService;

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
@RequestMapping(value = { "/widget/{widgetId}/widgetItem" })
public class WidgetItemController extends AbstractImageController {

	@Autowired
	private WidgetRepository widgetRepository;

	@Autowired
	private WidgetItemRepository widgetItemRepository;

	@Autowired
	private WidgetItemViewRepository widgetItemViewRepository;

	@Autowired
	private WidgetService widgetService;

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private SeriesRepository seriesRepository;

	@Autowired
	private AlbumRepository albumRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private UriRepository uriRepository;

	@Autowired
	private UriService uriService;
	
	@Autowired
	private SubscriberGroupRepository subscriberGroupRepository;

	private void setModel(Model model) {
		model.addAttribute("typeEnum", WidgetItemTypeEnum.values());
		model.addAttribute("statusEnum", OnlineStatusEnum.values());

		model.addAttribute("jumpTypeEnum", WidgetJumpTypeEnum.values());
		
		model.addAttribute("widgetJumpItemTypeEnum", WidgetJumpItemTypeEnum.values());
		model.addAttribute("templateParamCategoryEnum",
				TemplateParamCategoryEnum.values());

		List<SubscriberGroup> subscriberGroupList = subscriberGroupRepository
				.findAll();
		model.addAttribute("subscriberGroupList", subscriberGroupList);
	}

	@RequestMapping(value = { "" })
	public String list(@PathVariable("widgetId") Long widgetId, Model model,
			HttpServletRequest request, PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("sortIndex");
		}

		Widget widget = widgetRepository.findOne(widgetId);
		model.addAttribute("widget", widget);

		Page<WidgetItemView> page = find(request, pageInfo,
				widgetItemViewRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "widget/widgetItem/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model, @PathVariable("widgetId") Long widgetId) {
		WidgetItemView widgetItemView = new WidgetItemView();
		model.addAttribute("widgetItemView", widgetItemView);

		WidgetItem widgetItem = new WidgetItem();
		model.addAttribute("widgetItem", widgetItem);

		Widget widget = widgetRepository.findOne(widgetId);
		model.addAttribute("widget", widget);

		setModel(model);

		return "widget/widgetItem/edit";
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "推荐位项管理", action = "增加", message = "增加推荐位项")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(HttpServletRequest request, @PathVariable("widgetId") Long widgetId,
			@RequestBody UriImageBean<WidgetItem> imageBean) {
		return edit(request, widgetId, imageBean, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("widgetId") Long widgetId,
			@PathVariable("id") Long id) {
		WidgetItemView widgetItemView = widgetItemViewRepository.findOne(id);
		model.addAttribute("widgetItemView", widgetItemView);

		WidgetItem widgetItem = widgetItemRepository.findOne(id);
		model.addAttribute("widgetItem", widgetItem);
		if (widgetItem.getJumpItemType() != null
				&& widgetItem.getJumpItemId() != null) {
			if (widgetItem.getJumpItemType() == WidgetJumpItemTypeEnum.PROGRAM
					.getKey()) {
				Program program = programRepository.findOne(widgetItem
						.getJumpItemId());
				if (program != null) {
					model.addAttribute("jumpItemName", program.getName());
				}
			} else if (widgetItem.getJumpItemType() == WidgetJumpItemTypeEnum.SERIES
					.getKey()) {
				Series series = seriesRepository.findOne(widgetItem
						.getJumpItemId());
				if (series != null) {
					model.addAttribute("jumpItemName", series.getName());
				}
			} else if (widgetItem.getJumpItemType() == WidgetJumpItemTypeEnum.ALBUM
					.getKey()) {
				Album album = albumRepository.findOne(widgetItem
						.getJumpItemId());
				if (album != null) {
					model.addAttribute("jumpItemName", album.getName());
				}
			} else if (widgetItem.getJumpItemType() == WidgetJumpItemTypeEnum.URI
					.getKey()) {
				Uri uri = uriRepository.findOne(widgetItem.getJumpItemId());
				if (uri != null) {
					model.addAttribute("jumpItemName", uri.getName());
				}
			}
		}
		Widget widget = widgetRepository.findOne(widgetId);
		model.addAttribute("widget", widget);

		setModel(model);

		return "widget/widgetItem/edit";
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "推荐位项管理", action = "修改", message = "修改推荐位")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(HttpServletRequest request, @PathVariable("widgetId") Long widgetId,
			@RequestBody UriImageBean<WidgetItem> imageBean,
			@PathVariable("id") Long id) {
		WidgetItem widgetItem = imageBean.getData();
		String image1Data = imageBean.getImage1Data();
		String image2Data = imageBean.getImage2Data();
		String backgroundImageData = imageBean.getBackgroundImageData();

		WidgetItem widgetItemInfo = null;
		String image1Old = "";
		String image2Old = "";
		String backgroundImageOld = "";
		if (id == null) {
			widgetItemInfo = widgetItem;
			widgetItemInfo.setSiteCode(AdminGlobal.getSiteCode());
			if (widgetItem.getItemType() != WidgetItemTypeEnum.URI.getKey()) {
				WidgetItem existWidgetItem = widgetService.getWidgetItem(widgetId,
						widgetItem.getItemType(), widgetItem.getItemId());
				if (existWidgetItem != null) {
					throw new ServiceException("该元素已存在！");
				}
			}
		} else {
			widgetItemInfo = widgetItemRepository.findOne(id);
			if (widgetItem != null && !widgetItemInfo.getTitle().equals(widgetItem.getTitle())) {
				AdminGlobal.operationLogMessage.set("推荐位项修改标题：<br/>" + widgetItemInfo.getTitle() + "  ==>  " + widgetItem.getTitle());
			} else {
				AdminGlobal.operationLogAction.set("增加");
				AdminGlobal.operationLogMessage.set("增加推荐位项");
			}
			image1Old = widgetItemInfo.getImage1();
			image2Old = widgetItemInfo.getImage2();
			backgroundImageOld = widgetItemInfo.getBackgroundImage();
			BeanInfoUtil.bean2bean(widgetItem, widgetItemInfo,
					"title,image1,image2,sortIndex,status,jumpItemType,jumpItemId,jumpType,url,jumpParam1,jumpParam2,"
					+ "backgroundImage,internalParamCategory,groupCodes,validTime,expiredTime");
		}

		String image1 = "";
		String image2 = "";
		String backgroundImage = "";
		if (StringUtils.isNotEmpty(image1Data)) {
			image1 = upload(AdminConstants.MODULE_RESOURCE_WIDGET,
					AdminConstants.RESOURCE_TYPE_POSTER, image1Data);
			widgetItemInfo.setImage1(image1);
		}
		if (StringUtils.isNotEmpty(image2Data)) {
			image2 = upload(AdminConstants.MODULE_RESOURCE_WIDGET,
					AdminConstants.RESOURCE_TYPE_POSTER, image2Data);
			widgetItemInfo.setImage2(image2);
		}
		if (StringUtils.isNotEmpty(backgroundImageData)) {
			backgroundImage = upload(AdminConstants.MODULE_RESOURCE_WIDGET,
					AdminConstants.RESOURCE_TYPE_BACKGROUND,
					backgroundImageData);
			widgetItemInfo.setBackgroundImage(backgroundImage);
		}
		
		if (widgetItemInfo.getItemType() == WidgetItemTypeEnum.URI.getKey()
				|| (widgetItemInfo.getJumpItemType() != null && widgetItemInfo
						.getJumpItemType() == WidgetJumpItemTypeEnum.URI
						.getKey())) {
			List<UriParamBean> params = imageBean.getParams();
			Long uriId = null;
			if (widgetItemInfo.getJumpItemType() == WidgetJumpItemTypeEnum.URI
					.getKey()) {
				uriId = widgetItemInfo.getJumpItemId();
			} else {
				uriId = widgetItemInfo.getItemId();
			}
			String appCode = (String) request.getSession().getAttribute(
					"currentAppCode");
			uriService.saveWidgetAndCategory(uriId, params, appCode);
			String internalParam = uriService.getInternalParam(uriId, params);
			widgetItemInfo.setInternalParam(internalParam);
		}
		
		try {
			widgetService.saveWidgetItem(widgetItemInfo);
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			throw new ServiceException("该元素已存在！");
		}

		if (!StringUtils.trimToEmpty(image1Old).equals(
				StringUtils.trimToEmpty(widgetItem.getImage1()))) {
			// deleteOldResource(image1Old);
		}

		if (!StringUtils.trimToEmpty(image2Old).equals(
				StringUtils.trimToEmpty(widgetItem.getImage2()))) {
			// deleteOldResource(image2Old);
		}
		if (!StringUtils.trimToEmpty(backgroundImageOld).equals(
				StringUtils.trimToEmpty(widgetItem.getBackgroundImage()))) {
			deleteOldResource(backgroundImageOld);
		}
		AdminGlobal.operationLogTypeAndId.set(widgetItem.getItemType()+"$"+widgetItemInfo.getItemId());
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "推荐位管理", action = "删除", message = "删除推荐位项")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("widgetId") Long widgetId,
			@PathVariable("id") Long id) {
		WidgetItem widgetItemInfo = widgetItemRepository.findOne(id);
		if (widgetItemInfo != null) {
			String image1Old = widgetItemInfo.getImage1();
			String image2Old = widgetItemInfo.getImage2();
			String backgroundImageOld = widgetItemInfo.getBackgroundImage();
			widgetItemRepository.delete(widgetItemInfo);

			deleteOldResource(image1Old);
			deleteOldResource(image2Old);
			deleteOldResource(backgroundImageOld);
			AdminGlobal.operationLogTypeAndId.set(widgetItemInfo.getItemType()+"$"+widgetItemInfo.getItemId());
			AdminGlobal.operationLogMessage.set("删除推荐位项");
		}
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "推荐位管理", action = "上下线", message = "上下线推荐位项")
	@RequestMapping(value = { "{id}/status" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult status(@PathVariable("id") Long id) {
		WidgetItem widgetItemInfo = widgetItemRepository.findOne(id);
		if (widgetItemInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			widgetItemInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
		} else {
			widgetItemInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
		}
		widgetItemRepository.save(widgetItemInfo);
		AdminGlobal.operationLogTypeAndId.set(widgetItemInfo.getItemType()+"$"+widgetItemInfo.getItemId());
		if (widgetItemInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			AdminGlobal.operationLogAction.set("上线");
			AdminGlobal.operationLogMessage.set("上线推荐位项");
		} else if (widgetItemInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			AdminGlobal.operationLogAction.set("下线");
			AdminGlobal.operationLogMessage.set("下线推荐位项");
		}
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "推荐位管理", action = "批量修改", message = "批量上下线推荐位项")
	@RequestMapping(value = { "batchChangeStatus" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchChangeStatus(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String[] itemIdArr = itemIds.split(",");
		String typeAndIdStr = "";
		if (null != itemIdArr) {
			for (String itemIdStr : itemIdArr) {
				Long itemId = Long.valueOf(itemIdStr);
				WidgetItem widgetItemInfo = widgetItemRepository
						.findOne(itemId);
				widgetItemInfo.setStatus(batchBean.getStatus());
				widgetItemRepository.save(widgetItemInfo);

				typeAndIdStr += (widgetItemInfo.getItemType()+"$"+widgetItemInfo.getItemId()+",");
			}
			AdminGlobal.operationLogTypeAndId.set(typeAndIdStr);
			if (batchBean.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
				AdminGlobal.operationLogAction.set("批量上线");
				AdminGlobal.operationLogMessage.set("上线推荐位");
			} else if (batchBean.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
				AdminGlobal.operationLogAction.set("批量下线");
				AdminGlobal.operationLogMessage.set("下线推荐位");
			}
		}
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "推荐位管理", action = "批量删除", message = "批量删除推荐位项")
	@RequestMapping(value = { "batchDelete" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchDelete(@RequestBody BatchBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String[] itemIdArr = itemIds.split(",");
		String typeAndIdStr = "";
		if (null != itemIdArr) {
			for (String itemIdStr : itemIdArr) {
				Long itemId = Long.valueOf(itemIdStr);
				WidgetItem widgetItemInfo = widgetItemRepository
						.findOne(itemId);
				if (widgetItemInfo != null
						&& widgetItemInfo.getStatus() == OnlineStatusEnum.OFFLINE
								.getKey()) {
					widgetItemRepository.delete(widgetItemInfo);
				}
				typeAndIdStr += (widgetItemInfo.getItemType()+"$"+widgetItemInfo.getItemId()+",");
			}
			if (StringUtils.isEmpty(typeAndIdStr)) {
				AdminGlobal.operationLogActionResult.set("0");
			}
			AdminGlobal.operationLogTypeAndId.set(typeAndIdStr);
			AdminGlobal.operationLogMessage.set("删除推荐位项");
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "batchChangeTitle" }, method = RequestMethod.GET)
	public String batchChangeTitle(Model model,
			@PathVariable("widgetId") Long widgetId,
			@RequestParam(value = "itemIds") String itemIds) {
		Widget widget = widgetRepository.findOne(widgetId);
		model.addAttribute("widget", widget);

		List<Long> idList = new ArrayList<Long>();

		String[] itemIdArr = itemIds.split(",");
		if (null != itemIdArr) {
			for (String itemIdStr : itemIdArr) {
				Long itemId = Long.valueOf(itemIdStr);
				idList.add(itemId);
			}
		}

		if (idList.size() > 0) {
			List<WidgetItemView> widgetItemViewList = widgetItemViewRepository
					.findByIdIn(idList);

			List<WidgetItemView> widgetItemList = new ArrayList<WidgetItemView>();
			for (Long id : idList) {
				for (WidgetItemView widgetItemView : widgetItemViewList) {
					if (id.longValue() == widgetItemView.getId().longValue()) {
						widgetItemList.add(widgetItemView);
						break;
					}
				}
			}
			model.addAttribute("widgetItemList", widgetItemList);
		}

		setModel(model);

		return "widget/widgetItem/batchChangeTitle";
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "推荐位管理", action = "批量修改", message = "批量修改推荐位项定制名称/排序值")
	@RequestMapping(value = { "batchChangeTitle" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchChangeTitle(@RequestBody BatchTitleBean batchBean) {
		Long[] ids = batchBean.getIds();
		String typeAndIdStr = "";
		if (null != ids) {
			for (int i = 0; i < ids.length; i++) {
				WidgetItem widgetItem = widgetItemRepository.findOne(ids[i]);
				if (widgetItem != null) {
					widgetItem.setTitle(batchBean.getTitles()[i]);
					widgetItem.setSortIndex(batchBean.getSortIndexs()[i]);
					widgetItemRepository.save(widgetItem);
					typeAndIdStr += (widgetItem.getItemType()+"$"+widgetItem.getItemId()+",");
				}
			}
			AdminGlobal.operationLogTypeAndId.set(typeAndIdStr);
			AdminGlobal.operationLogMessage.set("修改推荐位项定制名称/排序值");
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "batchAdd" }, method = RequestMethod.GET)
	public String toBatchAdd(Model model,
			@PathVariable("widgetId") Long widgetId) {
		Widget widget = widgetRepository.findOne(widgetId);
		model.addAttribute("widget", widget);

		setModel(model);

		return "widget/widgetItem/batchAdd";
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "推荐位管理", action = "批量增加推荐位项", message = "批量增加推荐位项")
	@RequestMapping(value = { "batchAdd" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchAdd(@PathVariable("widgetId") Long widgetId,
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
				widgetService.batchUpdateMoveByAfter(widgetId, step,
						minSortIndex);
			}
			for (int i = 0; i < batchAddBean.getItemId().length; i++) {
				WidgetItem widgetItem = widgetService.getWidgetItem(widgetId,
						batchAddBean.getItemType()[i],
						batchAddBean.getItemId()[i]);
				if (widgetItem == null) {
					widgetItem = new WidgetItem();
					widgetItem.setWidgetId(widgetId);
					widgetItem.setItemType(batchAddBean.getItemType()[i]);
					widgetItem.setItemId(batchAddBean.getItemId()[i]);
					widgetItem.setSiteCode(AdminGlobal.getSiteCode());
				}
				widgetItem.setTitle(batchAddBean.getTitle()[i]);
				widgetItem.setStatus(batchAddBean.getStatus()[i]);
				widgetItem.setItemStatus(batchAddBean.getItemStatus()[i]);
				widgetItem.setSortIndex(batchAddBean.getSortIndex()[i]);
				widgetItemRepository.save(widgetItem);
				typeAndIdStr += (widgetItem.getItemType()+"$"+widgetItem.getItemId()+",");
			}
			AdminGlobal.operationLogTypeAndId.set(typeAndIdStr);
			AdminGlobal.operationLogMessage.set("增加推荐位项定制名称/排序值");
		}
		return new BaseResult();
	}
}
