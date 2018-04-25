package com.ai.cms.media.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import org.springframework.web.multipart.MultipartFile;

import com.ai.AdminConstants;
import com.ai.AdminGlobal;
import com.ai.cms.config.entity.Cp;
import com.ai.cms.config.entity.MediaTemplate;
import com.ai.cms.config.service.ConfigService;
import com.ai.cms.injection.entity.InjectionObject;
import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.enums.InjectionItemTypeEnum;
import com.ai.cms.injection.enums.InjectionStatusEnum;
import com.ai.cms.injection.enums.PlatformTypeEnum;
import com.ai.cms.injection.enums.PlayCodeStatusEnum;
import com.ai.cms.injection.service.InjectionService;
import com.ai.cms.media.bean.BatchBean;
import com.ai.cms.media.bean.BatchInjectionBean;
import com.ai.cms.media.bean.BatchMetadataBean;
import com.ai.cms.media.bean.BatchStatusBean;
import com.ai.cms.media.bean.BatchTypeBean;
import com.ai.cms.media.bean.BatchUploadResult;
import com.ai.cms.media.bean.ImageBean;
import com.ai.cms.media.bean.SeriesExportLog;
import com.ai.cms.media.bean.UploadImageBean;
import com.ai.cms.media.entity.MediaImage;
import com.ai.cms.media.entity.Series;
import com.ai.cms.media.repository.MediaImageRepository;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.cms.media.repository.SeriesRepository;
import com.ai.cms.media.service.MediaService;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.bean.ResultCode;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.AuditStatusEnum;
import com.ai.common.enums.ContentTypeEnum;
import com.ai.common.enums.MediaImageTypeEnum;
import com.ai.common.enums.MediaStatusEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.SeriesTypeEnum;
import com.ai.common.enums.SourceEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.excel.ExportExcel;
import com.ai.common.exception.ServiceException;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.common.utils.DateUtils;
import com.ai.env.handler.OperationLogAnnotation;
import com.ai.sys.security.SecurityUtils;

@Controller
@RequestMapping(value = { "/media/series" })
public class SeriesController extends AbstractImageController {

	@Autowired
	protected SeriesRepository seriesRepository;

	@Autowired
	protected ProgramRepository programRepository;

	@Autowired
	protected MediaImageRepository mediaImageRepository;

	@Autowired
	protected MediaService mediaService;

	@Autowired
	protected InjectionService injectionService;

	@Autowired
	protected ConfigService configService;

