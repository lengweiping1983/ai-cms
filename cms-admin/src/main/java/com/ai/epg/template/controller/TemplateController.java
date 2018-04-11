package com.ai.epg.template.controller;

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

import com.ai.AdminConstants;
import com.ai.cms.media.bean.ImageBean;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.TemplateTypeEnum;
import com.ai.common.enums.ValidStatusEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.exception.ServiceException;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.epg.config.entity.App;
import com.ai.epg.config.repository.AppRepository;
import com.ai.epg.template.entity.Template;
import com.ai.epg.template.repository.TemplateRepository;
import com.ai.epg.template.service.TemplateService;

@Controller
@RequestMapping(value = {"/template/template"})
public class TemplateController extends AbstractImageController {

    @Autowired
    private TemplateRepository templateRepository;
    
    @Autowired 
    private TemplateService templateService;

    @Autowired
    private AppRepository appRepository;

    private void setModel(Model model) {
    	model.addAttribute("yesNoEnum", YesNoEnum.values());
        model.addAttribute("statusEnum", ValidStatusEnum.values());
        model.addAttribute("typeEnum", TemplateTypeEnum.values());
        
        List<App> appList = appRepository.findAll();
        model.addAttribute("appList", appList);
    }

    @RequestMapping(value = {""})
    public String list(Model model, HttpServletRequest request, PageInfo pageInfo) {
        if (StringUtils.isEmpty(pageInfo.getOrder())) {
            //pageInfo.setOrder("sortIndex");
        	pageInfo.setOrder("appCode");
        }

        Page<Template> page = find(request, pageInfo, templateRepository);
        model.addAttribute("page", page);

        setModel(model);

        return "template/template/list";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.GET)
    public String toAdd(Model model) {
        Template template = new Template();
        model.addAttribute("template", template);

        setModel(model);

        return "template/template/edit";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult add(@RequestBody ImageBean<Template> imageBean) {
        return edit(imageBean, null);
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.GET)
    public String toEdit(Model model, @PathVariable("id") Long id) {
        Template template = templateRepository.findOne(id);
        model.addAttribute("template", template);

        setModel(model);

        return "template/template/edit";
    }

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody ImageBean<Template> imageBean,
			@PathVariable("id") Long id) {
		Template template = imageBean.getData();
		template.setCode(StringUtils.upperCase(template.getCode()));
		if ("SYSTEM".equalsIgnoreCase(template.getAppCode())) {
			template.setShare(YesNoEnum.YES.getKey());
		}
		String backgroundImageData = imageBean.getBackgroundImageData();

		if (checkCode(template.getId(), template.getAppCode(),
				template.getCode())) {
			throw new ServiceException("代码[" + template.getCode() + "]已使用！");
		}
		if (template.getStatus() != null
				&& template.getStatus().intValue() == OnlineStatusEnum.ONLINE
						.getKey()
				&& (StringUtils.isEmpty(backgroundImageData) && StringUtils
						.isEmpty(template.getBackgroundImage()))) {
			// throw new ServiceException("请上传效果图片！");
		}

		Template templateInfo = null;
		String backgroundImageOld = "";
		if (id == null) {
			templateInfo = template;
		} else {
			templateInfo = templateRepository.findOne(id);
			backgroundImageOld = templateInfo.getBackgroundImage();
			BeanInfoUtil.bean2bean(template, templateInfo,
					"name,sortIndex,status,type,share,configBackgroundImage,backgroundImage,description");
		}

		String backgroundImage = "";
		if (StringUtils.isNotEmpty(backgroundImageData)) {
			backgroundImage = upload(AdminConstants.MODULE_RESOURCE_TEMPLATE,
					AdminConstants.RESOURCE_TYPE_BACKGROUND,
					backgroundImageData);
			templateInfo.setBackgroundImage(backgroundImage);
		}

		templateRepository.save(templateInfo);

		if (!StringUtils.trimToEmpty(backgroundImageOld).equals(
				StringUtils.trimToEmpty(template.getBackgroundImage()))) {
			deleteOldResource(backgroundImageOld);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		Template templateInfo = templateRepository.findOne(id);
		if (templateInfo != null) {
			 String backgroundImageOld = templateInfo.getBackgroundImage();

			 templateService.deleteTemplate(templateInfo);

			 deleteOldResource(backgroundImageOld);
		}
		return new BaseResult();
	}

    @RequestMapping(value = {"check"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Object[] check(HttpServletRequest request, @RequestParam(value = "id", required = false) Long id, @RequestParam(value = "appCode") String appCode,
            @RequestParam(value = "fieldId") String fieldId, @RequestParam(value = "fieldValue") String fieldValue) {
        boolean exist = checkCode(id, appCode, fieldValue);

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

    private boolean checkCode(Long id, String appCode, String code) {
        boolean exist = false;
        Template template = null;
        if (StringUtils.isNotEmpty(code)) {
            template = templateRepository.findOneByAppCodeAndCode(appCode, code);
        }
        if (template != null) {
            if (id == null || id == -1 || template.getId().longValue() != id) {
                exist = true;
            }
        }
        return exist;
    }

	@RequestMapping(value = { "selectItem" })
	public String selectItem(Model model, HttpServletRequest request,
			PageInfo pageInfo) {

		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("sortIndex");
		}

		Page<Template> page = find(request, pageInfo, templateRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "template/template/selectItem";
	}
}
