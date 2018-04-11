package com.ai.epg.config.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.ai.common.entity.AbstractEntity;

/**
 * 入口实体
 */
@Entity
@Table(name = "epg_entry")
public class Entry extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "code")
	private String code;

	@NotNull
	@Column(name = "name")
	private String name;

	@NotNull
	@Column(name = "service_url")
	private String serviceUrl;

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

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

}
