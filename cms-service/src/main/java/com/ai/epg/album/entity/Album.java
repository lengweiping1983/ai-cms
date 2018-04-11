package com.ai.epg.album.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.AlbumTypeEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.YesNoEnum;

/**
 * 专题实体
 */
@Entity
@Table(name = "epg_album")
public class Album extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "type")
	private Integer type = AlbumTypeEnum.DEFAULT.getKey();

	private String code;// 专题代码

	private String name;// 专题名称

	private String title;// 显示名称

	@Column(name = "image1")
	private String image1;// 横海报

	@Column(name = "image2")
	private String image2;// 竖海报

	@Column(name = "background_whole")
	private Integer backgroundWhole = YesNoEnum.YES.getKey();// 背景是否是整张图
	
	@Column(name = "background_image")
	private String backgroundImage;// 背景图片

	@Column(name = "bg_up")
	private String bgUp;// 背景图片上

	@Column(name = "bg_down")
	private String bgDown;// 背景图片下

	@Column(name = "bg_left")
	private String bgLeft;// 背景图片左

	@Column(name = "bg_right")
	private String bgRight;// 背景图片右

	@Column(name = "template_code")
	private String templateCode;// 模板代码

	@Column(name = "app_code")
	private String appCode;// APP代码

	@Column(name = "status")
	private Integer status = OnlineStatusEnum.DEFAULT.getKey(); // 状态:0=未上线、1=已上线、2=已下线

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "online_time")
	private Date onlineTime;// 上线时间

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "offline_time")
	private Date offlineTime;// 下线时间

	private String tag;// tag，之间使用','分割

	private String keyword;// 关键字，内部使用

	private String description;// 描述

	@Column(name = "love_num")
	private int loveNum;// 喜欢数

	@Column(name = "hate_num")
	private int hateNum;// 不喜欢数

	@Column(name = "site_code")
	private String siteCode;// 渠道代码

	@Column(name = "widget_content_type")
	private Integer widgetContentType = 1; // 1=仅点播，2=仅直播，3=直播加点播

	@Column(name = "widget_code")
	private String widgetCode;// 推荐位代码

	@Column(name = "widget_left")
	private Integer widgetLeft = 158; // 推荐位left坐标

	@Column(name = "widget_top")
	private Integer widgetTop = 148; // 推荐位top坐标

	@Column(name = "widget_width")
	private Integer widgetWidth = 560; // 推荐位width坐标

	@Column(name = "widget_height")
	private Integer widgetHeight = 312; // 推荐位height坐标

	@Column(name = "check_duration")
	private Integer checkDuration = 120; // 定时检测时长

	@Column(name = "show_program_info")
	private Integer showProgramInfo = YesNoEnum.YES.getKey(); // 是否显示节目信息条

	@Column(name = "program_info_bar_height")
	private Integer programInfoBarHeight = 24; // 节目信息条高度
	
    @Column(name = "config_item_types")
	private String configItemTypes;// 可配置的元素类型

	@Column(name = "config_image1")
	private Integer configImage1 = YesNoEnum.YES.getKey(); // 横海报可配置

	@Column(name = "config_image1_width")
	private Integer configImage1Width;

	@Column(name = "config_image1_height")
	private Integer configImage1Height;

	@Column(name = "config_image2")
	private Integer configImage2 = YesNoEnum.NO.getKey(); // 竖海报可配置

	@Column(name = "config_image2_width")
	private Integer configImage2Width;

	@Column(name = "config_image2_height")
	private Integer configImage2Height;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Integer getBackgroundWhole() {
		return backgroundWhole;
	}

	public void setBackgroundWhole(Integer backgroundWhole) {
		this.backgroundWhole = backgroundWhole;
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public String getBgUp() {
		return bgUp;
	}

	public void setBgUp(String bgUp) {
		this.bgUp = bgUp;
	}

	public String getBgDown() {
		return bgDown;
	}

	public void setBgDown(String bgDown) {
		this.bgDown = bgDown;
	}

	public String getBgLeft() {
		return bgLeft;
	}

	public void setBgLeft(String bgLeft) {
		this.bgLeft = bgLeft;
	}

	public String getBgRight() {
		return bgRight;
	}

	public void setBgRight(String bgRight) {
		this.bgRight = bgRight;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public Integer getWidgetContentType() {
		return widgetContentType;
	}

	public void setWidgetContentType(Integer widgetContentType) {
		this.widgetContentType = widgetContentType;
	}

	public String getWidgetCode() {
		return widgetCode;
	}

	public void setWidgetCode(String widgetCode) {
		this.widgetCode = widgetCode;
	}

	public Integer getWidgetLeft() {
		return widgetLeft;
	}

	public void setWidgetLeft(Integer widgetLeft) {
		this.widgetLeft = widgetLeft;
	}

	public Integer getWidgetTop() {
		return widgetTop;
	}

	public void setWidgetTop(Integer widgetTop) {
		this.widgetTop = widgetTop;
	}

	public Integer getWidgetWidth() {
		return widgetWidth;
	}

	public void setWidgetWidth(Integer widgetWidth) {
		this.widgetWidth = widgetWidth;
	}

	public Integer getWidgetHeight() {
		return widgetHeight;
	}

	public void setWidgetHeight(Integer widgetHeight) {
		this.widgetHeight = widgetHeight;
	}

	public Integer getCheckDuration() {
		return checkDuration;
	}

	public void setCheckDuration(Integer checkDuration) {
		this.checkDuration = checkDuration;
	}

	public Integer getShowProgramInfo() {
		return showProgramInfo;
	}

	public void setShowProgramInfo(Integer showProgramInfo) {
		this.showProgramInfo = showProgramInfo;
	}

	public Integer getProgramInfoBarHeight() {
		return programInfoBarHeight;
	}

	public void setProgramInfoBarHeight(Integer programInfoBarHeight) {
		this.programInfoBarHeight = programInfoBarHeight;
	}

	public String getConfigItemTypes() {
		return configItemTypes;
	}

	public void setConfigItemTypes(String configItemTypes) {
		this.configItemTypes = configItemTypes;
	}

	public Integer getConfigImage1() {
		return configImage1;
	}

	public void setConfigImage1(Integer configImage1) {
		this.configImage1 = configImage1;
	}

	public Integer getConfigImage1Width() {
		return configImage1Width;
	}

	public void setConfigImage1Width(Integer configImage1Width) {
		this.configImage1Width = configImage1Width;
	}

	public Integer getConfigImage1Height() {
		return configImage1Height;
	}

	public void setConfigImage1Height(Integer configImage1Height) {
		this.configImage1Height = configImage1Height;
	}

	public Integer getConfigImage2() {
		return configImage2;
	}

	public void setConfigImage2(Integer configImage2) {
		this.configImage2 = configImage2;
	}

	public Integer getConfigImage2Width() {
		return configImage2Width;
	}

	public void setConfigImage2Width(Integer configImage2Width) {
		this.configImage2Width = configImage2Width;
	}

	public Integer getConfigImage2Height() {
		return configImage2Height;
	}

	public void setConfigImage2Height(Integer configImage2Height) {
		this.configImage2Height = configImage2Height;
	}

}
