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

	@Column(name = "cp_code")
	private String cpCode; // 内容提供商代码

	@Column(name = "dir_path")
	private String dirPath = "/";// FTP地址

	public String getCpCode() {
		return cpCode;
	}

	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}

	public String getDirPath() {
		return dirPath;
	}

	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}

}
