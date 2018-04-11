package com.ai.cms.media.bean;

public class BatchTitleBean {

	private Long[] ids;

	private String[] titles;// 定制名称

	private Integer[] sortIndexs; // 排序值

	public Long[] getIds() {
		return ids;
	}

	public void setIds(Long[] ids) {
		this.ids = ids;
	}

	public String[] getTitles() {
		return titles;
	}

	public void setTitles(String[] titles) {
		this.titles = titles;
	}

	public Integer[] getSortIndexs() {
		return sortIndexs;
	}

	public void setSortIndexs(Integer[] sortIndexs) {
		this.sortIndexs = sortIndexs;
	}

}
