package com.ai.cms.live.entity;

import com.ai.common.entity.AbstractEntity;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "cms_live_import")
public class ScheduleImportUpdate extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "file_name")
	private String fileName;// 文件名称

	@Column(name = "description")
	private String description;// 备注

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "import_time")
	private Date importTime;// 导入时间

	private Integer success = 0;// 成功数

	private Integer failure = 0;// 失败数

	@Column(name = "error_message")
	private String errorMessage;// 失败描述

	@Column(name = "site_code")
	private String siteCode;// 渠道代码

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

}
