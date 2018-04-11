package com.ai.epg.product.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.ChargeTimeUnitEnum;
import com.ai.common.enums.ValidStatusEnum;
import com.ai.common.enums.YesNoEnum;

/**
 * 计费点实体
 */
@Entity
@Table(name = "epg_charge")
public class Charge extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "app_code")
	private String appCode;// 所属APP代码

	@Column(name = "app_codes")
	private String appCodes;// 共享APP代码，一个计费点可能在多个应用中使用，使用','分割

	private Integer type;// 类型

	private String code;// 计费点代码

	private String name;// 计费点名称

	@Column(name = "time_unit")
	private Integer timeUnit = ChargeTimeUnitEnum.MONTH.getKey();// 时段单位

	@Column(name = "duration")
	private Integer duration = 1;// 时长

	private String intro;// 介绍

	private String description;// 描述

	@Column(name = "status")
	private Integer status = ValidStatusEnum.INVALID.getKey(); // 状态:0=失效,1=生效

	@Column(name = "origin_price")
	private Integer originPrice;// 原价

	private Integer price;// 现价

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "begin_time")
	private Date beginTime;// 有效开始时间

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time")
	private Date endTime;// 有效结束时间

	@Column(name = "subscription_extend")
	private Integer subscriptionExtend;// 是否自动续订（0=不续订、1=续订）

	@Column(name = "support_unsubscribe")
	private Integer supportUnsubscribe = YesNoEnum.NO.getKey(); // 是否支持退订

	@Column(name = "partner_product_code")
	private String partnerProductCode;// 运营商产品代码

	@Column(name = "sp_id")
	private String spId;// 运营商分配的SPID

	@Column(name = "sp_key")
	private String spKey;// 运营商分配的钥匙

	@Column(name = "sort_index")
	private Integer sortIndex = 999; // 排序值

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppCodes() {
		return appCodes;
	}

	public void setAppCodes(String appCodes) {
		this.appCodes = appCodes;
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

	public Integer getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(Integer timeUnit) {
		this.timeUnit = timeUnit;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
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

	public Integer getOriginPrice() {
		return originPrice;
	}

	public void setOriginPrice(Integer originPrice) {
		this.originPrice = originPrice;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getSubscriptionExtend() {
		return subscriptionExtend;
	}

	public void setSubscriptionExtend(Integer subscriptionExtend) {
		this.subscriptionExtend = subscriptionExtend;
	}

	public Integer getSupportUnsubscribe() {
		return supportUnsubscribe;
	}

	public void setSupportUnsubscribe(Integer supportUnsubscribe) {
		this.supportUnsubscribe = supportUnsubscribe;
	}

	public String getPartnerProductCode() {
		return partnerProductCode;
	}

	public void setPartnerProductCode(String partnerProductCode) {
		this.partnerProductCode = partnerProductCode;
	}

	public String getSpId() {
		return spId;
	}

	public void setSpId(String spId) {
		this.spId = spId;
	}

	public String getSpKey() {
		return spKey;
	}

	public void setSpKey(String spKey) {
		this.spKey = spKey;
	}

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

}
