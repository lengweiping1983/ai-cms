package com.ai.cms.live.entity;

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

import com.ai.cms.injection.enums.InjectionStatusEnum;
import com.ai.cms.injection.enums.PlayCodeStatusEnum;
import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.YesNoEnum;

/**
 * 时间表实体
 */
@Entity
@Table(name = "cms_live_schedule")
public class Schedule extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "null", value = ConstraintMode.NO_CONSTRAINT))
	private Channel channel;// 频道

	@Column(name = "channel_id")
	private Long channelId;// 频道Id

	@Column(name = "program_name")
	private String programName;// 节目名称

	@Column(name = "search_name")
	private String searchName;// 搜索名称供界面搜索

	private String tag;// tag

	private String keyword;// 关键字，内部使用

	private String viewpoint;// 看点

	@Column(name = "update_info")
	private String updateInfo;// 更新信息

	@Column(name = "episode_index")
	private Integer episodeIndex;// 更新到几集

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "begin_time")
	private Date beginTime;// 节目开始时间

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time")
	private Date endTime;// 节目结束时间

	@Column(name = "duration")
	private Integer duration = 0;// 时长，单位分钟

	@Column(name = "status")
	private Integer status = OnlineStatusEnum.ONLINE.getKey(); // 状态:0=未上线、1=已上线、2=已下线

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

	private String info;// 简介

	@Column(name = "split_program")
	private Integer splitProgram = YesNoEnum.NO.getKey();// 是否已拆条

	@Column(name = "program_id")
	private Long programId;// 拆条后的节目id

	@Column(name = "cloud_id")
	private String cloudId;// 云端id

	@Column(name = "cloud_code")
	private String cloudCode;// 云端代码

	@Column(name = "cp_code")
	private String cpCode; // 内容提供商代码,多个使用','分割

	@Column(name = "injection_status")
	private String injectionStatus = "" + InjectionStatusEnum.DEFAULT.getKey();// 分发状态

	@Column(name = "play_code")
	private String playCode;// 运营商侧播放代码

	@Column(name = "play_code_status")
	private Integer playCodeStatus = PlayCodeStatusEnum.DEFAULT.getKey();// 播放代码状态

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
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

	public String getViewpoint() {
		return viewpoint;
	}

	public void setViewpoint(String viewpoint) {
		this.viewpoint = viewpoint;
	}

	public String getUpdateInfo() {
		return updateInfo;
	}

	public void setUpdateInfo(String updateInfo) {
		this.updateInfo = updateInfo;
	}

	public Integer getEpisodeIndex() {
		return episodeIndex;
	}

	public void setEpisodeIndex(Integer episodeIndex) {
		this.episodeIndex = episodeIndex;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
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

	public String getInjectionStatus() {
		return injectionStatus;
	}

	public void setInjectionStatus(String injectionStatus) {
		this.injectionStatus = injectionStatus;
	}

	public String getPlayCode() {
		return playCode;
	}

	public void setPlayCode(String playCode) {
		this.playCode = playCode;
	}

	public Integer getPlayCodeStatus() {
		return playCodeStatus;
	}

	public void setPlayCodeStatus(Integer playCodeStatus) {
		this.playCodeStatus = playCodeStatus;
	}

}
