package com.ai.injection.job;

import iptv.CSPResult;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.message.MessageElement;
import org.apache.axis.types.Schema;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;

import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.entity.SendTask;
import com.ai.cms.injection.enums.InjectionItemTypeEnum;
import com.ai.cms.injection.enums.InjectionObjectTypeEnum;
import com.ai.cms.injection.enums.SendTaskStatusEnum;
import com.ai.cms.injection.repository.SendTaskRepository;
import com.ai.cms.injection.service.InjectionService;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.exception.DataException;
import com.ai.injection.service.GenProgramService;
import com.ai.injection.service.GenSeriesService;

@org.springframework.stereotype.Service
public class SendJob {
	private static final Log logger = LogFactory.getLog(SendJob.class);

	@Value("${spring.profiles.active:dev}")
	private String profiles;

	@Value("${injection.task.mode:1}")
	private int taskMode;

	@Value("${injection.task.maxNum:20}")
	private int taskMaxNum;

	@Value("${injection.task.maxRequestTimes:3}")
	private int taskMaxRequestTimes;

	@Value("${injection.task.timeout:6}")
	private int taskTimeout;

	@Autowired
	private InjectionService injectionService;

	@Autowired
	private SendTaskRepository sendTaskRepository;

	@Autowired
	private GenSeriesService genSeriesService;

	@Autowired
	private GenProgramService genProgramService;

