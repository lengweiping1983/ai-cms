package com.ai.epg.album.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ai.AdminConstants;
import com.ai.AdminGlobal;
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
import com.ai.common.enums.AlbumItemTypeEnum;
import com.ai.common.enums.AlbumJumpItemTypeEnum;
import com.ai.common.enums.AlbumJumpTypeEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.TemplateParamCategoryEnum;
import com.ai.common.enums.WidgetJumpItemTypeEnum;
import com.ai.common.exception.ServiceException;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;
import com.ai.epg.album.entity.Album;
import com.ai.epg.album.entity.AlbumItem;
import com.ai.epg.album.entity.AlbumItemView;
import com.ai.epg.album.repository.AlbumItemRepository;
import com.ai.epg.album.repository.AlbumItemViewRepository;
import com.ai.epg.album.repository.AlbumRepository;
import com.ai.epg.album.service.AlbumService;
import com.ai.epg.category.repository.CategoryRepository;
import com.ai.epg.subscriber.entity.SubscriberGroup;
import com.ai.epg.subscriber.repository.SubscriberGroupRepository;
import com.ai.epg.template.repository.TemplateRepository;
import com.ai.epg.uri.bean.UriImageBean;
import com.ai.epg.uri.bean.UriParamBean;
import com.ai.epg.uri.entity.Uri;
import com.ai.epg.uri.repository.UriRepository;
import com.ai.epg.uri.service.UriService;

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

@Controller
@RequestMapping(value = { "/album/{albumId}/albumItem" })
public class AlbumItemController extends AbstractImageController {

	@Autowired
	private AlbumRepository albumRepository;

	@Autowired
	private AlbumItemRepository albumItemRepository;

	@Autowired
	private AlbumItemViewRepository albumItemViewRepository;

	@Autowired
	private AlbumService albumService;
	
	@Autowired
	private SubscriberGroupRepository subscriberGroupRepository;
	
	@Autowired
	private TemplateRepository templateRepository;
	
	@Autowired
	private ProgramRepository programRepository;
	
	@Autowired
	private SeriesRepository seriesRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private UriRepository uriRepository;
	
	@Autowired
	private UriService uriService;
	
	private void setModel(Model model) {
		model.addAttribute("typeEnum", AlbumItemTypeEnum.values());
		model.addAttribute("statusEnum", OnlineStatusEnum.values());
		
		model.addAttribute("jumpTypeEnum", AlbumJumpTypeEnum.values());
		
		model.addAttribute("albumJumpItemTypeEnum", AlbumJumpItemTypeEnum.values());
		model.addAttribute("templateParamCategoryEnum",
				TemplateParamCategoryEnum.values());
		
		List<SubscriberGroup> subscriberGroupList = subscriberGroupRepository
				.findAll();
		model.addAttribute("subscriberGroupList", subscriberGroupList);
	}

	@RequestMapping(value = { "" })
	public String list(@PathVariable("albumId") Long albumId, Model model,
			HttpServletRequest request, PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("sortIndex");
		}

		Album album = albumRepository.findOne(albumId);
		model.addAttribute("album", album);

		Page<AlbumItemView> page = find(request, pageInfo,
				albumItemViewRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "album/albumItem/list";
	}

	@RequestMapping(value = { "listByPosition" })
	public String listByPosition(@PathVariable("albumId") Long albumId,
			Model model, HttpServletRequest request, PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("sortIndex");
		}

		Album album = albumRepository.findOne(albumId);
		model.addAttribute("album", album);

		String positionStr = "";
		List<AlbumItemView> list = albumItemViewRepository
				.findAllByAlbumId(albumId);
		for (AlbumItemView albumItemView : list) {
			if (StringUtils.isNotEmpty(albumItemView.getPosition())) {
				positionStr += StringUtils.trimToEmpty(albumItemView
						.getPosition()) + ",";
			}
		}
		model.addAttribute("positionStr", positionStr);

		setModel(model);

