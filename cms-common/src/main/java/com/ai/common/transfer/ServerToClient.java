package com.ai.common.transfer;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.common.exception.DataException;

public class ServerToClient extends AbstractFTPClient {
	private static final Logger logger = LoggerFactory
			.getLogger(ServerToClient.class);
	private FTPClient ftp = null;
	private FTPFileInfoVO ftpVO = null;
	private File localFile = null;
	private FTPTransferMode transferMode = FTPTransferMode.DOWNLOAD;

	public void setTransferMode(FTPTransferMode transferMode) {
		this.transferMode = transferMode;
	}

	public FTPTransferMode getTransferMode() {
		return transferMode;
	}

	public ServerToClient() {
		ftp = new FTPClient();
		ftp.addProtocolCommandListener(new FTPLogCommandListener(logger));
	}

	public boolean transfer(String remote, String local) {
		return transfer(remote, local, 0l, null);
	}

	public boolean transfer(String remote, String local, long taskId,
			TaskCallback callback) {
		ftpVO = convertFTPUrlToVO(remote);
		localFile = new File(local);
		this.taskId = taskId;
		this.callback = callback;
		return doTransferFlow();
	}

	@Override
	protected boolean connect() throws IOException {
		if (!connect(ftp, ftpVO)) {
			throw new DataException("Failed to connect to server.");
		}
		return true;
	}

	@Override
	protected boolean login() throws IOException {
		if (!ftp.login(ftpVO.getUserName(), ftpVO.getPassword())) {
			throw new DataException("Cannot login to server.");
		}
		return true;
	}

	@Override
	protected boolean doTransfer() throws IOException {
		if (transferMode == FTPTransferMode.DOWNLOAD) {
			return download();
		} else if (transferMode == FTPTransferMode.UPLOAD) {
			return upload();
		}
		return false;
	}

	private boolean download() throws IOException {
		InputStream input = null;
		OutputStream output = null;
		try {
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();

			FTPFile[] files = ftp.listFiles(ftpVO.getFileFullPath());
			if (files.length == 0) {
				throw new DataException("Remote file does not exist.");
			}
			long totalSize = files[0].getSize();
			long finishSize = 0L;
			double finishPercent = 0.0;

			if (localFile.exists()) {
				if (isResumeBroken()) {
					long localSize = localFile.length();
					if (localSize > totalSize) {
						logger.error("Local file exists and greater than remote file's size.");
						// 本地数据错误，重新下载
						if (!localFile.delete() || !localFile.createNewFile()) {
							throw new DataException(
									"Local file exists and cannot recreate it for redownloading.");
						}
					} else if (localSize == totalSize) {
						logger.info("Local file exists and equal remote file's size.");
						callback.success(taskId);
						return true;
					} else {
						logger.info("Local file exists and offset = "
								+ localSize);
						ftp.setRestartOffset(localSize);
						finishSize = localSize;
					}
				} else {
					if (!localFile.delete() || !localFile.createNewFile()) {
						throw new DataException(
								"Local file exists and cannot recreate it for redownloading.");
					}
				}
			} else {
				if (!localFile.getParentFile().exists()
						&& !localFile.getParentFile().mkdirs()) {
					throw new DataException(
							"Cannot create local file's directory.");
				}
				if (!localFile.createNewFile()) {
					throw new DataException("Cannot create local file.");
				}
			}

			callback.begining(taskId, totalSize, finishSize);
			output = new FileOutputStream(localFile, true);
			// return ftp.retrieveFile(ftpVO.getFileFullPath(), output);
			input = ftp.retrieveFileStream(ftpVO.getFileFullPath());
			byte[] buffer = new byte[1024];
			int count;
			long startTime = System.currentTimeMillis();
			while ((count = input.read(buffer)) != -1) {
				output.write(buffer, 0, count);
				finishSize += count;
				double currentPercent = (finishSize * 1.0) / (totalSize * 1.0);
				long now = System.currentTimeMillis();
				if (currentPercent - finishPercent > 0.01
						|| (now - startTime) > 60 * 1000l) {
					finishPercent = currentPercent;
					startTime = now;
					if (!callback.percent(taskId, finishPercent)) {
						break;
					}
				} else if (finishSize == totalSize) {
					finishPercent = currentPercent;
					callback.success(taskId);
				} else if (finishSize > totalSize) {

				}
			}
			return true;
		} finally {
			closeQuietly(input);
			closeQuietly(output);
		}
	}

