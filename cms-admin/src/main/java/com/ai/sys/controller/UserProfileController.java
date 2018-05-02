package com.ai.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.common.bean.BaseResult;
import com.ai.common.controller.AbstractController;
import com.ai.common.exception.RestException;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;
import com.ai.sys.bean.PasswordBean;
import com.ai.sys.entity.User;
import com.ai.sys.repository.UserRepository;
import com.ai.sys.security.SecurityUtils;
import com.ai.sys.security.UserAuthorizingRealm;
import com.ai.sys.service.UserService;

@Controller
@RequestMapping(value = { "/system/user/profile" })
public class UserProfileController extends AbstractController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = { "" })
	public String profile(Model model) {
		User sessionUser = SecurityUtils.getUser();
		User user = userRepository.findOne(sessionUser.getId());
		model.addAttribute("user", user);
		return "system/user/profile/info";
	}

	@RequestMapping(value = { "edit" }, method = RequestMethod.GET)
	public String toEdit(Model model) {
		User sessionUser = SecurityUtils.getUser();
		User user = userRepository.findOne(sessionUser.getId());
		model.addAttribute("user", user);
		return "/system/user/profile/edit";
	}

	@OperationLogAnnotation(module = "系统管理", subModule = "用户管理", action = "修改", message = "修改")
	@RequestMapping(value = { "edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody User paramUser) {
		User sessionUser = SecurityUtils.getUser();
		User user = userRepository.findOne(sessionUser.getId());
		BeanInfoUtil.bean2bean(paramUser, user,
				"loginName,name,no,email,phone,mobile");

		beanValidator(user);
		userRepository.save(user);

		return new BaseResult();
	}

	@RequestMapping(value = { "editPassword" }, method = RequestMethod.GET)
	public String toEditPassword(Model model) {
		User sessionUser = SecurityUtils.getUser();
		User user = userRepository.findOne(sessionUser.getId());
		model.addAttribute("user", user);
		return "system/user/profile/editPassword";
	}

	@OperationLogAnnotation(module = "系统管理", subModule = "用户管理", action = "修改", message = "修改密码")
	@RequestMapping(value = { "editPassword" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult editPassword(@RequestBody PasswordBean bean) {
		String message = "";
		User operationObjectList = null;

		User sessionUser = SecurityUtils.getUser();
		User user = userRepository.findOne(sessionUser.getId());
		if (user != null) {
			if (!UserAuthorizingRealm.validatePassword(bean.getPlainPassword(),
					user.getPassword())) {
				throw new RestException("请输入正确的原密码!");
			}

			user.setPassword(UserAuthorizingRealm.entryptPassword(bean
					.getPassword()));

			beanValidator(user);
			userRepository.save(user);
			operationObjectList = user;
		}
		return new BaseResult().setMessage(message).addOperationObject(
				userService.transformOperationObject(operationObjectList));
	}
}