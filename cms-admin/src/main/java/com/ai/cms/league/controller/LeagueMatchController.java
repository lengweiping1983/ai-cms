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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.AdminConstants;
import com.ai.AppGlobal;
import com.ai.cms.config.entity.Cp;
import com.ai.cms.config.repository.CpRepository;
import com.ai.cms.league.entity.LeagueMatch;
import com.ai.cms.league.entity.LeagueMatchCode;
import com.ai.cms.league.entity.LeagueSeason;
import com.ai.cms.league.repository.LeagueMatchCodeRepository;
import com.ai.cms.league.repository.LeagueMatchRepository;
import com.ai.cms.league.repository.LeagueRepository;
import com.ai.cms.league.repository.LeagueSeasonRepository;
import com.ai.cms.league.service.LeagueService;
import com.ai.cms.live.entity.Channel;
import com.ai.cms.live.entity.Schedule;
import com.ai.cms.live.repository.ChannelConfigRepository;
import com.ai.cms.live.repository.ChannelRepository;
import com.ai.cms.live.repository.ScheduleRepository;
import com.ai.cms.media.bean.BatchBean;
import com.ai.cms.media.bean.BatchMetadataBean;
import com.ai.cms.media.bean.BatchStatusBean;
import com.ai.cms.media.bean.IdDataBean;
import com.ai.cms.media.bean.ImageBean;
import com.ai.cms.media.entity.Program;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.bean.ResultCode;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.ContentTypeEnum;
import com.ai.common.enums.ItemTypeEnum;
import com.ai.common.enums.LeagueMatchTypeEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.SportContentTypeEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.exception.ServiceException;
import com.ai.common.utils.BeanInfoUtil;

@Controller
@RequestMapping(value = {"/league/leagueMatch"})
public class LeagueMatchController extends AbstractImageController {

	@Autowired
	private LeagueRepository leagueRepository;
	
    @Autowired
    private LeagueSeasonRepository leagueSeasonRepository;

    @Autowired
    private LeagueMatchRepository leagueMatchRepository;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private LeagueMatchCodeRepository leagueMatchCodeRepository;

    @Autowired
    private ChannelRepository channelRepository;
    
    @Autowired
    private ChannelConfigRepository channelConfigRepository;

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
    public String list(Model model, HttpServletRequest request, PageInfo pageInfo) {
        if (StringUtils.isEmpty(pageInfo.getOrder())) {
            pageInfo.setOrder("-beginTime");
        }
        Page<LeagueMatch> page = find(request, pageInfo, leagueMatchRepository);
        model.addAttribute("page", page);

        setModel(model);

        return "league/leagueMatch/list";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.GET)
    public String toAdd(Model model) {
        LeagueMatch leagueMatch = new LeagueMatch();
        model.addAttribute("leagueMatch", leagueMatch);

        setModel(model);

        return "league/leagueMatch/edit";
    }

