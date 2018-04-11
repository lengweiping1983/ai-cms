package com.ai.epg.subscriber.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ai.common.entity.AbstractCreateTimeEntityFormat;

/**
 * 用户信息表
 */
public class SubscriberFormat extends AbstractCreateTimeEntityFormat {
	private static final long serialVersionUID = 1L;

	private String groupCode;// 用户组代码

	private String providerCode;// 提供商代码

	private String providerName;// 提供商名称

	private String partnerUserId;// 运营商用户业务帐号（子账号）

	private String partnerUserPassword;// 运营商用户业务帐号对应的密码

	private String partnerUserToken;

	private String partnerMainAccount;// 运营商用户业务主账号（或运营商计费账号）

	private String status;// 状态（0=开户、1=激活、2=停机、3=销户（拆机）、4=暂停）

	private String userType;// 用户类型（1=普通商用用户、2=内部体验用户、3=内部测试用户）

	private String userName;// 用户名称

	private String idNumber;// 身份证号码

	private String telephone;// 固定号码

	private String mobilePhone;// 手机号码

	private String sex;// 性别

	private String address;// 住址

	private String postalCode;// 邮政编码

	private String email;// 电子邮箱

	@Column(name = "province_code")
	private String provinceCode;// 省代码

	@Column(name = "city_code")
	private String cityCode;// 市代码

	@Column(name = "county_code")
	private String countyCode;// 县代码

	@Column(name = "biz_type")
	private String bizType = "1";// 业务类型（1=IPTV、2=OTT）

	@Column(name = "sn")
	private String sn;// 终端序列号

	@Column(name = "open_time")
	private String openTime;// 用户开通时间

	@Column(name = "activate_time")
	private String activateTime;// 用户激活时间

	@Column(name = "suspend_time")
	private String suspendTime;// 用户停机时间

	@Column(name = "cancle_time")
	private String cancleTime;// 用户销户时间

	@Column(name = "os_version")
	private String osVersion;// 系统版本

	@Column(name = "app_version")
	private String appVersion;// 应用版本

	@Column(name = "app_code")
	private String appCode;// APP代码

	@Column(name = "last_login_time")
	private String lastLoginTime;// 用户最近一次登录时间

	@Column(name = "client_ip")
	private String clientIp;// 客户端IP

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getPartnerUserId() {
		return partnerUserId;
	}

	public void setPartnerUserId(String partnerUserId) {
		this.partnerUserId = partnerUserId;
	}

	public String getPartnerUserPassword() {
		return partnerUserPassword;
	}

	public void setPartnerUserPassword(String partnerUserPassword) {
		this.partnerUserPassword = partnerUserPassword;
	}

	public String getPartnerUserToken() {
		return partnerUserToken;
	}

	public void setPartnerUserToken(String partnerUserToken) {
		this.partnerUserToken = partnerUserToken;
	}

	public String getPartnerMainAccount() {
		return partnerMainAccount;
	}

	public void setPartnerMainAccount(String partnerMainAccount) {
		this.partnerMainAccount = partnerMainAccount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCountyCode() {
		return countyCode;
	}

	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getActivateTime() {
		return activateTime;
	}

	public void setActivateTime(String activateTime) {
		this.activateTime = activateTime;
	}

	public String getSuspendTime() {
		return suspendTime;
	}

	public void setSuspendTime(String suspendTime) {
		this.suspendTime = suspendTime;
	}

	public String getCancleTime() {
		return cancleTime;
	}

	public void setCancleTime(String cancleTime) {
		this.cancleTime = cancleTime;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	@Override
	public String toString() {
		return "{\"groupCode\":\"" + groupCode + "\",\"providerCode\":\"" + providerCode + "\",\"providerName\":\""
				+ providerName + "\",\"partnerUserId\":\"" + partnerUserId + "\",\"partnerUserPassword\":\""
				+ partnerUserPassword + "\",\"partnerUserToken\":\"" + partnerUserToken + "\",\"partnerMainAccount\":\""
				+ partnerMainAccount + "\",\"status\":\"" + status + "\",\"userType\":\"" + userType
				+ "\",\"userName\":\"" + userName + "\",\"idNumber\":\"" + idNumber + "\",\"telephone\":\"" + telephone
				+ "\",\"mobilePhone\":\"" + mobilePhone + "\",\"sex\":\"" + sex + "\",\"address\":\"" + address
				+ "\",\"postalCode\":\"" + postalCode + "\",\"email\":\"" + email + "\",\"provinceCode\":\""
				+ provinceCode + "\",\"cityCode\":\"" + cityCode + "\",\"countyCode\":\"" + countyCode
				+ "\",\"bizType\":\"" + bizType + "\",\"sn\":\"" + sn + "\",\"openTime\":\"" + openTime
				+ "\",\"activateTime\":\"" + activateTime + "\",\"suspendTime\":\"" + suspendTime
				+ "\",\"cancleTime\":\"" + cancleTime + "\",\"osVersion\":\"" + osVersion + "\",\"appVersion\":\""
				+ appVersion + "\",\"appCode\":\"" + appCode + "\",\"lastLoginTime\":\"" + lastLoginTime
				+ "\",\"clientIp\":\"" + clientIp + "\"} ";
	}
	
	

}
