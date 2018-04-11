package com.ai.cms.live.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ai.AdminConstants;
import com.ai.AdminGlobal;
import com.ai.cms.config.entity.Cp;
import com.ai.cms.config.repository.CpRepository;
import com.ai.cms.live.entity.Channel;
import com.ai.cms.live.repository.ChannelRepository;
import com.ai.cms.live.service.LiveService;
import com.ai.cms.media.bean.ImageBean;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.ChannelTypeEnum;
import com.ai.common.enums.ItemTypeEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;

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

@Controller
@RequestMapping(value = {"/live/channel"})
public class ChannelController extends AbstractImageController {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private LiveService liveService;
    
	@Autowired
	private CpRepository cpRepository;

    private void setModel(Model model, HttpServletRequest request) {
        model.addAttribute("statusEnum", OnlineStatusEnum.values());
        model.addAttribute("typeEnum", ChannelTypeEnum.values());
        model.addAttribute("yesNoEnum", YesNoEnum.values());
        
		List<Cp> cpList = cpRepository.findAll();
		model.addAttribute("cpList", cpList);
    }

    private void setContentPage(Model model, HttpServletRequest request, PageInfo pageInfo) {
        if (StringUtils.isEmpty(pageInfo.getOrder())) {
            pageInfo.setOrder("sortIndex");
        }
        List<PropertyFilter> filters = getPropertyFilters(request);

        Specification<Channel> specification = SpecificationUtils.getSpecification(filters);
        Page<Channel> page = find(specification, pageInfo, channelRepository);
        model.addAttribute("page", page);
    }

    @RequestMapping(value = {""})
    public String list(Model model, HttpServletRequest request, PageInfo pageInfo) {

        setContentPage(model, request, pageInfo);

        setModel(model, request);

        return "live/channel/list";
    }

    @RequiresPermissions("live:channel:add")
    @RequestMapping(value = {"add"}, method = RequestMethod.GET)
    public String toAdd(Model model, HttpServletRequest request) {
        Channel channel = new Channel();
        model.addAttribute("channel", channel);

        setModel(model, request);

        return "live/channel/edit";
    }

