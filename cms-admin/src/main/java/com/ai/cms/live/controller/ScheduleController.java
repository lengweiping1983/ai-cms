package com.ai.cms.live.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ai.AdminGlobal;
import com.ai.cms.injection.entity.InjectionObject;
import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.enums.InjectionItemTypeEnum;
import com.ai.cms.injection.enums.InjectionStatusEnum;
import com.ai.cms.injection.repository.InjectionObjectRepository;
import com.ai.cms.injection.repository.InjectionPlatformRepository;
import com.ai.cms.injection.service.InjectionService;
import com.ai.cms.live.entity.Channel;
import com.ai.cms.live.entity.Schedule;
import com.ai.cms.live.repository.ChannelRepository;
import com.ai.cms.live.repository.ScheduleRepository;
import com.ai.cms.live.service.LiveService;
import com.ai.cms.media.bean.BatchInjectionBean;
import com.ai.cms.media.bean.BatchStatusBean;
import com.ai.cms.media.bean.ImageBean;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.bean.ResultCode;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.*;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;

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

@Controller
@RequestMapping(value = { "/live/schedule" })
public class ScheduleController extends AbstractImageController {

	@Autowired
	private ChannelRepository channelRepository;

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private LiveService liveService;

	@Autowired
	private InjectionPlatformRepository injectionPlatformRepository;
	@Autowired
	private InjectionService injectionService;
	
	@Autowired
	private InjectionObjectRepository injectionObjectRepository;

	private void setModel(Model model) {
		model.addAttribute("statusEnum", OnlineStatusEnum.values());
		model.addAttribute("yesNoEnum", YesNoEnum.values());
		model.addAttribute("injectionStatusEnum", InjectionStatusEnum.values());
	}
	
