package com.ai.common.cache;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.data.domain.Pageable;

public class CacheKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(Object target, Method method, Object... params) {
		StringBuilder sb = new StringBuilder();
		sb.append(target.getClass().getName() + "$$$");
		sb.append(method.getName() + "$$$");
		for (Object obj : params) {
			if (obj == null) {
				continue;
			}
			if (obj instanceof Pageable) {
				Pageable pageable = (Pageable) obj;
				sb.append(pageable.getPageNumber() + "$$$");
				sb.append(pageable.getPageSize() + "$$$");
				if (pageable.getSort() != null) {
					sb.append(pageable.getSort().toString() + "$$$");
				}
			} else {
				sb.append(obj.toString() + "$$$");
			}
		}
		return sb.toString();
	}
}
