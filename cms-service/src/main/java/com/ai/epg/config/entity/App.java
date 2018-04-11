package com.ai.epg.config.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.ValidStatusEnum;
import com.ai.common.enums.YesNoEnum;

/**
 * APP实体
 */
@Entity
@Table(name = "epg_app")
public class App extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@NotNull
	private Integer type;// APP类型

	@NotNull
	private String code;// APP代码

	@NotNull
	private String name;// APP名称

	@Column(name = "alone_charge")
	private Integer aloneCharge = YesNoEnum.NO.getKey();// 独立计费点

	@Column(name = "charge_app_code")
	private String chargeAppCode;// 计费点所属应用代码

	@Column(name = "alone_order_page")
	private Integer aloneOrderPage = YesNoEnum.NO.getKey();// 独立订购页

	@Column(name = "access_cp_id")
	private String accessCpId;// 可访问的内容提供商，使用','分割

	@Column(name = "access_app")
	private String accessApp;// 可访问的应用，使用','分割

	@Column(name = "status")
	private Integer status = ValidStatusEnum.INVALID.getKey(); // 状态:0=失效,1=生效

	private String description;// 描述

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

	public Integer getAloneCharge() {
		return aloneCharge;
	}

	public void setAloneCharge(Integer aloneCharge) {
		this.aloneCharge = aloneCharge;
	}

	public String getChargeAppCode() {
		return chargeAppCode;
	}

	public void setChargeAppCode(String chargeAppCode) {
		this.chargeAppCode = chargeAppCode;
	}

	public Integer getAloneOrderPage() {
		return aloneOrderPage;
	}

	public void setAloneOrderPage(Integer aloneOrderPage) {
		this.aloneOrderPage = aloneOrderPage;
	}

	public String getAccessCpId() {
		return accessCpId;
	}

	public void setAccessCpId(String accessCpId) {
		this.accessCpId = accessCpId;
	}

	public String getAccessApp() {
		return accessApp;
	}

	public void setAccessApp(String accessApp) {
		this.accessApp = accessApp;
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

}
