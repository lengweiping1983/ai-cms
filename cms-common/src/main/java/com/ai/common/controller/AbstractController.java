package com.ai.common.controller;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.beanvalidator.BeanValidators;
import com.ai.common.exception.RestException;
import com.ai.common.exception.ServiceException;
import com.ai.common.exception.ValidateException;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;
import com.ai.common.utils.DateUtils;
import com.ai.common.utils.IpUtils;
import com.ai.common.utils.LoggerUtils;

public abstract class AbstractController {
	protected static final String SEARCH_KEY = "search_";

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;

	/**
	 * 消息实例对象
	 */
	@Autowired
	@Qualifier("errorMessageSource")
	protected MessageSource errorMessageSource;

	/**
	 * 获取错误消息.
	 *
	 * @param key
	 * @return
	 */
	protected String getMessage(int key) {
		return errorMessageSource.getMessage(String.valueOf(key), null, "",
				null);
	}

	/**
	 * 获取错误消息.
	 *
	 * @param key
	 * @return
	 */
	protected String getMessage(int key, String[] p) {
		return errorMessageSource.getMessage(String.valueOf(key), p, "", null);
	}

	/**
	 * 获取错误消息.
	 * 
	 * @param key
	 * @return
	 */
	protected String getMessage(String key) {
		return errorMessageSource.getMessage(key, null, "", null);
	}

	protected BaseResult newBaseResult(int resultCode) {
		String resultDesc = getMessage(resultCode);
		return new BaseResult(resultCode, resultDesc);
	}

	protected static <T> T checkResource(final T resource) {
		if (resource == null) {
			throw new RestException(HttpStatus.NOT_FOUND);
		}
		return resource;
	}

	/**
	 * 获取IP地址
	 *
	 * @param request
	 * @return
	 */
	protected String getIpAddr(HttpServletRequest request) {
		return IpUtils.getIpAddr(request);
	}

	protected String getRequestUrl(HttpServletRequest request) {
		StringBuffer sb = request.getRequestURL();
		String queryString = request.getQueryString();
		if (StringUtils.isNotEmpty(queryString)) {
			sb.append("?").append(queryString);
		}
		return sb.toString();
	}

	protected String getAllParam(HttpServletRequest request) {
		List<String> nameList = new ArrayList<String>();
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			nameList.add(name);
		}

		// Collections.sort(nameList, new Comparator<String>() {
		// public int compare(String o1, String o2) {
		// return o1.compareTo(o2);
		// }
		// });

