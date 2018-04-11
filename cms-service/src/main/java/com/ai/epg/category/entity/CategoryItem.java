package com.ai.epg.category.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.utils.DateUtils;

/**
 * 栏目项实体
 */
@Entity
@Table(name = "epg_category_item")
public class CategoryItem extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "category_id")
	private Long categoryId;// 所属栏目Id

	@Column(name = "item_type")
	private Integer itemType;// 元素类型

	@Column(name = "item_id")
	private Long itemId;// 元素标识

	private String title;// 定制名称

	private String image1;// 定制横海报

	private String image2;// 定制竖海报

	@Column(name = "sort_index")
	private Integer sortIndex = 1; // 排序值

	@Column(name = "status")
	private Integer status = OnlineStatusEnum.ONLINE.getKey(); // 状态:0=未上线、1=已上线、2=已下线
	
	@Column(name = "item_status")
	private Integer itemStatus = OnlineStatusEnum.DEFAULT.getKey(); // 元素状态:0=未上线、1=已上线、2=已下线

	@Column(name = "site_code")
	private String siteCode;// 渠道代码

	@Column(name = "group_codes")
	private String groupCodes;// 用户分组列表，使用','分割

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "valid_time")
	private Date validTime = DateUtils.parseDate("2017-01-01 00:00:00");// 生效时间

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expired_time")
	private Date expiredTime = DateUtils.parseDate("2099-12-30 23:59:59");// 失效时间

	@Column(name = "platform_id")
	private Long platformId;// 分发平台

	@NotNull
	@Column(name = "injection_status")
	private int injectionStatus = 1;// 分发状态

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "injection_time")
	private Date injectionTime;// 分发时间

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
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

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(Integer itemStatus) {
		this.itemStatus = itemStatus;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getGroupCodes() {
		return groupCodes;
	}

	public void setGroupCodes(String groupCodes) {
		this.groupCodes = groupCodes;
	}

	public Date getValidTime() {
		return validTime;
	}

	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}

	public Date getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
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

}