    @OperationLogAnnotation(module = "直播管理", subModule = "中央频道管理", action = "增加", message = "增加中央频道")
    @RequiresPermissions("live:channel:add")
    @RequestMapping(value = {"add"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult add(@RequestBody ImageBean<Channel> imageBean) {
        return edit(imageBean, null);
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.GET)
    public String toEdit(Model model, HttpServletRequest request, @PathVariable("id") Long id) {
        Channel channel = channelRepository.findOne(id);
        model.addAttribute("channel", channel);

        setModel(model, request);

        return "live/channel/edit";
    }

    @OperationLogAnnotation(module = "直播管理", subModule = "中央频道管理", action = "修改", message = "修改中央频道")
    @RequiresPermissions("live:channel:edit")
    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult edit(@RequestBody ImageBean<Channel> imageBean, @PathVariable("id") Long id) {
        Channel channel = imageBean.getData();
        String image1Data = imageBean.getImage1Data();
        String image2Data = imageBean.getImage2Data();

        Channel channelInfo = null;
        String image1Old = "";
        String image2Old = "";
        if (id == null) {
            channelInfo = channel;
            channelInfo.setSiteCode(AdminGlobal.getSiteCode());
        } else {
            channelInfo = channelRepository.findOne(id);
            if (channel != null && !channelInfo.getName().equals(channel.getName())) {
                AdminGlobal.operationLogMessage.set("频道修改标题：<br/> " + channelInfo.getName() + "  ==>  " + channel.getName());
            }
            image1Old = channelInfo.getImage1();
            image2Old = channelInfo.getImage2();

            BeanInfoUtil.bean2bean(channel, channelInfo, "type,code,name,number,"
                    + "country,state,city,zipcode,language,bilingual,image1,image2,status,sortIndex,info,cpId");
        }

        String image1 = "";
        String image2 = "";
        if (StringUtils.isNotEmpty(image1Data)) {
            channelInfo.setImage1(image1);
        }
        if (StringUtils.isNotEmpty(image2Data)) {
            image2 = upload(AdminConstants.MODULE_RESOURCE_LIVE, AdminConstants.RESOURCE_TYPE_POSTER, image2Data);
            channelInfo.setImage2(image2);
        }
        channelRepository.save(channelInfo);

        if (!StringUtils.trimToEmpty(image1Old).equals(StringUtils.trimToEmpty(channel.getImage1()))) {
            // deleteOldResource(image1Old);
        }

        if (!StringUtils.trimToEmpty(image2Old).equals(StringUtils.trimToEmpty(channel.getImage2()))) {
            // deleteOldResource(image2Old);
        }
        AdminGlobal.operationLogTypeAndId.set(ItemTypeEnum.CHANNEL.getKey() + "$"+channelInfo.getId());
        return new BaseResult();
    }

    @OperationLogAnnotation(module = "直播管理", subModule = "中央频道管理", action = "删除", message = "删除中央频道")
    @RequiresPermissions("live:channel:delete")
    @RequestMapping(value = {"{id}/delete"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult delete(@PathVariable("id") Long id) {
        Channel channelInfo = channelRepository.findOne(id);
        if (channelInfo != null) {
            String image1Old = channelInfo.getImage1();
            String image2Old = channelInfo.getImage2();

            liveService.deleteChannel(channelInfo);

            deleteOldResource(image1Old);
            deleteOldResource(image2Old);
            AdminGlobal.operationLogTypeAndId.set(ItemTypeEnum.CHANNEL.getKey() + "$"+channelInfo.getId());
            AdminGlobal.operationLogMessage.set("删除中央频道");
        }
        return new BaseResult();
    }

    @RequiresPermissions("live:channel:detail")
    @RequestMapping(value = {"{id}/detail"}, method = RequestMethod.GET)
    public String detail(Model model, HttpServletRequest request, @PathVariable("id") Long id) {
        Channel channel = channelRepository.findOne(id);
        model.addAttribute("channel", channel);

        setModel(model, request);

        return "live/channel/detail";
    }

    @RequestMapping(value = {"check"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Object[] check(HttpServletRequest request, @RequestParam(value = "id", required = false) Long id, @RequestParam(value = "fieldId") String fieldId,
            @RequestParam(value = "fieldValue") String fieldValue) {
        boolean exist = checkCode(id, fieldValue);

        Object[] jsonValidateReturn = new Object[3];
        jsonValidateReturn[0] = fieldId;
        jsonValidateReturn[1] = !exist;
        if (!exist) {
            jsonValidateReturn[2] = "可以使用!";
        } else {
            jsonValidateReturn[2] = "代码" + StringUtils.trim(fieldValue) + "已使用!";
        }
        return jsonValidateReturn;
    }

    private boolean checkCode(Long id, String code) {
        boolean exist = false;
        Channel channel = null;
        if (StringUtils.isNotEmpty(code)) {
            channel = channelRepository.findOneByCode(code);
        }
        if (channel != null) {
            if (id == null || id == -1 || channel.getId().longValue() != id) {
                exist = true;
            }
        }
        return exist;
    }

    @OperationLogAnnotation(module = "直播管理", subModule = "中央频道管理", action = "上下线", message = "上下线中央频道")
    @RequiresPermissions("live:channel:status")
    @RequestMapping(value = {"{id}/status"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult status(@PathVariable("id") Long id) {
        Channel channelInfo = channelRepository.findOne(id);
        if (channelInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
            channelInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
        } else {
            channelInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
        }
        if (channelInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
        	channelInfo.setOnlineTime(new Date());
        	channelInfo.setOfflineTime(null);
		} else if (channelInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			channelInfo.setOfflineTime(new Date());
		}
        channelRepository.save(channelInfo);
        AdminGlobal.operationLogTypeAndId.set(ItemTypeEnum.CHANNEL.getKey() + "$"+channelInfo.getId());
        if (channelInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
            AdminGlobal.operationLogAction.set("上线");
            AdminGlobal.operationLogMessage.set("上线中央频道");
        } else if (channelInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
            AdminGlobal.operationLogAction.set("下线");
            AdminGlobal.operationLogMessage.set("下线中央频道");
        }
        return new BaseResult();
    }

    @RequestMapping(value = {"batchToChannel"}, method = RequestMethod.GET)
    public String toBatchToChannel(Model model, @RequestParam(value = "itemType") Integer itemType, @RequestParam(value = "itemIds") String itemIds) {

        model.addAttribute("itemType", itemType);
        model.addAttribute("itemIds", itemIds);

        model.addAttribute("statusEnum", OnlineStatusEnum.values());

        return "live/channel/batchToChannel";
    }

    @RequestMapping(value = {"selectChannel"})
    public String selectChannel(Model model, HttpServletRequest request, PageInfo pageInfo) {

        setContentPage(model, request, pageInfo);

        setModel(model, request);

        return "live/channel/selectChannel";
    }

    @RequestMapping(value = {"selectItem"})
    public String selectItem(Model model, HttpServletRequest request, PageInfo pageInfo) {

        setContentPage(model, request, pageInfo);

        setModel(model, request);

        return "live/channel/selectItem";
    }
}
