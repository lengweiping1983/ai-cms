package com.ai.epg.product.controller;

import java.util.Date;
import java.util.List;

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

import com.ai.AppGlobal;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractController;
import com.ai.common.enums.ChargeTimeUnitEnum;
import com.ai.common.enums.ChargeTypeEnum;
import com.ai.common.enums.ValidStatusEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.exception.ServiceException;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.common.utils.DateUtils;
import com.ai.epg.config.entity.App;
import com.ai.epg.config.repository.AppRepository;
import com.ai.epg.product.entity.Charge;
import com.ai.epg.product.repository.ChargeRepository;

@Controller
@RequestMapping(value = {"/config/charge"})
public class ChargeController extends AbstractController {

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private AppRepository appRepository;

    private void setModel(Model model) {
        model.addAttribute("typeEnum", ChargeTypeEnum.values());
        model.addAttribute("statusEnum", ValidStatusEnum.values());
        model.addAttribute("yesNoEnum", YesNoEnum.values());
        
        model.addAttribute("chargeTimeUnitEnum", ChargeTimeUnitEnum.values());

        List<App> appList = appRepository.findAll();
        model.addAttribute("appList", appList);
    }
    @RequestMapping(value = {""})
    public String list(Model model, HttpServletRequest request, PageInfo pageInfo) {
        Page<Charge> page = find(request, pageInfo, chargeRepository);
        model.addAttribute("page", page);

        setModel(model);

        return "config/charge/list";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.GET)
    public String toAdd(Model model) {
        Charge charge = new Charge();
        charge.setBeginTime(new Date());
        charge.setEndTime(DateUtils.addYears(charge.getBeginTime(), 20));
        model.addAttribute("charge", charge);

        setModel(model);

        return "config/charge/edit";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult add(@RequestBody Charge charge) {
        return edit(charge, null);
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.GET)
    public String toEdit(Model model, @PathVariable("id") Long id) {
        Charge charge = chargeRepository.findOne(id);
        model.addAttribute("charge", charge);

        setModel(model);

        return "config/charge/edit";
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult edit(@RequestBody Charge charge, @PathVariable("id") Long id) {
		if (checkCode(charge.getId(), charge.getAppCode(), charge.getCode())) {
			throw new ServiceException("计费点代码[" + charge.getCode() + "]已使用！");
		}
        Charge chargeInfo = null;
        if (id == null) {
            chargeInfo = charge;
        } else {
            chargeInfo = chargeRepository.findOne(charge.getId());
            BeanInfoUtil.bean2bean(charge, chargeInfo,
                    "type,name,code,timeUnit,duration,status,intro,description,originPrice,price,beginTime,endTime,"
                    + "subscriptionExtend,supportUnsubscribe,partnerProductCode,spId,spKey,sortIndex,appCodes");
        }
        chargeRepository.save(chargeInfo);

        return new BaseResult();
    }

    @RequestMapping(value = {"{id}/delete"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult delete(@PathVariable("id") Long id) {
        chargeRepository.delete(id);
        return new BaseResult();
    }

    @RequestMapping(value = {"check"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Object[] check(HttpServletRequest request, @RequestParam(value = "id", required = false) Long id, @RequestParam(value = "appCode") String appCode,
            @RequestParam(value = "fieldId") String fieldId, @RequestParam(value = "fieldValue") String fieldValue) {
		Object[] jsonValidateReturn = new Object[3];
		jsonValidateReturn[0] = fieldId;
		jsonValidateReturn[1] = false;
		if (StringUtils.isEmpty(fieldValue)) {
			jsonValidateReturn[2] = "计费点代码不能为空!";
		} else if (fieldValue.indexOf(AppGlobal.FEE_PRE) != 0) {
			jsonValidateReturn[2] = "计费点代码[" + fieldValue + "]需要以"
					+ AppGlobal.FEE_PRE + "开头!";
		} else if (AppGlobal.FEE_PRE.equalsIgnoreCase(fieldValue)) {
			jsonValidateReturn[2] = "计费点代码[" + fieldValue + "]不正确!";
		} else {
			boolean exist = checkCode(id, appCode, fieldValue);
			if (!exist) {
				jsonValidateReturn[1] = true;
				jsonValidateReturn[2] = "可以使用!";
			} else {
				jsonValidateReturn[2] = "计费点代码" + StringUtils.trim(fieldValue)
						+ "已使用!";
			}
		}
		return jsonValidateReturn;
    }

    private boolean checkCode(Long id, String appCode, String code) {
        boolean exist = false;
        Charge charge = null;
        if (StringUtils.isNotEmpty(code)) {
            charge = chargeRepository.findOneByAppCodeAndCode(appCode, code);
        }
        if (charge != null) {
            if (id == null || id == -1 || charge.getId().longValue() != id) {
                exist = true;
            }
        }
        return exist;
    }

}
