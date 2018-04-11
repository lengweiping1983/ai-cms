package com.ai.cms.transcode.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.ai.common.entity.IdEntity;
import com.ai.common.enums.MediaFileTypeEnum;
import com.ai.common.enums.YesNoEnum;

@Entity
@Table(name = "cms_transcode_request_file")
public class TranscodeRequestFile extends IdEntity {
	private static final long serialVersionUID = 1L;

	public static final String METADATA = "episodeIndex,filePath,subtitleFilePath,mediaFileType,mediaId,mediaName,mediaFilename";

	@Column(name = "request_id")
	private Long requestId;// 转码工单id

	@Column(name = "episode_index")
	private Integer episodeIndex = 1;// 第几集

	@NotNull
	@Column(name = "file_path")
	private String filePath; // 文件路径

	@Column(name = "subtitle_file_path")
	private String subtitleFilePath; // 字幕文件路径

	@NotNull
	@Column(name = "media_file_type")
	private Integer mediaFileType = MediaFileTypeEnum.DEFAULT.getKey();

	@NotNull
	@Column(name = "new_media")
	private Integer newMedia = YesNoEnum.NO.getKey();// 是否生成新的媒资

	@Column(name = "media_id")
	private Long mediaId; // 媒资id

	@Column(name = "media_name")
	private String mediaName; // 媒资名称

	@Column(name = "media_filename")
	private String mediaFilename; // 媒体文件名标识,用作转码后的媒体文件名

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Integer getEpisodeIndex() {
		return episodeIndex;
	}

	public void setEpisodeIndex(Integer episodeIndex) {
		this.episodeIndex = episodeIndex;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSubtitleFilePath() {
		return subtitleFilePath;
	}

	public void setSubtitleFilePath(String subtitleFilePath) {
		this.subtitleFilePath = subtitleFilePath;
	}

	public Integer getMediaFileType() {
		return mediaFileType;
	}

	public void setMediaFileType(Integer mediaFileType) {
		this.mediaFileType = mediaFileType;
	}

	public Integer getNewMedia() {
		return newMedia;
	}

	public void setNewMedia(Integer newMedia) {
		this.newMedia = newMedia;
	}

	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getMediaFilename() {
		return mediaFilename;
	}

	public void setMediaFilename(String mediaFilename) {
		this.mediaFilename = mediaFilename;
	}

}
