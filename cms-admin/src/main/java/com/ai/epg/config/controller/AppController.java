package com.ai.epg.config.controller;

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

import com.ai.cms.config.entity.Cp;
import com.ai.cms.config.repository.CpRepository;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractController;
import com.ai.common.enums.AppTypeEnum;
import com.ai.common.enums.ValidStatusEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.epg.config.entity.App;
import com.ai.epg.config.repository.AppRepository;
import com.ai.sys.entity.Menu;
import com.ai.sys.repository.MenuRepository;

@Controller
@RequestMapping(value = {"/config/app"})
public class AppController extends AbstractController {

    @Autowired
    private AppRepository appRepository;
    
    @Autowired
    private CpRepository cpRepository;

    @Autowired
    private MenuRepository menuRepository;
    
    private void setModel(Model model) {
    	model.addAttribute("statusEnum", ValidStatusEnum.values());
        model.addAttribute("typeEnum", AppTypeEnum.values());
        model.addAttribute("yesNoEnum", YesNoEnum.values());
        List<Cp> cpList = cpRepository.findAll();
        model.addAttribute("cpList", cpList);
        
        List<App> appList = appRepository.findAll();
        model.addAttribute("appList", appList);
    }
    
    @RequestMapping(value = {""})
    public String list(Model model, HttpServletRequest request, PageInfo pageInfo) {
        Page<App> page = find(request, pageInfo, appRepository);
        model.addAttribute("page", page);

        setModel(model);

        return "config/app/list";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.GET)
    public String toAdd(Model model) {
    	setModel(model);

        return "config/app/edit";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult add(@RequestBody App app) {
        return edit(app, null);
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.GET)
    public String toEdit(Model model, @PathVariable("id") Long id) {
        App app = appRepository.findOne(id);
        model.addAttribute("app", app);

        setModel(model);
        
        return "config/app/edit";
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult edit(@RequestBody App app, @PathVariable("id") Long id) {
    	app.setCode(StringUtils.upperCase(app.getCode()));
        if (id == null) {
            appRepository.save(app);
        } else {
            App appInfo = appRepository.findOne(app.getId());
            BeanInfoUtil.bean2bean(app, appInfo, "type,name,code,aloneCharge,chargeAppCode,aloneOrderPage,cpCodes,appCodes,status,description");
            appRepository.save(appInfo);
        }
		if (id == null) {// 创建菜单
			Menu parentMenu = new Menu();
			parentMenu.setType(Menu.TYPE_MENU);
			parentMenu.setName(app.getName()+"管理");
			parentMenu.setHref("#");
			menuRepository.save(parentMenu);
			
			Menu widgetMenu = new Menu();
			widgetMenu.setType(Menu.TYPE_MENU);
			widgetMenu.setName("推荐位管理");
			widgetMenu.setHref("/widget/widget/?appCode="+StringUtils.trimToEmpty(app.getCode()));
			widgetMenu.setParent(parentMenu);
			widgetMenu.setParentId(parentMenu.getId());
			widgetMenu.setSort(1);
			menuRepository.save(widgetMenu);
			
			Menu categoryMenu = new Menu();
			categoryMenu.setType(Menu.TYPE_MENU);
			categoryMenu.setName("栏目管理");
			categoryMenu.setHref("/category/?appCode="+StringUtils.trimToEmpty(app.getCode()));
			categoryMenu.setParent(parentMenu);
			categoryMenu.setParentId(parentMenu.getId());
			categoryMenu.setSort(2);
			menuRepository.save(categoryMenu);
			
			Menu albumMenu = new Menu();
			albumMenu.setType(Menu.TYPE_MENU);
			albumMenu.setName("专题管理");
			albumMenu.setHref("/album/album/?appCode="+StringUtils.trimToEmpty(app.getCode()));
			albumMenu.setParent(parentMenu);
			albumMenu.setParentId(parentMenu.getId());
			albumMenu.setSort(3);
			menuRepository.save(albumMenu);
			
			Menu uriMenu = new Menu();
			uriMenu.setType(Menu.TYPE_MENU);
			uriMenu.setName("页面管理");
			uriMenu.setHref("/uri/uri/?appCode="+StringUtils.trimToEmpty(app.getCode()));
			uriMenu.setParent(parentMenu);
			uriMenu.setParentId(parentMenu.getId());
			uriMenu.setSort(4);
			menuRepository.save(uriMenu);
		}
        return new BaseResult();
    }

    @RequestMapping(value = {"{id}/delete"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult delete(@PathVariable("id") Long id) {
        appRepository.delete(id);
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
        App app = null;
        if (StringUtils.isNotEmpty(code)) {
            app = appRepository.findOneByCode(code);
        }
        if (app != null) {
            if (id == null || id == -1 || app.getId().longValue() != id) {
                exist = true;
            }
        }
        return exist;
    }

}
