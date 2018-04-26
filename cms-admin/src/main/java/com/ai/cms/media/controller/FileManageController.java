package com.ai.cms.media.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.cms.config.entity.CpFtp;
import com.ai.cms.config.service.ConfigService;
import com.ai.cms.media.bean.BatchPathBean;
import com.ai.cms.transcode.enums.TranscodeRequestTypeEnum;
import com.ai.cms.transcode.utils.ShaTaClient;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.FTPFileBean;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.FileTypeEnum;
import com.ai.common.exception.ServiceException;
import com.ai.common.utils.FtpUtils;
import com.ai.sys.entity.User;
import com.ai.sys.security.SecurityUtils;

@Controller
@RequestMapping(value = { "/media/file" })
public class FileManageController extends AbstractImageController {
	public static Map<Long, String> userAccessPathMap = new HashMap<Long, String>();

	private CacheManager cacheManager;

	private Cache ftpFileCache;

	@Autowired
	private ConfigService configService;

	@Value("${ftp.address:}")
	private String ftpAddress;

	@Value("${ftp.mode:}")
	private String ftpMode;

	@Value("${ftp.root.path:/}")
	private String ftpRootPath;

	@Value("${ftp.default.access.path:/}")
	private String ftpDefaultAccessPath;

	public String getFtpAddress(CpFtp cpFtp) {
		if (cpFtp != null) {
			return "ftp://" + cpFtp.getUsername() + ":" + cpFtp.getPassword()
					+ "@" + cpFtp.getIp() + ":" + cpFtp.getPort() + "/";
		}
		return ftpAddress;
	}

	public String getFtpRootPath(CpFtp cpFtp) {
		if (cpFtp != null) {
			return cpFtp.getRootPath();
		}
		return ftpRootPath;
	}

	public String getFtpDefaultAccessPath(CpFtp cpFtp) {
		if (cpFtp != null) {
			return cpFtp.getDefaultAccessPath();
		}
		return ftpDefaultAccessPath;
	}

	public CacheManager getCacheManager(CpFtp cpFtp) {
		return cacheManager;
	}

