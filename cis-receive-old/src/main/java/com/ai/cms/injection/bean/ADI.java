package com.ai.cms.injection.bean;

import java.util.ArrayList;
import java.util.List;

public class ADI {

	private String StaffID;

	private Integer Priority;

	private Reply Reply;

	private List<ObjectBean> Objects = new ArrayList<ObjectBean>();

	private List<MappingBean> Mappings = new ArrayList<MappingBean>();

	public String getStaffID() {
		return StaffID;
	}

	public void setStaffID(String staffID) {
		StaffID = staffID;
	}

	public Integer getPriority() {
		return Priority;
	}

	public void setPriority(Integer priority) {
		Priority = priority;
	}

	public List<ObjectBean> getObjects() {
		return Objects;
	}

	public void setObjects(List<ObjectBean> objects) {
		Objects = objects;
	}

	public List<MappingBean> getMappings() {
		return Mappings;
	}

	public void setMappings(List<MappingBean> mappings) {
		Mappings = mappings;
	}

	public Reply getReply() {
		return Reply;
	}

	public void setReply(Reply reply) {
		Reply = reply;
	}

}