		StringBuffer info = new StringBuffer("params:");
		for (String name : nameList) {
			String value = request.getParameter(name);
			info.append("<").append(name).append("=");
			info.append(value).append(">");
			if (names.hasMoreElements()) {
				info.append(",");
			}
		}
		return info.toString();
	}

	/**
	 * 实体对象参数有效性验证
	 * 
	 * @param object
	 *            验证的实体对象
	 * @param groups
	 *            验证组
	 */
	protected void beanValidator(Object object, Class<?>... groups) {
		BeanValidators.validateWithException(validator, object, groups);
	}

	/**
	 * 根据请求获取过滤条件
	 *
	 * @param request
	 * @return
	 */
	protected static Map<String, String> getParametersStartingWith(
			final HttpServletRequest request) {
		return getParametersStartingWith(request, SEARCH_KEY);
	}

	/**
	 * 取得带相同前缀的Request Parameters.
	 * <p/>
	 * 返回的结果Parameter名已去除前缀.
	 */
	protected static Map<String, String> getParametersStartingWith(
			final HttpServletRequest request, final String prefix) {
		String prefixStr = StringUtils.trimToEmpty(prefix);
		Map<String, String> params = new TreeMap<String, String>();

		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if (StringUtils.isEmpty(prefixStr)
					|| paramName.startsWith(prefixStr)) {
				String unprefixed = paramName.substring(prefixStr.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {
					// Do nothing, no values found at all.
				} else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}

	/**
	 * 根据请求获取查询对象
	 *
	 * @param request
	 * @param <T>
	 * @return
	 */
	protected static <T> List<PropertyFilter> getPropertyFilters(
			final HttpServletRequest request) {
		Map<String, String> searchMap = getParametersStartingWith(request);
		return getPropertyFilters(searchMap);
	}

	/**
	 * 根据过滤条件获取查询对象
	 *
	 * @param map
	 * @param <T>
	 * @return
	 */
	protected static <T> List<PropertyFilter> getPropertyFilters(
			final Map<String, String> map) {
		List<PropertyFilter> pfList = new ArrayList<PropertyFilter>();
		for (String key : map.keySet()) {
			if (StringUtils.isNotEmpty(map.get(key))) {
				pfList.add(new PropertyFilter(key, map.get(key)));
			}
		}
		return pfList;
	}

	/**
	 * 根据请求获取查询对象
	 *
	 * @param request
	 * 
	 * @param <T>
	 * @return
	 */
	protected static <T> Specification<T> getSpecification(
			final HttpServletRequest request) {
		List<PropertyFilter> pfList = getPropertyFilters(request);
		return SpecificationUtils.getSpecification(pfList);
	}

	/**
	 * 根据过滤条件获取查询对象
	 *
	 * @param map
	 * 
	 * @param <T>
	 * @return
	 */
	protected static <T> Specification<T> getSpecification(
			final Map<String, String> map) {
		List<PropertyFilter> pfList = getPropertyFilters(map);
		return SpecificationUtils.getSpecification(pfList);
	}

	/**
	 * 将搜索条件传递到页面
	 *
	 * @param request
	 * @param searchMap
	 */
	protected static void setAttribute(final HttpServletRequest request,
			final Map<String, String> searchMap) {
		for (String key : searchMap.keySet()) {
			String value = searchMap.get(key);
			if (StringUtils.isEmpty(value)) {// 过滤掉空值
				continue;
			}
			request.setAttribute(key, value);
		}
	}

	/**
	 * 初始化数据绑定 1. 将所有传递进来的String进行HTML编码，防止XSS攻击 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils
						.escapeHtml4(text.trim()));
			}

			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});
	}

	/**
	 * 根据请求获取查询结果
	 *
	 * @param request
	 * @param pageInfo
	 * @param abstractService
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T> Page<T> find(final HttpServletRequest request,
			final PageInfo pageInfo, final AbstractService abstractService) {
		Specification<T> specification = getSpecification(request);
		PageRequest pageRequest = PageInfo.getPageRequest(pageInfo);
		Page<T> page = abstractService.findAll(specification, pageRequest);
		return page;
	}

	/**
	 * 根据请求获取查询结果
	 *
	 * @param request
	 * @param pageInfo
	 * @param abstractRepository
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T> Page<T> find(final HttpServletRequest request,
			final PageInfo pageInfo, final AbstractRepository abstractRepository) {
		Specification<T> specification = getSpecification(request);
		PageRequest pageRequest = PageInfo.getPageRequest(pageInfo);
		Page<T> page = abstractRepository.findAll(specification, pageRequest);
		return page;
	}

	/**
	 * 根据请求获取查询结果
	 *
	 * @param map
	 * @param pageInfo
	 * @param abstractService
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T> Page<T> find(final Map<String, String> map,
			final PageInfo pageInfo, final AbstractService abstractService) {
		Specification<T> specification = getSpecification(map);
		PageRequest pageRequest = PageInfo.getPageRequest(pageInfo);
		Page<T> page = abstractService.findAll(specification, pageRequest);
		return page;
	}

	/**
	 * 根据请求获取查询结果
	 *
	 * @param map
	 * @param pageInfo
	 * @param abstractRepository
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T> Page<T> find(final Map<String, String> map,
			final PageInfo pageInfo, final AbstractRepository abstractRepository) {
		Specification<T> specification = getSpecification(map);
		PageRequest pageRequest = PageInfo.getPageRequest(pageInfo);
		Page<T> page = abstractRepository.findAll(specification, pageRequest);
		return page;
	}

	/**
	 * 根据请求获取查询结果
	 *
	 * @param specification
	 * @param pageInfo
	 * @param abstractService
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T> Page<T> find(Specification<T> specification,
			final PageInfo pageInfo, final AbstractService abstractService) {
		PageRequest pageRequest = PageInfo.getPageRequest(pageInfo);
		Page<T> page = abstractService.findAll(specification, pageRequest);
		return page;
	}

	/**
	 * 根据请求获取查询结果
	 *
	 * @param specification
	 * @param pageInfo
	 * @param abstractRepository
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T> Page<T> find(Specification<T> specification,
			final PageInfo pageInfo, final AbstractRepository abstractRepository) {
		PageRequest pageRequest = PageInfo.getPageRequest(pageInfo);
		Page<T> page = abstractRepository.findAll(specification, pageRequest);
		return page;
	}

	protected void infoMessage(String message) {
		LoggerUtils.infoMessage(logger, message);
	}

	protected void errorMessage(String message) {
		LoggerUtils.errorMessage(logger, message);
	}

	protected void errorMessage(int errorCode, String errorMessage) {
		LoggerUtils.errorMessage(logger, errorCode, errorMessage);
	}

	protected void errorMessage(int errorCode, String errorMessage, Exception e) {
		LoggerUtils.errorMessage(logger, errorCode, errorMessage, e);
	}

	protected void errorMessage(ServiceException e) {
		LoggerUtils.errorMessage(logger, e);
	}

	protected void errorMessage(ValidateException e) {
		LoggerUtils.errorMessage(logger, e);
	}

	protected void errorMessage(Exception e) {
		LoggerUtils.errorMessage(logger, e);
	}

}
