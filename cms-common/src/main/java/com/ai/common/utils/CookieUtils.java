package com.ai.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CookieUtils {
	public static Logger logger = LoggerFactory.getLogger(CookieUtils.class);

	public static void addCookie(HttpServletResponse response, String name,
			String value, int maxAge, String path, int encodeType) {
		if (StringUtils.isEmpty(name)) {
			return;
		}
		String saveValue = StringUtils.trimToEmpty(value);
		if (encodeType == 1) {
			saveValue = Base64.encodeBase64URLSafeString(saveValue.getBytes());
		}
		Cookie cookie = new Cookie(name, saveValue);
		cookie.setPath(path);
		if (maxAge > 0) {
			cookie.setMaxAge(maxAge);
		}
		logger.debug("save cookie " + name + "=" + value);
		response.addCookie(cookie);
	}

	public static Cookie getCookieByName(HttpServletRequest request, String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		Map<String, Cookie> cookieMap = getCookieMap(request);
		if (cookieMap.containsKey(name)) {
			Cookie cookie = (Cookie) cookieMap.get(name);
			return cookie;
		} else {
			return null;
		}
	}

	private static Map<String, Cookie> getCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}

	public static String getCookieValueByName(HttpServletRequest request,
			String name) {
		return getCookieValueByName(request, name, 0);
	}

	public static String getCookieValueByName(HttpServletRequest request,
			String name, int encodeType) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		Cookie cookie = getCookieByName(request, name);
		String value = (cookie != null) ? cookie.getValue() : "";
		if (encodeType == 1) {
			value = new String(Base64.decodeBase64(value));
		}
		logger.debug("get cookie " + name + "=" + value);
		return value;
	}

	public static void clearCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String path) {
		try {
			Cookie cookie = getCookieByName(request, name);
			if (cookie != null) {
				cookie.setMaxAge(0);
				cookie.setPath(path);
				response.addCookie(cookie);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void clearAllCookie(HttpServletRequest request,
			HttpServletResponse response, String path) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return;
		}
		try {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie != null) {
					cookie.setMaxAge(0);
					cookie.setPath(path);
					response.addCookie(cookie);
				}
			}
		} catch (Exception e) {
			logger.error("clearAllCookie error:", e);
		}
	}
	
	public static void clearOtherCookie(HttpServletRequest request,
			HttpServletResponse response, String path, List<String> retainKeys) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return;
		}
		try {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie != null) {
					if (retainKeys != null && retainKeys.size() > 0) {
						if (retainKeys.contains(cookie.getName())) {
							continue;
						}
					}
					cookie.setMaxAge(0);
					cookie.setPath(path);
					response.addCookie(cookie);
				}
			}
		} catch (Exception e) {
			logger.error("clearOtherCookie error:", e);
		}
	}

}
