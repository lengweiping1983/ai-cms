package com.ai.cms.star.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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

import com.ai.AdminConstants;
import com.ai.cms.media.bean.ImageBean;
import com.ai.cms.star.entity.Club;
import com.ai.cms.star.entity.Star;
import com.ai.cms.star.repository.ClubRepository;
import com.ai.cms.star.repository.StarRepository;
import com.ai.cms.star.service.StarService;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.StarTypeEnum;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.common.utils.BeanInfoUtil;

@Controller
@RequestMapping(value = {"/star/star"})
public class StarController extends AbstractImageController {

    @Autowired
    private StarRepository starRepository;

    @Autowired
    private StarService starService;

    @Autowired
    private ClubRepository clubRepository;

    private void setModel(Model model, HttpServletRequest request) {
        model.addAttribute("statusEnum", OnlineStatusEnum.values());
        model.addAttribute("typeEnum", StarTypeEnum.values());

        List<Club> clubList = clubRepository.findAll();
        model.addAttribute("clubList", clubList);
    }

    private void setContentPage(Model model, HttpServletRequest request, PageInfo pageInfo) {
        if (StringUtils.isEmpty(pageInfo.getOrder())) {
            pageInfo.setOrder("-createTime");
        }
        List<PropertyFilter> filters = getPropertyFilters(request);

        Specification<Star> specification = SpecificationUtils.getSpecification(filters);
        Page<Star> page = find(specification, pageInfo, starRepository);
        model.addAttribute("page", page);
    }

    @RequestMapping(value = {""})
    public String list(Model model, HttpServletRequest request, PageInfo pageInfo) {

        setContentPage(model, request, pageInfo);

        setModel(model, request);

        return "star/star/list";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.GET)
    public String toAdd(Model model, HttpServletRequest request) {
        Star star = new Star();
        model.addAttribute("star", star);

        setModel(model, request);

        return "star/star/edit";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult add(@RequestBody ImageBean<Star> imageBean) {
        return edit(imageBean, null);
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.GET)
    public String toEdit(Model model, HttpServletRequest request, @PathVariable("id") Long id) {
        Star star = starRepository.findOne(id);
        model.addAttribute("star", star);

        if (star != null && star.getClubId() != null) {
            Club club = clubRepository.findOne(star.getClubId());
            model.addAttribute("club", club);
        }

        setModel(model, request);

        return "star/star/edit";
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult edit(@RequestBody ImageBean<Star> imageBean, @PathVariable("id") Long id) {
        Star star = imageBean.getData();
        String image1Data = imageBean.getImage1Data();
        String image2Data = imageBean.getImage2Data();

        Star starInfo = null;
        String image1Old = "";
        String image2Old = "";
        if (id == null) {
            starInfo = star;
        } else {
            starInfo = starRepository.findOne(id);
            image1Old = starInfo.getImage1();
            image2Old = starInfo.getImage2();

            BeanInfoUtil.bean2bean(star, starInfo, "type,code,name,ename,tag,loveNum,hateNum,greetRate,evaluate,image1,image2,birthday,age,"
                    + "constellation,height,weight,country,area,nation,status,info,clubId,no,position,honor,militaryExploits,ko,surrender,determine,arm,leg");
        }

        String image1 = "";
        String image2 = "";
        if (StringUtils.isNotEmpty(image1Data)) {
            image1 = upload(AdminConstants.MODULE_RESOURCE_STAR, AdminConstants.RESOURCE_TYPE_POSTER, image1Data);
            starInfo.setImage1(image1);
        }
        if (StringUtils.isNotEmpty(image2Data)) {
            image2 = upload(AdminConstants.MODULE_RESOURCE_STAR, AdminConstants.RESOURCE_TYPE_POSTER, image2Data);
            starInfo.setImage2(image2);
        }
        starRepository.save(starInfo);

        if (!StringUtils.trimToEmpty(image1Old).equals(StringUtils.trimToEmpty(star.getImage1()))) {
            // deleteOldResource(image1Old);
        }

        if (!StringUtils.trimToEmpty(image2Old).equals(StringUtils.trimToEmpty(star.getImage2()))) {
            // deleteOldResource(image2Old);
        }

        return new BaseResult();
    }

    @RequestMapping(value = {"{id}/delete"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult delete(@PathVariable("id") Long id) {
        Star starInfo = starRepository.findOne(id);
        if (starInfo != null) {
            String image1Old = starInfo.getImage1();
            String image2Old = starInfo.getImage2();

            starService.deleteStar(starInfo);

            deleteOldResource(image1Old);
            deleteOldResource(image2Old);
        }
        return new BaseResult();
    }

    @RequestMapping(value = {"{id}/detail"}, method = RequestMethod.GET)
    public String detail(Model model, HttpServletRequest request, @PathVariable("id") Long id) {
        Star star = starRepository.findOne(id);
        model.addAttribute("star", star);

        if (star != null && star.getClubId() != null) {
            Club club = clubRepository.findOne(star.getClubId());
            model.addAttribute("club", club);
        }

        setModel(model, request);

        return "star/star/detail";
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
        Star star = null;
        if (StringUtils.isNotEmpty(code)) {
        }
        if (star != null) {
            if (id == null || id == -1 || star.getId().longValue() != id) {
                exist = true;
            }
        }
        return exist;
    }

    @RequestMapping(value = {"{id}/status"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult status(@PathVariable("id") Long id) {
        Star starInfo = starRepository.findOne(id);
        if (starInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
            starInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
        } else {
            starInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
        }
        
        if (starInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
        	starInfo.setOnlineTime(new Date());
        	starInfo.setOfflineTime(null);
		} else if (starInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			starInfo.setOfflineTime(new Date());
		}
        starRepository.save(starInfo);
        return new BaseResult();
    }

    @RequestMapping(value = {"selectStar"})
    public String selectStar(Model model, HttpServletRequest request, PageInfo pageInfo) {
        String type = request.getParameter("type");
        model.addAttribute("type", type);
        setContentPage(model, request, pageInfo);

        setModel(model, request);

        return "star/star/selectStar";
    }

    @RequestMapping(value = {"selectItem"})
    public String selectItem(Model model, HttpServletRequest request, PageInfo pageInfo) {

        setContentPage(model, request, pageInfo);

        setModel(model, request);

        return "star/star/selectItem";
    }
}
