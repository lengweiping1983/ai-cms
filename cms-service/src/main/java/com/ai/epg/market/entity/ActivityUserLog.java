package com.ai.epg.market.entity;

import com.ai.common.entity.IdEntity;
import com.ai.common.excel.annotation.ExcelField;

public class ActivityUserLog extends IdEntity {
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT = 1;

	@ExcelField(title = "活动名称", align = 1, groups = { DEFAULT }, sort = 1)
	private String activityName;

	@ExcelField(title = "参与时间", align = 1, groups = { DEFAULT }, sort = 30)
	private String createTime;// 参与时间

	@ExcelField(title = "业务帐号", align = 1, groups = { DEFAULT }, sort = 10)
	private String partnerUserId;// 运营商用户唯一标识

	@ExcelField(title = "手机号", align = 1, groups = { DEFAULT }, sort = 20)
	private String phone;// 手机号

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPartnerUserId() {
		return partnerUserId;
	}

	public void setPartnerUserId(String partnerUserId) {
		this.partnerUserId = partnerUserId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
