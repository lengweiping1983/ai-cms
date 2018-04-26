package com.ai.epg.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ai.AppGlobal;
import com.ai.cms.injection.enums.ProviderTypeEnum;
import com.ai.cms.injection.repository.InjectionObjectRepository;
import com.ai.cms.league.entity.League;
import com.ai.cms.league.entity.LeagueMatch;
import com.ai.cms.league.entity.LeagueMatchCode;
import com.ai.cms.league.entity.LeagueSeason;
import com.ai.cms.league.entity.LeagueSeasonClubView;
import com.ai.cms.league.entity.LeagueSeasonStarView;
import com.ai.cms.league.repository.LeagueMatchRepository;
import com.ai.cms.league.repository.LeagueRepository;
import com.ai.cms.league.repository.LeagueSeasonClubRepository;
import com.ai.cms.league.repository.LeagueSeasonRepository;
import com.ai.cms.league.repository.LeagueSeasonStarViewRepository;
import com.ai.cms.live.entity.ChannelConfig;
import com.ai.cms.live.entity.Schedule;
import com.ai.cms.live.repository.ChannelConfigRepository;
import com.ai.cms.live.repository.ScheduleRepository;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.SplitRProgram;
import com.ai.cms.media.entity.Trailer;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.cms.media.repository.SplitRProgramRepository;
import com.ai.cms.media.repository.TrailerRepository;
import com.ai.cms.star.entity.Club;
import com.ai.cms.star.repository.ClubRepository;
import com.ai.common.bean.PageInfo;
import com.ai.common.enums.CategoryItemTypeEnum;
import com.ai.common.enums.ItemTypeEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.WidgetItemTypeEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.utils.DateUtils;
import com.ai.common.utils.PathUtils;
import com.ai.epg.category.entity.CategoryItemView;
import com.ai.epg.widget.entity.WidgetItemView;

/**
 * 内容相关的接口
 *
 */
@Component
public class LeagueFacade {

	private static ProgramRepository programRepository;

	private static LeagueRepository leagueRepository;

	private static TrailerRepository trailerRepository;

	private static LeagueSeasonRepository leagueSeasonRepository;

	private static LeagueMatchRepository leagueMatchRepository;

	private static SplitRProgramRepository splitRProgramRepository;

	private static LeagueSeasonClubRepository leagueSeasonClubRepository;

	private static LeagueSeasonStarViewRepository leagueSeasonStarViewRepository;

	private static ChannelConfigRepository channelConfigRepository;

	private static ClubRepository clubRepository;

	private static ScheduleRepository scheduleRepository;

	private static InjectionObjectRepository injectionObjectRepository;

	@Autowired
	public void setProgramRepository(ProgramRepository programRepository) {
		LeagueFacade.programRepository = programRepository;
	}

	@Autowired
	public void setLeagueRepository(LeagueRepository leagueRepository) {
		LeagueFacade.leagueRepository = leagueRepository;
	}

	@Autowired
	public void setTrailerRepository(TrailerRepository trailerRepository) {
		LeagueFacade.trailerRepository = trailerRepository;
	}

	@Autowired
	public void setLeagueSeasonRepository(
			LeagueSeasonRepository leagueSeasonRepository) {
		LeagueFacade.leagueSeasonRepository = leagueSeasonRepository;
	}

	@Autowired
	public void setLeagueMatchRepository(
			LeagueMatchRepository leagueMatchRepository) {
		LeagueFacade.leagueMatchRepository = leagueMatchRepository;
	}

	@Autowired
	public void setSplitRProgramRepository(
			SplitRProgramRepository splitRProgramRepository) {
		LeagueFacade.splitRProgramRepository = splitRProgramRepository;
	}

	@Autowired
	public void setLeagueSeasonClubRepository(
			LeagueSeasonClubRepository leagueSeasonClubRepository) {
		LeagueFacade.leagueSeasonClubRepository = leagueSeasonClubRepository;
	}

	@Autowired
	public void setLeagueSeasonStarViewRepository(
			LeagueSeasonStarViewRepository leagueSeasonStarViewRepository) {
		LeagueFacade.leagueSeasonStarViewRepository = leagueSeasonStarViewRepository;
	}

	@Autowired
	public void setChannelConfigRepository(
			ChannelConfigRepository channelConfigRepository) {
		LeagueFacade.channelConfigRepository = channelConfigRepository;
	}

	@Autowired
	public void setClubRepository(ClubRepository clubRepository) {
		LeagueFacade.clubRepository = clubRepository;
	}

	@Autowired
	public void setScheduleRepository(ScheduleRepository scheduleRepository) {
		LeagueFacade.scheduleRepository = scheduleRepository;
	}

	@Autowired
	public void setInjectionObjectRepository(
			InjectionObjectRepository injectionObjectRepository) {
		LeagueFacade.injectionObjectRepository = injectionObjectRepository;
	}

	public static String getLiveDesc() {
		if ("shdx".equalsIgnoreCase(AppGlobal.getProfiles())) {
			return "比赛";
		}
		return "直播";
	}

	/**
	 * 通过code获取联赛对象
	 * 
	 * 
	 * @param series
	 * @param defaultStr
	 * @return
	 */
	public static League getLeague(final String appCode, final String defaultStr) {
		

		if (defaultStr == null) {
			return null;
		}
		return leagueRepository.findOneByCode(defaultStr);
	}

	/**
	 * 通过联赛获取当前赛季对象
	 * 
	 * 
	 * @param League
	 * @param List
	 *            <LeagueSeason>
	 * @return
	 */

