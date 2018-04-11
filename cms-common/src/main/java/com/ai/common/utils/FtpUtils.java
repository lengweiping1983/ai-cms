package com.ai.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.provider.GenericFileName;
import org.apache.commons.vfs.provider.ftp.FtpFileNameParser;

import com.ai.common.bean.FTPFileBean;
import com.ai.common.enums.FileTypeEnum;

public class FtpUtils {
	private static final Log logger = LogFactory.getLog(FtpUtils.class);

	private static FtpUtils ftpUtils;
	private static long lastAccessTime = System.currentTimeMillis();

	private FTPClient ftpClient;
	private String ip;
	private int port;
	private String user;
	private String password;
	private String mode;

	public FtpUtils(String ip, int port, String user, String password) {
		this.ftpClient = new FTPClient();
		this.ip = ip;
		this.port = port;
		this.user = user;
		this.password = password;
	}

	public static FtpUtils getFtp(String targetFtp) throws FileSystemException,
			IOException {
		GenericFileName gfn = (GenericFileName) FtpFileNameParser.getInstance()
				.parseUri(null, null, targetFtp);
		return new FtpUtils(gfn.getHostName(), gfn.getPort(),
				gfn.getUserName(), gfn.getPassword());
	}

	public synchronized static FtpUtils getInstance(String targetFtp,
			String mode) throws FileSystemException, IOException {
		return getInstance(targetFtp, mode, false);
	}

	public synchronized static FtpUtils getInstance(String targetFtp,
			String mode, boolean reConnect) throws FileSystemException,
			IOException {
		if (reConnect
				|| ftpUtils == null
				|| !ftpUtils.isConnected()
				|| (System.currentTimeMillis() - lastAccessTime) > 5 * 60 * 1000) {
			ftpUtils = FtpUtils.getFtp(targetFtp);
			ftpUtils.mode = mode;
			ftpUtils.ftpLogin();
		}
		return ftpUtils;
	}

	/**
	 * 登录FTP服务器
	 * 
	 * @throws IOException
	 */
	public boolean ftpLogin() throws IOException {
		boolean result = false;
		logger.info("login ftp server start...");
		FTPClientConfig ftpClientConfig = new FTPClientConfig();
		ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
		ftpClient.setControlEncoding("UTF-8");
		ftpClient.configure(ftpClientConfig);
		ftpClient.setConnectTimeout(30 * 1000);
		ftpClient.setControlKeepAliveReplyTimeout(30 * 1000);
		ftpClient.setControlKeepAliveTimeout(30 * 1000);
		ftpClient.setDataTimeout(30 * 1000);
		ftpClient.setDefaultTimeout(30 * 1000);

		if (port > 0) {
			ftpClient.connect(ip, port);
		} else {
			ftpClient.connect(ip);
		}
		logger.info("connect ftp server success.");
		// FTP服务器连接回答
		int reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			logger.error("login ftp server fail!");
			// 关闭FTP服务器连接
			ftpClient.disconnect();
			return result;
		}
		ftpClient.setKeepAlive(true);
		ftpClient.login(user, password);
		if ("active".equalsIgnoreCase(mode)) {
			ftpClient.enterLocalActiveMode();
		} else {
			ftpClient.enterLocalPassiveMode();
		}
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		ftpClient.setBufferSize(1024 * 1024);

