package com.ai.cms.media.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 剧头实体
 *
 */
@Entity
@Table(name = "cms_media_series")
public class Series extends BaseMedia {
	private static final long serialVersionUID = 1L;

	public static final String METADATA = BaseMedia.METADATA
			+ ",type,episodeTotal";
	public static final String METADATA_OTHER = BaseMedia.METADATA_OTHER;
	public static final String POSTER = BaseMedia.POSTER;

	private Integer type;// 类型:1=电视剧、2=栏目剧

	@Column(name = "episode_total")
	private Integer episodeTotal;// 总集数

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getEpisodeTotal() {
		return episodeTotal;
	}

	public void setEpisodeTotal(Integer episodeTotal) {
		this.episodeTotal = episodeTotal;
	}

}
