package com.ai.cms.league.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ai.common.entity.AbstractEntity;

/**
 * 赛事运营商Code
 *
 */
@Entity
@Table(name = "cms_league_match_code")
public class LeagueMatchCode extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "item_type")
	private Integer itemType;// 元素类型

	@Column(name = "item_id")
	private Long itemId;// 元素标识

	@Column(name = "charge_code")
	private String chargeCode;// 默认平台计费代码

	@Column(name = "play_code")
	private String playCode;// 默认平台内容回看代码

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

	public String getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	public String getPlayCode() {
		return playCode;
	}

	public void setPlayCode(String playCode) {
		this.playCode = playCode;
	}

}
