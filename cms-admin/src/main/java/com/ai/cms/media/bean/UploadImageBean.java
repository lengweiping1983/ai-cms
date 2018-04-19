package com.ai.cms.media.bean;

public class UploadImageBean {

	private String url;
	private String thumbnailUrl;
	private String name;
	private Long size;
	private String error;

	private String deleteType;
	private String deleteUrl;
	private Boolean deleteWithCredentials = Boolean.FALSE;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getDeleteType() {
		return deleteType;
	}

	public void setDeleteType(String deleteType) {
		this.deleteType = deleteType;
	}

	public String getDeleteUrl() {
		return deleteUrl;
	}

	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}

	public Boolean getDeleteWithCredentials() {
		return deleteWithCredentials;
	}

	public void setDeleteWithCredentials(Boolean deleteWithCredentials) {
		this.deleteWithCredentials = deleteWithCredentials;
	}

}
