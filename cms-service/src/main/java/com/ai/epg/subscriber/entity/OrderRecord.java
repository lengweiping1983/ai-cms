package com.ai.epg.subscriber.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.YesNoEnum;

/**
 * 订购记录实体
 * 
 */
@Entity
@Table(name = "user_order_record")
public class OrderRecord extends AbstractEntity {
	private static final long serialVersionUID = 1331711166107L;

	@Column(name = "type")
	private Integer type;// 类型
	
	@Column(name = "app_code")
	private String appCode;// APP代码

	@Column(name = "order_code")
	private String orderCode;// 订单号

	@Column(name = "transaction_code")
	private String transactionCode;// 交易号

	@Column(name = "return_url")
	private String returnUrl;// 返回址址

	@Column(name = "purchase_type")
	private Integer purchaseType;// 购买类型（1=PPV,2=包月,3=包年）

	@Column(name = "partner_user_id")
	private String partnerUserId;// 运营商用户唯一标识

	@Column(name = "partner_user_token")
	private String partnerUserToken;// 运营商用户token

	@Column(name = "partner_subscription_code")
	private String partnerSubscriptionCode;// 运营商交易号

	@Column(name = "product_code")
	private String productCode;// 产品代码

	@Column(name = "product_name")
	private String productName;// 产品名称

	@Column(name = "origin_price")
	private Integer originPrice;// 原价,单位分

	@Column(name = "price")
	private Integer price;// 价格,单位分

	@Column(name = "content_code")
	private String contentCode;// 内容Id/内容代码

	@Column(name = "item_type")
	private Integer itemType;// 元素类型

	@Column(name = "item_id")
	private Long itemId;// 元素标识

	@Column(name = "item_name")
	private String itemName;// 内容名称
	
	@Column(name = "play_mode")
	private String playMode;// 直播/点播
	
	@Column(name = "from_entrance")
	private String fromEntrance;// 从那个入口进来

	@Column(name = "payment_status")
	private Integer paymentStatus = 0;// 支付状态（0=待支付、1=支付成功、2=支付失败、3=支付超时、4=取消支付）

	@Column(name = "payment_type")
	private Integer paymentType;// 支付类型（1=帐单支付、2=微信支付、3=支付宝支付、4=手机话费支付）

	@Column(name = "payment_account")
	private String paymentAccount;// 支付帐号

	@Column(name = "subscription_extend")
	private Integer subscriptionExtend;// 是否自动续订（0=不续订、1=续订）

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "subscription_time")
	private Date subscriptionTime;// 订购时间

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "unsubscribe_time")
	private Date unsubscribeTime;// 退订时间

	@Column(name = "support_unsubscribe")
	private Integer supportUnsubscribe = YesNoEnum.NO.getKey(); // 是否支持退订
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "valid_time")
	private Date validTime;// 生效时间

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expired_time")
	private Date expiredTime;// 过期时间

	@Column(name = "result")
	private String result;// 结果

	@Column(name = "description")
	private String description;// 描述

	@Column(name = "client_ip")
	private String clientIp;// 客户端IP
	
	private String reserved1;
	private String reserved2;
	private String reserved3;
	private String reserved4;
	private String reserved5;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public Integer getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(Integer purchaseType) {
		this.purchaseType = purchaseType;
	}

	public String getPartnerUserId() {
		return partnerUserId;
	}

	public void setPartnerUserId(String partnerUserId) {
		this.partnerUserId = partnerUserId;
	}

	public String getPartnerUserToken() {
		return partnerUserToken;
	}

	public void setPartnerUserToken(String partnerUserToken) {
		this.partnerUserToken = partnerUserToken;
	}

	public String getPartnerSubscriptionCode() {
		return partnerSubscriptionCode;
	}

	public void setPartnerSubscriptionCode(String partnerSubscriptionCode) {
		this.partnerSubscriptionCode = partnerSubscriptionCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getOriginPrice() {
		return originPrice;
	}

	public void setOriginPrice(Integer originPrice) {
		this.originPrice = originPrice;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getContentCode() {
		return contentCode;
	}

	public void setContentCode(String contentCode) {
		this.contentCode = contentCode;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public String getPlayMode() {
		return playMode;
	}

	public void setPlayMode(String playMode) {
		this.playMode = playMode;
	}

	public String getFromEntrance() {
		return fromEntrance;
	}

	public void setFromEntrance(String fromEntrance) {
		this.fromEntrance = fromEntrance;
	}

	public Integer getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentAccount() {
		return paymentAccount;
	}

	public void setPaymentAccount(String paymentAccount) {
		this.paymentAccount = paymentAccount;
	}

	public Integer getSubscriptionExtend() {
		return subscriptionExtend;
	}

	public void setSubscriptionExtend(Integer subscriptionExtend) {
		this.subscriptionExtend = subscriptionExtend;
	}

	public Date getSubscriptionTime() {
		return subscriptionTime;
	}

	public void setSubscriptionTime(Date subscriptionTime) {
		this.subscriptionTime = subscriptionTime;
	}

	public Date getUnsubscribeTime() {
		return unsubscribeTime;
	}

	public void setUnsubscribeTime(Date unsubscribeTime) {
		this.unsubscribeTime = unsubscribeTime;
	}
	
	public Integer getSupportUnsubscribe() {
		return supportUnsubscribe;
	}

	public void setSupportUnsubscribe(Integer supportUnsubscribe) {
		this.supportUnsubscribe = supportUnsubscribe;
	}

	public Date getValidTime() {
		return validTime;
	}

	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}

	public Date getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
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
