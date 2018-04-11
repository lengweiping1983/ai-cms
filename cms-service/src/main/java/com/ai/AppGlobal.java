package com.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ai.cms.config.entity.Cp;
import com.ai.cms.config.entity.MediaTemplate;
import com.ai.cms.config.repository.CpRepository;
import com.ai.cms.config.repository.MediaTemplateRepository;
import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.repository.InjectionPlatformRepository;
import com.ai.common.enums.MediaStatusEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.utils.CookieUtils;
import com.ai.common.utils.LoggerUtils;
import com.ai.epg.album.entity.Album;
import com.ai.epg.album.repository.AlbumRepository;
import com.ai.epg.category.entity.Category;
import com.ai.epg.category.repository.CategoryRepository;
import com.ai.epg.config.entity.App;
import com.ai.epg.config.repository.AppRepository;
import com.ai.epg.product.entity.Charge;
import com.ai.epg.product.repository.ChargeRepository;
import com.ai.epg.subscriber.entity.Subscriber;
import com.ai.epg.subscriber.repository.SubscriberRepository;
import com.ai.epg.template.service.TemplateDefaultService;
import com.ai.epg.template.service.MarkerService;
import com.ai.epg.widget.entity.Widget;
import com.ai.epg.widget.repository.WidgetRepository;

@Component
public class AppGlobal implements CommandLineRunner {

	private static final Logger logger = LoggerFactory
			.getLogger(AppGlobal.class);
	
	
	public static final int maxAge = -1;
	public static final int oneDayAge = 1 * 24 * 3600;

	public static final String TEMPLATE_PARAM_NUMBER_PRE = "NO";
	public static final String ALBUM_CODE_PRE = "A_";
	public static final String CATEGORY_CODE_PRE = "C_";
	public static final String WIDGET_CODE_PRE = "W_";
	public static final String URI_CODE_PRE = "P_";

	public static final String FEE_PRE = "Fee_";
	public static final String FEE_FREE = "Fee_Free";

	public static final String P_CONTENT_COOKIE_KEY = "_ckey";

	public static final String P_PARTNER_PROVIDER_ID = "proId";
	public static final String P_PARTNER_PROVIDER_CODE = "proCode";
	public static final String P_PARTNER_USERID = "userId";
	public static final String P_PARTNER_USERTOKEN = "userToken";
	public static final String P_PARTNER_EPGGROUP = "EPGGroup";
	public static final String P_SUBSCRIBER_ID = "subId";
	public static final String P_SUBSCRIBER_GROUP_CODE = "sgcode";

	public static final String P_PARTNER_EPGINFO = "_epg_info";
	public static final String P_PARTNER_MONTHLY_ORDERED = "userXYZ";
	public static final String P_JUMP_PARAM = "jParam";
	public static final String P_FROM_ENTRANCE = "fEntrance";

	public static Map<String, String> cpIdToCodeMap = new HashMap<String, String>();
	public static Map<Long, String> albumIdToCodeMap = new HashMap<Long, String>();
	public static Map<String, Long> albumCodeToIdMap = new HashMap<String, Long>();
	public static Map<Long, String> categoryIdToCodeMap = new HashMap<Long, String>();
	public static Map<String, Long> categoryCodeToIdMap = new HashMap<String, Long>();
	public static Map<Long, String> widgetIdToCodeMap = new HashMap<Long, String>();
	public static Map<String, Long> widgetCodeToIdMap = new HashMap<String, Long>();
	public static Map<String, String> appCodeToChargeAppCodeMap = new HashMap<String, String>();
	public static Map<String, List<Charge>> appToChargeListMap = new HashMap<String, List<Charge>>();
	public static Map<String, String> injectionPlatformIdToCspIdMap = new HashMap<String, String>();
	public static Map<Long, MediaTemplate> mediaTemplateMap = new HashMap<Long, MediaTemplate>();
	public static Map<String, String> markerMap = new HashMap<String, String>();

	private static CpRepository cpRepository;

	private static AlbumRepository albumRepository;

	private static CategoryRepository categoryRepository;

	private static WidgetRepository widgetRepository;

	private static SubscriberRepository subscriberRepository;

	private static ChargeRepository chargeRepository;

	private static AppRepository appRepository;

	private static InjectionPlatformRepository injectionPlatformRepository;

