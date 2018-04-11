package com.ai.injection.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WriteBackRequestBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String mediaId;
	private Integer platFormId;
	private Integer taskType;

	private List<FileRequestBean> srcFiles = new ArrayList<FileRequestBean>();

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public Integer getPlatFormId() {
		return platFormId;
	}

	public void setPlatFormId(Integer platFormId) {
		this.platFormId = platFormId;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public List<FileRequestBean> getSrcFiles() {
		return srcFiles;
	}

	public void setSrcFiles(List<FileRequestBean> srcFiles) {
		this.srcFiles = srcFiles;
	}

}
