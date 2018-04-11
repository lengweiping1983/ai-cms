package com.ai.cms.league.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.OnlineStatusEnum;

/**
 * 榜单实体
 */
@Entity
@Table(name = "cms_league_season_star")
public class LeagueSeasonStar extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "league_season_id")
	private Long leagueSeasonId;// 赛季Id

	@Column(name = "item_type")
	private Integer itemType;// 元素类型

	@Column(name = "item_id")
	private Long itemId;// 元素标识

	private String title;// 定制名称

	private String image1;// 定制横海报

	private String image2;// 定制竖海报

	@Column(name = "sort_index")
	private Integer sortIndex = 999; // 排序值

	@Column(name = "status")
	private Integer status = OnlineStatusEnum.DEFAULT.getKey(); // 状态:0=未上线、1=已上线、2=已下线

	@Column(name = "site_num")
	private Integer siteNum = 0;// 场次

	@Column(name = "enter_num")
	private Integer enterNum = 0;// 进球

	@Column(name = "point_num")
	private Integer pointNum = 0;// 点球

	@Column(name = "assist_num")
	private Integer assistNum = 0;// 助攻

	public Long getLeagueSeasonId() {
		return leagueSeasonId;
	}

	public void setLeagueSeasonId(Long leagueSeasonId) {
		this.leagueSeasonId = leagueSeasonId;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
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

	public Integer getSiteNum() {
		return siteNum;
	}

	public void setSiteNum(Integer siteNum) {
		this.siteNum = siteNum;
	}

	public Integer getEnterNum() {
		return enterNum;
	}

	public void setEnterNum(Integer enterNum) {
		this.enterNum = enterNum;
	}

	public Integer getPointNum() {
		return pointNum;
	}

	public void setPointNum(Integer pointNum) {
		this.pointNum = pointNum;
	}

	public Integer getAssistNum() {
		return assistNum;
	}

	public void setAssistNum(Integer assistNum) {
		this.assistNum = assistNum;
	}

}