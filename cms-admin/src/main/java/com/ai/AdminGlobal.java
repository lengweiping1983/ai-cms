package com.ai;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ai.common.enums.MediaStatusEnum;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.utils.PathUtils;

@Component
public class AdminGlobal implements CommandLineRunner {

	private static final Logger logger = LoggerFactory
			.getLogger(AdminGlobal.class);

	public static double SIZE_KB = 1024;
	public static double SIZE_MB = 1024 * 1024;
	public static double SIZE_GB = 1024 * 1024 * 1024;
	public static java.text.DecimalFormat df = new java.text.DecimalFormat(
			"#.#");

	public static ThreadLocal<String> operationLogAction = new ThreadLocal<String>();
	public static ThreadLocal<String> operationLogActionResult = new ThreadLocal<String>();
	public static ThreadLocal<String> operationLogMessage = new ThreadLocal<String>();
	public static ThreadLocal<String> operationLogTypeAndId = new ThreadLocal<String>();

	public static String siteCode;

	public static boolean injectionEnabled = false;

	public static boolean injectionMedia = false;

	public static boolean injectionLive = false;

	public static boolean injectionCategory = false;

	public static boolean injectionService = false;

	public static String getSiteCodePath() {
		return siteCode;
	}

	public static String getSiteCode() {
		if ("center".equalsIgnoreCase(siteCode)) {
			return "";
		}
		return siteCode;
	}

	@Value("${site.code}")
	public void setSiteCode(String siteCode) {
		AdminGlobal.siteCode = siteCode;
	}

	public static boolean isInjectionEnabled() {
		return injectionEnabled;
	}

	@Value("${injection.enabled:false}")
	public void setInjectionEnabled(boolean injectionEnabled) {
		AdminGlobal.injectionEnabled = injectionEnabled;
	}

	public static boolean isInjectionMedia() {
		return injectionMedia;
	}

	@Value("${injection.media:false}")
	public void setInjectionMedia(boolean injectionMedia) {
		AdminGlobal.injectionMedia = injectionMedia;
	}

	public static boolean isInjectionLive() {
		return injectionLive;
	}

	@Value("${injection.live:false}")
	public void setInjectionLive(boolean injectionLive) {
		AdminGlobal.injectionLive = injectionLive;
	}

	public static boolean isInjectionCategory() {
		return injectionCategory;
	}

	@Value("${injection.category:false}")
	public void setInjectionCategory(boolean injectionCategory) {
		AdminGlobal.injectionCategory = injectionCategory;
	}

	public static boolean isInjectionService() {
		return injectionService;
	}

	@Value("${injection.service:false}")
	public void setInjectionService(boolean injectionService) {
		AdminGlobal.injectionService = injectionService;
	}

	public static String getStatusMothodName(int status) {
		OnlineStatusEnum statusEnum = OnlineStatusEnum.getEnumByKey(status);
		switch (statusEnum) {
		case ONLINE:
			return "下线";
		default:
			return "上线";
		}
	}

