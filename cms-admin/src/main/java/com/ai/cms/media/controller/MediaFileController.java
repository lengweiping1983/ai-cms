package com.ai.cms.media.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import com.ai.cms.config.service.ConfigService;
import com.ai.cms.injection.entity.InjectionObject;
import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.enums.InjectionItemTypeEnum;
import com.ai.cms.injection.enums.InjectionStatusEnum;
import com.ai.cms.injection.enums.PlayCodeStatusEnum;
import com.ai.cms.injection.service.InjectionService;
import com.ai.cms.media.bean.BatchBean;
import com.ai.cms.media.bean.BatchStatusBean;
import com.ai.cms.media.bean.ImageBean;
import com.ai.cms.media.entity.MediaFile;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.repository.MediaFileRepository;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.cms.media.repository.SeriesRepository;
import com.ai.cms.media.service.MediaService;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.bean.ResultCode;
import com.ai.common.controller.AbstractController;
import com.ai.common.enums.ContentTypeEnum;
import com.ai.common.enums.MediaFileTypeEnum;
import com.ai.common.enums.MediaStatusEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.ResolutionEnum;
import com.ai.common.enums.SourceEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;
import com.ai.sys.security.SecurityUtils;

@Controller
@RequestMapping(value = { "/media/mediaFile" })
public class MediaFileController extends AbstractController {

	@Autowired
	protected SeriesRepository seriesRepository;

	@Autowired
	protected ProgramRepository programRepository;

	@Autowired
	protected MediaFileRepository mediaFileRepository;

	@Autowired
	protected MediaService mediaService;

	@Autowired
	protected InjectionService injectionService;

	@Autowired
	protected ConfigService configService;

	protected void setModel(Model model) {
		model.addAttribute("statusEnum", OnlineStatusEnum.values());
		model.addAttribute("typeEnum", MediaFileTypeEnum.values());
		model.addAttribute("contentTypeEnum", ContentTypeEnum.toList());

		model.addAttribute("yesNoEnum", YesNoEnum.values());
		model.addAttribute("sourceEnum", SourceEnum.values());

		model.addAttribute("mediaStatusEnum", MediaStatusEnum.toMediaFileList());

		model.addAttribute("injectionStatusEnum", InjectionStatusEnum.values());
		model.addAttribute("playCodeStatusEnum", PlayCodeStatusEnum.values());

		model.addAttribute("cpList", configService.findAllCp());
		model.addAttribute("mediaTemplateList",
				configService.findAllMediaTemplate());
		List<InjectionPlatform> injectionPlatformList = injectionService
				.findAllInjectionPlatform();
		model.addAttribute("injectionPlatformList", injectionPlatformList);
		model.addAttribute("injectionPlatformMap", injectionService
				.findAllInjectionPlatformMap(injectionPlatformList));

		model.addAttribute("resolutionEnum", ResolutionEnum.values());
	}

	@RequiresPermissions("media:mediaFile:view")
	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		Page<MediaFile> page = find(request, pageInfo, mediaFileRepository);
		model.addAttribute("page", page);

		String search_programId__EQ_L = request
				.getParameter("search_programId__EQ_L");
		if (StringUtils.isNotEmpty(search_programId__EQ_L)) {
			Program program = programRepository.findOne(Long
					.valueOf(search_programId__EQ_L));
			model.addAttribute("program", program);
		}

		List<Long> itemIdList = page.getContent().stream().map(s -> s.getId())
				.collect(Collectors.toList());

		List<InjectionObject> injectionObjectListAll = injectionService
				.findInjectionObjectList(InjectionItemTypeEnum.MOVIE,
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

		return "media/mediaFile/list";
	}

	@RequiresPermissions("media:mediaFile:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model,
			@RequestParam(value = "programId", required = false) Long programId) {
		MediaFile mediaFile = new MediaFile();
		model.addAttribute("mediaFile", mediaFile);

		if (programId != null) {
			Program program = programRepository.findOne(programId);
			if (program != null) {
				mediaFile.setProgramId(programId);
				model.addAttribute("program", program);
			}
		}

		setModel(model);

		return "media/mediaFile/edit";
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "媒体内容管理", action = "增加", message = "增加媒体内容")
	@RequiresPermissions("media:mediaFile:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody MediaFile mediaFile) {
		return edit(mediaFile, null);
	}

