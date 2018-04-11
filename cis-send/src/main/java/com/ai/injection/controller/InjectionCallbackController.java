package com.ai.injection.controller;
//package com.ai.injection.controller;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.Date;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ai.common.controller.AbstractController;
//import com.ai.common.enums.ProviderTypeEnum;
//import com.ai.common.enums.YesNoEnum;
//import com.ai.common.exception.ValidateException;
//import com.ai.content.slaveentity.MediaFile;
//import com.ai.content.slaverepository.MediaFileRepository;
//import com.ai.content.slaveservice.ContentService;
//import com.ai.injection.bean.FileRequestBean;
//import com.ai.injection.bean.WriteBackRequestBean;
//import com.ai.injection.bean.WriteBackResponseBean;
//
//@Controller
//@RequestMapping(value = { "/callback" })
//public class InjectionCallbackController extends AbstractController {
//
//	@Autowired
//	private MediaService mediaService;
//
//	@Autowired
//	private MediaFileRepository mediaFileRepository;
//
//	@Value("${site.providerType:}")
//	private String providerType;
//
//	@Value("${site.platformId:}")
//	private String platformId;
//
//	@RequestMapping(value = { "writeback" }, produces = "application/json; charset=UTF-8")
//	@ResponseBody
//	public WriteBackResponseBean writeback(HttpServletRequest request,
//			HttpServletResponse response) throws IOException {
//		WriteBackResponseBean orderNotifyResponseBean = new WriteBackResponseBean();
//		String code = "0";
//		try {
//			String content = getStringByInputStream(request.getInputStream());
//
//			infoMessage(request.getServletPath() + " request:" + "["
//					+ getIpAddr(request) + "]{" + content + "}");
//			if (StringUtils.isEmpty(content)) {
//				throw new ValidateException(0, "request body is empty.");
//			}
//
//			ObjectMapper mapper = getObjectMapper();
//			WriteBackRequestBean orderNotifyRequestBean = mapper.readValue(
//					content, new TypeReference<WriteBackRequestBean>() {
//					});
//			List<FileRequestBean> fileList = orderNotifyRequestBean
//					.getSrcFiles();
//
//			String mediaId = orderNotifyRequestBean.getMediaId();
//			if (StringUtils.isEmpty(mediaId)) {
//				throw new ValidateException(0, "mediaId is empty.");
//			}
//			Integer platFormId = orderNotifyRequestBean.getPlatFormId();
//			if (platFormId == null) {
//				throw new ValidateException(0, "platFormId is null.");
//			}
//			Integer taskType = orderNotifyRequestBean.getTaskType();
//			if (taskType == null) {
//				taskType = 1;
//			}
//
//			List<MediaFile> mediaFileList = mediaFileRepository
//					.findByPartnerItemCode(mediaId);
//			if (mediaFileList == null || mediaFileList.size() == 0) {
//				throw new ValidateException(0, "mediaId is error.");
//			}
//
//			int providerTypeId = 0;
//			if (StringUtils.isNotEmpty(providerType)
//					&& StringUtils.isNotEmpty(platformId)) {
//				String[] providerTypeArr = providerType.split(",");
//				String[] platformIdArr = platformId.split(",");
//				for (int i = 0; i < providerTypeArr.length; i++) {
//					if (("" + platFormId).equals(platformIdArr[i])) {
//						if (("" + ProviderTypeEnum.HUAWEI.getKey())
//								.equals(providerTypeArr[i])) {
//							providerTypeId = ProviderTypeEnum.HUAWEI.getKey();
//						} else if (("" + ProviderTypeEnum.ZTE.getKey())
//								.equals(providerTypeArr[i])) {
//							providerTypeId = ProviderTypeEnum.ZTE.getKey();
//						}
//					}
//				}
//			}
//
//			MediaFile mediaFile = mediaFileList.get(0);
//			if (taskType == 2) {
//				if (providerTypeId == ProviderTypeEnum.HUAWEI.getKey()) {
//					// 华为
//					mediaFile.setHuaweiStatus(YesNoEnum.NO.getKey());
//					mediaFile.setHuaweiFileCode("");
//				} else if (providerTypeId == ProviderTypeEnum.ZTE.getKey()) {
//					// 中兴
//					mediaFile.setZteStatus(YesNoEnum.NO.getKey());
//					mediaFile.setZteFileCode("");
//				}
//			} else {
//				String playUrl = "";
//				if (fileList != null && fileList.size() > 0) {
//					playUrl = fileList.get(0).getPlayUrl();
//				}
//				if (providerTypeId == ProviderTypeEnum.HUAWEI.getKey()) {
//					// 华为
//					mediaFile.setHuaweiStatus(YesNoEnum.YES.getKey());
//					mediaFile.setHuaweiFileCode(playUrl);
//					mediaFile.setHuaweiInjectionTime(new Date());
//				} else if (providerTypeId == ProviderTypeEnum.ZTE.getKey()) {
//					// 中兴
//					mediaFile.setZteStatus(YesNoEnum.YES.getKey());
//					mediaFile.setZteFileCode(playUrl);
//					mediaFile.setZteInjectionTime(new Date());
//				}
//			}
//			mediaFileRepository.save(mediaFile);
//			mediaService.saveMediaFileAndInjection(mediaFile);
//			code = "1";
//		} catch (ValidateException e) {
//			orderNotifyResponseBean.setMessage(e.getMessage());
//			errorMessage(e);
//		} catch (Exception e) {
//			orderNotifyResponseBean.setMessage(e.getMessage());
//			errorMessage(e);
//		}
//		orderNotifyResponseBean.setCode(code);
//		return orderNotifyResponseBean;
//	}
//
//	public ObjectMapper getObjectMapper() {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//		return mapper;
//	}
//
//	private String getStringByInputStream(InputStream is) throws IOException {
//		StringBuffer sb = new StringBuffer();
//		BufferedReader br = null;
//		try {
//			String line = "";
//			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//			while ((line = br.readLine()) != null) {
//				sb.append(line);
//			}
//		} finally {
//			if (br != null) {
//				br.close();
//			}
//		}
//		return sb.toString();
//	}
//}