	public static String getMediaStatusDesc(int mediaStatus) {
		StringBuffer sb = new StringBuffer();
		if (mediaStatus == MediaStatusEnum.OK.getKey()) {
			sb.append("<span class=\"badge badge-success\">")
					.append(MediaStatusEnum.OK.getValue()).append("</span>");
			return sb.toString();
		}
		if (isMediaStatusOpen(mediaStatus, MediaStatusEnum.TRANSCODE.getKey())) {
			sb.append("<span class=\"badge badge-info\">")
					.append(MediaStatusEnum.TRANSCODE.getValue())
					.append("</span>");
		}

		if (isMediaStatusOpen(mediaStatus,
				MediaStatusEnum.TRANSCODE_FAIL.getKey())) {
			sb.append("<span class=\"badge badge-danger\">")
					.append(MediaStatusEnum.TRANSCODE_FAIL.getValue())
					.append("</span>");
		}

		if (isMediaStatusOpen(mediaStatus,
				MediaStatusEnum.TRANSCODE_PART_FAIL.getKey())) {
			sb.append("<span class=\"badge badge-danger\">")
					.append(MediaStatusEnum.TRANSCODE_PART_FAIL.getValue())
					.append("</span>");
		}

		if (isMediaStatusOpen(mediaStatus, MediaStatusEnum.DOWNLOAD.getKey())) {
			sb.append("<span class=\"badge badge-info\">")
					.append(MediaStatusEnum.DOWNLOAD.getValue())
					.append("</span>");
		}

		if (isMediaStatusOpen(mediaStatus,
				MediaStatusEnum.DOWNLOAD_FAIL.getKey())) {
			sb.append("<span class=\"badge badge-danger\">")
					.append(MediaStatusEnum.DOWNLOAD_FAIL.getValue())
					.append("</span>");
		}

		if (isMediaStatusOpen(mediaStatus,
				MediaStatusEnum.DOWNLOAD_PART_FAIL.getKey())) {
			sb.append("<span class=\"badge badge-danger\">")
					.append(MediaStatusEnum.DOWNLOAD_PART_FAIL.getValue())
					.append("</span>");
		}

		if (isMediaStatusOpen(mediaStatus, MediaStatusEnum.MISS_VIDEO.getKey())) {
			sb.append("<span class=\"badge badge-danger\">")
					.append(MediaStatusEnum.MISS_VIDEO.getValue())
					.append("</span>");
		}

		if (isMediaStatusOpen(mediaStatus, MediaStatusEnum.MISS_IMAGE.getKey())) {
			sb.append("<span class=\"badge badge-danger\">")
					.append(MediaStatusEnum.MISS_IMAGE.getValue())
					.append("</span>");
		}

		return sb.toString();
	}

	public static boolean isMediaStatusOpen(int mediaStatus, int mask) {
		if (mask <= 0) {
			return false;
		}
		int index = (mediaStatus / mask);
		if (index % 10 == 1) {
			return true;
		}
		return false;
	}

	public static int setMediaStatusOpen(int mediaStatus, int... masks) {
		int result = mediaStatus;
		for (int mask : masks) {
			if (!isMediaStatusOpen(result, mask)) {
				result += mask;
			}
		}
		return result;
	}

	public static int setMediaStatusClose(int mediaStatus, int... masks) {
		int result = mediaStatus;
		for (int mask : masks) {
			if (isMediaStatusOpen(result, mask)) {
				result -= mask;
			}
		}
		return result;
	}

	public static boolean isStatusOpen(int status, int mask) {
		if (mask <= 0) {
			return false;
		}
		int index = (status / mask);
		if (index % 10 == 1) {
			return true;
		}
		return false;
	}

	public static int setStatusOpen(int status, int... masks) {
		int result = status;
		for (int mask : masks) {
			if (!isStatusOpen(result, mask)) {
				result += mask;
			}
		}
		return result;
	}

	public static int setStatusClose(int status, int... masks) {
		int result = status;
		for (int mask : masks) {
			if (isStatusOpen(result, mask)) {
				result -= mask;
			}
		}
		return result;
	}

	public static String getFileSizeS(java.lang.Long fileSize) {
		if (fileSize == null) {
			return "";
		}
		if (fileSize > SIZE_GB) {
			return (df.format(fileSize / SIZE_GB) + "GB");
		} else if (fileSize > SIZE_MB) {
			return (df.format(fileSize / SIZE_MB) + "MB");
		} else {
			return (df.format(fileSize / SIZE_KB) + "KB");
		}
	}

	public static String ftpAddress;

	public static String ftpMode;

	public static String ftpRootPath;

	public static String ftpDefaultAccessPath;

	public static String transcodeOutputPath;

	public static String collectOutputPath;

	public static String getFtpAddress() {
		return ftpAddress;
	}

