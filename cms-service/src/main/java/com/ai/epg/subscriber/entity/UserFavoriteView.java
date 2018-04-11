package com.ai.epg.subscriber.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ai.common.entity.AbstractCreateTimeEntity;
import com.ai.common.enums.OnlineStatusEnum;

/**
 * 用户收藏信息表
 */
@Entity
@Table(name = "user_favorite_view")
public class UserFavoriteView extends AbstractCreateTimeEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "item_type")
	private Integer itemType;

	@Column(name = "item_id")
	private Long itemId;

	@Column(name = "name")
	private String name;// 内容名称，可以是定制名称

	@Column(name = "item_name")
	private String itemName;// 元素名称

	@Column(name = "item_title")
	private String itemTitle;// 元素显示名称

	@Column(name = "item_image1")
	private String itemImage1;// 元素横海报

	@Column(name = "item_image2")
	private String itemImage2;// 元素竖海报

	@Column(name = "item_status")
	private Integer itemStatus = OnlineStatusEnum.DEFAULT.getKey(); // 元素状态:0=未上线、1=已上线、2=已下线

	@Column(name = "app_code")
	private String appCode;// APP代码

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public String getItemImage1() {
		return itemImage1;
	}

	public void setItemImage1(String itemImage1) {
		this.itemImage1 = itemImage1;
	}

	public String getItemImage2() {
		return itemImage2;
	}

	public void setItemImage2(String itemImage2) {
		this.itemImage2 = itemImage2;
	}

	public Integer getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(Integer itemStatus) {
		this.itemStatus = itemStatus;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

}
