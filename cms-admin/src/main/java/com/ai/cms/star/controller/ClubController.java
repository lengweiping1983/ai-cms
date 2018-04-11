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
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.AdminConstants;
import com.ai.cms.media.bean.ImageBean;
import com.ai.cms.star.entity.Club;
import com.ai.cms.star.repository.ClubRepository;
import com.ai.cms.star.service.ClubService;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.ClubTypeEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.epg.seq.repository.SequenceRepository;

@Controller
@RequestMapping(value = {"/star/club"})
public class ClubController extends AbstractImageController {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubService clubService;

    @Autowired
    private SequenceRepository sequenceRepository;

    private void setModel(Model model, HttpServletRequest request) {
        model.addAttribute("statusEnum", OnlineStatusEnum.values());
        model.addAttribute("typeEnum", ClubTypeEnum.values());
    }

    private void setContentPage(Model model, HttpServletRequest request, PageInfo pageInfo) {
        if (StringUtils.isEmpty(pageInfo.getOrder())) {
            pageInfo.setOrder("-createTime");
        }
        List<PropertyFilter> filters = getPropertyFilters(request);

        Specification<Club> specification = SpecificationUtils.getSpecification(filters);
        Page<Club> page = find(specification, pageInfo, clubRepository);
        model.addAttribute("page", page);
    }

    @RequestMapping(value = {""})
    public String list(Model model, HttpServletRequest request, PageInfo pageInfo) {

        setContentPage(model, request, pageInfo);

        setModel(model, request);

        return "star/club/list";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.GET)
    public String toAdd(Model model, HttpServletRequest request) {
        Club club = new Club();
        model.addAttribute("club", club);

        setModel(model, request);

        return "star/club/edit";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult add(@RequestBody ImageBean<Club> imageBean) {
        return edit(imageBean, null);
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.GET)
    public String toEdit(Model model, HttpServletRequest request, @PathVariable("id") Long id) {
        Club club = clubRepository.findOne(id);
        model.addAttribute("club", club);

        setModel(model, request);

        return "star/club/edit";
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult edit(@RequestBody ImageBean<Club> imageBean, @PathVariable("id") Long id) {
        Club club = imageBean.getData();
        String image1Data = imageBean.getImage1Data();
        String image2Data = imageBean.getImage2Data();

        Club clubInfo = null;
        String image1Old = "";
        String image2Old = "";
        if (id == null) {
            clubInfo = club;
        } else {
            clubInfo = clubRepository.findOne(id);
            image1Old = clubInfo.getImage1();
            image2Old = clubInfo.getImage2();

            BeanInfoUtil.bean2bean(club, clubInfo, "type,name,shortName,coach,area,lhomeCourt,foundingTime,honor,info,status,greetRate");
        }

        String image1 = "";
        String image2 = "";
        if (StringUtils.isNotEmpty(image1Data)) {
            image1 = upload(AdminConstants.MODULE_RESOURCE_CLUB, AdminConstants.RESOURCE_TYPE_POSTER, image1Data);
            clubInfo.setImage1(image1);
        }
        if (StringUtils.isNotEmpty(image2Data)) {
            image2 = upload(AdminConstants.MODULE_RESOURCE_CLUB, AdminConstants.RESOURCE_TYPE_POSTER, image2Data);
            clubInfo.setImage2(image2);
        }
        clubRepository.save(clubInfo);

        if (!StringUtils.trimToEmpty(image1Old).equals(StringUtils.trimToEmpty(club.getImage1()))) {
            // deleteOldResource(image1Old);
        }

        if (!StringUtils.trimToEmpty(image2Old).equals(StringUtils.trimToEmpty(club.getImage2()))) {
            // deleteOldResource(image2Old);
        }

        return new BaseResult();
    }

    @RequestMapping(value = {"{id}/delete"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult delete(@PathVariable("id") Long id) {
        Club clubInfo = clubRepository.findOne(id);
        if (clubInfo != null) {
            String image1Old = clubInfo.getImage1();
            String image2Old = clubInfo.getImage2();

            clubService.deleteClub(clubInfo);

            deleteOldResource(image1Old);
            deleteOldResource(image2Old);
        }
        return new BaseResult();
    }

    @RequestMapping(value = {"{id}/detail"}, method = RequestMethod.GET)
    public String detail(Model model, HttpServletRequest request, @PathVariable("id") Long id) {
        Club club = clubRepository.findOne(id);
        model.addAttribute("club", club);

        setModel(model, request);

        return "star/club/detail";
    }

    @RequestMapping(value = {"{id}/status"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult status(@PathVariable("id") Long id) {
        Club clubInfo = clubRepository.findOne(id);
        if (clubInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
            clubInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
        } else {
            clubInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
        }
        
        if (clubInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
        	clubInfo.setOnlineTime(new Date());
        	clubInfo.setOfflineTime(null);
		} else if (clubInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			clubInfo.setOfflineTime(new Date());
		}
        clubRepository.save(clubInfo);
        return new BaseResult();
    }

    @RequestMapping(value = {"selectClub"})
    public String selectClub(Model model, HttpServletRequest request, PageInfo pageInfo) {
        String type = request.getParameter("type");
        model.addAttribute("type", type);

        setContentPage(model, request, pageInfo);

        setModel(model, request);

        return "star/club/selectClub";
    }

    @RequestMapping(value = {"selectItem"})
    public String selectItem(Model model, HttpServletRequest request, PageInfo pageInfo) {

        setContentPage(model, request, pageInfo);

        setModel(model, request);

        return "star/club/selectItem";
    }
}
