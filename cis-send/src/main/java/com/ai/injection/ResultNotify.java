package com.ai.injection;

import iptv.CSPResult;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.ai.AdminGlobal;
import com.ai.SpringContextHolder;
import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.entity.SendTask;
import com.ai.cms.injection.service.InjectionService;
import com.ai.common.exception.ValidateException;
import com.ai.common.utils.FtpUtils;

@Component
public class ResultNotify {
	private static final Log logger = LogFactory.getLog(ResultNotify.class);

	private static InjectionService injectionService;

	private static void validate(String CSPID, String LSPID,
			String CorrelateID, int CmdResult, String ResultFileURL)
			throws ValidateException {
		if (StringUtils.isEmpty(CSPID)) {
			throw new ValidateException(-1, "CSPID is empty.");
		}
		if (StringUtils.isEmpty(LSPID)) {
			throw new ValidateException(-1, "LSPID is empty.");
		}
		if (StringUtils.isEmpty(CorrelateID)) {
			throw new ValidateException(-1, "CorrelateID is empty.");
		}
	}

	public static synchronized CSPResult resultNotify(String CSPID,
			String LSPID, String CorrelateID, int CmdResult,
			String ResultFileURL) {
		logger.info("resultNotify request:CSPID={" + CSPID + "},LSPID={"
				+ LSPID + "},CorrelateID={" + CorrelateID + "},CmdResult={"
				+ CmdResult + "},ResultFileURL={" + ResultFileURL + "}");
		CSPResult cspResult = new CSPResult();
		int result = -1;
		String errorDescription = "";
		try {
			validate(CSPID, LSPID, CorrelateID, CmdResult, ResultFileURL);

			if (injectionService == null) {
				injectionService = (InjectionService) SpringContextHolder
						.getBean("injectionService");
			}

			SendTask sendTask = injectionService
					.findSendTaskByCorrelateId(CorrelateID);
			if (sendTask != null) {
				sendTask.setCmdResult(CmdResult);

				if (StringUtils.isNotEmpty(ResultFileURL)) {
					sendTask.setResultFileURL(StringUtils
							.trimToEmpty(replaceIP(ResultFileURL,
									AdminGlobal.getAccessIP())));

					InjectionPlatform platform = injectionService
							.findOneInjectionPlatform(sendTask.getPlatformId());
					String dirPath = "/send/"
							+ platform.getCspId()
							+ DateFormatUtils.format(sendTask.getCreateTime(),
									"/yyyyMM");
					String fileName = platform.getCspId() + "_"
							+ platform.getLspId() + "_"
							+ sendTask.getCorrelateId() + "_"
							+ System.currentTimeMillis() + ".xml";
					String filePath = dirPath + "/" + fileName;
					String distPath = AdminGlobal.getXmlUploadPath(filePath);
					try {
						FtpUtils.downloadFileByFtpPath(
								sendTask.getResultFileURL(), distPath,
								AdminGlobal.isChangeWorkingDirectory());
						sendTask.setResponseXmlFilePath(filePath);
						// sendTask.setResponseXmlFileContent(IOUtils
						// .readTxtFile(distPath));

						result = 0;
						errorDescription = "SUCCESS";

					} catch (IOException e) {
						logger.error(e.getMessage(), e);

						result = -1;
						errorDescription = "下载XML失败!";
					}
				} else {
					result = 0;
				}

				sendTask.setResponseTime(new Date());
				sendTask.setResponseResult(result);
				sendTask.setResponseErrorDescription(errorDescription);

				injectionService.saveSendTaskAndInjectionStatus(sendTask,
						CmdResult);
			}
		} catch (ValidateException e) {
			result = e.getResult();
			errorDescription = e.getMessage();
			logger.error("resultNotify error:" + errorDescription);
		} catch (Exception e) {
			result = -1;
			errorDescription = e.getMessage();
			logger.error("resultNotify error:" + errorDescription);
		}
		cspResult.setResult(result);
		cspResult.setErrorDescription(errorDescription);
		logger.info("resultNotify result:CSPID={" + CSPID + "},LSPID={" + LSPID
				+ "},CorrelateID={" + CorrelateID + "},CmdResult={" + CmdResult
				+ "},ResultFileURL={" + ResultFileURL + "}");
		return cspResult;
	}

	public static String replaceIP(String url, String accessIP) {
		if (StringUtils.isEmpty(url)) {
			return url;
		}
		if (StringUtils.isEmpty(accessIP)) {
			return url;
		}
		String ip = "";
		String regex = "((\\d+\\.){3}\\d+)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(url);
		while (m.find()) {
			ip = m.group(1);
		}
		if (StringUtils.isEmpty(ip)) {
			return url;
		}
		return url.replace(ip, accessIP);
	}

}