	public static LeagueSeason getLeagueSeason(final String appCode,
			League league) {
		
		List<LeagueSeason> leagueSeasonList = leagueSeasonRepository
				.findLeagueSeason(league.getId());
		LeagueSeason leagueSeason = new LeagueSeason();
		if (leagueSeasonList != null && leagueSeasonList.size() > 0) {
			leagueSeason = leagueSeasonList.get(0);
		}
		return leagueSeason;
	}

	/**
	 * 通过当前赛季的俱乐部
	 * 
	 * 
	 * @param League
	 * @param List
	 *            <LeagueSeasonClubView>
	 * @return
	 */

	public static Page<LeagueSeasonClubView> getByLeagueSeasonClub(
			final String appCode, LeagueSeason leagueSeason, int pageCount,
			int maxNum) {
		

		PageInfo pageInfo = new PageInfo(pageCount, maxNum);
		Page<LeagueSeasonClubView> page = leagueSeasonClubRepository
				.findPageListByLeagueSeasonIdClub(leagueSeason.getId(),
						pageInfo.getPageRequest());
		return page;
	}

	public static Map<Long, Club> getByLeagueMatchClub(final String appCode,
			List<LeagueMatch> leagueMatchList) {
		Map<Long, Club> result = new HashMap<Long, Club>();
		List<Long> itemIdList = new ArrayList<Long>();
		if (leagueMatchList != null && leagueMatchList.size() > 0) {
			for (int i = 0; i < leagueMatchList.size(); i++) {
				if (leagueMatchList.get(i).getHomeId() != null) {
					itemIdList.add(leagueMatchList.get(i).getHomeId());
				}
				if (leagueMatchList.get(i).getGuestId() != null) {
					itemIdList.add(leagueMatchList.get(i).getGuestId());
				}
			}
		}
		if (itemIdList.size() > 0) {
			List<Club> clubList = clubRepository.findAll(itemIdList);
			for (Club club : clubList) {
				result.put(club.getId(), club);
			}
		}
		return result;
	}

	public static Integer getProviderId() {
		if (AppGlobal.providerTypeId != null) {
			return AppGlobal.providerTypeId;
		}
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		String providerIdS = request
				.getParameter(AppGlobal.P_PARTNER_PROVIDER_ID);
		if (StringUtils.isEmpty(providerIdS)) {
			providerIdS = (String) request
					.getAttribute(AppGlobal.P_PARTNER_PROVIDER_ID);
		}
		if (StringUtils.isEmpty(providerIdS)) {
			providerIdS = AppGlobal.getProviderIdFromCookie(request);
		}
		if (StringUtils.isNotEmpty(providerIdS)) {
			Integer providerId = Integer.valueOf(providerIdS);
			return providerId;
		}
		return null;
	}

	public static ChannelConfig getChannelConfigByChannel(Long channelId,
			Map<String, ChannelConfig> channelMap, Integer providerId) {
		ChannelConfig channelConfig = null;
		if (providerId != null) {
			channelConfig = channelMap.get(providerId + ":" + channelId);
		}
		if (channelConfig == null) {
			channelConfig = channelMap.get("" + channelId + "");
		}
		return channelConfig;
	}

	public static Map<String, ChannelConfig> getChannelMap(final String appCode) {
		

		Map<String, ChannelConfig> channelMap = new HashMap<String, ChannelConfig>();
		List<ChannelConfig> channelConfigList = channelConfigRepository
				.findAllChannelConfig();
		for (ChannelConfig channelConfig : channelConfigList) {
			if (channelConfig.getProviderType() != null) {
				channelMap.put(channelConfig.getProviderType().intValue() + ":"
						+ channelConfig.getChannelId(), channelConfig);
				channelMap
						.put("" + channelConfig.getChannelId(), channelConfig);
			} else {
				channelMap
						.put("" + channelConfig.getChannelId(), channelConfig);
			}
		}
		return channelMap;
	}

