package com.ai.cms.transcode.api.controller;

import java.io.Serializable;

public class MediaFileBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String filePath;// 文件路径

	private Long fileSize;// 文件大小

	private String fileMd5;// 文件md5值

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

}
