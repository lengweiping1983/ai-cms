package com.ai.sys.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.YesNoEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 菜单实体
 *
 */
@Entity
@Table(name = "sys_menu")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Menu extends AbstractEntity implements Comparable<Menu> {
	public static final long serialVersionUID = 1L;

	public static final int TYPE_MENU = 1;
	public static final int TYPE_PERMISSION = 2;

	@Column(name = "type")
	private Integer type = TYPE_MENU; // 类型（1=菜单、2=权限）

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", insertable = false, updatable = false)
	private Menu parent; // 父级菜单

	@Column(name = "parent_id")
	private Long parentId;// 父级菜单Id

	@NotNull
	@Size(min = 1, max = 64)
	private String name; // 名称

	private String href; // 链接

	private String icon; // 图标

	private Integer sort = 999; // 排序值

	@Column(name = "is_show")
	private Integer isShow = YesNoEnum.YES.getKey(); // 是否显示

	private String permission; // 权限标识

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sys_role_rel_menu", joinColumns = { @JoinColumn(name = "menu_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	@Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<Role> roleList = new ArrayList<Role>(); // 拥有角色列表

	@Transient
	private List<Menu> childList = new ArrayList<Menu>();// 拥有子菜单列表

	@Transient
	private String requestUrl; // 请求URL

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public List<Menu> getChildList() {
		return childList;
	}

	public void setChildList(List<Menu> childList) {
		this.childList = childList;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	@Override
	public int compareTo(Menu o) {
		return this.getSort().compareTo(o.getSort());
	}

	@Override
	public String toString() {
		return "Menu{" + "id=" + id + ", permission='" + permission + '\''
				+ ", type=" + type + ", parent=" + parent + ", parentId="
				+ parentId + ", name='" + name + '\'' + ", href='" + href
				+ '\'' + ", icon='" + icon + '\'' + ", sort=" + sort + '}';
	}
}