	private static MediaTemplateRepository mediaTemplateRepository;
	
	private static TemplateDefaultService defaultTemplateService;
	
	private static MarkerService markerService;

	@Autowired
	public void setCpRepository(CpRepository cpRepository) {
		AppGlobal.cpRepository = cpRepository;
	}

	@Autowired
	public void setAlbumRepository(AlbumRepository albumRepository) {
		AppGlobal.albumRepository = albumRepository;
	}

	@Autowired
	public void setCategoryRepository(CategoryRepository categoryRepository) {
		AppGlobal.categoryRepository = categoryRepository;
	}

	@Autowired
	public void setWidgetRepository(WidgetRepository widgetRepository) {
		AppGlobal.widgetRepository = widgetRepository;
	}

	@Autowired
	public void setSubscriberRepository(
			SubscriberRepository subscriberRepository) {
		AppGlobal.subscriberRepository = subscriberRepository;
	}

	@Autowired
	public void setChargeRepository(ChargeRepository chargeRepository) {
		AppGlobal.chargeRepository = chargeRepository;
	}

	@Autowired
	public void setAppRepository(AppRepository appRepository) {
		AppGlobal.appRepository = appRepository;
	}

	@Autowired
	public void setInjectionPlatformRepository(
			InjectionPlatformRepository injectionPlatformRepository) {
		AppGlobal.injectionPlatformRepository = injectionPlatformRepository;
	}

	public static MediaTemplateRepository getMediaTemplateRepository() {
		return mediaTemplateRepository;
	}

	@Autowired
	public void setMediaTemplateRepository(
			MediaTemplateRepository mediaTemplateRepository) {
		AppGlobal.mediaTemplateRepository = mediaTemplateRepository;
	}

	@Autowired
	public void setDefaultTemplateService(
			TemplateDefaultService defaultTemplateService) {
		AppGlobal.defaultTemplateService = defaultTemplateService;
	}
	
	@Autowired
	public void setMarkerService(
			MarkerService markerService) {
		AppGlobal.markerService = markerService;
	}

	@Override
	public void run(String... args) {
		logger.info("AppGlobal startup load data ...");
		fixedTimeLoadData();
		startUpLoadData();
		
//		daytxt2save();
//		weektxt2save();
//		monthtxt2save();
	}

	@Scheduled(cron = "${load.data.schedule:0 0/10 * * * ?}")
	public void execute() {
		logger.info("AppGlobal load data ...");
		fixedTimeLoadData();
	}

	/**
	 * 固定的时间内加载数据库数据到内存中
	 */
	private void fixedTimeLoadData() {
		try {
			loadCpData();
			loadAlbumData();
			loadCategoryData();
			loadWidgetData();
			loadChargeData();
			loadInjectionPlatformData();
//			LoadTagMaps.LoadPrograms();
//			LoadTagMaps.LoadSeries();
			defaultTemplateService.initDefaultTemplateConfig();
		} catch (Exception e) {

		}
	}

	/**
	 * 启动时加载数据库数据到内存中
	 */
	private void startUpLoadData() {
		try {
			loadMediaTemplateData();
			markerMap = markerService.getMarkerMap();
		} catch (Exception e) {

		}
	}

	private void loadCpData() {
		List<Cp> cpList = cpRepository.findAll();
		if (cpList != null) {
			for (Cp cp : cpList) {
				cpIdToCodeMap.put("" + cp.getId(),
						StringUtils.trimToEmpty(cp.getCode()));
			}
		}
	}

	private void loadAlbumData() {
		List<Album> albumList = albumRepository.findAllAlbum();
		if (albumList != null) {
			for (Album album : albumList) {
				String code = StringUtils.trimToEmpty(album.getCode()) + "@"
						+ StringUtils.trimToEmpty(album.getAppCode());
				albumIdToCodeMap.put(album.getId(), code);
				albumCodeToIdMap.put(code, album.getId());
			}
		}
	}

	private void loadCategoryData() {
		List<Category> categoryList = categoryRepository.findAllCategory();
		if (categoryList != null) {
			for (Category category : categoryList) {
				String code = StringUtils.trimToEmpty(category.getCode()) + "@"
						+ StringUtils.trimToEmpty(category.getAppCode());
				categoryIdToCodeMap.put(category.getId(), code);
				categoryCodeToIdMap.put(code, category.getId());
			}
		}
	}

