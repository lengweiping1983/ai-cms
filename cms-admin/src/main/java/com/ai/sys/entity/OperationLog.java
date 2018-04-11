package com.ai.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ai.common.entity.AbstractEntity;

@Entity
@Table(name = "sys_operation_log")
public class OperationLog extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "user_name", nullable = false)
	private String userName;

	private String ip;

	private String uri;

	private String module;

	@Column(name = "sub_module")
	private String subModule;

	private String action;

	@Column(name = "action_result")
	private Integer actionResult; // 操作结果 0：成功，-1：失败

	private String message;

	@Column(name = "object_id")
	private String objectId;// 对象ID

	@Column(name = "object_name")
	private String objectName;// 对象名称

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getSubModule() {
		return subModule;
	}

	public void setSubModule(String subModule) {
		this.subModule = subModule;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Integer getActionResult() {
		return actionResult;
	}

	public void setActionResult(Integer actionResult) {
		this.actionResult = actionResult;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	@Override
	public String toString() {
		return "OperationLog [userId=" + userId + ", userName=" + userName
				+ ", ip=" + ip + ", uri=" + uri + ", module=" + module
				+ ", subModule=" + subModule + ", action=" + action
				+ ", actionResult=" + actionResult + ", message=" + message
				+ ", objectId=" + objectId + ", objectName=" + objectName + "]";
	}

}
