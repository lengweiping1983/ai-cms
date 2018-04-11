package com.ai.cms.media.bean;

public class BatchAddBean {
	private Integer[] itemType;// 元素类型
	private Long[] itemId;
	private String[] title;// 定制名称
	private Integer[] sortIndex; // 排序值
	private Integer[] status; // 状态
	private Integer[] itemStatus;// 元素状态

	public Integer[] getItemType() {
		return itemType;
	}

	public void setItemType(Integer[] itemType) {
		this.itemType = itemType;
	}

	public Long[] getItemId() {
		return itemId;
	}

	public void setItemId(Long[] itemId) {
		this.itemId = itemId;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public Integer[] getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(Integer[] sortIndex) {
		this.sortIndex = sortIndex;
	}

	public Integer[] getStatus() {
		return status;
	}

	public void setStatus(Integer[] status) {
		this.status = status;
	}

	public Integer[] getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(Integer[] itemStatus) {
		this.itemStatus = itemStatus;
	}

}