	@Value("${ftp.address:}")
	public void setFtpAddress(String ftpAddress) {
		AdminGlobal.ftpAddress = ftpAddress;
	}

	public static String getFtpMode() {
		return ftpMode;
	}

	@Value("${ftp.mode:}")
	public void setFtpMode(String ftpMode) {
		AdminGlobal.ftpMode = ftpMode;
	}

	public static String getFtpRootPath() {
		return ftpRootPath;
	}

	@Value("${ftp.root.path:/}")
	public void setFtpRootPath(String ftpRootPath) {
		AdminGlobal.ftpRootPath = PathUtils.getFilePath(ftpRootPath);
	}

	public static String getFtpDefaultAccessPath() {
		return ftpDefaultAccessPath;
	}

	@Value("${ftp.default.access.path:/}")
	public void setFtpDefaultAccessPath(String ftpDefaultAccessPath) {
		AdminGlobal.ftpDefaultAccessPath = ftpDefaultAccessPath;
	}

	public static String getTranscodeOutputPath() {
		return transcodeOutputPath;
	}

	@Value("${transcode.output.path:transcode}")
	public void setTranscodeOutputPath(String transcodeOutputPath) {
		AdminGlobal.transcodeOutputPath = PathUtils
				.getFilePath(transcodeOutputPath);
	}

	public static String getCollectOutputPath() {
		return collectOutputPath;
	}

	@Value("${collect.output.path:download}")
	public void setCollectOutputPath(String collectOutputPath) {
		AdminGlobal.collectOutputPath = collectOutputPath;
	}

	@Override
	public void run(String... args) {
		logger.info("AdminGlobal startup load data ...");
		loadData();
	}

	@Scheduled(cron = "${load.data.schedule:0 0/10 * * * ?}")
	public void execute() {
		logger.info("AdminGlobal load data ...");
		loadData();
	}

	private void loadData() {
		try {
		} catch (Exception e) {

		}
	}

	public static String getImagePath(String path) {
		return getImageWebPath(path);
	}

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

	public static String m3u8UploadPath;

	public static String m3u8WebPath;

	public static String m3u8FtpPath;

	public static String getM3U8UploadPath() {
		return m3u8UploadPath;
	}

	@Value("${m3u8.upload.path:}")
	public void setM3U8UploadPath(String m3u8UploadPath) {
		AdminGlobal.m3u8UploadPath = m3u8UploadPath;
	}

	public static String getM3U8WebPath() {
		return m3u8WebPath;
	}

	@Value("${m3u8.web.path:}")
	public void setM3U8WebPath(String m3u8WebPath) {
		AdminGlobal.m3u8WebPath = m3u8WebPath;
	}

	public static String getM3U8FtpPath() {
		return m3u8FtpPath;
	}

	@Value("${m3u8.ftp.path:}")
	public void setM3U8FtpPath(String m3u8FtpPath) {
		AdminGlobal.m3u8FtpPath = m3u8FtpPath;
	}

	public static String getM3U8UploadPath(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}
		if (path.indexOf("http") == 0) {
			return path;
		}
		if (path.indexOf("ftp") == 0) {
			return path;
		}
		return joinPath(getM3U8UploadPath(), path);
	}

	public static String getM3U8WebPath(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}
		if (path.indexOf("http") == 0) {
			return path;
		}
		if (path.indexOf("ftp") == 0) {
			return path;
		}
		return joinPath(getM3U8WebPath(), path);
	}

	public static String getM3U8FtpPath(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}
		if (path.indexOf("http") == 0) {
			return path;
		}
		if (path.indexOf("ftp") == 0) {
			return path;
		}
		return joinPath(getM3U8FtpPath(), path);
	}

	public static String webAccessUrl;

	public static String getWebAccessUrl() {
		return webAccessUrl;
	}

	@Value("${web.access.url:}")
	public void setWebAccessUrl(String webAccessUrl) {
		AdminGlobal.webAccessUrl = webAccessUrl;
	}

}
