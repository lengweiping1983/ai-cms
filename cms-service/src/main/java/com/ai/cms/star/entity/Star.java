package com.ai.cms.star.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.StarTypeEnum;

/**
 * 明星实体
 *
 */
@Entity
@Table(name = "cms_star")
public class Star extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@NotNull
	private Integer type = StarTypeEnum.MOVIE.getKey();// 类型

	@Column(name = "club_id")
	private Long clubId;// 所属俱乐部id

	private String no; // 编号
	private String position; // 位置
	private String honor; // 荣誉

	@NotNull
	private String name; // 姓名

	private String ename; // 英文名

	private String tag;// tag，之间使用','分割
	private String keyword;// 关键字，内部使用

	@Column(name = "greet_rate")
	private Float greetRate;// 受欢迎度

	private String evaluate;// 评价

	private String image1;// 横海报
	private String image2;// 竖海报

	@Temporal(TemporalType.TIMESTAMP)
	private Date birthday;// 生日
	private Integer age;// 年龄

	private String constellation;// 星座
	private Integer height;// 身高
	private Integer weight;// 体重

	private String country;// 国家
	private String area;// 地区
	private String nation; // 民族

	private String info;// 简介

	@Column(name = "military_exploits")
	private String militaryExploits;// 战绩
	private String ko;// KO/TKO;
	private String surrender;// 降服
	private String determine;// 判定
	private Integer arm;// 臂展
	private Integer leg;// 腿展

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

	@Column(name = "love_num")
	private int loveNum;// 喜欢数

	@Column(name = "hate_num")
	private int hateNum;// 不喜欢数

	@Column(name = "cloud_id")
	private String cloudId;// 云端id

	@Column(name = "cloud_code")
	private String cloudCode;// 云端代码

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getClubId() {
		return clubId;
	}

	public void setClubId(Long clubId) {
		this.clubId = clubId;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getHonor() {
		return honor;
	}

	public void setHonor(String honor) {
		this.honor = honor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
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

	public Float getGreetRate() {
		return greetRate;
	}

	public void setGreetRate(Float greetRate) {
		this.greetRate = greetRate;
	}

	public String getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
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

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getMilitaryExploits() {
		return militaryExploits;
	}

	public void setMilitaryExploits(String militaryExploits) {
		this.militaryExploits = militaryExploits;
	}

	public String getKo() {
		return ko;
	}

	public void setKo(String ko) {
		this.ko = ko;
	}

	public String getSurrender() {
		return surrender;
	}

	public void setSurrender(String surrender) {
		this.surrender = surrender;
	}

	public String getDetermine() {
		return determine;
	}

	public void setDetermine(String determine) {
		this.determine = determine;
	}

	public Integer getArm() {
		return arm;
	}

	public void setArm(Integer arm) {
		this.arm = arm;
	}

	public Integer getLeg() {
		return leg;
	}

	public void setLeg(Integer leg) {
		this.leg = leg;
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

}
