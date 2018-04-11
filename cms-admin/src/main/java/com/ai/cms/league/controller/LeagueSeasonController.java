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
import com.ai.AppGlobal;
import com.ai.cms.config.entity.Cp;
import com.ai.cms.config.repository.CpRepository;
import com.ai.cms.injection.enums.ProviderTypeEnum;
import com.ai.cms.league.entity.League;
import com.ai.cms.league.entity.LeagueMatchCode;
import com.ai.cms.league.entity.LeagueSeason;
import com.ai.cms.league.repository.LeagueMatchCodeRepository;
import com.ai.cms.league.repository.LeagueRepository;
import com.ai.cms.league.repository.LeagueSeasonRepository;
import com.ai.cms.league.service.LeagueService;
import com.ai.cms.live.entity.Channel;
import com.ai.cms.live.entity.Schedule;
import com.ai.cms.live.repository.ChannelRepository;
import com.ai.cms.live.repository.ScheduleRepository;
import com.ai.cms.media.bean.IdDataBean;
import com.ai.cms.media.bean.ImageBean;
import com.ai.cms.media.entity.Program;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.ItemTypeEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.utils.BeanInfoUtil;

@Controller
@RequestMapping(value = { "/league/leagueSeason" })
public class LeagueSeasonController extends AbstractImageController {

	@Autowired
	private LeagueSeasonRepository leagueSeasonRepository;

	@Autowired
	private LeagueService leagueService;

	@Autowired
	private LeagueRepository leagueRepository;

	@Autowired
	private LeagueMatchCodeRepository leagueMatchCodeRepository;

	@Autowired
	private ChannelRepository channelRepository;

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private CpRepository cpRepository;