	private void loadWidgetData() {
		List<Widget> widgetList = widgetRepository.findAllWidget();
		if (widgetList != null) {
			for (Widget widget : widgetList) {
				String code = StringUtils.trimToEmpty(widget.getCode()) + "@"
						+ StringUtils.trimToEmpty(widget.getAppCode());
				widgetIdToCodeMap.put(widget.getId(), code);
				widgetCodeToIdMap.put(code, widget.getId());
			}
		}
	}

	private void loadChargeData() {
		List<App> appList = appRepository.findAllOnlineApp();
		List<Charge> chargeList = chargeRepository.findAllChargeByOnline();
		for (App app : appList) {
			if (app.getAloneCharge() != null
					&& app.getAloneCharge() == YesNoEnum.NO.getKey()
					&& StringUtils.isNotEmpty(app.getChargeAppCode())) {
				appCodeToChargeAppCodeMap.put(app.getCode(),
						StringUtils.trimToEmpty(app.getChargeAppCode()));
			} else {
				appCodeToChargeAppCodeMap.put(app.getCode(), app.getCode());
			}
			
			List<Charge> appChargeList = new ArrayList<Charge>();
			for (Charge charge : chargeList) {
				boolean flag = false;
				if (StringUtils.isEmpty(charge.getAppCodes())
						|| "*".equals(charge.getAppCodes())) {// 全部应用共享
					flag = true;
				} else if (StringUtils.isNotEmpty(charge.getAppCodes())
						&& charge.getAppCodes().contains(app.getCode())) {// 已共享到该应用
					flag = true;
				} else if (app.getCode().equalsIgnoreCase(charge.getAppCode())) {// 等于所属应用
					flag = true;
				}
				if (flag && !appChargeList.contains(charge)) {
					appChargeList.add(charge);
				}
			}
			appToChargeListMap.put(app.getCode(), appChargeList);
		}
	}

	private void loadInjectionPlatformData() {
		List<InjectionPlatform> injectionPlatformList = injectionPlatformRepository
				.findAll();
		if (injectionPlatformList != null) {
			for (InjectionPlatform injectionPlatform : injectionPlatformList) {
				injectionPlatformIdToCspIdMap.put(
						"" + injectionPlatform.getId(),
						StringUtils.trimToEmpty(injectionPlatform.getCspId()));
			}
		}
	}

	private void loadMediaTemplateData() {
		List<MediaTemplate> mediaTemplateList = mediaTemplateRepository
				.findAll();
		if (mediaTemplateList != null) {
			for (MediaTemplate mediaTemplate : mediaTemplateList) {
				mediaTemplateMap.put(mediaTemplate.getId(),
						mediaTemplate);
			}
		}
	}

	public static String getCpCode(Long cpId) {
		return getCpCode("" + cpId);
	}

	public static String getCpCode(String cpId) {
		if (cpId != null) {
			String cpCode = AppGlobal.cpIdToCodeMap.get("" + cpId);
			return StringUtils.trimToEmpty(cpCode);
		}
		return "";
	}

	public static String profiles;

	public static Integer providerTypeId;

	public static String providerType;

	public static boolean checkLookBackCode = false;

	public static boolean chargeOnlyLive = false;

	public static String webAccessUrl;

	public static String partnerBackUrl;

	public static String imageWebPath;

	public static String getProfiles() {
		return profiles;
	}

	@Value("${spring.profiles.active:dev}")
	public void setProfiles(String profiles) {
		AppGlobal.profiles = profiles;
	}

	public static String getProviderType() {
		return providerType;
	}

	@Value("${site.providerType:}")
	public void setProviderType(String providerType) {
		AppGlobal.providerType = providerType;
		if (StringUtils.isNotEmpty(providerType) && !providerType.contains(",")) {
			try {
				providerTypeId = Integer.valueOf(StringUtils
						.trimToEmpty(providerType));
			} catch (Exception e) {

			}
		}
	}

	public static boolean isCheckLookBackCode() {
		return checkLookBackCode;
	}

	@Value("${site.checkLookBackCode:false}")
	public void setCheckLookBackCode(boolean checkLookBackCode) {
		AppGlobal.checkLookBackCode = checkLookBackCode;
	}

