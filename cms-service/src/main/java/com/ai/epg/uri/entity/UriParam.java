package com.ai.epg.uri.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ai.common.entity.AbstractEntity;

/**
 * 页面的参数配置
 * 
 */
@Entity
@Table(name = "epg_uri_param")
public class UriParam extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "uri_id")
	private Long uriId;

	@Column(name = "number")
	private String number;// 编号

	@Column(name = "item_type")
	private Integer itemType;// 类型

	@Column(name = "item_id")
	private Long itemId;// 元素id

	@Column(name = "style")
	private String style;// 样式

	public Long getUriId() {
		return uriId;
	}

	public void setUriId(Long uriId) {
		this.uriId = uriId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}