	public void getData(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		Page<Schedule> page = find(request, pageInfo, scheduleRepository);
		model.addAttribute("page", page);

		List<Long> itemIdList = new ArrayList<Long>();
		if (page != null && page.getContent() != null) {
			for (Schedule schedule : page.getContent()) {
				itemIdList.add(schedule.getId());
			}
		}
		List<InjectionObject> injectionObjectList = injectionObjectRepository
				.findByItemTypeAndItemIdIn(
						InjectionItemTypeEnum.SCHEDULE.getKey(), itemIdList);
		if (page != null && page.getContent() != null) {
			for (Schedule schedule : page.getContent()) {
				boolean found = false;
				for (InjectionObject injectionObject : injectionObjectList) {
					if (schedule.getId().longValue() == injectionObject
							.getItemId().longValue()) {
						found = true;
//						schedule.setInjectionStatus(injectionObject
//								.getInjectionStatus());
//						schedule.setInjectionTime(injectionObject
//								.getInjectionTime());
//						schedule.setPartnerItemCode(injectionObject
//								.getPartnerItemCode());
					}
				}
				if (!found) {
//					schedule.setInjectionStatus(InjectionStatusEnum.WAIT
//							.getKey());
				}
			}
		}
		
		setModel(model);
	}

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-beginTime");
		}
		
		getData(model, request, pageInfo);

		return "live/schedule/list";
	}

	@RequiresPermissions("live:schedule:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model) {
		Schedule schedule = new Schedule();
		schedule.setBeginTime(new Date());
		schedule.setEndTime(new Date());
		model.addAttribute("schedule", schedule);

		setModel(model);

		return "live/schedule/edit";
	}

	@OperationLogAnnotation(module = "直播管理", subModule = "直播节目管理", action = "增加", message = "增加直播节目")
	@RequiresPermissions("live:schedule:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody ImageBean<Schedule> imageBean) {
		return edit(imageBean, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		Schedule schedule = scheduleRepository.findOne(id);
		model.addAttribute("schedule", schedule);

		if (schedule != null && schedule.getChannelId() != null) {
			Channel channel = channelRepository
					.findOne(schedule.getChannelId());
			model.addAttribute("channel", channel);
		}
		if (schedule != null && schedule.getProgramId() != null) {
			Program program = programRepository
					.findOne(schedule.getProgramId());
			model.addAttribute("program", program);
		}

		setModel(model);

		if (schedule.getStatus() == OnlineStatusEnum.ONLINE.getKey()
				|| schedule.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			OnlineStatusEnum[] statusEnum = { OnlineStatusEnum.ONLINE,
					OnlineStatusEnum.OFFLINE };
			model.addAttribute("statusEnum", statusEnum);
		}

		return "live/schedule/edit";
	}

	@OperationLogAnnotation(module = "直播管理", subModule = "直播节目管理", action = "修改", message = "修改直播节目")
	@RequiresPermissions("live:schedule:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody ImageBean<Schedule> imageBean,
			@PathVariable("id") Long id) {
		Schedule schedule = imageBean.getData();
		Schedule scheduleInfo = null;
		if (id == null) {
			scheduleInfo = schedule;
		} else {
			scheduleInfo = scheduleRepository.findOne(schedule.getId());
			if (schedule != null && !scheduleInfo.getProgramName().equals(schedule.getProgramName())) {
				AdminGlobal.operationLogMessage.set("直播节目单修改标题：<br/>" + scheduleInfo.getProgramName() + "  ==>  " + schedule.getProgramName());
			}
			BeanInfoUtil
					.bean2bean(
							schedule,
							scheduleInfo,
							"programName,searchName,genres,viewpoint,updateInfo,"
									+ "episodeIndex,tag,keyword,beginTime,endTime,duration,status,"
									+ "info,splitProgram,mediaId,mediaEpisode,programId,channel,channelId,partnerItemCode,cpCode");
		}
		if (schedule.getChannelId() != null) {
			Channel channel = channelRepository
					.findOne(schedule.getChannelId());
			if (channel != null) {
				scheduleInfo.setCpCode(channel.getCpCode());
			}
		}

		liveService.saveSchedule(scheduleInfo);
		AdminGlobal.operationLogTypeAndId.set(ItemTypeEnum.SCHEDULE.getKey()+"$"+schedule.getId());
		AdminGlobal.operationLogMessage.set("直播节目单修改");
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "直播管理", subModule = "直播节目管理", action = "删除", message = "删除直播节目")
	@RequiresPermissions("live:schedule:delete")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		Schedule scheduleInfo = scheduleRepository.findOne(id);
		if (scheduleInfo != null) {
			liveService.deleteSchedule(scheduleInfo);
			AdminGlobal.operationLogTypeAndId.set(ItemTypeEnum.SCHEDULE.getKey()+"$"+scheduleInfo.getId());
			AdminGlobal.operationLogMessage.set("删除中央频道");
		}
		return new BaseResult();
	}

	@RequiresPermissions("live:schedule:view")
	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable("id") Long id) {
		Schedule schedule = scheduleRepository.findOne(id);
		model.addAttribute("schedule", schedule);

		if (schedule != null && schedule.getChannelId() != null) {
			Channel channel = channelRepository
					.findOne(schedule.getChannelId());
			model.addAttribute("channel", channel);
		}
		if (schedule != null && schedule.getProgramId() != null) {
			Program program = programRepository
					.findOne(schedule.getProgramId());
			model.addAttribute("program", program);
		}

		setModel(model);

		return "live/schedule/detail";
	}

	@OperationLogAnnotation(module = "直播管理", subModule = "直播节目管理", action = "上下线", message = "上下线直播节目")
	@RequiresPermissions("live:schedule:status")
	@RequestMapping(value = { "{id}/status" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult status(@PathVariable("id") Long id) {
		Schedule scheduleInfo = scheduleRepository.findOne(id);
		if (scheduleInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			scheduleInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
		} else {
			scheduleInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
		}
		if (scheduleInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
        	scheduleInfo.setOnlineTime(new Date());
        	scheduleInfo.setOfflineTime(null);
		} else if (scheduleInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			scheduleInfo.setOfflineTime(new Date());
		}
		scheduleRepository.save(scheduleInfo);
		AdminGlobal.operationLogTypeAndId.set(ItemTypeEnum.SCHEDULE.getKey()+"$"+scheduleInfo.getId());
		if (scheduleInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			AdminGlobal.operationLogAction.set("上线");
			AdminGlobal.operationLogMessage.set("上线直播节目");
		} else if (scheduleInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			AdminGlobal.operationLogAction.set("下线");
			AdminGlobal.operationLogMessage.set("下线直播节目");
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "selectItem" })
	public String selectItem(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-beginTime");
		}

		getData(model, request, pageInfo);

		return "live/schedule/selectItem";
	}

	@RequestMapping(value = { "selectSchedule" })
	public String selectSchedule(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-beginTime");
		}

		getData(model, request, pageInfo);

		return "live/schedule/selectSchedule";
	}

	@RequestMapping(value = { "batchInjection" }, method = RequestMethod.GET)
	public String tobatchInjection(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);
		
		List<InjectionPlatform> injectionPlatformList = injectionPlatformRepository.findAll();
		model.addAttribute("injectionPlatformList", injectionPlatformList);

		return "live/schedule/batchInjection";
	}

	@OperationLogAnnotation(module = "直播管理", subModule = "直播节目管理", action = "批量分发", message = "批量分发直播节目")
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
				Schedule schedule = scheduleRepository.findOne(itemId);
