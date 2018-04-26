package com.ai.cms.injection.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.ai.cms.injection.enums.DownloadModuleEnum;
import com.ai.cms.injection.enums.DownloadTaskStatusEnum;
import com.ai.common.entity.AbstractEntity;

@Entity
@Table(name = "cms_injection_download_task")
public class DownloadTask extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@NotNull
	private Integer module = DownloadModuleEnum.RECEIVE.getKey();// 模块

	@NotNull
	@Column(name = "pid")
	private Long pid;// 大任务Id

	@NotNull
	private String name; // 任务名称

	@Column(name = "program_id")
	private Long programId; // 节目id

	@Column(name = "media_file_id")
	private Long mediaFileId; // 媒体文件id

	@Column(name = "input_file_path")
	private String inputFilePath; // 输入文件路径

	@Column(name = "output_file_path")
	private String outputFilePath; // 输出文件路径

	@Column(name = "first_request_time")
	private Date firstRequestTime; // 第一次下载时间

	@Column(name = "last_request_time")
	private Date lastRequestTime; // 最后下载时间

	@Column(name = "request_times")
	private Integer requestTimes = 0;// 下载次数

	@Column(name = "request_total_times")
	private Integer requestTotalTimes = 0;// 下载总次数

	@Column(name = "response_code")
	private String responseCode; // 响应代码

	@Column(name = "response_msg")
	private String responseMsg; // 响应信息

	@Column(name = "response_time")
	private Date responseTime; // 外部响应时间

	private Integer priority = 1;// 优先级

	private Integer status = DownloadTaskStatusEnum.DEFAULT.getKey(); // 任务状态

	private Integer percent = 0;// 完成百分比

	@Column(name = "file_size")
	private Long fileSize;// 文件大小

	@Column(name = "file_md5")
	private String fileMd5;// 文件md5值

	@Column(name = "source_file_size")
	private Long sourceFileSize;// 原始文件大小

	@Column(name = "source_file_md5")
	private String sourceFileMd5;// 原始文件md5值

	@Column(name = "cp_code")
	private String cpCode; // 内容提供商代码,多个使用','分割

	public DownloadTask() {

	}

	public Integer getModule() {
		return module;
	}

	public void setModule(Integer module) {
		this.module = module;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public Long getMediaFileId() {
		return mediaFileId;
	}

	public void setMediaFileId(Long mediaFileId) {
		this.mediaFileId = mediaFileId;
	}

	public String getInputFilePath() {
		return inputFilePath;
	}

	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	public String getOutputFilePath() {
		return outputFilePath;
	}

	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}

	public Date getFirstRequestTime() {
		return firstRequestTime;
	}

	public void setFirstRequestTime(Date firstRequestTime) {
		this.firstRequestTime = firstRequestTime;
	}

	public Date getLastRequestTime() {
		return lastRequestTime;
	}

	public void setLastRequestTime(Date lastRequestTime) {
		this.lastRequestTime = lastRequestTime;
	}

	public Integer getRequestTimes() {
		return requestTimes;
	}

	public void setRequestTimes(Integer requestTimes) {
		this.requestTimes = requestTimes;
	}

	public Integer getRequestTotalTimes() {
		return requestTotalTimes;
	}

	public void setRequestTotalTimes(Integer requestTotalTimes) {
		this.requestTotalTimes = requestTotalTimes;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPercent() {
		return percent;
	}

	public void setPercent(Integer percent) {
		this.percent = percent;
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

	public String getCpCode() {
		return cpCode;
	}

	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}

}
