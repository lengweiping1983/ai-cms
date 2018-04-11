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

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.OnlineStatusEnum;

/**
 * 频道配置实体
 */
@Entity
@Table(name = "cms_live_channel_config")
public class ChannelConfig extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "null", value = ConstraintMode.NO_CONSTRAINT))
	private Channel channel;// 频道

	@Column(name = "channel_id")
	private Long channelId;// 频道Id

	@Column(name = "provider_type")
	private Integer providerType = 0; // 提供商

	@Column(name = "partner_channel_code")
	private String partnerChannelCode;// 本地频道代码

	@Column(name = "partner_channel_name")
	private String partnerChannelName;// 本地频道名称

	@Column(name = "partner_channel_number")
	private String partnerChannelNumber;// 本地频道号

	@Column(name = "play_by_channel_number")
	private Integer playByChannelNumber = 1;// 使用频道号来播放

	private String url;// 直播地址

	@Column(name = "live_encrypt")
	private Integer liveEncrypt = 0;// 直播是否加密

	@Column(name = "time_shift")
	private Integer timeShift = 0;// 是否支持时移

	@Column(name = "time_shift_duration")
	private Integer timeShiftDuration = 60;// 时移时长，单位分钟

	@Column(name = "time_shift_url")
	private String timeShiftUrl;// 时移地址

	@Column(name = "time_shift_encrypt")
	private Integer timeShiftEncrypt = 0;// 时移是否加密

	@Column(name = "look_back")
	private Integer lookBack = 0;// 是否支持回看

	@Column(name = "look_back_duration")
	private Integer lookBackDuration = 48;// 回看时长，单位小时

	@Column(name = "look_back_url")
	private String lookBackUrl;// 回看地址;

	@Column(name = "look_back_encrypt")
	private Integer lookBackEncrypt = 0;// 回看是否加密

	@Column(name = "enbody_duration")
	private Integer enbodyDuration = 0;// 收录时长，单位分钟

	@Column(name = "pre_time")
	private Integer preTime = 5;// 赛前时长，单位分钟

	@Column(name = "suf_time")
	private Integer sufTime = 0;// 赛后时长，单位分钟

	@Column(name = "image1")
	private String image1;// 横海报

	@Column(name = "image2")
	private String image2;// 竖海报

	@Column(name = "status")
	private Integer status = OnlineStatusEnum.DEFAULT.getKey(); // 状态:0=未上线、1=已上线、2=已下线

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

	@Column(name = "sort_index")
	private Integer sortIndex = 999; // 排序值

	private String info;// 简介

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

	public Integer getProviderType() {
		return providerType;
	}

	public void setProviderType(Integer providerType) {
		this.providerType = providerType;
	}

	public String getPartnerChannelCode() {
		return partnerChannelCode;
	}

	public void setPartnerChannelCode(String partnerChannelCode) {
		this.partnerChannelCode = partnerChannelCode;
	}

	public String getPartnerChannelName() {
		return partnerChannelName;
	}

	public void setPartnerChannelName(String partnerChannelName) {
		this.partnerChannelName = partnerChannelName;
	}

	public String getPartnerChannelNumber() {
		return partnerChannelNumber;
	}

	public void setPartnerChannelNumber(String partnerChannelNumber) {
		this.partnerChannelNumber = partnerChannelNumber;
	}

	public Integer getPlayByChannelNumber() {
		return playByChannelNumber;
	}

	public void setPlayByChannelNumber(Integer playByChannelNumber) {
		this.playByChannelNumber = playByChannelNumber;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getLiveEncrypt() {
		return liveEncrypt;
	}

	public void setLiveEncrypt(Integer liveEncrypt) {
		this.liveEncrypt = liveEncrypt;
	}

	public Integer getTimeShift() {
		return timeShift;
	}

	public void setTimeShift(Integer timeShift) {
		this.timeShift = timeShift;
	}

	public Integer getTimeShiftDuration() {
		return timeShiftDuration;
	}

	public void setTimeShiftDuration(Integer timeShiftDuration) {
		this.timeShiftDuration = timeShiftDuration;
	}

	public String getTimeShiftUrl() {
		return timeShiftUrl;
	}

	public void setTimeShiftUrl(String timeShiftUrl) {
		this.timeShiftUrl = timeShiftUrl;
	}

	public Integer getTimeShiftEncrypt() {
		return timeShiftEncrypt;
	}

	public void setTimeShiftEncrypt(Integer timeShiftEncrypt) {
		this.timeShiftEncrypt = timeShiftEncrypt;
	}

	public Integer getLookBack() {
		return lookBack;
	}

	public void setLookBack(Integer lookBack) {
		this.lookBack = lookBack;
	}

	public Integer getLookBackDuration() {
		return lookBackDuration;
	}

	public void setLookBackDuration(Integer lookBackDuration) {
		this.lookBackDuration = lookBackDuration;
	}

	public String getLookBackUrl() {
		return lookBackUrl;
	}

	public void setLookBackUrl(String lookBackUrl) {
		this.lookBackUrl = lookBackUrl;
	}

	public Integer getLookBackEncrypt() {
		return lookBackEncrypt;
	}

	public void setLookBackEncrypt(Integer lookBackEncrypt) {
		this.lookBackEncrypt = lookBackEncrypt;
	}

	public Integer getEnbodyDuration() {
		return enbodyDuration;
	}

	public void setEnbodyDuration(Integer enbodyDuration) {
		this.enbodyDuration = enbodyDuration;
	}

	public Integer getPreTime() {
		return preTime;
	}

	public void setPreTime(Integer preTime) {
		this.preTime = preTime;
	}

	public Integer getSufTime() {
		return sufTime;
	}

	public void setSufTime(Integer sufTime) {
		this.sufTime = sufTime;
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

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
