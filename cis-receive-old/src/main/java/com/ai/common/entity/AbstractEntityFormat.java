package com.ai.common.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.ai.common.entity.IdEntity;

/**
 * 实体父类，记录创建与修改时间等信息
 *
 */
@MappedSuperclass
public abstract class AbstractEntityFormat extends IdEntity {
	public static final long serialVersionUID = 1L;

	@Column(name = "create_time")
	private String createTime;// 创建时间

	@Column(name = "update_time")
	private String updateTime;// 最后修改时间

	public AbstractEntityFormat() {
	}

	@PrePersist
	public void preCreate() {
		String currentTime = new String();
		this.createTime = currentTime;
	}

	@PreUpdate
	public void preUpdate() {
		Date currentTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.updateTime = sdf.format(currentTime);
		if (this.createTime == null) {
			this.createTime = this.updateTime;
		}
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
