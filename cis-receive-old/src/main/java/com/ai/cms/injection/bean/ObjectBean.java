package com.ai.cms.injection.bean;

public abstract class ObjectBean {

	protected String ElementType;

	private String Action;

	private String ID;

	private String Code;

	private String Name;

	private String partnerItemCode;// 元素运营商侧Code

	public String getElementType() {
		return ElementType;
	}

	public void setElementType(String elementType) {
		ElementType = elementType;
	}

	public String getAction() {
		return Action;
	}

	public void setAction(String action) {
		Action = action;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPartnerItemCode() {
		return partnerItemCode;
	}

	public void setPartnerItemCode(String partnerItemCode) {
		this.partnerItemCode = partnerItemCode;
	}

}
