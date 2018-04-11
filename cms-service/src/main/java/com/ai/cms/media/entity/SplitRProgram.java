package com.ai.cms.media.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ai.common.entity.AbstractEntity;

/**
 * 拆条关系
 *
 */
@Entity
@Table(name = "cms_media_split")
public class SplitRProgram extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "item_type")
	private Integer itemType;// 元素类型

	@Column(name = "item_id")
	private Long itemId;// 元素id

	@Column(name = "program_id")
	private Long programId;// 拆条后的节目id

	@Column(name = "site_code")
	private String siteCode;// 渠道代码

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

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

}
