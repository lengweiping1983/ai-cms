package com.ai.cms.injection.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.ai.cms.injection.enums.InjectionDirectionEnum;
import com.ai.cms.injection.enums.PlatformTypeEnum;
import com.ai.cms.injection.enums.ProviderInterfaceModeEnum;
import com.ai.cms.injection.enums.ProviderTypeEnum;
import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.ValidStatusEnum;
import com.ai.common.enums.YesNoEnum;

/**
 * 分发平台
 *
 */
@Entity
@Table(name = "cms_injection_platform")
public class InjectionPlatform extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "site_code")
	private String siteCode;// 所属渠道

	@NotNull
	@Column(name = "name")
	private String name; // 平台名称

	@NotNull
	@Column(name = "type")
	private Integer type = PlatformTypeEnum.MEDIA_SYSTEM.getKey(); // 平台类型

	@Column(name = "depend_platform_id")
	private String dependPlatformId; // 依赖平台

	@NotNull
	@Column(name = "provider")
	private Integer provider = ProviderTypeEnum.HUAWEI.getKey(); // 平台服务公司

	@NotNull
	@Column(name = "interface_mode")
	private Integer interfaceMode = ProviderInterfaceModeEnum.C2.getKey();// 接口方式

	@NotNull
	@Column(name = "need_download_image")
	private Integer needDownloadImage = YesNoEnum.NO.getKey();// 是否要下载海报

	@NotNull
	@Column(name = "need_download_video")
	private Integer needDownloadVideo = YesNoEnum.NO.getKey();// 是否要下载视频

	@NotNull
	@Column(name = "need_audit")
	private Integer needAudit = YesNoEnum.YES.getKey();// 是否要审核

	@NotNull
	@Column(name = "need_injection")
	private Integer needInjection = YesNoEnum.NO.getKey();// 是否要自动分发

	@NotNull
	@Column(name = "csp_id")
	private String cspId; // 互相约定的上层标识

	@NotNull
	@Column(name = "lsp_id")
	private String lspId; // 互相约定的下层标识

	@NotNull
	@Column(name = "service_url")
	private String serviceUrl; // 接口地址

	@Column(name = "live_service_url")
	private String liveServiceUrl; // 直播接口地址

	@NotNull
	@Column(name = "is_wsdl")
	private Integer isWSDL = YesNoEnum.YES.getKey();

	@Column(name = "namespace")
	private String namespace = "iptv";

	@Column(name = "template_id")
	private String templateId; // 码率模板Id,多个使用','分割

	private String description;// 描述

	@NotNull
	@Column(name = "direction")
	private Integer direction = InjectionDirectionEnum.SEND.getKey();// 数据分发方向

	@NotNull
	@Column(name = "is_callback")
	private Integer isCallback = YesNoEnum.NO.getKey();// 是否需要回写

	@NotNull
	@Column(name = "status")
	private Integer status = ValidStatusEnum.VALID.getKey(); // 状态:0=失效,1=生效

	@NotNull
	@Column(name = "template_custom")
	private Integer templateCustom = YesNoEnum.NO.getKey();// 模板自定义

	@NotNull
	@Column(name = "play_code_custom")
	private Integer playCodeCustom = YesNoEnum.NO.getKey();// 播放代码自定义

	@Column(name = "code_prefix")
	private String codePrefix = "00000000"; // 代码前缀

	@Column(name = "correlate_prefix")
	private String correlatePrefix = ""; // 工单前缀

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDependPlatformId() {
		return dependPlatformId;
	}

	public void setDependPlatformId(String dependPlatformId) {
		this.dependPlatformId = dependPlatformId;
	}

	public Integer getProvider() {
		return provider;
	}

	public void setProvider(Integer provider) {
		this.provider = provider;
	}

	public Integer getInterfaceMode() {
		return interfaceMode;
	}

	public void setInterfaceMode(Integer interfaceMode) {
		this.interfaceMode = interfaceMode;
	}

	public Integer getNeedDownloadImage() {
		return needDownloadImage;
	}

	public void setNeedDownloadImage(Integer needDownloadImage) {
		this.needDownloadImage = needDownloadImage;
	}

	public Integer getNeedDownloadVideo() {
		return needDownloadVideo;
	}

	public void setNeedDownloadVideo(Integer needDownloadVideo) {
		this.needDownloadVideo = needDownloadVideo;
	}

	public Integer getNeedAudit() {
		return needAudit;
	}

	public void setNeedAudit(Integer needAudit) {
		this.needAudit = needAudit;
	}

	public Integer getNeedInjection() {
		return needInjection;
	}

	public void setNeedInjection(Integer needInjection) {
		this.needInjection = needInjection;
	}

	public String getCspId() {
		return cspId;
	}

	public void setCspId(String cspId) {
		this.cspId = cspId;
	}

	public String getLspId() {
		return lspId;
	}

	public void setLspId(String lspId) {
		this.lspId = lspId;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getLiveServiceUrl() {
		return liveServiceUrl;
	}

	public void setLiveServiceUrl(String liveServiceUrl) {
		this.liveServiceUrl = liveServiceUrl;
	}

	public Integer getIsWSDL() {
		return isWSDL;
	}

	public void setIsWSDL(Integer isWSDL) {
		this.isWSDL = isWSDL;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	public Integer getIsCallback() {
		return isCallback;
	}

	public void setIsCallback(Integer isCallback) {
		this.isCallback = isCallback;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getTemplateCustom() {
		return templateCustom;
	}

	public void setTemplateCustom(Integer templateCustom) {
		this.templateCustom = templateCustom;
	}

	public Integer getPlayCodeCustom() {
		return playCodeCustom;
	}

	public void setPlayCodeCustom(Integer playCodeCustom) {
		this.playCodeCustom = playCodeCustom;
	}

	public String getCodePrefix() {
		return codePrefix;
	}

	public void setCodePrefix(String codePrefix) {
		this.codePrefix = codePrefix;
	}

	public String getCorrelatePrefix() {
		return correlatePrefix;
	}

	public void setCorrelatePrefix(String correlatePrefix) {
		this.correlatePrefix = correlatePrefix;
	}

}