	@RequiresPermissions("media:mediaFile:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		MediaFile mediaFile = mediaFileRepository.findOne(id);
		model.addAttribute("mediaFile", mediaFile);

		if (mediaFile.getProgramId() != null) {
			Program program = programRepository.findOne(mediaFile
					.getProgramId());
			if (program != null) {
				model.addAttribute("program", program);
			}
		}

		setModel(model);

		return "media/mediaFile/edit";
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "媒体内容管理", action = "修改", message = "修改媒体内容")
	@RequiresPermissions("media:mediaFile:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody MediaFile mediaFile,
			@PathVariable("id") Long id) {
		String message = "";
		MediaFile operationObjectList = null;

		MediaFile mediaFileInfo = null;
		String oldFilePath = "";
		String newFilePath = mediaFile.getFilePath();
		Long oldTemplateId = null;
		Long newTemplateId = mediaFile.getTemplateId();
		if (id == null) {
			mediaFileInfo = mediaFile;
		} else {
			mediaFileInfo = mediaFileRepository.findOne(mediaFile.getId());
			oldFilePath = mediaFileInfo.getFilePath();
			oldTemplateId = mediaFileInfo.getTemplateId();
			if (!StringUtils.trimToEmpty(oldFilePath).equals(
					StringUtils.trimToEmpty(newFilePath))
					|| oldTemplateId != newTemplateId) {
				message = "播放地址[" + oldFilePath + "  ==>  "
						+ mediaFile.getFilePath() + "]";
				message += "码率[" + oldTemplateId + "  ==>  "
						+ mediaFile.getTemplateId() + "]";
			}
			BeanInfoUtil
					.bean2bean(mediaFile, mediaFileInfo, MediaFile.METADATA);
		}
		if (!StringUtils.trimToEmpty(oldFilePath).equals(
				StringUtils.trimToEmpty(newFilePath))
				|| oldTemplateId != newTemplateId) {
			mediaFileInfo.setFilePath(oldFilePath);
			mediaFileInfo.setTemplateId(oldTemplateId);
			mediaService.saveMediaFileAndMediaStatus(mediaFileInfo,
					newFilePath, newTemplateId);
		} else {
			mediaService.saveMediaFile(mediaFileInfo);
		}
		operationObjectList = mediaFileInfo;
		return new BaseResult()
				.setMessage(message)
				.addOperationObject(
						mediaService
								.transformMediaFileOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "媒体内容管理", action = "删除", message = "删除媒体内容")
	@RequiresPermissions("media:mediaFile:delete")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		String message = "";
		MediaFile operationObjectList = null;

		MediaFile mediaFileInfo = mediaFileRepository.findOne(id);
		if (mediaFileInfo != null) {
			mediaService.deleteMediaFile(mediaFileInfo);
			operationObjectList = mediaFileInfo;
		} else {
			message = "媒体内容不存在！";
		}
		return new BaseResult()
				.setMessage(message)
				.addOperationObject(
						mediaService
								.transformMediaFileOperationObject(operationObjectList));
	}

