package com.ai.cms.league.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.OnlineStatusEnum;

/**
 * 赛季关联俱乐部实体
 */
@Entity
@Table(name = "cms_league_season_club")
public class LeagueSeasonClub extends AbstractEntity {
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

	@Column(name = "win_num")
	private Integer winNum = 0;// 胜

	@Column(name = "flat_num")
	private Integer flatNum = 0;// 平

	@Column(name = "lose_num")
	private Integer loseNum = 0;// 输

	@Column(name = "enter_num")
	private Integer enterNum = 0;// 进球

	@Column(name = "point_num")
	private Integer pointNum = 0;// 点球

	@Column(name = "fumble_num")
	private Integer fumbleNum = 0;// 失球

	@Column(name = "credit_num")
	private Integer creditNum = 0;// 积分

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

	public Integer getWinNum() {
		return winNum;
	}

	public void setWinNum(Integer winNum) {
		this.winNum = winNum;
	}

	public Integer getFlatNum() {
		return flatNum;
	}

	public void setFlatNum(Integer flatNum) {
		this.flatNum = flatNum;
	}

	public Integer getLoseNum() {
		return loseNum;
	}

	public void setLoseNum(Integer loseNum) {
		this.loseNum = loseNum;
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

	public Integer getFumbleNum() {
		return fumbleNum;
	}

	public void setFumbleNum(Integer fumbleNum) {
		this.fumbleNum = fumbleNum;
	}

	public Integer getCreditNum() {
		return creditNum;
	}

	public void setCreditNum(Integer creditNum) {
		this.creditNum = creditNum;
	}

}