package com.ai.common.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import com.ai.common.entity.IdEntity;

/**
 * 实体父类，记录创建时间等信息
 *
 */
@MappedSuperclass
public abstract class AbstractCreateTimeEntityFormat extends IdEntity {
	public static final long serialVersionUID = 1L;

	@Column(name = "create_time")
	private String createTime;// 创建时间

	public AbstractCreateTimeEntityFormat() {
	}

	@PrePersist
	public void preCreate() {
		Date currentTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.createTime = sdf.format(currentTime);
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
