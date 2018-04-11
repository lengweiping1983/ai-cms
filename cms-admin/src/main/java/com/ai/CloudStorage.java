package com.ai;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CloudStorage {
	public static String clientId;

	public static String password;

	public static String url = "http://116.211.105.148/cig";

	public static String filePathPrefix;

	public static String videoDownloadUrl;

	public static String getVideoDownloadUrl() {
		return videoDownloadUrl;
	}

	@Value("${cloud.video.download.url:}")
	public void setVideoDownloadUrl(String videoDownloadUrl) {
		CloudStorage.videoDownloadUrl = videoDownloadUrl;
	}

	public static String getMediaFilePath(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}
		if (path.indexOf("http") == 0) {
			return path;
		}
		if (path.indexOf("ftp") == 0) {
			return path;
		}
		return AdminGlobal.joinPath(getVideoDownloadUrl(), path);
	}

	public static String getClientId() {
		return clientId;
	}

	@Value("${cloud.storage.clientId:}")
	public void setClientId(String clientId) {
		CloudStorage.clientId = clientId;
	}

	public static String getPassword() {
		return password;
	}

	@Value("${cloud.storage.password:}")
	public void setPassword(String password) {
		CloudStorage.password = password;
	}

	public static String getUrl() {
		return url;
	}

	@Value("${cloud.storage.url:}")
	public void setUrl(String url) {
		CloudStorage.url = url;
	}

	public static String getFilePathPrefix() {
		return filePathPrefix;
	}

	@Value("${cloud.storage.filePathPrefix:}")
	public void setFilePathPrefix(String filePathPrefix) {
		CloudStorage.filePathPrefix = filePathPrefix;
	}

}
