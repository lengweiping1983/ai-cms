package com.ai.epg.template.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.ValidStatusEnum;
import com.ai.common.enums.YesNoEnum;

/**
 * EPG展示模板实体
 */
@Entity
@Table(name = "epg_template")
public class Template extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	private String code;// 模板代码

	private String name;// 模板名称

	private String description;// 描述

	@Column(name = "app_code")
	private String appCode;// APP代码

	@Column(name = "sort_index")
	private Integer sortIndex = 999; // 排序值

	@Column(name = "status")
	private Integer status = ValidStatusEnum.INVALID.getKey(); // 状态:0=失效,1=生效

	@Column(name = "type")
	private Integer type; // 类型

	private Integer share = YesNoEnum.NO.getKey(); // 是否共享

	@Column(name = "config_background_image")
	private Integer configBackgroundImage = YesNoEnum.YES.getKey(); // 背景图片可配置

	@Column(name = "background_image")
	private String backgroundImage;// 效果图片

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

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getShare() {
		return share;
	}

	public void setShare(Integer share) {
		this.share = share;
	}

	public Integer getConfigBackgroundImage() {
		return configBackgroundImage;
	}

	public void setConfigBackgroundImage(Integer configBackgroundImage) {
		this.configBackgroundImage = configBackgroundImage;
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

}
