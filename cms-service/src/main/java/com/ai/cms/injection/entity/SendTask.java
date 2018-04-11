package com.ai.cms.injection.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.ai.cms.injection.enums.SendTaskStatusEnum;
import com.ai.common.entity.AbstractEntity;

/**
 * 发送任务实体
 *
 */
@Entity
@Table(name = "cms_injection_send_task")
public class SendTask extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "platform_id")
	private Long platformId;// 分发平台

	@NotNull
	@Column(name = "category")
	private String category;// 分组

	@Column(name = "type")
	private Integer type;// 类型:1=对象(Object)、2=映射关系(Mapping)

	@Column(name = "action")
	private Integer action;// 操作:1=创建、2=修改、3=删除

	@Column(name = "item_type")
	private Integer itemType;// 元素类型

	@Column(name = "item_id")
	private Long itemId;// 元素id

	@Column(name = "sub_item_id")
	private String subItemId;// 子元素id

	private String name; // 任务名称

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "first_request_time")
	private Date firstRequestTime; // 第一次发送请求时间

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_request_time")
	private Date lastRequestTime; // 最后发送请求时间

	@Column(name = "request_times")
	private Integer requestTimes = 0;// 发送请求次数

	@Column(name = "request_total_times")
	private Integer requestTotalTimes = 0;// 发送请求总次数

	@Column(name = "pre_task_id")
	private Long preTaskId; // 前置任务id,空无前置任务

	@Column(name = "pre_task_status")
	private Integer preTaskStatus; // 前置任务是否完成:0=未完成、1=已完成

	@NotNull
	private Integer priority = 1;// 优先级

	@NotNull
	private Integer status = SendTaskStatusEnum.DEFAULT.getKey(); // 任务状态

	@Column(name = "correlate_id")
	private String correlateId; // 相关性标识，用于关联指令执行请求消息和结果通知消息。对于重发的指令，该字段值保持一致，对于不同的指令，该字段在同层内具有唯一性。

	@Column(name = "cmd_file_url")
	private String cmdFileURL; // XML指令文件的URL，此URL为标准FTP方式，样例ftp://username:password@ip:port/..../xxx.xml。

	@Column(name = "request_result")
	private Integer requestResult; // 请求消息结果：0：成功，-1：失败

	@Column(name = "request_error_description")
	private String requestErrorDescription; // 错误信息详细描述

	@Column(name = "cmd_result")
	private Integer cmdResult; // 命令执行结果：0：成功，-1：通常失败，其他结果待定义

	@Column(name = "result_file_url")
	private String resultFileURL; // 查询结果XML文件的URL，该字段仅针对查询结果通知消息出现，此URL为标准FTP方式，样例ftp://username:password@ip:port/..../xxx.xml。

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "response_time")
	private Date responseTime; // 响应时间

	@Column(name = "response_result")
	private Integer responseResult; // 响应消息结果：0：成功，-1：失败

	@Column(name = "response_error_description")
	private String responseErrorDescription; // 错误信息详细描述

	@Column(name = "request_xml_file_path")
	private String requestXmlFilePath; // 请求xml本地路径

	@Column(name = "request_xml_file_content")
	private String requestXmlFileContent; // 请求xml内容

	@Column(name = "response_xml_file_path")
	private String responseXmlFilePath; // 响应xml本地路径

	@Column(name = "response_xml_file_content")
	private String responseXmlFileContent; // 响应xml内容

	@Column(name = "operation_object_ids")
	private String operationObjectIds; // 操作对象Id列表,格式如o:Program:REGIST:288872:00000001000000010000000000288872;o:Picture:REGIST:1000288872:00000001000000080000001000288872;o:Movie:REGIST:285303:00000001000000020000000000285303;

	public SendTask() {

	}

	public SendTask(InjectionObject injectionObject) {
		if (injectionObject == null || injectionObject.getPlatformId() == null
				|| injectionObject.getCategory() == null) {
			throw new RuntimeException();
		}
		this.platformId = injectionObject.getPlatformId();
		this.category = injectionObject.getCategory();
	}

	public Long getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getAction() {
		return action;
	}

	public void setAction(Integer action) {
		this.action = action;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getSubItemId() {
		return subItemId;
	}

	public void setSubItemId(String subItemId) {
		this.subItemId = subItemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getFirstRequestTime() {
		return firstRequestTime;
	}

	public void setFirstRequestTime(Date firstRequestTime) {
		this.firstRequestTime = firstRequestTime;
	}

	public Date getLastRequestTime() {
		return lastRequestTime;
	}

	public void setLastRequestTime(Date lastRequestTime) {
		this.lastRequestTime = lastRequestTime;
	}

	public Integer getRequestTimes() {
		return requestTimes;
	}

	public void setRequestTimes(Integer requestTimes) {
		this.requestTimes = requestTimes;
	}

	public Integer getRequestTotalTimes() {
		return requestTotalTimes;
	}

	public void setRequestTotalTimes(Integer requestTotalTimes) {
		this.requestTotalTimes = requestTotalTimes;
	}

	public Long getPreTaskId() {
		return preTaskId;
	}

	public void setPreTaskId(Long preTaskId) {
		this.preTaskId = preTaskId;
	}

	public Integer getPreTaskStatus() {
		return preTaskStatus;
	}

	public void setPreTaskStatus(Integer preTaskStatus) {
		this.preTaskStatus = preTaskStatus;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCorrelateId() {
		return correlateId;
	}

	public void setCorrelateId(String correlateId) {
		this.correlateId = correlateId;
	}

	public String getCmdFileURL() {
		return cmdFileURL;
	}

	public void setCmdFileURL(String cmdFileURL) {
		this.cmdFileURL = cmdFileURL;
	}

	public Integer getRequestResult() {
		return requestResult;
	}

	public void setRequestResult(Integer requestResult) {
		this.requestResult = requestResult;
	}

	public String getRequestErrorDescription() {
		return requestErrorDescription;
	}

	public void setRequestErrorDescription(String requestErrorDescription) {
		this.requestErrorDescription = requestErrorDescription;
	}

	public Integer getCmdResult() {
		return cmdResult;
	}

	public void setCmdResult(Integer cmdResult) {
		this.cmdResult = cmdResult;
	}

	public String getResultFileURL() {
		return resultFileURL;
	}

	public void setResultFileURL(String resultFileURL) {
		this.resultFileURL = resultFileURL;
	}

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public Integer getResponseResult() {
		return responseResult;
	}

	public void setResponseResult(Integer responseResult) {
		this.responseResult = responseResult;
	}

	public String getResponseErrorDescription() {
		return responseErrorDescription;
	}

	public void setResponseErrorDescription(String responseErrorDescription) {
		this.responseErrorDescription = responseErrorDescription;
	}

	public String getRequestXmlFilePath() {
		return requestXmlFilePath;
	}

	public void setRequestXmlFilePath(String requestXmlFilePath) {
		this.requestXmlFilePath = requestXmlFilePath;
	}

	public String getRequestXmlFileContent() {
		return requestXmlFileContent;
	}

	public void setRequestXmlFileContent(String requestXmlFileContent) {
		this.requestXmlFileContent = requestXmlFileContent;
	}

	public String getResponseXmlFilePath() {
		return responseXmlFilePath;
	}

	public void setResponseXmlFilePath(String responseXmlFilePath) {
		this.responseXmlFilePath = responseXmlFilePath;
	}

	public String getResponseXmlFileContent() {
		return responseXmlFileContent;
	}

	public void setResponseXmlFileContent(String responseXmlFileContent) {
		this.responseXmlFileContent = responseXmlFileContent;
	}

	public String getOperationObjectIds() {
		return operationObjectIds;
	}

	public void setOperationObjectIds(String operationObjectIds) {
		this.operationObjectIds = operationObjectIds;
	}

}
