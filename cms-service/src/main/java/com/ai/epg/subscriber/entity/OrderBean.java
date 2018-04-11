package com.ai.epg.subscriber.entity;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.ChargeTimeUnitEnum;
import com.ai.common.enums.OrderStatusEnum;

/**
 * 订购收费对象
 */
public class OrderBean extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	private Integer type;// 类型

	private String name;// 计费点名称
	
	private Integer timeUnit = ChargeTimeUnitEnum.MONTH.getKey();// 时段单位

	private Integer duration = 1;// 时长

	private Integer price;// 现价

	private String intro;// 简单介绍

	private String description;// 描述

	private String partnerProductCode;// 本地产品代码

	private Integer displayStatus = OrderStatusEnum.ORDER.getKey(); // 订购状态（0=不可订购，1=立即订购，2=已订购,3=退订，4=不可退订）

	private Integer clickStatus = 1; // 点击订购的标记（0=不可订购，1=已订购，2=可订购）

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getPartnerProductCode() {
		return partnerProductCode;
	}

	public void setPartnerProductCode(String partnerProductCode) {
		this.partnerProductCode = partnerProductCode;
	}

	public Integer getDisplayStatus() {
		return displayStatus;
	}

	public void setDisplayStatus(Integer displayStatus) {
		this.displayStatus = displayStatus;
	}

	public Integer getClickStatus() {
		return clickStatus;
	}

	public void setClickStatus(Integer clickStatus) {
		this.clickStatus = clickStatus;
	}
}