	/**
	 * 计算赛季状态
	 * 
	 * @param leagueSeason
	 * @return
	 */
	private static LeagueSeason sumLeagueSeason(final String appCode,
			LeagueSeason leagueSeason, Map<String, ChannelConfig> channelMap, Integer providerId,
			Map<String, SplitRProgram> splitRProgramMap,
			Map<Long, Program> programMap, Map<String, Program> mediaMap) {
		
		if (leagueSeason == null) {
			return null;
		}
		Date currentTime = new Date();
		leagueSeason.setPlayBeginTime(leagueSeason.getBeginTime());
		leagueSeason.setPlayEndTime(leagueSeason.getEndTime());
		if (leagueSeason.getBeginTime() == null
				|| leagueSeason.getDuration() == null) {
			return leagueSeason;
		}

		// 0=数据错误(页面提示内容已下架)，1=未开赛，2=即将开始，3=正在直播，4=已完赛，5=精彩回看，6=暂无直播
		Date beginTime = leagueSeason.getBeginTime();
		Date endTime = DateUtils.addMinutes(beginTime,
				leagueSeason.getDuration());

		if (currentTime.getTime() >= endTime.getTime()) {
//			if (checkProgramStatusByMediaIdAndEpisodeIndex(
//					ItemTypeEnum.LEAGUE_SEASON.getKey(), leagueSeason.getId(),
//					leagueSeason.getSplitProgram(), leagueSeason.getMediaId(),
//					leagueSeason.getMediaEpisode(),
//					leagueSeason.getProgramId(), splitRProgramMap, programMap,
//					mediaMap)) {
//				// 已完赛并已拆条
//				leagueSeason.setPlayStatus(5);
//				leagueSeason.setPlayStatusDesc("精彩回看");
//				return leagueSeason;
//			}
		}

		ChannelConfig channelConfig = null;
		if (leagueSeason.getChannelId() != null) {
			channelConfig = getChannelConfigByChannel(
					leagueSeason.getChannelId(), channelMap, providerId);
		}
		int preTime = (channelConfig != null) ? channelConfig.getPreTime() : 0;// 赛前时长，单位分钟
		int sufTime = (channelConfig != null) ? channelConfig.getSufTime() : 0;// 赛后时长，单位分钟
		beginTime = DateUtils.addMinutes(beginTime, -preTime);
		endTime = DateUtils.addMinutes(endTime, sufTime);
		leagueSeason.setPlayBeginTime(beginTime);
		leagueSeason.setPlayEndTime(endTime);
		if (currentTime.getTime() < beginTime.getTime()) {// 未开赛
			if ((currentTime.getTime() + 30 * 60 * 1000) > leagueSeason
					.getBeginTime().getTime()) {
				if (channelConfig != null) {
					leagueSeason.setPlayStatus(2);
					leagueSeason.setPlayStatusDesc("即将开始");
				} else {
					leagueSeason.setPlayStatus(6);
					leagueSeason.setPlayStatusDesc("暂无" + getLiveDesc());
				}
			} else {
				leagueSeason.setPlayStatus(1);
				leagueSeason.setPlayStatusDesc("未开赛");
			}
			return leagueSeason;
		} else if (currentTime.getTime() >= beginTime.getTime()
				&& currentTime.getTime() < endTime.getTime()) {
			if (channelConfig != null) {
				leagueSeason.setPlayStatus(3);
				leagueSeason.setPlayStatusDesc("正在" + getLiveDesc());
			} else {
				leagueSeason.setPlayStatus(6);
				leagueSeason.setPlayStatusDesc("暂无" + getLiveDesc());
			}
			return leagueSeason;
		} else if (currentTime.getTime() >= endTime.getTime()) {// 已完赛,回看
			if (channelConfig != null && channelConfig.getLookBack() != null
					&& channelConfig.getLookBack() == YesNoEnum.YES.getKey()) {// 支持回看
				int enbodyDuration = channelConfig.getEnbodyDuration() != null ? channelConfig
						.getEnbodyDuration() : 0;// 单位分钟
				int lookBackDuration = channelConfig.getLookBackDuration() != null ? channelConfig
						.getLookBackDuration() * 60 : 0;// 单位小时，转化为分钟
				if (currentTime.getTime() >= (beginTime.getTime() + enbodyDuration * 60 * 1000)
						&& currentTime.getTime() < (beginTime.getTime() + lookBackDuration * 60 * 1000)) {
					boolean checkLookBackCodeResult = false;
					if (AppGlobal.isCheckLookBackCode()) {
						if (leagueSeason.getScheduleId() != null) {
							Schedule schedule = scheduleRepository
									.findOne(leagueSeason.getScheduleId());
							if (schedule != null) {
//								InjectionObject injectionObject = injectionObjectRepository
//										.findOneByItemTypeAndItemId(
//												InjectionItemTypeEnum.SCHEDULE
//														.getKey(), schedule
//														.getId());
//								if (injectionObject != null
//										&& StringUtils
//												.isNotEmpty(injectionObject
//														.getPartnerItemCode())) {
//									checkLookBackCodeResult = true;
//								}
							}
						}
					} else {
						checkLookBackCodeResult = true;
					}
					if (checkLookBackCodeResult) {
						leagueSeason.setPlayStatus(5);
						leagueSeason.setPlayStatusDesc("精彩回看");
						return leagueSeason;
					}
				}
			}
		}
		leagueSeason.setPlayStatus(4);
		leagueSeason.setPlayStatusDesc("已完赛");
		return leagueSeason;
	}

	/**
	 * 计算赛季状态
	 * 
	 * @param leagueSeason
	 * @return
	 */
	public static LeagueSeason sumLeagueSeason(final String appCode,
			LeagueSeason leagueSeason) {
		if (leagueSeason == null) {
			return leagueSeason;
		}
		Map<String, ChannelConfig> channelMap = getChannelMap(appCode);
		Integer providerId = getProviderId();
		sumLeagueSeason(appCode, leagueSeason, channelMap, providerId, null, null, null);
		return leagueSeason;
	}

