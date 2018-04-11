package com.ai.sys.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.ai.common.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 字典实体
 */
@Entity
@Table(name = "sys_dic")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Dic extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 1, max = 255)
    private String code;

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @Size(min = 0, max = 255)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "dic_id")
    @OrderBy(value = "sort asc")
    @Fetch(FetchMode.SUBSELECT)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<DicItem> dicItemList = new ArrayList<DicItem>();

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

    public List<DicItem> getDicItemList() {
        return dicItemList;
    }

    public void setDicItemList(List<DicItem> dicItemList) {
        this.dicItemList = dicItemList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Dic{" + "id=" + id + ", description='" + description + '\'' + ", code='" + code + '\'' + ", name='" + name + '\'' + '}';
    }
}