//				if (schedule != null) {
//					injectionService.inInjection(schedule, batchBean.getPlatformId(),
//							batchBean.getPriority(), currentTime, currentTime);
//				}
			}
			String typeAndIdStr = "";
			for (String s : itemIdArr) {
				typeAndIdStr += (ItemTypeEnum.SCHEDULE.getKey()+"$"+s+",");
			}
			AdminGlobal.operationLogTypeAndId.set(typeAndIdStr);
			AdminGlobal.operationLogMessage.set("批量分发直播节目");
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "batchOutInjection" }, method = RequestMethod.GET)
	public String batchOutInjection(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		return "live/schedule/batchOutInjection";
	}

	@OperationLogAnnotation(module = "直播管理", subModule = "直播节目管理", action = "批量分发", message = "批量回收直播节目")
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
				Schedule schedule = scheduleRepository.findOne(itemId);
//				if (schedule != null) {
//					injectionService.outInjection(schedule, batchBean.getPlatformId(),
//							batchBean.getPriority(), currentTime, currentTime);
//				}
			}
			String typeAndIdStr = "";
			for (String s : itemIdArr) {
				typeAndIdStr += (ItemTypeEnum.SCHEDULE.getKey()+"$"+s+",");
			}
			AdminGlobal.operationLogTypeAndId.set(typeAndIdStr);
			AdminGlobal.operationLogMessage.set("批量回收直播节目");
		}
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "直播管理", subModule = "直播节目管理", action = "批量分发", message = "重置分发状态")
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
//				List<InjectionObject> injectionObjectList = injectionObjectRepository
//						.findListByItemTypeAndItemId(
//								InjectionItemTypeEnum.SCHEDULE.getKey(), itemId);
//				for(InjectionObject injectionObject:injectionObjectList) { 
//					if (injectionObject != null) {
//						injectionObject.setInjectionStatus(InjectionStatusEnum.WAIT
//								.getKey());
//						injectionObjectRepository.save(injectionObject);
//					}
//				}
			}
			String typeAndIdStr = "";
			for (String s : itemIdArr) {
				typeAndIdStr += (ItemTypeEnum.SCHEDULE.getKey()+"$"+s+",");
			}
			AdminGlobal.operationLogTypeAndId.set(typeAndIdStr);
			AdminGlobal.operationLogMessage.set("重置分发状态直播节目");
		}
		return new BaseResult();
	}
	
}
