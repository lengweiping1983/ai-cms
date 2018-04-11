package com.ai.epg.subscriber.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ai.common.entity.AbstractCreateTimeEntity;

/**
 * 用户播放记录
 */
@Entity
@Table(name = "user_play_record")
public class PlayRecord extends AbstractCreateTimeEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "app_code")
	private String appCode;// APP代码

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "item_type")
	private Integer itemType;

	@Column(name = "item_id")
	private Long itemId;

	@Column(name = "play_mode")
	private String playMode;

	@Column(name = "play_total_length")
	private Integer playTotalLength;// 播放总时长，单位秒

	@Column(name = "duration_time")
	private Integer durationTime;// 节目总时长，单位秒

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

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

	public String getPlayMode() {
		return playMode;
	}

	public void setPlayMode(String playMode) {
		this.playMode = playMode;
	}

	public Integer getPlayTotalLength() {
		return playTotalLength;
	}

	public void setPlayTotalLength(Integer playTotalLength) {
		this.playTotalLength = playTotalLength;
	}

	public Integer getDurationTime() {
		return durationTime;
	}

	public void setDurationTime(Integer durationTime) {
		this.durationTime = durationTime;
	}

}
