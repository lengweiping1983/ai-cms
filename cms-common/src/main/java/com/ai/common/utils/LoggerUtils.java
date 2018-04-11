package com.ai.common.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ai.common.exception.ServiceException;
import com.ai.common.exception.ValidateException;

public class LoggerUtils {

	public static void infoEntrance(String param, Logger logger, String message) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		LoggerInfoObject loggerInfoObject = new LoggerInfoObject();
		loggerInfoObject.setIp(IpUtils.getIpAddr(request));
		loggerInfoObject.setParam(param);
		request.setAttribute("contextLoggerInfoObject", loggerInfoObject);

		logger.info(loggerInfoObject.toString() + "["
				+ request.getServletPath() + "]" + "{"
				+ StringUtils.trimToEmpty(message) + "}");
	}

	public static void infoMessage(Logger logger, String message) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		LoggerInfoObject loggerInfoObject = (LoggerInfoObject) request
				.getAttribute("contextLoggerInfoObject");

		if (loggerInfoObject != null) {
			logger.info(loggerInfoObject.toString() + "["
					+ request.getServletPath() + "]" + "{" + message + "}");
		} else {
			logger.info("[" + request.getServletPath() + "]" + "{" + message
					+ "}");
		}
	}

	public static void errorMessage(Logger logger, String message) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		LoggerInfoObject loggerInfoObject = (LoggerInfoObject) request
				.getAttribute("contextLoggerInfoObject");

		if (loggerInfoObject != null) {
			logger.error(loggerInfoObject.toString() + "["
					+ request.getServletPath() + "]" + "{" + message + "}");
		} else {
			logger.error("[" + request.getServletPath() + "]" + "{" + message
					+ "}");
		}
	}

	public static void errorMessage(Logger logger, String message, Exception e) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		LoggerInfoObject loggerInfoObject = (LoggerInfoObject) request
				.getAttribute("contextLoggerInfoObject");

		if (loggerInfoObject != null) {
			logger.error(
					loggerInfoObject.toString() + "["
							+ request.getServletPath() + "]" + "{" + message
							+ "}", e);
		} else {
			logger.error("[" + request.getServletPath() + "]" + "{" + message
					+ "}", e);
		}
	}

	public static void errorMessage(Logger logger, int errorCode,
			String errorMessage) {
		String message = "<errorCode=" + errorCode + "><errorMessage="
				+ errorMessage + ">";
		errorMessage(logger, message);
	}

	public static void errorMessage(Logger logger, int errorCode,
			String errorMessage, Exception e) {
		String message = "<errorCode=" + errorCode + "><errorMessage="
				+ errorMessage + ">";
		errorMessage(logger, message, e);
	}

	public static void errorMessage(Logger logger, ServiceException e) {
		String message = "<errorCode=" + e.getCode() + "><errorMessage="
				+ e.getMessage() + ">";
		errorMessage(logger, message, e);
	}

	public static void errorMessage(Logger logger, ValidateException e) {
		String message = "<errorCode=" + e.getResult() + "><errorMessage="
				+ e.getMessage() + ">";
		errorMessage(logger, message, e);
	}

	public static void errorMessage(Logger logger, Exception e) {
		String message = "<errorMessage=" + e.getMessage() + ">";
		errorMessage(logger, message, e);
	}
}