	/**
	 * 计算赛季状态
	 * 
	 * @param leagueSeasonList
	 * @return
	 */
	public static List<LeagueSeason> sumLeagueSeasonList(final String appCode,
			List<LeagueSeason> leagueSeasonList) {
		Map<String, ChannelConfig> channelMap = getChannelMap(appCode);

		Map<String, SplitRProgram> splitRProgramMap = new HashMap<String, SplitRProgram>();

		Map<Long, Program> programMap = new HashMap<Long, Program>();
		Map<String, Program> mediaMap = new HashMap<String, Program>();

		List<Long> programIdList = new ArrayList<Long>();
		List<Long> mediaIdList = new ArrayList<Long>();

		List<Long> itemIdList = new ArrayList<Long>();
		for (LeagueSeason leagueSeason : leagueSeasonList) {
			if (!itemIdList.contains(leagueSeason.getId())) {
				itemIdList.add(leagueSeason.getId());
			}

			if (leagueSeason.getProgramId() != null
					&& !programIdList.contains(leagueSeason.getProgramId())) {
				programIdList.add(leagueSeason.getProgramId());
			}

//			if (leagueSeason.getMediaId() != null
//					&& !mediaIdList.contains(leagueSeason.getMediaId())) {
//				mediaIdList.add(leagueSeason.getMediaId());
//			}
		}

		if (itemIdList != null && itemIdList.size() > 0) {
			List<SplitRProgram> splitRProgramList = splitRProgramRepository
					.findByItemTypeAndItemIdIn(
							ItemTypeEnum.LEAGUE_SEASON.getKey(), itemIdList);
			for (SplitRProgram splitRProgram : splitRProgramList) {
				if (splitRProgram.getItemType() != null
						&& splitRProgram.getItemType() != null
						&& splitRProgram.getProgramId() != null) {
					splitRProgramMap.put(splitRProgram.getItemType().intValue()
							+ ":" + splitRProgram.getItemId().longValue(),
							splitRProgram);
					if (!programIdList.contains(splitRProgram.getProgramId())) {
						programIdList.add(splitRProgram.getProgramId());
					}
				}
			}
		}

		if (programIdList != null && programIdList.size() > 0) {
			List<Program> programList = programRepository
					.findByIdInAndStatus(programIdList);
			for (Program program : programList) {
				programMap.put(program.getId(), program);
			}
		}
		if (mediaIdList != null && mediaIdList.size() > 0) {
//			List<Program> programList = programRepository
//					.findByMediaIdIn(mediaIdList);
//			for (Program program : programList) {
//				if (program.getMediaId() != null
//						&& program.getEpisodeIndex() != null) {
//					mediaMap.put(program.getMediaId().longValue() + ":"
//							+ program.getEpisodeIndex().intValue(), program);
//				}
//			}
		}

		Integer providerId = getProviderId();
		for (LeagueSeason leagueSeason : leagueSeasonList) {
			sumLeagueSeason(appCode, leagueSeason, channelMap, providerId,
					splitRProgramMap, programMap, mediaMap);
		}
		return leagueSeasonList;
	}

	/**
	 * 根据赛事ID获取赛事信息
	 * 
	 * @return
	 */
	public static List<LeagueMatch> getMatchListInfo(final String appCode,
			List<CategoryItemView> CategoryItemviewList) {
		

		List<Long> idList = new ArrayList<Long>();
		for (CategoryItemView categoryItemView : CategoryItemviewList) {
			if (categoryItemView.getItemType() != null
					&& categoryItemView.getItemType() == CategoryItemTypeEnum.LEAGUE_MATCH
							.getKey()) {
				idList.add(categoryItemView.getItemId());
			}
		}
		if (idList != null && idList.size() > 0) {
			return leagueMatchRepository.findByIdIn(idList);
		}
		return new ArrayList<LeagueMatch>();
	}

	/**
	 * 检查拆条的节目是否已到本驻地
	 * 
	 * @param mediaId
	 * @param episodeIndex
	 * @param programId
	 * @return
	 */
	public static boolean checkProgramStatusByMediaIdAndEpisodeIndex(
			Integer itemType, Long itemId, Integer splitProgram, Long mediaId,
			Integer episodeIndex, Long programId,
			Map<String, SplitRProgram> splitRProgramMap,
			Map<Long, Program> programMap, Map<String, Program> mediaMap) {

		SplitRProgram splitRProgram = null;
		if (splitRProgramMap != null) {// 本地拆条操作
			splitRProgram = splitRProgramMap.get(itemType.intValue() + ":"
					+ itemId.longValue());
		} else {
			splitRProgram = splitRProgramRepository.findOneByItemTypeAndItemId(
					itemType, itemId);
		}
		if (splitRProgram != null && splitRProgram.getProgramId() != null) {
			Program program = null;
			if (programMap != null) {
				program = programMap.get(splitRProgram.getProgramId());
			} else {
				program = programRepository.findOne(splitRProgram
						.getProgramId());
			}
			if (program != null
					&& program.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
				return true;
			}
		}

		if (splitProgram != null && splitProgram == YesNoEnum.YES.getKey()) {// 中央拆条操作
			if (programId != null) {
				Program program = null;
				if (programMap != null) {
					program = programMap.get(programId);
				} else {
					program = programRepository.findOne(programId);
				}
				if (program != null
						&& program.getStatus() == OnlineStatusEnum.ONLINE
								.getKey()) {
					return true;
				}
			}
			if (mediaId != null && episodeIndex != null) {
				// 选择已分发的节目即可
				if (mediaMap != null) {
					Program program = mediaMap.get(mediaId.longValue() + ":"
							+ episodeIndex.intValue());
					if (program != null) {
						return true;
					}
				} else {
//					List<Program> programList = programRepository
//							.findByMediaIdAndEpisodeIndex(mediaId, episodeIndex);
//					if (programList != null && programList.size() > 0) {
//						return true;
//					}
				}
			}
		}
		return false;
	}

	public static boolean checkCdnFileCode(LeagueMatchCode leagueMatchCode) {
		String cdnFileCode = getCdnFileCode(leagueMatchCode);
		if (StringUtils.isNotEmpty(cdnFileCode)) {
			return true;
		}
		return false;
	}

	public static String getCdnFileCode(LeagueMatchCode leagueMatchCode) {
		String cdnFileCode = "";
		if (leagueMatchCode != null) {
			Integer providerId = getProviderId();
			if (providerId != null) {
			}
			if (StringUtils.isEmpty(cdnFileCode)) {
				cdnFileCode = StringUtils.trimToEmpty(leagueMatchCode.getChargeCode());
			}
		}
		return cdnFileCode;
	}

