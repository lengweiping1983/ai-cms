package com.ai.common.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

public class LoggerInfoObject {
	String transaction = RandomStringUtils.randomAlphanumeric(12);
	String ip;
	String param;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	@Override
	public String toString() {
		return "[" + transaction + "][" + ip + "]" + "["
				+ StringUtils.trimToEmpty(param) + "]";
	}

}
