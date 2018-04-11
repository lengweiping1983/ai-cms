package com.ai.common.enums;


public enum PaymentStatusEnum {
	 WIAT_PAYMENT(0, "待支付"), PAYMENT_SUCESS(1, "支付成功"),PAYMENT_FAIL(2, "支付失败"), 
	 PAYMENT_OVERTIME(3, "支付超时"), CANCEL_PAYMENT(4, "取消支付");
	
	int key;
	String value;
	
	PaymentStatusEnum(int key,String value){
		this.key =key;
		this.value = value;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public static PaymentStatusEnum getEnumByKey(int key) {
        for (PaymentStatusEnum objEnum : PaymentStatusEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }
}
