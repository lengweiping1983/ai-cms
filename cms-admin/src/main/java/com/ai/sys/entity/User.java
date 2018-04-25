package com.ai.sys.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.ai.common.entity.AbstractEntity;
import com.ai.sys.enums.UserTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 用户实体
 *
 */
@Entity
@Table(name = "sys_user")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class User extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(min = 1, max = 64)
	@Column(name = "login_name", unique = true, nullable = false)
	private String loginName;// 帐号

	@Column(name = "password", nullable = false)
	private String password;// 密码

	@NotNull
	@Size(min = 1, max = 64)
	@Column(name = "name", nullable = false)
	private String name; // 姓名

	@Size(min = 0, max = 64)
	@Column(name = "no")
	private String no; // 工号

	@Size(min = 0, max = 64)
	@Column(name = "email")
	private String email; // 邮箱

	@Size(min = 0, max = 64)
	@Column(name = "phone")
	private String phone; // 固定电话

	@Size(min = 0, max = 64)
	@Column(name = "mobile")
	private String mobile; // 移动电话

	@Column(name = "status")
	private Integer status;// 状态（0=停用、1=启用）

	@Column(name = "can_be_delete")
	private boolean canBeDelete = true;// 能否删除

	@Column(name = "login_num")
	private Integer loginNum = 0; // 登录次数

	@Column(name = "login_ip")
	private String loginIp; // 最后登录IP

	@Column(name = "login_time")
	private Date loginTime; // 最后登录时间

	@Column(name = "last_login_ip")
	private String lastLoginIp; // 上次登录IP

	@Column(name = "last_login_time")
	private Date lastLoginTime; // 上次登录时间

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sys_user_rel_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	@Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<Role> roleList = new ArrayList<Role>(); // 拥有角色列表

	private Integer type = UserTypeEnum.SYSTEM.getKey();

	@Column(name = "cp_id")
	private String cpId; // 内容提供商id,多个使用','分割

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public boolean isCanBeDelete() {
		return canBeDelete;
	}

	public void setCanBeDelete(boolean canBeDelete) {
		this.canBeDelete = canBeDelete;
	}

	public Integer getLoginNum() {
		return loginNum;
	}

	public void setLoginNum(Integer loginNum) {
		this.loginNum = loginNum;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	@Transient
	public boolean isAdmin() {
		return id != null && id == 1;
	}

	@Override
	public String toString() {
		return "User [loginName=" + loginName + ", password=" + password
				+ ", name=" + name + ", no=" + no + ", email=" + email
				+ ", phone=" + phone + ", mobile=" + mobile + ", status="
				+ status + ", canBeDelete=" + canBeDelete + ", loginNum="
				+ loginNum + ", loginIp=" + loginIp + ", loginTime="
				+ loginTime + ", lastLoginIp=" + lastLoginIp
				+ ", lastLoginTime=" + lastLoginTime + ", roleList=" + roleList
				+ ", type=" + type + ", cpId=" + cpId + "]";
	}

}
