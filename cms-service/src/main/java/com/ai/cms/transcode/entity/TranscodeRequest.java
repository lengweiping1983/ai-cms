package com.ai.cms.transcode.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.ai.cms.transcode.enums.GenProgramNameRuleEnum;
import com.ai.cms.transcode.enums.GenTaskModeStatusEnum;
import com.ai.cms.transcode.enums.TranscodeRequestStatusEnum;
import com.ai.cms.transcode.enums.TranscodeRequestTypeEnum;
import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.YesNoEnum;

@Entity
@Table(name = "cms_transcode_request")
public class TranscodeRequest extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	public static final String METADATA = "type,name,contentType,mediaId,mediaName,mediaFilename,genProgramNameRule,tag,internalTag,episodeTotal,cpCode"
			+ ",templateId,genTask,priority,comment,needSnapshot,timePoints,originFileDeal,coverFile";

	@NotNull
	private Integer type = TranscodeRequestTypeEnum.MOVIE.getKey();// 转码工单类型:1=单集类型工单、2=多集类型工单、3=批量单集工单

	@NotNull
	private String name; // 工单名称

	@Column(name = "content_type")
	private Integer contentType; // 内容类型

	@NotNull
	@Column(name = "new_media")
	private Integer newMedia = YesNoEnum.NO.getKey();// 是否生成新的媒资

	@Column(name = "media_id")
	private Long mediaId; // 媒资id

	@Column(name = "media_name")
	private String mediaName; // 媒资名称

	@Column(name = "media_filename")
	private String mediaFilename; // 媒体文件名标识,用作转码后的媒体文件名

	@NotNull
	@Column(name = "gen_program_name_rule")
	private Integer genProgramNameRule = GenProgramNameRuleEnum.EPISODE
			.getKey();// 生成节目名称规则

	private String tag;// tag，之间使用','分割

	@Column(name = "internal_tag")
	private String internalTag;// 内部标签，内部使用

	@Column(name = "episode_total")
	private Integer episodeTotal;// 总集数

	@Column(name = "cp_code")
	private String cpCode; // 内容提供商代码,多个使用','分割

	@Column(name = "template_id")
	private String templateId; // 码率模板Id,多个使用','分割

	@NotNull
	@Column(name = "gen_task")
	private Integer genTask = GenTaskModeStatusEnum.DEFAULT.getKey(); // 转码模式:1=正常转码、2=已转好码[仅生成媒资元数据]

	@NotNull
	private Integer priority = 5;// 优先级,0-9,9最高

	private String comment;// 备注

	@NotNull
	@Column(name = "need_snapshot")
	private Integer needSnapshot = YesNoEnum.NO.getKey(); // 是否需要截图:0=不需要、1=需要,截图的时间/路径设置,另外定义

	@Column(name = "time_points")
	private String timePoints; // 抽帧偏移时间,单位秒 多个偏移时间之间用,隔开

	@NotNull
	@Column(name = "origin_file_deal")
	private Integer originFileDeal = YesNoEnum.NO.getKey(); // 原始文件处理方式:0=不处理、1=删除、2=归档

	@NotNull
	@Column(name = "cover_file")
	private Integer coverFile = YesNoEnum.NO.getKey(); // 覆盖之前的文件

	@NotNull
	private Integer status = TranscodeRequestStatusEnum.EDIT.getKey(); // 状态

	@NotNull
	@Column(name = "task_total")
	private long taskTotal = 0l; // 总任务数

	@NotNull
	@Column(name = "task_success")
	private long taskSuccess = 0l; // 成功任务数

	@NotNull
	@Column(name = "task_fail")
	private long taskFail = 0l; // 失败任务数

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "request_id")
	@OrderBy(value = "episodeIndex asc")
	@Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<TranscodeRequestFile> fileList = new ArrayList<TranscodeRequestFile>();

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

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	public Integer getNewMedia() {
		return newMedia;
	}

	public void setNewMedia(Integer newMedia) {
		this.newMedia = newMedia;
	}

	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getMediaFilename() {
		return mediaFilename;
	}

	public void setMediaFilename(String mediaFilename) {
		this.mediaFilename = mediaFilename;
	}

	public Integer getGenProgramNameRule() {
		return genProgramNameRule;
	}

	public void setGenProgramNameRule(Integer genProgramNameRule) {
		this.genProgramNameRule = genProgramNameRule;
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

	public Integer getEpisodeTotal() {
		return episodeTotal;
	}

	public void setEpisodeTotal(Integer episodeTotal) {
		this.episodeTotal = episodeTotal;
	}

	public String getCpCode() {
		return cpCode;
	}

	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Integer getGenTask() {
		return genTask;
	}

	public void setGenTask(Integer genTask) {
		this.genTask = genTask;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getNeedSnapshot() {
		return needSnapshot;
	}

	public void setNeedSnapshot(Integer needSnapshot) {
		this.needSnapshot = needSnapshot;
	}

	public String getTimePoints() {
		return timePoints;
	}

	public void setTimePoints(String timePoints) {
		this.timePoints = timePoints;
	}

	public Integer getOriginFileDeal() {
		return originFileDeal;
	}

	public void setOriginFileDeal(Integer originFileDeal) {
		this.originFileDeal = originFileDeal;
	}

	public Integer getCoverFile() {
		return coverFile;
	}

	public void setCoverFile(Integer coverFile) {
		this.coverFile = coverFile;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public long getTaskTotal() {
		return taskTotal;
	}

	public void setTaskTotal(long taskTotal) {
		this.taskTotal = taskTotal;
	}

	public long getTaskSuccess() {
		return taskSuccess;
	}

	public void setTaskSuccess(long taskSuccess) {
		this.taskSuccess = taskSuccess;
	}

	public long getTaskFail() {
		return taskFail;
	}

	public void setTaskFail(long taskFail) {
		this.taskFail = taskFail;
	}

	public List<TranscodeRequestFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<TranscodeRequestFile> fileList) {
		this.fileList = fileList;
	}

}
