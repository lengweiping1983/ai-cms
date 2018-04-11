package com.ai.cms.media.bean;

public class BatchInjectionBean extends BatchBean {

	private Long[] platformId;

	private String[] templateId;

	private Integer[] priority;

	public Long[] getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Long[] platformId) {
		this.platformId = platformId;
	}

	public String[] getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String[] templateId) {
		this.templateId = templateId;
	}

	public Integer[] getPriority() {
		return priority;
	}

	public void setPriority(Integer[] priority) {
		this.priority = priority;
	}

}
