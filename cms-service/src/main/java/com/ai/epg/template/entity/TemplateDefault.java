package com.ai.epg.template.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ai.common.entity.AbstractEntity;

/**
 * 内容对应的默认模板代码
 * 
 */
@Entity
@Table(name = "epg_template_default")
public class TemplateDefault extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "env")
	private String env;

	private Integer type;

	@Column(name = "app_code")
	private String appCode;

	@Column(name = "category_code")
	private String categoryCode;

	@Column(name = "page_type")
	private String pageType;

	@Column(name = "item_type")
	private int itemType;

	@Column(name = "content_type")
	private int contentType;

	@Column(name = "template_code")
	private String templateCode;

	private String path;

	private String description;

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}