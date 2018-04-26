package com.ai.cms.league.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.AdminConstants;
import com.ai.cms.config.entity.Cp;
import com.ai.cms.config.repository.CpRepository;
import com.ai.cms.league.entity.LeagueMatch;
import com.ai.cms.league.entity.LeagueSeason;
import com.ai.cms.league.repository.LeagueMatchRepository;
import com.ai.cms.league.repository.LeagueSeasonRepository;
import com.ai.cms.league.service.LeagueService;
import com.ai.cms.live.entity.Channel;
import com.ai.cms.live.entity.Schedule;
import com.ai.cms.live.repository.ChannelRepository;
import com.ai.cms.live.repository.ScheduleRepository;
import com.ai.cms.media.bean.ImageBean;
import com.ai.cms.media.entity.Program;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.LeagueMatchTypeEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.SportContentTypeEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.exception.ServiceException;
import com.ai.common.utils.BeanInfoUtil;

@Controller
@RequestMapping(value = {"/league/{leagueSeasonId}/leagueMatch"})
public class LeagueSeasonMatchController extends AbstractImageController {

    @Autowired
    private LeagueSeasonRepository leagueSeasonRepository;

    @Autowired
    private LeagueMatchRepository leagueMatchRepository;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CpRepository cpRepository;

    private void setModel(Model model) {
        model.addAttribute("statusEnum", OnlineStatusEnum.values());
        model.addAttribute("yesNoEnum", YesNoEnum.values());
        model.addAttribute("sportContentTypeEnum", SportContentTypeEnum.values());

        model.addAttribute("typeEnum", LeagueMatchTypeEnum.values());

        List<Cp> cpList = cpRepository.findAll();
        model.addAttribute("cpList", cpList);
        
        List<Channel> channelList = channelRepository.findAll();
        model.addAttribute("channelList", channelList);
    }

