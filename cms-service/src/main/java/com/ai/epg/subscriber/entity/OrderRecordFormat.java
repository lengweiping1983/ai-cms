package com.ai.epg.subscriber.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ai.common.entity.AbstractEntityFormat;

/**
 * 订购记录实体
 * 
 */
public class OrderRecordFormat extends AbstractEntityFormat {
	private static final long serialVersionUID = 1331711166107L;

	private String appCode="";// APP代码

	private String orderCode="";// 订单代码

	private String transactionCode="";// 交易号
	
	private String purchaseType="";// 购买类型（1=PPV,2=包月,3=包年）

	private String partnerUserId="";// 运营商用户唯一标识

	private String partnerUserToken="";// 运营商用户token

	private String partnerSubscriptionCode="";// 运营商交易号

	private String productCode="";// 产品代码

	private String productName="";// 产品名称

	private String price="0";// 价格,单位分

	private String contentCode="";// 内容Id/内容代码

	private String itemType="";// 元素类型

	private String itemId="";// 元素标识

	private String itemName="";// 内容名称

	private String paymentStatus = "0";// 支付状态（0=待支付、1=支付成功、2=支付失败、3=支付超时、4=取消支付）

	private String paymentType="";// 支付类型（1=帐单支付、2=微信支付）

	private String paymentAccount="";// 支付帐号

	private String subscriptionExtend="";// 是否自动续订（0=不续订、1=续订）

	private String subscriptionTime="";// 订购时间

	private String unsubscribeTime="";// 退订时间

	private String validTime="";// 生效时间

	private String expiredTime="";// 过期时间

	private String result="";// 结果

	private String description="";// 描述

	private String clientIp="";// 客户端IP

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

	public String getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getContentCode() {
		return contentCode;
	}

	public void setContentCode(String contentCode) {
		this.contentCode = contentCode;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentAccount() {
		return paymentAccount;
	}

	public void setPaymentAccount(String paymentAccount) {
		this.paymentAccount = paymentAccount;
	}

	public String getSubscriptionExtend() {
		return subscriptionExtend;
	}

	public void setSubscriptionExtend(String subscriptionExtend) {
		this.subscriptionExtend = subscriptionExtend;
	}

	public String getSubscriptionTime() {
		return subscriptionTime;
	}

	public void setSubscriptionTime(String subscriptionTime) {
		this.subscriptionTime = subscriptionTime;
	}

	public String getUnsubscribeTime() {
		return unsubscribeTime;
	}

	public void setUnsubscribeTime(String unsubscribeTime) {
		this.unsubscribeTime = unsubscribeTime;
	}

	public String getValidTime() {
		return validTime;
	}

	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}

	public String getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(String expiredTime) {
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
	
	
	@Override
	public String toString() {
		return "{\"appCode\":\"" + appCode + "\",\"orderCode\":\"" + orderCode + "\",\"transactionCode\":\""
				+ transactionCode + "\",\"purchaseType\":\"" + purchaseType + "\",\"partnerUserId\":\"" + partnerUserId
				+ "\",\"partnerUserToken\":\"" + partnerUserToken + "\",\"partnerSubscriptionCode\":\""
				+ partnerSubscriptionCode + "\",\"productCode\":\"" + productCode + "\",\"productName\":\""
				+ productName + "\",\"price\":\"" + price
				+ "\",\"contentCode\":\"" + contentCode + "\",\"itemType\":\"" + itemType + "\",\"itemId\":\"" + itemId
				+ "\",\"itemName\":\"" + itemName + "\",\"paymentStatus\":\"" + paymentStatus + "\",\"paymentType\":\""
				+ paymentType + "\",\"paymentAccount\":\"" + paymentAccount + "\",\"subscriptionExtend\":\""
				+ subscriptionExtend + "\",\"subscriptionTime\":\"" + subscriptionTime + "\",\"unsubscribeTime\":\""
				+ unsubscribeTime + "\",\"validTime\":\"" + validTime + "\",\"expiredTime\":\"" + expiredTime
				+ "\",\"result\":\"" + result + "\",\"description\":\"" + description + "\",\"clientIp\":\"" + clientIp
				+ "\"} ";
	}
}