	/**
	 * 计算赛事状态
	 * 
	 * @param leagueMatch
	 * @return
	 */
	private static LeagueMatch sumLeagueMatch(final String appCode,
			LeagueMatch leagueMatch, Map<String, ChannelConfig> channelMap, Integer providerId,
			Map<String, SplitRProgram> splitRProgramMap,
			Map<Long, Program> programMap, Map<String, Program> mediaMap) {
		

		if (leagueMatch == null) {
			return null;
		}

		Date currentTime = new Date();
		leagueMatch.setPlayBeginTime(leagueMatch.getBeginTime());
		leagueMatch.setPlayEndTime(leagueMatch.getEndTime());
		if (leagueMatch.getBeginTime() == null
				|| leagueMatch.getDuration() == null) {
			return leagueMatch;
		}

		// 0=数据错误(页面提示内容已下架)，1=未开赛，2=即将开始，3=正在直播，4=已完赛，5=精彩回看，6=暂无直播
		Date beginTime = leagueMatch.getBeginTime();
		Date endTime = DateUtils.addMinutes(beginTime,
				leagueMatch.getDuration());

		if (currentTime.getTime() >= endTime.getTime()) {
//			if (checkProgramStatusByMediaIdAndEpisodeIndex(
//					ItemTypeEnum.LEAGUE_MATCH.getKey(), leagueMatch.getId(),
//					leagueMatch.getSplitProgram(), leagueMatch.getMediaId(),
//					leagueMatch.getMediaEpisode(), leagueMatch.getProgramId(),
//					splitRProgramMap, programMap, mediaMap)) {
//				// 已完赛并已拆条
//				leagueMatch.setPlayStatus(5);
//				leagueMatch.setPlayStatusDesc("精彩回看");
//				return leagueMatch;
//			}
		}

		ChannelConfig channelConfig = null;
		if (leagueMatch.getChannelId() != null) {
			channelConfig = getChannelConfigByChannel(
					leagueMatch.getChannelId(), channelMap, providerId);
		}
		int preTime = (channelConfig != null) ? channelConfig.getPreTime() : 0;// 赛前时长，单位分钟
		int sufTime = (channelConfig != null) ? channelConfig.getSufTime() : 0;// 赛后时长，单位分钟
		beginTime = DateUtils.addMinutes(beginTime, -preTime);
		endTime = DateUtils.addMinutes(endTime, sufTime);
		leagueMatch.setPlayBeginTime(beginTime);
		leagueMatch.setPlayEndTime(endTime);
		if (currentTime.getTime() < beginTime.getTime()) {// 未开赛
			if ((currentTime.getTime() + 30 * 60 * 1000) > leagueMatch
					.getBeginTime().getTime()) {
				if (channelConfig != null) {
					leagueMatch.setPlayStatus(2);
					leagueMatch.setPlayStatusDesc("即将开始");
				} else {
					leagueMatch.setPlayStatus(6);
					leagueMatch.setPlayStatusDesc("暂无" + getLiveDesc());
				}
			} else {
				leagueMatch.setPlayStatus(1);
				leagueMatch.setPlayStatusDesc("未开赛");
			}
			return leagueMatch;
		} else if (currentTime.getTime() >= beginTime.getTime()
				&& currentTime.getTime() < endTime.getTime()) {
			if (channelConfig != null) {
				leagueMatch.setPlayStatus(3);
				leagueMatch.setPlayStatusDesc("正在" + getLiveDesc());
			} else {
				leagueMatch.setPlayStatus(6);
				leagueMatch.setPlayStatusDesc("暂无" + getLiveDesc());
			}
			return leagueMatch;
		} else if (currentTime.getTime() >= endTime.getTime()) {// 已完赛,回看
			if (channelConfig != null && channelConfig.getLookBack() != null
					&& channelConfig.getLookBack() == YesNoEnum.YES.getKey()) {// 支持回看
				int enbodyDuration = channelConfig.getEnbodyDuration() != null ? channelConfig
						.getEnbodyDuration() : 0;// 单位分钟
				int lookBackDuration = channelConfig.getLookBackDuration() != null ? channelConfig
						.getLookBackDuration() * 60 : 0;// 单位小时，转化为分钟
				if (currentTime.getTime() >= (beginTime.getTime() + enbodyDuration * 60 * 1000)
						&& currentTime.getTime() < (beginTime.getTime() + lookBackDuration * 60 * 1000)) {
					boolean checkLookBackCodeResult = false;
					if (AppGlobal.isCheckLookBackCode()) {
						if (leagueMatch.getScheduleId() != null) {
							Schedule schedule = scheduleRepository
									.findOne(leagueMatch.getScheduleId());
							if (schedule != null) {
//								InjectionObject injectionObject = injectionObjectRepository
//										.findOneByItemTypeAndItemId(
//												InjectionItemTypeEnum.SCHEDULE
//														.getKey(), schedule
//														.getId());
//								if (injectionObject != null
//										&& StringUtils
//												.isNotEmpty(injectionObject
//														.getPartnerItemCode())) {
//									checkLookBackCodeResult = true;
//								}
							}
						}
					} else {
						checkLookBackCodeResult = true;
					}
					if (checkLookBackCodeResult) {
						leagueMatch.setPlayStatus(5);
						leagueMatch.setPlayStatusDesc("精彩回看");
						return leagueMatch;
					}
				}
			}
		}
		leagueMatch.setPlayStatus(4);
		leagueMatch.setPlayStatusDesc("已完赛");
		return leagueMatch;
	}

