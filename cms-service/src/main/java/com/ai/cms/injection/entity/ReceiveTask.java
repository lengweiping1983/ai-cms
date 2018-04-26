package com.ai.cms.injection.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.ai.cms.injection.enums.ReceiveResponseStatusEnum;
import com.ai.cms.injection.enums.ReceiveTaskStatusEnum;
import com.ai.common.entity.AbstractEntity;

/**
 * 接收任务实体
 *
 */
@Entity
@Table(name = "cms_injection_receive_task")
public class ReceiveTask extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "receive_time")
	private Date receiveTime; // 接收时间

	@NotNull
	@Column(name = "platform_id")
	private Long platformId;// 分发平台

	@Column(name = "correlate_id")
	private String correlateId; // 相关性标识，用于关联指令执行请求消息和结果通知消息。对于重发的指令，该字段值保持一致，对于不同的指令，该字段在同层内具有唯一性。

	@NotNull
	private Integer priority = 1;// 优先级

	@NotNull
	private Integer status = ReceiveTaskStatusEnum.DEFAULT.getKey(); // 任务状态

	@Column(name = "download_times")
	private Integer downloadTimes = 0;// 下载文件次数

	@Column(name = "cmd_file_url")
	private String cmdFileURL; // XML指令文件的URL，此URL为标准FTP方式，样例ftp://username:password@ip:port/..../xxx.xml。

	@Column(name = "request_result")
	private Integer requestResult; // 请求消息结果：0：成功，-1：失败

	@Column(name = "request_error_description")
	private String requestErrorDescription; // 错误信息详细描述

	@Column(name = "cmd_result")
	private Integer cmdResult; // 命令执行结果：0：成功，-1：通常失败，其他结果待定义

	@Column(name = "result_file_url")
	private String resultFileURL; // 查询结果XML文件的URL，该字段仅针对查询结果通知消息出现，此URL为标准FTP方式，样例ftp://username:password@ip:port/..../xxx.xml。

	@Column(name = "response_result")
	private Integer responseResult; // 响应消息结果：0：成功，-1：失败

	@Column(name = "response_error_description")
	private String responseErrorDescription; // 错误信息详细描述

	@Column(name = "response_status")
	private Integer responseStatus = ReceiveResponseStatusEnum.DEFAULT.getKey(); // 响应状态:0=未发送、3=发送成功、4=发送失败

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "first_response_time")
	private Date firstResponseTime; // 第一次发送响应时间

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_response_time")
	private Date lastResponseTime; // 最后发送响应时间

	@Column(name = "response_times")
	private Integer responseTimes = 0;// 发送响应次数

	@Column(name = "response_total_times")
	private Integer responseTotalTimes = 0;// 发送响应总次数

	@Column(name = "request_xml_file_path")
	private String requestXmlFilePath; // 请求xml本地路径

	@Column(name = "request_xml_file_content")
	private String requestXmlFileContent; // 请求xml内容

	@Column(name = "response_xml_file_path")
	private String responseXmlFilePath; // 响应xml本地路径

	@Column(name = "response_xml_file_content")
	private String responseXmlFileContent; // 响应xml内容

	@Column(name = "reply_result")
	private Integer replyResult; // 0：成功，1：部分成功，其他：错误代码

	@Column(name = "reply_error_description")
	private String replyErrorDescription; // 错误信息详细描述

	@NotNull
	@Column(name = "download_total")
	private Long downloadTotal = 0l; // 下载总数

	@NotNull
	@Column(name = "download_success")
	private Long downloadSuccess = 0l; // 下载成功任务数

	@NotNull
	@Column(name = "download_fail")
	private Long downloadFail = 0l; // 下载失败任务数

	@Column(name = "cp_code")
	private String cpCode; // 内容提供商代码,多个使用','分割

	public ReceiveTask() {

	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Long getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}

	public String getCorrelateId() {
		return correlateId;
	}

	public void setCorrelateId(String correlateId) {
		this.correlateId = correlateId;
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

	public Integer getDownloadTimes() {
		return downloadTimes;
	}

	public void setDownloadTimes(Integer downloadTimes) {
		this.downloadTimes = downloadTimes;
	}

	public String getCmdFileURL() {
		return cmdFileURL;
	}

	public void setCmdFileURL(String cmdFileURL) {
		this.cmdFileURL = cmdFileURL;
	}

	public Integer getRequestResult() {
		return requestResult;
	}

	public void setRequestResult(Integer requestResult) {
		this.requestResult = requestResult;
	}

	public String getRequestErrorDescription() {
		return requestErrorDescription;
	}

	public void setRequestErrorDescription(String requestErrorDescription) {
		this.requestErrorDescription = requestErrorDescription;
	}

	public Integer getCmdResult() {
		return cmdResult;
	}

	public void setCmdResult(Integer cmdResult) {
		this.cmdResult = cmdResult;
	}

	public String getResultFileURL() {
		return resultFileURL;
	}

	public void setResultFileURL(String resultFileURL) {
		this.resultFileURL = resultFileURL;
	}

	public Integer getResponseResult() {
		return responseResult;
	}

	public void setResponseResult(Integer responseResult) {
		this.responseResult = responseResult;
	}

	public String getResponseErrorDescription() {
		return responseErrorDescription;
	}

	public void setResponseErrorDescription(String responseErrorDescription) {
		this.responseErrorDescription = responseErrorDescription;
	}

	public Integer getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(Integer responseStatus) {
		this.responseStatus = responseStatus;
	}

	public Date getFirstResponseTime() {
		return firstResponseTime;
	}

	public void setFirstResponseTime(Date firstResponseTime) {
		this.firstResponseTime = firstResponseTime;
	}

	public Date getLastResponseTime() {
		return lastResponseTime;
	}

	public void setLastResponseTime(Date lastResponseTime) {
		this.lastResponseTime = lastResponseTime;
	}

	public Integer getResponseTimes() {
		return responseTimes;
	}

	public void setResponseTimes(Integer responseTimes) {
		this.responseTimes = responseTimes;
	}

	public Integer getResponseTotalTimes() {
		return responseTotalTimes;
	}

	public void setResponseTotalTimes(Integer responseTotalTimes) {
		this.responseTotalTimes = responseTotalTimes;
	}

	public String getRequestXmlFilePath() {
		return requestXmlFilePath;
	}

	public void setRequestXmlFilePath(String requestXmlFilePath) {
		this.requestXmlFilePath = requestXmlFilePath;
	}

	public String getRequestXmlFileContent() {
		return requestXmlFileContent;
	}

	public void setRequestXmlFileContent(String requestXmlFileContent) {
		this.requestXmlFileContent = requestXmlFileContent;
	}

	public String getResponseXmlFilePath() {
		return responseXmlFilePath;
	}

	public void setResponseXmlFilePath(String responseXmlFilePath) {
		this.responseXmlFilePath = responseXmlFilePath;
	}

	public String getResponseXmlFileContent() {
		return responseXmlFileContent;
	}

	public void setResponseXmlFileContent(String responseXmlFileContent) {
		this.responseXmlFileContent = responseXmlFileContent;
	}

	public Integer getReplyResult() {
		return replyResult;
	}

	public void setReplyResult(Integer replyResult) {
		this.replyResult = replyResult;
	}

	public String getReplyErrorDescription() {
		return replyErrorDescription;
	}

	public void setReplyErrorDescription(String replyErrorDescription) {
		this.replyErrorDescription = replyErrorDescription;
	}

	public Long getDownloadTotal() {
		return downloadTotal;
	}

	public void setDownloadTotal(Long downloadTotal) {
		this.downloadTotal = downloadTotal;
	}

	public Long getDownloadSuccess() {
		return downloadSuccess;
	}

	public void setDownloadSuccess(Long downloadSuccess) {
		this.downloadSuccess = downloadSuccess;
	}

	public Long getDownloadFail() {
		return downloadFail;
	}

	public void setDownloadFail(Long downloadFail) {
		this.downloadFail = downloadFail;
	}

	public String getCpCode() {
		return cpCode;
	}

	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}

}
