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
 * 服务实体
 */
@Entity
@Table(name = "epg_service")
public class Service extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	private String code;// 服务代码

	private String name;// 服务名称

	private Integer type;// 服务类型

	@Column(name = "sort_name")
	private String sortName;// 索引名称供界面排序

	@Column(name = "search_name")
	private String searchName;// 搜索名称供界面搜索

	@Column(name = "rental_period")
	private String rentalPeriod;

	@Column(name = "order_number")
	private String orderNumber;// 订购编号

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "licensing_window_start")
	private Date licensingWindowStart = DateUtils
			.parseDate("2016-01-01 00:00:00");// 授权开始时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "licensing_window_end")
	private Date licensingWindowEnd = DateUtils
			.parseDate("2035-12-31 23:59:59");// 授权结束时间

	private Integer price;// 价格

	@Column(name = "status")
	private Integer status = OnlineStatusEnum.DEFAULT.getKey(); // 状态

	private String description;// 描述

	private String tag;// tag，之间使用','分割
	private String keyword;// 关键字，内部使用

	private String reserved1;
	private String reserved2;
	private String reserved3;
	private String reserved4;
	private String reserved5;

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getRentalPeriod() {
		return rentalPeriod;
	}

	public void setRentalPeriod(String rentalPeriod) {
		this.rentalPeriod = rentalPeriod;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
