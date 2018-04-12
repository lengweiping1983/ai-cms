package com.ai.common.transfer;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.common.exception.DataException;

public abstract class AbstractFTPClient {
	private static final Logger logger = LoggerFactory
			.getLogger(AbstractFTPClient.class);
	private static final String REGEX_FTP_URL = "^ftp://(.*):(.*)@(\\d+\\.\\d+\\.\\d+\\.\\d+):?(\\d+)?(/.+[^/])$";
	private static final String STR_DIR_SEPARATOR = "/";

	protected TaskCallback callback;
	protected long taskId;

	private int retryTimes = 0;
	private int currentRetryTimes = 0;
	private long retryWaitTime = 1L * 1000L;
	private boolean resumeBroken = true;
	private boolean renew = false;

	public int getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}

	public int getCurrentRetryTimes() {
		return currentRetryTimes;
	}

	public long getRetryWaitTime() {
		return retryWaitTime / 1000L;
	}

	public void setRetryWaitTime(long retryWaitTime) {
		this.retryWaitTime = retryWaitTime * 1000L;
	}

	public boolean isResumeBroken() {
		return resumeBroken;
	}

	public void setResumeBroken(boolean resumeBroken) {
		this.resumeBroken = resumeBroken;
	}

	public boolean isRenew() {
		return renew;
	}

	public void setRenew(boolean renew) {
		this.renew = renew;
	}

	protected boolean doTransferFlow() {
		boolean result = false;
		while (true) {
			try {
				if (!connect()) {
					continue;
				}

				if (!login()) {
					continue;
				}

				if (!doTransfer()) {
					continue;
				} else {
					result = true;
				}
			} catch (DataException e) {
				logger.error(e.getMessage(), e);
				callback.fail(taskId, currentRetryTimes, e.getMessage());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				callback.fail(taskId, currentRetryTimes, e.getMessage());
			} finally {
				logoutAndDisconnect();
				if (!result && shouldRetry()) {
					waitForRetry();
				} else {
					return result;
				}
			}
		}
	}

	protected abstract boolean connect() throws IOException;

	protected abstract boolean login() throws IOException;

	protected abstract void logoutAndDisconnect();

	protected abstract boolean doTransfer() throws IOException;

	public static FTPFileInfoVO convertFTPUrlToVO(String url) {
		FTPFileInfoVO vo = new FTPFileInfoVO();
		Matcher matcher = Pattern.compile(REGEX_FTP_URL).matcher(url);
		if (matcher.find()) {
			vo.setUserName(matcher.group(1));
			vo.setPassword(matcher.group(2));
			vo.setIp(matcher.group(3));
			vo.setPort(matcher.group(4));
			vo.setUrl(url);
			String fileFullPath = matcher.group(5);
			vo.setFileFullPath(fileFullPath);
			if (fileFullPath.lastIndexOf(STR_DIR_SEPARATOR) > 0) {
				vo.setFileDirectory(fileFullPath.substring(0,
						fileFullPath.lastIndexOf(STR_DIR_SEPARATOR)));
				vo.setFileName(fileFullPath.substring(fileFullPath
						.lastIndexOf(STR_DIR_SEPARATOR) + 1));
			} else {
				vo.setFileDirectory(null);
				vo.setFileName(fileFullPath);
			}
			return vo;
		}
		throw new DataException(url + "is invalid.");
	}

	protected boolean connect(FTPClient ftp, FTPFileInfoVO vo)
			throws IOException {
		if (vo.getPort() == null || vo.getPort().length() == 0) {
			ftp.connect(vo.getIp());
		} else {
			ftp.connect(vo.getIp(), Integer.parseInt(vo.getPort()));
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Connected to " + vo.getIp() + " on "
					+ ftp.getRemotePort());
		}
		if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
			logger.error("FTP server refused connection.");
			return false;
		}
		return true;
	}

	protected boolean changeDir(FTPClient ftp, String path) {
		if (path == null || path.length() == 0) {
			return true;
		}
		try {
			if (!ftp.changeWorkingDirectory(STR_DIR_SEPARATOR)) {
				return false;
			}
			String[] dirs = path.split(STR_DIR_SEPARATOR);
			for (String dir : dirs) {
				if (dir.length() == 0) {
					continue;
				}
				if (!ftp.changeWorkingDirectory(dir)) {
					if (ftp.makeDirectory(dir)
							&& ftp.changeWorkingDirectory(dir)) {
						continue;
					} else {
						return false;
					}
				}
			}
		} catch (IOException e) {
			logger.error("Failed to ChangeDir to " + path + " .", e);
			return false;
		}
		return true;
	}

	protected boolean shouldRetry() {
		if (currentRetryTimes < retryTimes) {
			currentRetryTimes++;
			return true;
		} else {
			return false;
		}
	}

	protected void waitForRetry() {
		try {
			Thread.sleep(retryWaitTime);
		} catch (InterruptedException e) {
			logger.error("WaitForRetry failed.", e);
		}
	}
}
