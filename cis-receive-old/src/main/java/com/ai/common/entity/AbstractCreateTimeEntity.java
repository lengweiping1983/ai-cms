package com.ai.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ai.common.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 实体父类，记录创建时间等信息
 *
 */
@MappedSuperclass
public abstract class AbstractCreateTimeEntity extends IdEntity {
    public static final long serialVersionUID = 1L;

    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;// 创建时间

    public AbstractCreateTimeEntity() {
    }

    @PrePersist
    public void preCreate() {
        Date currentTime = new Date();
        this.createTime = currentTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
