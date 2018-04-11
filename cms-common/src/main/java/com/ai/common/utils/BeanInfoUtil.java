package com.ai.common.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;

public class BeanInfoUtil {

    /**
     * 将制定属性进行bean赋值
     *
     * @param src
     * @param desc
     * @param include
     *            "user,name" 包含某些属性
     * @return
     */
    public static Object bean2bean(Object src, Object desc, String include) {
        try {
            BeanInfo srcBean = Introspector.getBeanInfo(src.getClass());
            BeanInfo descBean = Introspector.getBeanInfo(desc.getClass());
            PropertyDescriptor[] descPDs = descBean.getPropertyDescriptors();
            PropertyDescriptor[] srcPDs = srcBean.getPropertyDescriptors();

            // 覆盖的属性
            Set<String> includeSet = null;
            if (StringUtils.isNotEmpty(include)) {
                String[] es = StringUtils.split(include, ",");
                includeSet = Sets.newHashSet(es);
            }

            // 循环遍历属性
            for (PropertyDescriptor dp : descPDs) {
                String name = dp.getName();
                if (!name.equals("class") && (includeSet == null || includeSet.contains(name))) {

                    for (PropertyDescriptor sp : srcPDs) {

                        if (name.equals(sp.getName())) {
                            Method readMethod = sp.getReadMethod();
                            Object result = readMethod.invoke(src);

                            Method writeMethod = dp.getWriteMethod();
                            writeMethod.invoke(desc, result);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return desc;
    }

}