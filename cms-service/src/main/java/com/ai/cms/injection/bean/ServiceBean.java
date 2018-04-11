package com.ai.cms.injection.bean;

import com.ai.cms.injection.enums.InjectionItemTypeEnum;

public class ServiceBean extends ObjectBean {
	private String Name;
	private String Type;
	private String LicensingWindowStart;
	private String LicensingWindowEnd;
	private String Price;
	private String Status;

	public ServiceBean() {
		ElementType = InjectionItemTypeEnum.PACKAGE.getValue();
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getLicensingWindowStart() {
		return LicensingWindowStart;
	}

	public void setLicensingWindowStart(String licensingWindowStart) {
		LicensingWindowStart = licensingWindowStart;
	}

	public String getLicensingWindowEnd() {
		return LicensingWindowEnd;
	}

	public void setLicensingWindowEnd(String licensingWindowEnd) {
		LicensingWindowEnd = licensingWindowEnd;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

}
