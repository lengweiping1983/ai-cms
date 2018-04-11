package com.ai.cms.media.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.AdminGlobal;
import com.ai.cms.media.bean.ImageBean;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.Trailer;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.cms.media.repository.TrailerRepository;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.TrailerTypeEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.exception.ServiceException;
import com.ai.common.utils.BeanInfoUtil;

@Controller
@RequestMapping(value = {"/media/trailer"})
public class TrailerController extends AbstractImageController {

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private TrailerRepository trailerRepository;

    private void setModel(Model model) {
        model.addAttribute("statusEnum", OnlineStatusEnum.values());
        model.addAttribute("typeEnum", TrailerTypeEnum.values());

        model.addAttribute("yesNoEnum", YesNoEnum.values());
    }

    @RequestMapping(value = {""})
    public String list(Model model, HttpServletRequest request, PageInfo pageInfo) {
        String managementName = request.getParameter("managementName");
        model.addAttribute("managementName", managementName);
        String itemName = request.getParameter("itemName");
        model.addAttribute("itemName", itemName);

        if (StringUtils.isEmpty(pageInfo.getOrder())) {
            pageInfo.setOrder("sortIndex");
        }
        Page<Trailer> page = find(request, pageInfo, trailerRepository);
        model.addAttribute("page", page);

        List<Long> idList = new ArrayList<Long>();
        for (Trailer trailer : page.getContent()) {
            idList.add(trailer.getProgramId());
        }
        List<Program> programList = programRepository.findByIdIn(idList);
        model.addAttribute("programList", programList);

        setModel(model);

        return "media/trailer/list";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.GET)
    public String toAdd(Model model, HttpServletRequest request) {
        String itemType = request.getParameter("itemType");
        String itemId = request.getParameter("itemId");

        Trailer trailer = new Trailer();
        trailer.setItemType(Integer.valueOf(itemType));
        trailer.setItemId(Long.valueOf(itemId));
        model.addAttribute("trailer", trailer);

        setModel(model);

        return "media/trailer/edit";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult add(@RequestBody ImageBean<Trailer> imageBean) {
        return edit(imageBean, null);
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.GET)
    public String toEdit(Model model, HttpServletRequest request, @PathVariable("id") Long id) {
        Trailer trailer = trailerRepository.findOne(id);
        model.addAttribute("trailer", trailer);

        if (trailer != null && trailer.getProgramId() != null) {
            Program program = programRepository.findOne(trailer.getProgramId());
            model.addAttribute("program", program);
        }

        setModel(model);

        if (trailer.getStatus() == OnlineStatusEnum.ONLINE.getKey() || trailer.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
            OnlineStatusEnum[] statusEnum = {OnlineStatusEnum.ONLINE, OnlineStatusEnum.OFFLINE};
            model.addAttribute("statusEnum", statusEnum);
        }

        return "media/trailer/edit";
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult edit(@RequestBody ImageBean<Trailer> imageBean, @PathVariable("id") Long id) {
        Trailer trailer = imageBean.getData();

        Trailer trailerInfo = null;
        if (id != null && id > 0) {
            trailerInfo = trailerRepository.findOne(id);
            BeanInfoUtil.bean2bean(trailer, trailerInfo, "type,mediaId,mediaEpisode,programId,itemType,itemId,sortIndex,status,seriesCode");
        } else {
            trailerInfo = trailer;
            trailerInfo.setSiteCode(AdminGlobal.getSiteCode());

            Trailer existTrailer = trailerRepository.findOneByProgramIdAndItemTypeAndItemId(trailer.getProgramId(), trailer.getItemType(), trailer.getItemId());
            if (existTrailer != null) {
                throw new ServiceException("该节目已存在！");
            }
        }

        trailerRepository.save(trailerInfo);

        return new BaseResult();
    }

    @RequestMapping(value = {"{id}/delete"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult delete(@PathVariable("id") Long id) {
        Trailer trailerInfo = trailerRepository.findOne(id);
        if (trailerInfo != null) {
            trailerRepository.delete(trailerInfo);
        }
        return new BaseResult();
    }

    @RequestMapping(value = {"{id}/detail"}, method = RequestMethod.GET)
    public String detail(Model model, @PathVariable("id") Long id) {
        Trailer trailer = trailerRepository.findOne(id);
        model.addAttribute("trailer", trailer);

        if (trailer != null && trailer.getProgramId() != null) {
            Program program = programRepository.findOne(trailer.getProgramId());
            model.addAttribute("program", program);
        }

        setModel(model);

        return "media/trailer/detail";
    }

    @RequestMapping(value = {"{id}/status"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult status(@PathVariable("id") Long id) {
        Trailer trailerInfo = trailerRepository.findOne(id);
        if (trailerInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
            trailerInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
        } else {
            trailerInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
        }
        trailerRepository.save(trailerInfo);
        return new BaseResult();
    }

}
