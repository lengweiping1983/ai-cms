package com.ai.epg.template.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.AppGlobal;
import com.ai.common.enums.PageTypeEnum;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;
import com.ai.epg.template.entity.TemplateDefault;
import com.ai.epg.template.repository.TemplateDefaultRepository;

@Service
@Transactional(readOnly = true)
public class TemplateDefaultService extends
		AbstractService<TemplateDefault, Long> {

	public static String TEMPLATE_PATH_KEY_FORMAT = "%s#%d#%s";

	public static String TEMPLATE_CODE_KEY_FORMAT = "%s#%s#%d#%d#%s";

	private static Map<String, String> defaultTemplatePathMap = new HashMap<String, String>();

	private static Map<String, String> defaultTemplateCodeMap = new HashMap<String, String>();

	@Value("${spring.profiles.active:dev}")
	private String profiles;

	@Autowired
	private TemplateDefaultRepository templateDefaultRepository;

	@Override
	public AbstractRepository<TemplateDefault, Long> getRepository() {
		return templateDefaultRepository;
	}

	/**
	 * 初始化映射表格
	 */
	public void initDefaultTemplateConfig() {
		initDefaultTemplatePath();
		initDefaultTemplateCode();
	}

	public void initDefaultTemplatePath() {
		Map<String, String> map = new HashMap<String, String>();
		List<TemplateDefault> defaultTemplateList = templateDefaultRepository
				.findByEnvAndType(profiles, 1);
		if (defaultTemplateList == null || defaultTemplateList.size() <= 0) {
			defaultTemplateList = templateDefaultRepository.findByEnvAndType(
					"default", 1);
		}
		for (TemplateDefault template : defaultTemplateList) {
			String appCode = template.getAppCode();
			String categoryCode = template.getCategoryCode();
			if (StringUtils.isEmpty(appCode)) {
				appCode = "*";
			}
			if (StringUtils.isEmpty(categoryCode)) {
				categoryCode = "*";
			}
			String key = String.format(TEMPLATE_PATH_KEY_FORMAT, appCode,
					template.getItemType(), categoryCode);
			if (StringUtils.isNotEmpty(template.getPath())) {
				map.put(key, StringUtils.trimToEmpty(template.getPath()));
			}
		}
		defaultTemplatePathMap = map;
	}

	public void initDefaultTemplateCode() {
		Map<String, String> map = new HashMap<String, String>();
		List<TemplateDefault> defaultTemplateList = templateDefaultRepository
				.findByEnvAndType(profiles, 2);
		if (defaultTemplateList == null || defaultTemplateList.size() <= 0) {
			defaultTemplateList = templateDefaultRepository.findByEnvAndType(
					"default", 2);
		}
		for (TemplateDefault template : defaultTemplateList) {
			String pageType = template.getPageType();
			String appCode = template.getAppCode();
			String categoryCode = template.getCategoryCode();
			if (StringUtils.isEmpty(pageType)) {
				pageType = "*";
			}
			if (StringUtils.isEmpty(appCode)) {
				appCode = "*";
			}
			if (StringUtils.isEmpty(categoryCode)) {
				categoryCode = "*";
			}
			String key = String.format(TEMPLATE_CODE_KEY_FORMAT, pageType,
					appCode, template.getItemType(), template.getContentType(),
					categoryCode);
			if (StringUtils.isNotEmpty(template.getTemplateCode())) {
				map.put(key,
						StringUtils.trimToEmpty(template.getTemplateCode()));
			}
		}
		defaultTemplateCodeMap = map;
	}

	public String getTemplatePath(String appCode, Integer itemType,
			String categoryCode) {
		if (StringUtils.isEmpty(categoryCode)) {
			categoryCode = "*";
		}
		String templatePath = null;
		String key = "";
		// 递归查找配置
		key = String.format(TEMPLATE_PATH_KEY_FORMAT, appCode, itemType,
				categoryCode);
		templatePath = defaultTemplatePathMap.get(key);
		if (StringUtils.isEmpty(templatePath)) {
			key = String.format(TEMPLATE_PATH_KEY_FORMAT, appCode, itemType,
					"*");
			templatePath = defaultTemplatePathMap.get(key);
		}
		if (StringUtils.isEmpty(templatePath)) {
			key = String.format(TEMPLATE_PATH_KEY_FORMAT, "*", itemType, "*");
			templatePath = defaultTemplatePathMap.get(key);
		}
		return StringUtils.trimToEmpty(templatePath);
	}

	public String getItemTypeTemplateCode(String appCode, Integer itemType) {
		String templateCode = "DETAIL_TYPE" + itemType + "@SYSTEM";
		return StringUtils.trimToEmpty(templateCode);
	}

	public final String getOrderAppCode(String appCode) {
		String orderAppCode = AppGlobal.appCodeToChargeAppCodeMap.get(appCode);
		if (StringUtils.isNotEmpty(orderAppCode)) {
			return orderAppCode;
		}
		return appCode;
	}

	public String getOrderTemplateCode(PageTypeEnum pageType, String appCode) {
		String templateCode = null;
		String key = "";
		// 递归查找配置
		key = String.format(TEMPLATE_CODE_KEY_FORMAT, pageType.name(), appCode,
				0, 0, "*");
		templateCode = defaultTemplateCodeMap.get(key);
		if (StringUtils.isEmpty(templateCode)) {
			String orderAppCode = getOrderAppCode(appCode);
			key = String.format(TEMPLATE_CODE_KEY_FORMAT, pageType.name(),
					orderAppCode, 0, 0, "*");
			templateCode = defaultTemplateCodeMap.get(key);
		}
		if (StringUtils.isEmpty(templateCode)) {
			key = String.format(TEMPLATE_CODE_KEY_FORMAT, pageType.name(), "*",
					0, 0, "*");
			templateCode = defaultTemplateCodeMap.get(key);
		}
		if (StringUtils.isEmpty(templateCode)) {
			templateCode = pageType.name().toUpperCase() + "@SYSTEM";
		}
		return StringUtils.trimToEmpty(templateCode);
	}

	public String getTagTemplateCode(String appCode) {
		String templateCode = null;
		String key = "";
		// 递归查找配置
		key = String.format(TEMPLATE_CODE_KEY_FORMAT, PageTypeEnum.TAG,
				appCode, 0, 0, "*");
		templateCode = defaultTemplateCodeMap.get(key);
		if (StringUtils.isEmpty(templateCode)) {
			key = String.format(TEMPLATE_CODE_KEY_FORMAT, PageTypeEnum.TAG,
					"*", 0, 0, "*");
			templateCode = defaultTemplateCodeMap.get(key);
		}
		if (StringUtils.isEmpty(templateCode)) {
			templateCode = PageTypeEnum.TAG.name().toUpperCase() + "@SYSTEM";
		}
		return StringUtils.trimToEmpty(templateCode);
	}

	public String getStarTemplateCode(String appCode) {
		String templateCode = null;
		String key = "";
		// 递归查找配置
		key = String.format(TEMPLATE_CODE_KEY_FORMAT, PageTypeEnum.STAR,
				appCode, 0, 0, "*");
		templateCode = defaultTemplateCodeMap.get(key);
		if (StringUtils.isEmpty(templateCode)) {
			key = String.format(TEMPLATE_CODE_KEY_FORMAT, PageTypeEnum.STAR,
					"*", 0, 0, "*");
			templateCode = defaultTemplateCodeMap.get(key);
		}
		if (StringUtils.isEmpty(templateCode)) {
			templateCode = PageTypeEnum.STAR.name().toUpperCase() + "@SYSTEM";
		}
		return StringUtils.trimToEmpty(templateCode);
	}

	public String getDetailTemplateCode(String appCode, Integer itemType,
			Integer contentType, String categoryCode) {
		if (StringUtils.isEmpty(categoryCode)) {
			categoryCode = "*";
		}
		String templateCode = null;
		String key = "";
		// 递归查找配置
		key = String.format(TEMPLATE_CODE_KEY_FORMAT, PageTypeEnum.DETAIL,
				appCode, itemType, contentType, categoryCode);
		templateCode = defaultTemplateCodeMap.get(key);
		if (StringUtils.isEmpty(templateCode)) {
			key = String.format(TEMPLATE_CODE_KEY_FORMAT, PageTypeEnum.DETAIL,
					appCode, itemType, contentType, "*");
			templateCode = defaultTemplateCodeMap.get(key);
		}
		if (StringUtils.isEmpty(templateCode)) {
			key = String.format(TEMPLATE_CODE_KEY_FORMAT, PageTypeEnum.DETAIL,
					appCode, itemType, 0, "*");
			templateCode = defaultTemplateCodeMap.get(key);
		}
		if (StringUtils.isEmpty(templateCode)) {
			key = String.format(TEMPLATE_CODE_KEY_FORMAT, PageTypeEnum.DETAIL,
					"*", itemType, contentType, "*");
			templateCode = defaultTemplateCodeMap.get(key);
		}
		if (StringUtils.isEmpty(templateCode)) {
			key = String.format(TEMPLATE_CODE_KEY_FORMAT, PageTypeEnum.DETAIL,
					"*", itemType, 0, "*");
			templateCode = defaultTemplateCodeMap.get(key);
		}
		if (StringUtils.isEmpty(templateCode)) {
			templateCode = getItemTypeTemplateCode(appCode, itemType);
		}
		return StringUtils.trimToEmpty(templateCode);
	}

	public String getPlayTemplateCode(String appCode) {
		String templateCode = "PLAY@SYSTEM";
		return StringUtils.trimToEmpty(templateCode);
	}

	public String getPlayTemplateCode(String appCode, Integer itemType,
			Integer contentType, String categoryCode) {
		if (StringUtils.isEmpty(categoryCode)) {
			categoryCode = "*";
		}
		String templateCode = null;
		String key = "";
		// 递归查找配置
		key = String.format(TEMPLATE_CODE_KEY_FORMAT, PageTypeEnum.PLAY,
				appCode, itemType, contentType, categoryCode);
		templateCode = defaultTemplateCodeMap.get(key);
		if (StringUtils.isEmpty(templateCode)) {
			key = String.format(TEMPLATE_CODE_KEY_FORMAT, PageTypeEnum.PLAY,
					appCode, itemType, contentType, "*");
			templateCode = defaultTemplateCodeMap.get(key);
		}
		if (StringUtils.isEmpty(templateCode)) {
			key = String.format(TEMPLATE_CODE_KEY_FORMAT, PageTypeEnum.PLAY,
					appCode, itemType, 0, "*");
			templateCode = defaultTemplateCodeMap.get(key);
		}
		if (StringUtils.isEmpty(templateCode)) {
			key = String.format(TEMPLATE_CODE_KEY_FORMAT, PageTypeEnum.PLAY,
					"*", itemType, contentType, "*");
			templateCode = defaultTemplateCodeMap.get(key);
		}
		if (StringUtils.isEmpty(templateCode)) {
			key = String.format(TEMPLATE_CODE_KEY_FORMAT, PageTypeEnum.PLAY,
					"*", itemType, 0, "*");
			templateCode = defaultTemplateCodeMap.get(key);
		}
		if (StringUtils.isEmpty(templateCode)) {
			templateCode = getPlayTemplateCode(appCode);
		}
		return StringUtils.trimToEmpty(templateCode);
	}
	
	public String getPlayTemplateCode(String appCode, Integer itemType,
			Integer contentType, String categoryCode, String pageType) {
		if (StringUtils.isEmpty(categoryCode)) {
			categoryCode = "*";
		}
		String templateCode = null;
		String key = "";
		// 递归查找配置
		key = String.format(TEMPLATE_CODE_KEY_FORMAT, pageType, appCode,
				itemType, contentType, categoryCode);
		templateCode = defaultTemplateCodeMap.get(key);
		if (StringUtils.isEmpty(templateCode)) {
			key = String.format(TEMPLATE_CODE_KEY_FORMAT, pageType, appCode,
					itemType, contentType, "*");
			templateCode = defaultTemplateCodeMap.get(key);
		}
		if (StringUtils.isEmpty(templateCode)) {
			key = String.format(TEMPLATE_CODE_KEY_FORMAT, pageType, appCode,
					itemType, 0, "*");
			templateCode = defaultTemplateCodeMap.get(key);
		}
		if (StringUtils.isEmpty(templateCode)) {
			templateCode = getPlayTemplateCode(appCode, itemType, contentType,
					categoryCode);
		}
		return StringUtils.trimToEmpty(templateCode);
	}

	public String getErrorTemplateCode(String appCode) {
		String templateCode = null;
		String key = "";
		// 递归查找配置
		key = String.format(TEMPLATE_CODE_KEY_FORMAT, PageTypeEnum.ERROR,
				appCode, 0, 0, "*");
		templateCode = defaultTemplateCodeMap.get(key);
		if (StringUtils.isEmpty(templateCode)) {
			key = String.format(TEMPLATE_CODE_KEY_FORMAT, PageTypeEnum.ERROR,
					"*", 0, 0, "*");
			templateCode = defaultTemplateCodeMap.get(key);
		}
		if (StringUtils.isEmpty(templateCode)) {
			templateCode = PageTypeEnum.ERROR.name().toUpperCase() + "@SYSTEM";
		}
		return StringUtils.trimToEmpty(templateCode);
	}
}