	public static boolean isChargeOnlyLive() {
		return chargeOnlyLive;
	}

	@Value("${charge.only.live:false}")
	public void setChargeOnlyLive(boolean chargeOnlyLive) {
		AppGlobal.chargeOnlyLive = chargeOnlyLive;
	}

	public static String getWebAccessUrl() {
		return webAccessUrl;
	}

	@Value("${web.access.url:}")
	public void setWebAccessUrl(String webAccessUrl) {
		AppGlobal.webAccessUrl = webAccessUrl;
	}

	public static String getPartnerBackUrl() {
		return partnerBackUrl;
	}

	@Value("${partner.backUrl:}")
	public void setPartnerBackUrl(String partnerBackUrl) {
		AppGlobal.partnerBackUrl = partnerBackUrl;
	}

	public static String getImageWebPath() {
		return imageWebPath;
	}

	@Value("${image.web.path:}")
	public void setImageWebPath(String imageWebPath) {
		AppGlobal.imageWebPath = imageWebPath;
	}

	public static String getImagePath(String path) {
		if (StringUtils.isEmpty(path)) {
			return "";
		}
		if (path.indexOf("http") == 0) {
			return path;
		}
		return StringUtils.trimToEmpty(getImageWebPath())
				+ StringUtils.trimToEmpty(path);
	}

	/**
	 * 缩进字符串
	 * 
	 * @param str
	 * @param maxWidth
	 * @return
	 */
	public static String abbreviate(String str, int maxWidth) {
		if (str == null) {
			return "";
		}
		if (str.length() <= maxWidth) {
			return str;
		}
		final String abrevMarker = "...";
		return str.substring(0, maxWidth - 1) + abrevMarker;
	}

	/**
	 * 截取字符
	 * 
	 * @param source
	 * @param maxWidth
	 * @return
	 */
	public static String cutCharacter(String source, int maxWidth) {
		String sources = source.trim();
		if (sources == null) {
			return "";
		} else if (sources.length() > maxWidth) {
			sources = source.substring(0, maxWidth);
		}
		return sources;
	}