	private void setModel(Model model) {
		model.addAttribute("statusEnum", OnlineStatusEnum.values());
		model.addAttribute("yesNoEnum", YesNoEnum.values());

		List<Cp> cpList = cpRepository.findAll();
		model.addAttribute("cpList", cpList);
	}

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}
		Page<LeagueSeason> page = find(request, pageInfo,
				leagueSeasonRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "league/leagueSeason/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model) {
		LeagueSeason leagueSeason = new LeagueSeason();
		model.addAttribute("leagueSeason", leagueSeason);

		setModel(model);

		return "league/leagueSeason/edit";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody ImageBean<LeagueSeason> imageBean) {
		return edit(imageBean, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		LeagueSeason leagueSeason = leagueSeasonRepository.findOne(id);
		model.addAttribute("leagueSeason", leagueSeason);

		if (leagueSeason != null && leagueSeason.getLeagueId() != null) {
			League league = leagueRepository
					.findOne(leagueSeason.getLeagueId());
			model.addAttribute("league", league);
		}

		if (leagueSeason != null && leagueSeason.getChannelId() != null) {
			Channel channel = channelRepository.findOne(leagueSeason
					.getChannelId());
			model.addAttribute("channel", channel);
		}
		if (leagueSeason != null && leagueSeason.getScheduleId() != null) {
			Schedule schedule = scheduleRepository.findOne(leagueSeason
					.getScheduleId());
			model.addAttribute("schedule", schedule);
		}

		Program program = leagueService.getSplitProgram(leagueSeason);
		model.addAttribute("program", program);

		setModel(model);

		if (leagueSeason.getStatus() == OnlineStatusEnum.ONLINE.getKey()
				|| leagueSeason.getStatus() == OnlineStatusEnum.OFFLINE
						.getKey()) {
			OnlineStatusEnum[] statusEnum = { OnlineStatusEnum.ONLINE,
					OnlineStatusEnum.OFFLINE };
			model.addAttribute("statusEnum", statusEnum);
		}

		return "league/leagueSeason/edit";
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody ImageBean<LeagueSeason> imageBean,
			@PathVariable("id") Long id) {
		LeagueSeason leagueSeason = imageBean.getData();
		String image1Data = imageBean.getImage1Data();
		String image2Data = imageBean.getImage2Data();

		LeagueSeason leagueSeasonInfo = null;
		String image1Old = "";
		String image2Old = "";
		if (id != null && id > 0) {
			leagueSeasonInfo = leagueSeasonRepository.findOne(id);
			image1Old = leagueSeasonInfo.getImage1();
			image2Old = leagueSeasonInfo.getImage2();
			BeanInfoUtil
					.bean2bean(
							leagueSeason,
							leagueSeasonInfo,
							"leagueId,name,title,searchName,tag,keyword,area,viewpoint,info,beginTime,endTime,duration,image1,image2,status,"
									+ "channelId,scheduleId,splitProgram,mediaId,mediaEpisode,programId,cpId");
		} else {
			leagueSeasonInfo = leagueSeason;
		}

		String image1 = "";
		String image2 = "";
		if (StringUtils.isNotEmpty(image1Data)) {
			image1 = upload(AdminConstants.MODULE_RESOURCE_LEAGUE,
					AdminConstants.RESOURCE_TYPE_POSTER, image1Data);
			leagueSeasonInfo.setImage1(image1);
		}
		if (StringUtils.isNotEmpty(image2Data)) {
			image2 = upload(AdminConstants.MODULE_RESOURCE_LEAGUE,
					AdminConstants.RESOURCE_TYPE_POSTER, image2Data);
			leagueSeasonInfo.setImage2(image2);
		}
		leagueService.saveLeagueSeason(leagueSeasonInfo);

		if (!StringUtils.trimToEmpty(image1Old).equals(
				StringUtils.trimToEmpty(leagueSeason.getImage1()))) {
			// deleteOldResource(image1Old);
		}

		if (!StringUtils.trimToEmpty(image2Old).equals(
				StringUtils.trimToEmpty(leagueSeason.getImage2()))) {
			// deleteOldResource(image2Old);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		LeagueSeason leagueSeasonInfo = leagueSeasonRepository.findOne(id);
		if (leagueSeasonInfo != null) {
			String image1Old = leagueSeasonInfo.getImage1();
			String image2Old = leagueSeasonInfo.getImage2();

			leagueService.deleteLeagueSeason(leagueSeasonInfo);

			deleteOldResource(image1Old);
			deleteOldResource(image2Old);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, @PathVariable("id") Long id) {
		LeagueSeason leagueSeason = leagueSeasonRepository.findOne(id);
		model.addAttribute("leagueSeason", leagueSeason);

		if (leagueSeason != null && leagueSeason.getLeagueId() != null) {
			League league = leagueRepository
					.findOne(leagueSeason.getLeagueId());
			model.addAttribute("league", league);
		}

		if (leagueSeason != null && leagueSeason.getChannelId() != null) {
			Channel channel = channelRepository.findOne(leagueSeason
					.getChannelId());
			model.addAttribute("channel", channel);
		}
		if (leagueSeason != null && leagueSeason.getScheduleId() != null) {
			Schedule schedule = scheduleRepository.findOne(leagueSeason
					.getScheduleId());
			model.addAttribute("schedule", schedule);
		}

		Program program = leagueService.getSplitProgram(leagueSeason);
		model.addAttribute("program", program);

		setModel(model);

		return "league/leagueSeason/detail";
	}

	@RequestMapping(value = { "{id}/status" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult status(@PathVariable("id") Long id) {
		LeagueSeason leagueSeasonInfo = leagueSeasonRepository.findOne(id);
		if (leagueSeasonInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			leagueSeasonInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
		} else {
			leagueSeasonInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
		}
		if (leagueSeasonInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			leagueSeasonInfo.setOnlineTime(new Date());
			leagueSeasonInfo.setOfflineTime(null);
		} else if (leagueSeasonInfo.getStatus() == OnlineStatusEnum.OFFLINE.getKey()) {
			leagueSeasonInfo.setOfflineTime(new Date());
		}
		leagueService.saveLeagueSeason(leagueSeasonInfo);
		return new BaseResult();
	}

	@RequestMapping(value = { "selectLeagueSeason" })
	public String selectLeagueSeason(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}
		Page<LeagueSeason> page = find(request, pageInfo,
				leagueSeasonRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "league/leagueSeason/selectLeagueSeason";
	}

	@RequestMapping(value = { "selectItem" })
	public String selectItem(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}
		Page<LeagueSeason> page = find(request, pageInfo,
				leagueSeasonRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "league/leagueSeason/selectItem";
	}

	@RequestMapping(value = { "{id}/code" }, method = RequestMethod.GET)
	public String code(Model model, @PathVariable("id") Long id) {
		LeagueSeason leagueSeason = leagueSeasonRepository.findOne(id);
		model.addAttribute("leagueSeason", leagueSeason);

		LeagueMatchCode leagueMatchCode = leagueMatchCodeRepository
				.findByItemTypeAndItemId(ItemTypeEnum.LEAGUE_SEASON.getKey(),
						id);
		model.addAttribute("leagueMatchCode", leagueMatchCode);

		model.addAttribute("providerType", AppGlobal.getProviderType());
		return "league/leagueSeason/code";
	}

	@RequestMapping(value = { "{id}/changeData" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult changeData(@PathVariable("id") Long id,
			IdDataBean<String> bean) {
		LeagueMatchCode leagueMatchCode = leagueMatchCodeRepository
				.findByItemTypeAndItemId(ItemTypeEnum.LEAGUE_SEASON.getKey(),
						id);
		if (leagueMatchCode == null) {
			leagueMatchCode = new LeagueMatchCode();
			leagueMatchCode.setItemType(ItemTypeEnum.LEAGUE_SEASON.getKey());
			leagueMatchCode.setItemId(id);
		}
		if (bean.getType() != null) {
			
		}
		return new BaseResult();
	}

}
