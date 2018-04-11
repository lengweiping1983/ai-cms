package com.ai.sys.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ai.common.controller.AbstractController;
import com.ai.env.handler.OperationLogAnnotation;
import com.ai.sys.service.UserService;

@Controller
public class LoginController extends AbstractController {

	@Autowired
	private UserService userService;

	@Value("${login.verificationFlag:true}")
	private boolean verificationFlag;

	@OperationLogAnnotation(module = "系统管理", subModule = "系统管理", action = "注销", message = "退出系统")
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/logout")
	public String logout() {
		SecurityUtils.getSubject().logout();
		return "index/login";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/login")
	public String login() {
		SecurityUtils.getSubject().logout();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		request.setAttribute("verificationFlag", verificationFlag);
		return "index/login";
	}

	@OperationLogAnnotation(module = "系统管理", subModule = "系统管理", action = "登录", message = "登录系统")
	@RequestMapping(method = RequestMethod.POST, value = "/login")
	public String login(
			HttpServletRequest httpServletRequest,
			Model model,
			@RequestParam(required = true, value = "loginName") String loginName,
			@RequestParam(required = true, value = "password") String password,
			@RequestParam(value = "rememberMe", defaultValue = "false") boolean rememberMe,
			final RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpServletResponse response) {

		// 验证码
		request.setAttribute("verificationFlag", verificationFlag);
		if (verificationFlag) {
			String captchaId = (String) httpServletRequest.getSession()
					.getAttribute("vrifyCode");
			String parameter = httpServletRequest.getParameter("vrifyCode");
			if (StringUtils.isEmpty(parameter)) {
				model.addAttribute("message", "请输入验证码！");
				return "index/login";
			} else {
				if (!StringUtils.trimToEmpty(captchaId).equalsIgnoreCase(
						parameter)) {
					model.addAttribute("message", "验证码错误！");
					return "index/login";
				}
			}
		}

		model.addAttribute("loginName", loginName);
		Subject currentUser = SecurityUtils.getSubject();

		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
				loginName, password);
		usernamePasswordToken.setRememberMe(rememberMe);
		try {
			currentUser.login(usernamePasswordToken);
		} catch (DisabledAccountException ex) {
			model.addAttribute("message", "您的帐号已停用！");
			return "index/login";
		} catch (AuthenticationException ex) {
			model.addAttribute("message", "帐号或密码错误！");
			return "index/login";
		}

		String ip = getIpAddr(request);
		// 更新登录IP和时间
		userService.updateUserLoginInfo(com.ai.sys.security.SecurityUtils
				.getUser().getId(), ip);

		String successUrl = null;
		SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
		if (savedRequest != null
				&& savedRequest.getMethod().equalsIgnoreCase(
						AccessControlFilter.GET_METHOD)) {
			successUrl = savedRequest.getRequestUrl();
		}

		if (successUrl == null) {
			successUrl = "/";
		}
		response.addHeader("location", request.getContextPath() + successUrl);
		response.setStatus(302);
		return null;
		// try {
		// WebUtils.redirectToSavedRequest(request, response, "/");
		// return null;
		// } catch (IOException ex) {
		// throw Exceptions.unchecked(ex);
		// }
	}
}