	/**
	 * 计算赛事状态
	 * 
	 * @param leagueMatch
	 * @return
	 */
	public static LeagueMatch sumLeagueMatch(final String appCode,
			LeagueMatch leagueMatch) {
		if (leagueMatch == null) {
			return leagueMatch;
		}
		Map<String, ChannelConfig> channelMap = getChannelMap(appCode);
		Integer providerId = getProviderId();
		sumLeagueMatch(appCode, leagueMatch, channelMap, providerId, null, null, null);
		return leagueMatch;
	}

	/**
	 * 计算赛事状态
	 * 
	 * @param leagueMatchList
	 * @return
	 */
	public static List<LeagueMatch> sumLeagueMatchList(final String appCode,
			List<LeagueMatch> leagueMatchList) {
		Map<String, ChannelConfig> channelMap = getChannelMap(appCode);

		Map<String, SplitRProgram> splitRProgramMap = new HashMap<String, SplitRProgram>();

		Map<Long, Program> programMap = new HashMap<Long, Program>();
		Map<String, Program> mediaMap = new HashMap<String, Program>();

		List<Long> programIdList = new ArrayList<Long>();
		List<Long> mediaIdList = new ArrayList<Long>();

		List<Long> itemIdList = new ArrayList<Long>();
		for (LeagueMatch leagueMatch : leagueMatchList) {
			if (!itemIdList.contains(leagueMatch.getId())) {
				itemIdList.add(leagueMatch.getId());
			}

			if (leagueMatch.getProgramId() != null
					&& !programIdList.contains(leagueMatch.getProgramId())) {
				programIdList.add(leagueMatch.getProgramId());
			}

//			if (leagueMatch.getMediaId() != null
//					&& !mediaIdList.contains(leagueMatch.getMediaId())) {
//				mediaIdList.add(leagueMatch.getMediaId());
//			}
		}

		if (itemIdList != null && itemIdList.size() > 0) {
			List<SplitRProgram> splitRProgramList = splitRProgramRepository
					.findByItemTypeAndItemIdIn(
							ItemTypeEnum.LEAGUE_MATCH.getKey(), itemIdList);
			for (SplitRProgram splitRProgram : splitRProgramList) {
				if (splitRProgram.getItemType() != null
						&& splitRProgram.getItemType() != null
						&& splitRProgram.getProgramId() != null) {
					splitRProgramMap.put(splitRProgram.getItemType().intValue()
							+ ":" + splitRProgram.getItemId().longValue(),
							splitRProgram);
					if (!programIdList.contains(splitRProgram.getProgramId())) {
						programIdList.add(splitRProgram.getProgramId());
					}
				}
			}
		}

		if (programIdList != null && programIdList.size() > 0) {
			List<Program> programList = programRepository
					.findByIdInAndStatus(programIdList);
			for (Program program : programList) {
				programMap.put(program.getId(), program);
			}
		}
		if (mediaIdList != null && mediaIdList.size() > 0) {
//			List<Program> programList = programRepository
//					.findByMediaIdIn(mediaIdList);
//			for (Program program : programList) {
//				if (program.getMediaId() != null
//						&& program.getEpisodeIndex() != null) {
//					mediaMap.put(program.getMediaId().longValue() + ":"
//							+ program.getEpisodeIndex().intValue(), program);
//				}
//			}
		}

		Integer providerId = getProviderId();
		for (LeagueMatch leagueMatch : leagueMatchList) {
			sumLeagueMatch(appCode, leagueMatch, channelMap, providerId, splitRProgramMap,
					programMap, mediaMap);
		}
		return leagueMatchList;
	}

	/**
	 * 根据当前时间获取获最近一场比赛
	 * 
	 * 
	 * @param
	 * @param 最近比赛列表
	 * @return leagueMatch
	 */
	public static LeagueMatch getLatestMatch(final String appCode,
			List<LeagueMatch> leagueMatchList) {
		

		if (leagueMatchList.size() > 0) {
			int lastestIndex = getLatestMatchIndex(appCode, leagueMatchList);
			return leagueMatchList.get(lastestIndex);
		}
		return null;
	}

	public static int getLatestMatchIndex(final String appCode,
			List<LeagueMatch> leagueMatchList) {
		if (leagueMatchList.size() > 0) {
			Date currentTime = new Date();
			int lastestIndex = 0;
			for (; lastestIndex < leagueMatchList.size(); lastestIndex++) {
				LeagueMatch leagueMatch = leagueMatchList.get(lastestIndex);
				if (leagueMatch.getBeginTime().getTime() >= currentTime
						.getTime()) {
					if (lastestIndex > 0) {
						LeagueMatch preLeagueMatch = leagueMatchList
								.get(lastestIndex - 1);
						if (Math.abs(currentTime.getTime()
								- preLeagueMatch.getBeginTime().getTime()) < Math
								.abs(currentTime.getTime()
										- leagueMatch.getBeginTime().getTime())) {
							lastestIndex = lastestIndex - 1;// 前一个离当前时间最近
						}
					}
					break;
				}
			}
			if (lastestIndex == leagueMatchList.size()) {
				lastestIndex = leagueMatchList.size() - 1;
			}
			return lastestIndex;
		}
		return 0;
	}

	/**
	 * 获取当前赛季前后几天的赛事情况
	 * 
	 * @param leagueSeason
	 * @param day
	 * @param maxNum
	 * @return
	 */
	public static List<LeagueMatch> getLeagueMatchTimeNowByDate(
			final String appCode, LeagueSeason leagueSeason, int day, int maxNum) {
		

		List<LeagueMatch> leagueMatchList = leagueMatchRepository
				.findByLeagueSeasonId(leagueSeason.getId());
		if (leagueMatchList == null || leagueMatchList.size() <= maxNum) {
			return leagueMatchList;
		}
		int lastestIndex = getLatestMatchIndex(appCode, leagueMatchList);
		int end = lastestIndex + maxNum / 2 + 1;// 最后一个元素
		if (end > leagueMatchList.size()) {
			end = leagueMatchList.size();// 列表中最后一个
		}
		if (end < maxNum) {
			end = maxNum;
		}
		List<LeagueMatch> resultList = leagueMatchList.subList(end - maxNum,
				end);
		return resultList;
	}

