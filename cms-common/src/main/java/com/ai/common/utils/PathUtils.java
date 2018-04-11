package com.ai.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

public class PathUtils {

	/**
	 * 获取文件路径，以'/'开头，结尾不包括'/'
	 */
	public static String getFilePath(final String sourcePath) {
		String path = StringUtils.trimToEmpty(sourcePath);
		if (StringUtils.isEmpty(path) || path.charAt(0) != '/') {
			path = "/" + path;
		}
		if (!"/".equals(path) && path.charAt(path.length() - 1) == '/') {
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}

	/**
	 * 连接两个路径
	 */
	public static String joinPath(String sourceRootPath, String sourceFilePath) {
		String rootPath = getFilePath(sourceRootPath);
		String filePath = getFilePath(sourceFilePath);
		if (StringUtils.isNotEmpty(rootPath)
				&& rootPath.charAt(rootPath.length() - 1) == '/') {
			rootPath = rootPath.substring(0, rootPath.length() - 1);
		}
		return rootPath + filePath;
	}

	public static String joinUrl(String url, String path) {
		if (StringUtils.isEmpty(path)) {
			return url;
		}
		String symbol = "";
		if (url.contains("?")) {
			symbol = "&";
		} else {
			symbol = "?";
		}
		return url + symbol + path;
	}

	public static String getJoinSymbol(String url) {
		String symbol = "";
		if (url != null && url.contains("?")) {
			symbol = "&";
		} else {
			symbol = "?";
		}
		return symbol;
	}

	public static String getJoinSymbol(StringBuffer url) {
		return getJoinSymbol(url.toString());
	}

	public static String urlEncode(String source, String charset) {
		if (StringUtils.isEmpty(source)) {
			return "";
		}
		try {
			return java.net.URLEncoder.encode(source, charset);
		} catch (Exception e) {
			return "";
		}
	}

	public static String urlEncodeGBK(String source) {
		return urlEncode(source, "GBK");
	}

	public static String urlEncodeUTF8(String source) {
		return urlEncode(source, "UTF-8");
	}

	public static String urlDecode(String source, String charset) {
		if (StringUtils.isEmpty(source)) {
			return "";
		}
		try {
			return java.net.URLDecoder.decode(source, charset);
		} catch (Exception e) {
			return "";
		}
	}

	public static String urlDecodeGBK(String source) {
		return urlDecode(source, "GBK");
	}

	public static String urlDecodeUTF8(String source) {
		return urlDecode(source, "UTF-8");
	}

	public static String getTagParamValueByName(String INFO, String name) {
		if (StringUtils.isEmpty(INFO)) {
			return "";
		}
		Pattern p = Pattern.compile("<" + name + ">([^<]+)</" + name + ">");
		Matcher m = p.matcher(INFO);
		if (m.find()) {
			return m.group(1);
		}
		return "";
	}

	public static JSONObject getJsonByTagParam(String INFO) {
		JSONObject json = new JSONObject();
		if (StringUtils.isNotEmpty(INFO)) {
			Pattern p = Pattern.compile("<((?:(?)\\w)+?)>");
			Matcher m = p.matcher(INFO);
			while (m.find()) {
				String name = m.group(1);
				String value = getTagParamValueByName(INFO, name);
				json.put(name, value);
			}
		}
		return json;
	}

	public static String addParamToTag(String name, String value) {
		if (StringUtils.isEmpty(name)) {
			return "";
		}
		if (StringUtils.isEmpty(value)) {
			value = "";
		}
		String param = "<" + StringUtils.trimToEmpty(name) + ">"
				+ StringUtils.trimToEmpty(value) + "</"
				+ StringUtils.trimToEmpty(name) + ">";
		return param;
	}
}
