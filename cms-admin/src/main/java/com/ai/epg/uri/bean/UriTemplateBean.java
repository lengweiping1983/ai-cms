package com.ai.epg.uri.bean;

import java.util.ArrayList;
import java.util.List;

import com.ai.common.enums.YesNoEnum;

public class UriTemplateBean {

	private Long id;// 模板id

	private String code;// 模板代码

	private String name;// 模板名称

	private Integer configBackgroundImage = YesNoEnum.NO.getKey(); // 背景图片可配置

	private String backgroundImage;// 效果图片

	private boolean dynamicCategory;

	private Integer internalParamCategory;// 参数分类

	private List<UriParamBean> params = new ArrayList<UriParamBean>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getConfigBackgroundImage() {
		return configBackgroundImage;
	}

	public void setConfigBackgroundImage(Integer configBackgroundImage) {
		this.configBackgroundImage = configBackgroundImage;
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public boolean isDynamicCategory() {
		return dynamicCategory;
	}

	public void setDynamicCategory(boolean dynamicCategory) {
		this.dynamicCategory = dynamicCategory;
	}

	public Integer getInternalParamCategory() {
		return internalParamCategory;
	}

	public void setInternalParamCategory(Integer internalParamCategory) {
		this.internalParamCategory = internalParamCategory;
	}

	public List<UriParamBean> getParams() {
		return params;
	}

	public void setParams(List<UriParamBean> params) {
		this.params = params;
	}

}