	/**
	 * 缩进字符串and 换行
	 * 
	 * @param source
	 * @param maxWidth
	 * @return
	 */
	public static String reviateWrapSpace(String source, int maxWidth) {
		if (source == null) {
			return "";
		}
		String result = abbreviate(source, maxWidth).replace("\r\n",
				"<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		return result;
	}

	// 赶考标清的课程
	public static String reviateWrapSpacesd(String source, int maxWidth) {
		if (source == null) {
			return "";
		}
		String result = abbreviate(source, maxWidth).replace("\r\n", "<br/>");
		return result;
	}

	// 赶考标清的新闻
	public static String reviateWrapSpacesdNews(String source, int maxWidth) {
		if (source == null) {
			return "";
		}
		String result = abbreviate(source, maxWidth).replace("\r\n", "<br/>");
		return result;
	}

	public static String getMediaStatusDesc(int mediaStatus) {
		StringBuffer sb = new StringBuffer();
		if (mediaStatus == MediaStatusEnum.OK.getKey()) {
			sb.append("<span class=\"badge badge-success\">")
					.append(MediaStatusEnum.OK.getValue()).append("</span>");
			return sb.toString();
		}

		if (isMediaStatusOpen(mediaStatus, MediaStatusEnum.DOWNLOAD.getKey())) {
			sb.append("<span class=\"badge badge-info\">")
					.append(MediaStatusEnum.DOWNLOAD.getValue())
					.append("</span>");
		}

		if (isMediaStatusOpen(mediaStatus,
				MediaStatusEnum.DOWNLOAD_FAIL.getKey())) {
			sb.append("<span class=\"badge badge-danger\">")
					.append(MediaStatusEnum.DOWNLOAD_FAIL.getValue())
					.append("</span>");
		}

		if (isMediaStatusOpen(mediaStatus,
				MediaStatusEnum.DOWNLOAD_PART_FAIL.getKey())) {
			sb.append("<span class=\"badge badge-danger\">")
					.append(MediaStatusEnum.DOWNLOAD_PART_FAIL.getValue())
					.append("</span>");
		}

		if (isMediaStatusOpen(mediaStatus, MediaStatusEnum.MISS_VIDEO.getKey())) {
			sb.append("<span class=\"badge badge-danger\">")
					.append(MediaStatusEnum.MISS_VIDEO.getValue())
					.append("</span>");
		}

		if (isMediaStatusOpen(mediaStatus, MediaStatusEnum.MISS_IMAGE.getKey())) {
			sb.append("<span class=\"badge badge-danger\">")
					.append(MediaStatusEnum.MISS_IMAGE.getValue())
					.append("</span>");
		}

		return sb.toString();
	}

	public static boolean isMediaStatusOpen(int mediaStatus, int mask) {
		if (mask <= 0) {
			return false;
		}
		int index = (mediaStatus / mask);
		if (index % 10 == 1) {
			return true;
		}
		return false;
	}

	public static int setMediaStatusOpen(int mediaStatus, int... masks) {
		int result = mediaStatus;
		for (int mask : masks) {
			if (!isMediaStatusOpen(result, mask)) {
				result += mask;
			}
		}
		return result;
	}

	public static int setMediaStatusClose(int mediaStatus, int... masks) {
		int result = mediaStatus;
		for (int mask : masks) {
			if (isMediaStatusOpen(result, mask)) {
				result -= mask;
			}
		}
		return result;
	}

	public static boolean isStatusOpen(int status, int mask) {
		if (mask <= 0) {
			return false;
		}
		int index = (status / mask);
		if (index % 10 == 1) {
			return true;
		}
		return false;
	}

	public static int setStatusOpen(int status, int... masks) {
		int result = status;
		for (int mask : masks) {
			if (!isStatusOpen(result, mask)) {
				result += mask;
			}
		}
		return result;
	}

	public static int setStatusClose(int status, int... masks) {
		int result = status;
		for (int mask : masks) {
			if (isStatusOpen(result, mask)) {
				result -= mask;
			}
		}
		return result;
	}

	/**
	 * 获取包月是否已订购标记
	 * 
	 * @param request
	 * @return
	 */
	public static String getMonthlyOrderedFromCookie(HttpServletRequest request) {
		String value = CookieUtils.getCookieValueByName(request,
				AppGlobal.P_PARTNER_MONTHLY_ORDERED, 0);
		return value;
	}

	/**
	 * 设置包月是否已订购标记
	 * 
	 * @param response
	 * @param value
	 */
	public static void setMonthlyOrderedToCookie(HttpServletResponse response,
			String value) {
		CookieUtils.addCookie(response, AppGlobal.P_PARTNER_MONTHLY_ORDERED,
				value, AppGlobal.oneDayAge, "/", 0);
	}

	/**
	 * 清理包月是否已订购标记
	 * 
	 * @param request
	 * @param response
	 */
	public static void clearMonthlyOrderedToCookie(HttpServletRequest request,
			HttpServletResponse response) {
		CookieUtils.clearCookie(request, response,
				AppGlobal.P_PARTNER_MONTHLY_ORDERED, "/");
	}

	/**
	 * 获取运营商EGPINFO
	 * 
	 * @param request
	 * @return
	 */
	public static String getEPGInfoFromCookie(HttpServletRequest request) {
		String EPGInfo = CookieUtils.getCookieValueByName(request,
				AppGlobal.P_PARTNER_EPGINFO, 1);
		return EPGInfo;
	}

	/**
	 * 设置运营商EGPINFO
	 * 
	 * @param response
	 * @param EPGInfo
	 */
	public static void setEPGInfoToCookie(HttpServletResponse response,
			String EPGInfo) {
		CookieUtils.addCookie(response, AppGlobal.P_PARTNER_EPGINFO, EPGInfo,
				AppGlobal.maxAge, "/", 1);
	}

	/**
	 * 获取跳转参数
	 * 
	 * @param request
	 * @return
	 */
	public static String getJumpParamFromCookie(HttpServletRequest request) {
		String jumpParam = CookieUtils.getCookieValueByName(request,
				AppGlobal.P_JUMP_PARAM, 1);
		return jumpParam;
	}

	/**
	 * 设置跳转参数
	 * 
	 * @param response
	 * @param jumpParam
	 */
	public static void setJumpParamToCookie(HttpServletResponse response,
			String jumpParam) {
		CookieUtils.addCookie(response, AppGlobal.P_JUMP_PARAM, jumpParam,
				AppGlobal.maxAge, "/", 1);
	}

	/**
	 * 获取入口位置参数
	 * 
	 * @param request
	 * @return
	 */
	public static String getFromEntranceFromCookie(HttpServletRequest request) {
		String fromEntrance = CookieUtils.getCookieValueByName(request,
				AppGlobal.P_FROM_ENTRANCE, 0);
		return fromEntrance;
	}

	/**
	 * 设置入口位置参数
	 * 
	 * @param response
	 * @param fromEntrance
	 */
	public static void setFromEntranceToCookie(HttpServletResponse response,
			String fromEntrance) {
		CookieUtils.addCookie(response, AppGlobal.P_FROM_ENTRANCE,
				fromEntrance, AppGlobal.maxAge, "/", 0);
	}

	/********************* 如下几个值放到同一个Cookie中 **********************/

	/**
	 * Map转换成String
	 * 
	 * @param map
	 * @return
	 */
	public static String mapToString(Map<String, String> map) {
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
		for (; iterator.hasNext();) {
			Entry<String, String> entry = iterator.next();
			if (StringUtils.isEmpty(entry.getKey())
					|| StringUtils.isEmpty(entry.getValue())) {
				continue;
			}
			sb.append(entry.getKey()).append("^^^").append(entry.getValue())
					.append("$$$");
		}
		return sb.toString();
	}

	/**
	 * String转换成Map
	 * 
	 * @param mapString
	 * @return
	 */
	public static Map<String, String> stringToMap(String mapString) {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isNotEmpty(mapString)) {
			StringTokenizer entrys = new StringTokenizer(mapString, "$$$");
			for (; entrys.hasMoreTokens();) {
				StringTokenizer items = new StringTokenizer(entrys.nextToken(),
						"^^^");
				map.put(items.nextToken(),
						items.hasMoreTokens() ? items.nextToken() : "");
			}
		}
		return map;
	}

