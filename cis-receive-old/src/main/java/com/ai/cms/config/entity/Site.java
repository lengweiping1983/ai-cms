package com.ai.cms.config.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.ValidStatusEnum;

/**
 * 渠道实体
 */
@Entity
@Table(name = "com_site")
public class Site extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@NotNull
	private String code;// 渠道代码

	@NotNull
	private String name;// 渠道名称

	private String description;// 描述

	@NotNull
	@Column(name = "status")
	private Integer status = ValidStatusEnum.VALID.getKey(); // 状态:0=失效,1=生效

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

}
