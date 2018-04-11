package com.ai.cms.media.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.ai.cms.injection.enums.InjectionStatusEnum;
import com.ai.cms.injection.enums.PlayCodeStatusEnum;
import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.MediaFileTypeEnum;
import com.ai.common.enums.MediaStatusEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.SourceEnum;
import com.ai.common.enums.YesNoEnum;

/**
 * 媒体内容实体
 *
 */
@Entity
@Table(name = "cms_media_file")
public class MediaFile extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	public static final String METADATA = "type,templateId,duration,fileSize,fileMd5,filePath,reserved1,reserved2,reserved3,reserved4,reserved5";

	public static final String METADATA_OTHER = "";

	@Column(name = "series_id")
	private Long seriesId;// 所属剧头id

	@Column(name = "episode_index")
	private Integer episodeIndex = 1;// 第几集

	@Column(name = "program_id")
	private Long programId;// 所属节目id

	@NotNull
	private Integer type = MediaFileTypeEnum.DEFAULT.getKey();// 文件类型:1=正片、2=片花

	@Column(name = "template_id")
	private Long templateId = -1l;// 码率模板id

	@Column(name = "media_spec")
	private String mediaSpec;

	private Integer bitrate;// 码率，单位为K
	private String resolution;// 分辨率
	private String format;// 格式
	private Integer duration;// 播放时长，单位为秒
	private Integer subtitle = YesNoEnum.YES.getKey();// 是否有字幕:0=否、1=是

	@Column(name = "file_size")
	private Long fileSize;// 文件大小
	@Column(name = "file_md5")
	private String fileMd5;// 文件md5值
	@Column(name = "file_path")
	private String filePath;// 文件路径

	@Column(name = "source_file_size")
	private Long sourceFileSize;// 原始文件大小
	@Column(name = "source_file_md5")
	private String sourceFileMd5;// 原始文件md5值
	@Column(name = "source_file_path")
	private String sourceFilePath;// 原始文件路径

	@Column(name = "storage_no")
	private String storageNo; // 存储编号

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "online_time")
	private Date onlineTime;// 上线时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "offline_time")
	private Date offlineTime;// 下线时间
	@Column(name = "online_user")
	private String onlineUser;// 上线人
	@Column(name = "offline_user")
	private String offlineUser;// 下线人

	@NotNull
	@Column(name = "status")
	private Integer status = OnlineStatusEnum.DEFAULT.getKey();// 上线状态:0=未上线、1=已上线、2=已下线

	@NotNull
	@Column(name = "media_status")
	private Integer mediaStatus = MediaStatusEnum.MISS_VIDEO.getKey();// 媒资状态

	@NotNull
	private Integer source = SourceEnum.MANUAL.getKey();// 方式:0=手工录入、1=批量导入、2=自动采集、3=转码、4=中央CMS、5=分发

	@Column(name = "injection_status")
	private String injectionStatus = "" + InjectionStatusEnum.DEFAULT.getKey();// 分发状态

	@Column(name = "play_code")
	private String playCode;// 运营商侧播放代码

	@Column(name = "play_code_status")
	private Integer playCodeStatus = PlayCodeStatusEnum.DEFAULT.getKey();// 播放代码状态

	@Column(name = "cloud_id")
	private String cloudId;// 云端id

	@Column(name = "cloud_code")
	private String cloudCode;// 云端代码

	private String reserved1;
	private String reserved2;
	private String reserved3;
	private String reserved4;
	private String reserved5;

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

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getMediaSpec() {
		return mediaSpec;
	}

	public void setMediaSpec(String mediaSpec) {
		this.mediaSpec = mediaSpec;
	}

	public Integer getBitrate() {
		return bitrate;
	}

	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(Integer subtitle) {
		this.subtitle = subtitle;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getSourceFileSize() {
		return sourceFileSize;
	}

	public void setSourceFileSize(Long sourceFileSize) {
		this.sourceFileSize = sourceFileSize;
	}

	public String getSourceFileMd5() {
		return sourceFileMd5;
	}

	public void setSourceFileMd5(String sourceFileMd5) {
		this.sourceFileMd5 = sourceFileMd5;
	}

	public String getSourceFilePath() {
		return sourceFilePath;
	}

	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}

	public String getStorageNo() {
		return storageNo;
	}

	public void setStorageNo(String storageNo) {
		this.storageNo = storageNo;
	}

	public Date getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(Date onlineTime) {
		this.onlineTime = onlineTime;
	}

	public Date getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(Date offlineTime) {
		this.offlineTime = offlineTime;
	}

	public String getOnlineUser() {
		return onlineUser;
	}

	public void setOnlineUser(String onlineUser) {
		this.onlineUser = onlineUser;
	}

	public String getOfflineUser() {
		return offlineUser;
	}

	public void setOfflineUser(String offlineUser) {
		this.offlineUser = offlineUser;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getMediaStatus() {
		return mediaStatus;
	}

	public void setMediaStatus(Integer mediaStatus) {
		this.mediaStatus = mediaStatus;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getInjectionStatus() {
		return injectionStatus;
	}

	public void setInjectionStatus(String injectionStatus) {
		this.injectionStatus = injectionStatus;
	}

	public String getPlayCode() {
		return playCode;
	}

	public void setPlayCode(String playCode) {
		this.playCode = playCode;
	}

	public Integer getPlayCodeStatus() {
		return playCodeStatus;
	}

	public void setPlayCodeStatus(Integer playCodeStatus) {
		this.playCodeStatus = playCodeStatus;
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

	public String getReserved1() {
		return reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getReserved2() {
		return reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	public String getReserved3() {
		return reserved3;
	}

	public void setReserved3(String reserved3) {
		this.reserved3 = reserved3;
	}

	public String getReserved4() {
		return reserved4;
	}

	public void setReserved4(String reserved4) {
		this.reserved4 = reserved4;
	}

	public String getReserved5() {
		return reserved5;
	}

	public void setReserved5(String reserved5) {
		this.reserved5 = reserved5;
	}

}