    @RequestMapping(value = {""})
    public String list(@PathVariable("leagueSeasonId") Long leagueSeasonId, Model model, HttpServletRequest request, PageInfo pageInfo) {
        if (StringUtils.isEmpty(pageInfo.getOrder())) {
            pageInfo.setOrder("-beginTime");
        }

        LeagueSeason leagueSeason = leagueSeasonRepository.findOne(leagueSeasonId);
        model.addAttribute("leagueSeason", leagueSeason);

        Page<LeagueMatch> page = find(request, pageInfo, leagueMatchRepository);
        model.addAttribute("page", page);

        setModel(model);

        return "league/leagueSeasonMatch/list";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.GET)
    public String toAdd(Model model, @PathVariable("leagueSeasonId") Long leagueSeasonId) {
        LeagueSeason leagueSeason = leagueSeasonRepository.findOne(leagueSeasonId);

        LeagueMatch leagueMatch = new LeagueMatch();
        leagueMatch.setLeagueSeason(leagueSeason);
        model.addAttribute("leagueMatch", leagueMatch);

        model.addAttribute("leagueSeason", leagueSeason);

        setModel(model);

        return "league/leagueSeasonMatch/edit";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult edit(@RequestBody ImageBean<LeagueMatch> imageBean, @PathVariable("leagueSeasonId") Long leagueSeasonId) {
        return edit(imageBean, leagueSeasonId, null);
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.GET)
    public String toEdit(Model model, @PathVariable("leagueSeasonId") Long leagueSeasonId, @PathVariable("id") Long id) {
        LeagueMatch leagueMatch = leagueMatchRepository.findOne(id);
        model.addAttribute("leagueMatch", leagueMatch);

        if (leagueMatch != null && leagueMatch.getLeagueSeasonId() != null) {
            LeagueSeason leagueSeason = leagueSeasonRepository.findOne(leagueMatch.getLeagueSeasonId());
            model.addAttribute("leagueSeason", leagueSeason);
        }

        if (leagueMatch != null && leagueMatch.getChannelId() != null) {
            Channel channel = channelRepository.findOne(leagueMatch.getChannelId());
            model.addAttribute("channel", channel);
        }
        if (leagueMatch != null && leagueMatch.getScheduleId() != null) {
            Schedule schedule = scheduleRepository.findOne(leagueMatch.getScheduleId());
            model.addAttribute("schedule", schedule);
        }

		Program program = leagueService.getSplitProgram(leagueMatch);
		model.addAttribute("program", program);

        setModel(model);

        if (leagueMatch.getStatus() == OnlineStatusEnum.ONLINE.getKey() || leagueMatch.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
            OnlineStatusEnum[] statusEnum = {OnlineStatusEnum.ONLINE, OnlineStatusEnum.OFFLINE};
            model.addAttribute("statusEnum", statusEnum);
        }

        return "league/leagueSeasonMatch/edit";
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult edit(@RequestBody ImageBean<LeagueMatch> imageBean, @PathVariable("leagueSeasonId") Long leagueSeasonId, @PathVariable("id") Long id) {
        LeagueMatch leagueMatch = imageBean.getData();
        String image1Data = imageBean.getImage1Data();
        String image2Data = imageBean.getImage2Data();

        if (leagueMatch.getHomeId() != null && leagueMatch.getGuestId() != null && leagueMatch.getHomeId().longValue() == leagueMatch.getGuestId().longValue()
                && leagueMatch.getHomeType() != null && leagueMatch.getGuestType() != null
                && leagueMatch.getHomeType().intValue() == leagueMatch.getGuestType().intValue()) {
            throw new ServiceException("主场和客场不能相同！");
        }

        LeagueMatch leagueMatchInfo = null;
        String image1Old = "";
        String image2Old = "";
        if (id == null) {
            leagueMatchInfo = leagueMatch;
        } else {
            leagueMatchInfo = leagueMatchRepository.findOne(leagueMatch.getId());
            image1Old = leagueMatchInfo.getImage1();
            image2Old = leagueMatchInfo.getImage2();
            BeanInfoUtil
                    .bean2bean(
                            leagueMatch,
                            leagueMatchInfo,
                            "type,sportContentType,leagueSeasonId,name,title,searchName,tag,keyword,area,beginTime,duration,viewpoint,info,"
                                    + "leagueIndex,episodeIndex,homeId,guestId,homeName,guestName,homeType,guestType,homeScore,guestScore,pointStatus,homePointNum,guestPointNum,"
                                    + "image1,image2,channelId,scheduleId,splitProgram,mediaId,mediaEpisode,programId,cpCode");
        }

        String image1 = "";
        String image2 = "";
        if (StringUtils.isNotEmpty(image1Data)) {
            image1 = upload(AdminConstants.MODULE_RESOURCE_LEAGUE, AdminConstants.RESOURCE_TYPE_POSTER, image1Data);
            leagueMatchInfo.setImage1(image1);
        }
        if (StringUtils.isNotEmpty(image2Data)) {
            image2 = upload(AdminConstants.MODULE_RESOURCE_LEAGUE, AdminConstants.RESOURCE_TYPE_POSTER, image2Data);
            leagueMatchInfo.setImage2(image2);
        }
        leagueService.saveLeagueMatch(leagueMatchInfo);

        if (!StringUtils.trimToEmpty(image1Old).equals(StringUtils.trimToEmpty(leagueMatch.getImage1()))) {
            // deleteOldResource(image1Old);
        }

        if (!StringUtils.trimToEmpty(image2Old).equals(StringUtils.trimToEmpty(leagueMatch.getImage2()))) {
            // deleteOldResource(image2Old);
        }
        return new BaseResult();
    }

    @RequestMapping(value = {"{id}/delete"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult delete(@PathVariable("leagueSeasonId") Long leagueSeasonId, @PathVariable("id") Long id) {
        LeagueMatch leagueMatchInfo = leagueMatchRepository.findOne(id);
        if (leagueMatchInfo != null) {
            String image1Old = leagueMatchInfo.getImage1();
            String image2Old = leagueMatchInfo.getImage2();

            leagueService.deleteLeagueMatch(leagueMatchInfo);

            deleteOldResource(image1Old);
            deleteOldResource(image2Old);
        }
        return new BaseResult();
    }

    @RequestMapping(value = {"{id}/status"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult status(@PathVariable("id") Long id) {
        LeagueMatch leagueMatchInfo = leagueMatchRepository.findOne(id);
        if (leagueMatchInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
            leagueMatchInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
        } else {
            leagueMatchInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
        }
        if (leagueMatchInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
        	leagueMatchInfo.setOnlineTime(new Date());
        	leagueMatchInfo.setOfflineTime(null);
		} else if (leagueMatchInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			leagueMatchInfo.setOfflineTime(new Date());
		}
        leagueService.saveLeagueMatch(leagueMatchInfo);
        return new BaseResult();
    }
}
