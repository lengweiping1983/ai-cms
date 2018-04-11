package com.ai.epg.subscriber.entity;

import com.ai.common.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户听单信息表
 */
@Entity
@Table(name = "user_listen_list")
public class UserListenList extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "app_code")
    private String appCode;// APP代码

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "item_type")
    private Integer itemType;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "duration_time")
    private Integer durationTime;// 节目总时长，单位秒

    @Column(name = "play_total_length")
    private Integer playTotalLength;// 播放总时长，单位秒

    @Column(name = "play_time")
    private Integer playTime;// 播放时长，单位秒

    @Column(name = "name")
    private String name; //播放视频名称

    @Column(name = "series_id")
    private Long seriesId; //剧头ID

    @Column(name = "episode")
    private Integer episode;  //多集节目类型的集数

    @Column(name = "played_episode")
    private String playedEpisode; // 播放过的集数，以逗号分隔  1，2，3，6

    @Column(name = "series_episode_index")
    private Integer seriesEpisodeIndex; //正在播放的第几集

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

    public Integer getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(Integer durationTime) {
        this.durationTime = durationTime;
    }

    public Integer getPlayTotalLength() {
        return playTotalLength;
    }

    public void setPlayTotalLength(Integer playTotalLength) {
        this.playTotalLength = playTotalLength;
    }

    public Integer getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Integer playTime) {
        this.playTime = playTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Long seriesId) {
        this.seriesId = seriesId;
    }

    public Integer getEpisode() {
        return episode;
    }

    public void setEpisode(Integer episode) {
        this.episode = episode;
    }

    public String getPlayedEpisode() {
        return playedEpisode;
    }

    public void setPlayedEpisode(String playedEpisode) {
        this.playedEpisode = playedEpisode;
    }

    public Integer getSeriesEpisodeIndex() {
        return seriesEpisodeIndex;
    }

    public void setSeriesEpisodeIndex(Integer seriesEpisodeIndex) {
        this.seriesEpisodeIndex = seriesEpisodeIndex;
    }
}
