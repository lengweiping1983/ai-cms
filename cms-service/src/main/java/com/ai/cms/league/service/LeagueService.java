package com.ai.cms.league.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.AppGlobal;
import com.ai.cms.league.entity.League;
import com.ai.cms.league.entity.LeagueMatch;
import com.ai.cms.league.entity.LeagueSeason;
import com.ai.cms.league.repository.LeagueMatchRepository;
import com.ai.cms.league.repository.LeagueRepository;
import com.ai.cms.league.repository.LeagueSeasonRepository;
import com.ai.cms.live.entity.ChannelConfig;
import com.ai.cms.live.repository.ChannelConfigRepository;
import com.ai.cms.live.repository.ChannelRepository;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.SplitRProgram;
import com.ai.cms.media.repository.MediaFileRepository;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.cms.media.repository.SplitRProgramRepository;
import com.ai.cms.star.entity.Club;
import com.ai.cms.star.repository.ClubRepository;
import com.ai.cms.star.repository.StarRepository;
import com.ai.common.enums.ItemTypeEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;

@Service
@Transactional(value = "slaveTransactionManager", readOnly = true)
public class LeagueService extends AbstractService<League, Long> {

	@Autowired
	private LeagueRepository leagueRepository;

	@Autowired
	private LeagueSeasonRepository leagueSeasonRepository;

	@Autowired
	private LeagueMatchRepository leagueMatchRepository;

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	protected MediaFileRepository mediaFileRepository;
	
	@Autowired
	protected SplitRProgramRepository splitRProgramRepository;

	@Autowired
	private ClubRepository clubRepository;

	@Autowired
	private StarRepository starRepository;

	@Autowired
	private ChannelRepository channelRepository;

	@Autowired
	private ChannelConfigRepository channelConfigRepository;

