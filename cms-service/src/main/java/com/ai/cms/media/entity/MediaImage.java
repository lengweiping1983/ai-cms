package com.ai.cms.media.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.ai.common.entity.AbstractEntity;

/**
 * 媒体图片实体
 *
 */
@Entity
@Table(name = "cms_media_image")
public class MediaImage extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	public static final String METADATA = "seriesId,programId,type,sortIndex,fileSize,filePath,sourceFileName";

	@Column(name = "series_id")
	private Long seriesId;// 所属剧头id

	@Column(name = "program_id")
	private Long programId;// 所属节目id

	@NotNull
	private Integer type = 2;// 2=剧照

	@Column(name = "sort_index")
	private Integer sortIndex = 1; // 排序值

	@Column(name = "file_size")
	private Long fileSize;// 文件大小
	@Column(name = "file_path")
	private String filePath;// 文件路径

	@Column(name = "source_file_name")
	private String sourceFileName;

	public Long getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(Long seriesId) {
		this.seriesId = seriesId;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSourceFileName() {
		return sourceFileName;
	}

	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

}
