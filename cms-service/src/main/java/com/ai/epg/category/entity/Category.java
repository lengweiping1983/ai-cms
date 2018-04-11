package com.ai.epg.category.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.CategoryTypeEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.YesNoEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 栏目实体
 */
@Entity
@Table(name = "epg_category")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Category extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", insertable = false, updatable = false)
	private Category parent; // 父级栏目

	@Column(name = "parent_id")
	private Long parentId;// 父级栏目Id

	@Column(name = "type")
	private Integer type = CategoryTypeEnum.DEFAULT.getKey(); // 类型（1=普通栏目、2=父栏目）

	private String code;// 栏目代码

	private String name;// 栏目名称

	private String title;// 显示名称

	private String description;// 描述

	@Column(name = "status")
	private Integer status = OnlineStatusEnum.DEFAULT.getKey(); // 状态:0=未上线、1=已上线、2=已下线

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "online_time")
	private Date onlineTime;// 上线时间

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "offline_time")
	private Date offlineTime;// 下线时间

	@Column(name = "sort_index")
	private Integer sortIndex = 999; // 排序值

	@Column(name = "app_code")
	private String appCode;// APP代码

	@Column(name = "site_code")
	private String siteCode;// 渠道代码

	private String tag;// tag，之间使用','分割
	private String keyword;// 关键字，内部使用

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

	@Transient
	private List<Category> childList = new ArrayList<Category>();// 拥有子栏目列表

	@Column(name = "platform_id")
	private Long platformId;// 分发平台

	@NotNull
	@Column(name = "injection_status")
	private int injectionStatus = 1;// 分发状态

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "injection_time")
	private Date injectionTime;// 分发时间

	@Column(name = "partner_item_code")
	private String partnerItemCode;// 运营商侧Code

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
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

	public List<Category> getChildList() {
		return childList;
	}

	public void setChildList(List<Category> childList) {
		this.childList = childList;
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

}
