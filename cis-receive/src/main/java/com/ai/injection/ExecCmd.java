package com.ai.injection;

import iptv.CSPResult;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.entity.ReceiveTask;
import com.ai.cms.injection.enums.ReceiveResponseStatusEnum;
import com.ai.cms.injection.enums.ReceiveTaskStatusEnum;
import com.ai.common.exception.ValidateException;
import com.ai.injection.service.ReceiveService;

@Component
public class ExecCmd {
	private static final Log logger = LogFactory.getLog(ExecCmd.class);

	private static ReceiveService receiveService;

	@Autowired
	public void setReceiveService(ReceiveService receiveService) {
		ExecCmd.receiveService = receiveService;
	}

	private static void validate(String CSPID, String LSPID,
			String CorrelateID, String CmdFileURL) throws ValidateException {
		if (StringUtils.isEmpty(CSPID)) {
			throw new ValidateException(-1, "CSPID is empty.");
		}
		if (StringUtils.isEmpty(LSPID)) {
			throw new ValidateException(-1, "LSPID is empty.");
		}
		if (StringUtils.isEmpty(CorrelateID)) {
			throw new ValidateException(-1, "CorrelateID is empty.");
		}
		if (StringUtils.isEmpty(CmdFileURL)) {
			throw new ValidateException(-1, "CmdFileURL is empty.");
		}
	}

	public static CSPResult execCmd(String CSPID, String LSPID,
			String CorrelateID, String CmdFileURL) {
		logger.info("execCmd request:CSPID={" + CSPID + "},LSPID={" + LSPID
				+ "},CorrelateID={" + CorrelateID + "},CmdFileURL={"
				+ CmdFileURL + "}");
		CSPResult cspResult = new CSPResult();
		int result = -1;
		String errorDescription = "";
		try {
			validate(CSPID, LSPID, CorrelateID, CmdFileURL);

			InjectionPlatform platform = receiveService
					.findReceiveInjectionPlatform(CSPID, LSPID);
			if (platform == null) {
				throw new ValidateException(-1, "CSPID or LSPID error.");
			}

			result = 0;
			errorDescription = "SUCCESS";

			ReceiveTask receiveTask = receiveService
					.findByPlatformIdAndCorrelateId(platform.getId(),
							CorrelateID);
			if (receiveTask == null) {
				receiveTask = new ReceiveTask();
				receiveTask.setCpCode(CSPID);
				receiveTask.setPlatformId(platform.getId());
				receiveTask.setCorrelateId(CorrelateID);
				receiveTask.setReceiveTime(new Date());
			}
			receiveTask.setCmdFileURL(StringUtils.trimToEmpty(CmdFileURL));

			receiveTask.setRequestResult(result);
			receiveTask.setRequestErrorDescription(errorDescription);

			receiveTask.setDownloadTimes(0);
			receiveTask.setResponseTimes(0);

			receiveTask.setStatus(ReceiveTaskStatusEnum.WAIT.getKey());
			receiveTask.setResponseStatus(ReceiveResponseStatusEnum.DEFAULT
					.getKey());
			receiveService.saveReceiveTask(receiveTask);
		} catch (ValidateException e) {
			result = e.getResult();
			errorDescription = e.getMessage();
			logger.error("execCmd error:" + errorDescription);
		} catch (Exception e) {
			result = -1;
			errorDescription = e.getMessage();
			logger.error("execCmd error:" + errorDescription);
		}
		cspResult.setResult(result);
		cspResult.setErrorDescription(errorDescription);
		logger.info("execCmd result:CSPID={" + CSPID + "},LSPID={" + LSPID
				+ "},CorrelateID={" + CorrelateID + "},CmdFileURL={"
				+ CmdFileURL + "}" + "result={" + result
				+ "},errorDescription={" + errorDescription + "}");
		return cspResult;
	}

}
