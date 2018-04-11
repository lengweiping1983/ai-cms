package com.ai.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ai.common.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 字典项实体
 *
 */
@Entity
@Table(name = "sys_dic_item")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DicItem extends AbstractEntity implements Comparable<DicItem> {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 1, max = 255)
    private String code;// 项代码

    @NotNull
    @Size(min = 1, max = 255)
    private String name;// 项名称

    @NotNull
    @Size(min = 1, max = 255)
    private String value;// 项值

    @NotNull
    private Integer sort = 999;// 排序值

    @Column(name = "dic_id")
    private Long dicId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getDicId() {
        return dicId;
    }

    public void setDicId(Long dicId) {
        this.dicId = dicId;
    }

    @Override
    public String toString() {
        return "DicItem{" + "id=" + id + ", name='" + name + '\'' + ", value='" + value + '\'' + ", sort=" + sort + ", dicId=" + dicId + '}';
    }

    @Override
    public int compareTo(DicItem o) {
        return this.getSort().compareTo(o.getSort());
    }
}
