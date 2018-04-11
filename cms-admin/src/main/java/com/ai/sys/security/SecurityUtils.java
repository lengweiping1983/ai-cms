package com.ai.sys.security;

import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.subject.Subject;

import com.ai.sys.entity.User;

public class SecurityUtils {
	/**
	 * 获取当前用户
	 *
	 * @return
	 */
	public static User getUser() {
		try {
			Subject subject = org.apache.shiro.SecurityUtils.getSubject();
			User user = (User) subject.getPrincipal();
			return user;
		} catch (UnavailableSecurityManagerException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static Long getUserId() {
		User user = SecurityUtils.getUser();
		if (user != null) {
			return user.getId();
		}
		return null;
	}

	public static String getUserName() {
		User user = SecurityUtils.getUser();
		if (user != null) {
			return user.getName();
		}
		return null;
	}
}
