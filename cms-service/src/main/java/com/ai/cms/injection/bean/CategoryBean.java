package com.ai.cms.injection.bean;

import com.ai.cms.injection.enums.InjectionItemTypeEnum;

public class CategoryBean extends ObjectBean {
	private String Name;
	private String ParentCode;
	private String ParentID;
	private String Status;
	private String Sequence;
	private String Description;

	private String parentPartnerItemCode;// 父元素运营商侧Code

	public CategoryBean() {
		ElementType = InjectionItemTypeEnum.CATEGORY.getValue();
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getParentCode() {
		return ParentCode;
	}

	public void setParentCode(String parentCode) {
		ParentCode = parentCode;
	}

	public String getParentID() {
		return ParentID;
	}

	public void setParentID(String parentID) {
		ParentID = parentID;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getSequence() {
		return Sequence;
	}

	public void setSequence(String sequence) {
		Sequence = sequence;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getParentPartnerItemCode() {
		return parentPartnerItemCode;
	}

	public void setParentPartnerItemCode(String parentPartnerItemCode) {
		this.parentPartnerItemCode = parentPartnerItemCode;
	}

}
