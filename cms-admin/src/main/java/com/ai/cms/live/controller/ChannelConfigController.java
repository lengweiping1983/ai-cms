package com.ai.cms.live.controller;

import javax.servlet.http.HttpServletRequest;

import com.ai.AdminConstants;
import com.ai.AdminGlobal;
import com.ai.cms.injection.enums.ProviderTypeEnum;
import com.ai.cms.live.entity.Channel;
import com.ai.cms.live.entity.ChannelConfig;
import com.ai.cms.live.repository.ChannelConfigRepository;
import com.ai.cms.live.repository.ChannelRepository;
import com.ai.cms.live.service.LiveService;
import com.ai.cms.media.bean.ImageBean;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.ItemTypeEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.exception.ServiceException;
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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = { "/live/channelConfig" })
public class ChannelConfigController extends AbstractImageController {

	@Autowired
	private ChannelRepository channelRepository;

	@Autowired
	private ChannelConfigRepository channelConfigRepository;

	@Autowired
	private LiveService liveService;

	private void setModel(Model model) {
		model.addAttribute("statusEnum", OnlineStatusEnum.values());
		model.addAttribute("yesNoEnum", YesNoEnum.values());
		model.addAttribute("providerTypeEnum", ProviderTypeEnum.values());
	}

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("sortIndex");
		}
		Page<ChannelConfig> page = find(request, pageInfo,
				channelConfigRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "live/channelConfig/list";
	}

	@RequiresPermissions("live:channelConfig:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model) {
		ChannelConfig channelConfig = new ChannelConfig();
		model.addAttribute("channelConfig", channelConfig);

		setModel(model);

		return "live/channelConfig/edit";
	}

	@OperationLogAnnotation(module = "直播管理", subModule = "频道配置管理", action = "增加", message = "增加频道配置")
	@RequiresPermissions("live:channelConfig:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody ImageBean<ChannelConfig> imageBean) {
		return edit(imageBean, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		ChannelConfig channelConfig = channelConfigRepository.findOne(id);
		model.addAttribute("channelConfig", channelConfig);

		if (channelConfig != null && channelConfig.getChannelId() != null) {
			Channel channel = channelRepository.findOne(channelConfig
					.getChannelId());
			model.addAttribute("channel", channel);
		}

		setModel(model);

		if (channelConfig.getStatus() == OnlineStatusEnum.ONLINE.getKey()
				|| channelConfig.getStatus() == OnlineStatusEnum.OFFLINE
						.getKey()) {
			OnlineStatusEnum[] statusEnum = { OnlineStatusEnum.ONLINE,
					OnlineStatusEnum.OFFLINE };
			model.addAttribute("statusEnum", statusEnum);
		}

		return "live/channelConfig/edit";
	}

	@OperationLogAnnotation(module = "直播管理", subModule = "频道配置管理", action = "修改", message = "修改频道配置")
	@RequiresPermissions("live:channelConfig:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody ImageBean<ChannelConfig> imageBean,
			@PathVariable("id") Long id) {
		ChannelConfig channelConfig = imageBean.getData();
		String image1Data = imageBean.getImage1Data();
		String image2Data = imageBean.getImage2Data();

		ChannelConfig channelConfigInfo = null;
		String image1Old = "";
		String image2Old = "";
		if (id == null) {
			channelConfigInfo = channelConfig;
			if (channelConfig != null && !channelConfigInfo.getPartnerChannelName().equals(channelConfig.getPartnerChannelName())) {
				AdminGlobal.operationLogMessage.set("频道配置修改标题：<br/>" + channelConfigInfo.getPartnerChannelName() + "  ==>  " + channelConfig.getPartnerChannelName());
			}
			ChannelConfig existChannelConfig = channelConfigRepository
					.findByChannelIdAndProviderType(
							channelConfig.getChannelId(),
							channelConfig.getProviderType());
			if (existChannelConfig != null) {
				throw new ServiceException("该频道已存在！");
			}
		} else {
			ChannelConfig existChannelConfig = channelConfigRepository
					.findByChannelIdAndProviderType(
							channelConfig.getChannelId(),
							channelConfig.getProviderType());
			if (existChannelConfig != null
					&& existChannelConfig.getId() != id.longValue()) {
				throw new ServiceException("该频道已存在！");
			}

			channelConfigInfo = channelConfigRepository.findOne(id);
			image1Old = channelConfigInfo.getImage1();
			image2Old = channelConfigInfo.getImage2();

			BeanInfoUtil
					.bean2bean(
							channelConfig,
							channelConfigInfo,
							"providerType,partnerChannelCode,partnerChannelName,partnerChannelNumber,playByChannelNumber,"
									+ "url,liveEncrypt,timeShift,timeShiftDuration,timeShiftUrl,timeShiftEncrypt,lookBack,lookBackUrl,lookBackEncrypt,"
									+ "enbodyDuration,lookBackDuration,preTime,sufTime,"
									+ "image1,image2,status,sortIndex,info,channel,channelId");
		}

		String image1 = "";
		String image2 = "";
		if (StringUtils.isNotEmpty(image1Data)) {
			image1 = upload(AdminConstants.MODULE_RESOURCE_LIVE,
					AdminConstants.RESOURCE_TYPE_POSTER, image1Data);
			channelConfigInfo.setImage1(image1);
		}
		if (StringUtils.isNotEmpty(image2Data)) {
			image2 = upload(AdminConstants.MODULE_RESOURCE_LIVE,
					AdminConstants.RESOURCE_TYPE_POSTER, image2Data);
			channelConfigInfo.setImage2(image2);
		}
		liveService.saveChannelConfig(channelConfigInfo);

		if (!StringUtils.trimToEmpty(image1Old).equals(
				StringUtils.trimToEmpty(channelConfig.getImage1()))) {
			// deleteOldResource(image1Old);
		}

		if (!StringUtils.trimToEmpty(image2Old).equals(
				StringUtils.trimToEmpty(channelConfig.getImage2()))) {
			// deleteOldResource(image2Old);
		}
		AdminGlobal.operationLogTypeAndId.set("101$"+channelConfig.getId());
		AdminGlobal.operationLogMessage.set("修改频道配置");
		return new BaseResult();
	}

	@OperationLogAnnotation(module = "直播管理", subModule = "频道配置管理", action = "删除", message = "删除频道配置")
	@RequiresPermissions("live:channelConfig:delete")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		ChannelConfig channelConfigInfo = channelConfigRepository.findOne(id);
		if (channelConfigInfo != null) {
			liveService.deleteChannelConfig(channelConfigInfo);
			AdminGlobal.operationLogTypeAndId.set("101$"+channelConfigInfo.getId());
			AdminGlobal.operationLogMessage.set("删除频道配置");
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable("id") Long id) {
		ChannelConfig channelConfig = channelConfigRepository.findOne(id);
		model.addAttribute("channelConfig", channelConfig);

		if (channelConfig != null && channelConfig.getChannelId() != null) {
			Channel channel = channelRepository.findOne(channelConfig
					.getChannelId());
			model.addAttribute("channel", channel);
		}

		setModel(model);

		return "live/channelConfig/detail";
	}

	@OperationLogAnnotation(module = "直播管理", subModule = "频道配置管理", action = "上下线", message = "上下线频道配置")
	@RequiresPermissions("live:channelConfig:status")
	@RequestMapping(value = { "{id}/status" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult status(@PathVariable("id") Long id) {
		ChannelConfig channelConfigInfo = channelConfigRepository.findOne(id);
		if (channelConfigInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			channelConfigInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
		} else {
			channelConfigInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
		}
		liveService.saveChannelConfig(channelConfigInfo);
		AdminGlobal.operationLogTypeAndId.set("101$"+channelConfigInfo.getId());
		if (channelConfigInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			AdminGlobal.operationLogAction.set("上线");
			AdminGlobal.operationLogMessage.set("上线频道配置");
		} else if (channelConfigInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			AdminGlobal.operationLogAction.set("下线");
			AdminGlobal.operationLogMessage.set("下线频道配置");
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "selectItem" })
	public String selectItem(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("sortIndex");
		}
		Page<ChannelConfig> page = find(request, pageInfo,
				channelConfigRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "live/channelConfig/selectItem";
	}

}