	@Override
	public AbstractRepository<League, Long> getRepository() {
		return leagueRepository;
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void deleteLeague(League league) {
		if (league != null) {
			leagueRepository.delete(league);
		}
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void saveLeague(League league) {
		if (league != null) {
			leagueRepository.save(league);
		}
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void deleteLeagueSeason(LeagueSeason leagueSeason) {
		if (leagueSeason != null) {
			leagueSeasonRepository.delete(leagueSeason);
		}
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void saveLeagueSeason(LeagueSeason leagueSeason) {
		if (leagueSeason != null) {
			if (leagueSeason.getBeginTime() != null
					&& leagueSeason.getDuration() != null) {
				leagueSeason
						.setEndTime(DateUtils.addMinutes(
								leagueSeason.getBeginTime(),
								leagueSeason.getDuration()));
			}
			if (leagueSeason.getSplitProgram() != null
					&& leagueSeason.getSplitProgram() == YesNoEnum.YES.getKey()) {
				SplitRProgram splitRProgram = splitRProgramRepository
						.findOneByItemTypeAndItemId(
								ItemTypeEnum.LEAGUE_SEASON.getKey(),
								leagueSeason.getId());
				if (splitRProgram == null) {
					splitRProgram = new SplitRProgram();
				}
				splitRProgram.setItemType(ItemTypeEnum.LEAGUE_SEASON.getKey());
				splitRProgram.setItemId(leagueSeason.getId());
				splitRProgram.setProgramId(leagueSeason.getProgramId());
				splitRProgramRepository.save(splitRProgram);
			} else {
				splitRProgramRepository.deleteByItemTypeAndItemId(
						ItemTypeEnum.LEAGUE_SEASON.getKey(),
						leagueSeason.getId());
				leagueSeason.setProgramId(null);
			}
			leagueSeasonRepository.save(leagueSeason);
		}
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void deleteLeagueMatch(LeagueMatch leagueMatch) {
		if (leagueMatch != null) {
			splitRProgramRepository.deleteByItemTypeAndItemId(
					ItemTypeEnum.LEAGUE_MATCH.getKey(), leagueMatch.getId());
			leagueMatchRepository.delete(leagueMatch);
		}
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void saveLeagueMatch(LeagueMatch leagueMatch) {
		if (leagueMatch != null) {
			if (leagueMatch.getBeginTime() != null
					&& leagueMatch.getDuration() != null) {
				leagueMatch.setEndTime(DateUtils.addMinutes(
						leagueMatch.getBeginTime(), leagueMatch.getDuration()));
			}
			if (leagueMatch.getSplitProgram() != null
					&& leagueMatch.getSplitProgram() == YesNoEnum.YES.getKey()) {
				SplitRProgram splitRProgram = splitRProgramRepository
						.findOneByItemTypeAndItemId(
								ItemTypeEnum.LEAGUE_MATCH.getKey(),
								leagueMatch.getId());
				if (splitRProgram == null) {
					splitRProgram = new SplitRProgram();
				}
				splitRProgram.setItemType(ItemTypeEnum.LEAGUE_MATCH.getKey());
				splitRProgram.setItemId(leagueMatch.getId());
				splitRProgram.setProgramId(leagueMatch.getProgramId());
				splitRProgramRepository.save(splitRProgram);
			} else {
				splitRProgramRepository
						.deleteByItemTypeAndItemId(
								ItemTypeEnum.LEAGUE_MATCH.getKey(),
								leagueMatch.getId());
				leagueMatch.setProgramId(null);
			}

			leagueMatchRepository.save(leagueMatch);
		}
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public Program getSplitProgram(LeagueMatch leagueMatch) {
		if (leagueMatch == null) {
			return null;
		}
		SplitRProgram splitRProgram = splitRProgramRepository
				.findOneByItemTypeAndItemId(ItemTypeEnum.LEAGUE_MATCH.getKey(),
						leagueMatch.getId());// 本地拆条操作
		if (splitRProgram != null && splitRProgram.getProgramId() != null) {
			Program program = programRepository.findOne(splitRProgram
					.getProgramId());
			if ((leagueMatch.getSplitProgram() == null || leagueMatch
					.getSplitProgram() == YesNoEnum.NO.getKey())
					&& program != null) {
				leagueMatch.setSplitProgram(YesNoEnum.YES.getKey());
				leagueMatchRepository.save(leagueMatch);
			}
			return program;
		} else if (leagueMatch.getSplitProgram() != null
				&& leagueMatch.getSplitProgram() == YesNoEnum.YES.getKey()
				&& leagueMatch.getProgramId() != null) {
			Program program = programRepository.findOne(leagueMatch
					.getProgramId());
			return program;
		}
		return null;
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public Program getSplitProgram(LeagueSeason leagueSeason) {
		if (leagueSeason == null) {
			return null;
		}
		SplitRProgram splitRProgram = splitRProgramRepository
				.findOneByItemTypeAndItemId(
						ItemTypeEnum.LEAGUE_SEASON.getKey(),
						leagueSeason.getId());// 本地拆条操作
		if (splitRProgram != null && splitRProgram.getProgramId() != null) {
			Program program = programRepository.findOne(splitRProgram
					.getProgramId());
			if ((leagueSeason.getSplitProgram() == null || leagueSeason
					.getSplitProgram() == YesNoEnum.NO.getKey())
					&& program != null) {
				leagueSeason.setSplitProgram(YesNoEnum.YES.getKey());
				leagueSeasonRepository.save(leagueSeason);
			}
			return program;
		} else if (leagueSeason.getSplitProgram() != null
				&& leagueSeason.getSplitProgram() == YesNoEnum.YES.getKey()
				&& leagueSeason.getProgramId() != null) {
			Program program = programRepository.findOne(leagueSeason
					.getProgramId());
			return program;
		}
		return null;
	}

	public String getLeagueMatchJson(LeagueMatch leagueMatch) {
		if (leagueMatch == null) {
			return "";
		}
		List<Long> clubIdList = new ArrayList<Long>();
		List<Long> channelIdList = new ArrayList<Long>();
		if (leagueMatch != null) {
			if (leagueMatch.getHomeId() != null
					&& !clubIdList.contains(leagueMatch.getHomeId())) {
				clubIdList.add(leagueMatch.getHomeId());
			}
			if (leagueMatch.getGuestId() != null
					&& !clubIdList.contains(leagueMatch.getGuestId())) {
				clubIdList.add(leagueMatch.getGuestId());
			}
			if (leagueMatch.getChannelId() != null
					&& !channelIdList.contains(leagueMatch.getChannelId())) {
				channelIdList.add(leagueMatch.getChannelId());
			}
		}
		Map<Long, Club> clubMap = new HashMap<Long, Club>();
		if (clubIdList != null && clubIdList.size() > 0) {	
			List<Club> clubList = clubRepository.findAll(clubIdList);
			for (Club club : clubList) {
				clubMap.put(club.getId(), club);
			}
		}
		Map<Long, ChannelConfig> channelConfigMap = new HashMap<Long, ChannelConfig>();
		if (channelIdList != null && channelIdList.size() > 0) {
			List<ChannelConfig> channelConfigList = channelConfigRepository
					.findByChannelIdIn(channelIdList);
			for (ChannelConfig channelConfig : channelConfigList) {
				channelConfigMap.put(channelConfig.getChannelId(), channelConfig);
			}
		}
		return getLeagueMatchJson(leagueMatch, clubMap, channelConfigMap);
	}
	
	@SuppressWarnings("unchecked")
	public String getLeagueMatchJson(LeagueMatch leagueMatch,
			Map<Long, Club> clubMap, Map<Long, ChannelConfig> channelConfigMap) {
		if (leagueMatch == null) {
			return "";
		}
		// String leagueName = null;
		// String leagueSeasonName = null;
		String channelNumber = null;
		String scheduleCode = null;

		// if (leagueMatch != null && leagueMatch.getLeagueSeasonId() != null) {
		// LeagueSeason leagueSeason = leagueSeasonRepository
		// .findOne(leagueMatch.getLeagueSeasonId());
		// leagueSeasonName = (leagueSeason != null) ? StringUtils
		// .trimToEmpty(leagueSeason.getTitle()) : null;
		//
		// if (leagueSeason != null) {
		// League league = leagueRepository.findOne(leagueSeason
		// .getLeagueId());
		// leagueName = (league != null) ? StringUtils.trimToEmpty(league
		// .getTitle()) : null;
		// }
		// }
		if (leagueMatch != null && leagueMatch.getChannelId() != null) {
			ChannelConfig channelConfig = channelConfigMap.get(leagueMatch
					.getChannelId());
			channelNumber = (channelConfig != null) ? StringUtils
					.trimToEmpty(channelConfig.getPartnerChannelNumber())
					: null;
		}
		org.json.simple.JSONObject json = new org.json.simple.JSONObject();
		json.put("itemType", ItemTypeEnum.LEAGUE_MATCH.getKey());
		// json.put("leagueName", StringUtils.trimToEmpty(leagueName));
		// json.put("leagueSeasonName",
		// StringUtils.trimToEmpty(leagueSeasonName));
		json.put("code", leagueMatch.getId());
		json.put("name", StringUtils.trimToEmpty(leagueMatch.getName()));
		json.put("title", StringUtils.trimToEmpty(leagueMatch.getTitle()));
		json.put("tag", StringUtils.trimToEmpty(leagueMatch.getTag()));
		json.put("keyword", StringUtils.trimToEmpty(leagueMatch.getKeyword()));
		json.put("area", StringUtils.trimToEmpty(leagueMatch.getArea()));
		json.put("viewpoint",
				StringUtils.trimToEmpty(leagueMatch.getViewpoint()));

		json.put("type", leagueMatch.getType());
		json.put("beginTime",
				(leagueMatch.getBeginTime() != null) ? leagueMatch
						.getBeginTime().getTime() : null);
		json.put("endTime", (leagueMatch.getEndTime() != null) ? leagueMatch
				.getEndTime().getTime() : null);
		json.put("duration", leagueMatch.getDuration());
		json.put("leagueIndex", leagueMatch.getLeagueIndex());
		json.put("episodeIndex", leagueMatch.getEpisodeIndex());
		json.put("homeName", StringUtils.trimToEmpty(leagueMatch.getHomeName()));
		json.put("guestName",
				StringUtils.trimToEmpty(leagueMatch.getGuestName()));

		if (leagueMatch.getHomeId() != null) {
			Club club = clubMap.get(leagueMatch.getHomeId());
			if (club != null) {
				json.put("homeImagePath",
						AppGlobal.getImagePath(club.getImage1()));
			}
		}
		if (leagueMatch.getGuestId() != null) {
			Club club = clubMap.get(leagueMatch.getGuestId());
			if (club != null) {
				json.put("guestImagePath",
						AppGlobal.getImagePath(club.getImage1()));
			}
		}

		json.put("homeScore", leagueMatch.getHomeScore());
		json.put("guestScore", leagueMatch.getGuestScore());
		json.put("pointStatus", leagueMatch.getPointStatus());
		json.put("homePointNum", leagueMatch.getHomePointNum());
		json.put("guestPointNum", leagueMatch.getGuestPointNum());
		json.put("channelNumber", StringUtils.trimToEmpty(channelNumber));
		json.put("scheduleCode", StringUtils.trimToEmpty(scheduleCode));
		json.put("splitProgram", leagueMatch.getSplitProgram());
		json.put("programId", leagueMatch.getProgramId());
		if (leagueMatch.getProgramId() != null) {
			getCdnFileCodeByProgramId(leagueMatch.getProgramId(), json);
		}
		if (StringUtils.isNotEmpty(leagueMatch.getKeyword())
				&& !leagueMatch.getKeyword().contains(AppGlobal.FEE_FREE)) {// 收费
			json.put("isPay", 1);
		} else {
			json.put("isPay", 0);
		}
		return json.toString();
	}

	/**
	 * 根据节目Id获取CDN侧的内容代码
	 */
	@SuppressWarnings("unchecked")
	public String getCdnFileCodeByProgramId(Long programId,
			org.json.simple.JSONObject json) {
		if (programId == null) {
			return "";
		}
//		List<MediaFile> mfList = mediaFileRepository.findByProgramIdAndType(
//				programId, MediaFileTypeEnum.DEFAULT.getKey());
//		if (mfList != null) {
//			for (MediaFile mediaFile : mfList) {
//				String partnerContentId = mediaFile.getCdnFileCode();
//				if (StringUtils.isNotEmpty(partnerContentId)) {
//					json.put("partnerProgramCode",
//							StringUtils.trimToEmpty(mediaFile.getCdnFileCode()));
//					json.put("partnerMediaFileCode", StringUtils
//							.trimToEmpty(mediaFile.getPartnerItemCode()));
//					return StringUtils.trimToEmpty(partnerContentId);
//				}
//			}
//		}
		return "";
	}

}