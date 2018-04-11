package com.ai.epg.activity.controller;

import javax.servlet.http.HttpServletRequest;

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

import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractController;
import com.ai.common.enums.ActivityTypeEnum;
import com.ai.common.enums.ValidStatusEnum;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.epg.market.entity.Activity;
import com.ai.epg.market.repository.ActivityRepository;

@Controller
@RequestMapping(value = {"/activity/activity"})
public class ActivityController extends AbstractController {

    @Autowired
    private ActivityRepository activityRepository;

    @RequestMapping(value = {""})
    public String list(Model model, HttpServletRequest request, PageInfo pageInfo) {
        Page<Activity> page = find(request, pageInfo, activityRepository);
        model.addAttribute("page", page);

        model.addAttribute("statusEnum", ValidStatusEnum.values());
        model.addAttribute("typeEnum", ActivityTypeEnum.values());
        
        return "activity/activity/list";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.GET)
    public String toAdd(Model model) {
        model.addAttribute("statusEnum", ValidStatusEnum.values());
        model.addAttribute("typeEnum", ActivityTypeEnum.values());

        return "activity/activity/edit";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult add(@RequestBody Activity activity) {
        return edit(activity, null);
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.GET)
    public String toEdit(Model model, @PathVariable("id") Long id) {
        Activity activity = activityRepository.findOne(id);
        model.addAttribute("activity", activity);

        model.addAttribute("statusEnum", ValidStatusEnum.values());
        model.addAttribute("typeEnum", ActivityTypeEnum.values());
        return "activity/activity/edit";
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult edit(@RequestBody Activity activity, @PathVariable("id") Long id) {
        if (id == null) {
            activityRepository.save(activity);
        } else {
            Activity activityInfo = activityRepository.findOne(activity.getId());
            BeanInfoUtil.bean2bean(activity, activityInfo, "code,name,type,status,beginTime,endTime,address,description");
            activityRepository.save(activityInfo);
        }
        return new BaseResult();
    }
    
    @RequestMapping(value = {"{id}/delete"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult delete(@PathVariable("id") Long id) {
        activityRepository.delete(id);
        return new BaseResult();
    }

    @RequestMapping(value = {"check"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Object[] check(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "fieldId") String fieldId,
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
        Activity activity = null;
        if (StringUtils.isNotEmpty(code)) {
            activity = activityRepository.findOneByCode(code);
        }
        if (activity != null) {
			if (id == null || id == -1 || activity.getId().longValue() != id) {
                exist = true;
            }
        }
        return exist;
    }

}
