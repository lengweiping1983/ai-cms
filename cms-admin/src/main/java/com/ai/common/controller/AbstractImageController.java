package com.ai.common.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ai.AdminGlobal;
import com.ai.common.utils.Encodes;

public abstract class AbstractImageController extends AbstractController {

	public void deleteOldResource(String sourceImageUrl) {
		if (StringUtils.isEmpty(sourceImageUrl)) {
			return;
		}
		File file = new File(
				StringUtils.trimToEmpty(AdminGlobal.imageUploadPath)
						+ sourceImageUrl);
		if (file.exists()) {
			file.delete();
		}
	}

	public static String getImageFileName(String imageFormat) {
		java.util.Random random = new java.util.Random();
		String fileName = System.currentTimeMillis() + "" + random.nextInt(10)
				+ imageFormat;
		return fileName;
	}

	public static String getImageOutputDirPath(String module, String type) {
		String date = DateFormatUtils.format(new Date(), "/yyyyMM");
		String dirPath = "/" + module + "/" + type + date;

		if (StringUtils.isNotEmpty(AdminGlobal.getSiteCodePath())) {
			dirPath = "/"
					+ StringUtils.trimToEmpty(AdminGlobal.getSiteCodePath())
					+ dirPath;
		}
		return dirPath;
	}

	public String upload(String module, String type, String imageData) {
		if (StringUtils.isEmpty(module) || StringUtils.isEmpty(type)
				|| StringUtils.isEmpty(imageData)) {
			return "";
		}
		FileOutputStream write = null;
		try {
			String[] data = imageData.split(",");
			if (null != data && data.length > 1) {
				String imageFormat = "";
				if (-1 != data[0].indexOf("image/jpeg")) {
					imageFormat = ".jpg";
				} else if (-1 != data[0].indexOf("image/gif")) {
					imageFormat = ".gif";
				} else {
					imageFormat = ".png";
				}

				String imageUploadPath = StringUtils
						.trimToEmpty(AdminGlobal.imageUploadPath);
				String dirPath = getImageOutputDirPath(module, type);
				String fileName = getImageFileName(imageFormat);

				File dir = new File(imageUploadPath + dirPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}

				String filePath = dirPath + "/" + fileName;

				write = new FileOutputStream(imageUploadPath + filePath);
				byte[] decoderBytes = Encodes.decodeBase64(data[1]);
				write.write(decoderBytes);
				write.flush();
				return filePath;
			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (write != null) {
				try {
					write.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return "";
	}

	public String upload(String module, String type, MultipartFile file) {
		if (StringUtils.isEmpty(module) || StringUtils.isEmpty(type)
				|| file == null) {
			return "";
		}
		FileOutputStream write = null;
		try {
			String suffix = StringUtils.substringAfterLast(
					file.getOriginalFilename(), ".");
			String imageFormat = "." + suffix;
			String imageUploadPath = StringUtils
					.trimToEmpty(AdminGlobal.imageUploadPath);
			String dirPath = getImageOutputDirPath(module, type);
			String fileName = getImageFileName(imageFormat);

			File dir = new File(imageUploadPath + dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String filePath = dirPath + "/" + fileName;

			write = new FileOutputStream(imageUploadPath + filePath);
			write.write(file.getBytes());
			write.flush();
			return filePath;
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (write != null) {
				try {
					write.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return "";
	}
}
