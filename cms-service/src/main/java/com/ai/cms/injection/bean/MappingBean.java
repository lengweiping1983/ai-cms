package com.ai.cms.injection.bean;

public class MappingBean {

	private String ID;
	private String Action;
	private String ParentType;
	private String ElementType;
	private String ParentID;
	private String ElementID;
	private String ParentCode;
	private String ElementCode;
	private String Type;
	private String Sequence;
	private String ValidStart;// 当Mapping的ParentType为Package(SVOD)时,
								// 标识SVOD节目的服务起始时间(YYYYMMDDHH24MiSS)
	private String ValidEnd;// 当Mapping的ParentType为Package(SVOD)时,
							// 标识SVOD节目的服务终止时间(YYYYMMDDHH24MiSS)

	private String ElementName;
	private String ParentName;

	private String Result;
	private String ErrorDescription;

	private String partnerItemCode;// 元素运营商侧Code

	private String parentPartnerItemCode;// 父元素运营商侧Code

	private String ParentSubType;// 父元素子类型

	private Long relId;// 关联id

	public Long getRelId() {
		return relId;
	}

	public void setRelId(Long relId) {
		this.relId = relId;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getAction() {
		return Action;
	}

	public void setAction(String action) {
		Action = action;
	}

	public String getParentType() {
		return ParentType;
	}

	public void setParentType(String parentType) {
		ParentType = parentType;
	}

	public String getElementType() {
		return ElementType;
	}

	public void setElementType(String elementType) {
		ElementType = elementType;
	}

	public String getParentID() {
		return ParentID;
	}

	public void setParentID(String parentID) {
		ParentID = parentID;
	}

	public String getElementID() {
		return ElementID;
	}

	public void setElementID(String elementID) {
		ElementID = elementID;
	}

	public String getParentCode() {
		return ParentCode;
	}

	public void setParentCode(String parentCode) {
		ParentCode = parentCode;
	}

	public String getElementCode() {
		return ElementCode;
	}

	public void setElementCode(String elementCode) {
		ElementCode = elementCode;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getSequence() {
		return Sequence;
	}

	public void setSequence(String sequence) {
		Sequence = sequence;
	}

	public String getValidStart() {
		return ValidStart;
	}

	public void setValidStart(String validStart) {
		ValidStart = validStart;
	}

	public String getValidEnd() {
		return ValidEnd;
	}

	public void setValidEnd(String validEnd) {
		ValidEnd = validEnd;
	}

	public String getResult() {
		return Result;
	}

	public void setResult(String result) {
		Result = result;
	}

	public String getErrorDescription() {
		return ErrorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		ErrorDescription = errorDescription;
	}

	public String getElementName() {
		return ElementName;
	}

	public void setElementName(String elementName) {
		ElementName = elementName;
	}

	public String getParentName() {
		return ParentName;
	}

	public void setParentName(String parentName) {
		ParentName = parentName;
	}

	public String getPartnerItemCode() {
		return partnerItemCode;
	}

	public void setPartnerItemCode(String partnerItemCode) {
		this.partnerItemCode = partnerItemCode;
	}

	public String getParentPartnerItemCode() {
		return parentPartnerItemCode;
	}

	public void setParentPartnerItemCode(String parentPartnerItemCode) {
		this.parentPartnerItemCode = parentPartnerItemCode;
	}

	public String getParentSubType() {
		return ParentSubType;
	}

	public void setParentSubType(String parentSubType) {
		ParentSubType = parentSubType;
	}

	@Override
	public String toString() {
		return "MappingBean [ID=" + ID + ", Action=" + Action + ", ParentType="
				+ ParentType + ", ElementType=" + ElementType + ", ParentID="
				+ ParentID + ", ElementID=" + ElementID + ", ParentCode="
				+ ParentCode + ", ElementCode=" + ElementCode + ", Type="
				+ Type + ", Sequence=" + Sequence + ", ValidStart="
				+ ValidStart + ", ValidEnd=" + ValidEnd + ", ElementName="
				+ ElementName + ", ParentName=" + ParentName + ", Result="
				+ Result + ", ErrorDescription=" + ErrorDescription + "]";
	}

}
