package com.ai.cms.transcode.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
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

import com.ai.cms.config.entity.Cp;
import com.ai.cms.config.entity.CpFtp;
import com.ai.cms.config.entity.MediaTemplate;
import com.ai.cms.config.service.ConfigService;
import com.ai.cms.media.bean.BatchBean;
import com.ai.cms.media.bean.BatchTranscodeBean;
import com.ai.cms.transcode.entity.TranscodeRequest;
import com.ai.cms.transcode.entity.TranscodeRequestImportLog;
import com.ai.cms.transcode.enums.GenProgramNameRuleEnum;
import com.ai.cms.transcode.enums.GenTaskModeStatusEnum;
import com.ai.cms.transcode.enums.NeedSnapshotEnum;
import com.ai.cms.transcode.enums.OriginFileDealEnum;
import com.ai.cms.transcode.enums.TranscodeRequestStatusEnum;
import com.ai.cms.transcode.enums.TranscodeRequestTypeEnum;
import com.ai.cms.transcode.repository.TranscodeRequestRepository;
import com.ai.cms.transcode.service.TranscodeService;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.OperationObject;
import com.ai.common.bean.PageInfo;
import com.ai.common.bean.ResultCode;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.ContentTypeEnum;
import com.ai.common.enums.MediaFileTypeEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.excel.ExportExcel;
import com.ai.common.exception.ServiceException;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;
import com.ai.sys.security.SecurityUtils;

@Controller
@RequestMapping(value = { "/transcode/transcodeRequest" })
public class TranscodeRequestController extends AbstractImageController {
	String PAGE_LIST = "transcode/transcodeRequest/list";

	String PAGE_EDIT = "transcode/transcodeRequest/edit";
	String PAGE_BATCH_EDIT = "transcode/transcodeRequest/batchEdit";
	String PAGE_EDIT_TV = "transcode/transcodeRequest/editTv";

	@Autowired
	private TranscodeRequestRepository transcodeRequestRepository;

	@Autowired
	private TranscodeService transcodeService;

	@Autowired
	private ConfigService configService;

	private CacheManager cacheManager;

	private Cache transcodeRequestCache;

	public CacheManager getCacheManager(CpFtp cpFtp) {
		return cacheManager;
	}

