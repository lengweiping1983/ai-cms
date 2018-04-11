package com.ai.cms.transcode.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import com.ai.CloudStorage;

public class ShaTaClient {
	private static final Log log = LogFactory.getLog(ShaTaClient.class);

	private static final String ACL_UPLOAD = "upload";
	private static final String ACL_BASIC_MANAGE = "basic-manage";
	private static final String ACL_ADVANCED_MANAGE = "advanced-manage";

	// accessToken.url
	private String accessTokenUrl = "/rest/authorization";

	// offlineUpload.url
	private String offlineUploadUrl = "/rest/file/offline-upload";

	// encode.url
	private String encodeUrl = "/rest/file/encode";

	// image.url
	private String imageUrl = "/rest/file/image";

	// rename.url
	private String renameUrl = "/rest/file/rename";

	// fetch quota.url
	private String quotaUrl = "/rest/quota";

	// list Directory.url
	private String listDirectoryUrl = "/rest/file/list";

	// delete File.url
	private String deleteFileUrl = "/rest/file/delete";

	private String getUrl(String url) {
		return StringUtils.defaultString(CloudStorage.getUrl(),
				"http://liantiaocsm01.chinacloudapp.cn:8080/otvcloud") + url;
	}

	public static String getfilePath(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}
		if (path.indexOf("http") == 0) {
			return path;
		}
		if (path.indexOf("ftp") == 0) {
			return path;
		}
		return StringUtils.trimToEmpty(CloudStorage.getFilePathPrefix())
				+ StringUtils.trimToEmpty(path);
	}

	private String getMD5(String accessTokenClientId, String timestamp,
			String acl, String accessTokenPassword) {
		StringBuffer sb = new StringBuffer();
		sb.append(accessTokenClientId).append("|");
		sb.append(timestamp).append("|");
		sb.append(acl).append("|");
		sb.append(accessTokenPassword);
		String md5 = Md5Utils.encode(sb.toString());
		return md5;
	}

	private String accessToken(String acl) throws Exception {
		String accessTokenClientId = StringUtils.defaultString(
				CloudStorage.getClientId(), "bestv");
		String accessTokenPassword = StringUtils.defaultString(
				CloudStorage.getPassword(), "bestv_upload");
		String timestamp = System.currentTimeMillis() + "";
		String md5 = getMD5(accessTokenClientId, timestamp, acl,
				accessTokenPassword);
		JSONObject jo = new JSONObject();
		jo.put("client_id", accessTokenClientId);
		jo.put("timestamp", timestamp);
		jo.put("hash", md5);
		jo.put("acl", acl);
		String data = jo.toString();
		log.info("start accessToken url:" + getUrl(accessTokenUrl) + " data:"
				+ data);
		String result = put(getUrl(accessTokenUrl), data);
		log.info("end accessToken url:" + getUrl(accessTokenUrl) + " data:"
				+ data + " response:" + result);

		JSONObject json = new JSONObject(result);
		return json.get("access_token") + "";
	}

	public String offlineUpload(String fileUrl, String path, String callback)
			throws Exception {
		String token = accessToken(ACL_UPLOAD);
		String url = getUrl(offlineUploadUrl) + "?access_token=" + token;
		JSONObject jo = new JSONObject();
		jo.put("url", getfilePath(fileUrl));
		jo.put("path", path);
		jo.put("callback", callback);
		String data = jo.toString();
		log.info("start offlineUpload url:" + url + " data:" + data);
		String result = post(url, data);
		log.info("end offlineUpload url:" + url + " data:" + data
				+ " response:" + result);

		return data;
	}

	public String fetchQuota() throws Exception {
		String token = accessToken(ACL_BASIC_MANAGE);
		StringBuffer sb = new StringBuffer();
		sb.append(getUrl(quotaUrl)).append("?access_token=").append(token);

		String url = sb.toString();
		log.info("start fetchQuota url:" + url);
		String result = get(url);
		log.info("end fetchQuota url:" + url);

		JSONObject json = new JSONObject(result);
		return json.getString("used");
	}

	public JSONObject listDirectory(String path) throws Exception {
		String token = accessToken(ACL_BASIC_MANAGE);
		StringBuffer sb = new StringBuffer();
		sb.append(getUrl(listDirectoryUrl)).append("?access_token=")
				.append(token);
		sb.append("&path=").append(encode(getfilePath(path)));

		String url = sb.toString();
		log.info("start list directory url:" + url);
		String result = get(url);
		log.info("end list directory url:" + url);

		JSONObject json = new JSONObject(result);
		return json;
	}

	public void deleteFile(String path) throws Exception {
		deleteFile(path, false);
	}

	public void deleteFile(String path, boolean recursive) throws Exception {
		String token = accessToken(ACL_BASIC_MANAGE);
		String url = getUrl(deleteFileUrl) + "?access_token=" + token
				+ "&path=" + encode(getfilePath(path)) + "&recursive="
				+ recursive;
		log.info("start delete file url:" + url);
		delete(url);
		log.info("end delete file url:" + url);
	}

	public String image(String mediaPath, String profile, String timePoints,
			String imagePath, String callback) throws Exception {
		String token = accessToken(ACL_ADVANCED_MANAGE);
		StringBuffer sb = new StringBuffer();
		sb.append(getUrl(imageUrl)).append("?access_token=").append(token);
		sb.append("&path=").append(encode(getfilePath(mediaPath)));
		sb.append("&profile=").append(profile);
		if (StringUtils.isNotEmpty(timePoints)) {
			sb.append("&ss=").append(timePoints);
		} else {
			sb.append("&ss=").append("10");
		}
		sb.append("&image_path=").append(encode(imagePath));
		sb.append("&callback=").append(encode(callback));

		String url = sb.toString();
		log.info("start image url:" + url);
		String result = post(url);
		log.info("end image url:" + url + " response:" + result);

		return result;
	}

	public String encode(String mediaPath, String profile, String targetPath,
			String callback) throws Exception {
		String token = accessToken(ACL_ADVANCED_MANAGE);
		StringBuffer sb = new StringBuffer();
		sb.append(getUrl(encodeUrl)).append("?access_token=").append(token);
		sb.append("&path=").append(encode(getfilePath(mediaPath)));
		sb.append("&profile=").append(profile);
		sb.append("&target_path=").append(encode(targetPath));
		sb.append("&callback=").append(encode(callback));

		String url = sb.toString();
		log.info("start encode url:" + url);
		String result = post(url);
		log.info("end encode url:" + url + " response:" + result);

		return result;
	}

	public String rename(String from, String to) throws Exception {
		if (StringUtils.isEmpty(from) || StringUtils.isEmpty(to)
				|| from.equals(to)) {
			return "";
		}
		String token = accessToken(ACL_BASIC_MANAGE);
		StringBuffer sb = new StringBuffer();
		sb.append(getUrl(renameUrl)).append("?access_token=").append(token);
		sb.append("&from=").append(encode(from));
		sb.append("&to=").append(encode(to));

		String url = sb.toString();
		log.info("start rename url:" + url);
		String result = post(url);
		log.info("end rename url:" + url + " response:" + result);

		return result;
	}

	public static void main(String[] args) throws Exception {
		ShaTaClient shaTaClient = new ShaTaClient();
		System.out.println(shaTaClient.accessToken(ACL_BASIC_MANAGE));

		// JSONObject json = null;
		// json = shaTaClient.listDirectory("/");
		// System.out.println(json.toString());

		// json = shaTaClient.listDirectory("");
		// System.out.println(json.toString());

		JSONObject json = shaTaClient
				.listDirectory("/transcode/8/201606/2016MZBXWKBCYMZBBNXCPSQXZLX/4m_VBR/2016MZBXWKBCYMZBBNXCPSQXZLX_4_248579_1_4m_VBR.TS");
		System.out.println(json);

	}

	public static String encode(String source)
			throws UnsupportedEncodingException {
		return URLEncoder.encode(source, "UTF-8");
	}

	public static String get(String url) throws IOException {
		return send(url, null, "GET");
	}

	public static String post(String url) throws IOException {
		return send(url, null, "POST");
	}

	public static String post(String url, String data) throws IOException {
		return send(url, data, "POST");
	}

	public static String put(String url) throws IOException {
		return send(url, null, "PUT");
	}

	public static String put(String url, String data) throws IOException {
		return send(url, data, "PUT");
	}

	public static String delete(String url) throws IOException {
		return send(url, null, "DELETE");
	}

	public static String send(String urlLocation, String data,
			String requestMethod) throws IOException {
		URL url = new URL(urlLocation);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		if (StringUtils.isBlank(requestMethod)) {
			connection.setRequestMethod("POST");
		} else {
			connection.setRequestMethod(requestMethod);
		}
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		connection.setConnectTimeout(1000 * 5);
		connection.setRequestProperty("Content-Type", "textml;charset=UTF-8");
		connection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
		connection.setRequestProperty("Charset", "UTF-8");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		StringBuffer body = new StringBuffer();
		OutputStream os = null;
		InputStream is = null;
		try {
			if (!"DELETE".equalsIgnoreCase(requestMethod)
					&& StringUtils.isNotEmpty(data)) {
				os = connection.getOutputStream();
				if (StringUtils.isNotEmpty(data)) {
					os.write(data.getBytes("UTF-8"));
				}
				os.flush();
			}
			if (connection.getResponseCode() == 200) {
				is = connection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "UTF-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					body.append(line);
				}
			} else {
				throw new IOException("responseCode="
						+ connection.getResponseCode());
			}
		} catch (IOException e) {
			log.error(e.toString(), e);
			throw e;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (os != null) {
				os.close();
			}
			if (is != null) {
				is.close();
			}
		}
		return body.toString();
	}
}
