package com.ai.cms.media.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.MediaImageTypeEnum;
import com.ai.common.enums.SourceEnum;

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
	private Integer type = MediaImageTypeEnum.STILLS.getKey();

	@Column(name = "sort_index")
	private Integer sortIndex = 1; // 排序值

	@Column(name = "file_size")
	private Long fileSize;// 文件大小
	@Column(name = "file_path")
	private String filePath;// 文件路径

	@Column(name = "source_file_name")
	private String sourceFileName;

	@Column(name = "cloud_id")
	private String cloudId;// 云端id
	@Column(name = "cloud_code")
	private String cloudCode;// 云端代码

	@NotNull
	private Integer source = SourceEnum.MANUAL.getKey();// 方式:0=手工录入、1=批量导入、2=自动采集、3=转码、4=中央CMS、5=分发

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

	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}

	public String getCloudCode() {
		return cloudCode;
	}

	public void setCloudCode(String cloudCode) {
		this.cloudCode = cloudCode;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

}
