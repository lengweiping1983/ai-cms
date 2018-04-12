package com.ai.common.transfer;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerToServer extends AbstractFTPClient {
	private static final Logger logger = LoggerFactory
			.getLogger(ServerToServer.class);
	private FTPClient ftp1 = null;
	private FTPFileInfoVO ftp1VO = null;

	private FTPClient ftp2 = null;
	private FTPFileInfoVO ftp2VO = null;

	public ServerToServer() {
		ftp1 = new FTPClient();
		ftp2 = new FTPClient();
		ftp1.addProtocolCommandListener(new FTPLogCommandListener(logger));
		ftp2.addProtocolCommandListener(new FTPLogCommandListener(logger));
	}

	public void setControlEncoding(String encoding1, String encoding2) {
		ftp1.setControlEncoding(encoding1);
		ftp2.setControlEncoding(encoding2);
	}

	public boolean transfer(String server1Url, String server2Url) {
		ftp1VO = convertFTPUrlToVO(server1Url);
		ftp2VO = convertFTPUrlToVO(server2Url);
		return doTransferFlow();
	}

	@Override
	protected boolean connect() throws IOException {
		if (!connect(ftp1, ftp1VO)) {
			logger.error("Failed to connect to server1.");
			return false;
		}
		if (!connect(ftp2, ftp2VO)) {
			logger.error("Failed to connect to server2.");
			return false;
		}
		return true;
	}

	@Override
	protected boolean login() throws IOException {
		if (!ftp1.login(ftp1VO.getUserName(), ftp1VO.getPassword())) {
			logger.error("Cannot login to server1.");
			return false;
		}
		if (!ftp2.login(ftp2VO.getUserName(), ftp2VO.getPassword())) {
			logger.error("Cannot login to server2.");
			return false;
		}
		return true;
	}

	@Override
	protected boolean doTransfer() throws IOException {
		FTPFile[] files1 = ftp1.listFiles(ftp1VO.getFileFullPath());
		FTPFile[] files2 = ftp2.listFiles(ftp2VO.getFileFullPath());
		if (files1.length == 0) {
			logger.error("Specified file does not exist in ftp1.");
			return false;
		} else {
			if (files2.length != 0) {
				if (isResumeBroken()) {
					long ftp1Size = files1[0].getSize();
					long ftp2Size = files2[0].getSize();
					if (ftp1Size <= ftp2Size) {
						logger.error("Ftp2 file exists and its size is large or equal than ftp1 file's size.");
						return false;
					} else {
						ftp1.setRestartOffset(ftp2Size);
						ftp2.setRestartOffset(ftp2Size);
					}
				} else {
					if (!ftp2.deleteFile(ftp2VO.getFileFullPath())) {
						logger.error("Ftp2 file exists and cannot delete it for retransferring.");
						return false;
					}
				}
			}
		}
		if (!changeDir(ftp2, ftp2VO.getFileDirectory())) {
			logger.error("Change directory failed.");
			return false;
		}

		ftp1.setFileType(FTP.BINARY_FILE_TYPE);
		ftp2.setFileType(FTP.BINARY_FILE_TYPE);

		ftp2.enterRemotePassiveMode();
		ftp1.enterRemoteActiveMode(
				InetAddress.getByName(ftp2.getPassiveHost()),
				ftp2.getPassivePort());
		if (ftp1.remoteRetrieve(ftp1VO.getFileFullPath())
				&& ftp2.remoteStore(ftp2VO.getFileName())) {
			ftp1.completePendingCommand();
			ftp2.completePendingCommand();
			return true;
		} else {
			logger.error("Couldn't initiate transfer. Check that filenames are valid.");
			return false;
		}
	}

	@Override
	protected void logoutAndDisconnect() {
		try {
			if (ftp1.isConnected()) {
				ftp1.logout();
			}
		} catch (IOException e) {
			logger.error("ftp1 logout failed.", e);
		}
		try {
			if (ftp2.isConnected()) {
				ftp2.logout();
			}
		} catch (IOException e) {
			logger.error("ftp2 logout failed.", e);
		}

		try {
			if (ftp1.isConnected()) {
				ftp1.disconnect();
			}
		} catch (IOException e) {
			logger.error("ftp1 disconnect failed.", e);
		}

		try {
			if (ftp2.isConnected()) {
				ftp2.disconnect();
			}
		} catch (IOException e) {
			logger.error("ftp2 disconnect failed.", e);
		}
	}
}
