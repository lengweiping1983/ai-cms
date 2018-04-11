package com.ai.cms.transcode.api.controller;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.cms.transcode.service.TranscodeService;
import com.ai.cms.transcode.utils.ShaTaClient;
import com.ai.common.bean.ObjectResult;
import com.ai.common.controller.AbstractController;
import com.ai.common.exception.ServiceException;

@Controller
@RequestMapping(value = { "/api/transcode" })
public class TranscodeCallbackController extends AbstractController {
	private static final Log log = LogFactory
			.getLog(TranscodeCallbackController.class);

	@Autowired
	private TranscodeService transcodeService;

	public String readJSONString(HttpServletRequest request) {
		StringBuffer json = new StringBuffer();
		try {
			BufferedReader reader = request.getReader();
			String line = null;
			while ((line = reader.readLine()) != null) {
				json.append(line);
			}
		} catch (Exception e) {
			log.error("readJSONString error:", e);
		}
		return json.toString();
	}

	@RequestMapping(value = "/offlineUpload")
	public String offlineUpload(HttpServletRequest request, ModelMap model,
			HttpServletResponse response) throws Exception {
		log.info(request.getServletPath() + " request:" + "["
				+ getIpAddr(request) + "]{" + getAllParam(request) + "}");

		response.setCharacterEncoding("UTF-8");

		String fileUrl = request.getParameter("fileUrl");
		String path = request.getParameter("path");
		String callback = request.getParameter("callback");

		ShaTaClient stClient = new ShaTaClient();

		String ret = stClient.offlineUpload(fileUrl, path, callback);

		response.getWriter().print(ret);
		response.getWriter().flush();
		response.getWriter().close();

		return null;
	}

	@RequestMapping(value = "/offlineUploadCallback")
	public String offlineUploadCallback(HttpServletRequest request,
			ModelMap model, HttpServletResponse response) throws Exception {
		log.info(request.getServletPath() + " request:" + "["
				+ getIpAddr(request) + "]{" + getAllParam(request) + "}");
		response.setCharacterEncoding("UTF-8");

		String json = readJSONString(request);
		log.info("offlineUpload Task Finished:" + json);
		if (StringUtils.isEmpty(json)) {
			response.getWriter().print("json is null");
			response.getWriter().flush();
			response.getWriter().close();
			return null;
		}

		JSONObject jsonObject = new JSONObject(json);

		String url = jsonObject.getString("url");
		String path = jsonObject.getString("path");
		// String size = jsonObject.getString("size");
		// String md5 = jsonObject.getString("md5");
		String status = jsonObject.getString("status");// 0 :success
		// String resultCode = jsonObject.getString("resultCode");
		// String description = jsonObject.getString("description");

		log.info("url:" + url + " path:" + path + " status:" + status);
		if (StringUtils.isNotEmpty(path)) {
			response.getWriter().print("success");
			response.getWriter().flush();
			response.getWriter().close();

			return null;
		}

		response.getWriter().print("failed! path is invalid!");
		response.getWriter().flush();
		response.getWriter().close();

		return null;
	}

	@RequestMapping(value = "/encode")
	public String encode(HttpServletRequest request, ModelMap model,
			HttpServletResponse response) throws Exception {
		log.info(request.getServletPath() + " request:" + "["
				+ getIpAddr(request) + "]{" + getAllParam(request) + "}");

		response.setCharacterEncoding("UTF-8");

		String mediaPath = request.getParameter("mediaPath");
		String profile = request.getParameter("profile");
		String targetPath = request.getParameter("targetPath");
		String encodeCallback = request.getParameter("encodeCallback");

		ShaTaClient stClient = new ShaTaClient();

		String ret = stClient.encode(mediaPath, profile, targetPath,
				encodeCallback);

		response.getWriter().print(ret);
		response.getWriter().flush();
		response.getWriter().close();

		return null;
	}

