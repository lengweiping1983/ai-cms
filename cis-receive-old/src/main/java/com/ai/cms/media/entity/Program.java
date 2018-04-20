package com.ai.cms.media.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.ai.cms.injection.enums.PlayCodeStatusEnum;
import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.MediaStatusEnum;
import com.ai.common.enums.MediaSyncEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.ProgramTypeEnum;
import com.ai.common.enums.SourceEnum;
import com.ai.common.utils.DateUtils;

/**
 * 节目实体
 *
 */
@Entity
@Table(name = "epg_content_program")
public class Program extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	public final static String PROGRAM_CODE_PREFIX = "";

	public static final String METADATA = "contentType,title,searchName,language,caption,directors,directorsPinyin,actors,actorsPinyin,comperes,guests,genres,year,"
			+ "area,orgAirDate,authorizeInfo,authorizeAddress,licensingWindowStart,licensingWindowEnd,tags,keyword,viewpoint,rating,duration,info,"
			+ "episodeIndex,cpId,broadcastLicense,internalTag,reserved1,reserved2";
	public static final String STAGE = "stageImages";
	public static final String POSTER = "image1,image2";
	public static final String METADATA_OTHER = "sourceType,orderNumber,originalName,sortName,kpeople,scriptWriter,reporter,incharge,mediaId,mediaCode,"
			+ "vspCode,spCode,cpCode,copyRight,loveNum,hateNum,reserved3,reserved4,reserved5";

	private String code;// 内容唯一标识，用于外部系统交互

	@Column(name = "content_type")
	private Integer contentType;// 内容类型:1=电影, 2=电视剧, 3=综艺, 4=体育, 5=少儿, 6=纪实,
								// 7=生活, 8=教育, 9=音乐, 10=新闻, 11=财经, 12=法治, 13=电竞,
								// 14=原创

	@NotNull
	private String name;// 管理名称

	private String title;// 显示名称

	@Column(name = "order_number")
	private String orderNumber;// 节目订购编号

	@Column(name = "original_name")
	private String originalName;// 原名

	@Column(name = "sort_name")
	private String sortName;// 索引名称供界面排序

	@Column(name = "search_name")
	private String searchName;// 搜索名称供界面搜索

	private String language;// 语言
	private String caption;// 是否有字幕:0=否、1=是

	private String directors;// 导演，使用','分割

	@Column(name = "directors_pinyin")
	private String directorsPinyin;// 导演名拼音首字母，使用','分割

	private String actors;// 演员，使用','分割

	@Column(name = "actors_pinyin")
	private String actorsPinyin;// 演员名拼音首字母，使用','分割

	private String comperes;// 主持人，使用','分割
	private String guests;// 嘉宾，使用','分割

	private String kpeople;// 主要人物
	@Column(name = "script_writer")
	private String scriptWriter;// 编剧
	private String reporter;// 记者
	private String incharge;// 其他责任人

	@Transient
	private String genres;// 类别，使用','分割, 不需要了，现统一使用TAG

	private String year;// 年份
	private String area;// 地区

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "org_air_date")
	private Date orgAirDate;// 首播时间
	@Column(name = "authorize_info")
	private String authorizeInfo;// 版权性质
	@Column(name = "authorize_address")
	private String authorizeAddress;// 授权地址
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "licensing_window_start")
	private Date licensingWindowStart = DateUtils
			.parseDate("2016-01-01 00:00:00");// 授权开始时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "licensing_window_end")
	private Date licensingWindowEnd = DateUtils
			.parseDate("2035-12-31 23:59:59");// 授权结束时间

	private String tags;// tag，之间使用','分割
	private String keyword;// 关键字，内部使用
	private String viewpoint;// 看点

	private Float rating;// 评分，10分制，有一个小数
	private Integer duration;// 时长，单位分钟

	private String info;// 简介

	@Column(name = "episode_index")
	private Integer episodeIndex = 1;// 第几集

	@NotNull
	private int type = ProgramTypeEnum.MOVIE.getKey();// 节目类型:1=单集类型、2=多集类型,多集类型通过series_id得到剧头

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "series_id", insertable = false, updatable = false)
	private Series series;// 所属剧头，针对剧头

	@Column(name = "series_id")
	private Long seriesId;// 所属剧头id

	@Column(name = "series_code")
	private String seriesCode;// 所属剧头代码

	@Column(name = "source_type")
	private Integer sourceType;// 1:VOD,5:Advertisement

	private String image1;// 海报1
	private String image2;// 海报2

	@Column(name = "image1_code")
	private String image1Code;// 海报1标识

	@Column(name = "image2_code")
	private String image2Code;// 海报2标识
	
	private String image3;// 横海报(大)
	private String image4;// 竖海报(大)
	@Column(name = "image3_code")
	private String image3Code;// 横海报(大)标识
	@Column(name = "image4_code")
	private String image4Code;// 竖海报(大)标识
	

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

	@Column(name = "stage_images")
	private String stageImages;// 剧照图片，使用','分割，每段使用图片url方式构成

	@NotNull
	@Column(name = "status")
	private int status = OnlineStatusEnum.DEFAULT.getKey();// 状态:0=待上线、1=已上线、2=已下线

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "online_time")
	private Date onlineTime;// 上线时间

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "offline_time")
	private Date offlineTime;// 下线时间

	@Column(name = "media_id")
	private Long mediaId;// 媒资id,一个媒资如有多个码率生成多个内容

	@Column(name = "media_code")
	private String mediaCode;// 关联内容唯一标识

	@NotNull
	@Column(name = "media_status")
	private int mediaStatus = MediaStatusEnum.MISS_VIDEO.getKey()
			+ MediaStatusEnum.MISS_IMAGE.getKey();// 媒资状态

	@NotNull
	@Column(name = "media_sync")
	private int mediaSync = MediaSyncEnum.METADATA.getKey()
			+ MediaSyncEnum.STAGE.getKey() + MediaSyncEnum.POSTER.getKey();// 同步更新媒资信息

	@NotNull
	private int source = SourceEnum.MANUAL.getKey();// 方式:0=手工录入、1=批量导入、2=自动采集、3=转码、4=中央CMS、5=SMP

	@Column(name = "platform_id")
	private Long platformId;// 注入平台

	@NotNull
	@Column(name = "injection_status")
	private int injectionStatus = 1;// 注入状态

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "injection_time")
	private Date injectionTime;// 注入时间

	@Column(name = "partner_item_code")
	private String partnerItemCode;// 运营商侧Code
	
	@Column(name = "partner_play_code_status")
	private Integer partnerPlayCodeStatus = PlayCodeStatusEnum.DEFAULT.getKey();// 播放代码输入状态

	@Column(name = "cloud_id")
	private String cloudId;// 云端id

	@Column(name = "cloud_code")
	private String cloudCode;// 云端代码

	@Column(name = "template_id")
	private Long templateId;// 转码模板id

	@Column(name = "vsp_code")
	private String vspCode;// 内容服务平台标识
	@Column(name = "sp_code")
	private String spCode;// 提供商标识
	@Column(name = "cp_code")
	private String cpCode;// 内容提供商标识
	@Column(name = "copy_right")
	private String copyRight;// 版权方标识
	@Column(name = "cp_id")
	private Long cpId;// 内容提供商id

	@Column(name = "broadcast_license")
	private String broadcastLicense;// 播出许可证

	@Column(name = "internal_tag")
	private String internalTag;// 内部标签

	@Column(name = "love_num")
	private int loveNum;// 喜欢数

	@Column(name = "hate_num")
	private int hateNum;// 不喜欢数

	private String products;// 产品列表，使用','分割

	private String reserved1;
	private String reserved2;
	private String reserved3;
	private String reserved4;
	private String reserved5;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getDirectors() {
		return directors;
	}

	public void setDirectors(String directors) {
		this.directors = directors;
	}

	public String getDirectorsPinyin() {
		return directorsPinyin;
	}

	public void setDirectorsPinyin(String directorsPinyin) {
		this.directorsPinyin = directorsPinyin;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public String getActorsPinyin() {
		return actorsPinyin;
	}

	public void setActorsPinyin(String actorsPinyin) {
		this.actorsPinyin = actorsPinyin;
	}

	public String getComperes() {
		return comperes;
	}

	public void setComperes(String comperes) {
		this.comperes = comperes;
	}

	public String getGuests() {
		return guests;
	}

	public void setGuests(String guests) {
		this.guests = guests;
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

	public String getGenres() {
		return genres;
	}

	public void setGenres(String genres) {
		this.genres = genres;
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

	public Date getOrgAirDate() {
		return orgAirDate;
	}

	public void setOrgAirDate(Date orgAirDate) {
		this.orgAirDate = orgAirDate;
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

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Integer getEpisodeIndex() {
		return episodeIndex;
	}

	public void setEpisodeIndex(Integer episodeIndex) {
		this.episodeIndex = episodeIndex;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Series getSeries() {
		return series;
	}

	public void setSeries(Series series) {
		this.series = series;
	}

	public Long getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(Long seriesId) {
		this.seriesId = seriesId;
	}

	public String getSeriesCode() {
		return seriesCode;
	}

	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
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

	public String getStageImages() {
		return stageImages;
	}

	public void setStageImages(String stageImages) {
		this.stageImages = stageImages;
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

	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	public String getMediaCode() {
		return mediaCode;
	}

	public void setMediaCode(String mediaCode) {
		this.mediaCode = mediaCode;
	}

	public int getMediaStatus() {
		return mediaStatus;
	}

	public void setMediaStatus(int mediaStatus) {
		this.mediaStatus = mediaStatus;
	}

	public int getMediaSync() {
		return mediaSync;
	}

	public void setMediaSync(int mediaSync) {
		this.mediaSync = mediaSync;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public Long getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}

	public int getInjectionStatus() {
		return injectionStatus;
	}

	public void setInjectionStatus(int injectionStatus) {
		this.injectionStatus = injectionStatus;
	}

	public Date getInjectionTime() {
		return injectionTime;
	}

	public void setInjectionTime(Date injectionTime) {
		this.injectionTime = injectionTime;
	}

	public String getPartnerItemCode() {
		return partnerItemCode;
	}

	public void setPartnerItemCode(String partnerItemCode) {
		this.partnerItemCode = partnerItemCode;
	}
	
	public Integer getPartnerPlayCodeStatus() {
		return partnerPlayCodeStatus;
	}

	public void setPartnerPlayCodeStatus(Integer partnerPlayCodeStatus) {
		this.partnerPlayCodeStatus = partnerPlayCodeStatus;
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

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
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

	public String getCopyRight() {
		return copyRight;
	}

	public void setCopyRight(String copyRight) {
		this.copyRight = copyRight;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
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

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
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

	public String getBroadcastLicense() {
		return broadcastLicense;
	}

	public void setBroadcastLicense(String broadcastLicense) {
		this.broadcastLicense = broadcastLicense;
	}

	public String getInternalTag() {
		return internalTag;
	}

	public void setInternalTag(String internalTag) {
		this.internalTag = internalTag;
	}

}