package com.ai.epg.uri.bean;

import com.ai.common.enums.TemplateParamCategoryEnum;
import com.ai.common.enums.TemplateParamModeEnum;

public class UriParamBean {
	private Long id;

	private Long itemId;

	private String number;// 编号

	private Integer type;// 类型

	private Integer mode = TemplateParamModeEnum.REQUIRED.getKey();// 模式

	private Integer internalParamCategory = TemplateParamCategoryEnum.MAIN
			.getKey();// 参数分类

	private String typeName;// 类型名称

	private String modeName;// 模式名称

	private String style;// 样式

	private String styleConfig;// 样式配置

	private String code;// 代码

	private String name;// 名称

	private String description;// 描述

	private Integer configCode; // 代码可配置

	private String configItemTypes;// 可配置的元素类型

	private Integer configImage1; // 横海报可配置

	private Integer configImage1Width;

	private Integer configImage1Height;

	private Integer configImage2; // 竖海报可配置

	private Integer configImage2Width;

	private Integer configImage2Height;

	private boolean isExist;

	private Integer autoCreateItem;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public Integer getInternalParamCategory() {
		return internalParamCategory;
	}

	public void setInternalParamCategory(Integer internalParamCategory) {
		this.internalParamCategory = internalParamCategory;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getModeName() {
		return modeName;
	}

	public void setModeName(String modeName) {
		this.modeName = modeName;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStyleConfig() {
		return styleConfig;
	}

	public void setStyleConfig(String styleConfig) {
		this.styleConfig = styleConfig;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getConfigCode() {
		return configCode;
	}

	public void setConfigCode(Integer configCode) {
		this.configCode = configCode;
	}

	public String getConfigItemTypes() {
		return configItemTypes;
	}

	public void setConfigItemTypes(String configItemTypes) {
		this.configItemTypes = configItemTypes;
	}

	public Integer getConfigImage1() {
		return configImage1;
	}

	public void setConfigImage1(Integer configImage1) {
		this.configImage1 = configImage1;
	}

	public Integer getConfigImage1Width() {
		return configImage1Width;
	}

	public void setConfigImage1Width(Integer configImage1Width) {
		this.configImage1Width = configImage1Width;
	}

	public Integer getConfigImage1Height() {
		return configImage1Height;
	}

	public void setConfigImage1Height(Integer configImage1Height) {
		this.configImage1Height = configImage1Height;
	}

	public Integer getConfigImage2() {
		return configImage2;
	}

	public void setConfigImage2(Integer configImage2) {
		this.configImage2 = configImage2;
	}

	public Integer getConfigImage2Width() {
		return configImage2Width;
	}

	public void setConfigImage2Width(Integer configImage2Width) {
		this.configImage2Width = configImage2Width;
	}

	public Integer getConfigImage2Height() {
		return configImage2Height;
	}

	public void setConfigImage2Height(Integer configImage2Height) {
		this.configImage2Height = configImage2Height;
	}

	public boolean isExist() {
		return isExist;
	}

	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}

	public Integer getAutoCreateItem() {
		return autoCreateItem;
	}

	public void setAutoCreateItem(Integer autoCreateItem) {
		this.autoCreateItem = autoCreateItem;
	}

}