	/**
	 * 获取当前赛季的射手榜
	 * 
	 * 
	 * @param
	 * @param Series
	 * @return Program
	 */
	public static List<LeagueSeasonStarView> getPageListByLeagueSeasonIdStartSS(
			final String appCode, final LeagueSeason leagueSeason,
			final int maxWidth) {
		

		PageInfo pageInfo = new PageInfo(0, maxWidth);
		Page<LeagueSeasonStarView> page = leagueSeasonStarViewRepository
				.findPageListByLeagueSeasonIdStartSS(leagueSeason.getId(),
						pageInfo.getPageRequest());
		return page.getContent();
	}

	/**
	 * 获取当前赛季的助攻榜
	 * 
	 * 
	 * @param
	 * @param Series
	 * @return Program
	 */
	public static List<LeagueSeasonStarView> getPageListByLeagueSeasonIdStartZG(
			final String appCode, final LeagueSeason leagueSeason,
			final int maxWidth) {
		

		PageInfo pageInfo = new PageInfo(0, maxWidth);
		Page<LeagueSeasonStarView> page = leagueSeasonStarViewRepository
				.findPageListByLeagueSeasonIdStartZG(leagueSeason.getId(),
						pageInfo.getPageRequest());
		return page.getContent();
	}

	/**
	 * 获取当前赛季的第几赛程
	 * 
	 * 
	 * @param
	 * @param leagueSeason
	 * @return leagueMatch int
	 */
	public static Page<LeagueMatch> getLeagueSeasonMatchByIndex(
			final String appCode, final LeagueSeason leagueSeason,
			final int leagueIndex, final int currentPage, final int pageSize) {
		
		PageInfo pageInfo = new PageInfo(currentPage, pageSize);
		Page<LeagueMatch> page = leagueMatchRepository
				.findLeagueSeasonByLeagueIndex(leagueSeason.getId(),
						leagueIndex, pageInfo.getPageRequest());
		return page;
	}

	/**
	 * 获取近期赛事
	 * 
	 * 
	 * @param
	 * @param sportContentType
	 * @return leagueMatch
	 */
	public static Page<LeagueMatch> getNowTimeLeagueMatchByNowList(
			final String appCode, int sportContentType, final int currentPage,
			final int pageSize) {
		
		PageInfo pageInfo = new PageInfo(currentPage, pageSize);
		Page<LeagueMatch> page = leagueMatchRepository.getLeagueMatchListByNow(
				sportContentType, pageInfo.getPageRequest());
		return page;
	}

	/**
	 * 获取前3天后七天的比赛
	 * 
	 * 
	 * @param
	 * @param sportContentType
	 * @return leagueMatch
	 */
	public static Page<LeagueMatch> getLeagueMatchListByNow(
			final String appCode, int sportContentType, final int currentPage,
			final int pageSize) {
		
		Date beginTime = DateUtils.getDateBefore(new Date(), 3);
		Date endTime = DateUtils.getDateAfter(new Date(), 7);
		PageInfo pageInfo = new PageInfo(currentPage, pageSize);
		Page<LeagueMatch> page = leagueMatchRepository
				.getLeagueMatchListByNow(beginTime, endTime, sportContentType,
						pageInfo.getPageRequest());

		return page;
	}

	/**
	 * 根据赛事ID获取赛事详细信息
	 * 
	 * 
	 * @param
	 * @param code
	 * @return leagueMatch
	 */
	public static LeagueMatch getLeagueMatchById(final String appCode, Long id) {
		
		if (id != null) {
			LeagueMatch leagueMatch = leagueMatchRepository.findOne(id);
			return leagueMatch;
		}
		return new LeagueMatch();
	}

	/**
	 * 根据明星对象获取明星相关赛事
	 * 
	 * 
	 * @param star明星对象
	 * @param maxNum最大数量
	 * @return leagueMatch list
	 */
	public static List<LeagueMatch> getRelatedMatchListByStarId(
			final String appCode, Long starId, int maxNum) {
		
		PageInfo pageInfo = new PageInfo(0, maxNum);
		return leagueMatchRepository.getRelatedMatchPageListByStarId(starId,
				new Date(), pageInfo.getPageRequest()).getContent();
	}

	/**
	 * 获取比赛详情地址
	 * 
	 * @param leagueMatch
	 * @return
	 */
	public static String getLeagueMatchDetailUrl(final String appCode,
			final LeagueMatch leagueMatch) {
		if (leagueMatch == null) {
			return "";
		}
		String path = ItemTypeEnum.LEAGUE_MATCH.getKey() + "/"
				+ leagueMatch.getId() + "/detail";
		return path;
	}

