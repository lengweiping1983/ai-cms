package com.ai.epg.uri.bean;

import java.util.ArrayList;
import java.util.List;

import com.ai.cms.media.bean.ImageBean;

public class UriImageBean<T> extends ImageBean<T> {

	private List<UriParamBean> params = new ArrayList<UriParamBean>();

	public List<UriParamBean> getParams() {
		return params;
	}

	public void setParams(List<UriParamBean> params) {
		this.params = params;
	}

}
