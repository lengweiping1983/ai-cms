package com.ai.cms.media.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.MediaFileTypeEnum;
import com.ai.common.enums.MediaStatusEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.SourceEnum;
import com.ai.common.enums.YesNoEnum;

@Entity
@Table(name = "epg_media_file")
public class MediaFile extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	public final static String MEDIA_FILE_CODE_PREFIX = "";

	@Column(name = "series_id")
	private Long seriesId;// 所属剧头id

	@Column(name = "series_code")
	private String seriesCode;// 所属剧头代码

	@Column(name = "program_id")
	private Long programId;// 所属节目id

	@Column(name = "program_code")
	private String programCode;// 所属节目代码

	private String code;// 媒体文件唯一标识

	private int type = MediaFileTypeEnum.DEFAULT.getKey();// 文件类型:1=正片、2=片花

	private Integer bitrate;// 码率，单位为K

	private String resolution;// 分辨率

	private String format;// 格式

	private Integer duration;// 播放时长，单位为秒

	@Column(name = "file_path")
	private String filePath;// 文件路径

	@Column(name = "file_size")
	private Long fileSize;// 文件大小

	@Column(name = "file_md5")
	private String fileMd5;// 文件md5值

	@NotNull
	@Column(name = "status")
	private int status = OnlineStatusEnum.DEFAULT.getKey();// 状态:0=未上线、1=已上线、2=已下线

	@Column(name = "cp_id")
	private Long cpId;// 内容提供商id

	@Column(name = "media_id")
	private Long mediaId;// 媒资id,一个媒资如有多个码率生成多个内容

	@NotNull
	@Column(name = "media_status")
	private int mediaStatus = MediaStatusEnum.MISS_VIDEO.getKey();// 媒资状态

	@Column(name = "media_spec")
	private String mediaSpec;

	@NotNull
	private int source = SourceEnum.MANUAL.getKey();// 方式:0=手工录入、1=批量导入、2=自动采集、3=转码、4=中央CMS、5=SMP

	@Column(name = "platform_id")
	private Long platformId;// 注入平台
	
	@NotNull
	@Column(name = "injection_status")
	private int injectionStatus = 1;// 注入状态

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "injection_time")
	private Date injectionTime;// 注入时间

	@Column(name = "partner_item_code")
	private String partnerItemCode;// 运营商侧Code
	
	private String caption;// 是否有字幕:0=否、1=是

	@Column(name = "cloud_id")
	private String cloudId;// 云端id

	@Column(name = "cloud_code")
	private String cloudCode;// 云端代码

	@Column(name = "template_id")
	private Long templateId;// 转码模板id

	@Column(name = "cdn_file_code")
	private String cdnFileCode;// 运营商侧节目代码(默认)

	@Column(name = "cdn_file_path")
	private String cdnFilePath;// 运营商侧节目路径

	@Column(name = "huawei_file_code")
	private String huaweiFileCode;// 华为平台节目代码

	@Column(name = "huawei_status")
	private Integer huaweiStatus = YesNoEnum.NO.getKey();// 华为平台节目状态:0=否、1=是

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "huawei_injection_time")
	private Date huaweiInjectionTime;// 华为平台注入时间

	@Column(name = "zte_file_code")
	private String zteFileCode;// 中兴平台节目代码

	@Column(name = "zte_status")
	private Integer zteStatus = YesNoEnum.NO.getKey();// 中兴平台节目状态:0=否、1=是

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "zte_injection_time")
	private Date zteInjectionTime;// 中兴平台注入时间

	@Column(name = "ut_file_code")
	private String utFileCode;// UT平台节目代码

	@Column(name = "ut_status")
	private Integer utStatus = YesNoEnum.NO.getKey();// UT平台节目状态:0=否、1=是

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ut_injection_time")
	private Date utInjectionTime;// UT平台注入时间

	@Column(name = "fiberhome_file_code")
	private String fiberhomeFileCode;// 烽火平台节目代码

	@Column(name = "fiberhome_status")
	private Integer fiberhomeStatus = YesNoEnum.NO.getKey();// 烽火平台节目状态:0=否、1=是

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fiberhome_injection_time")
	private Date fiberhomeInjectionTime;// 烽火平台注入时间
	
    @Column(name = "storage_no")
    private String storageNo; // 存储编号
	
	private String reserved1;
	private String reserved2;
	private String reserved3;
	private String reserved4;
	private String reserved5;
	
	@Column(name = "source_file_size")
	private Long sourceFileSize;// 原始文件大小
	@Column(name = "source_file_md5")
	private String sourceFileMd5;// 原始文件md5值
	@Column(name = "source_file_path")
	private String sourceFilePath;// 原始文件路径
	
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

	public Long getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(Long seriesId) {
		this.seriesId = seriesId;
	}

	public String getSeriesCode() {
		return seriesCode;
	}

	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public String getProgramCode() {
		return programCode;
	}

	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	public int getMediaStatus() {
		return mediaStatus;
	}

	public void setMediaStatus(int mediaStatus) {
		this.mediaStatus = mediaStatus;
	}

	public String getMediaSpec() {
		return mediaSpec;
	}

	public void setMediaSpec(String mediaSpec) {
		this.mediaSpec = mediaSpec;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}
	
	public Long getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}

	public int getInjectionStatus() {
		return injectionStatus;
	}

	public void setInjectionStatus(int injectionStatus) {
		this.injectionStatus = injectionStatus;
	}

	public Date getInjectionTime() {
		return injectionTime;
	}

	public void setInjectionTime(Date injectionTime) {
		this.injectionTime = injectionTime;
	}

	public String getPartnerItemCode() {
		return partnerItemCode;
	}

	public void setPartnerItemCode(String partnerItemCode) {
		this.partnerItemCode = partnerItemCode;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
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

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getCdnFileCode() {
		return cdnFileCode;
	}

	public void setCdnFileCode(String cdnFileCode) {
		this.cdnFileCode = cdnFileCode;
	}

	public String getCdnFilePath() {
		return cdnFilePath;
	}

	public void setCdnFilePath(String cdnFilePath) {
		this.cdnFilePath = cdnFilePath;
	}

	public String getHuaweiFileCode() {
		return huaweiFileCode;
	}

	public void setHuaweiFileCode(String huaweiFileCode) {
		this.huaweiFileCode = huaweiFileCode;
	}

	public Integer getHuaweiStatus() {
		return huaweiStatus;
	}

	public void setHuaweiStatus(Integer huaweiStatus) {
		this.huaweiStatus = huaweiStatus;
	}

	public Date getHuaweiInjectionTime() {
		return huaweiInjectionTime;
	}

	public void setHuaweiInjectionTime(Date huaweiInjectionTime) {
		this.huaweiInjectionTime = huaweiInjectionTime;
	}

	public String getZteFileCode() {
		return zteFileCode;
	}

	public void setZteFileCode(String zteFileCode) {
		this.zteFileCode = zteFileCode;
	}

	public Integer getZteStatus() {
		return zteStatus;
	}

	public void setZteStatus(Integer zteStatus) {
		this.zteStatus = zteStatus;
	}

	public Date getZteInjectionTime() {
		return zteInjectionTime;
	}

	public void setZteInjectionTime(Date zteInjectionTime) {
		this.zteInjectionTime = zteInjectionTime;
	}

	public String getUtFileCode() {
		return utFileCode;
	}

	public void setUtFileCode(String utFileCode) {
		this.utFileCode = utFileCode;
	}

	public Integer getUtStatus() {
		return utStatus;
	}

	public void setUtStatus(Integer utStatus) {
		this.utStatus = utStatus;
	}

	public Date getUtInjectionTime() {
		return utInjectionTime;
	}

	public void setUtInjectionTime(Date utInjectionTime) {
		this.utInjectionTime = utInjectionTime;
	}

	public String getFiberhomeFileCode() {
		return fiberhomeFileCode;
	}

	public void setFiberhomeFileCode(String fiberhomeFileCode) {
		this.fiberhomeFileCode = fiberhomeFileCode;
	}

	public Integer getFiberhomeStatus() {
		return fiberhomeStatus;
	}

	public void setFiberhomeStatus(Integer fiberhomeStatus) {
		this.fiberhomeStatus = fiberhomeStatus;
	}

	public Date getFiberhomeInjectionTime() {
		return fiberhomeInjectionTime;
	}

	public void setFiberhomeInjectionTime(Date fiberhomeInjectionTime) {
		this.fiberhomeInjectionTime = fiberhomeInjectionTime;
	}

	public String getStorageNo() {
		return storageNo;
	}

	public void setStorageNo(String storageNo) {
		this.storageNo = storageNo;
	}

}
