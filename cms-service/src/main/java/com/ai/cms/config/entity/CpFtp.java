package com.ai.cms.config.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ai.common.entity.AbstractEntity;

/**
 * 提供商FTP帐号实体
 */
@Entity
@Table(name = "cms_cp_ftp")
public class CpFtp extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "cp_id")
	private Long cpId;// 提供商Id

	private String ip;// ip

	private Integer port = 21;// 端口

	private String username;// 用户名

	private String password;// 密码

	@Column(name = "root_path")
	private String rootPath = "/";// FTP根目录

	@Column(name = "default_access_path")
	private String defaultAccessPath = "/";// 默认访问目录

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getDefaultAccessPath() {
		return defaultAccessPath;
	}

	public void setDefaultAccessPath(String defaultAccessPath) {
		this.defaultAccessPath = defaultAccessPath;
	}

}
