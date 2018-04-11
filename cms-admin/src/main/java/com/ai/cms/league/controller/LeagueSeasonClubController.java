package com.ai.cms.league.controller;

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
import com.ai.cms.league.entity.LeagueSeason;
import com.ai.cms.league.entity.LeagueSeasonClub;
import com.ai.cms.league.entity.LeagueSeasonClubView;
import com.ai.cms.league.repository.LeagueSeasonClubRepository;
import com.ai.cms.league.repository.LeagueSeasonClubViewRepository;
import com.ai.cms.league.repository.LeagueSeasonRepository;
import com.ai.cms.media.bean.ImageBean;
import com.ai.cms.star.entity.Club;
import com.ai.cms.star.repository.ClubRepository;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.exception.ServiceException;
import com.ai.common.utils.BeanInfoUtil;

@Controller
@RequestMapping(value = { "/league/{leagueSeasonId}/leagueSeasonClub" })
public class LeagueSeasonClubController extends AbstractImageController {

	@Autowired
	private LeagueSeasonRepository leagueSeasonRepository;

	@Autowired
	private LeagueSeasonClubRepository leagueSeasonClubRepository;

	@Autowired
	private LeagueSeasonClubViewRepository leagueSeasonClubViewRepository;

	@Autowired
	private ClubRepository clubRepository;

	private void setModel(Model model) {
		model.addAttribute("statusEnum", OnlineStatusEnum.values());
	}

	@RequestMapping(value = { "" })
	public String list(@PathVariable("leagueSeasonId") Long leagueSeasonId,
			Model model, HttpServletRequest request, PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("sortIndex");
		}

		LeagueSeason leagueSeason = leagueSeasonRepository
				.findOne(leagueSeasonId);
		model.addAttribute("leagueSeason", leagueSeason);

		Page<LeagueSeasonClubView> page = find(request, pageInfo,
				leagueSeasonClubViewRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "league/leagueSeasonClub/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model,
			@PathVariable("leagueSeasonId") Long leagueSeasonId) {
		LeagueSeasonClubView leagueSeasonClubView = new LeagueSeasonClubView();
		model.addAttribute("leagueSeasonClub", leagueSeasonClubView);

		LeagueSeason leagueSeason = leagueSeasonRepository
				.findOne(leagueSeasonId);
		model.addAttribute("leagueSeason", leagueSeason);

		setModel(model);

		return "league/leagueSeasonClub/edit";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@PathVariable("leagueSeasonId") Long leagueSeasonId,
			@RequestBody ImageBean<LeagueSeasonClub> imageBean) {
		return edit(leagueSeasonId, imageBean, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model,
			@PathVariable("leagueSeasonId") Long leagueSeasonId,
			@PathVariable("id") Long id) {
		LeagueSeasonClubView leagueSeasonClubView = leagueSeasonClubViewRepository
				.findOne(id);
		model.addAttribute("leagueSeasonClub", leagueSeasonClubView);

		LeagueSeason leagueSeason = leagueSeasonRepository
				.findOne(leagueSeasonId);
		model.addAttribute("leagueSeason", leagueSeason);

		setModel(model);

		return "league/leagueSeasonClub/edit";
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@PathVariable("leagueSeasonId") Long leagueSeasonId,
			@RequestBody ImageBean<LeagueSeasonClub> imageBean,
			@PathVariable("id") Long id) {
		LeagueSeasonClub leagueSeasonClub = imageBean.getData();
		String image1Data = imageBean.getImage1Data();
		String image2Data = imageBean.getImage2Data();

		LeagueSeasonClub leagueSeasonClubInfo = null;
		String image1Old = "";
		String image2Old = "";
		if (id == null) {
			leagueSeasonClubInfo = leagueSeasonClub;

			LeagueSeasonClub existLeagueSeasonClub = leagueSeasonClubRepository
					.findOneByLeagueSeasonIdAndItemTypeAndItemId(
							leagueSeasonId, leagueSeasonClub.getItemType(),
							leagueSeasonClub.getItemId());
			if (existLeagueSeasonClub != null) {
				Club club = clubRepository
						.findOne(leagueSeasonClub.getItemId());
				throw new ServiceException("俱乐部["
						+ ((club != null) ? club.getName() : "") + "]已存在该赛季中！");
			}
		} else {
			leagueSeasonClubInfo = leagueSeasonClubRepository.findOne(id);
			image1Old = leagueSeasonClubInfo.getImage1();
			image2Old = leagueSeasonClubInfo.getImage2();
			BeanInfoUtil
					.bean2bean(
							leagueSeasonClub,
							leagueSeasonClubInfo,
							"title,image1,image2,sortIndex,status,winNum,flatNum,loseNum,enterNum,pointNum,fumbleNum,creditNum");
		}

		String image1 = "";
		String image2 = "";
		if (StringUtils.isNotEmpty(image1Data)) {
			image1 = upload(AdminConstants.MODULE_RESOURCE_LEAGUE,
					AdminConstants.RESOURCE_TYPE_POSTER, image1Data);
			leagueSeasonClubInfo.setImage1(image1);
		}
		if (StringUtils.isNotEmpty(image2Data)) {
			image2 = upload(AdminConstants.MODULE_RESOURCE_LEAGUE,
					AdminConstants.RESOURCE_TYPE_POSTER, image2Data);
			leagueSeasonClubInfo.setImage2(image2);
		}
		leagueSeasonClubRepository.save(leagueSeasonClubInfo);

		if (!StringUtils.trimToEmpty(image1Old).equals(
				StringUtils.trimToEmpty(leagueSeasonClub.getImage1()))) {
			// deleteOldResource(image1Old);
		}

		if (!StringUtils.trimToEmpty(image2Old).equals(
				StringUtils.trimToEmpty(leagueSeasonClub.getImage2()))) {
			// deleteOldResource(image2Old);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(
			@PathVariable("leagueSeasonId") Long leagueSeasonId,
			@PathVariable("id") Long id) {
		LeagueSeasonClub leagueSeasonClubInfo = leagueSeasonClubRepository
				.findOne(id);
		if (leagueSeasonClubInfo != null) {
			String image1Old = leagueSeasonClubInfo.getImage1();
			String image2Old = leagueSeasonClubInfo.getImage2();
			leagueSeasonClubRepository.delete(leagueSeasonClubInfo);

			deleteOldResource(image1Old);
			deleteOldResource(image2Old);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/status" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult status(@PathVariable("id") Long id) {
		LeagueSeasonClub leagueSeasonClubInfo = leagueSeasonClubRepository
				.findOne(id);
		if (leagueSeasonClubInfo.getStatus() == OnlineStatusEnum.ONLINE
				.getKey()) {
			leagueSeasonClubInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
		} else {
			leagueSeasonClubInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
		}
		leagueSeasonClubRepository.save(leagueSeasonClubInfo);
		return new BaseResult();
	}
}
