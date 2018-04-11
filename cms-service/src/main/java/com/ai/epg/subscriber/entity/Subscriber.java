package com.ai.epg.subscriber.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ai.common.entity.AbstractCreateTimeEntity;

/**
 * 用户信息表
 */
@Entity
@Table(name = "user_subscriber")
public class Subscriber extends AbstractCreateTimeEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "group_code")
	private String groupCode;// 用户分组代码

	@Column(name = "from_entrance")
	private String fromEntrance;// 从那个入口进来
	
	@Column(name = "provider_id")
	private String providerId;// 提供商Id
	
	@Column(name = "provider_code")
	private String providerCode;// 提供商代码

	@Column(name = "provider_name")
	private String providerName;// 提供商名称

	@Column(name = "partner_group_code")
	private String partnerGroupCode;// 运营商用户分组

	@Column(name = "partner_user_id")
	private String partnerUserId;// 运营商用户业务帐号（子账号）

	@Column(name = "partner_user_password")
	private String partnerUserPassword;// 运营商用户业务帐号对应的密码

	@Column(name = "partner_user_token")
	private String partnerUserToken;

	@Column(name = "partner_main_account")
	private String partnerMainAccount;// 运营商用户业务主账号（或运营商计费账号）

	@Column(name = "status")
	private Integer status;// 状态（0=开户、1=激活、2=停机、3=销户（拆机）、4=暂停）

	@Column(name = "user_type")
	private Integer userType;// 用户类型（1=普通商用用户、2=内部体验用户、3=内部测试用户）

	@Column(name = "user_name")
	private String userName;// 用户名称

	@Column(name = "id_number")
	private String idNumber;// 身份证号码

	@Column(name = "telephone")
	private String telephone;// 固定号码

	@Column(name = "mobile_phone")
	private String mobilePhone;// 手机号码

	@Column(name = "sex")
	private String sex;// 性别

	@Column(name = "address")
	private String address;// 住址

	@Column(name = "postal_code")
	private String postalCode;// 邮政编码

	@Column(name = "email")
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
	
	@Column(name = "mac")
	private String mac;// 终端MAC地址

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "open_time")
	private Date openTime;// 用户开通时间

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "activate_time")
	private Date activateTime;// 用户激活时间

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "suspend_time")
	private Date suspendTime;// 用户停机时间

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cancle_time")
	private Date cancleTime;// 用户销户时间

	@Column(name = "os_version")
	private String osVersion;// 系统版本

	@Column(name = "app_version")
	private String appVersion;// 应用版本

	@Column(name = "app_code")
	private String appCode;// APP代码

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_login_time")
	private Date lastLoginTime;// 用户最近一次登录时间

	@Column(name = "client_ip")
	private String clientIp;// 客户端IP

	/**** 不输出 *******/
	@Column(name = "wechat_no")
	private String wechatNo;// 微信号

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "wechat_bind_time")
	private Date wechatBindTime;// 微信绑定时间

	@Column(name = "alipay_no")
	private String alipayNo;// 支付宝号

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "alipay_bind_time")
	private Date alipayBindTime;// 支付宝绑定时间

	@Column(name = "user_id")
	private String userId;// 用户业务帐号

	@Column(name = "user_password")
	private String userPassword;// 用户业务帐号对应的密码

	@Column(name = "user_token")
	private String userToken;
	
	
	private String reserved1;
	private String reserved2;
	private String reserved3;
	private String reserved4;
	private String reserved5;

	/**** 不输出 *******/

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getFromEntrance() {
		return fromEntrance;
	}

	public void setFromEntrance(String fromEntrance) {
		this.fromEntrance = fromEntrance;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
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
	
	public String getPartnerGroupCode() {
		return partnerGroupCode;
	}

	public void setPartnerGroupCode(String partnerGroupCode) {
		this.partnerGroupCode = partnerGroupCode;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
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

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Date getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	public Date getActivateTime() {
		return activateTime;
	}

	public void setActivateTime(Date activateTime) {
		this.activateTime = activateTime;
	}

	public Date getSuspendTime() {
		return suspendTime;
	}

	public void setSuspendTime(Date suspendTime) {
		this.suspendTime = suspendTime;
	}

	public Date getCancleTime() {
		return cancleTime;
	}

	public void setCancleTime(Date cancleTime) {
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

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getWechatNo() {
		return wechatNo;
	}

	public void setWechatNo(String wechatNo) {
		this.wechatNo = wechatNo;
	}

	public Date getWechatBindTime() {
		return wechatBindTime;
	}

	public void setWechatBindTime(Date wechatBindTime) {
		this.wechatBindTime = wechatBindTime;
	}

	public String getAlipayNo() {
		return alipayNo;
	}

	public void setAlipayNo(String alipayNo) {
		this.alipayNo = alipayNo;
	}

	public Date getAlipayBindTime() {
		return alipayBindTime;
	}

	public void setAlipayBindTime(Date alipayBindTime) {
		this.alipayBindTime = alipayBindTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getReserved1() {
		return reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getReserved2() {
		return reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	public String getReserved3() {
		return reserved3;
	}

	public void setReserved3(String reserved3) {
		this.reserved3 = reserved3;
	}

	public String getReserved4() {
		return reserved4;
	}

	public void setReserved4(String reserved4) {
		this.reserved4 = reserved4;
	}

	public String getReserved5() {
		return reserved5;
	}

	public void setReserved5(String reserved5) {
		this.reserved5 = reserved5;
	}

}
