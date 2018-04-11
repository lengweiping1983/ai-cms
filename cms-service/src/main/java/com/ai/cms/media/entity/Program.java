package com.ai.cms.media.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.ai.common.enums.ProgramTypeEnum;

/**
 * 节目实体
 *
 */
@Entity
@Table(name = "cms_media_program")
public class Program extends BaseMedia {
	private static final long serialVersionUID = 1L;

	public static final String METADATA = BaseMedia.METADATA
			+ ",type,seriesId,episodeIndex,sourceType";
	public static final String METADATA_OTHER = BaseMedia.METADATA_OTHER;
	public static final String POSTER = BaseMedia.POSTER;

	@NotNull
	private Integer type = ProgramTypeEnum.MOVIE.getKey();// 节目类型:1=单集类型、2=多集类型,多集类型通过seriesId得到剧头

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "series_id", insertable = false, updatable = false)
	private Series series;// 所属剧头，针对剧头

	@Column(name = "series_id")
	private Long seriesId;// 所属剧头id

	@NotNull
	@Column(name = "episode_index")
	private Integer episodeIndex = 1;// 第几集

	@Column(name = "source_type")
	private Integer sourceType;// 1:VOD,5:Advertisement

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Series getSeries() {
		return series;
	}

	public void setSeries(Series series) {
		this.series = series;
	}

	public Long getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(Long seriesId) {
		this.seriesId = seriesId;
	}

	public Integer getEpisodeIndex() {
		return episodeIndex;
	}

	public void setEpisodeIndex(Integer episodeIndex) {
		this.episodeIndex = episodeIndex;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

}