package com.ai.cms.league.controller;

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
import com.ai.cms.league.entity.League;
import com.ai.cms.league.repository.LeagueRepository;
import com.ai.cms.league.service.LeagueService;
import com.ai.cms.media.bean.ImageBean;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.LeagueTypeEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.common.utils.BeanInfoUtil;

@Controller
@RequestMapping(value = {"/league/league"})
public class LeagueController extends AbstractImageController {

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private LeagueService leagueService;

    private void setModel(Model model, HttpServletRequest request) {
        model.addAttribute("statusEnum", OnlineStatusEnum.values());
        model.addAttribute("typeEnum", LeagueTypeEnum.values());
    }

    private void setContentPage(Model model, HttpServletRequest request, PageInfo pageInfo) {
        List<PropertyFilter> filters = getPropertyFilters(request);

        Specification<League> specification = SpecificationUtils.getSpecification(filters);
        Page<League> page = find(specification, pageInfo, leagueRepository);
        model.addAttribute("page", page);
    }

    @RequestMapping(value = {""})
    public String list(Model model, HttpServletRequest request, PageInfo pageInfo) {

        setContentPage(model, request, pageInfo);

        setModel(model, request);

        return "league/league/list";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.GET)
    public String toAdd(Model model, HttpServletRequest request) {
        League league = new League();
        model.addAttribute("league", league);

        setModel(model, request);

        return "league/league/edit";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult add(@RequestBody ImageBean<League> imageBean) {
        return edit(imageBean, null);
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.GET)
    public String toEdit(Model model, HttpServletRequest request, @PathVariable("id") Long id) {
        League league = leagueRepository.findOne(id);
        model.addAttribute("league", league);

        setModel(model, request);

        return "league/league/edit";
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult edit(@RequestBody ImageBean<League> imageBean, @PathVariable("id") Long id) {
        League league = imageBean.getData();
        String image1Data = imageBean.getImage1Data();
        String image2Data = imageBean.getImage2Data();

        League leagueInfo = null;
        String image1Old = "";
        String image2Old = "";
        if (id == null) {
            leagueInfo = league;
        } else {
            leagueInfo = leagueRepository.findOne(id);
            image1Old = leagueInfo.getImage1();
            image2Old = leagueInfo.getImage2();

            BeanInfoUtil.bean2bean(league, leagueInfo, "type,code,name,title,tag,area,num,greetRate,info,status");
        }

        String image1 = "";
        String image2 = "";
        if (StringUtils.isNotEmpty(image1Data)) {
            image1 = upload(AdminConstants.MODULE_RESOURCE_LEAGUE, AdminConstants.RESOURCE_TYPE_POSTER, image1Data);
            leagueInfo.setImage1(image1);
        }
        if (StringUtils.isNotEmpty(image2Data)) {
            image2 = upload(AdminConstants.MODULE_RESOURCE_LEAGUE, AdminConstants.RESOURCE_TYPE_POSTER, image2Data);
            leagueInfo.setImage2(image2);
        }
        leagueRepository.save(leagueInfo);

        if (!StringUtils.trimToEmpty(image1Old).equals(StringUtils.trimToEmpty(league.getImage1()))) {
            // deleteOldResource(image1Old);
        }

        if (!StringUtils.trimToEmpty(image2Old).equals(StringUtils.trimToEmpty(league.getImage2()))) {
            // deleteOldResource(image2Old);
        }

        return new BaseResult();
    }

    @RequestMapping(value = {"{id}/delete"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult delete(@PathVariable("id") Long id) {
        League leagueInfo = leagueRepository.findOne(id);
        if (leagueInfo != null) {
            String image1Old = leagueInfo.getImage1();
            String image2Old = leagueInfo.getImage2();

            leagueService.deleteLeague(leagueInfo);

            deleteOldResource(image1Old);
            deleteOldResource(image2Old);
        }
        return new BaseResult();
    }

    @RequestMapping(value = {"{id}/detail"}, method = RequestMethod.GET)
    public String detail(Model model, HttpServletRequest request, @PathVariable("id") Long id) {
        League league = leagueRepository.findOne(id);
        model.addAttribute("league", league);

        setModel(model, request);

        return "league/league/detail";
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
        League league = null;
        if (StringUtils.isNotEmpty(code)) {
            league = leagueRepository.findOneByCode(code);
        }
        if (league != null) {
            if (id == null || id == -1 || league.getId().longValue() != id) {
                exist = true;
            }
        }
        return exist;
    }

    @RequestMapping(value = {"{id}/status"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult status(@PathVariable("id") Long id) {
        League leagueInfo = leagueRepository.findOne(id);
        if (leagueInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
            leagueInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
        } else {
            leagueInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
        }
        if (leagueInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
        	leagueInfo.setOnlineTime(new Date());
        	leagueInfo.setOfflineTime(null);
		} else if (leagueInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			leagueInfo.setOfflineTime(new Date());
		}
        leagueRepository.save(leagueInfo);
        return new BaseResult();
    }

    @RequestMapping(value = {"selectLeague"})
    public String selectLeague(Model model, HttpServletRequest request, PageInfo pageInfo) {

        setContentPage(model, request, pageInfo);

        setModel(model, request);

        return "league/league/selectLeague";
    }

    @RequestMapping(value = {"selectItem"})
    public String selectItem(Model model, HttpServletRequest request, PageInfo pageInfo) {

        setContentPage(model, request, pageInfo);

        setModel(model, request);

        return "league/league/selectItem";
    }
}