		return "album/albumItem/listByPosition";
	}

	@RequestMapping(value = { "editByPosition" }, method = RequestMethod.GET)
	public String toEditByPosition(Model model, HttpServletRequest request,
			@PathVariable("albumId") Long albumId) {
		String positionId = request.getParameter("positionId");

		AlbumItemView albumItemView = albumItemViewRepository
				.findOneByAlbumIdAndPositionId(albumId, positionId);
		if (albumItemView == null) {
			albumItemView = new AlbumItemView();
			albumItemView.setPositionId(positionId);
		}
		
		AlbumItem albumItem = albumItemRepository
				.findOneByAlbumIdAndPositionId(albumId, positionId);
		if (albumItem == null) {
			albumItem = new AlbumItem();
			albumItem.setPositionId(positionId);
		}

		model.addAttribute("albumItemView", albumItemView);
		model.addAttribute("albumItem", albumItem);
		
		if (albumItem.getJumpItemType() != null
				&& albumItem.getJumpItemId() != null) {
			if (albumItem.getJumpItemType() == WidgetJumpItemTypeEnum.PROGRAM
					.getKey()) {
				Program program = programRepository.findOne(albumItem
						.getJumpItemId());
				if (program != null) {
					model.addAttribute("jumpItemName", program.getName());
				}
			} else if (albumItem.getJumpItemType() == WidgetJumpItemTypeEnum.SERIES
					.getKey()) {
				Series series = seriesRepository.findOne(albumItem
						.getJumpItemId());
				if (series != null) {
					model.addAttribute("jumpItemName", series.getName());
				}
			} else if (albumItem.getJumpItemType() == WidgetJumpItemTypeEnum.ALBUM
					.getKey()) {
				Album album = albumRepository.findOne(albumItem
						.getJumpItemId());
				if (album != null) {
					model.addAttribute("jumpItemName", album.getName());
				}
			} else if (albumItem.getJumpItemType() == WidgetJumpItemTypeEnum.URI
					.getKey()) {
				Uri uri = uriRepository.findOne(albumItem
						.getJumpItemId());
				if (uri != null) {
					model.addAttribute("jumpItemName", uri.getName());
				}
			}
		}

		Album album = albumRepository.findOne(albumId);
		model.addAttribute("album", album);

		setModel(model);
		return "album/albumItem/editByPosition";
	}

	@RequestMapping(value = { "editByPosition" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult editByPosition(Model model, HttpServletRequest request,
			@PathVariable("albumId") Long albumId,
			@RequestBody UriImageBean<AlbumItem> imageBean) {
		AlbumItem albumItem = imageBean.getData();

		AlbumItem albumItemInfo = albumItemRepository
				.findOneByAlbumIdAndPositionId(albumId,
						albumItem.getPositionId());
		if (albumItemInfo != null) {
			BeanInfoUtil.bean2bean(albumItem, albumItemInfo, "position");
			albumItemRepository.save(albumItemInfo);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model, HttpServletRequest request,
			@PathVariable("albumId") Long albumId) {
		AlbumItemView albumItemView = new AlbumItemView();
		model.addAttribute("albumItemView", albumItemView);
		
		AlbumItem albumItem = new AlbumItem();
		model.addAttribute("albumItem", albumItem);

		Album album = albumRepository.findOne(albumId);
		model.addAttribute("album", album);

		setModel(model);

		return "album/albumItem/edit";
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "专题项管理", action = "增加", message = "增加专题项")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(HttpServletRequest request, @PathVariable("albumId") Long albumId,
			@RequestBody UriImageBean<AlbumItem> imageBean) {
		return edit(request, albumId, imageBean, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("albumId") Long albumId,
			@PathVariable("id") Long id) {
		AlbumItemView albumItemView = albumItemViewRepository.findOne(id);
		model.addAttribute("albumItemView", albumItemView);
		
		AlbumItem albumItem = albumItemRepository.findOne(id);
		model.addAttribute("albumItem", albumItem);
		
		if (albumItem.getJumpItemType() != null
				&& albumItem.getJumpItemId() != null) {
			if (albumItem.getJumpItemType() == WidgetJumpItemTypeEnum.PROGRAM
					.getKey()) {
				Program program = programRepository.findOne(albumItem
						.getJumpItemId());
				if (program != null) {
					model.addAttribute("jumpItemName", program.getName());
				}
			} else if (albumItem.getJumpItemType() == WidgetJumpItemTypeEnum.SERIES
					.getKey()) {
				Series series = seriesRepository.findOne(albumItem
						.getJumpItemId());
				if (series != null) {
					model.addAttribute("jumpItemName", series.getName());
				}
			} else if (albumItem.getJumpItemType() == WidgetJumpItemTypeEnum.ALBUM
					.getKey()) {
				Album album = albumRepository.findOne(albumItem
						.getJumpItemId());
				if (album != null) {
					model.addAttribute("jumpItemName", album.getName());
				}
			} else if (albumItem.getJumpItemType() == WidgetJumpItemTypeEnum.URI
					.getKey()) {
				Uri uri = uriRepository.findOne(albumItem
						.getJumpItemId());
				if (uri != null) {
					model.addAttribute("jumpItemName", uri.getName());
				}
			}
		}

		Album album = albumRepository.findOne(albumId);
		model.addAttribute("album", album);

		setModel(model);

		return "album/albumItem/edit";
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "专题管理", action = "修改", message = "修改专题项")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(HttpServletRequest request, @PathVariable("albumId") Long albumId,
			@RequestBody UriImageBean<AlbumItem> imageBean,
			@PathVariable("id") Long id) {
		AlbumItem albumItem = imageBean.getData();
		String image1Data = imageBean.getImage1Data();
		String image2Data = imageBean.getImage2Data();
		String backgroundImageData = imageBean.getBackgroundImageData();

		AlbumItem albumItemInfo = null;
		String image1Old = "";
		String image2Old = "";
		String backgroundImageOld = "";
		if (id == null) {
			albumItemInfo = albumItem;
			albumItemInfo.setSiteCode(AdminGlobal.getSiteCode());			
			if (albumItem.getItemType() != AlbumItemTypeEnum.URI.getKey()) {
				AlbumItem existAlbumItem = albumService.getAlbumItem(albumId,
						albumItem.getItemType(), albumItem.getItemId());
				if (existAlbumItem != null) {
					throw new ServiceException("该元素已存在！");
				}
			}
		} else {
			albumItemInfo = albumItemRepository.findOne(id);
			if (albumItem != null && !albumItemInfo.getTitle().equals(albumItem.getTitle())) {
				AdminGlobal.operationLogMessage.set("专题项修改标题：<br/>" + albumItemInfo.getTitle() + "  ==>  " + albumItem.getTitle());
			} else {
				AdminGlobal.operationLogAction.set("增加");
				AdminGlobal.operationLogMessage.set("增加专题项");
			}
			image1Old = albumItemInfo.getImage1();
			image2Old = albumItemInfo.getImage2();
			backgroundImageOld = albumItemInfo.getBackgroundImage();
			BeanInfoUtil.bean2bean(albumItem, albumItemInfo,
					"title,image1,image2,sortIndex,status,jumpItemType,jumpItemId,jumpType,url,jumpParam1,jumpParam2,"
					+ "backgroundImage,internalParamCategory,groupCodes,validTime,expiredTime,positionId,position");
		}

		String image1 = "";
		String image2 = "";
		String backgroundImage = "";
		if (StringUtils.isNotEmpty(image1Data)) {
			image1 = upload(AdminConstants.MODULE_RESOURCE_ALBUM,
					AdminConstants.RESOURCE_TYPE_POSTER, image1Data);
			albumItemInfo.setImage1(image1);
		}
		if (StringUtils.isNotEmpty(image2Data)) {
			image2 = upload(AdminConstants.MODULE_RESOURCE_ALBUM,
					AdminConstants.RESOURCE_TYPE_POSTER, image2Data);
			albumItemInfo.setImage2(image2);
		}
		if (StringUtils.isNotEmpty(backgroundImageData)) {
			backgroundImage = upload(AdminConstants.MODULE_RESOURCE_ALBUM,
					AdminConstants.RESOURCE_TYPE_BACKGROUND,
					backgroundImageData);
			albumItemInfo.setBackgroundImage(backgroundImage);
		}
		
		if (albumItemInfo.getItemType() == AlbumItemTypeEnum.URI.getKey()
				|| (albumItemInfo.getJumpItemType() != null && albumItemInfo
						.getJumpItemType() == AlbumJumpItemTypeEnum.URI
						.getKey())) {
			List<UriParamBean> params = imageBean.getParams();
			Long uriId = null;
			if (albumItemInfo.getJumpItemType() == AlbumJumpItemTypeEnum.URI
					.getKey()) {
				uriId = albumItemInfo.getJumpItemId();
			} else {
				uriId = albumItemInfo.getItemId();
			}
			String appCode = (String) request.getSession().getAttribute(
					"currentAppCode");
			uriService.saveWidgetAndCategory(uriId, params, appCode);
			String internalParam = uriService.getInternalParam(uriId, params);
			albumItemInfo.setInternalParam(internalParam);
		}
		
		try {
			albumService.saveAlbumItem(albumItemInfo);
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			throw new ServiceException("该元素已存在！");
		}

		if (!StringUtils.trimToEmpty(image1Old).equals(
				StringUtils.trimToEmpty(albumItem.getImage1()))) {
			// deleteOldResource(image1Old);
		}

		if (!StringUtils.trimToEmpty(image2Old).equals(
				StringUtils.trimToEmpty(albumItem.getImage2()))) {
			// deleteOldResource(image2Old);
		}
		if (!StringUtils.trimToEmpty(backgroundImageOld).equals(
				StringUtils.trimToEmpty(albumItem.getBackgroundImage()))) {
			 deleteOldResource(backgroundImageOld);
		}
		AdminGlobal.operationLogTypeAndId.set(albumItem.getItemType()+"$"+albumItemInfo.getItemId());
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "专题管理", action = "删除", message = "删除专题项")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("albumId") Long albumId,
			@PathVariable("id") Long id) {
		AlbumItem albumItemInfo = albumItemRepository.findOne(id);
		if (albumItemInfo != null) {
			String image1Old = albumItemInfo.getImage1();
			String image2Old = albumItemInfo.getImage2();
			String backgroundImageOld = albumItemInfo.getBackgroundImage();
			albumItemRepository.delete(albumItemInfo);

			deleteOldResource(image1Old);
			deleteOldResource(image2Old);
			deleteOldResource(backgroundImageOld);
			AdminGlobal.operationLogTypeAndId.set(albumItemInfo.getItemType()+"$"+albumItemInfo.getItemId());
			AdminGlobal.operationLogMessage.set("删除专题项");
		}
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "专题管理", action = "上下线", message = "上下线专题项")
	@RequestMapping(value = { "{id}/status" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult status(@PathVariable("id") Long id) {
		AlbumItem albumItemInfo = albumItemRepository.findOne(id);
		if (albumItemInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			albumItemInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
		} else {
			albumItemInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
		}
		albumItemRepository.save(albumItemInfo);
		AdminGlobal.operationLogTypeAndId.set(albumItemInfo.getItemType()+"$"+albumItemInfo.getItemId());
		if (albumItemInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			AdminGlobal.operationLogAction.set("上线");
			AdminGlobal.operationLogMessage.set("上线专题项");
		} else if (albumItemInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			AdminGlobal.operationLogAction.set("下线");
			AdminGlobal.operationLogMessage.set("下线专题项");
		}
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "专题管理", action = "批量修改", message = "批量上下线专题项")
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
				AlbumItem albumItemInfo = albumItemRepository.findOne(itemId);
				albumItemInfo.setStatus(batchBean.getStatus());
				albumItemRepository.save(albumItemInfo);
				typeAndIdStr += (albumItemInfo.getItemType()+"$"+albumItemInfo.getItemId()+",");
			}
			AdminGlobal.operationLogTypeAndId.set(typeAndIdStr);
			if (batchBean.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
				AdminGlobal.operationLogAction.set("批量上线");
				AdminGlobal.operationLogMessage.set("上线专题项");
			} else if (batchBean.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
				AdminGlobal.operationLogAction.set("批量下线");
				AdminGlobal.operationLogMessage.set("下线专题项");
			}
		}
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "专题管理", action = "批量修改", message = "批量删除专题项")
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
				AlbumItem albumItemInfo = albumItemRepository.findOne(itemId);
				if (albumItemInfo != null
						&& albumItemInfo.getStatus() == OnlineStatusEnum.OFFLINE
								.getKey()) {
					albumItemRepository.delete(albumItemInfo);
					typeAndIdStr += (albumItemInfo.getItemType()+"$"+albumItemInfo.getItemId()+",");
				}

			}
			AdminGlobal.operationLogTypeAndId.set(typeAndIdStr);
			AdminGlobal.operationLogMessage.set("删除专题项");
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "batchChangeTitle" }, method = RequestMethod.GET)
	public String batchChangeTitle(Model model,
			@PathVariable("albumId") Long albumId,
			@RequestParam(value = "itemIds") String itemIds) {
		Album album = albumRepository.findOne(albumId);
		model.addAttribute("album", album);

		List<Long> idList = new ArrayList<Long>();

		String[] itemIdArr = itemIds.split(",");
		if (null != itemIdArr) {
			for (String itemIdStr : itemIdArr) {
				Long itemId = Long.valueOf(itemIdStr);
				idList.add(itemId);
			}
		}

		if (idList.size() > 0) {
			List<AlbumItemView> albumItemViewList = albumItemViewRepository
					.findByIdIn(idList);

			List<AlbumItemView> albumItemList = new ArrayList<AlbumItemView>();
			for (Long id : idList) {
				for (AlbumItemView albumItemView : albumItemViewList) {
					if (id.longValue() == albumItemView.getId().longValue()) {
						albumItemList.add(albumItemView);
						break;
					}
				}
			}
			model.addAttribute("albumItemList", albumItemList);
		}

		setModel(model);

		return "album/albumItem/batchChangeTitle";
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "专题管理", action = "批量修改", message = "批量修改专题项定制名称/排序值")
	@RequestMapping(value = { "batchChangeTitle" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchChangeTitle(@RequestBody BatchTitleBean batchBean) {
		Long[] ids = batchBean.getIds();
		String typeAndIdStr = "";
		if (null != ids) {
			for (int i = 0; i < ids.length; i++) {
				AlbumItem albumItem = albumItemRepository.findOne(ids[i]);
				if (albumItem != null) {
					albumItem.setTitle(batchBean.getTitles()[i]);
					albumItem.setSortIndex(batchBean.getSortIndexs()[i]);
					albumItemRepository.save(albumItem);
					typeAndIdStr += (albumItem.getItemType()+"$"+albumItem.getItemId()+",");
				}
			}
			AdminGlobal.operationLogTypeAndId.set(typeAndIdStr);
			AdminGlobal.operationLogMessage.set("修改专题项定制名称/排序值");
		}
		return new BaseResult();
	}
	
	@RequestMapping(value = { "batchAdd" }, method = RequestMethod.GET)
	public String toBatchAdd(Model model,
			@PathVariable("albumId") Long albumId) {
		Album album = albumRepository.findOne(albumId);
		model.addAttribute("album", album);

		setModel(model);

		return "album/albumItem/batchAdd";
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "专题管理", action = "批量增加专题项", message = "批量增加专题项")
	@RequestMapping(value = { "batchAdd" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchAdd(@PathVariable("albumId") Long albumId,
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
				albumService.batchUpdateMoveByAfter(albumId, step,
						minSortIndex);
			}
			for (int i = 0; i < batchAddBean.getItemId().length; i++) {
				AlbumItem albumItem = albumService.getAlbumItem(albumId,
						batchAddBean.getItemType()[i],
						batchAddBean.getItemId()[i]);
				if (albumItem == null) {
					albumItem = new AlbumItem();
					albumItem.setAlbumId(albumId);
					albumItem.setItemType(batchAddBean.getItemType()[i]);
					albumItem.setItemId(batchAddBean.getItemId()[i]);
					albumItem.setSiteCode(AdminGlobal.getSiteCode());
				}
				albumItem.setTitle(batchAddBean.getTitle()[i]);
				albumItem.setStatus(batchAddBean.getStatus()[i]);
				albumItem.setItemStatus(batchAddBean.getItemStatus()[i]);
				albumItem.setSortIndex(batchAddBean.getSortIndex()[i]);
				albumItemRepository.save(albumItem);
				typeAndIdStr += (albumItem.getItemType()+"$"+albumItem.getItemId()+",");
			}
			AdminGlobal.operationLogTypeAndId.set(typeAndIdStr);
			AdminGlobal.operationLogMessage.set("增加专题项定制名称/排序值");
		}
		return new BaseResult();
	}
}
