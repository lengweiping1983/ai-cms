package com.ai.injection.job;

import iptv.CSPResult;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.message.MessageElement;
import org.apache.axis.types.Schema;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ai.AdminGlobal;
import com.ai.cms.injection.bean.ADI;
import com.ai.cms.injection.bean.ObjectBean;
import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.entity.ReceiveTask;
import com.ai.cms.injection.enums.InjectionActionTypeEnum;
import com.ai.cms.injection.enums.InjectionItemTypeEnum;
import com.ai.cms.injection.enums.ReceiveResponseStatusEnum;
import com.ai.cms.injection.enums.ReceiveTaskStatusEnum;
import com.ai.cms.injection.repository.InjectionPlatformRepository;
import com.ai.cms.injection.repository.ReceiveTaskRepository;
import com.ai.cms.injection.service.DownloadService;
import com.ai.common.exception.ServiceException;
import com.ai.common.utils.FtpUtils;
import com.ai.injection.service.ReceiveService;
import com.ai.injection.service.XMLGenerator;
import com.ai.injection.service.XMLParser;

@Service
public class ReceiveJob {
	private static final Log logger = LogFactory.getLog(ReceiveJob.class);

	@Value("${spring.profiles.active:dev}")
	private String profiles;

	@Value("${injection.task.maxNum:20}")
	private int taskMaxNum;

	@Value("${injection.task.maxRequestTimes:3}")
	private int taskMaxRequestTimes;

	@Autowired
	private InjectionPlatformRepository injectionPlatformRepository;

	@Autowired
	private ReceiveTaskRepository receiveTaskRepository;

	@Autowired
	private ReceiveService receiveService;

	@Autowired
	private DownloadService downloadService;

	@Autowired
	private XMLGenerator xmlGenerator;

