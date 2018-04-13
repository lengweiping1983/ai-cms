package com.ai.injection.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.cms.injection.entity.InjectionObject;
import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.enums.InjectionItemTypeEnum;
import com.ai.cms.injection.enums.InjectionStatusEnum;
import com.ai.cms.injection.repository.InjectionPlatformRepository;
import com.ai.cms.injection.service.InjectionService;
import com.ai.common.controller.AbstractController;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.exception.ValidateException;
import com.ai.injection.bean.FileRequestBean;
import com.ai.injection.bean.WriteBackRequestBean;
import com.ai.injection.bean.WriteBackResponseBean;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = { "/callback" })
public class InjectionCallbackController extends AbstractController {

	@Autowired
	private InjectionService injectionService;

	@Autowired
	private InjectionPlatformRepository injectionPlatformRepository;

	@Value("${injection.platformId:1}")
	private Long platformId;

	@RequestMapping(value = { "writeback" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public WriteBackResponseBean writeback(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		WriteBackResponseBean orderNotifyResponseBean = new WriteBackResponseBean();
		String code = "0";
		try {
			String content = getStringByInputStream(request.getInputStream());

			infoMessage(request.getServletPath() + " request:" + "["
					+ getIpAddr(request) + "]{" + content + "}");
			if (StringUtils.isEmpty(content)) {
				throw new ValidateException(0, "request body is empty!");
			}

			ObjectMapper mapper = getObjectMapper();
			WriteBackRequestBean orderNotifyRequestBean = mapper.readValue(
					content, new TypeReference<WriteBackRequestBean>() {
					});
			List<FileRequestBean> fileList = orderNotifyRequestBean
					.getSrcFiles();

			String mediaId = orderNotifyRequestBean.getMediaId();
			if (StringUtils.isEmpty(mediaId)) {
				throw new ValidateException(0, "mediaId is empty!");
			}
			Integer platFormId = orderNotifyRequestBean.getPlatFormId();
			if (platFormId == null) {
				throw new ValidateException(0, "platFormId is null!");
			}
			Integer taskType = orderNotifyRequestBean.getTaskType();
			if (taskType == null) {
				taskType = 1;
			}
			String playUrl = "";
			if (fileList != null && fileList.size() > 0) {
				playUrl = fileList.get(0).getPlayUrl();
			}
			updateStatus(platFormId, mediaId, taskType, playUrl, platformId);
			code = "1";
		} catch (ValidateException e) {
			orderNotifyResponseBean.setMessage(e.getMessage());
			errorMessage(e);
		} catch (Exception e) {
			orderNotifyResponseBean.setMessage(e.getMessage());
			errorMessage(e);
		}
		orderNotifyResponseBean.setCode(code);
		return orderNotifyResponseBean;
	}

	private void updateStatus(Integer platFormId, String mediaId,
			Integer taskType, String playUrl, Long platformId) {
		InjectionPlatform platform = injectionService
				.findOneInjectionPlatform(platformId);
		if (platform == null) {
			throw new ValidateException(0, "data error!");
		}

		InjectionPlatform indirectPlatform = null;
		if (platform.getIsCallback() == YesNoEnum.YES.getKey()
				&& StringUtils.isNotEmpty(platform.getIndirectPlatformId())) {
			List<Long> ids = new ArrayList<Long>();
			for (String indirectPlatformId : platform.getIndirectPlatformId()
					.split(",")) {
				ids.add(Long.valueOf(indirectPlatformId));
			}
			if (ids.size() > 0) {
				List<InjectionPlatform> injectionPlatformList = injectionService
						.findAllInjectionPlatform(ids);
				for (InjectionPlatform injectionPlatform : injectionPlatformList) {
					if (("" + platFormId).equals(injectionPlatform
							.getPlatformCode())) {
						indirectPlatform = injectionPlatform;
						break;
					}
				}
			}
		}
		if (indirectPlatform == null) {
			throw new ValidateException(0, "data error!");
		}

		InjectionObject injectionObject = injectionService
				.findByPlatformIdAndItemTypeAndPartnerItemCode(
						indirectPlatform.getId(),
						InjectionItemTypeEnum.MOVIE.getKey(), mediaId);
		if (injectionObject == null) {
			throw new ValidateException(0, "mediaId error!");
		}

		if (taskType == 2) {
			injectionObject
					.setInjectionStatus(InjectionStatusEnum.RECOVERY_SUCCESS
							.getKey());
		} else {
			injectionObject
					.setInjectionStatus(InjectionStatusEnum.INJECTION_SUCCESS
							.getKey());
			injectionObject.setInjectionTime(new Date());
			injectionObject.setPartnerItemReserved1(playUrl);
		}
		injectionService.saveInjectionObject(injectionObject);
		injectionService.updateMediaFileInjectionObjectAndInjectionStatus(
				indirectPlatform, injectionObject.getCategory(),
				injectionObject.getItemId());
	}

	@RequestMapping(value = { "test" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public WriteBackResponseBean test(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		WriteBackResponseBean orderNotifyResponseBean = new WriteBackResponseBean();
		String code = "0";
		try {
			String mediaId = "00000000000000020000000000000400";
			Integer platFormId = 108;
			Integer taskType = 2;
			String playUrl = "";
			Long platformId = 25l;

			updateStatus(platFormId, mediaId, taskType, playUrl, platformId);
			code = "1";
		} catch (ValidateException e) {
			orderNotifyResponseBean.setMessage(e.getMessage());
			errorMessage(e);
		} catch (Exception e) {
			orderNotifyResponseBean.setMessage(e.getMessage());
			errorMessage(e);
		}
		orderNotifyResponseBean.setCode(code);
		return orderNotifyResponseBean;
	}

	public ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		return mapper;
	}

	private String getStringByInputStream(InputStream is) throws IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			String line = "";
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}
		return sb.toString();
	}
}
