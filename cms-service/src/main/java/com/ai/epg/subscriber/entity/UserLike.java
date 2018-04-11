package com.ai.epg.subscriber.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ai.common.entity.AbstractCreateTimeEntity;

/**
 * 用户点赞信息
 */
@Entity
@Table(name = "user_like")
public class UserLike extends AbstractCreateTimeEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "item_id")
	private Long itemId;

	@Column(name = "item_type")
	private Integer itemType;

	@Column(name = "name")
	private String name;// 内容名称，可以是定制名称

	@Column(name = "app_code")
	private String appCode;// APP代码

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

}
