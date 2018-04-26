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
import com.ai.cms.media.bean.MediaFileExportLog;
import com.ai.cms.media.bean.ProgramExportLog;
import com.ai.cms.media.bean.UploadImageBean;
import com.ai.cms.media.entity.MediaFile;
import com.ai.cms.media.entity.MediaImage;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.Series;
import com.ai.cms.media.repository.MediaFileRepository;
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
import com.ai.common.enums.MediaFileTypeEnum;
import com.ai.common.enums.MediaImageTypeEnum;
import com.ai.common.enums.MediaStatusEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.ProgramTypeEnum;
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
@RequestMapping(value = { "/media/program" })
public class ProgramController extends AbstractImageController {

	@Autowired
	protected SeriesRepository seriesRepository;

	@Autowired
	protected ProgramRepository programRepository;

	@Autowired
	protected MediaFileRepository mediaFileRepository;

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
		model.addAttribute("typeEnum", ProgramTypeEnum.values());
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
		if (SecurityUtils.getCpCode() != null) {
			filters.add(new PropertyFilter("cpCode__INMASK_S", ""
					+ SecurityUtils.getCpCode()));
		}
		Specification<Program> specification = SpecificationUtils
				.getSpecification(filters);
		Page<Program> page = find(specification, pageInfo, programRepository);
		model.addAttribute("page", page);

		List<Long> itemIdList = page.getContent().stream().map(s -> s.getId())
				.collect(Collectors.toList());

		List<InjectionObject> injectionObjectListAll = injectionService
				.findInjectionObjectList(InjectionItemTypeEnum.PROGRAM,
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

		return "media/program/list";
	}

	@RequestMapping(value = { "selectItem" })
	public String selectItem(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		list(model, request, pageInfo);

		return "media/program/selectItem";
	}

	@RequestMapping(value = { "selectProgram" })
	public String selectProgram(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		list(model, request, pageInfo);

		return "media/program/selectProgram";
	}

	@RequestMapping(value = { "listBySeriesId" })
	public String listBySeriesId(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("episodeIndex");
		}
		list(model, request, pageInfo);

		String search_seriesId__EQ_L = request
				.getParameter("search_seriesId__EQ_L");
		if (StringUtils.isNotEmpty(search_seriesId__EQ_L)) {
			Series series = seriesRepository.findOne(Long
					.valueOf(search_seriesId__EQ_L));
			model.addAttribute("series", series);
		}

