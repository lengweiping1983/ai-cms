package com.ai.cms.league.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.YesNoEnum;

/**
 * 赛事实体
 *
 */
@Entity
@Table(name = "cms_league_match")
public class LeagueMatch extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "league_season_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "null", value = ConstraintMode.NO_CONSTRAINT))
	private LeagueSeason leagueSeason;// 所属赛季

	@Column(name = "league_season_id")
	private Long leagueSeasonId;// 所属赛季id

	private Integer type;// 赛事类型

	@Column(name = "sport_content_type")
	private Integer sportContentType;// 内容类型

	@NotNull
	private String name;// 赛事名称

	@NotNull
	private String title;// 标题
	private String caption;// 副标题

	@Column(name = "search_name")
	private String searchName;// 搜索名称供界面搜索

	private String tag;// tag

	private String keyword;// 关键字，内部使用

	private String area;// 地点

	private String viewpoint;// 看点

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "begin_time")
	private Date beginTime;// 开赛时间

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time")
	private Date endTime;// 完赛时间

	private Integer duration = 120;// 时长，单位分钟

	@Column(name = "league_index")
	private Integer leagueIndex = 1;// 赛程

	@Column(name = "episode_index")
	private Integer episodeIndex = 1;// 场次

	@Column(name = "home_name")
	private String homeName;// 主场

	@Column(name = "guest_name")
	private String guestName;// 客场

	@Column(name = "home_type")
	private Integer homeType;// 主场参赛对象1=团队，2=个人

	@Column(name = "guest_type")
	private Integer guestType;// 客场参赛对象1=团队，2=个人

	@Column(name = "home_id")
	private Long homeId;// 主场Id

	@Column(name = "guest_id")
	private Long guestId;// 客场id

	@Column(name = "home_score")
	private Integer homeScore;// 主场得分

	@Column(name = "guest_score")
	private Integer guestScore;// 客场得分

	@Column(name = "point_status")
	private Integer pointStatus = YesNoEnum.NO.getKey();// 点球大战:0=否，1=是

	@Column(name = "home_point_num")
	private Integer homePointNum;// 主场点球数

	@Column(name = "guest_point_num")
	private Integer guestPointNum;// 客场点球数

	private String image1;// 横海报

	private String image2;// 竖海报

	private String info;// 简介

	@NotNull
	@Column(name = "status")
	private int status = OnlineStatusEnum.DEFAULT.getKey();// 状态:0=待上线、1=已上线、2=已下线

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "online_time")
	private Date onlineTime;// 上线时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "offline_time")
	private Date offlineTime;// 下线时间
	@Column(name = "online_user")
	private String onlineUser;// 上线人
	@Column(name = "offline_user")
	private String offlineUser;// 下线人

	@Column(name = "cloud_id")
	private String cloudId;// 云端id

	@Column(name = "cloud_code")
	private String cloudCode;// 云端代码

	@Column(name = "cp_code")
	private String cpCode; // 内容提供商代码,多个使用','分割

	@Column(name = "channel_id")
	private Long channelId;// 频道id

	@Column(name = "schedule_id")
	private Long scheduleId;// 直播节目id

	@Column(name = "split_program")
	private Integer splitProgram = YesNoEnum.NO.getKey();// 是否已拆条

	@Column(name = "program_id")
	private Long programId;// 拆条后的节目id

	@Transient
	private int playStatus = 0;// 0=数据错误(页面提示内容已下架)，1=未开赛，2=即将开始，3=正在直播，4=已完赛，5=精彩回看，6=暂无直播

	@Transient
	private String playStatusDesc;

	@Transient
	private Date playBeginTime;// 播放开始时间

	@Transient
	private Date playEndTime;// 播放结束时间

	public LeagueSeason getLeagueSeason() {
		return leagueSeason;
	}

	public void setLeagueSeason(LeagueSeason leagueSeason) {
		this.leagueSeason = leagueSeason;
	}

	public Long getLeagueSeasonId() {
		return leagueSeasonId;
	}

	public void setLeagueSeasonId(Long leagueSeasonId) {
		this.leagueSeasonId = leagueSeasonId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSportContentType() {
		return sportContentType;
	}

	public void setSportContentType(Integer sportContentType) {
		this.sportContentType = sportContentType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getViewpoint() {
		return viewpoint;
	}

	public void setViewpoint(String viewpoint) {
		this.viewpoint = viewpoint;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getLeagueIndex() {
		return leagueIndex;
	}

	public void setLeagueIndex(Integer leagueIndex) {
		this.leagueIndex = leagueIndex;
	}

	public Integer getEpisodeIndex() {
		return episodeIndex;
	}

	public void setEpisodeIndex(Integer episodeIndex) {
		this.episodeIndex = episodeIndex;
	}

	public String getHomeName() {
		return homeName;
	}

	public void setHomeName(String homeName) {
		this.homeName = homeName;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public Integer getHomeType() {
		return homeType;
	}

	public void setHomeType(Integer homeType) {
		this.homeType = homeType;
	}

	public Integer getGuestType() {
		return guestType;
	}

	public void setGuestType(Integer guestType) {
		this.guestType = guestType;
	}

	public Long getHomeId() {
		return homeId;
	}

	public void setHomeId(Long homeId) {
		this.homeId = homeId;
	}

	public Long getGuestId() {
		return guestId;
	}

	public void setGuestId(Long guestId) {
		this.guestId = guestId;
	}

	public Integer getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(Integer homeScore) {
		this.homeScore = homeScore;
	}

	public Integer getGuestScore() {
		return guestScore;
	}

	public void setGuestScore(Integer guestScore) {
		this.guestScore = guestScore;
	}

	public Integer getPointStatus() {
		return pointStatus;
	}

	public void setPointStatus(Integer pointStatus) {
		this.pointStatus = pointStatus;
	}

	public Integer getHomePointNum() {
		return homePointNum;
	}

	public void setHomePointNum(Integer homePointNum) {
		this.homePointNum = homePointNum;
	}

	public Integer getGuestPointNum() {
		return guestPointNum;
	}

	public void setGuestPointNum(Integer guestPointNum) {
		this.guestPointNum = guestPointNum;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(Date onlineTime) {
		this.onlineTime = onlineTime;
	}

	public Date getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(Date offlineTime) {
		this.offlineTime = offlineTime;
	}

	public String getOnlineUser() {
		return onlineUser;
	}

	public void setOnlineUser(String onlineUser) {
		this.onlineUser = onlineUser;
	}

	public String getOfflineUser() {
		return offlineUser;
	}

	public void setOfflineUser(String offlineUser) {
		this.offlineUser = offlineUser;
	}

	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}

	public String getCloudCode() {
		return cloudCode;
	}

	public void setCloudCode(String cloudCode) {
		this.cloudCode = cloudCode;
	}

	public String getCpCode() {
		return cpCode;
	}

	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getSplitProgram() {
		return splitProgram;
	}

	public void setSplitProgram(Integer splitProgram) {
		this.splitProgram = splitProgram;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public int getPlayStatus() {
		return playStatus;
	}

	public void setPlayStatus(int playStatus) {
		this.playStatus = playStatus;
	}

	public String getPlayStatusDesc() {
		return playStatusDesc;
	}

	public void setPlayStatusDesc(String playStatusDesc) {
		this.playStatusDesc = playStatusDesc;
	}

	public Date getPlayBeginTime() {
		return playBeginTime;
	}

	public void setPlayBeginTime(Date playBeginTime) {
		this.playBeginTime = playBeginTime;
	}

	public Date getPlayEndTime() {
		return playEndTime;
	}

	public void setPlayEndTime(Date playEndTime) {
		this.playEndTime = playEndTime;
	}

}
