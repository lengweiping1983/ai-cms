package com.ai.cms.transcode.entity;

import com.ai.common.entity.IdEntity;
import com.ai.common.excel.annotation.ExcelField;

public class TranscodeRequestImportLog extends IdEntity {
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT = 1;

	@ExcelField(title = "工单类型", align = 2, groups = { DEFAULT }, sort = 10)
	private String type;// 工单类型

	@ExcelField(title = "工单名称", align = 2, groups = { DEFAULT }, sort = 20)
	private String name; // 工单名称

	@ExcelField(title = "媒资名称", align = 2, groups = { DEFAULT }, sort = 30)
	private String mediaName; // 媒资名称

	@ExcelField(title = "内容类型", align = 2, groups = { DEFAULT }, sort = 40)
	private String contentType; // 内容类型

	@ExcelField(title = "TAG", align = 2, groups = { DEFAULT }, sort = 50)
	private String tag;// tag，之间使用','分割

	@ExcelField(title = "internalTag", align = 2, groups = { DEFAULT }, sort = 51)
	private String internalTag;// 内部标签，内部使用

	@ExcelField(title = "提供商", align = 2, groups = { DEFAULT }, sort = 60)
	private String cpName;

	@ExcelField(title = "码率模板", align = 2, groups = { DEFAULT }, sort = 70)
	private String templateTitle; // 码率模板Id,多个使用','分割

	@ExcelField(title = "状态", align = 2, groups = { DEFAULT }, sort = 80)
	private String status; // 请求状态

	@ExcelField(title = "总集数", align = 2, groups = { DEFAULT }, sort = 90)
	private Integer episodeTotal;// 总集数

	@ExcelField(title = "优先级", align = 2, groups = { DEFAULT }, sort = 100)
	private Integer priority = 5;// 优先级,0-9,9最高

	@ExcelField(title = "总任务数", align = 2, groups = { DEFAULT }, sort = 110)
	private Long taskTotal = 0l; // 总任务数

	@ExcelField(title = "成功任务数", align = 2, groups = { DEFAULT }, sort = 120)
	private Long taskSuccess = 0l; // 成功任务数

	@ExcelField(title = " 失败任务数", align = 2, groups = { DEFAULT }, sort = 130)
	private Long taskFail = 0l; // 失败任务数

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getInternalTag() {
		return internalTag;
	}

	public void setInternalTag(String internalTag) {
		this.internalTag = internalTag;
	}

	public String getCpName() {
		return cpName;
	}

	public void setCpName(String cpName) {
		this.cpName = cpName;
	}

	public String getTemplateTitle() {
		return templateTitle;
	}

	public void setTemplateTitle(String templateTitle) {
		this.templateTitle = templateTitle;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getEpisodeTotal() {
		return episodeTotal;
	}

	public void setEpisodeTotal(Integer episodeTotal) {
		this.episodeTotal = episodeTotal;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Long getTaskTotal() {
		return taskTotal;
	}

	public void setTaskTotal(Long taskTotal) {
		this.taskTotal = taskTotal;
	}

	public Long getTaskSuccess() {
		return taskSuccess;
	}

	public void setTaskSuccess(Long taskSuccess) {
		this.taskSuccess = taskSuccess;
	}

	public Long getTaskFail() {
		return taskFail;
	}

	public void setTaskFail(Long taskFail) {
		this.taskFail = taskFail;
	}

}