	/**
	 * index进来时设置userId,userToken到Cookie中，nginx访问日志需要
	 * 
	 * @param request
	 * @param response
	 * @param partnerUserId
	 * @param partnerUserToken
	 */
	public static void initUserIdAndUserTokenToCookie(
			HttpServletRequest request, HttpServletResponse response,
			String partnerUserId, String partnerUserToken) {
		if (StringUtils.isEmpty(partnerUserId)) {
			return;
		}
		Subscriber subscriber = new Subscriber();
		subscriber.setPartnerUserId(StringUtils.trimToEmpty(partnerUserId));
		subscriber.setPartnerUserToken(StringUtils
				.trimToEmpty(partnerUserToken));
		addCookie(request, response, subscriber);
	}

	public static void addCookie(HttpServletRequest request,
			HttpServletResponse response, Subscriber subscriber) {
		if (subscriber == null) {
			return;
		}
		Map<String, String> map = new HashMap<String, String>();

		if (StringUtils.isNotEmpty(subscriber.getGroupCode())) {
			map.put(AppGlobal.P_SUBSCRIBER_GROUP_CODE, Base64
					.encodeBase64URLSafeString(subscriber.getGroupCode()
							.getBytes()));
		}
		if (subscriber.getId() != null) {
			map.put(AppGlobal.P_SUBSCRIBER_ID, "" + subscriber.getId());
		}
		if (subscriber.getProviderId() != null) {
			map.put(AppGlobal.P_PARTNER_PROVIDER_ID, subscriber.getProviderId());
		}
		if (StringUtils.isNotEmpty(subscriber.getProviderCode())) {
			map.put(AppGlobal.P_PARTNER_PROVIDER_CODE, Base64
					.encodeBase64URLSafeString(subscriber.getProviderCode()
							.getBytes()));
		}
		if (StringUtils.isNotEmpty(subscriber.getPartnerGroupCode())) {
			map.put(AppGlobal.P_PARTNER_EPGGROUP, Base64
					.encodeBase64URLSafeString(subscriber.getPartnerGroupCode()
							.getBytes()));
		}
		if (StringUtils.isNotEmpty(subscriber.getPartnerUserId())) {
			map.put(AppGlobal.P_PARTNER_USERID, Base64
					.encodeBase64URLSafeString(subscriber.getPartnerUserId()
							.getBytes()));
		}
		if (StringUtils.isNotEmpty(subscriber.getPartnerUserToken())) {
			map.put(AppGlobal.P_PARTNER_USERTOKEN, Base64
					.encodeBase64URLSafeString(subscriber.getPartnerUserToken()
							.getBytes()));
		}
		String mapString = mapToString(map);
		LoggerUtils.infoMessage(logger, mapString);
		CookieUtils.addCookie(response, AppGlobal.P_CONTENT_COOKIE_KEY,
				mapString, AppGlobal.maxAge, "/", 0);
		request.setAttribute(AppGlobal.P_CONTENT_COOKIE_KEY, mapString);
	}

