package com.ai.cms.media.bean;

import java.util.ArrayList;
import java.util.List;

import com.ai.common.bean.BaseResult;

public class BatchUploadResult extends BaseResult {

	private List<UploadImageBean> files = new ArrayList<UploadImageBean>();

	public List<UploadImageBean> getFiles() {
		return files;
	}

	public void setFiles(List<UploadImageBean> files) {
		this.files = files;
	}

}