		return "media/program/listBySeriesId";
	}

	@RequiresPermissions("media:program:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model,
			@RequestParam(value = "seriesId", required = false) Long seriesId) {
		Program program = new Program();
		model.addAttribute("program", program);

		if (seriesId != null) {
			Series series = seriesRepository.findOne(seriesId);
			if (series != null) {
				program.setSeriesId(seriesId);
				program.setSeries(series);
				program.setType(ProgramTypeEnum.TV.getKey());
				program.setContentType(series.getContentType());
				model.addAttribute("series", series);
			}
		}

		if (SecurityUtils.getCpCode() != null) {
			model.addAttribute("currentCpCode", SecurityUtils.getCpCode());
		}
		setModel(model);

		return "media/program/edit";
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "节目管理", action = "增加", message = "增加节目")
	@RequiresPermissions("media:program:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody ImageBean<Program> imageBean) {
		return edit(imageBean, null, false, false);
	}

	@RequiresPermissions("media:program:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		Program program = programRepository.findOne(id);
		model.addAttribute("program", program);

		if (program != null && program.getSeriesId() != null) {
			Series series = seriesRepository.findOne(program.getSeriesId());
			model.addAttribute("series", series);
		}

		if (SecurityUtils.getCpCode() != null) {
			model.addAttribute("currentCpCode", SecurityUtils.getCpCode());
		}
		setModel(model);

		return "media/program/edit";
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "节目管理", action = "修改", message = "修改节目")
	@RequiresPermissions("media:program:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody ImageBean<Program> imageBean,
			@PathVariable("id") Long id, boolean notAutoCreate,
			boolean notAutoAudit) {
		String message = "";
		Program operationObjectList = null;

		Program program = imageBean.getData();
		String image1Data = imageBean.getImage1Data();
		String image2Data = imageBean.getImage2Data();
		String image3Data = imageBean.getImage3Data();
		String image4Data = imageBean.getImage4Data();

		Program programInfo = null;
		String image1Old = "";
		String image2Old = "";
		String image3Old = "";
		String image4Old = "";
		if (id != null && id > 0) {
			programInfo = programRepository.findOne(id);
			if (programInfo != null) {
				if (!programInfo.getTitle().equals(program.getTitle())) {
					message = "标题[" + programInfo.getTitle() + "  ==>  "
							+ program.getTitle() + "]";
				}
				image1Old = programInfo.getImage1();
				image2Old = programInfo.getImage2();
				image3Old = programInfo.getImage3();
				image4Old = programInfo.getImage4();
				BeanInfoUtil.bean2bean(program, programInfo, Program.METADATA
						+ "," + Program.POSTER + "");
			}
		}

		if (!notAutoCreate) {
			if (programInfo == null) {
				programInfo = program;
			}

			String image1 = "";
			String image2 = "";
			String image3 = "";
			String image4 = "";
			if (StringUtils.isNotEmpty(image1Data)) {
				image1 = upload(AdminConstants.MODULE_RESOURCE_MEDIA,
						AdminConstants.RESOURCE_TYPE_POSTER, image1Data);
				programInfo.setImage1(image1);
			}
			if (StringUtils.isNotEmpty(image2Data)) {
				image2 = upload(AdminConstants.MODULE_RESOURCE_MEDIA,
						AdminConstants.RESOURCE_TYPE_POSTER, image2Data);
				programInfo.setImage2(image2);
			}
			if (StringUtils.isNotEmpty(image3Data)) {
				image3 = upload(AdminConstants.MODULE_RESOURCE_MEDIA,
						AdminConstants.RESOURCE_TYPE_POSTER, image3Data);
				programInfo.setImage3(image3);
			}
			if (StringUtils.isNotEmpty(image4Data)) {
				image4 = upload(AdminConstants.MODULE_RESOURCE_MEDIA,
						AdminConstants.RESOURCE_TYPE_POSTER, image4Data);
				programInfo.setImage4(image4);
			}
			if (programInfo.getType() != ProgramTypeEnum.TV.getKey()) {
				programInfo.setSeries(null);
				programInfo.setSeriesId(null);
			}
			if (!notAutoAudit) {
				programInfo.setAuditStatus(AuditStatusEnum.AUDIT_FIRST_PASS
						.getKey());
				programInfo.setStorageTime(new Date());
			}
			mediaService.saveProgram(programInfo);
			operationObjectList = programInfo;

			if (!StringUtils.trimToEmpty(image1Old).equals(
					StringUtils.trimToEmpty(program.getImage1()))) {
				deleteOldResource(image1Old);
			}
			if (!StringUtils.trimToEmpty(image2Old).equals(
					StringUtils.trimToEmpty(program.getImage2()))) {
				deleteOldResource(image2Old);
			}
			if (!StringUtils.trimToEmpty(image3Old).equals(
					StringUtils.trimToEmpty(program.getImage3()))) {
				deleteOldResource(image3Old);
			}
			if (!StringUtils.trimToEmpty(image4Old).equals(
					StringUtils.trimToEmpty(program.getImage4()))) {
				deleteOldResource(image4Old);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformProgramOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "节目管理", action = "删除", message = "删除节目")
	@RequiresPermissions("media:program:delete")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		String message = "";
		Program operationObjectList = null;
		Program programInfo = programRepository.findOne(id);
		if (programInfo != null) {
			String image1Old = programInfo.getImage1();
			String image2Old = programInfo.getImage2();
			String image3Old = programInfo.getImage3();
			String image4Old = programInfo.getImage4();

			mediaService.deleteProgram(programInfo);
			operationObjectList = programInfo;

			deleteOldResource(image1Old);
			deleteOldResource(image2Old);
			deleteOldResource(image3Old);
			deleteOldResource(image4Old);
		} else {
			message = "节目不存在！";
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformProgramOperationObject(operationObjectList));
	}

	@RequiresPermissions("media:program:view")
	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable("id") Long id) {
		model.addAttribute("readOnly", "true");
		return toEdit(model, id);
	}

	@RequiresPermissions("media:program:batchEdit")
	@RequestMapping(value = { "batchType" }, method = RequestMethod.GET)
	public String batchType(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		setModel(model);

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		return "media/program/batchType";
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "节目管理", action = "批量修改", message = "批量修改节目")
	@RequiresPermissions("media:program:batchEdit")
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
				|| (type == ProgramTypeEnum.TV.getKey() && seriesId == null)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		Series series = null;
		if (type == ProgramTypeEnum.TV.getKey()) {
			series = seriesRepository.findOne(seriesId);
		}
		String message = "";
		List<Program> operationObjectList = new ArrayList<Program>();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Program programInfo = programRepository.findOne(itemId);
			if (programInfo != null) {
				programInfo.setType(type);
				programInfo.setSeries(series);
				programInfo.setSeriesId(seriesId);
				mediaService.saveProgram(programInfo);
				operationObjectList.add(programInfo);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformProgramOperationObject(operationObjectList));
	}

	@RequiresPermissions("media:program:batchEdit")
	@RequestMapping(value = { "batchChangeMetadata" }, method = RequestMethod.GET)
	public String batchChangeMetadata(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		setModel(model);

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		return "media/program/batchChangeMetadata";
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "节目管理", action = "批量修改", message = "批量修改节目")
	@RequiresPermissions("media:program:batchEdit")
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
		List<Program> operationObjectList = new ArrayList<Program>();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Program programInfo = programRepository.findOne(itemId);
			if (programInfo != null) {
				if (batchBean.getContentTypeSwitch() != null
						&& batchBean.getContentTypeSwitch().equals("on")) {
					programInfo.setContentType(batchBean.getContentType());
				}
				if (batchBean.getCpCodeSwitch() != null
						&& batchBean.getCpCodeSwitch().equals("on")) {
					programInfo.setCpCode(batchBean.getCpCode());
				}
				if (batchBean.getTagSwitch() != null
						&& batchBean.getTagSwitch().equals("on")) {
					programInfo.setTag(batchBean.getTag());
				}
				if (batchBean.getKeywordSwitch() != null
						&& batchBean.getKeywordSwitch().equals("on")) {
					programInfo.setKeyword(batchBean.getKeyword());
				}

				if (batchBean.getDirectorSwitch() != null
						&& batchBean.getDirectorSwitch().equals("on")) {
					programInfo.setDirector(batchBean.getDirector());
				}
				if (batchBean.getActorSwitch() != null
						&& batchBean.getActorSwitch().equals("on")) {
					programInfo.setActor(batchBean.getActor());
				}
				if (batchBean.getCompereSwitch() != null
						&& batchBean.getCompereSwitch().equals("on")) {
					programInfo.setCompere(batchBean.getCompere());
				}
				if (batchBean.getGuestSwitch() != null
						&& batchBean.getGuestSwitch().equals("on")) {
					programInfo.setGuest(batchBean.getGuest());
				}
				if (batchBean.getYearSwitch() != null
						&& batchBean.getYearSwitch().equals("on")) {
					programInfo.setYear(batchBean.getYear());
				}
				if (batchBean.getAreaSwitch() != null
						&& batchBean.getAreaSwitch().equals("on")) {
					programInfo.setArea(batchBean.getArea());
				}
				if (batchBean.getLanguageSwitch() != null
						&& batchBean.getLanguageSwitch().equals("on")) {
					programInfo.setLanguage(batchBean.getLanguage());
				}
				if (batchBean.getKuoZhanOneSwitch() != null
						&& batchBean.getKuoZhanOneSwitch().equals("on")) {
					programInfo.setReserved1(batchBean.getKuoZhanOne());
				}
				if (batchBean.getKuoZhanTwoSwitch() != null
						&& batchBean.getKuoZhanTwoSwitch().equals("on")) {
					programInfo.setReserved2(batchBean.getKuoZhanTwo());
				}
				mediaService.saveProgram(programInfo);
				operationObjectList.add(programInfo);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformProgramOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "节目管理", action = "批量删除", message = "批量删除节目")
	@RequiresPermissions("media:program:batchDelete")
	@RequestMapping(value = { "batchDelete" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchDelete(@RequestBody BatchBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<Program> operationObjectList = new ArrayList<Program>();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Program programInfo = programRepository.findOne(itemId);
			if (programInfo != null
					&& programInfo.getStatus() != OnlineStatusEnum.ONLINE
							.getKey()) {
				mediaService.deleteProgram(programInfo);
				operationObjectList.add(programInfo);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformProgramOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "节目管理", action = "上线", message = "上线节目")
	@RequiresPermissions("media:program:online")
	@RequestMapping(value = { "{id}/online" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult online(@PathVariable("id") Long id) {
		String message = "";
		Program operationObjectList = null;
		Program programInfo = programRepository.findOne(id);
		if (programInfo != null) {
			programInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
			programInfo.setOnlineUser(SecurityUtils.getUserName());
			programInfo.setOnlineTime(new Date());
			programInfo.setOfflineTime(null);
			mediaService.saveProgram(programInfo);
			operationObjectList = programInfo;
		} else {
			message = "节目不存在！";
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformProgramOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "节目管理", action = "下线", message = "下线节目")
	@RequiresPermissions("media:program:offline")
	@RequestMapping(value = { "{id}/offline" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult offline(@PathVariable("id") Long id) {
		String message = "";
		Program operationObjectList = null;
		Program programInfo = programRepository.findOne(id);
		if (programInfo != null) {
			programInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
			programInfo.setOfflineUser(SecurityUtils.getUserName());
			programInfo.setOfflineTime(new Date());
			mediaService.saveProgram(programInfo);
			operationObjectList = programInfo;
		} else {
			message = "节目不存在！";
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformProgramOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "节目管理", action = "批量上线", message = "批量上线节目")
	@RequiresPermissions("media:program:batchOnline")
	@RequestMapping(value = { "batchOnline" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchOnline(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<Program> operationObjectList = new ArrayList<Program>();
		Date currentTime = new Date();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Program programInfo = programRepository.findOne(itemId);
			if (programInfo != null) {
				programInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
				programInfo.setOnlineUser(SecurityUtils.getUserName());
				programInfo.setOnlineTime(currentTime);
				programInfo.setOfflineTime(null);
				mediaService.saveProgram(programInfo);
				operationObjectList.add(programInfo);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformProgramOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "节目管理", action = "批量下线", message = "批量下线节目")
	@RequiresPermissions("media:program:batchOffline")
	@RequestMapping(value = { "batchOffline" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchOffline(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<Program> operationObjectList = new ArrayList<Program>();
		Date currentTime = new Date();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Program programInfo = programRepository.findOne(itemId);
			if (programInfo != null) {
				programInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
				programInfo.setOfflineUser(SecurityUtils.getUserName());
				programInfo.setOfflineTime(currentTime);
				mediaService.saveProgram(programInfo);
				operationObjectList.add(programInfo);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformProgramOperationObject(operationObjectList));
	}

	@RequiresPermissions("media:program:batchInjection")
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
			List<Program> programList = programRepository.findAll(ids);
			for (Program program : programList) {
				String[] templateIdArr = StringUtils.trimToEmpty(
						program.getTemplateId()).split(",");

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

		return "media/program/batchInjection";
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "节目管理", action = "批量分发", message = "批量分发节目")
	@RequiresPermissions("media:program:batchInjection")
	@RequestMapping(value = { "batchInjection" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchInjection(@RequestBody BatchInjectionBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<Program> operationObjectList = new ArrayList<Program>();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Program program = programRepository.findOne(itemId);
			if (program != null) {
				injectionService.inInjection(program.getSeries(), program,
						batchBean.getPlatformId(), batchBean.getTemplateId(),
						batchBean.getPriority(), SecurityUtils.getCpCode());
				operationObjectList.add(program);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformProgramOperationObject(operationObjectList));
	}

	@RequiresPermissions("media:program:batchOutInjection")
	@RequestMapping(value = { "batchOutInjection" }, method = RequestMethod.GET)
	public String batchOutInjection(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		setModel(model);

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		return "media/program/batchOutInjection";
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "节目管理", action = "批量回收", message = "批量回收节目")
	@RequiresPermissions("media:program:batchOutInjection")
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
		List<Program> operationObjectList = new ArrayList<Program>();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Program program = programRepository.findOne(itemId);
			if (program != null) {
				injectionService.outInjection(program.getSeries(), program,
						batchBean.getPlatformId(), batchBean.getTemplateId(),
						batchBean.getPriority(), SecurityUtils.getCpCode());
				operationObjectList.add(program);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformProgramOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "节目管理", action = "批量分发", message = "批量重置分发状态")
	@RequiresPermissions("media:program:batchInjection")
	@RequestMapping(value = { "resetInjectionStatus" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult resetInjectionStatus(@RequestBody BatchBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<Program> operationObjectList = new ArrayList<Program>();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			Program program = programRepository.findOne(itemId);
			if (program != null) {
				injectionService.resetInjectionStatus(program);
				operationObjectList.add(program);
			}
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformProgramOperationObject(operationObjectList));
	}

	@RequiresPermissions("media:program:playCode")
	@RequestMapping(value = { "{id}/playCode" }, method = RequestMethod.GET)
	public String toPlayCode(Model model, @PathVariable("id") Long id) {
		Program program = programRepository.findOne(id);
		model.addAttribute("program", program);

		setModel(model);

		return "media/program/playCode";
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "节目管理", action = "修改", message = "修改播放代码")
	@RequiresPermissions("media:program:playCode")
	@RequestMapping(value = { "{id}/playCode" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult playCode(@RequestBody ImageBean<Program> imageBean,
			@PathVariable("id") Long id) {
		String message = "";
		Program operationObjectList = null;
		Program programInfo = programRepository.findOne(id);
		if (programInfo != null) {
			if (StringUtils.isNotEmpty(imageBean.getData().getPlayCode())) {
				programInfo
						.setPlayCodeStatus(PlayCodeStatusEnum.INPUT.getKey());
			} else {
				programInfo.setPlayCodeStatus(PlayCodeStatusEnum.DEFAULT
						.getKey());
			}
			programInfo.setPlayCode(imageBean.getData().getPlayCode());
			mediaService.saveProgram(programInfo);
			operationObjectList = programInfo;
		} else {
			message = "节目不存在！";
		}
		return new BaseResult().setMessage(message).addOperationObject(
				mediaService
						.transformProgramOperationObject(operationObjectList));
	}

	@RequiresPermissions("media:program:batchExport")
	@RequestMapping(value = "batchExport", method = RequestMethod.GET)
	public String batchExport(Model model,
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
			List<Program> programs = programRepository.findAll(ids);
			getExportExcel(programs, response);
			return null;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@RequiresPermissions("media:program:batchExport")
	@RequestMapping(value = { "exportAll" }, method = RequestMethod.GET)
	public BaseResult exportAll(Model model, HttpServletRequest request,
			PageInfo pageInfo, HttpServletResponse response) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}
		try {
			pageInfo.setSize(50000);
			Page<Program> page = find(request, pageInfo, programRepository);
			getExportExcel(page.getContent(), response);
			return null;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@RequiresPermissions("media:program:batchExport")
	@RequestMapping(value = "batchExportBySeries", method = RequestMethod.GET)
	public String batchExportBySeries(Model model,
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
			List<Program> programTempList = programRepository
					.findBySeriesIdIn(ids);
			List<Program> programs = new ArrayList<Program>();
			for (Long id : ids) {
				for (Program program : programTempList) {
					if (program.getSeriesId() != null
							&& program.getSeriesId().longValue() == id
									.longValue()) {
						programs.add(program);
					}
				}
			}
			getExportExcel(programs, response);
			return null;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@RequiresPermissions("media:program:batchExport")
	@RequestMapping(value = { "exportAllBySeries" }, method = RequestMethod.GET)
	public BaseResult exportAllBySeries(Model model,
			HttpServletRequest request, PageInfo pageInfo,
			HttpServletResponse response) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}
		try {
			pageInfo.setSize(50000);
			Page<Series> page = find(request, pageInfo, seriesRepository);

			List<Long> ids = new ArrayList<Long>();
			for (Series series : page.getContent()) {
				ids.add(series.getId());
			}
			List<Program> programTempList = programRepository
					.findBySeriesIdIn(ids);
			List<Program> programs = new ArrayList<Program>();
			for (Long id : ids) {
				for (Program program : programTempList) {
					if (program.getSeriesId() != null
							&& program.getSeriesId().longValue() == id
									.longValue()) {
						programs.add(program);
					}
				}
			}
			getExportExcel(programs, response);
			return null;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	private boolean getExportExcel(List<Program> programs,
			HttpServletResponse response) throws IOException {
		List<Long> ids = new ArrayList<Long>();
		for (Program program : programs) {
			ids.add(program.getId());
		}

		List<MediaFileExportLog> logs = new ArrayList<MediaFileExportLog>();
		if (ids.size() > 0) {
			List<MediaFile> mediaFiles = mediaFileRepository
					.findByProgramIdListIn(ids);

			for (MediaFile mediaFile : mediaFiles) {
				for (Program program : programs) {
					if (program.getId().longValue() == mediaFile.getProgramId()
							.longValue()) {
						MediaFileExportLog log = new MediaFileExportLog();
						log.setId(mediaFile.getId());
						log.setName(program.getName());
						log.setTitle(program.getTitle());
						if (program.getContentType() != null) {
							log.setContentType(ContentTypeEnum.getEnumByKey(
									program.getContentType()).getValue());
						}
						if (program.getEpisodeIndex() != null) {
							log.setEpisodeIndex("" + program.getEpisodeIndex());
						}

						log.setMediaFileType(MediaFileTypeEnum.getEnumByKey(
								mediaFile.getType()).getValue());
						if (mediaFile.getDuration() != null) {
							log.setDuration("" + mediaFile.getDuration());
						}
						if (StringUtils.isNotEmpty(mediaFile.getFilePath())) {
							log.setFilePath(AdminGlobal
									.getVideoWebPath(mediaFile.getFilePath()));
						}
						if (StringUtils.isNotEmpty(mediaFile.getFileMd5())) {
							log.setFileMd5(mediaFile.getFileMd5());
						}
						if (mediaFile.getFileSize() != null) {
							log.setFileSize("" + mediaFile.getFileSize());
						}
						if (mediaFile.getBitrate() != null) {
							log.setBitrate("" + mediaFile.getBitrate());
						}
						log.setProgramPlayCode(program.getPlayCode());
						log.setPlayCode(mediaFile.getPlayCode());
						logs.add(log);
					}
				}
			}
		}

		String fileName = "媒体内容元数据.xlsx";
		new ExportExcel("媒体内容元数据", "媒体内容元数据", MediaFileExportLog.class, 2,
				MediaFileExportLog.DEFAULT).setDataList(logs)
				.write(response, fileName).dispose();
		return true;
	}

	@RequiresPermissions("media:program:batchExport")
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
			List<Program> programs = programRepository.findAll(ids);
			getExportMetadataExcel(programs, response);
			return null;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@RequiresPermissions("media:program:batchExport")
	@RequestMapping(value = { "exportAllMetadata" }, method = RequestMethod.GET)
	public BaseResult exportAllMetadata(Model model,
			HttpServletRequest request, PageInfo pageInfo,
			HttpServletResponse response) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}
		try {
			pageInfo.setSize(100000);
			Page<Program> page = find(request, pageInfo, programRepository);
			getExportMetadataExcel(page.getContent(), response);
			return null;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	private boolean getExportMetadataExcel(List<Program> programs,
			HttpServletResponse response) throws IOException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, Cp> cpMap = configService.findAllCpMap();
		Map<Long, Series> seriesMap = new HashMap<Long, Series>();
		List<Long> ids = new ArrayList<Long>();
		for (Program program : programs) {
			if (program.getType() == ProgramTypeEnum.TV.getKey()
					&& program.getSeriesId() != null) {
				ids.add(program.getSeriesId());
			}
		}

		if (ids.size() > 0) {
			List<Series> seriesList = seriesRepository.findByListIn(ids);
			for (Series series : seriesList) {
				seriesMap.put(series.getId(), series);
			}
		}

		List<ProgramExportLog> logs = new ArrayList<ProgramExportLog>();
		for (Program program : programs) {
			ProgramExportLog log = new ProgramExportLog();
			log.setType(ProgramTypeEnum.getEnumByKey(program.getType())
					.getValue());
			if (program.getType() == ProgramTypeEnum.TV.getKey()
					&& program.getSeriesId() != null) {
				Series series = seriesMap.get(program.getSeriesId());
				log.setSeriesName(series != null ? series.getName() : "");
			}
			log.setEpisodeIndex(program.getEpisodeIndex());

			log.setCpName(configService.getCpShortNameByCpCode(cpMap,
					program.getCpCode()));

			log.setId(program.getId());
			log.setName(program.getName());
			log.setTitle(program.getTitle());
			log.setCaption(program.getCaption());
			if (program.getContentType() != null) {
				log.setContentType(ContentTypeEnum.getEnumByKey(
						program.getContentType()).getValue());
			}

			log.setDirector(program.getDirector());
			log.setActor(program.getActor());

			log.setYear(program.getYear());
			log.setArea(program.getArea());
			log.setLanguage(program.getLanguage());

			if (program.getRating() != null) {
				log.setRating("" + program.getRating());
			}
			log.setDuration(program.getDuration());
			if (program.getSubtitle() != null) {
				log.setSubtitle(YesNoEnum.getEnumByKey(program.getSubtitle())
						.getValue());
			}

			log.setTag(program.getTag());
			log.setInternalTag(program.getInternalTag());
			log.setKeyword(program.getKeyword());

			log.setViewpoint(program.getViewpoint());
			log.setInfo(program.getInfo());

			if (program.getOrgAirDate() != null) {
				log.setOrgAirDate(DateUtils.formatDate(program.getOrgAirDate(),
						"yyyy-MM-dd HH:mm:ss"));
			}
			log.setBroadcastLicense(program.getBroadcastLicense());
			log.setAuthorizeInfo(program.getAuthorizeInfo());
			log.setAuthorizeAddress(program.getAuthorizeAddress());
			if (program.getLicensingWindowStart() != null) {
				log.setLicensingWindowStart(DateUtils.formatDate(
						program.getLicensingWindowStart(),
						"yyyy-MM-dd HH:mm:ss"));
			}
			if (program.getLicensingWindowEnd() != null) {
				log.setLicensingWindowEnd(DateUtils.formatDate(
						program.getLicensingWindowEnd(), "yyyy-MM-dd HH:mm:ss"));
			}

			if (StringUtils.isNotEmpty(program.getImage1())) {
				log.setImage1(AdminGlobal.getImageWebPath(program.getImage1()));
			}
			if (StringUtils.isNotEmpty(program.getImage2())) {
				log.setImage2(AdminGlobal.getImageWebPath(program.getImage2()));
			}
			if (StringUtils.isNotEmpty(program.getImage3())) {
				log.setImage3(AdminGlobal.getImageWebPath(program.getImage3()));
			}
			if (StringUtils.isNotEmpty(program.getImage4())) {
				log.setImage4(AdminGlobal.getImageWebPath(program.getImage4()));
			}

			log.setStatus(OnlineStatusEnum.getEnumByKey(program.getStatus())
					.getValue());
			if (program.getOnlineTime() != null) {
				log.setOnlineTime(df.format(program.getOnlineTime()));
			}
			if (program.getOfflineTime() != null) {
				log.setOfflineTime(df.format(program.getOfflineTime()));
			}
			logs.add(log);
		}
		String fileName = "节目元数据.xlsx";
		new ExportExcel("节目元数据", "节目元数据", ProgramExportLog.class, 2,
				ProgramExportLog.DEFAULT).setDataList(logs)
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
				.findByProgramIdAndType(id, type);
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
					+ "/media/program/" + id + "/deleteImage/"
					+ mediaImage.getId());
			result.getFiles().add(uploadImageBean);
		}
		return result;
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "节目管理", action = "批量上传", message = "批量上传剧照")
	@RequiresPermissions("media:program:edit")
	@RequestMapping(value = { "{id}/batchUploadStills" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BatchUploadResult batchUploadStills(Model model,
			@PathVariable("id") Long id,
			@RequestParam(value = "files[]") MultipartFile[] files) {
		BatchUploadResult result = new BatchUploadResult();
		int type = MediaImageTypeEnum.STILLS.getKey();
		int beginSortIndex = 0;
		Integer maxSortIndex = mediaImageRepository
				.findMaxSortIndexByProgramIdAndType(id, type);
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
			mediaImage.setProgramId(id);
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
					+ "/media/program/" + id + "/deleteImage/"
					+ mediaImage.getId());
			result.getFiles().add(uploadImageBean);
		}
		return result;
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "节目管理", action = "删除", message = "删除剧照")
	@RequiresPermissions("media:program:edit")
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