	/**
	 * 根据赛事和TAG获取指定数量相关内容列表
	 * 
	 * @param id
	 * @param tag
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public static List<LeagueMatch> getLeagueMatchListByStarIdAndTag(
			final String appCode, final LeagueMatch leagueMatch,
			final int maxWidth) {
		
		Page<LeagueMatch> page = getLeagueMatchPageListByStarIdAndTag(appCode,
				leagueMatch, 0, maxWidth);
		if (page == null) {
			return new ArrayList<LeagueMatch>();
		}
		return page.getContent();
	}

	/**
	 * 根据赛事和TAG分页获取指定数量相关内容列表
	 * 
	 * @param id
	 * @param tag
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public static Page<LeagueMatch> getLeagueMatchPageListByStarIdAndTag(
			final String appCode, final LeagueMatch leagueMatch,
			final int currentPage, final int pageSize) {
		
		String keyword = leagueMatch.getKeyword();
		PageInfo pageInfo = new PageInfo(currentPage, pageSize);
		Page<LeagueMatch> page = leagueMatchRepository
				.findPageListByStarIdAndTag(keyword, pageInfo.getPageRequest());
		return page;
	}

	/**
	 * 根据赛季ID获取该赛季下所有赛事
	 * 
	 * @param leagueSeasonId
	 * @return
	 */
	public static List<LeagueMatch> getLeagueMatchListByLeagueSeasonId(
			final String appCode, final Long id) {
		
		List<LeagueMatch> leagueMatchList = leagueMatchRepository
				.findByLeagueSeasonId(id);
		return leagueMatchList;
	}

	/**
	 * 根据赛季ID获取赛季
	 * 
	 * @param leagueSeasonId
	 * @return
	 */
	public static LeagueSeason getLeagueSeasonByLeagueSeasonId(
			final String appCode, final Long id) {
		
		LeagueSeason leagueSeason = leagueSeasonRepository.findOne(id);
		return leagueSeason;
	}

	/**
	 * 根据赛季ID获取赛季关联所有节目
	 * 
	 * @param leagueSeasonId
	 * @return
	 */
	public static List<Trailer> getItemTraileList(final String appCode,
			final Long id) {
		
		List<Trailer> trailerlist = trailerRepository.fintrailerlist(id);
		return trailerlist;
	}

	/**
	 * 获取推荐位项详情地址
	 * 
	 * @param widgetItemView
	 * @return
	 */
	public static String getWidgetItemDetailUrl(final String appCode,
			final WidgetItemView widgetItemView) {
		return getWidgetItemDetailUrlAddParam(appCode, widgetItemView, null);
	}

	/**
	 * 获取推荐位项详情地址
	 * 
	 * @param widgetItemView
	 * @param paramStr
	 * @return
	 */
	public static String getWidgetItemDetailUrlAddParam(final String appCode,
			final WidgetItemView widgetItemView, final String paramStr) {
		if (widgetItemView == null) {
			return "";
		}
		
		String path = "";
		if (widgetItemView.getItemType() == WidgetItemTypeEnum.LEAGUE.getKey()) {
			path = "league/" + widgetItemView.getItemCode() + "/MATCH_LEAGUE";
		} else if (widgetItemView.getItemType() == WidgetItemTypeEnum.LEAGUE_SEASON
				.getKey()) {
			path = "leagueSeason/" + widgetItemView.getItemId()
					+ "/MATCH_LEAGUE";
		}
		String url = PathUtils.joinUrl(path, StringUtils.trimToEmpty(paramStr));
		return url;
	}

	/**
	 * 获取赛事横海报
	 * 
	 * @param leagueMatch
	 * @param defaultStr
	 * @return
	 */
	public static String getLeagueMatchImage1(final String appCode,
			final LeagueMatch leagueMatch, final String defaultStr) {
		if (leagueMatch == null) {
			return "";
		}
		return AppGlobal.getImagePath(StringUtils.defaultString(
				leagueMatch.getImage1(), defaultStr));
	}

	/**
	 * 获取赛事播放地址
	 * 
	 * @param appCode
	 * @param leagueMatch
	 * @param paramStr
	 * @return
	 */
	public static String getLeagueMatchPlayUrlAddParam(final String appCode,
			final LeagueMatch leagueMatch, final String paramStr) {
		if (leagueMatch == null) {
			return "";
		}
		String url = PathUtils.joinUrl(ItemTypeEnum.LEAGUE_MATCH.getKey() + "/"
				+ leagueMatch.getId() + "/play",
				StringUtils.trimToEmpty(paramStr));
		url = addLeagueMatchExtParam(appCode, leagueMatch, url);
		return url;
	}

	public static String addLeagueMatchExtParam(final String appCode,
			final LeagueMatch leagueMatch, final String url) {
//		String cpCode = AppGlobal.cpIdToCodeMap.get("" + leagueMatch.getCpId());
		return PathUtils.joinUrl(url,
				"cpCode=" + StringUtils.trimToEmpty(leagueMatch.getCpCode()));
	}
	
	/**
	 * 获取赛季播放地址
	 * 
	 * @param appCode
	 * @param leagueSeason
	 * @param paramStr
	 * @return
	 */
	public static String getLeagueSeasonPlayUrlAddParam(final String appCode,
			final LeagueSeason leagueSeason, final String paramStr) {
		if (leagueSeason == null) {
			return "";
		}
		String url = PathUtils.joinUrl(ItemTypeEnum.LEAGUE_SEASON.getKey() + "/"
				+ leagueSeason.getId() + "/play",
				StringUtils.trimToEmpty(paramStr));
		url = addLeagueSeasonExtParam(appCode, leagueSeason, url);
		return url;
	}

	public static String addLeagueSeasonExtParam(final String appCode,
			final LeagueSeason leagueSeason, final String url) {
//		String cpCode = AppGlobal.cpIdToCodeMap.get("" + leagueSeason.getCpId());
		return PathUtils.joinUrl(url,
				"cpCode=" + StringUtils.trimToEmpty(leagueSeason.getCpCode()));
	}
}