	@RequiresPermissions("media:mediaFile:view")
	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable("id") Long id) {
		model.addAttribute("readOnly", "true");
		return toEdit(model, id);
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "媒体内容管理", action = "批量删除", message = "批量删除媒体内容")
	@RequiresPermissions("media:mediaFile:batchDelete")
	@RequestMapping(value = { "batchDelete" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchDelete(@RequestBody BatchBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<MediaFile> operationObjectList = new ArrayList<MediaFile>();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			MediaFile mediaFileInfo = mediaFileRepository.findOne(itemId);
			if (mediaFileInfo != null
					&& mediaFileInfo.getStatus() != OnlineStatusEnum.ONLINE
							.getKey()) {
				mediaService.deleteMediaFile(mediaFileInfo);
				operationObjectList.add(mediaFileInfo);
			}
		}
		return new BaseResult()
				.setMessage(message)
				.addOperationObject(
						mediaService
								.transformMediaFileOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "媒体内容管理", action = "上线", message = "上线媒体内容")
	@RequiresPermissions("media:mediaFile:online")
	@RequestMapping(value = { "{id}/online" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult online(@PathVariable("id") Long id) {
		String message = "";
		MediaFile operationObjectList = null;
		MediaFile mediaFileInfo = mediaFileRepository.findOne(id);
		if (mediaFileInfo != null) {
			mediaFileInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
			mediaFileInfo.setOnlineUser(SecurityUtils.getUserName());
			mediaFileInfo.setOnlineTime(new Date());
			mediaFileInfo.setOfflineTime(null);
			mediaService.saveMediaFile(mediaFileInfo);
			operationObjectList = mediaFileInfo;
		} else {
			message = "媒体内容不存在！";
		}
		return new BaseResult()
				.setMessage(message)
				.addOperationObject(
						mediaService
								.transformMediaFileOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "媒体内容管理", action = "下线", message = "下线媒体内容")
	@RequiresPermissions("media:mediaFile:offline")
	@RequestMapping(value = { "{id}/offline" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult offline(@PathVariable("id") Long id) {
		String message = "";
		MediaFile operationObjectList = null;
		MediaFile mediaFileInfo = mediaFileRepository.findOne(id);
		if (mediaFileInfo != null) {
			mediaFileInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
			mediaFileInfo.setOfflineUser(SecurityUtils.getUserName());
			mediaFileInfo.setOfflineTime(new Date());
			mediaService.saveMediaFile(mediaFileInfo);
			operationObjectList = mediaFileInfo;
		} else {
			message = "媒体内容不存在！";
		}
		return new BaseResult()
				.setMessage(message)
				.addOperationObject(
						mediaService
								.transformMediaFileOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "媒体内容管理", action = "批量上线", message = "批量上线媒体内容")
	@RequiresPermissions("media:mediaFile:batchOnline")
	@RequestMapping(value = { "batchOnline" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchOnline(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<MediaFile> operationObjectList = new ArrayList<MediaFile>();
		Date currentTime = new Date();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			MediaFile mediaFileInfo = mediaFileRepository.findOne(itemId);
			if (mediaFileInfo != null) {
				mediaFileInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
				mediaFileInfo.setOnlineUser(SecurityUtils.getUserName());
				mediaFileInfo.setOnlineTime(currentTime);
				mediaFileInfo.setOfflineTime(null);
				mediaService.saveMediaFile(mediaFileInfo);
				operationObjectList.add(mediaFileInfo);
			}
		}
		return new BaseResult()
				.setMessage(message)
				.addOperationObject(
						mediaService
								.transformMediaFileOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "媒体内容管理", action = "批量下线", message = "批量下线媒体内容")
	@RequiresPermissions("media:mediaFile:batchOffline")
	@RequestMapping(value = { "batchOffline" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchOffline(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String message = "";
		List<MediaFile> operationObjectList = new ArrayList<MediaFile>();
		Date currentTime = new Date();
		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			MediaFile mediaFileInfo = mediaFileRepository.findOne(itemId);
			if (mediaFileInfo != null) {
				mediaFileInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
				mediaFileInfo.setOfflineUser(SecurityUtils.getUserName());
				mediaFileInfo.setOfflineTime(currentTime);
				mediaService.saveMediaFile(mediaFileInfo);
				operationObjectList.add(mediaFileInfo);
			}
		}
		return new BaseResult()
				.setMessage(message)
				.addOperationObject(
						mediaService
								.transformMediaFileOperationObject(operationObjectList));
	}

	@RequiresPermissions("media:program:playCode")
	@RequestMapping(value = { "{id}/playCode" }, method = RequestMethod.GET)
	public String toPlayCode(Model model, @PathVariable("id") Long id) {
		MediaFile mediaFile = mediaFileRepository.findOne(id);
		model.addAttribute("mediaFile", mediaFile);

		setModel(model);

		return "media/mediaFile/playCode";
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "媒体内容管理", action = "修改", message = "修改播放代码")
	@RequiresPermissions("media:mediaFile:playCode")
	@RequestMapping(value = { "{id}/playCode" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult playCode(@RequestBody ImageBean<MediaFile> imageBean,
			@PathVariable("id") Long id) {
		String message = "";
		MediaFile operationObjectList = null;
		MediaFile mediaFileInfo = mediaFileRepository.findOne(id);
		if (mediaFileInfo != null) {
			if (StringUtils.isNotEmpty(imageBean.getData().getPlayCode())) {
				mediaFileInfo.setPlayCodeStatus(PlayCodeStatusEnum.INPUT
						.getKey());
			} else {
				mediaFileInfo.setPlayCodeStatus(PlayCodeStatusEnum.DEFAULT
						.getKey());
			}
			mediaFileInfo.setPlayCode(imageBean.getData().getPlayCode());
			mediaFileInfo.setGlobalCode(imageBean.getData().getGlobalCode());
			mediaService.saveMediaFileAndMediaStatus(mediaFileInfo,
					mediaFileInfo.getFilePath(), mediaFileInfo.getTemplateId());
			operationObjectList = mediaFileInfo;
		} else {
			message = "媒体内容不存在！";
		}
		return new BaseResult()
				.setMessage(message)
				.addOperationObject(
						mediaService
								.transformMediaFileOperationObject(operationObjectList));
	}
}
