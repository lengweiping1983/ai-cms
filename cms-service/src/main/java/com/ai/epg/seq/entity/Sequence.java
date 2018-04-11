package com.ai.epg.seq.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ai.common.entity.IdEntity;

/**
 * 序列号实体
 *
 */
@Entity
@Table(name = "sequence")
public class Sequence extends IdEntity {
    private static final long serialVersionUID = 1L;

    private String name; // 名称

    private Integer currentValue;// 当前值

    private Integer increment;// 增长值

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }

    public Integer getIncrement() {
        return increment;
    }

    public void setIncrement(Integer increment) {
        this.increment = increment;
    }

}
