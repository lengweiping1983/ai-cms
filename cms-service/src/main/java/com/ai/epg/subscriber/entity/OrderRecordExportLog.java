package com.ai.epg.subscriber.entity;

import com.ai.common.entity.IdEntity;
import com.ai.common.excel.annotation.ExcelField;

public class OrderRecordExportLog extends IdEntity {
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT = 1;
	
	@ExcelField(title = "业务帐号", align = 1, groups = { DEFAULT }, sort = 1)
	private String partnerUserId = "业务帐号";// 运营商用户唯一标识
	
	@ExcelField(title = "订单号", align = 1, groups = { DEFAULT }, sort = 10)
	private String orderCode;// 订单号

	@ExcelField(title = "产品代码", align = 1, groups = { DEFAULT }, sort = 20)
	private String productCode;//产品代码
	
	@ExcelField(title = "内容代码", align = 1, groups = { DEFAULT }, sort = 30)
	private String contentCode;//内容代码

	@ExcelField(title = "元素类型", align = 1, groups = { DEFAULT }, sort = 40)
	private String itemType;// 元素类型

	@ExcelField(title = "元素id", align = 1, groups = { DEFAULT }, sort = 50)
	private String itemId;//元素id

	@ExcelField(title = "元素名称", align = 1, groups = { DEFAULT }, sort = 60)
	private String itemName;// 元素名称

	@ExcelField(title = "播放方式", align = 1, groups = { DEFAULT }, sort = 70)
	private String playMode;// 播放方式

	@ExcelField(title = "外部入口", align = 1, groups = { DEFAULT }, sort = 80)
	private String fromEntrance;// 外部入口

	@ExcelField(title = "订购时间", align = 1, groups = { DEFAULT }, sort = 90)
	private String subscriptionTime;// 订购时间

	@ExcelField(title = "退订时间", align = 1, groups = { DEFAULT }, sort = 100)
	private String unsubscribeTime;// 退订时间

	@ExcelField(title = "支付状态", align = 1, groups = { DEFAULT }, sort = 110)
	private String paymentStatus;// 支付状态（0=待支付、1=支付成功、2=支付失败、3=支付超时、4=取消支付）

	@ExcelField(title = "生效时间", align = 1, groups = { DEFAULT }, sort = 120)
	private String validTime;// 生效时间

	@ExcelField(title = "过期时间", align = 1, groups = { DEFAULT }, sort = 130)
	private String expiredTime;// 过期时间

	@ExcelField(title = "客户端IP", align = 1, groups = { DEFAULT }, sort = 140)
	private String clientIp;// 客户端IP

	public String getPartnerUserId() {
		return partnerUserId;
	}

	public void setPartnerUserId(String partnerUserId) {
		this.partnerUserId = partnerUserId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
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

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
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

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	
	

}
