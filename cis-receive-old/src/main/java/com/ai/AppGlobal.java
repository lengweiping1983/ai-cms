package com.ai;

import org.springframework.stereotype.Component;

import com.ai.common.enums.MediaStatusEnum;

@Component
public class AppGlobal {

//	private static final Logger logger = LoggerFactory
//			.getLogger(AppGlobal.class);
	
	public static String getMediaStatusDesc(int mediaStatus) {
		StringBuffer sb = new StringBuffer();
		if (mediaStatus == MediaStatusEnum.OK.getKey()) {
			sb.append("<span class=\"badge badge-success\">")
					.append(MediaStatusEnum.OK.getValue()).append("</span>");
			return sb.toString();
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
}