	/**
	 * 获取运营商用户Id
	 * 
	 * @param request
	 * @return
	 */
	public static String getUserIdFromCookie(HttpServletRequest request) {
		String userId = _getCookieValueByName(request,
				AppGlobal.P_PARTNER_USERID, 1);
		if (StringUtils.isEmpty(userId)) {
			userId = CookieUtils.getCookieValueByName(request,
					AppGlobal.P_PARTNER_USERID, 1);
		}
		return userId;
	}

	// /**
	// * 设置运营商用户Id
	// *
	// * @param response
	// * @param userId
	// */
	// public static void setUserIdToCookie(HttpServletResponse response,
	// String userId) {
	// CookieUtils.addCookie(response, AppGlobal.P_PARTNER_USERID, userId,
	// AppGlobal.maxAge, "/", 1);
	// }

	public static String _getCookieValueByName(HttpServletRequest request,
			String name, int encodeType) {
		if (StringUtils.isEmpty(name)) {
			return "";
		}
		String contentCookieKey = (String) request
				.getAttribute(AppGlobal.P_CONTENT_COOKIE_KEY);
		if (StringUtils.isEmpty(contentCookieKey)) {
			contentCookieKey = CookieUtils.getCookieValueByName(request,
					AppGlobal.P_CONTENT_COOKIE_KEY, 0);
		}
		if (StringUtils.isEmpty(contentCookieKey)) {// 为空的情况来，到数据库获取
			String sessionId = (request.getSession() != null) ? request
					.getSession().getId() : "";
			if (StringUtils.isNotEmpty(sessionId)) {
				List<Subscriber> subscriberList = subscriberRepository
						.findReserved5(sessionId);
				if (subscriberList != null && subscriberList.size() > 0) {
					Subscriber subscriber = subscriberList.get(0);
					HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes()).getResponse();
					addCookie(request, response, subscriber);
				}
			}
		}
		Map<String, String> map = stringToMap(contentCookieKey);
		String value = map.get(name);
		if (StringUtils.isNotEmpty(value) && encodeType == 1) {
			value = new String(Base64.decodeBase64(value));
		}
		return StringUtils.trimToEmpty(value);
	}

	/**
	 * 获取运营商用户Token
	 * 
	 * @param request
	 * @return
	 */
	public static String getUserTokenFromCookie(HttpServletRequest request) {
		String userToken = _getCookieValueByName(request,
				AppGlobal.P_PARTNER_USERTOKEN, 1);
		if (StringUtils.isEmpty(userToken)) {
			userToken = CookieUtils.getCookieValueByName(request,
					AppGlobal.P_PARTNER_USERTOKEN, 1);
		}
		return userToken;
	}

	/**
	 * 设置运营商用户Token
	 * 
	 * @param response
	 * @param userToken
	 */
	// public static void setUserTokenToCookie(HttpServletResponse response,
	// String userToken) {
	// CookieUtils.addCookie(response, AppGlobal.P_PARTNER_USERTOKEN,
	// userToken, AppGlobal.maxAge, "/", 1);
	// }

	/**
	 * 获取运营商EPG分组代码
	 * 
	 * @param request
	 * @return
	 */
	public static String getEPGGroupFromCookie(HttpServletRequest request) {
		String EPGGroup = _getCookieValueByName(request,
				AppGlobal.P_PARTNER_EPGGROUP, 1);
		return EPGGroup;
	}

	/**
	 * 设置运营商EPG分组代码
	 * 
	 * @param response
	 * @param EPGGroup
	 */
	// public static void setEPGGroupToCookie(HttpServletResponse response,
	// String EPGGroup) {
	// CookieUtils.addCookie(response, AppGlobal.P_PARTNER_EPGGROUP, EPGGroup,
	// AppGlobal.maxAge, "/", 1);
	// }

	/**
	 * 获取用户Id,本系统的用户Id
	 * 
	 * @param request
	 * @return
	 */
	public static String getSubscriberIdFromCookie(HttpServletRequest request) {
		String subscriberId = _getCookieValueByName(request,
				AppGlobal.P_SUBSCRIBER_ID, 0);
		if (StringUtils.isEmpty(subscriberId)) {
			subscriberId = CookieUtils.getCookieValueByName(request,
					AppGlobal.P_SUBSCRIBER_ID, 1);
		}
		return subscriberId;
	}

	/**
	 * 设置用户Id,本系统的用户Id
	 * 
	 * @param response
	 * @param subscriberId
	 */
	// public static void setSubscriberIdToCookie(HttpServletResponse response,
	// String subscriberId) {
	// CookieUtils.addCookie(response, AppGlobal.P_SUBSCRIBER_ID,
	// subscriberId, AppGlobal.maxAge, "/", 1);
	// }

	public static String getSubscriberGroupCode() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		String subscriberGroupCode = (String) request
				.getAttribute(AppGlobal.P_SUBSCRIBER_GROUP_CODE);
		if (StringUtils.isEmpty(subscriberGroupCode)) {
			subscriberGroupCode = AppGlobal.getGroupCodeFromCookie(request);
		}
		return subscriberGroupCode;
	}

	/**
	 * 获取用户分组,本系统的用户分组
	 * 
	 * @param request
	 * @return
	 */
	public static String getGroupCodeFromCookie(HttpServletRequest request) {
		String groupCode = _getCookieValueByName(request,
				AppGlobal.P_SUBSCRIBER_GROUP_CODE, 1);
		return groupCode;
	}

	/**
	 * 设置用户分组,本系统的用户分组
	 * 
	 * @param response
	 * @param groupCode
	 */
	// public static void setGroupCodeToCookie(HttpServletResponse response,
	// String groupCode) {
	// CookieUtils.addCookie(response, AppGlobal.P_SUBSCRIBER_GROUP_CODE,
	// StringUtils.trimToEmpty(groupCode), AppGlobal.maxAge, "/", 1);
	// }

	/**
	 * 获取提供商Id,如华为,中兴
	 * 
	 * @param request
	 * @return
	 */
	public static String getProviderIdFromCookie(HttpServletRequest request) {
		String providerId = _getCookieValueByName(request,
				AppGlobal.P_PARTNER_PROVIDER_ID, 0);
		if (StringUtils.isEmpty(providerId)) {
			providerId = CookieUtils.getCookieValueByName(request,
					AppGlobal.P_PARTNER_PROVIDER_ID, 1);
		}
		return providerId;
	}

	/**
	 * 设置提供商Id,如华为,中兴
	 * 
	 * @param response
	 * @param providerId
	 */
	// public static void setProviderIdToCookie(HttpServletResponse response,
	// String providerId) {
	// CookieUtils.addCookie(response, AppGlobal.P_PARTNER_PROVIDER_ID,
	// providerId, AppGlobal.maxAge, "/", 1);
	// }

	/**
	 * 获取提供商代码,如华为,中兴
	 * 
	 * @param request
	 * @return
	 */
	public static String getProviderCodeFromCookie(HttpServletRequest request) {
		String providerCode = _getCookieValueByName(request,
				AppGlobal.P_PARTNER_PROVIDER_CODE, 1);
		if (StringUtils.isEmpty(providerCode)) {
			providerCode = CookieUtils.getCookieValueByName(request,
					AppGlobal.P_PARTNER_PROVIDER_CODE, 1);
		}
		return providerCode;
	}

	/**
	 * 设置提供商代码,如华为,中兴
	 * 
	 * @param response
	 * @param providerCode
	 */
	// public static void setProviderCodeToCookie(HttpServletResponse response,
	// String providerCode) {
	// CookieUtils.addCookie(response, AppGlobal.P_PARTNER_PROVIDER_CODE,
	// providerCode, AppGlobal.maxAge, "/", 1);
	// }
	
	
}