	@Autowired
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
		this.transcodeRequestCache = cacheManager
				.getCache("TranscodeRequestController.transcodeRequestCache");
	}

	public TranscodeRequest getCacheTranscodeRequest() {
		String cacheKey = SecurityUtils.getUserId() + "-"
				+ StringUtils.trimToEmpty(SecurityUtils.getCpCode());
		ValueWrapper valueWrapper = transcodeRequestCache.get(cacheKey);
		if (valueWrapper != null) {
			TranscodeRequest transcodeRequest = (TranscodeRequest) valueWrapper
					.get();
			return transcodeRequest;
		}
		return null;
	}

	public void putCacheTranscodeRequest(TranscodeRequest transcodeRequest) {
		String cacheKey = SecurityUtils.getUserId() + "-"
				+ StringUtils.trimToEmpty(SecurityUtils.getCpCode());
		transcodeRequestCache.put(cacheKey, transcodeRequest);
	}

	private void setModel(Model model) {
		model.addAttribute("statusEnum", TranscodeRequestStatusEnum.values());
		model.addAttribute("typeEnum", TranscodeRequestTypeEnum.values());

		model.addAttribute("contentTypeEnum", ContentTypeEnum.toList());
		model.addAttribute("mediaFileTypeEnum", MediaFileTypeEnum.values());

		model.addAttribute("yesNoEnum", YesNoEnum.values());

		model.addAttribute("genTaskModeStatusEnum",
				GenTaskModeStatusEnum.values());
		model.addAttribute("genProgramNameRuleEnum",
				GenProgramNameRuleEnum.values());

		model.addAttribute("needSnapshotEnum", NeedSnapshotEnum.values());
		model.addAttribute("originFileDealEnum", OriginFileDealEnum.values());

		model.addAttribute("cpList", configService.findAllCp());
		model.addAttribute("mediaTemplateList",
				configService.findAllMediaTemplate());
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
		Specification<TranscodeRequest> specification = SpecificationUtils
				.getSpecification(filters);
		Page<TranscodeRequest> page = find(specification, pageInfo,
				transcodeRequestRepository);
		// Page<TranscodeRequest> page = find(request, pageInfo,
		// transcodeRequestRepository);
		model.addAttribute("page", page);

		setModel(model);

		return PAGE_LIST;
	}

	@RequestMapping(value = { "add/{type}" }, method = RequestMethod.GET)
	public String toAdd(Model model, @PathVariable("type") Integer type) {
		TranscodeRequest transcodeRequest = new TranscodeRequest();
		model.addAttribute("transcodeRequest", transcodeRequest);

		transcodeRequest.setType(type);

		TranscodeRequest cacheTranscodeRequest = getCacheTranscodeRequest();
		if (cacheTranscodeRequest != null) {
			transcodeRequest.setTemplateId(cacheTranscodeRequest
					.getTemplateId());
			transcodeRequest.setGenTask(cacheTranscodeRequest.getGenTask());
			transcodeRequest.setGenProgramNameRule(cacheTranscodeRequest
					.getGenProgramNameRule());
		}

		setModel(model);

		if (SecurityUtils.getCpCode() != null) {
			model.addAttribute("currentCpCode", SecurityUtils.getCpCode());
		}

		TranscodeRequestTypeEnum typeEnum = TranscodeRequestTypeEnum
				.getEnumByKey(type);
		switch (typeEnum) {
		case TV:
			transcodeRequest.setContentType(ContentTypeEnum.TV.getKey());
			return PAGE_EDIT_TV;
		case BATCH_MOVIE:
			return PAGE_BATCH_EDIT;
		default:
			return PAGE_EDIT;
		}
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		TranscodeRequest transcodeRequest = transcodeRequestRepository
				.findOne(id);
		model.addAttribute("transcodeRequest", transcodeRequest);

		setModel(model);

		if (SecurityUtils.getCpCode() != null) {
			model.addAttribute("currentCpCode", SecurityUtils.getCpCode());
		}

		TranscodeRequestTypeEnum typeEnum = TranscodeRequestTypeEnum
				.getEnumByKey(transcodeRequest.getType());
		switch (typeEnum) {
		case TV:
			return PAGE_EDIT_TV;
		case BATCH_MOVIE:
			return PAGE_BATCH_EDIT;
		default:
			return PAGE_EDIT;
		}
	}

	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable("id") Long id) {
		model.addAttribute("readOnly", "true");
		return toEdit(model, id);
	}

	@OperationLogAnnotation(module = "媒资生产", subModule = "转码工单管理", action = "增加", message = "增加工单")
	@RequiresPermissions("transcode:transcodeRequest:add")
	@RequestMapping(value = { "add" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody TranscodeRequest transcodeRequest) {
		return edit(transcodeRequest);
	}

	@OperationLogAnnotation(module = "媒资生产", subModule = "转码工单管理", action = "修改", message = "修改工单")
	@RequiresPermissions("transcode:transcodeRequest:edit")
	@RequestMapping(value = { "edit" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody TranscodeRequest transcodeRequest) {
		String message = "";
		TranscodeRequest operationObjectList = null;

		transcodeService.updateTranscodeRequest(transcodeRequest, false);
		operationObjectList = transcodeRequest;
		putCacheTranscodeRequest(transcodeRequest);

		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资生产", subModule = "转码工单管理", action = "执行", message = "执行工单")
	@RequiresPermissions("transcode:transcodeRequest:produce")
	@RequestMapping(value = { "produce" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult produce(@RequestBody TranscodeRequest transcodeRequest) {
		long startTime = System.currentTimeMillis();

		String message = "";
		TranscodeRequest operationObjectList = null;

		transcodeService.updateTranscodeRequest(transcodeRequest, true);
		operationObjectList = transcodeRequest;
		putCacheTranscodeRequest(transcodeRequest);

		logger.info("produce run time="
				+ (System.currentTimeMillis() - startTime) + "ms.");
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资生产", subModule = "转码工单管理", action = "批量执行", message = "批量执行工单")
	@RequiresPermissions("transcode:transcodeRequest:batchProduce")
	@RequestMapping(value = { "batchProduce" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchProduce(@RequestBody BatchBean batchBean) {
		long startTime = System.currentTimeMillis();
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}

		String message = "";
		List<TranscodeRequest> operationObjectList = new ArrayList<TranscodeRequest>();

		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			TranscodeRequest transcodeRequest = transcodeRequestRepository
					.findOne(itemId);
			transcodeService.updateTranscodeRequest(transcodeRequest, true);
			operationObjectList.add(transcodeRequest);
		}
		logger.info("batch produce run time="
				+ (System.currentTimeMillis() - startTime) + "ms.");
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "媒资生产", subModule = "转码工单管理", action = "删除", message = "删除工单")
	@RequiresPermissions("transcode:transcodeRequest:delete")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		String message = "";
		TranscodeRequest operationObjectList = null;

		TranscodeRequest transcodeRequest = transcodeRequestRepository
				.findOne(id);
		if (transcodeRequest != null) {
			transcodeService.deleteTranscodeRequest(transcodeRequest);
			operationObjectList = transcodeRequest;
		}

		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@RequestMapping(value = { "batchCopy" }, method = RequestMethod.GET)
	public String toBatchCopy(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		setModel(model);

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		return "transcode/transcodeRequest/batchCopy";
	}

	@OperationLogAnnotation(module = "媒资生产", subModule = "转码工单管理", action = "批量复制", message = "批量复制工单")
	@RequiresPermissions("transcode:transcodeRequest:batchCopy")
	@RequestMapping(value = { "batchCopy" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchCopy(@RequestBody BatchTranscodeBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}

		String message = "";
		List<TranscodeRequest> operationObjectList = new ArrayList<TranscodeRequest>();

		String[] itemIdArr = itemIds.split(",");
		for (String itemIdStr : itemIdArr) {
			Long itemId = Long.valueOf(itemIdStr);
			TranscodeRequest transcodeRequest = transcodeRequestRepository
					.findOne(itemId);
			transcodeService.copyTranscodeRequest(transcodeRequest,
					batchBean.getTemplateId());
			operationObjectList.add(transcodeRequest);
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@RequiresPermissions("transcode:transcodeRequest:batchExport")
	@RequestMapping(value = "batchExport", method = RequestMethod.GET)
	public String batchExport(Model model,
			@RequestParam(value = "itemIds") String itemIds,
			HttpServletResponse response) {
		try {
			if (StringUtils.isEmpty(itemIds)) {
				return null;
			}
			List<Long> ids = new ArrayList<Long>();
			String[] itemIdArr = itemIds.split(",");
			for (String itemIdStr : itemIdArr) {
				Long itemId = Long.valueOf(itemIdStr);
				ids.add(itemId);
			}
			List<TranscodeRequest> transcodeRequests = transcodeRequestRepository
					.findAll(ids);
			getExportExcel(transcodeRequests, response);
			return null;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@RequiresPermissions("transcode:transcodeRequest:batchExport")
	@RequestMapping(value = { "exportAll" }, method = RequestMethod.GET)
	public BaseResult exportAll(Model model, HttpServletRequest request,
			PageInfo pageInfo, HttpServletResponse response) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}
		try {
			pageInfo.setSize(50000);
			Page<TranscodeRequest> page = find(request, pageInfo,
					transcodeRequestRepository);
			getExportExcel(page.getContent(), response);
			return null;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	private boolean getExportExcel(List<TranscodeRequest> transcodeRequests,
			HttpServletResponse response) throws IOException {
		List<Long> ids = new ArrayList<Long>();
		for (TranscodeRequest transcodeRequest : transcodeRequests) {
			ids.add(transcodeRequest.getId());
		}

		Map<String, Cp> cpMap = configService.findAllCpMap();
		Map<Long, MediaTemplate> mediaTemplateMap = configService
				.findAllMediaTemplateMap();

		List<TranscodeRequestImportLog> logs = new ArrayList<TranscodeRequestImportLog>();

		if (ids.size() > 0) {
			for (TranscodeRequest transcodeRequest : transcodeRequests) {
				TranscodeRequestImportLog log = new TranscodeRequestImportLog();

				BeanInfoUtil
						.bean2bean(
								transcodeRequest,
								log,
								"name,mediaName,tag,internalTag,episodeTotal,priority,taskTotal,taskSuccess,taskFail");
				log.setType(TranscodeRequestTypeEnum.getEnumByKey(
						transcodeRequest.getType()).getValue());
				log.setContentType(ContentTypeEnum.getEnumByKey(
						transcodeRequest.getContentType()).getValue());
				log.setCpName(configService.getCpNameByCpCode(cpMap,
						transcodeRequest.getCpCode()));
				log.setTemplateTitle(configService
						.getTemplateTitleByTemplateId(mediaTemplateMap,
								transcodeRequest.getTemplateId()));
				log.setStatus(TranscodeRequestStatusEnum.getEnumByKey(
						transcodeRequest.getStatus()).getValue());
				logs.add(log);
			}
		}

		String fileName = "转码工单.xlsx";
		new ExportExcel("转码工单", "转码工单", TranscodeRequestImportLog.class, 2,
				TranscodeRequestImportLog.DEFAULT).setDataList(logs)
				.write(response, fileName).dispose();
		return true;
	}

	public List<OperationObject> transformOperationObject(
			List<TranscodeRequest> transcodeRequestList) {
		if (transcodeRequestList == null || transcodeRequestList.size() <= 0) {
			return null;
		}
		List<OperationObject> list = new ArrayList<OperationObject>();
		for (TranscodeRequest transcodeRequest : transcodeRequestList) {
			OperationObject operationObject = new OperationObject();
			operationObject.setId(transcodeRequest.getId());
			operationObject.setName(transcodeRequest.getName());
			list.add(operationObject);
		}
		return list;
	}

	public OperationObject transformOperationObject(
			TranscodeRequest transcodeRequest) {
		if (transcodeRequest == null) {
			return null;
		}
		OperationObject operationObject = new OperationObject();
		operationObject.setId(transcodeRequest.getId());
		operationObject.setName(transcodeRequest.getName());
		return operationObject;
	}
}
