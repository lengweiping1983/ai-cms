package com.ai.cms.injection.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.ai.cms.injection.enums.InjectionStatusEnum;
import com.ai.common.entity.AbstractEntity;

/**
 * 分发对象实体
 *
 */
@Entity
@Table(name = "cms_injection_object")
public class InjectionObject extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "platform_id")
	private Long platformId;// 分发平台

	@NotNull
	@Column(name = "category")
	private String category;// 分组

	@NotNull
	@Column(name = "item_type")
	private Integer itemType;// 元素类型

	@NotNull
	@Column(name = "item_id")
	private Long itemId;// 元素id

	@Column(name = "item_parent_id")
	private Long itemParentId;// 元素父id

	@NotNull
	@Column(name = "injection_status")
	private Integer injectionStatus = InjectionStatusEnum.DEFAULT.getKey();// 分发状态

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "injection_time")
	private Date injectionTime;// 分发时间

	@Column(name = "partner_item_code")
	private String partnerItemCode;// 运营商侧Code

	@Column(name = "partner_item_reserved1")
	private String partnerItemReserved1;

	@Column(name = "partner_item_reserved2")
	private String partnerItemReserved2;

	public InjectionObject() {

	}

	public InjectionObject(Long platformId, String category) {
		this.platformId = platformId;
		this.category = category;
	}

	public Long getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public Long getItemParentId() {
		return itemParentId;
	}

	public void setItemParentId(Long itemParentId) {
		this.itemParentId = itemParentId;
	}

	public Integer getInjectionStatus() {
		return injectionStatus;
	}

	public void setInjectionStatus(Integer injectionStatus) {
		this.injectionStatus = injectionStatus;
	}

	public Date getInjectionTime() {
		return injectionTime;
	}

	public void setInjectionTime(Date injectionTime) {
		this.injectionTime = injectionTime;
	}

	public String getPartnerItemCode() {
		return partnerItemCode;
	}

	public void setPartnerItemCode(String partnerItemCode) {
		this.partnerItemCode = partnerItemCode;
	}

	public String getPartnerItemReserved1() {
		return partnerItemReserved1;
	}

	public void setPartnerItemReserved1(String partnerItemReserved1) {
		this.partnerItemReserved1 = partnerItemReserved1;
	}

	public String getPartnerItemReserved2() {
		return partnerItemReserved2;
	}

	public void setPartnerItemReserved2(String partnerItemReserved2) {
		this.partnerItemReserved2 = partnerItemReserved2;
	}

}
