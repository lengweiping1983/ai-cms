package com.ai.cms.star.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.ClubTypeEnum;
import com.ai.common.enums.OnlineStatusEnum;

/**
 * 俱乐部实体
 * 
 */
@Entity
@Table(name = "cms_club")
public class Club extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@NotNull
	private Integer type = ClubTypeEnum.FOOTBALL.getKey();// 类型

	@NotNull
	private String name;// 俱乐部名称

	@Column(name = "short_name")
	private String shortName;// 简称

	private String coach;// 主帅名称

	private String area;// 地区

	@Column(name = "home_court")
	private String homeCourt; // 主场地点

	@Column(name = "founding_time")
	private Date foundingTime; // 成立时间

	private String honor; // 俱乐部荣誉

	private String info; // 俱乐部简介

	private String image1;// 横海报

	private String image2;// 竖海报

	@NotNull
	@Column(name = "status")
	private int status = OnlineStatusEnum.DEFAULT.getKey(); // 状态:0=待上线、1=已上线、2=已下线

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

	@Column(name = "greet_rate")
	private Float greetRate;// 受欢迎度

	@Column(name = "love_num")
	private int loveNum;// 喜欢数

	@Column(name = "hate_num")
	private int hateNum;// 不喜欢数

	@Column(name = "cloud_id")
	private String cloudId;// 云端id

	@Column(name = "cloud_code")
	private String cloudCode;// 云端代码

	private String tag;// tag，之间使用','分割
	private String keyword;// 关键字，内部使用

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getCoach() {
		return coach;
	}

	public void setCoach(String coach) {
		this.coach = coach;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getHomeCourt() {
		return homeCourt;
	}

	public void setHomeCourt(String homeCourt) {
		this.homeCourt = homeCourt;
	}

	public Date getFoundingTime() {
		return foundingTime;
	}

	public void setFoundingTime(Date foundingTime) {
		this.foundingTime = foundingTime;
	}

	public String getHonor() {
		return honor;
	}

	public void setHonor(String honor) {
		this.honor = honor;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
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

	public Float getGreetRate() {
		return greetRate;
	}

	public void setGreetRate(Float greetRate) {
		this.greetRate = greetRate;
	}

	public int getLoveNum() {
		return loveNum;
	}

	public void setLoveNum(int loveNum) {
		this.loveNum = loveNum;
	}

	public int getHateNum() {
		return hateNum;
	}

	public void setHateNum(int hateNum) {
		this.hateNum = hateNum;
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

}