	@Scheduled(cron = "${injection.task.schedule:0 0/2 * * * ?}")
	public void execute() {
		logger.info("SendJob begin.");
		int newTaskMaxNum = taskMaxNum;
		if (newTaskMaxNum <= 0) {
			return;
		}

		long startTime = System.currentTimeMillis();
		Date currentTime = new Date();
		// 1.获取已发送的任务
		PageRequest pageRequest = new PageRequest(0, 500);
		Page<SendTask> page = sendTaskRepository.findByStatus(
				SendTaskStatusEnum.PROCESSING.getKey(), pageRequest);
		List<SendTask> runTaskList = page.getContent();
		newTaskMaxNum -= runTaskList.size();
		for (SendTask sendTask : runTaskList) {
			// 如果任务执行超过6个小时，置为超时。
			if ((currentTime.getTime() - sendTask.getLastRequestTime()
					.getTime()) > taskTimeout * 3600 * 1000l) {
				// 置为超时
				sendTask.setStatus(SendTaskStatusEnum.TIMEOUT.getKey());
				injectionService.saveSendTask(sendTask);

				if (newTaskMaxNum < taskMaxNum) {
					newTaskMaxNum++;
				}
			}
		}

		if (taskMode == 2) {// 控制每小时任务数
			Date preHour = DateUtils.addHours(currentTime, -1);
			long oneHourNum = sendTaskRepository
					.countByLastRequestTime(preHour);
			newTaskMaxNum = taskMaxNum - (int) oneHourNum;
		}

		if (newTaskMaxNum <= 0) {
			return;
		}

		// 2.发送之前未发送的任务
		try {
			int sendNum = sendTask(newTaskMaxNum);
			if (sendNum > 0) {
				newTaskMaxNum -= sendNum;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		// 3.生成任务并发送
		try {
			genTask(newTaskMaxNum);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		logger.info("SendJob end run("
				+ (System.currentTimeMillis() - startTime) / 1000 + ")s.");
	}

	public int genTask(int newTaskMaxNum) {
		int sendNum = 0;
		if (newTaskMaxNum <= 0) {
			return sendNum;
		}

		PageRequest pageRequest = new PageRequest(0, newTaskMaxNum);
		Page<SendTask> page = sendTaskRepository
				.findNeedGenSendTask(pageRequest);
		List<SendTask> taskList = page.getContent();
		for (SendTask sendTask : taskList) {
			boolean result = false;
			try {
				if (sendTask.getPlatformId() == null) {
					throw new DataException("data is error.");
				}
				if (sendTask.getType() == InjectionObjectTypeEnum.OBJECT
						.getKey()
						&& sendTask.getItemType() == InjectionItemTypeEnum.SERIES
								.getKey()) {
					result = genSeriesService.genTask(sendTask);
				} else if (sendTask.getType() == InjectionObjectTypeEnum.OBJECT
						.getKey()
						&& sendTask.getItemType() == InjectionItemTypeEnum.PROGRAM
								.getKey()) {
					result = genProgramService.genTask(sendTask);
				}
			} catch (DataException e) {
				logger.error(e.getMessage(), e);
				sendTask.setStatus(SendTaskStatusEnum.STOP.getKey());
				injectionService.saveSendTask(sendTask);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				sendTask.setStatus(SendTaskStatusEnum.STOP.getKey());
				injectionService.saveSendTask(sendTask);
			}
			if (result) {
				boolean sendResult = sendTask(sendTask);
				if (sendResult) {
					sendNum++;
				}
			}
		}
		return sendNum;
	}

	private int sendTask(int newTaskMaxNum) {
		int sendNum = 0;
		if (newTaskMaxNum <= 0) {
			return sendNum;
		}

		PageRequest pageRequest = new PageRequest(0, newTaskMaxNum);
		Page<SendTask> page = sendTaskRepository.findByRequestTimes(
				taskMaxRequestTimes, pageRequest);
		List<SendTask> taskList = page.getContent();
		for (SendTask sendTask : taskList) {
			boolean sendResult = sendTask(sendTask);
			if (sendResult) {
				sendNum++;
			}
		}
		return sendNum;
	}

	private synchronized boolean sendTask(SendTask sendTask) {
		if (sendTask == null || sendTask.getPlatformId() == null) {
			return false;
		}
		InjectionPlatform platform = injectionService
				.findOneInjectionPlatform(sendTask.getPlatformId());
		if (platform == null) {
			return false;
		}
		logger.info("send task begin... CorrelateId={"
				+ sendTask.getCorrelateId() + "}");
		Date currentTime = new Date();
		if (sendTask.getFirstRequestTime() == null) {
			sendTask.setFirstRequestTime(currentTime);
		}
		sendTask.setLastRequestTime(currentTime);

		sendTask.setRequestTimes(sendTask.getRequestTimes() + 1);
		sendTask.setRequestTotalTimes(sendTask.getRequestTotalTimes() + 1);
		try {
			String soapUrl = StringUtils.trimToEmpty(platform.getServiceUrl());
			CSPResult cspResult = null;
			if (platform.getIsWSDL() == YesNoEnum.YES.getKey()) {
				cspResult = execCmd(soapUrl, platform.getNamespace(),
						platform.getCspId(), platform.getLspId(),
						sendTask.getCorrelateId(), sendTask.getCmdFileURL(),
						sendTask);
			} else {
				cspResult = execCmdBySoap(soapUrl, platform.getNamespace(),
						platform.getCspId(), platform.getLspId(),
						sendTask.getCorrelateId(), sendTask.getCmdFileURL(),
						sendTask);
			}
			sendTask.setRequestResult(cspResult.getResult());
			sendTask.setRequestErrorDescription(cspResult.getErrorDescription());
			if (cspResult.getResult() == 0) {
				sendTask.setStatus(SendTaskStatusEnum.PROCESSING.getKey());
			} else {
				sendTask.setStatus(SendTaskStatusEnum.FAIL.getKey());
			}
			injectionService.saveSendTask(sendTask);
			logger.info("send task end. CorrelateId={"
					+ sendTask.getCorrelateId() + "}" + ",result={"
					+ cspResult.getResult() + "}" + ",errorDescription={"
					+ cspResult.getErrorDescription() + "}");
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	public CSPResult execCmd(String soapUrl, String namespace, String CSPID,
			String LSPID, String CorrelateID, String CmdFileURL,
			SendTask sendTask) throws Exception {
		CSPResult cspResult = new CSPResult();
		int result = -1;
		String errorDescription = "";
		org.apache.axis.client.Service service = new org.apache.axis.client.Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new URL(soapUrl));
		call.setOperationName(new QName(namespace, "ExecCmd"));

		call.addParameter("CSPID", XMLType.XSD_STRING, ParameterMode.IN);
		call.addParameter("LSPID", XMLType.XSD_STRING, ParameterMode.IN);
		call.addParameter("CorrelateID", XMLType.XSD_STRING, ParameterMode.IN);
		call.addParameter("CmdFileURL", XMLType.XSD_STRING, ParameterMode.IN);
		call.setEncodingStyle("UTF-8");
		call.setReturnType(XMLType.XSD_SCHEMA);

		Object o = call.invoke(new Object[] { CSPID, LSPID, CorrelateID,
				CmdFileURL });
		Schema schema = (Schema) o;
		MessageElement[] messageElements = schema.get_any();
		for (MessageElement m : messageElements) {
			if ("Result".equalsIgnoreCase(m.getName())) {
				logger.info("execCmd Result={" + m.getValue() + "}");
				if (StringUtils.isNotEmpty(m.getValue())) {
					result = Integer.valueOf(m.getValue());
				} else {
					result = 0;
				}
			} else if ("ErrorDescription".equalsIgnoreCase(m.getName())) {
				logger.info("execCmd ErrorDescription={" + m.getValue() + "}");
				errorDescription = m.getValue();
			}
		}

		cspResult.setResult(result);
		cspResult.setErrorDescription(errorDescription);
		return cspResult;
	}

	public CSPResult execCmdBySoap(String soapUrl, String namespace,
			String CSPID, String LSPID, String CorrelateID, String CmdFileURL,
			SendTask sendTask) throws Exception {
		CSPResult cspResult = new CSPResult();
		int result = -1;
		String errorDescription = "";

		String soapRequestData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:iptv=\"iptv\">";
		soapRequestData += "<soapenv:Header/>";
		soapRequestData += "<soapenv:Body>";
		soapRequestData += "<" + namespace + ":ExecCmd>";
		soapRequestData += "<CSPID>" + StringUtils.trimToEmpty(CSPID)
				+ "</CSPID>";
		soapRequestData += "<LSPID>" + StringUtils.trimToEmpty(LSPID)
				+ "</LSPID>";
		soapRequestData += "<CorrelateID>"
				+ StringUtils.trimToEmpty(CorrelateID) + "</CorrelateID>";
		soapRequestData += "<CmdFileURL>" + StringUtils.trimToEmpty(CmdFileURL)
				+ "</CmdFileURL>";
		soapRequestData += "</" + namespace + ":ExecCmd>";
		soapRequestData += "</soapenv:Body>";
		soapRequestData += "</soapenv:Envelope>";
		logger.info("execCmd request " + soapRequestData);

		PostMethod postMethod = new PostMethod(soapUrl);
		postMethod.addRequestHeader("Content-Type", "text/xml;charset=UTF-8");
		postMethod.addRequestHeader("SOAPAction", "");

		byte[] b = soapRequestData.getBytes("utf-8");
		InputStream is = new ByteArrayInputStream(b, 0, b.length);
		RequestEntity re = new InputStreamRequestEntity(is, b.length,
				"application/soap+xml; charset=utf-8");
		postMethod.setRequestEntity(re);

		HttpClient httpClient = new HttpClient();

		int statusCode = httpClient.executeMethod(postMethod);
		logger.info("execCmd response statusCode=" + statusCode);

		SAXReader reader = new SAXReader();
		Document document = reader.read(postMethod.getResponseBodyAsStream());
		Element root = document.getRootElement();
		Element body = null;
		if (root != null) {
			body = root.element("Body");
		}
		Element ExecCmdResponseElement = null;
		Element ExecCmdReturnElement = null;
		if (body != null) {
			ExecCmdResponseElement = body.element("ExecCmdResponse");
			if (ExecCmdResponseElement != null) {
				ExecCmdReturnElement = ExecCmdResponseElement
						.element("ExecCmdReturn");
			}
		}

		if (ExecCmdReturnElement != null) {
			if (ExecCmdReturnElement.element("Result") != null) {
				String Result = ExecCmdReturnElement.element("Result")
						.getTextTrim();
				logger.info("execCmd Result={" + Result + "}");

				if (StringUtils.isNotEmpty(Result)) {
					result = Integer.valueOf(Result);
				} else {
					result = 0;
				}
			}
			if (ExecCmdReturnElement.element("ErrorDescription") != null) {
				String ErrorDescription = ExecCmdReturnElement.element(
						"ErrorDescription").getTextTrim();
				logger.info("execCmd ErrorDescription={" + ErrorDescription
						+ "}");
				errorDescription = ErrorDescription;
			}
		}
		cspResult.setResult(result);
		cspResult.setErrorDescription(errorDescription);
		return cspResult;
	}

}
