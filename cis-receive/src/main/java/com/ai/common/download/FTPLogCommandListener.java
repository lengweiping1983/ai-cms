package com.ai.common.download;

import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.slf4j.Logger;

public class FTPLogCommandListener implements ProtocolCommandListener {
	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");
	private Logger logger = null;

	public FTPLogCommandListener(Logger logger) {
		this.logger = logger;
	}

	public void protocolCommandSent(ProtocolCommandEvent event) {
		if (logger.isDebugEnabled() && event.getMessage() != null) {
			logger.debug(event.getMessage().replace(LINE_SEPARATOR, ""));
		}
	}

	public void protocolReplyReceived(ProtocolCommandEvent event) {
		if (logger.isDebugEnabled() && event.getMessage() != null) {
			logger.debug(event.getMessage().replace(LINE_SEPARATOR, ""));
		}
	}
}