	private boolean upload() throws IOException {
		InputStream input = null;
		OutputStream output = null;
		try {
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();

			if (!localFile.exists()) {
				throw new DataException("Local file does not exist.");
			}

			FTPFile[] files = ftp.listFiles(ftpVO.getFileFullPath());
			long totalSize = localFile.length();
			long finishSize = 0L;
			double finishPercent = 0.0;

			if (files.length != 0) {
				if (isResumeBroken()) {
					long remoteSize = files[0].getSize();
					if (totalSize < remoteSize) {
						logger.error("Remote file exists and greater than local file's size.");
						// 远程数据错误，重新上线
						if (!ftp.deleteFile(ftpVO.getFileFullPath())) {
							throw new DataException(
									"Remote file exists and cannot delete it for reuploading.");
						}
					} else if (totalSize == remoteSize) {
						logger.info("Remote file exists and equal local file's size.");
						callback.success(taskId);
						return true;
					} else {
						logger.info("Remote file exists and offset = "
								+ remoteSize);
						ftp.setRestartOffset(remoteSize);
						finishSize = remoteSize;
					}
				} else {
					if (!ftp.deleteFile(ftpVO.getFileFullPath())) {
						throw new DataException(
								"Remote file exists and cannot delete it for reuploading.");
					}
				}
			}
			if (!changeDir(ftp, ftpVO.getFileDirectory())) {
				throw new DataException("Change directory failed.");
			}
			input = new FileInputStream(localFile);
			if (finishSize != input.skip(finishSize)) {
				throw new DataException("Skip uploaded file size failed.");
			}
			callback.begining(taskId, totalSize, finishSize);
			// return ftp.storeFile(ftpVO.getFileName(), input);
			output = ftp.appendFileStream(ftpVO.getFileName());
			byte[] buffer = new byte[1024];
			int count;
			long startTime = System.currentTimeMillis();
			while ((count = input.read(buffer)) != -1) {
				output.write(buffer, 0, count);
				finishSize += count;
				double currentPercent = (finishSize * 1.0) / (totalSize * 1.0);
				long now = System.currentTimeMillis();
				if (currentPercent - finishPercent > 0.01
						|| (now - startTime) > 60 * 1000l) {
					finishPercent = currentPercent;
					startTime = now;
					if (!callback.percent(taskId, finishPercent)) {
						break;
					}
				} else if (finishSize >= totalSize) {
					finishPercent = currentPercent;
					callback.success(taskId);
				}
			}
			return true;
		} finally {
			closeQuietly(input);
			closeQuietly(output);
		}
	}

	@Override
	protected void logoutAndDisconnect() {
		try {
			if (ftp.isConnected()) {
				ftp.logout();
			}
		} catch (IOException e) {
			logger.error("ftp logout failed.", e);
		}
		try {
			if (ftp.isConnected()) {
				ftp.disconnect();
			}
		} catch (IOException e) {
			logger.error("ftp disconnect failed.", e);
		}
	}

	private void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException e) {
			logger.debug("closeQuietly", e);
		}
	}

	public static void main(String[] args) {
		ServerToClient stc = new ServerToClient();
		stc.setRetryTimes(3);
		stc.setRetryWaitTime(1L);
		stc.setTransferMode(FTPTransferMode.UPLOAD);

		TaskCallback callback = new TaskCallback() {

			@Override
			public boolean success(long taskId) {
				System.out.println("taskId=" + taskId + ",success");
				return false;
			}

			@Override
			public boolean fail(long taskId, int times, String message) {
				System.out.println("taskId=" + taskId + ",fail,times=" + times
						+ ",message=" + message);
				return false;
			}

			@Override
			public boolean percent(long taskId, double percent) {
				System.out.println("taskId=" + taskId + ",percent=" + percent);
				return true;
			}

		};

		String remote = "ftp://anonymous:epg123@127.0.0.1:21//Users/luqin/work/workspace_java/epg/epg-admin/src/main/webapp/video/3.mp4";
		String local = "/Users/luqin/work/workspace_java/epg/epg-admin/src/main/webapp/video/1.ts";
		if (stc.transfer(remote, local, 1, callback)) {
			System.out.println("Success!");
		} else {
			System.out.println("Failure!");
		}
	}
}
