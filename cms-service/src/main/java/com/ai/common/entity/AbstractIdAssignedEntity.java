package com.ai.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 实体父类，记录创建与修改时间等信息
 *
 */
@MappedSuperclass
public abstract class AbstractIdAssignedEntity implements Serializable {
	public static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "system-assigned", strategy = "assigned")
	@GeneratedValue(generator = "system-assigned")
	protected Long id = 0l;// 主键

	@JsonIgnore
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	private Date createTime;// 创建时间

	@JsonIgnore
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	private Date updateTime;// 最后修改时间

	public AbstractIdAssignedEntity() {
	}

	@PrePersist
	public void preCreate() {
		Date currentTime = new Date();
		this.createTime = currentTime;
	}

	@PreUpdate
	public void preUpdate() {
		Date currentTime = new Date();
		if (this.createTime == null) {
			this.createTime = currentTime;
		}
		this.updateTime = currentTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
