package com.ai.cms.media.bean;

import java.io.Serializable;
import java.util.Date;

import com.ai.common.excel.annotation.ExcelField;

public class SeriesImportLog implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT = 1;

	// @ExcelField(title = "剧头/节目", align = 1, groups = { DEFAULT }, sort = 1)
	// private String seriesFlag = "剧头";// 剧头/节目

	@ExcelField(title = "剧头类型", align = 1, groups = { DEFAULT }, sort = 11)
	private String type;// 剧头类型:1=电视剧、2=栏目剧

	@ExcelField(title = "总集数", align = 1, groups = { DEFAULT }, sort = 21)
	private String episodeTotal;// 总集数

	@ExcelField(title = "内容提供商", align = 1, groups = { DEFAULT }, sort = 31)
	private String cpName;// 内容提供商

	@ExcelField(title = "剧头ID", align = 1, groups = { DEFAULT }, sort = 41)
	private Long id;

	@ExcelField(title = "剧头名称", align = 1, groups = { DEFAULT }, sort = 51)
	private String name;// 剧头名称

	@ExcelField(title = "标题", align = 1, groups = { DEFAULT }, sort = 61)
	private String title;// 标题

	@ExcelField(title = "副标题", align = 1, groups = { DEFAULT }, sort = 71)
	private String caption;// 副标题

	@ExcelField(title = "内容类型", align = 2, groups = { DEFAULT }, sort = 81)
	private String contentType;// 内容类型

	@ExcelField(title = "导演", align = 1, groups = { DEFAULT }, sort = 91)
	private String director;// 导演，使用','分割

	@ExcelField(title = "演员", align = 1, groups = { DEFAULT }, sort = 111)
	private String actor;// 演员，使用','分割

	@ExcelField(title = "年份", align = 1, groups = { DEFAULT }, sort = 131)
	private String year;// 年份

	@ExcelField(title = "地区", align = 1, groups = { DEFAULT }, sort = 141)
	private String area;// 地区

	@ExcelField(title = "语言", align = 1, groups = { DEFAULT }, sort = 151)
	private String language;// 语言

	@ExcelField(title = "评分", align = 1, groups = { DEFAULT }, sort = 161)
	private String rating;// 评分，10分制，有一个小数

	@ExcelField(title = "时长", align = 1, groups = { DEFAULT }, sort = 171)
	private String duration;// 时长，单位分钟

	@ExcelField(title = "是否有字幕", align = 2, groups = { DEFAULT }, sort = 181)
	private String subtitle;// 是否有字幕:0=否、1=是

	@ExcelField(title = "TAG", align = 1, groups = { DEFAULT }, sort = 191)
	private String tag;// tag，之间使用','分割

	@ExcelField(title = "内部标签", align = 1, groups = { DEFAULT }, sort = 201)
	private String internalTag;// 内部标签

	@ExcelField(title = "关键字", align = 1, groups = { DEFAULT }, sort = 211)
	private String keyword;// 关键字，内部使用

	@ExcelField(title = "看点", align = 1, groups = { DEFAULT }, sort = 221)
	private String viewpoint;// 看点

	@ExcelField(title = "简介", align = 1, groups = { DEFAULT }, sort = 231)
	private String info;// 简介

	@ExcelField(title = "首播时间", align = 1, groups = { DEFAULT }, sort = 241)
	private Date orgAirDate;// 首播时间

	@ExcelField(title = "播出许可证", align = 1, groups = { DEFAULT }, sort = 251)
	private String broadcastLicense;// 播出许可证

	@ExcelField(title = "版权性质", align = 1, groups = { DEFAULT }, sort = 261)
	private String authorizeInfo;// 版权性质

	@ExcelField(title = "授权地址", align = 1, groups = { DEFAULT }, sort = 271)
	private String authorizeAddress;// 授权地址

	@ExcelField(title = "授权开始时间", align = 1, groups = { DEFAULT }, sort = 281)
	private Date licensingWindowStart;// 授权开始时间

	@ExcelField(title = "授权结束时间", align = 1, groups = { DEFAULT }, sort = 291)
	private Date licensingWindowEnd;// 授权结束时间

	@ExcelField(title = "横海报", align = 1, groups = { DEFAULT }, sort = 301)
	private String image1;// 横海报

	@ExcelField(title = "竖海报", align = 1, groups = { DEFAULT }, sort = 311)
	private String image2;// 竖海报

	@ExcelField(title = "横海报(大)", align = 1, groups = { DEFAULT }, sort = 321)
	private String image3;// 横海报(大)

	@ExcelField(title = "竖海报(大)", align = 1, groups = { DEFAULT }, sort = 331)
	private String image4;// 竖海报(大)

	@ExcelField(title = "状态", align = 2, groups = { DEFAULT }, sort = 341)
	private String status;// 状态:0=待上线、1=已上线、2=已下线

	@ExcelField(title = "上线时间", align = 2, groups = { DEFAULT }, sort = 351)
	private String onlineTime;// 上线时间

	@ExcelField(title = "下线时间", align = 2, groups = { DEFAULT }, sort = 361)
	private String offlineTime;// 下线时间

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEpisodeTotal() {
		return episodeTotal;
	}

	public void setEpisodeTotal(String episodeTotal) {
		this.episodeTotal = episodeTotal;
	}

	public String getCpName() {
		return cpName;
	}

	public void setCpName(String cpName) {
		this.cpName = cpName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getInternalTag() {
		return internalTag;
	}

	public void setInternalTag(String internalTag) {
		this.internalTag = internalTag;
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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Date getOrgAirDate() {
		return orgAirDate;
	}

	public void setOrgAirDate(Date orgAirDate) {
		this.orgAirDate = orgAirDate;
	}

	public String getBroadcastLicense() {
		return broadcastLicense;
	}

	public void setBroadcastLicense(String broadcastLicense) {
		this.broadcastLicense = broadcastLicense;
	}

	public String getAuthorizeInfo() {
		return authorizeInfo;
	}

	public void setAuthorizeInfo(String authorizeInfo) {
		this.authorizeInfo = authorizeInfo;
	}

	public String getAuthorizeAddress() {
		return authorizeAddress;
	}

	public void setAuthorizeAddress(String authorizeAddress) {
		this.authorizeAddress = authorizeAddress;
	}

	public Date getLicensingWindowStart() {
		return licensingWindowStart;
	}

	public void setLicensingWindowStart(Date licensingWindowStart) {
		this.licensingWindowStart = licensingWindowStart;
	}

	public Date getLicensingWindowEnd() {
		return licensingWindowEnd;
	}

	public void setLicensingWindowEnd(Date licensingWindowEnd) {
		this.licensingWindowEnd = licensingWindowEnd;
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

	public String getImage3() {
		return image3;
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}

	public String getImage4() {
		return image4;
	}

	public void setImage4(String image4) {
		this.image4 = image4;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime;
	}

	public String getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(String offlineTime) {
		this.offlineTime = offlineTime;
	}

}
