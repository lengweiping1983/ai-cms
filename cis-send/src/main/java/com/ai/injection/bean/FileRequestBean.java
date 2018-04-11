package com.ai.injection.bean;

import java.io.Serializable;

public class FileRequestBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String sourceUrl;
	private String md5;
	private String playUrl;

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

}
