package com.ai.cms.injection.bean;

import com.ai.cms.injection.enums.InjectionItemTypeEnum;

public class ScheduleBean extends ObjectBean {
	private String parentPartnerItemCode;// 父元素运营商侧Code
	private String ChannelCode;
	private String ChannelID;
	private String Duration;
	private String Genre;
	private String ObjectID;
	private String ObjectType;
	private String ProgramName;
	private String StartDate;
	private String StartTime;
	private String Status;

	public ScheduleBean() {
		ElementType = InjectionItemTypeEnum.SCHEDULE.getValue();
	}

	public String getChannelCode() {
		return ChannelCode;
	}

	public void setChannelCode(String channelCode) {
		ChannelCode = channelCode;
	}

	public String getChannelID() {
		return ChannelID;
	}

	public void setChannelID(String channelID) {
		ChannelID = channelID;
	}

	public String getDuration() {
		return Duration;
	}

	public void setDuration(String duration) {
		Duration = duration;
	}

	public String getGenre() {
		return Genre;
	}

	public void setGenre(String genre) {
		Genre = genre;
	}

	public String getObjectID() {
		return ObjectID;
	}

	public void setObjectID(String objectID) {
		ObjectID = objectID;
	}

	public String getObjectType() {
		return ObjectType;
	}

	public void setObjectType(String objectType) {
		ObjectType = objectType;
	}

	public String getProgramName() {
		return ProgramName;
	}

	public void setProgramName(String programName) {
		ProgramName = programName;
	}

	public String getStartDate() {
		return StartDate;
	}

	public void setStartDate(String startDate) {
		StartDate = startDate;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getParentPartnerItemCode() {
		return parentPartnerItemCode;
	}

	public void setParentPartnerItemCode(String parentPartnerItemCode) {
		this.parentPartnerItemCode = parentPartnerItemCode;
	}

}
