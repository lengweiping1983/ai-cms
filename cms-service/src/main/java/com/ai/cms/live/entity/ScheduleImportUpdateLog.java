package com.ai.cms.live.entity;


import com.ai.common.entity.IdEntity;
import com.ai.common.excel.annotation.ExcelField;

public class ScheduleImportUpdateLog extends IdEntity {
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT = 1;

	@ExcelField(title = "播出日期", align = 1, groups = {DEFAULT}, sort = 1)
	private String airDate;// 节目单日期

	@ExcelField(title = "时间", align = 1, groups = {DEFAULT}, sort = 10)
	private String time;// 节目单日期

	@ExcelField(title = "时长", align = 1, groups = {DEFAULT}, sort = 11)
	private String timeLength; //节目单时长

	@ExcelField(title = "播出节目", align = 2, groups = {DEFAULT}, sort = 20)
	private String contentName;// 内容名称

	@ExcelField(title = "频道", align = 1, groups = {DEFAULT}, sort = 21)
	private String channelName;// 频道名称

	public String getAirDate() {
		return airDate;
	}

	public void setAirDate(String airDate) {
		this.airDate = airDate;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTimeLength() {
		return timeLength;
	}

	public void setTimeLength(String timeLength) {
		this.timeLength = timeLength;
	}

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
}
