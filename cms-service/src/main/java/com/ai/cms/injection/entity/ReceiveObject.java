package com.ai.cms.injection.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.ai.cms.injection.enums.DownloadModuleEnum;
import com.ai.cms.injection.enums.InjectionStatusEnum;
import com.ai.common.entity.AbstractEntity;

/**
 * 接收对象实体
 *
 */
@Entity
@Table(name = "cms_injection_receive_object")
public class ReceiveObject extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@NotNull
	private Integer module = DownloadModuleEnum.RECEIVE.getKey();// 模块

	@NotNull
	@Column(name = "pid")
	private Long pid;// 大任务Id

	@Column(name = "receive_code")
	private String receiveCode;// 接收到的代码

	@NotNull
	@Column(name = "item_type")
	private Integer itemType;// 元素类型

	@NotNull
	@Column(name = "item_id")
	private Long itemId;// 元素id

	@NotNull
	@Column(name = "status")
	private Integer status = InjectionStatusEnum.DEFAULT.getKey();// 状态

	public Integer getModule() {
		return module;
	}

	public void setModule(Integer module) {
		this.module = module;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
