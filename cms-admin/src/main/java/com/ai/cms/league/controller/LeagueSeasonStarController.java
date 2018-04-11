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
import com.ai.cms.league.entity.LeagueSeasonStar;
import com.ai.cms.league.entity.LeagueSeasonStarView;
import com.ai.cms.league.repository.LeagueSeasonRepository;
import com.ai.cms.league.repository.LeagueSeasonStarRepository;
import com.ai.cms.league.repository.LeagueSeasonStarViewRepository;
import com.ai.cms.media.bean.ImageBean;
import com.ai.cms.star.entity.Star;
import com.ai.cms.star.repository.StarRepository;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.exception.ServiceException;
import com.ai.common.utils.BeanInfoUtil;

@Controller
@RequestMapping(value = { "/league/{leagueSeasonId}/leagueSeasonStar" })
public class LeagueSeasonStarController extends AbstractImageController {

	@Autowired
	private LeagueSeasonRepository leagueSeasonRepository;

	@Autowired
	private LeagueSeasonStarRepository leagueSeasonStarRepository;

	@Autowired
	private LeagueSeasonStarViewRepository leagueSeasonStarViewRepository;

	@Autowired
	private StarRepository starRepository;

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

		Page<LeagueSeasonStarView> page = find(request, pageInfo,
				leagueSeasonStarViewRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "league/leagueSeasonStar/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model,
			@PathVariable("leagueSeasonId") Long leagueSeasonId) {
		LeagueSeasonStarView leagueSeasonStarView = new LeagueSeasonStarView();
		model.addAttribute("leagueSeasonStar", leagueSeasonStarView);

		LeagueSeason leagueSeason = leagueSeasonRepository
				.findOne(leagueSeasonId);
		model.addAttribute("leagueSeason", leagueSeason);

		setModel(model);

		return "league/leagueSeasonStar/edit";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@PathVariable("leagueSeasonId") Long leagueSeasonId,
			@RequestBody ImageBean<LeagueSeasonStar> imageBean) {
		return edit(leagueSeasonId, imageBean, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model,
			@PathVariable("leagueSeasonId") Long leagueSeasonId,
			@PathVariable("id") Long id) {
		LeagueSeasonStarView leagueSeasonStarView = leagueSeasonStarViewRepository
				.findOne(id);
		model.addAttribute("leagueSeasonStar", leagueSeasonStarView);

		LeagueSeason leagueSeason = leagueSeasonRepository
				.findOne(leagueSeasonId);
		model.addAttribute("leagueSeason", leagueSeason);

		setModel(model);

		return "league/leagueSeasonStar/edit";
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@PathVariable("leagueSeasonId") Long leagueSeasonId,
			@RequestBody ImageBean<LeagueSeasonStar> imageBean,
			@PathVariable("id") Long id) {
		LeagueSeasonStar leagueSeasonStar = imageBean.getData();
		String image1Data = imageBean.getImage1Data();
		String image2Data = imageBean.getImage2Data();

		LeagueSeasonStar leagueSeasonStarInfo = null;
		String image1Old = "";
		String image2Old = "";
		if (id == null) {
			leagueSeasonStarInfo = leagueSeasonStar;

			LeagueSeasonStar existLeagueSeasonStar = leagueSeasonStarRepository
					.findOneByLeagueSeasonIdAndItemTypeAndItemId(
							leagueSeasonId, leagueSeasonStar.getItemType(),
							leagueSeasonStar.getItemId());
			if (existLeagueSeasonStar != null) {
				Star star = starRepository
						.findOne(leagueSeasonStar.getItemId());
				throw new ServiceException("球星["
						+ ((star != null) ? star.getName() : "") + "]已存在该赛季中！");
			}
		} else {
			leagueSeasonStarInfo = leagueSeasonStarRepository.findOne(id);
			image1Old = leagueSeasonStarInfo.getImage1();
			image2Old = leagueSeasonStarInfo.getImage2();
			BeanInfoUtil
					.bean2bean(leagueSeasonStar, leagueSeasonStarInfo,
							"title,image1,image2,sortIndex,status,siteNum,enterNum,pointNum,assistNum");
		}

		String image1 = "";
		String image2 = "";
		if (StringUtils.isNotEmpty(image1Data)) {
			image1 = upload(AdminConstants.MODULE_RESOURCE_LEAGUE,
					AdminConstants.RESOURCE_TYPE_POSTER, image1Data);
			leagueSeasonStarInfo.setImage1(image1);
		}
		if (StringUtils.isNotEmpty(image2Data)) {
			image2 = upload(AdminConstants.MODULE_RESOURCE_LEAGUE,
					AdminConstants.RESOURCE_TYPE_POSTER, image2Data);
			leagueSeasonStarInfo.setImage2(image2);
		}
		leagueSeasonStarRepository.save(leagueSeasonStarInfo);

		if (!StringUtils.trimToEmpty(image1Old).equals(
				StringUtils.trimToEmpty(leagueSeasonStar.getImage1()))) {
			// deleteOldResource(image1Old);
		}

		if (!StringUtils.trimToEmpty(image2Old).equals(
				StringUtils.trimToEmpty(leagueSeasonStar.getImage2()))) {
			// deleteOldResource(image2Old);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(
			@PathVariable("leagueSeasonId") Long leagueSeasonId,
			@PathVariable("id") Long id) {
		LeagueSeasonStar leagueSeasonStarInfo = leagueSeasonStarRepository
				.findOne(id);
		if (leagueSeasonStarInfo != null) {
			String image1Old = leagueSeasonStarInfo.getImage1();
			String image2Old = leagueSeasonStarInfo.getImage2();
			leagueSeasonStarRepository.delete(leagueSeasonStarInfo);

			deleteOldResource(image1Old);
			deleteOldResource(image2Old);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/status" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult status(@PathVariable("id") Long id) {
		LeagueSeasonStar leagueSeasonStarInfo = leagueSeasonStarRepository
				.findOne(id);
		if (leagueSeasonStarInfo.getStatus() == OnlineStatusEnum.ONLINE
				.getKey()) {
			leagueSeasonStarInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
		} else {
			leagueSeasonStarInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
		}
		leagueSeasonStarRepository.save(leagueSeasonStarInfo);
		return new BaseResult();
	}
}
