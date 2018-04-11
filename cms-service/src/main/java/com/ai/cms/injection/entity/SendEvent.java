//package com.ai.cms.injection.entity;
//
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import javax.validation.constraints.NotNull;
//
//import com.ai.common.entity.IdEntity;
//import com.ai.common.enums.YesNoEnum;
//
///**
// * 事件实体
// *
// */
//@Entity
//@Table(name = "cms_injection_send_event")
//public class SendEvent extends IdEntity {
//	private static final long serialVersionUID = 1L;
//
//	@NotNull
//	@Column(name = "platform_id")
//	private Long platformId;// 分发平台
//
//	@NotNull
//	@Column(name = "category")
//	private String category;// 分组
//
//	@NotNull
//	@Column(name = "type")
//	private Integer type;// 类型:1=对象(Object)、2=映射关系(Mapping)
//
//	@NotNull
//	@Column(name = "action")
//	private Integer action;// 操作:1=创建、2=修改、3=删除
//
//	@NotNull
//	@Column(name = "item_type")
//	private Integer itemType;// 元素类型
//
//	@NotNull
//	@Column(name = "item_id")
//	private Long itemId;// 元素id
//
//	@Column(name = "item_code")
//	private String itemCode;// 元素代码
//
//	@Column(name = "item_name")
//	private String itemName;// 元素名称
//
//	@Column(name = "partner_item_code")
//	private String partnerItemCode;// 元素运营商侧Code
//
//	@Column(name = "parent_item_type")
//	private Integer parentItemType;// 父元素类型
//
//	@Column(name = "parent_item_id")
//	private Long parentItemId;// 父元素id
//
//	@Column(name = "parent_item_code")
//	private String parentItemCode;// 父元素代码
//
//	@Column(name = "parent_item_name")
//	private String parentItemName;// 父元素名称
//
//	@Column(name = "parent_partner_item_code")
//	private String parentPartnerItemCode;// 父元素运营商侧Code
//
//	@NotNull
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "fire_time")
//	private Date fireTime;// 发生时间
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "next_check_time")
//	private Date nextCheckTime;// 下一次检查时间
//
//	@Column(name = "correlate_id")
//	private String correlateId; // 多个事件生成一个指令
//
//	@NotNull
//	@Column(name = "status")
//	private Integer status = YesNoEnum.NO.getKey();// 0=未生成任务，1=已生成任务
//
//	@NotNull
//	private Integer priority = 1;// 优先级:1-10，10最高，默认为1
//
//	@Column(name = "sort_index")
//	private Integer sortIndex = 1; // 排序值
//
//	@Column(name = "rel_id")
//	private Long relId;// 关联id
//
//	public SendEvent() {
//
//	}
//
//	public SendEvent(InjectionObject injectionObject) {
//		if (injectionObject == null || injectionObject.getPlatformId() == null
//				|| injectionObject.getCategory() == null) {
//			throw new RuntimeException();
//		}
//		this.platformId = injectionObject.getPlatformId();
//		this.category = injectionObject.getCategory();
//	}
//
//	public SendEvent(Long platformId, String category) {
//		if (platformId == null || category == null) {
//			throw new RuntimeException();
//		}
//		this.platformId = platformId;
//		this.category = category;
//	}
//
//	public Long getPlatformId() {
//		return platformId;
//	}
//
//	public void setPlatformId(Long platformId) {
//		this.platformId = platformId;
//	}
//
//	public String getCategory() {
//		return category;
//	}
//
//	public void setCategory(String category) {
//		this.category = category;
//	}
//
//	public Integer getType() {
//		return type;
//	}
//
//	public void setType(Integer type) {
//		this.type = type;
//	}
//
//	public Integer getAction() {
//		return action;
//	}
//
//	public void setAction(Integer action) {
//		this.action = action;
//	}
//
//	public Integer getItemType() {
//		return itemType;
//	}
//
//	public void setItemType(Integer itemType) {
//		this.itemType = itemType;
//	}
//
//	public Long getItemId() {
//		return itemId;
//	}
//
//	public void setItemId(Long itemId) {
//		this.itemId = itemId;
//	}
//
//	public String getItemCode() {
//		return itemCode;
//	}
//
//	public void setItemCode(String itemCode) {
//		this.itemCode = itemCode;
//	}
//
//	public String getItemName() {
//		return itemName;
//	}
//
//	public void setItemName(String itemName) {
//		this.itemName = itemName;
//	}
//
//	public String getPartnerItemCode() {
//		return partnerItemCode;
//	}
//
//	public void setPartnerItemCode(String partnerItemCode) {
//		this.partnerItemCode = partnerItemCode;
//	}
//
//	public Integer getParentItemType() {
//		return parentItemType;
//	}
//
//	public void setParentItemType(Integer parentItemType) {
//		this.parentItemType = parentItemType;
//	}
//
//	public Long getParentItemId() {
//		return parentItemId;
//	}
//
//	public void setParentItemId(Long parentItemId) {
//		this.parentItemId = parentItemId;
//	}
//
//	public String getParentItemCode() {
//		return parentItemCode;
//	}
//
//	public void setParentItemCode(String parentItemCode) {
//		this.parentItemCode = parentItemCode;
//	}
//
//	public String getParentItemName() {
//		return parentItemName;
//	}
//
//	public void setParentItemName(String parentItemName) {
//		this.parentItemName = parentItemName;
//	}
//
//	public String getParentPartnerItemCode() {
//		return parentPartnerItemCode;
//	}
//
//	public void setParentPartnerItemCode(String parentPartnerItemCode) {
//		this.parentPartnerItemCode = parentPartnerItemCode;
//	}
//
//	public Date getFireTime() {
//		return fireTime;
//	}
//
//	public void setFireTime(Date fireTime) {
//		this.fireTime = fireTime;
//	}
//
//	public Date getNextCheckTime() {
//		return nextCheckTime;
//	}
//
//	public void setNextCheckTime(Date nextCheckTime) {
//		this.nextCheckTime = nextCheckTime;
//	}
//
//	public String getCorrelateId() {
//		return correlateId;
//	}
//
//	public void setCorrelateId(String correlateId) {
//		this.correlateId = correlateId;
//	}
//
//	public Integer getStatus() {
//		return status;
//	}
//
//	public void setStatus(Integer status) {
//		this.status = status;
//	}
//
//	public Integer getPriority() {
//		return priority;
//	}
//
//	public void setPriority(Integer priority) {
//		this.priority = priority;
//	}
//
//	public Integer getSortIndex() {
//		return sortIndex;
//	}
//
//	public void setSortIndex(Integer sortIndex) {
//		this.sortIndex = sortIndex;
//	}
//
//	public Long getRelId() {
//		return relId;
//	}
//
//	public void setRelId(Long relId) {
//		this.relId = relId;
//	}
//
//}
