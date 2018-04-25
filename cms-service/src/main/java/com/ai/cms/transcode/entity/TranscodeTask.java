package com.ai.cms.transcode.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.ai.cms.transcode.enums.TranscodeTaskStatusEnum;
import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.YesNoEnum;

@Entity
@Table(name = "cms_transcode_task")
public class TranscodeTask extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@NotNull
	private Integer type; // 任务类型:1=预处理、2=转码、3=抽帧、4=后处理

	@NotNull
	private String name; // 任务名称

	@NotNull
	@Column(name = "request_id")
	private Long requestId;// 转码工单id

	@NotNull
	@Column(name = "request_file_id")
	private Long requestFileId;// 转码工单文件id

	@NotNull
	@Column(name = "program_id")
	private Long programId; // 节目id

	@NotNull
	@Column(name = "media_file_id")
	private Long mediaFileId; // 媒体内容id

	@NotNull
	@Column(name = "template_id")
	private Long templateId; // 码率模板id

	@Column(name = "profile")
	private String profile; // 对外转码的profile

	@Column(name = "time_points")
	private String timePoints; // 抽帧偏移时间,单位秒 多个偏移时间之间用,隔开

	@NotNull
	@Column(name = "input_file_path")
	private String inputFilePath; // 输入文件路径

	@Column(name = "input_subtitle")
	private String inputSubtitleFilePath; // 字幕文件路径

	@NotNull
	@Column(name = "output_file_path")
	private String outputFilePath; // 输出文件路径

	@Column(name = "first_request_time")
	private Date firstRequestTime; // 第一次发送指令时间

	@Column(name = "last_request_time")
	private Date lastRequestTime; // 最后发送指令时间

	@Column(name = "request_times")
	private Integer requestTimes = 0;// 发送指令次数

	@Column(name = "request_total_times")
	private Integer requestTotalTimes = 0;// 发送指令总次数

	@Column(name = "cloud_task_id")
	private String cloudTaskId; // 云端的TaskId

	@Column(name = "response_code")
	private String responseCode; // 响应代码

	@Column(name = "response_msg")
	private String responseMsg; // 响应信息

	@Column(name = "response_time")
	private Date responseTime; // 外部响应时间

	@Column(name = "pre_task_id")
	private Long preTaskId; // 前置任务id,空无前置任务

	@Column(name = "pre_task_status")
	private Integer preTaskStatus; // 前置任务是否完成:0=未完成、1=已完成

	@NotNull
	private Integer priority = 5;// 优先级,0-9,9最高

	@NotNull
	private Integer status = TranscodeTaskStatusEnum.WAIT.getKey(); // 任务状态:1=等待处理、2=指令已发送、3=处理成功、4=处理失败、5=处理超时

	@NotNull
	@Column(name = "is_callback")
	private Integer isCallback = YesNoEnum.NO.getKey();// 0=未回写，1=已回写

	@Column(name = "cp_id")
	private String cpId; // 内容提供商id,多个使用','分割

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Long getRequestFileId() {
		return requestFileId;
	}

	public void setRequestFileId(Long requestFileId) {
		this.requestFileId = requestFileId;
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

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getTimePoints() {
		return timePoints;
	}

	public void setTimePoints(String timePoints) {
		this.timePoints = timePoints;
	}

	public String getInputFilePath() {
		return inputFilePath;
	}

	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	public String getInputSubtitleFilePath() {
		return inputSubtitleFilePath;
	}

	public void setInputSubtitleFilePath(String inputSubtitleFilePath) {
		this.inputSubtitleFilePath = inputSubtitleFilePath;
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

	public String getCloudTaskId() {
		return cloudTaskId;
	}

	public void setCloudTaskId(String cloudTaskId) {
		this.cloudTaskId = cloudTaskId;
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

	public Long getPreTaskId() {
		return preTaskId;
	}

	public void setPreTaskId(Long preTaskId) {
		this.preTaskId = preTaskId;
	}

	public Integer getPreTaskStatus() {
		return preTaskStatus;
	}

	public void setPreTaskStatus(Integer preTaskStatus) {
		this.preTaskStatus = preTaskStatus;
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

	public Integer getIsCallback() {
		return isCallback;
	}

	public void setIsCallback(Integer isCallback) {
		this.isCallback = isCallback;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

}
