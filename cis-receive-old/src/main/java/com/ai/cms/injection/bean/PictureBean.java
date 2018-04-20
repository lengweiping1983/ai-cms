package com.ai.cms.injection.bean;

import com.ai.cms.injection.enums.InjectionItemTypeEnum;

public class PictureBean extends ObjectBean {

	private String FileURL;

	private String Description;

	private String Result;

	private String ErrorDescription;

	public PictureBean() {
		ElementType = InjectionItemTypeEnum.PICTURE.getValue();
	}

	public String getFileURL() {
		return FileURL;
	}

	public void setFileURL(String fileURL) {
		FileURL = fileURL;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
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

	@Override
	public String toString() {
		return "PictureBean [FileURL=" + FileURL + ", Description="
				+ Description + ", Result=" + Result + ", ErrorDescription="
				+ ErrorDescription + ", getElementType()=" + getElementType()
				+ ", getAction()=" + getAction() + ", getID()=" + getID()
				+ ", getCode()=" + getCode() + "]";
	}
}