    @RequestMapping(value = {"add"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult add(@RequestBody ImageBean<LeagueMatch> imageBean) {
        return edit(imageBean, null);
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.GET)
    public String toEdit(Model model, @PathVariable("id") Long id) {
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

        return "league/leagueMatch/edit";
    }

    @RequestMapping(value = {"{id}/edit"}, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult edit(@RequestBody ImageBean<LeagueMatch> imageBean, @PathVariable("id") Long id) {
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
        if (id != null && id > 0) {
            leagueMatchInfo = leagueMatchRepository.findOne(id);
            image1Old = leagueMatchInfo.getImage1();
            image2Old = leagueMatchInfo.getImage2();
            BeanInfoUtil
                    .bean2bean(
                            leagueMatch,
                            leagueMatchInfo,
                            "type,sportContentType,leagueSeasonId,name,title,searchName,tag,keyword,area,beginTime,duration,viewpoint,info,"
                                    + "leagueIndex,episodeIndex,homeId,guestId,homeName,guestName,homeType,guestType,homeScore,guestScore,pointStatus,homePointNum,guestPointNum,"
                                    + "image1,image2,channelId,scheduleId,splitProgram,mediaId,mediaEpisode,programId,cpCode");
        } else {
            leagueMatchInfo = leagueMatch;
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
    public BaseResult delete(@PathVariable("id") Long id) {
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

    @RequestMapping(value = {"{id}/detail"}, method = RequestMethod.GET)
    public String detail(Model model, @PathVariable("id") Long id) {
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

        return "league/leagueMatch/detail";
    }

	@RequestMapping(value = { "{id}/detailJson" }, method = RequestMethod.GET)
	public String detailJson(Model model, @PathVariable("id") Long id) {
		LeagueMatch leagueMatch = leagueMatchRepository.findOne(id);
		if (leagueMatch != null) {
			String json = leagueService.getLeagueMatchJson(leagueMatch);
			model.addAttribute("jsonString", json);
		}
		
		return "league/leagueMatch/detailJson";
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

    @RequestMapping(value = {"selectItem"})
    public String selectItem(Model model, HttpServletRequest request, PageInfo pageInfo) {
        if (StringUtils.isEmpty(pageInfo.getOrder())) {
            pageInfo.setOrder("-beginTime");
        }
        Page<LeagueMatch> page = find(request, pageInfo, leagueMatchRepository);
        model.addAttribute("page", page);

        setModel(model);

        return "league/leagueMatch/selectItem";
    }

    @RequestMapping(value = {"selectLeagueMatch"})
    public String selectLeagueMatch(Model model, HttpServletRequest request, PageInfo pageInfo) {
        if (StringUtils.isEmpty(pageInfo.getOrder())) {
            pageInfo.setOrder("-beginTime");
        }
        Page<LeagueMatch> page = find(request, pageInfo, leagueMatchRepository);
        model.addAttribute("page", page);

        setModel(model);

        return "league/leagueMatch/selectLeagueMatch";
    }

    @RequestMapping(value = {"batchChangeMetadata"}, method = RequestMethod.GET)
    public String batchChangeMetadata(Model model, @RequestParam(value = "itemType") Integer itemType, @RequestParam(value = "itemIds") String itemIds) {

        model.addAttribute("itemType", itemType);
        model.addAttribute("itemIds", itemIds);

        model.addAttribute("contentTypeEnum", ContentTypeEnum.values());

        List<Cp> cpList = cpRepository.findAll();
        model.addAttribute("cpList", cpList);

        return "league/leagueMatch/batchChangeMetadata";
    }

    @RequestMapping(value = {"batchChangeMetadata"}, method = {RequestMethod.POST}, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult batchChangeMetadata(@RequestBody BatchMetadataBean batchBean) {
        Integer itemType = batchBean.getItemType();
        String itemIds = batchBean.getItemIds();
        if (itemType == null || StringUtils.isEmpty(itemIds)) {
            return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
        }
        String[] itemIdArr = itemIds.split(",");
        if (null != itemIdArr) {
            for (String itemIdStr : itemIdArr) {
                Long itemId = Long.valueOf(itemIdStr);
                updateMetadata(itemId, batchBean);
            }
        }
        return new BaseResult();
    }

    @RequestMapping(value = {"batchChangeStatus"}, method = RequestMethod.GET)
    public String toBatchChangeStatus(Model model, @RequestParam(value = "itemType") Integer itemType, @RequestParam(value = "itemIds") String itemIds) {

        model.addAttribute("itemType", itemType);
        model.addAttribute("itemIds", itemIds);

        OnlineStatusEnum[] statusEnum = {OnlineStatusEnum.ONLINE, OnlineStatusEnum.OFFLINE};
        model.addAttribute("statusEnum", statusEnum);

        return "league/leagueMatch/batchChangeStatus";
    }

    @RequestMapping(value = {"batchChangeStatus"}, method = {RequestMethod.POST}, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult batchChangeStatus(@RequestBody BatchStatusBean batchBean) {
        Integer itemType = batchBean.getItemType();
        String itemIds = batchBean.getItemIds();
        if (itemType == null || StringUtils.isEmpty(itemIds)) {
            return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
        }
        String[] itemIdArr = itemIds.split(",");
        if (null != itemIdArr) {
            for (String itemIdStr : itemIdArr) {
                Long itemId = Long.valueOf(itemIdStr);
                LeagueMatch leagueMatchInfo = leagueMatchRepository.findOne(itemId);
                leagueMatchInfo.setStatus(batchBean.getStatus());
                if (leagueMatchInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
                	leagueMatchInfo.setOnlineTime(new Date());
                	leagueMatchInfo.setOfflineTime(null);
        		} else if (leagueMatchInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
        			leagueMatchInfo.setOfflineTime(new Date());
        		}
                leagueMatchRepository.save(leagueMatchInfo);
            }
        }
        return new BaseResult();
    }

    // @Transactional(value = "slaveTransactionManager", readOnly = false)
    public void updateMetadata(Long itemId, BatchMetadataBean batchBean) {
        LeagueMatch leagueMatchInfo = leagueMatchRepository.findOne(itemId);
        if (batchBean.getCpCodeSwitch() != null && batchBean.getCpCodeSwitch().equals("on")) {
            leagueMatchInfo.setCpCode(batchBean.getCpCode());
        }
        if (batchBean.getTagSwitch() != null && batchBean.getTagSwitch().equals("on")) {
            leagueMatchInfo.setTag(batchBean.getTag());
        }
        if (batchBean.getKeywordSwitch() != null && batchBean.getKeywordSwitch().equals("on")) {
            leagueMatchInfo.setKeyword(batchBean.getKeyword());
        }
        leagueMatchRepository.save(leagueMatchInfo);
    }

	@RequestMapping(value = { "{id}/code" }, method = RequestMethod.GET)
	public String code(Model model, @PathVariable("id") Long id) {
		LeagueMatch leagueMatch = leagueMatchRepository.findOne(id);
		model.addAttribute("leagueMatch", leagueMatch);

		LeagueMatchCode leagueMatchCode = leagueMatchCodeRepository
				.findByItemTypeAndItemId(ItemTypeEnum.LEAGUE_MATCH.getKey(), id);
		model.addAttribute("leagueMatchCode", leagueMatchCode);

		model.addAttribute("providerType", AppGlobal.getProviderType());
		return "league/leagueMatch/code";
	}

	@RequestMapping(value = { "{id}/changeData" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult changeData(@PathVariable("id") Long id,
			IdDataBean<String> bean) {
		LeagueMatchCode leagueMatchCode = leagueMatchCodeRepository
				.findByItemTypeAndItemId(ItemTypeEnum.LEAGUE_MATCH.getKey(), id);
		if (leagueMatchCode == null) {
			leagueMatchCode = new LeagueMatchCode();
			leagueMatchCode.setItemType(ItemTypeEnum.LEAGUE_MATCH.getKey());
			leagueMatchCode.setItemId(id);
		}
		if (bean.getType() != null) {
			
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "batchDelete" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchDelete(@RequestBody BatchBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String[] itemIdArr = itemIds.split(",");
		if (null != itemIdArr) {
			for (String itemIdStr : itemIdArr) {
				Long itemId = Long.valueOf(itemIdStr);
				LeagueMatch leagueMatchInfo = leagueMatchRepository
						.findOne(itemId);
				if (leagueMatchInfo != null
						&& leagueMatchInfo.getStatus() == OnlineStatusEnum.OFFLINE
								.getKey()) {
					leagueService.deleteLeagueMatch(leagueMatchInfo);
				}
			}
		}
		return new BaseResult();
	}
}
