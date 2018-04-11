package com.ai.epg.album.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ai.AdminConstants;
import com.ai.AdminGlobal;
import com.ai.AppGlobal;
import com.ai.cms.media.bean.ImageBean;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.*;
import com.ai.common.exception.ServiceException;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;
import com.ai.epg.album.entity.Album;
import com.ai.epg.album.repository.AlbumItemRepository;
import com.ai.epg.album.repository.AlbumRepository;
import com.ai.epg.album.service.AlbumService;
import com.ai.epg.config.entity.App;
import com.ai.epg.config.repository.AppRepository;
import com.ai.epg.template.entity.Template;
import com.ai.epg.template.repository.TemplateRepository;
import com.ai.epg.template.service.TemplateService;
import com.ai.epg.widget.entity.Widget;

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
@RequestMapping(value = { "/album/album" })
public class AlbumController extends AbstractImageController {

	@Autowired
	private AlbumRepository albumRepository;

	@Autowired
	private AlbumItemRepository albumItemRepository;

	@Autowired
	private AlbumService albumService;

	@Autowired
	private TemplateRepository templateRepository;
	
	@Autowired
	private TemplateService templateService;

	@Autowired
	private AppRepository appRepository;

	private void setModel(Model model, HttpServletRequest request) {
		model.addAttribute("statusEnum", OnlineStatusEnum.values());
		model.addAttribute("typeEnum", AlbumTypeEnum.values());
		model.addAttribute("itemTypeEnum", AlbumItemTypeEnum.values());
		model.addAttribute("yesNoEnum", YesNoEnum.values());

		String currentAppCode = (String) request.getSession().getAttribute(
				"currentAppCode");
		if (StringUtils.isNotEmpty(currentAppCode)) {
			List<Template> templateList = templateRepository
					.findByAppCodeAndTypeIncludeShare(currentAppCode, TemplateTypeEnum.ALBUM.getKey());
			model.addAttribute("templateList", templateList);
		} else {
			List<Template> templateList = templateRepository.findByType(TemplateTypeEnum.ALBUM.getKey());
			model.addAttribute("templateList", templateList);
		}

		List<App> appList = appRepository.findAll();
		model.addAttribute("appList", appList);
	}

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		String currentAppCode = request.getParameter("appCode");
		if (StringUtils.isNotEmpty(currentAppCode)) {
			request.getSession().setAttribute("currentAppCode", currentAppCode);
		} else {
			String changeAppCode = request.getParameter("changeAppCode");
			if (StringUtils.isNotEmpty(changeAppCode)) {
				request.getSession().removeAttribute("currentAppCode");
			}
		}
		
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}

		List<PropertyFilter> filters = getPropertyFilters(request);

		String appCode = (String) request.getParameter("search_appCode__EQ_S");
		if (null == appCode) {
			appCode = (String) request.getSession().getAttribute(
					"currentAppCode");
			if (StringUtils.isNotEmpty(appCode)) {
				filters.add(new PropertyFilter("appCode__EQ_S", appCode));
				request.setAttribute("search_appCode__EQ_S", appCode);
			}
		}

		Specification<Album> specification = SpecificationUtils
				.getSpecification(filters);
		Page<Album> page = find(specification, pageInfo, albumRepository);
		model.addAttribute("page", page);

		setModel(model, request);

		return "album/album/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model, HttpServletRequest request) {
		Album album = new Album();
		model.addAttribute("album", album);
		setModel(model, request);

		return "album/album/edit";
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "专题管理", action = "增加", message = "增加专题")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody ImageBean<Album> imageBean) {
		return edit(imageBean, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, HttpServletRequest request,
			@PathVariable("id") Long id) {
		Album album = albumRepository.findOne(id);
		model.addAttribute("album", album);
		
		if (album != null && StringUtils.isNotEmpty(album.getTemplateCode())) {
			Template template = templateService.getTemplate(album.getAppCode(),
					album.getTemplateCode());
			model.addAttribute("template", template);
		}

		setModel(model, request);

		return "album/album/edit";
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "专题管理", action = "修改", message = "修改专题")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody ImageBean<Album> imageBean,
			@PathVariable("id") Long id) {
		Album album = imageBean.getData();
		album.setCode(StringUtils.upperCase(album.getCode()));
		String image1Data = imageBean.getImage1Data();
		String image2Data = imageBean.getImage2Data();
		String backgroundImageData = imageBean.getBackgroundImageData();
		String bgUpData = imageBean.getBgUpData();
		String bgDownData = imageBean.getBgDownData();
		String bgLeftData = imageBean.getBgLeftData();
		String bgRightData = imageBean.getBgRightData();

		if (album.getStatus() != null
				&& album.getStatus().intValue() == OnlineStatusEnum.ONLINE
						.getKey()
				&& album.getBackgroundWhole() != null
				&& album.getBackgroundWhole() == YesNoEnum.YES.getKey()
				&& (StringUtils.isEmpty(backgroundImageData) && StringUtils
						.isEmpty(album.getBackgroundImage()))) {
			throw new ServiceException("请上传背景图片!");
		}

		Album albumInfo = null;
		String image1Old = "";
		String image2Old = "";
		String backgroundImageOld = "";
		// String bgUpOld;// 背景图片上
		// String bgDownOld;// 背景图片下
		// String bgLeftOld;// 背景图片左
		// String bgRightOld;// 背景图片右
		if (id == null) {
			albumInfo = album;
			albumInfo.setSiteCode(AdminGlobal.getSiteCode());
		} else {
			albumInfo = albumRepository.findOne(id);
			if (album != null && !albumInfo.getTitle().equals(album.getTitle())) {
				AdminGlobal.operationLogMessage.set("专题修改标题：<br/>" + albumInfo.getTitle() + "  ==>  " + album.getTitle());
			} else {
				AdminGlobal.operationLogAction.set("增加");
				AdminGlobal.operationLogMessage.set("增加专题");
			}
			image1Old = albumInfo.getImage1();
			image2Old = albumInfo.getImage2();
			backgroundImageOld = albumInfo.getBackgroundImage();
			// bgUpOld = albumInfo.getBgUp();
			// bgDownOld = albumInfo.getBgDown();
			// bgLeftOld = albumInfo.getBgLeft();
			// bgRightOld = albumInfo.getBgRight();
			BeanInfoUtil
					.bean2bean(
							album,
							albumInfo,
							"type,code,name,title,image1,image2,backgroundWhole,backgroundImage,bgUp,bgDown,bgLeft,bgRight,templateCode,status,"
									+ "configItemTypes,"
									+ "configImage1,configImage1Width,configImage1Height,"
									+ "configImage2,configImage2Width,configImage2Height,"
									+ "tag,keyword,description,"
									+ "widgetContentType,widgetCode,widgetLeft,widgetTop,widgetWidth,widgetHeight,checkDuration,showProgramInfo,programInfoBarHeight");
		}

		String image1 = "";
		String image2 = "";
		String backgroundImage = "";
		String bgUp;// 背景图片上
		String bgDown;// 背景图片下
		String bgLeft;// 背景图片左
		String bgRight;// 背景图片右
		if (StringUtils.isNotEmpty(image1Data)) {
			image1 = upload(AdminConstants.MODULE_RESOURCE_ALBUM,
					AdminConstants.RESOURCE_TYPE_POSTER, image1Data);
			albumInfo.setImage1(image1);
		}
		if (StringUtils.isNotEmpty(image2Data)) {
			image2 = upload(AdminConstants.MODULE_RESOURCE_ALBUM,
					AdminConstants.RESOURCE_TYPE_POSTER, image2Data);
			albumInfo.setImage2(image2);
		}
		if (StringUtils.isNotEmpty(backgroundImageData)) {
			backgroundImage = upload(AdminConstants.MODULE_RESOURCE_ALBUM,
					AdminConstants.RESOURCE_TYPE_BACKGROUND,
					backgroundImageData);
			albumInfo.setBackgroundImage(backgroundImage);
		}

		if (StringUtils.isNotEmpty(bgUpData)) {
			bgUp = upload(AdminConstants.MODULE_RESOURCE_ALBUM,
					AdminConstants.RESOURCE_TYPE_BACKGROUND, bgUpData);
			albumInfo.setBgUp(bgUp);
		}
		if (StringUtils.isNotEmpty(bgDownData)) {
			bgDown = upload(AdminConstants.MODULE_RESOURCE_ALBUM,
					AdminConstants.RESOURCE_TYPE_BACKGROUND, bgDownData);
			albumInfo.setBgDown(bgDown);
		}
		if (StringUtils.isNotEmpty(bgLeftData)) {
			bgLeft = upload(AdminConstants.MODULE_RESOURCE_ALBUM,
					AdminConstants.RESOURCE_TYPE_BACKGROUND, bgLeftData);
			albumInfo.setBgLeft(bgLeft);
		}
		if (StringUtils.isNotEmpty(bgRightData)) {
			bgRight = upload(AdminConstants.MODULE_RESOURCE_ALBUM,
					AdminConstants.RESOURCE_TYPE_BACKGROUND, bgRightData);
			albumInfo.setBgRight(bgRight);
		}

		albumRepository.save(albumInfo);

		if (!StringUtils.trimToEmpty(image1Old).equals(
				StringUtils.trimToEmpty(album.getImage1()))) {
			// deleteOldResource(image1Old);
		}

		if (!StringUtils.trimToEmpty(image2Old).equals(
				StringUtils.trimToEmpty(album.getImage2()))) {
			// deleteOldResource(image2Old);
		}

		if (!StringUtils.trimToEmpty(backgroundImageOld).equals(
				StringUtils.trimToEmpty(album.getBackgroundImage()))) {
			// deleteOldResource(backgroundImageOld);
		}
		AdminGlobal.operationLogTypeAndId.set(ItemTypeEnum.ALBUM.getKey()+"$"+albumInfo.getId());
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "专题管理", action = "删除", message = "删除专题")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		Album albumInfo = albumRepository.findOne(id);
		if (albumInfo != null) {
			// String image1Old = albumInfo.getImage1();
			// String image2Old = albumInfo.getImage2();
			// String backgroundImageOld = albumInfo.getBackgroundImage();

			albumService.deleteAlbum(albumInfo);

			// deleteOldResource(image1Old);
			// deleteOldResource(image2Old);
			// deleteOldResource(backgroundImageOld);
			AdminGlobal.operationLogTypeAndId.set(ItemTypeEnum.ALBUM.getKey()+"$"+albumInfo.getId());
			AdminGlobal.operationLogMessage.set("删除专题");
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, HttpServletRequest request,
			@PathVariable("id") Long id) {
		Album album = albumRepository.findOne(id);
		model.addAttribute("album", album);

		if (album != null && StringUtils.isNotEmpty(album.getTemplateCode())) {
			Template template = templateService.getTemplate(album.getAppCode(),
					album.getTemplateCode());
			model.addAttribute("template", template);
		}
		
		setModel(model, request);

		return "album/album/detail";
	}

	@RequestMapping(value = { "check" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Object[] check(HttpServletRequest request,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "fieldId") String fieldId,
			@RequestParam(value = "fieldValue") String fieldValue) {
		Object[] jsonValidateReturn = new Object[3];
		jsonValidateReturn[0] = fieldId;
		jsonValidateReturn[1] = false;
		if (StringUtils.isEmpty(fieldValue)) {
			jsonValidateReturn[2] = "专题代码不能为空!";
		} else if (StringUtils.upperCase(fieldValue).indexOf(
				AppGlobal.ALBUM_CODE_PRE) != 0) {
			jsonValidateReturn[2] = "专题代码[" + fieldValue + "]需要以"
					+ AppGlobal.ALBUM_CODE_PRE + "开头!";
		} else if (AppGlobal.ALBUM_CODE_PRE.equalsIgnoreCase(fieldValue)) {
			jsonValidateReturn[2] = "专题代码[" + fieldValue + "]不正确!";
		} else {
			boolean exist = checkCode(id, fieldValue);
			if (!exist) {
				jsonValidateReturn[1] = true;
				jsonValidateReturn[2] = "可以使用!";
			} else {
				jsonValidateReturn[2] = "专题代码" + StringUtils.trim(fieldValue)
						+ "已使用!";
			}
		}
		return jsonValidateReturn;
	}

	private boolean checkCode(Long id, String code) {
		boolean exist = false;
		if (StringUtils.isNotEmpty(code)) {
			List<Album> albumList = albumRepository.findAllCode(code);
			if (albumList != null) {
				for (Album tempAlbum : albumList) {
					if (id == null || id == -1
							|| tempAlbum.getId().longValue() != id) {
						exist = true;
					}
				}
			}
		}
		return exist;
	}

	@OperationLogAnnotation(module = "项目管理", subModule = "专题管理", action = "上下线", message = "上下线专题")
	@RequestMapping(value = { "{id}/status" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult status(@PathVariable("id") Long id) {
		Album albumInfo = albumRepository.findOne(id);
		if (albumInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			albumInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
		} else {
			albumInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
		}

		if (albumInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			albumInfo.setOnlineTime(new Date());
			albumInfo.setOfflineTime(null);
		} else if (albumInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			albumInfo.setOfflineTime(new Date());
		}
		if (albumInfo.getStatus() != null
				&& albumInfo.getStatus().intValue() == OnlineStatusEnum.ONLINE
						.getKey() && albumInfo.getBackgroundWhole() != null
				&& albumInfo.getBackgroundWhole() == YesNoEnum.YES.getKey()
				&& (StringUtils.isEmpty(albumInfo.getBackgroundImage()))) {
			throw new ServiceException("请上传背景图片!");
		}
		albumRepository.save(albumInfo);
		AdminGlobal.operationLogTypeAndId.set(ItemTypeEnum.ALBUM.getKey()+"$"+albumInfo.getId());
		if (albumInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			AdminGlobal.operationLogAction.set("上线");
			AdminGlobal.operationLogMessage.set("上线专题");
		} else if (albumInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			AdminGlobal.operationLogAction.set("下线");
			AdminGlobal.operationLogMessage.set("下线专题");
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "batchToAlbum" }, method = RequestMethod.GET)
	public String toBatchToAlbum(Model model, HttpServletRequest request,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		setModel(model, request);

		return "album/album/batchToAlbum";
	}

	@OperationLogAnnotation(module = "内容管理", subModule = "节目/剧头管理", action = "批量编排", message = "批量编排到专题")
	@RequestMapping(value = { "batchToAlbum" }, method = { RequestMethod.POST }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchToAlbum(
			@RequestParam(value = "selectAlbumId") String selectAlbumId,
			@RequestParam(value = "status") Integer status,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds,
			@RequestParam(value = "sortIndex") Integer sortIndex) {
		if (selectAlbumId == null || itemType == null
				|| StringUtils.isEmpty(itemIds)) {
			return new BaseResult();
		}
		albumService.batchUpdate(selectAlbumId, status, itemType, itemIds,
				sortIndex);

		String typeAndIdStr = "";
		for (String s : itemIds.split(",")) {
			typeAndIdStr += (itemType+"$"+s+",");
		}
		AdminGlobal.operationLogTypeAndId.set(typeAndIdStr);
		Album album = albumRepository.findOne(Long.valueOf(selectAlbumId));
		AdminGlobal.operationLogMessage.set("编排到专题: <br/>" + album.getAppCode() + "  ==>  " + album.getTitle());
		return new BaseResult();
	}

	@RequestMapping(value = { "selectAlbum" })
	public String selectAlbum(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}

		List<PropertyFilter> filters = getPropertyFilters(request);

		String appCode = (String) request.getParameter("search_appCode__EQ_S");
		if (null == appCode) {
			appCode = (String) request.getSession().getAttribute(
					"currentAppCode");
			if (StringUtils.isNotEmpty(appCode)) {
				filters.add(new PropertyFilter("appCode__EQ_S", appCode));
				request.setAttribute("search_appCode__EQ_S", appCode);
			}
		}

		Specification<Album> specification = SpecificationUtils
				.getSpecification(filters);
		Page<Album> page = find(specification, pageInfo, albumRepository);
		model.addAttribute("page", page);

		setModel(model, request);

		return "album/album/selectAlbum";
	}

	@RequestMapping(value = { "selectItem" })
	public String selectItem(Model model, HttpServletRequest request,
			PageInfo pageInfo) {

		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}

		List<PropertyFilter> filters = getPropertyFilters(request);

		String appCode = (String) request.getParameter("search_appCode__EQ_S");
		if (null == appCode) {
			appCode = (String) request.getSession().getAttribute(
					"currentAppCode");
			if (StringUtils.isNotEmpty(appCode)) {
				filters.add(new PropertyFilter("appCode__EQ_S", appCode));
				request.setAttribute("search_appCode__EQ_S", appCode);
			}
		}

		Specification<Album> specification = SpecificationUtils
				.getSpecification(filters);
		Page<Album> page = find(specification, pageInfo, albumRepository);
		model.addAttribute("page", page);

		setModel(model, request);

		return "album/album/selectItem";
	}
}
