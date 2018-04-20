package com.ai;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AdminGlobal {
	public static String imageUploadPath;

	public static String imageWebPath;

	public static String imageFtpPath;

	public static String getImageUploadPath() {
		return StringUtils.trimToEmpty(imageUploadPath);
	}

	@Value("${image.upload.path:}")
	public void setImageUploadPath(String imageUploadPath) {
		AdminGlobal.imageUploadPath = imageUploadPath;
	}

	public static String getImageWebPath() {
		return StringUtils.trimToEmpty(imageWebPath);
	}

	@Value("${image.web.path:}")
	public void setImageWebPath(String imageWebPath) {
		AdminGlobal.imageWebPath = imageWebPath;
	}

	public static String getImageFtpPath() {
		return imageFtpPath;
	}

	@Value("${image.ftp.path:}")
	public void setImageFtpPath(String imageFtpPath) {
		AdminGlobal.imageFtpPath = imageFtpPath;
	}

	public static String getImageUploadPath(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}
		if (path.indexOf("http") == 0) {
			return path;
		}
		if (path.indexOf("ftp") == 0) {
			return path;
		}
		return joinPath(getImageUploadPath(), path);
	}

	public static String getImageWebPath(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}
		if (path.indexOf("http") == 0) {
			return path;
		}
		if (path.indexOf("ftp") == 0) {
			return path;
		}
		return joinPath(getImageWebPath(), path);
	}

	public static String getImageFtpPath(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}
		if (path.indexOf("http") == 0) {
			return path;
		}
		if (path.indexOf("ftp") == 0) {
			return path;
		}
		return joinPath(getImageFtpPath(), path);
	}

	public static String xmlUploadPath;

	public static String xmlWebPath;

	public static String xmlFtpPath;

	public static String getXmlUploadPath() {
		return xmlUploadPath;
	}

	@Value("${xml.upload.path:}")
	public void setXmlUploadPath(String xmlUploadPath) {
		AdminGlobal.xmlUploadPath = xmlUploadPath;
	}

	public static String getXmlWebPath() {
		return xmlWebPath;
	}

	@Value("${xml.web.path:}")
	public void setXmlWebPath(String xmlWebPath) {
		AdminGlobal.xmlWebPath = xmlWebPath;
	}

	public static String getXmlFtpPath() {
		return xmlFtpPath;
	}

	@Value("${xml.ftp.path:}")
	public void setXmlFtpPath(String xmlFtpPath) {
		AdminGlobal.xmlFtpPath = xmlFtpPath;
	}

	public static String getXmlUploadPath(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}
		if (path.indexOf("http") == 0) {
			return path;
		}
		if (path.indexOf("ftp") == 0) {
			return path;
		}
		return joinPath(getXmlUploadPath(), path);
	}

	public static String getXmlWebPath(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}
		if (path.indexOf("http") == 0) {
			return path;
		}
		if (path.indexOf("ftp") == 0) {
			return path;
		}
		return joinPath(getXmlWebPath(), path);
	}

	public static String getXmlFtpPath(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}
		if (path.indexOf("http") == 0) {
			return path;
		}
		if (path.indexOf("ftp") == 0) {
			return path;
		}
		return joinPath(getXmlFtpPath(), path);
	}

	public static String videoUploadPath;

	public static String videoWebPath;

	public static String videoFtpPath;

	public static String getVideoUploadPath() {
		return videoUploadPath;
	}

	@Value("${video.upload.path:}")
	public void setVideoUploadPath(String videoUploadPath) {
		AdminGlobal.videoUploadPath = videoUploadPath;
	}

	public static String getVideoWebPath() {
		return videoWebPath;
	}

	@Value("${video.web.path:}")
	public void setVideoWebPath(String videoWebPath) {
		AdminGlobal.videoWebPath = videoWebPath;
	}

	public static String getVideoFtpPath() {
		return videoFtpPath;
	}

	@Value("${video.ftp.path:}")
	public void setVideoFtpPath(String videoFtpPath) {
		AdminGlobal.videoFtpPath = videoFtpPath;
	}

	public static String getVideoWebPath(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}
		if (path.indexOf("http") == 0) {
			return path;
		}
		if (path.indexOf("ftp") == 0) {
			return path;
		}
		return joinPath(getVideoWebPath(), path);
	}

	public static String getVideoUploadPath(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}
		if (path.indexOf("http") == 0) {
			return path;
		}
		if (path.indexOf("ftp") == 0) {
			return path;
		}
		return joinPath(getVideoUploadPath(), path);
	}

	public static String getVideoFtpPath(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}
		if (path.indexOf("http") == 0) {
			return path;
		}
		if (path.indexOf("ftp") == 0) {
			return path;
		}
		return joinPath(getVideoFtpPath(), path);
	}

	/**
	 * 连接两个路径
	 */
	public static String joinPath(String sourceRootPath, String sourceFilePath) {
		String rootPath = StringUtils.trimToEmpty(sourceRootPath);
		String filePath = StringUtils.trimToEmpty(sourceFilePath);
		if (StringUtils.isNotEmpty(rootPath)
				&& rootPath.charAt(rootPath.length() - 1) == '/') {
			rootPath = rootPath.substring(0, rootPath.length() - 1);
		}
		if (StringUtils.isEmpty(filePath) || filePath.charAt(0) != '/') {
			filePath = "/" + filePath;
		}
		return rootPath + filePath;
	}
}
