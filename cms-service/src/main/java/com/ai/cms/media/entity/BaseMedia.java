package com.ai.cms.media.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.ai.cms.injection.enums.InjectionStatusEnum;
import com.ai.cms.injection.enums.PlayCodeStatusEnum;
import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.AuditStatusEnum;
import com.ai.common.enums.ContentTypeEnum;
import com.ai.common.enums.MediaStatusEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.SourceEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.utils.DateUtils;

@MappedSuperclass
public abstract class BaseMedia extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	public static final String METADATA = "name,enName,title,caption,contentType,director,actor,directorPinyin,actorPinyin,compere,guest,"
			+ "year,area,language,rating,duration,subtitle,tag,internalTag,keyword,viewpoint,info,"
			+ "orgAirDate,broadcastLicense,authorizeInfo,authorizeAddress,licensingWindowStart,licensingWindowEnd,"
			+ "searchName,reserved1,reserved2,reserved3,reserved4,reserved5,cpCode";
	public static final String METADATA_OTHER = "source,loveNum,hateNum,cloudId,cloudCode,kpeople,scriptWriter,reporter,incharge,"
			+ "copyRight,vspCode,spCode,orderNumber,sortName,filename,storagePath,";
	public static final String POSTER = "image1,image2,image3,image4";

	@NotNull
	private String name;// 内容名称

	private String enName;// 英文名称

	@NotNull
	private String title;// 标题
	private String caption;// 副标题

	@NotNull
	@Column(name = "content_type")
	private Integer contentType = ContentTypeEnum.MOVIE.getKey();// 内容类型

	private String director;// 导演，使用','分割
	private String actor;// 演员，使用','分割

	@Column(name = "director_pinyin")
	private String directorPinyin;// 导演名拼音首字母，使用','分割
	@Column(name = "actor_pinyin")
	private String actorPinyin;// 演员名拼音首字母，使用','分割

	private String year;// 年份
	private String area;// 地区
	private String language;// 语言
	private Float rating;// 评分，10分制，有一个小数
	private Integer duration;// 时长，单位分钟
	private Integer subtitle = YesNoEnum.YES.getKey();// 是否有字幕:0=否、1=是

	private String tag;// tag，之间使用','分割
	@Column(name = "internal_tag")
	private String internalTag;// 内部标签，内部使用
	private String keyword;// 关键字，内部使用
	private String viewpoint;// 看点
	private String info;// 简介

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "org_air_date")
	private Date orgAirDate;// 首播时间
	@Column(name = "broadcast_license")
	private String broadcastLicense;// 播出许可证

	@Column(name = "authorize_info")
	private String authorizeInfo;// 版权性质
	@Column(name = "authorize_address")
	private String authorizeAddress;// 授权地址

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "licensing_window_start")
	private Date licensingWindowStart = DateUtils
			.parseDate("2018-01-01 00:00:00");// 授权开始时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "licensing_window_end")
	private Date licensingWindowEnd = DateUtils
			.parseDate("2029-12-31 23:59:59");// 授权结束时间

	private String image1;// 横海报
	private String image2;// 竖海报
	private String image3;// 横海报(大)
	private String image4;// 竖海报(大)

	@Column(name = "image1_code")
	private String image1Code;// 横海报标识
	@Column(name = "image2_code")
	private String image2Code;// 竖海报标识
	@Column(name = "image3_code")
	private String image3Code;// 横海报(大)标识
	@Column(name = "image4_code")
	private String image4Code;// 竖海报(大)标识

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

	@NotNull
	@Column(name = "status")
	private Integer status = OnlineStatusEnum.DEFAULT.getKey();// 上线状态:0=待上线、1=已上线、2=已下线

	@NotNull
	@Column(name = "media_status")
	private Integer mediaStatus = MediaStatusEnum.MISS_VIDEO.getKey()
			+ MediaStatusEnum.MISS_IMAGE.getKey();// 媒资状态

	@NotNull
	private Integer source = SourceEnum.MANUAL.getKey();// 方式:0=手工录入、1=批量导入、2=自动采集、3=转码、4=中央CMS、5=分发

	@Column(name = "sort_name")
	private String sortName;// 索引名称供界面排序
	@Column(name = "search_name")
	private String searchName;// 搜索名称供界面搜索
	@Column(name = "order_number")
	private String orderNumber;// 节目订购编号
	@Column(name = "original_name")
	private String originalName;// 原名

	@Column(name = "love_num")
	private Integer loveNum;// 喜欢数
	@Column(name = "hate_num")
	private Integer hateNum;// 不喜欢数

	@Column(name = "injection_status")
	private String injectionStatus = "" + InjectionStatusEnum.DEFAULT.getKey();// 分发状态

	@Column(name = "receive_code")
	private String receiveCode;// 接收到的代码

	@Column(name = "play_code")
	private String playCode;// 全局代码

	@Column(name = "play_code_status")
	private Integer playCodeStatus = PlayCodeStatusEnum.DEFAULT.getKey();// 播放代码状态

	@Column(name = "cloud_id")
	private String cloudId;// 云端id
	@Column(name = "cloud_code")
	private String cloudCode;// 云端代码

	private String compere;// 主持人，使用','分割
	private String guest;// 嘉宾，使用','分割
	private String kpeople;// 主要人物
	@Column(name = "script_writer")
	private String scriptWriter;// 编剧
	private String reporter;// 记者
	private String incharge;// 其他责任人

	@Column(name = "copy_right")
	private String copyRight;// 版权方标识
	@Column(name = "vsp_code")
	private String vspCode;// 内容服务平台标识
	@Column(name = "sp_code")
	private String spCode;// 服务提供商标识
	@Column(name = "cp_code")
	private String cpCode; // 内容提供商代码,多个使用','分割

	private String filename; // 媒体文件名标识,用作转码后的媒体文件名

	@Column(name = "storage_path")
	private String storagePath; // 存储路径

	@Column(name = "template_id")
	private String templateId;// 码率模板id，使用','分割

	@Column(name = "audit_status")
	private Integer auditStatus = AuditStatusEnum.EDIT.getKey();// 审核状态

	@Column(name = "submit_user")
	private String submitUser;// 提交人

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "submit_time")
	private Date submitTime;// 提交时间

	@Column(name = "audit_user")
	private String auditUser;// 审核人

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "audit_time")
	private Date auditTime;// 审核时间

	@Column(name = "audit_comment")
	private String auditComment;// 审核备注

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "storage_time")
	private Date storageTime;// 入库时间

	@Column(name = "site_code")
	private String siteCode;

	private String reserved1;
	private String reserved2;
	private String reserved3;
	private String reserved4;
	private String reserved5;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
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

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
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

	public String getDirectorPinyin() {
		return directorPinyin;
	}

	public void setDirectorPinyin(String directorPinyin) {
		this.directorPinyin = directorPinyin;
	}

	public String getActorPinyin() {
		return actorPinyin;
	}

	public void setActorPinyin(String actorPinyin) {
		this.actorPinyin = actorPinyin;
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

	public Float getRating() {
		return rating;
	}

	public void setRating(Float rating) {
		this.rating = rating;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(Integer subtitle) {
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

	public String getImage1Code() {
		return image1Code;
	}

	public void setImage1Code(String image1Code) {
		this.image1Code = image1Code;
	}

	public String getImage2Code() {
		return image2Code;
	}

	public void setImage2Code(String image2Code) {
		this.image2Code = image2Code;
	}

	public String getImage3Code() {
		return image3Code;
	}

	public void setImage3Code(String image3Code) {
		this.image3Code = image3Code;
	}

	public String getImage4Code() {
		return image4Code;
	}

	public void setImage4Code(String image4Code) {
		this.image4Code = image4Code;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getMediaStatus() {
		return mediaStatus;
	}

	public void setMediaStatus(Integer mediaStatus) {
		this.mediaStatus = mediaStatus;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public Integer getLoveNum() {
		return loveNum;
	}

	public void setLoveNum(Integer loveNum) {
		this.loveNum = loveNum;
	}

	public Integer getHateNum() {
		return hateNum;
	}

	public void setHateNum(Integer hateNum) {
		this.hateNum = hateNum;
	}

	public String getInjectionStatus() {
		return injectionStatus;
	}

	public void setInjectionStatus(String injectionStatus) {
		this.injectionStatus = injectionStatus;
	}

	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
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

	public String getCompere() {
		return compere;
	}

	public void setCompere(String compere) {
		this.compere = compere;
	}

	public String getGuest() {
		return guest;
	}

	public void setGuest(String guest) {
		this.guest = guest;
	}

	public String getKpeople() {
		return kpeople;
	}

	public void setKpeople(String kpeople) {
		this.kpeople = kpeople;
	}

	public String getScriptWriter() {
		return scriptWriter;
	}

	public void setScriptWriter(String scriptWriter) {
		this.scriptWriter = scriptWriter;
	}

	public String getReporter() {
		return reporter;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public String getIncharge() {
		return incharge;
	}

	public void setIncharge(String incharge) {
		this.incharge = incharge;
	}

	public String getCopyRight() {
		return copyRight;
	}

	public void setCopyRight(String copyRight) {
		this.copyRight = copyRight;
	}

	public String getVspCode() {
		return vspCode;
	}

	public void setVspCode(String vspCode) {
		this.vspCode = vspCode;
	}

	public String getSpCode() {
		return spCode;
	}

	public void setSpCode(String spCode) {
		this.spCode = spCode;
	}

	public String getCpCode() {
		return cpCode;
	}

	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getStoragePath() {
		return storagePath;
	}

	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getSubmitUser() {
		return submitUser;
	}

	public void setSubmitUser(String submitUser) {
		this.submitUser = submitUser;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public String getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getAuditComment() {
		return auditComment;
	}

	public void setAuditComment(String auditComment) {
		this.auditComment = auditComment;
	}

	public Date getStorageTime() {
		return storageTime;
	}

	public void setStorageTime(Date storageTime) {
		this.storageTime = storageTime;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getReserved1() {
		return reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getReserved2() {
		return reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	public String getReserved3() {
		return reserved3;
	}

	public void setReserved3(String reserved3) {
		this.reserved3 = reserved3;
	}

	public String getReserved4() {
		return reserved4;
	}

	public void setReserved4(String reserved4) {
		this.reserved4 = reserved4;
	}

	public String getReserved5() {
		return reserved5;
	}

	public void setReserved5(String reserved5) {
		this.reserved5 = reserved5;
	}

}