	protected void setModel(Model model) {
		model.addAttribute("statusEnum", OnlineStatusEnum.values());
		model.addAttribute("typeEnum", SeriesTypeEnum.values());
		model.addAttribute("contentTypeEnum", ContentTypeEnum.toList());

		model.addAttribute("yesNoEnum", YesNoEnum.values());
		model.addAttribute("sourceEnum", SourceEnum.values());

		model.addAttribute("mediaStatusEnum", MediaStatusEnum.values());
		model.addAttribute("platformTypeEnum", PlatformTypeEnum.values());
		model.addAttribute("injectionStatusEnum", InjectionStatusEnum.values());
		model.addAttribute("playCodeStatusEnum", PlayCodeStatusEnum.values());

		model.addAttribute("auditStatusEnum", AuditStatusEnum.toList());

		model.addAttribute("cpList", configService.findAllCp());
		model.addAttribute("mediaTemplateList",
				configService.findAllMediaTemplate());
		List<InjectionPlatform> injectionPlatformList = injectionService
				.findAllInjectionPlatform();
		model.addAttribute("injectionPlatformList", injectionPlatformList);
		model.addAttribute("injectionPlatformMap", injectionService
				.findAllInjectionPlatformMap(injectionPlatformList));
	}

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}

		List<PropertyFilter> filters = getPropertyFilters(request);
		if (SecurityUtils.getCpId() != null) {
			filters.add(new PropertyFilter("cpId__INMASK_S", ""
					+ SecurityUtils.getCpId()));
		}
		Specification<Series> specification = SpecificationUtils
				.getSpecification(filters);
		Page<Series> page = find(specification, pageInfo, seriesRepository);
		model.addAttribute("page", page);

		List<Long> itemIdList = page.getContent().stream().map(s -> s.getId())
				.collect(Collectors.toList());

		List<InjectionObject> injectionObjectListAll = injectionService
				.findInjectionObjectList(InjectionItemTypeEnum.SERIES,
						itemIdList);
		Map<Long, List<InjectionObject>> injectionObjectMap = new HashMap<Long, List<InjectionObject>>();
		for (InjectionObject injectionObject : injectionObjectListAll) {
			List<InjectionObject> injectionObjectList = injectionObjectMap
					.get(injectionObject.getItemId());
			if (injectionObjectList == null) {
				injectionObjectList = new ArrayList<InjectionObject>();
				injectionObjectMap.put(injectionObject.getItemId(),
						injectionObjectList);
			}
			injectionObjectList.add(injectionObject);
		}
		model.addAttribute("injectionObjectMap", injectionObjectMap);

		setModel(model);

		return "media/series/list";
	}

	@RequestMapping(value = { "selectItem" })
	public String selectItem(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		list(model, request, pageInfo);

		return "media/series/selectItem";
	}

	@RequestMapping(value = { "selectSeries" })
	public String selectSeries(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		list(model, request, pageInfo);

		return "media/series/selectSeries";
	}

	@RequiresPermissions("media:series:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model) {
		Series series = new Series();
		model.addAttribute("series", series);

		if (SecurityUtils.getCpId() != null) {
			model.addAttribute("currentCpId", SecurityUtils.getCpId());
		}
		setModel(model);

		return "media/series/edit";
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "剧头管理", action = "增加", message = "增加剧头")
	@RequiresPermissions("media:series:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody ImageBean<Series> imageBean) {
		return edit(imageBean, null, false, false);
	}

	@RequiresPermissions("media:series:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		Series series = seriesRepository.findOne(id);
		model.addAttribute("series", series);

		if (SecurityUtils.getCpId() != null) {
			model.addAttribute("currentCpId", SecurityUtils.getCpId());
		}
		setModel(model);

		return "media/series/edit";
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "剧头管理", action = "修改", message = "修改剧头")
	@RequiresPermissions("media:series:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody ImageBean<Series> imageBean,
			@PathVariable("id") Long id, boolean notAutoCreate,
			boolean notAutoAudit) {
		String message = "";
		Series operationObjectList = null;

		Series series = imageBean.getData();
		String image1Data = imageBean.getImage1Data();
		String image2Data = imageBean.getImage2Data();
		String image3Data = imageBean.getImage3Data();
		String image4Data = imageBean.getImage4Data();

		Series seriesInfo = null;
		String image1Old = "";
		String image2Old = "";
		String image3Old = "";
		String image4Old = "";
		if (id != null && id > 0) {
			seriesInfo = seriesRepository.findOne(id);
			if (seriesInfo != null) {
				if (!seriesInfo.getTitle().equals(series.getTitle())) {
					message = "标题[" + seriesInfo.getTitle() + "  ==>  "
							+ series.getTitle() + "]";
				}
				image1Old = seriesInfo.getImage1();
				image2Old = seriesInfo.getImage2();
				image3Old = seriesInfo.getImage3();
				image4Old = seriesInfo.getImage4();
				BeanInfoUtil.bean2bean(series, seriesInfo, Series.METADATA
						+ "," + Series.POSTER + "");
			}
		}
		if (!notAutoCreate) {
			if (seriesInfo == null) {
				seriesInfo = series;
			}

			String image1 = "";
			String image2 = "";
			String image3 = "";
			String image4 = "";
			if (StringUtils.isNotEmpty(image1Data)) {
				image1 = upload(AdminConstants.MODULE_RESOURCE_MEDIA,
						AdminConstants.RESOURCE_TYPE_POSTER, image1Data);
				seriesInfo.setImage1(image1);
			}
			if (StringUtils.isNotEmpty(image2Data)) {
				image2 = upload(AdminConstants.MODULE_RESOURCE_MEDIA,
						AdminConstants.RESOURCE_TYPE_POSTER, image2Data);
				seriesInfo.setImage2(image2);
			}
			if (StringUtils.isNotEmpty(image3Data)) {
				image3 = upload(AdminConstants.MODULE_RESOURCE_MEDIA,
						AdminConstants.RESOURCE_TYPE_POSTER, image3Data);
				seriesInfo.setImage3(image3);
			}
			if (StringUtils.isNotEmpty(image4Data)) {
				image4 = upload(AdminConstants.MODULE_RESOURCE_MEDIA,
						AdminConstants.RESOURCE_TYPE_POSTER, image4Data);
				seriesInfo.setImage4(image4);
			}
			if (!notAutoAudit) {
				seriesInfo.setAuditStatus(AuditStatusEnum.AUDIT_FIRST_PASS
						.getKey());
				seriesInfo.setStorageTime(new Date());
			}
			mediaService.saveSeries(seriesInfo);
			operationObjectList = seriesInfo;

			if (!StringUtils.trimToEmpty(image1Old).equals(
					StringUtils.trimToEmpty(series.getImage1()))) {
				deleteOldResource(image1Old);
			}
			if (!StringUtils.trimToEmpty(image2Old).equals(
					StringUtils.trimToEmpty(series.getImage2()))) {
				deleteOldResource(image2Old);
			}
			if (!StringUtils.trimToEmpty(image3Old).equals(
					StringUtils.trimToEmpty(series.getImage3()))) {
				deleteOldResource(image3Old);
			}
			if (!StringUtils.trimToEmpty(image4Old).equals(
					StringUtils.trimToEmpty(series.getImage4()))) {
				deleteOldResource(image4Old);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformSeriesOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "剧头管理", action = "删除", message = "删除剧头")
	@RequiresPermissions("media:series:delete")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		String message = "";
		Series operationObjectList = null;
		Series seriesInfo = seriesRepository.findOne(id);
		if (seriesInfo != null) {
			String image1Old = seriesInfo.getImage1();
			String image2Old = seriesInfo.getImage2();
			String image3Old = seriesInfo.getImage3();
			String image4Old = seriesInfo.getImage4();

			mediaService.deleteSeries(seriesInfo);
			operationObjectList = seriesInfo;

			deleteOldResource(image1Old);
			deleteOldResource(image2Old);
			deleteOldResource(image3Old);
			deleteOldResource(image4Old);
		} else {
			message = "剧头不存在！";
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformSeriesOperationObject(operationObjectList));
	}

	@RequiresPermissions("media:series:view")
	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable("id") Long id) {
		model.addAttribute("readOnly", "true");
		return toEdit(model, id);
	}

	@RequiresPermissions("media:series:batchEdit")
	@RequestMapping(value = { "batchType" }, method = RequestMethod.GET)
	public String batchType(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		setModel(model);

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		return "media/series/batchType";
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "剧头管理", action = "批量修改", message = "批量修改剧头")
	@RequiresPermissions("media:series:batchEdit")
	@RequestMapping(value = { "batchType" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchType(@RequestBody BatchTypeBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		Integer type = batchBean.getType();
		Long seriesId = batchBean.getSeriesId();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		if (type == null
				|| (type == SeriesTypeEnum.TV.getKey() && seriesId == null)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<Series> operationObjectList = new ArrayList<Series>();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Series seriesInfo = seriesRepository.findOne(itemId);
			if (seriesInfo != null) {
				seriesInfo.setType(type);
				mediaService.saveSeries(seriesInfo);
				operationObjectList.add(seriesInfo);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformSeriesOperationObject(operationObjectList));
	}

	@RequiresPermissions("media:series:batchEdit")
	@RequestMapping(value = { "batchChangeMetadata" }, method = RequestMethod.GET)
	public String batchChangeMetadata(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		setModel(model);

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		return "media/series/batchChangeMetadata";
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "剧头管理", action = "批量修改", message = "批量修改剧头")
	@RequiresPermissions("media:series:batchEdit")
	@RequestMapping(value = { "batchChangeMetadata" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchChangeMetadata(
			@RequestBody BatchMetadataBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<Series> operationObjectList = new ArrayList<Series>();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Series seriesInfo = seriesRepository.findOne(itemId);
			if (seriesInfo != null) {
				if (batchBean.getContentTypeSwitch() != null
						&& batchBean.getContentTypeSwitch().equals("on")) {
					seriesInfo.setContentType(batchBean.getContentType());
				}
				if (batchBean.getCpIdSwitch() != null
						&& batchBean.getCpIdSwitch().equals("on")) {
					seriesInfo.setCpId(batchBean.getCpId());
					mediaService.updateCpIdBySeriesId(seriesInfo);
				}
				if (batchBean.getTagSwitch() != null
						&& batchBean.getTagSwitch().equals("on")) {
					seriesInfo.setTag(batchBean.getTag());
				}
				if (batchBean.getKeywordSwitch() != null
						&& batchBean.getKeywordSwitch().equals("on")) {
					seriesInfo.setKeyword(batchBean.getKeyword());
				}

				if (batchBean.getDirectorSwitch() != null
						&& batchBean.getDirectorSwitch().equals("on")) {
					seriesInfo.setDirector(batchBean.getDirector());
				}
				if (batchBean.getActorSwitch() != null
						&& batchBean.getActorSwitch().equals("on")) {
					seriesInfo.setActor(batchBean.getActor());
				}
				if (batchBean.getCompereSwitch() != null
						&& batchBean.getCompereSwitch().equals("on")) {
					seriesInfo.setCompere(batchBean.getCompere());
				}
				if (batchBean.getGuestSwitch() != null
						&& batchBean.getGuestSwitch().equals("on")) {
					seriesInfo.setGuest(batchBean.getGuest());
				}
				if (batchBean.getYearSwitch() != null
						&& batchBean.getYearSwitch().equals("on")) {
					seriesInfo.setYear(batchBean.getYear());
				}
				if (batchBean.getAreaSwitch() != null
						&& batchBean.getAreaSwitch().equals("on")) {
					seriesInfo.setArea(batchBean.getArea());
				}
				if (batchBean.getLanguageSwitch() != null
						&& batchBean.getLanguageSwitch().equals("on")) {
					seriesInfo.setLanguage(batchBean.getLanguage());
				}
				if (batchBean.getKuoZhanOneSwitch() != null
						&& batchBean.getKuoZhanOneSwitch().equals("on")) {
					seriesInfo.setReserved1(batchBean.getKuoZhanOne());
				}
				if (batchBean.getKuoZhanTwoSwitch() != null
						&& batchBean.getKuoZhanTwoSwitch().equals("on")) {
					seriesInfo.setReserved2(batchBean.getKuoZhanTwo());
				}
				mediaService.saveSeries(seriesInfo);
				operationObjectList.add(seriesInfo);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformSeriesOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "剧头管理", action = "批量删除", message = "批量删除剧头")
	@RequiresPermissions("media:series:batchDelete")
	@RequestMapping(value = { "batchDelete" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchDelete(@RequestBody BatchBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<Series> operationObjectList = new ArrayList<Series>();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Series seriesInfo = seriesRepository.findOne(itemId);
			if (seriesInfo != null
					&& seriesInfo.getStatus() != OnlineStatusEnum.ONLINE
							.getKey()) {
				mediaService.deleteSeries(seriesInfo);
				operationObjectList.add(seriesInfo);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformSeriesOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "剧头管理", action = "上线", message = "上线剧头")
	@RequiresPermissions("media:series:online")
	@RequestMapping(value = { "{id}/online" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult online(@PathVariable("id") Long id) {
		String message = "";
		Series operationObjectList = null;
		Series seriesInfo = seriesRepository.findOne(id);
		if (seriesInfo != null) {
			seriesInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
			seriesInfo.setOnlineUser(SecurityUtils.getUserName());
			seriesInfo.setOnlineTime(new Date());
			seriesInfo.setOfflineTime(null);
			mediaService.saveSeries(seriesInfo);
			operationObjectList = seriesInfo;
		} else {
			message = "剧头不存在！";
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformSeriesOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "剧头管理", action = "下线", message = "下线剧头")
	@RequiresPermissions("media:series:offline")
	@RequestMapping(value = { "{id}/offline" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult offline(@PathVariable("id") Long id) {
		String message = "";
		Series operationObjectList = null;
		Series seriesInfo = seriesRepository.findOne(id);
		if (seriesInfo != null) {
			seriesInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
			seriesInfo.setOfflineUser(SecurityUtils.getUserName());
			seriesInfo.setOfflineTime(new Date());
			mediaService.saveSeries(seriesInfo);
			operationObjectList = seriesInfo;
		} else {
			message = "剧头不存在！";
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformSeriesOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "剧头管理", action = "批量上线", message = "批量上线剧头")
	@RequiresPermissions("media:series:batchOnline")
	@RequestMapping(value = { "batchOnline" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchOnline(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<Series> operationObjectList = new ArrayList<Series>();
		Date currentTime = new Date();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Series seriesInfo = seriesRepository.findOne(itemId);
			if (seriesInfo != null) {
				seriesInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
				seriesInfo.setOnlineUser(SecurityUtils.getUserName());
				seriesInfo.setOnlineTime(currentTime);
				seriesInfo.setOfflineTime(null);
				mediaService.saveSeries(seriesInfo);
				operationObjectList.add(seriesInfo);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformSeriesOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "剧头管理", action = "批量下线", message = "批量下线剧头")
	@RequiresPermissions("media:series:batchOffline")
	@RequestMapping(value = { "batchOffline" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchOffline(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<Series> operationObjectList = new ArrayList<Series>();
		Date currentTime = new Date();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Series seriesInfo = seriesRepository.findOne(itemId);
			if (seriesInfo != null) {
				seriesInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
				seriesInfo.setOfflineUser(SecurityUtils.getUserName());
				seriesInfo.setOfflineTime(currentTime);
				mediaService.saveSeries(seriesInfo);
				operationObjectList.add(seriesInfo);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformSeriesOperationObject(operationObjectList));
	}

	@RequiresPermissions("media:series:batchInjection")
	@RequestMapping(value = { "batchInjection" }, method = RequestMethod.GET)
	public String batchInjection(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		setModel(model);

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		if (itemType != null && StringUtils.isNotEmpty(itemIds)) {
			Map<Long, Boolean> canInjectionTemplateIdMap = new HashMap<Long, Boolean>();
			List<MediaTemplate> mediaTemplateList = configService
					.findAllMediaTemplate();
			for (MediaTemplate mediaTemplate : mediaTemplateList) {
				canInjectionTemplateIdMap.put(mediaTemplate.getId(), true);
			}

			List<Long> ids = new ArrayList<Long>();
			for (String id : itemIds.split(",")) {
				ids.add(Long.valueOf(id));
			}
			List<Series> seriesList = seriesRepository.findAll(ids);
			for (Series series : seriesList) {
				String[] templateIdArr = StringUtils.trimToEmpty(
						series.getTemplateId()).split(",");

				for (Long checkId : canInjectionTemplateIdMap.keySet()) {
					boolean existed = false;
					for (String tempId : templateIdArr) {
						if (("" + checkId).equals(tempId)) {
							existed = true;
							break;
						}
					}
					if (!existed) {
						canInjectionTemplateIdMap.put(checkId, false);
					}
				}
			}
			model.addAttribute("canInjectionTemplateIdMap",
					canInjectionTemplateIdMap);
		}

		return "media/series/batchInjection";
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "剧头管理", action = "批量分发", message = "批量分发剧头")
	@RequiresPermissions("media:series:batchInjection")
	@RequestMapping(value = { "batchInjection" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchInjection(@RequestBody BatchInjectionBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<Series> operationObjectList = new ArrayList<Series>();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Series series = seriesRepository.findOne(itemId);
			if (series != null) {
				injectionService.inInjection(series, batchBean.getPlatformId(),
						batchBean.getTemplateId(), batchBean.getPriority());
				operationObjectList.add(series);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformSeriesOperationObject(operationObjectList));
	}

	@RequiresPermissions("media:series:batchOutInjection")
	@RequestMapping(value = { "batchOutInjection" }, method = RequestMethod.GET)
	public String batchOutInjection(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		setModel(model);

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		return "media/series/batchOutInjection";
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "剧头管理", action = "批量回收", message = "批量回收剧头")
	@RequiresPermissions("media:series:batchOutInjection")
	@RequestMapping(value = { "batchOutInjection" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchOutInjection(
			@RequestBody BatchInjectionBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<Series> operationObjectList = new ArrayList<Series>();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Series series = seriesRepository.findOne(itemId);
			if (series != null) {
				injectionService.outInjection(series,
						batchBean.getPlatformId(), batchBean.getTemplateId(),
						batchBean.getPriority());
				operationObjectList.add(series);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformSeriesOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "剧头管理", action = "批量分发", message = "批量重置分发状态")
	@RequiresPermissions("media:series:batchInjection")
	@RequestMapping(value = { "resetInjectionStatus" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult resetInjectionStatus(@RequestBody BatchBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<Series> operationObjectList = new ArrayList<Series>();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Series series = seriesRepository.findOne(itemId);
			if (series != null) {
				injectionService.resetInjectionStatus(series);
				operationObjectList.add(series);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformSeriesOperationObject(operationObjectList));
	}

	@RequiresPermissions("media:series:batchExport")
	@RequestMapping(value = "batchExportMetadata", method = RequestMethod.GET)
	public String batchExportMetadata(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds,
			HttpServletResponse response) {
		try {
			if (itemType == null || StringUtils.isEmpty(itemIds)) {
				return null;
			}

			List<Long> ids = new ArrayList<Long>();
			String[] itemIdArr = itemIds.split(",");
			for (String itemIdStr : itemIdArr) {
				Long itemId = Long.valueOf(itemIdStr);
				ids.add(itemId);
			}
			List<Series> seriess = seriesRepository.findAll(ids);
			getExportMetadataExcel(seriess, response);
			return null;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@RequiresPermissions("media:series:batchExport")
	@RequestMapping(value = { "exportAllMetadata" }, method = RequestMethod.GET)
	public BaseResult exportAllMetadata(Model model,
			HttpServletRequest request, PageInfo pageInfo,
			HttpServletResponse response) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}
		try {
			pageInfo.setSize(100000);
			Page<Series> page = find(request, pageInfo, seriesRepository);
			getExportMetadataExcel(page.getContent(), response);
			return null;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	private boolean getExportMetadataExcel(List<Series> seriess,
			HttpServletResponse response) throws IOException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<Long, Cp> cpMap = configService.findAllCpMap();

		List<SeriesExportLog> logs = new ArrayList<SeriesExportLog>();
		for (Series series : seriess) {
			SeriesExportLog log = new SeriesExportLog();
			if (series.getType() != null) {
				log.setType(SeriesTypeEnum.getEnumByKey(series.getType())
						.getValue());
			}
			log.setEpisodeTotal(series.getEpisodeTotal());
			log.setCpName(configService.getCpShortNameByCpId(cpMap,
					series.getCpId()));

			log.setId(series.getId());
			log.setName(series.getName());
			log.setTitle(series.getTitle());
			log.setCaption(series.getCaption());
			if (series.getContentType() != null) {
				log.setContentType(ContentTypeEnum.getEnumByKey(
						series.getContentType()).getValue());
			}

			log.setDirector(series.getDirector());
			log.setActor(series.getActor());

			log.setYear(series.getYear());
			log.setArea(series.getArea());
			log.setLanguage(series.getLanguage());

			if (series.getRating() != null) {
				log.setRating("" + series.getRating());
			}
			log.setDuration(series.getDuration());
			if (series.getSubtitle() != null) {
				log.setSubtitle(YesNoEnum.getEnumByKey(series.getSubtitle())
						.getValue());
			}

			log.setTag(series.getTag());
			log.setInternalTag(series.getInternalTag());
			log.setKeyword(series.getKeyword());

			log.setViewpoint(series.getViewpoint());
			log.setInfo(series.getInfo());

			if (series.getOrgAirDate() != null) {
				log.setOrgAirDate(DateUtils.formatDate(series.getOrgAirDate(),
						"yyyy-MM-dd HH:mm:ss"));
			}
			log.setBroadcastLicense(series.getBroadcastLicense());
			log.setAuthorizeInfo(series.getAuthorizeInfo());
			log.setAuthorizeAddress(series.getAuthorizeAddress());
			if (series.getLicensingWindowStart() != null) {
				log.setLicensingWindowStart(DateUtils.formatDate(
						series.getLicensingWindowStart(), "yyyy-MM-dd HH:mm:ss"));
			}
			if (series.getLicensingWindowEnd() != null) {
				log.setLicensingWindowEnd(DateUtils.formatDate(
						series.getLicensingWindowEnd(), "yyyy-MM-dd HH:mm:ss"));
			}

			if (StringUtils.isNotEmpty(series.getImage1())) {
				log.setImage1(AdminGlobal.getImageWebPath(series.getImage1()));
			}
			if (StringUtils.isNotEmpty(series.getImage2())) {
				log.setImage2(AdminGlobal.getImageWebPath(series.getImage2()));
			}
			if (StringUtils.isNotEmpty(series.getImage3())) {
				log.setImage3(AdminGlobal.getImageWebPath(series.getImage3()));
			}
			if (StringUtils.isNotEmpty(series.getImage4())) {
				log.setImage4(AdminGlobal.getImageWebPath(series.getImage4()));
			}

			log.setStatus(OnlineStatusEnum.getEnumByKey(series.getStatus())
					.getValue());
			if (series.getOnlineTime() != null) {
				log.setOnlineTime(df.format(series.getOnlineTime()));
			}
			if (series.getOfflineTime() != null) {
				log.setOfflineTime(df.format(series.getOfflineTime()));
			}
			logs.add(log);
		}

		String fileName = "剧头元数据.xlsx";
		new ExportExcel("剧头元数据", "剧头元数据", SeriesExportLog.class, 2,
				SeriesExportLog.DEFAULT).setDataList(logs)
				.write(response, fileName).dispose();
		return true;
	}

	@RequestMapping(value = { "{id}/batchUploadStills" }, method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BatchUploadResult batchUploadStills(Model model,
			@PathVariable("id") Long id) {
		BatchUploadResult result = new BatchUploadResult();
		int type = MediaImageTypeEnum.STILLS.getKey();
		List<MediaImage> mediaImageList = mediaImageRepository
				.findBySeriesIdAndType(id, type);
		for (MediaImage mediaImage : mediaImageList) {
			UploadImageBean uploadImageBean = new UploadImageBean();
			uploadImageBean.setUrl(AdminGlobal.getImageWebPath(mediaImage
					.getFilePath()));
			uploadImageBean.setThumbnailUrl(AdminGlobal
					.getImageWebPath(mediaImage.getFilePath()));
			uploadImageBean.setName(mediaImage.getSourceFileName());
			uploadImageBean.setSize(mediaImage.getFileSize());
			uploadImageBean.setError(null);
			uploadImageBean.setDeleteType("DELETE");
			uploadImageBean.setDeleteUrl(AdminGlobal.getWebAccessUrl()
					+ "/media/series/" + id + "/deleteImage/"
					+ mediaImage.getId());
			result.getFiles().add(uploadImageBean);
		}
		return result;
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "剧头管理", action = "批量上传", message = "批量上传剧照")
	@RequiresPermissions("media:series:edit")
	@RequestMapping(value = { "{id}/batchUploadStills" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BatchUploadResult batchUploadStills(Model model,
			@PathVariable("id") Long id,
			@RequestParam(value = "files[]") MultipartFile[] files) {
		BatchUploadResult result = new BatchUploadResult();
		int type = MediaImageTypeEnum.STILLS.getKey();
		int beginSortIndex = 0;
		Integer maxSortIndex = mediaImageRepository
				.findMaxSortIndexBySeriesIdAndType(id, type);
		if (maxSortIndex != null) {
			beginSortIndex = maxSortIndex.intValue();
		}
		int index = 0;
		for (MultipartFile file : files) {
			index++;
			String filePath = upload(AdminConstants.MODULE_RESOURCE_MEDIA,
					AdminConstants.RESOURCE_TYPE_IMAGE, file);
			String fileName = file.getOriginalFilename();
			MediaImage mediaImage = new MediaImage();
			mediaImage.setSeriesId(id);
			mediaImage.setType(type);
			mediaImage.setSortIndex(beginSortIndex + index);
			mediaImage.setFileSize(file.getSize());
			mediaImage.setFilePath(filePath);
			mediaImage.setSourceFileName(fileName);
			mediaImageRepository.save(mediaImage);

			UploadImageBean uploadImageBean = new UploadImageBean();
			uploadImageBean.setUrl(AdminGlobal.getImageWebPath(mediaImage
					.getFilePath()));
			uploadImageBean.setThumbnailUrl(AdminGlobal
					.getImageWebPath(mediaImage.getFilePath()));
			uploadImageBean.setName(mediaImage.getSourceFileName());
			uploadImageBean.setSize(mediaImage.getFileSize());
			uploadImageBean.setError(null);
			uploadImageBean.setDeleteType("DELETE");
			uploadImageBean.setDeleteUrl(AdminGlobal.getWebAccessUrl()
					+ "/media/series/" + id + "/deleteImage/"
					+ mediaImage.getId());
			result.getFiles().add(uploadImageBean);
		}
		return result;
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "剧头管理", action = "删除", message = "删除剧照")
	@RequiresPermissions("media:series:edit")
	@RequestMapping(value = { "{id}/deleteImage/{imageId}" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BatchUploadResult deleteImage(Model model,
			@PathVariable("id") Long id, @PathVariable("imageId") Long imageId) {
		BatchUploadResult result = new BatchUploadResult();
		MediaImage mediaImage = mediaImageRepository.findOne(imageId);
		if (mediaImage != null) {
			mediaImageRepository.delete(mediaImage);
		}
		return result;
	}

}