		logger.info("login ftp server success.");
		result = true;
		return result;
	}

	/**
	 * 退出FTP服务器
	 */
	public boolean ftpLogout() throws IOException {
		boolean result = false;
		if (null == ftpClient || !ftpClient.isConnected()) {
			return result;
		}
		logger.info("logout ftp server start...");
		try {
			// 退出FTP服务器
			result = ftpClient.logout();
			if (result) {
				logger.info("logout ftp server success.");
			} else {
				logger.error("logout ftp server fail!");
			}
		} finally {
			try {
				// 关闭FTP服务器连接
				ftpClient.disconnect();
			} catch (IOException e) {
				logger.error("close ftp connect error:", e);
			}
		}
		return result;
	}

	/**
	 * 是否连接
	 */
	public boolean isConnected() {
		return ftpClient.isConnected();
	}

	/**
	 * 获取文件列表
	 */
	public List<FTPFileBean> getFiles(final String ftpRootPath,
			final String accessPath, final String name) throws IOException {
		try {
			List<FTPFileBean> result = _getFiles(ftpRootPath, accessPath,
					StringUtils.trimToEmpty(name));
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ftpLogin();
			List<FTPFileBean> result = _getFiles(ftpRootPath, accessPath,
					StringUtils.trimToEmpty(name));
			return result;
		}
	}

	/**
	 * 重命文件
	 */
	public boolean rename(final String from, final String to)
			throws IOException {
		return ftpClient.rename(from, to);
	}

	/**
	 * 获取文件列表
	 */
	private List<FTPFileBean> _getFiles(final String ftpRootPath,
			final String accessPath, final String name) throws IOException {
		lastAccessTime = System.currentTimeMillis();
		List<FTPFileBean> result = new ArrayList<FTPFileBean>();
		String listPath = PathUtils.joinPath(ftpRootPath, accessPath);
		logger.info("get ftp file list " + listPath + " begin...");
		FTPFile[] files = ftpClient.listFiles(listPath);
		for (FTPFile file : files) {
			FTPFileBean bean = new FTPFileBean();
			if (file.isDirectory()) {
				bean.setType(FileTypeEnum.DIR.getKey());
			} else if (file.isSymbolicLink()) {
				bean.setType(FileTypeEnum.DIR.getKey());
			} else if (file.isFile()) {
				bean.setType(FileTypeEnum.FILE.getKey());
			} else {
				continue;
			}
			if (StringUtils.isNotEmpty(name) && !file.getName().contains(name)) {
				continue;
			}
			bean.setName(file.getName());
			bean.setSize(file.getSize());
			bean.setPath(PathUtils.joinPath(accessPath, file.getName()));
			result.add(bean);
		}

		Collections.sort(result);
		if (StringUtils.isNotEmpty(accessPath) && !"/".equals(accessPath)) {
			FTPFileBean up = new FTPFileBean();
			up.setType(FileTypeEnum.DIR.getKey());
			up.setName("..");
			up.setSize(4096l);
			int index = PathUtils.getFilePath(accessPath).lastIndexOf("/");
			String upPath = PathUtils.getFilePath(accessPath
					.substring(0, index));
			up.setPath(upPath);
			result.add(0, up);
		}
		logger.info("get ftp file list " + listPath + " end.");
		return result;
	}

	public boolean downloadFile(String remoteFilePath, String localFilePath,
			boolean changeWorkingDirectory) throws IOException {
		boolean success = false;
		if (null == ftpClient || !ftpClient.isConnected()) {
			return success;
		}
		FileOutputStream out = null;
		try {
			String dirPath = StringUtils
					.substringBeforeLast(localFilePath, "/");
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			out = new FileOutputStream(localFilePath);
			logger.info("download ftp file(" + remoteFilePath + ") start...");
			if (changeWorkingDirectory) {
				String remoteDirPath = StringUtils.substringAfter(
						remoteFilePath, "/");
				remoteDirPath = StringUtils.substringBeforeLast(remoteDirPath,
						"/");
				String remoteFileName = StringUtils.substringAfterLast(
						remoteFilePath, "/");
				logger.info("download ftp changeWorkingDirectory("
						+ remoteDirPath + ").");
				ftpClient.changeWorkingDirectory(remoteDirPath);
				logger.info("download ftp file(" + remoteFileName
						+ ") start...");
				success = ftpClient.retrieveFile(remoteFileName, out);
			} else {
				success = ftpClient.retrieveFile(remoteFilePath, out);
			}
			if (success) {
				out.flush();
				logger.info("download ftp file(" + remoteFilePath
						+ ") success to (" + localFilePath + ")");
			} else {
				throw new IOException("download ftp file(" + remoteFilePath
						+ ") fail to (" + localFilePath + ")");
			}
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("error:", e);
				}
			}
		}
		return success;
	}

	public static boolean downloadFileTo(String targetFtpPath,
			String destRootPath) throws IOException {
		return downloadFileTo(targetFtpPath, destRootPath, false);
	}

	public static boolean downloadFileTo(String targetFtpPath,
			String destRootPath, boolean changeWorkingDirectory)
			throws IOException {
		String filePath = getFilePath(targetFtpPath);
		FtpUtils ftpUtils = FtpUtils.getFtp(targetFtpPath);
		ftpUtils.ftpLogin();
		boolean result = ftpUtils.downloadFile(filePath, destRootPath
				+ filePath, changeWorkingDirectory);
		ftpUtils.ftpLogout();
		return result;
	}

	public static boolean downloadFileByFtpPath(String targetFtpPath,
			String destPath) throws IOException {
		return downloadFileByFtpPath(targetFtpPath, destPath, false);
	}

	public static boolean downloadFileByFtpPath(String targetFtpPath,
			String destPath, boolean changeWorkingDirectory) throws IOException {
		String filePath = getFilePath(targetFtpPath);
		FtpUtils ftpUtils = FtpUtils.getFtp(targetFtpPath);
		ftpUtils.ftpLogin();
		boolean result = ftpUtils.downloadFile(filePath, destPath,
				changeWorkingDirectory);
		ftpUtils.ftpLogout();
		return result;
	}

	public static String getFilePath(String source) {
		String str = source;
		str = StringUtils.substringAfterLast(str, "@");
		str = StringUtils.substringAfter(str, "/");
		if (str.charAt(0) != '/') {
			str = "/" + str;
		}
		return str;
	}

}
