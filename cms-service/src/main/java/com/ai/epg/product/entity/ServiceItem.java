package com.ai.epg.product.entity;

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
 * 服务项实体
 */
@Entity
@Table(name = "epg_service_item")
public class ServiceItem extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "service_id")
	private Long serviceId;// 所属服务Id

	@Column(name = "item_type")
	private Integer itemType;// 元素类型

	@Column(name = "item_id")
	private Long itemId;// 元素标识

	@Column(name = "sort_index")
	private Integer sortIndex = 1; // 排序值

	@Column(name = "status")
	private Integer status = OnlineStatusEnum.ONLINE.getKey(); // 状态:0=未上线、1=已上线、2=已下线
	
	@Column(name = "item_status")
	private Integer itemStatus = OnlineStatusEnum.DEFAULT.getKey(); // 元素状态:0=未上线、1=已上线、2=已下线

	@Column(name = "site_code")
	private String siteCode;// 渠道代码

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

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
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