	@Scheduled(cron = "${injection.task.schedule:0 0/5 * * * ?}")
	public void execute() {
		logger.info("ReceiveJob begin.");
		long startTime = System.currentTimeMillis();

		try {
			checkInjectionTask();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		try {
			handleTask();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		try {
			sendTask();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("ReceiveJob end run("
				+ (System.currentTimeMillis() - startTime) / 1000 + ")s.");
	}

	private void checkInjectionTask() {
		if (taskMaxNum <= 0) {
			return;
		}
		PageRequest pageRequest = new PageRequest(0, taskMaxNum);
		Page<ReceiveTask> page = receiveTaskRepository.findByNextCheckTime(
				new Date(), pageRequest);
		List<ReceiveTask> taskList = page.getContent();
		for (ReceiveTask receiveTask : taskList) {
			checkInjectionTask(receiveTask);
		}
	}

	private void checkInjectionTask(ReceiveTask receiveTask) {
		logger.info("check injection task begin... CorrelateId={"
				+ receiveTask.getCorrelateId() + "}");
		InjectionPlatform platform = null;
		try {
			// 1.检查参数
			if (receiveTask.getPlatformId() == null) {
				// 属于数据异常
				throw new ServiceException("数据错误！");
			}
			platform = injectionPlatformRepository.findOne(receiveTask
					.getPlatformId());
			if (platform == null) {
				throw new ServiceException("配置错误！");
			}
			boolean result = downloadService.autoInjectionTask(receiveTask,
					platform);
			if (!result) {
				downloadService.checkInjectionTask(receiveTask, platform);
			}
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			// 等待5分钟
			receiveTask.setNextCheckTime(DateUtils.addMinutes(new Date(), 2));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 等待5分钟
			receiveTask.setNextCheckTime(DateUtils.addMinutes(new Date(), 2));
		}
		receiveService.saveReceiveTask(receiveTask);
		logger.info("check injection task end. CorrelateId={"
				+ receiveTask.getCorrelateId() + "}");
	}

	public String getXmlFileContent(ADI adi) {
		StringBuffer sb = new StringBuffer();
		if (adi != null && adi.getObjects() != null) {
			for (ObjectBean objectBean : adi.getObjects()) {
				InjectionItemTypeEnum typeEnum = InjectionItemTypeEnum
						.getEnumByValue(objectBean.getElementType());
				InjectionActionTypeEnum actionEnum = InjectionActionTypeEnum
						.getEnumByValue(objectBean.getAction());
				if (typeEnum == null || actionEnum == null) {
					continue;
				}
				sb.append("ElementType=\"" + typeEnum.getValue() + "\"" + ":"
						+ "Action=\"" + actionEnum.getValue() + "\"" + ":"
						+ objectBean.getID() + ":" + objectBean.getCode() + ":"
						+ objectBean.getName() + ";");
			}
		}
		return sb.toString();
	}

	private void handleTask() {
		if (taskMaxNum <= 0) {
			return;
		}
		PageRequest pageRequest = new PageRequest(0, taskMaxNum);
		Page<ReceiveTask> page = receiveTaskRepository.findByDownloadTimes(
				taskMaxRequestTimes, pageRequest);
		List<ReceiveTask> taskList = page.getContent();
		for (ReceiveTask receiveTask : taskList) {
			handleTask(receiveTask);
		}
	}

	private void handleTask(ReceiveTask receiveTask) {
		logger.info("handle task begin... CorrelateId={"
				+ receiveTask.getCorrelateId() + "}");
		InjectionPlatform platform = null;
		try {
			// 1.检查参数
			if (StringUtils.isEmpty(receiveTask.getCmdFileURL())) {
				throw new ServiceException("XML文件地址为空！");
			}
			if (receiveTask.getPlatformId() == null) {
				// 属于数据异常
				throw new ServiceException("数据错误！");
			}
			platform = injectionPlatformRepository.findOne(receiveTask
					.getPlatformId());
			if (platform == null) {
				throw new ServiceException("配置错误！");
			}

			String dirPath = "/receive/"
					+ platform.getCspId()
					+ DateFormatUtils.format(receiveTask.getCreateTime(),
							"/yyyyMM");
			String fileName = platform.getCspId() + "_" + platform.getLspId()
					+ "_" + receiveTask.getCorrelateId() + "_"
					+ System.currentTimeMillis() + "_request.xml";
			String filePath = dirPath + "/" + fileName;
			String distPath = AdminGlobal.getXmlUploadPath(filePath);

			// 2.下载XML
			try {
				if (receiveTask.getDownloadTimes() == null) {
					receiveTask.setDownloadTimes(0);
				}
				receiveTask
						.setDownloadTimes(receiveTask.getDownloadTimes() + 1);
				FtpUtils.downloadFileByFtpPath(receiveTask.getCmdFileURL(),
						distPath);
				receiveTask.setRequestXmlFilePath(filePath);
				// receiveTask.setRequestXmlFileContent(IOUtils
				// .readTxtFile(distPath));
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				if (receiveTask.getDownloadTimes() >= taskMaxRequestTimes) {
					throw new ServiceException("XML文件下载失败！");
				}
			}

			// 3.解析XML
			ADI adi = null;
			try {
				adi = XMLParser.parser(distPath);
				receiveTask.setPriority(adi.getPriority());
				receiveTask.setRequestXmlFileContent(getXmlFileContent(adi));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException("XML文件解析出错！");
			}

			// 4.保存元数据
			receiveService.handleTask(receiveTask, platform, adi);

			receiveTask.setReplyResult(0);
			receiveTask.setReplyErrorDescription("SUCCESS");

			downloadService.checkTaskStatus(receiveTask);

		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);

			receiveTask.setReplyResult(-1);
			receiveTask.setReplyErrorDescription(e.getMessage());

			receiveTask.setStatus(ReceiveTaskStatusEnum.FAIL.getKey());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			receiveTask.setReplyResult(-1);
			receiveTask.setReplyErrorDescription("ERROR");

			receiveTask.setStatus(ReceiveTaskStatusEnum.FAIL.getKey());
		}

		receiveService.saveReceiveTask(receiveTask);

		logger.info("handle task end. CorrelateId={"
				+ receiveTask.getCorrelateId() + "}" + ",ReplyResult={"
				+ receiveTask.getReplyResult() + "}"
				+ ",ReplyErrorDescription={"
				+ receiveTask.getReplyErrorDescription() + "}");
	}

	private void sendTask() {
		if (taskMaxNum <= 0) {
			return;
		}
		PageRequest pageRequest = new PageRequest(0, taskMaxNum);
		Page<ReceiveTask> page = receiveTaskRepository.findByResponseTimes(
				taskMaxRequestTimes, pageRequest);
		List<ReceiveTask> taskList = page.getContent();
		for (ReceiveTask receiveTask : taskList) {
			sendTask(receiveTask);
		}
	}

	private void sendTask(ReceiveTask receiveTask) {
		logger.info("send task begin... CorrelateId={"
				+ receiveTask.getCorrelateId() + "}");
		InjectionPlatform platform = null;
		try {
			// 1.验证数据
			if (receiveTask.getPlatformId() == null) {
				// 属于数据异常
				throw new ServiceException("数据错误！");
			}
			platform = injectionPlatformRepository.findOne(receiveTask
					.getPlatformId());
			if (platform == null) {
				throw new ServiceException("配置错误！");
			}

			// 2.生成结果XML
			try {
				receiveTask.setCmdResult(receiveTask.getReplyResult());
				String resultFileURL = xmlGenerator.genXML(receiveTask,
						platform);
				receiveTask.setResultFileURL(AdminGlobal
						.getXmlFtpPath(resultFileURL));
				receiveTask.setResponseXmlFilePath(resultFileURL);
				// receiveTask.setResponseXmlFileContent(IOUtils
				// .readTxtFile(AdminGlobal.getXmlUploadPath(resultFileURL)));
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException("生成结果XML错误！");
			}

			// 3.发送结果
			try {
				Date currentTime = new Date();
				if (receiveTask.getFirstResponseTime() == null) {
					receiveTask.setFirstResponseTime(currentTime);
				}
				receiveTask.setLastResponseTime(currentTime);
				receiveTask
						.setResponseTimes(receiveTask.getResponseTimes() + 1);
				receiveTask.setResponseTotalTimes(receiveTask
						.getResponseTotalTimes() + 1);

				CSPResult cspResult = resultNotify(platform,
						receiveTask.getCorrelateId(),
						receiveTask.getCmdResult(),
						receiveTask.getResultFileURL());

				receiveTask.setResponseResult(cspResult.getResult());
				receiveTask.setResponseErrorDescription(cspResult
						.getErrorDescription());

				receiveTask.setResponseStatus(ReceiveResponseStatusEnum.SUCCESS
						.getKey());// 发送成功
			} catch (Exception e) {
				logger.error(e.getMessage(), e);

				receiveTask.setResponseStatus(ReceiveResponseStatusEnum.FAIL
						.getKey());// 发送失败
				throw new ServiceException("发送结果失败！");
			}
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		receiveService.saveReceiveTask(receiveTask);

		logger.info("send task end. CorrelateId={"
				+ receiveTask.getCorrelateId() + "}" + ",ResponseResult={"
				+ receiveTask.getResponseResult() + "}"
				+ ",ResponseErrorDescription={"
				+ receiveTask.getResponseErrorDescription() + "}");
	}

	public CSPResult resultNotify(InjectionPlatform platform,
			String CorrelateID, int CmdResult, String ResultFileURL)
			throws Exception {
		CSPResult cspResult = new CSPResult();
		int result = -1;
		String errorDescription = "";
		org.apache.axis.client.Service service = new org.apache.axis.client.Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new URL(StringUtils.trimToEmpty(platform
				.getServiceUrl())));
		call.setOperationName(new QName(StringUtils.trimToEmpty(platform
				.getNamespace()), "ResultNotify"));

		call.addParameter("CSPID", XMLType.XSD_STRING, ParameterMode.IN);
		call.addParameter("LSPID", XMLType.XSD_STRING, ParameterMode.IN);
		call.addParameter("CorrelateID", XMLType.XSD_STRING, ParameterMode.IN);
		call.addParameter("CmdResult", XMLType.XSD_INT, ParameterMode.IN);
		call.addParameter("ResultFileURL", XMLType.XSD_STRING, ParameterMode.IN);
		call.setEncodingStyle("UTF-8");
		call.setReturnType(XMLType.XSD_SCHEMA);

		Object o = call.invoke(new Object[] {
				StringUtils.trimToEmpty(platform.getCspId()),
				StringUtils.trimToEmpty(platform.getLspId()), CorrelateID,
				CmdResult, ResultFileURL });
		Schema schema = (Schema) o;
		MessageElement[] messageElements = schema.get_any();
		for (MessageElement m : messageElements) {
			if ("Result".equalsIgnoreCase(m.getName())) {
				result = Integer.valueOf(m.getValue());
			} else if ("ErrorDescription".equalsIgnoreCase(m.getName())) {
				errorDescription = m.getValue();
			}
		}

		cspResult.setResult(result);
		cspResult.setErrorDescription(errorDescription);
		return cspResult;
	}

}
