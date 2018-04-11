package com.ai.epg.template.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ai.common.entity.AbstractEntity;
import com.ai.common.enums.TemplateParamCategoryEnum;
import com.ai.common.enums.TemplateParamModeEnum;
import com.ai.common.enums.TemplateParamTypeEnum;
import com.ai.common.enums.YesNoEnum;

/**
 * 模板对应的参数配置
 * 
 */
@Entity
@Table(name = "epg_template_param")
public class TemplateParam extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "template_id")
	private Long templateId;

	@Column(name = "number")
	private String number;// 编号

	@Column(name = "type")
	private Integer type = TemplateParamTypeEnum.WIDGET.getKey();// 类型

	@Column(name = "code")
	private String code;// 代码

	@Column(name = "name")
	private String name;// 名称

	@Column(name = "mode")
	private Integer mode = TemplateParamModeEnum.REQUIRED.getKey();// 模式

	@Column(name = "internal_param_category")
	private Integer internalParamCategory = TemplateParamCategoryEnum.MAIN
			.getKey();// 参数分类

	@Column(name = "style_config")
	private String styleConfig;// 样式配置

	@Column(name = "description")
	private String description;// 描述

	@Column(name = "config_code")
	private Integer configCode = YesNoEnum.YES.getKey(); // 代码可配置

	@Column(name = "widget_type")
	private Integer widgetType; // 子类型

	@Column(name = "widget_item_num")
	private Integer widgetItemNum = 1; // 显示元素个数

	@Column(name = "config_item_types")
	private String configItemTypes;// 可配置的元素类型

	@Column(name = "config_image1")
	private Integer configImage1 = YesNoEnum.YES.getKey(); // 横海报可配置

	@Column(name = "config_image1_width")
	private Integer configImage1Width;

	@Column(name = "config_image1_height")
	private Integer configImage1Height;

	@Column(name = "config_image2")
	private Integer configImage2 = YesNoEnum.NO.getKey(); // 竖海报可配置

	@Column(name = "config_image2_width")
	private Integer configImage2Width;

	@Column(name = "config_image2_height")
	private Integer configImage2Height;

	@Column(name = "sort_index")
	private Integer sortIndex = 1; // 排序值

	@Column(name = "position_id")
	private String positionId;// 位置Id

	@Column(name = "position")
	private String position;// 位置信息

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
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

	public String getStyleConfig() {
		return styleConfig;
	}

	public void setStyleConfig(String styleConfig) {
		this.styleConfig = styleConfig;
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

	public Integer getWidgetType() {
		return widgetType;
	}

	public void setWidgetType(Integer widgetType) {
		this.widgetType = widgetType;
	}

	public Integer getWidgetItemNum() {
		return widgetItemNum;
	}

	public void setWidgetItemNum(Integer widgetItemNum) {
		this.widgetItemNum = widgetItemNum;
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

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}