	@Autowired
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
		this.ftpFileCache = cacheManager
				.getCache("FileManageController.FtpFile");
	}

	@SuppressWarnings("unchecked")
	public List<FTPFileBean> _getFtpFileList(String accessPath, int refresh,
			boolean cache) {
		List<FTPFileBean> files = null;
		try {
			if (cache) {
				ValueWrapper valueWrapper = ftpFileCache.get(StringUtils
						.trimToEmpty(SecurityUtils.getCpCode())
						+ "-"
						+ accessPath);
				if (valueWrapper != null) {
					files = (List<FTPFileBean>) valueWrapper.get();
				}
			}
			if (files == null || refresh == 1) {
				CpFtp cpFtp = configService.findCpFtpByCpCode(SecurityUtils
						.getCpCode());

				files = FtpUtils.getInstance(getFtpAddress(cpFtp), ftpMode)
						.getFiles(getFtpRootPath(cpFtp), accessPath, "");
				if ((files == null || files.size() == 0)
						&& accessPath.equals(getFtpDefaultAccessPath(cpFtp))) {
					files = FtpUtils.getInstance(getFtpAddress(cpFtp), ftpMode,
							true).getFiles(getFtpRootPath(cpFtp), accessPath,
							"");
				}
				if (cache) {
					ftpFileCache.put(
							StringUtils.trimToEmpty(SecurityUtils.getCpCode())
									+ "-" + accessPath, files);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException("访问FTP错误!");
		}
		return files;
	}

	public List<FTPFileBean> getFtpFileList(String accessPath, String name,
			int refresh) {
		List<FTPFileBean> files = _getFtpFileList(accessPath, refresh, true);
		if (StringUtils.isEmpty(name)) {
			return files;
		}
		List<FTPFileBean> filesTemp = new ArrayList<FTPFileBean>();
		for (FTPFileBean file : files) {
			if (file.getName().contains(name)) {
				filesTemp.add(file);
			}
		}
		return filesTemp;
	}

	@RequestMapping(value = { "" })
	public String list(
			Model model,
			@RequestParam(value = "path", required = false) String path,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "refresh", required = false, defaultValue = "0") Integer refresh) {

		User user = SecurityUtils.getUser();
		String accessPath = StringUtils.trimToEmpty(path);
		if (StringUtils.isEmpty(accessPath)) {
			accessPath = userAccessPathMap.get(user.getId());
			if (StringUtils.isEmpty(accessPath)) {
				CpFtp cpFtp = configService.findCpFtpByCpCode(SecurityUtils
						.getCpCode());
				accessPath = getFtpDefaultAccessPath(cpFtp);
			}
		}
		List<FTPFileBean> files = getFtpFileList(accessPath, name, refresh);
		model.addAttribute("files", files);

		userAccessPathMap.put(user.getId(), accessPath);
		model.addAttribute("path", accessPath);
		model.addAttribute("typeEnum", FileTypeEnum.values());
		model.addAttribute("transcodeRequestTypeEnum",
				TranscodeRequestTypeEnum.values());

		return "media/file/list";
	}

	@RequestMapping(value = { "trim" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult trim(
			@RequestParam(value = "path", required = false) String path) {
		logger.info("trim path {" + path + "}");
		String accessPath = StringUtils.trimToEmpty(path);
		if (StringUtils.isEmpty(accessPath)) {
			return new BaseResult();
		}
		try {
			List<FTPFileBean> files = _getFtpFileList(accessPath, 1, false);

			int num = 0;
			ShaTaClient shaTaClient = new ShaTaClient();
			for (FTPFileBean file : files) {
				String sourceFileName = file.getName();
				if (StringUtils.isNotEmpty(sourceFileName)
						&& containSpecialChar(sourceFileName)) {
					String destFileName = replaceSpecialChar(sourceFileName);
					String from = file.getPath();
					String to = file.getPath().replace(sourceFileName,
							destFileName);
					if (StringUtils.isNotEmpty(from)
							&& StringUtils.isNotEmpty(to) && !from.equals(to)) {
						if (num > 200) {
							break;
						}
						try {
							logger.info("rename form {" + from + "} to {" + to
									+ "}");
							num++;
							shaTaClient.rename(from, to);
						} catch (Exception e) {
							// throw new ServiceException("重命名错误!" +
							// e.getMessage());
							logger.error("rename error.", e);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new ServiceException("访问FTP错误!");
		}

		return new BaseResult();
	}

	public static void main(String[] args) {
		String sourceFileName = "27寓言两则（揠苗助长&守株待兔）.mpg";
		if (StringUtils.isNotEmpty(sourceFileName)
				&& containSpecialChar(sourceFileName)) {
			String destFileName = replaceSpecialChar(sourceFileName);
			System.out.println(destFileName);
		}
	}

	public final static String[] SPECIAL_CHAR = { " ", "'", "\"", ",", "[",
			"]", "【", "】", "(", ")", "+", "-", "=", "&" };

	public static boolean containSpecialChar(String fileName) {
		for (String c : SPECIAL_CHAR) {
			if (fileName.indexOf(c) > 0) {
				return true;
			}
		}
		return false;
	}

	public static String replaceSpecialChar(String fileName) {
		String result = fileName;
		for (String c : SPECIAL_CHAR) {
			result = result.replace(c, "");
		}
		return result;
	}

	@RequestMapping(value = { "selectFile" })
	public String selectFile(
			Model model,
			@RequestParam(value = "path", required = false) String path,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "refresh", required = false, defaultValue = "0") Integer refresh) {
		User user = SecurityUtils.getUser();
		String accessPath = StringUtils.trimToEmpty(path);
		if (StringUtils.isEmpty(accessPath)) {
			accessPath = userAccessPathMap.get(user.getId());
			if (StringUtils.isEmpty(accessPath)) {
				CpFtp cpFtp = configService.findCpFtpByCpCode(SecurityUtils
						.getCpCode());
				accessPath = getFtpDefaultAccessPath(cpFtp);
			}
		}

		List<FTPFileBean> files = getFtpFileList(accessPath, name, refresh);
		model.addAttribute("files", files);

		userAccessPathMap.put(user.getId(), accessPath);
		model.addAttribute("path", accessPath);
		model.addAttribute("typeEnum", FileTypeEnum.values());

		return "media/file/selectFile";
	}

	@RequestMapping(value = { "rename" }, method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public String toRename(Model model,
			@RequestParam(value = "from", required = false) String from) {
		model.addAttribute("from", from);
		return "media/file/edit";
	}

	@RequestMapping(value = { "rename" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult rename(@RequestBody RenameBean renameBean) {
		String from = renameBean.getFrom();
		String to = renameBean.getTo();
		logger.info("rename form {" + from + "} to {" + to + "}");
		if (StringUtils.isEmpty(from) || StringUtils.isEmpty(to)) {
			throw new ServiceException("重命名错误!");
		}
		if (from.equals(to)) {
			throw new ServiceException("重命名错误，原路径与目标路径相同!");
		}
		ShaTaClient shaTaClient = new ShaTaClient();
		try {
			shaTaClient.rename(from, to);
		} catch (Exception e) {
			throw new ServiceException("重命名错误!" + e.getMessage());
		}
		return new BaseResult();
	}

	@RequiresPermissions("media:file:delete")
	@RequestMapping(value = { "delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(
			@RequestParam(value = "path", required = false) String path) {
		logger.info("delete file {" + path + "}");
		ShaTaClient shaTaClient = new ShaTaClient();
		try {
			shaTaClient.deleteFile(path);
		} catch (Exception e) {
			throw new ServiceException("删除文件错误!" + e.getMessage());
		}
		return new BaseResult();
	}

	@RequiresPermissions("media:file:batchDelete")
	@RequestMapping(value = { "batchDelete" }, method = RequestMethod.GET)
	public String batchDelete(Model model) {
		return "media/file/batchDelete";
	}

	@RequiresPermissions("media:file:batchDelete")
	@RequestMapping(value = { "batchDelete" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchDelete(@RequestBody BatchPathBean batchPathBean) {
		logger.info("batch delete file begin.");
		ShaTaClient shaTaClient = new ShaTaClient();
		try {
			String[] paths = batchPathBean.getPaths();
			if (paths != null) {
				for (String path : paths) {
					if (StringUtils.isNotEmpty(path)) {
						logger.info("delete file {" + path + "}");
						shaTaClient.deleteFile(path);
					}
				}
			}
		} catch (Exception e) {
			throw new ServiceException("删除文件错误!" + e.getMessage());
		}
		logger.info("batch delete file end.");
		return new BaseResult();
	}

}

class RenameBean {
	String from;

	String to;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

}
