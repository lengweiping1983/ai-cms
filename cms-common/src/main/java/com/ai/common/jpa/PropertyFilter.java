package com.ai.common.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 过滤条件封装类
 */
public class PropertyFilter {

    // 属性比较类型。依次是等于，不等于，LIKE，小于，大于，小于等于，大于等于，包含，不包含
    public enum MatchType {
        EQ, NQ, LIKE, LT, GT, LE, GE, IN, NIN, ISNULL, ISEMPTY, BITMASK, CHARMASK, INMASK;
    }

    // 属性数据类型.
    public enum PropertyType {
        S(String.class), I(Integer.class), L(Long.class), N(Double.class), D(Date.class);

        private Class<?> clazz;

        PropertyType(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> getValue() {
            return clazz;
        }
    }

    private String propertyName = null;

    private MatchType matchType = null;

    private Class<?> propertyType = null;

    private Object propertyValue = null;

    private List<PropertyFilter> subList = null;

    public PropertyFilter() {

    }

    public PropertyFilter(String propertyName, MatchType matchType, Class<?> propertyType, Object propertyValue) {
        this.propertyName = propertyName;
        this.matchType = matchType;
        this.propertyType = propertyType;
        this.propertyValue = propertyValue;
    }

    /**
     * @param filterName
     *            比较属性字符串,含待比较的比较类型、属性值类型及属性列表. 例如：userName__LIKE_S 或者
     *            module_id__EQ_I(字段module.id)
     * @param value
     *            待比较的值.
     */
    public PropertyFilter(final String filterName, final String value) {
        String matchTypeStr = StringUtils.substringAfter(filterName, "__");
        String matchTypeCode = StringUtils.substringBefore(matchTypeStr, "_");
        String propertyTypeCode = StringUtils.substringAfter(matchTypeStr, "_").substring(0, 1);

        try {
            matchType = Enum.valueOf(MatchType.class, matchTypeCode);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("filter名称[" + filterName + "]没有按规则编写,无法得到属性比较类型." + matchTypeCode, e);
        }

        try {
            propertyType = Enum.valueOf(PropertyType.class, propertyTypeCode).getValue();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("filter名称[" + filterName + "]没有按规则编写,无法得到属性值类型." + propertyTypeCode, e);
        }

        propertyName = StringUtils.substringBefore(filterName, "__").trim();
        if (null == value) {
            this.propertyValue = "";
        } else {
            this.propertyValue = value.trim();
        }
        if (this.propertyValue != null
				&& StringUtils.isNotEmpty(this.propertyValue.toString())
				&& ("L".equals(propertyTypeCode) || "I"
						.equals(propertyTypeCode))) {
			try {
				Long.valueOf(this.propertyValue.toString());
			} catch (Exception e) {
				this.propertyValue = "-99999";
			}
		}
    }

    /**
     * 将搜索条件进行转换
     */
    public static List<PropertyFilter> parse(final Map<String, String> searchMap) {
        List<PropertyFilter> pfList = new ArrayList<PropertyFilter>();
        for (String key : searchMap.keySet()) {
            String value = searchMap.get(key);
            if (StringUtils.isEmpty(value)) {// 过滤掉空值
                continue;
            }
            pfList.add(new PropertyFilter(key, value));
        }
        return pfList;
    }

    /**
     * 获取属性名称
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * 获取比较类型
     */
    public MatchType getMatchType() {
        return matchType;
    }

    /**
     * 获取比较值的类型
     */
    public Class<?> getPropertyType() {
        return propertyType;
    }

    /**
     * 获取比较值
     */
    public Object getPropertyValue() {
        return propertyValue;
    }

    public List<PropertyFilter> getSubList() {
        return subList;
    }

    public void setSubList(List<PropertyFilter> subList) {
        this.subList = subList;
    }

}