	@RequestMapping(value = "/encodeCallback")
	public String encodeCallback(HttpServletRequest request, ModelMap model,
			HttpServletResponse response) throws Exception {
		log.info(request.getServletPath() + " request:" + "["
				+ getIpAddr(request) + "]{" + getAllParam(request) + "}");

		response.setCharacterEncoding("UTF-8");

		String json = readJSONString(request);
		log.info("Encode Task Finished:" + json);
		if (StringUtils.isEmpty(json)) {
			response.getWriter().print("json is null");
			response.getWriter().flush();
			response.getWriter().close();
			return null;
		}

		JSONObject jsonObject = new JSONObject(json);
		String task_id = jsonObject.getString("task_id");
		int result = jsonObject.getInt("result"); // 0=成功,1=失败,2=取消
		String message = jsonObject.getString("message");

		if (StringUtils.isNotEmpty(task_id)) {
			transcodeService.updateTranscodeTaskFromCallback(task_id, result,
					message);

			response.getWriter().print("success");
			response.getWriter().flush();
			response.getWriter().close();

			return null;
		}

		response.getWriter().print("failed! task_id is invalid!");
		response.getWriter().flush();
		response.getWriter().close();

		return null;
	}

	@RequestMapping(value = "/imageCallback")
	public String imageCallback(HttpServletRequest request, ModelMap model,
			HttpServletResponse response) throws Exception {
		log.info(request.getServletPath() + " request:" + "["
				+ getIpAddr(request) + "]{" + getAllParam(request) + "}");

		response.setCharacterEncoding("UTF-8");

		String json = readJSONString(request);
		log.info("IdsBean Task Finished:" + json);
		if (StringUtils.isEmpty(json)) {
			response.getWriter().print("json is null");
			response.getWriter().flush();
			response.getWriter().close();
			return null;
		}

		JSONObject jsonObject = new JSONObject(json);

		// JSONArray paths = jsonObject.getJSONArray("paths");
		String task_id = jsonObject.getString("task_id");
		int result = jsonObject.getInt("result"); // 0,成功 ，1失败， 2 取消
		String message = jsonObject.getString("message");

		if (StringUtils.isNotEmpty(task_id)) {
			transcodeService.updateTranscodeTaskFromCallback(task_id, result,
					message);

			response.getWriter().print("success");
			response.getWriter().flush();
			response.getWriter().close();

			return null;
		}

		response.getWriter().print("failed! task_id is invalid!");
		response.getWriter().flush();
		response.getWriter().close();

		return null;
	}

	@RequestMapping(value = { "/refreshFileMd5" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ObjectResult<MediaFileBean> refreshFileMd5(
			HttpServletRequest request, ModelMap model,
			HttpServletResponse response) throws Exception {
		ObjectResult<MediaFileBean> result = new ObjectResult<MediaFileBean>();

		int resultCode = 0;
		try {
			MediaFileBean mediaFileBean = new MediaFileBean();

			log.info(request.getServletPath() + " request:" + "["
					+ getIpAddr(request) + "]{" + getAllParam(request) + "}");

			String sourcePath = request.getParameter("filePath");
			mediaFileBean.setFilePath(sourcePath);
			ShaTaClient shaTaClient = new ShaTaClient();
			JSONObject allJson = shaTaClient.listDirectory(sourcePath);
			log.info("sourcePath = " + sourcePath + " json string:" + allJson);
			if (allJson != null && allJson.has("paths")) {
				boolean found = false;
				JSONArray jsonArray = allJson.getJSONArray("paths");
				if (jsonArray != null && jsonArray.length() > 0) {
					JSONObject json = jsonArray.getJSONObject(0);
					String path = json.getString("path");
					String size = json.optString("size");
					String md5 = json.optString("md5");
					if (path != null && path.equalsIgnoreCase(sourcePath)) {
						found = true;
						mediaFileBean.setFileSize(Long.valueOf(size));
						mediaFileBean.setFileMd5(md5);
					}
				}
				if (!found) {
				}
			}

			result.setData(mediaFileBean);
		} catch (ServiceException e) {
			resultCode = e.getCode();
			logger.error("refreshFileMd5 exception:" + "{<ResultCode="
					+ resultCode + ">}", e);
		} catch (Exception e) {
			resultCode = 9999;
			logger.error("refreshFileMd5 exception:" + "{<ResultCode="
					+ resultCode + ">}", e);
		}
		result.setCode(resultCode);
		return result;
	}
}
