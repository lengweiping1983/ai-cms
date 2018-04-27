package com.ai.cms.media.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.MediaImportTypeEnum;
import com.ai.common.enums.YesNoEnum;

@Entity
@Table(name = "cms_media_import")
public class MediaImport extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	private Integer type = MediaImportTypeEnum.SERIES_META_DATA.getKey();// 类型

	@Column(name = "create_metadata")
	private Integer createMetadata = YesNoEnum.NO.getKey(); // 是否创建元数据

	@Column(name = "description")
	private String description;// 备注

	@Column(name = "file_name")
	private String fileName;// 文件名称

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "import_time")
	private Date importTime;// 导入时间

	private Integer success = 0;// 成功数

	private Integer failure = 0;// 失败数

	@Column(name = "error_message")
	private String errorMessage;// 失败描述

	@Column(name = "site_code")
	private String siteCode;// 渠道代码

	@Column(name = "audit_status")
	private String auditStatus;// 审核状态

	@Column(name = "cp_code")
	private String cpCode; // 内容提供商代码

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCreateMetadata() {
		return createMetadata;
	}

	public void setCreateMetadata(Integer createMetadata) {
		this.createMetadata = createMetadata;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getImportTime() {
		return importTime;
	}

	public void setImportTime(Date importTime) {
		this.importTime = importTime;
	}

	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}

	public Integer getFailure() {
		return failure;
	}

	public void setFailure(Integer failure) {
		this.failure = failure;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getCpCode() {
		return cpCode;
	}

	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}

}
