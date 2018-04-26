package com.ai.sys.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.bean.jstree.JsTreeBean;
import com.ai.common.controller.AbstractController;
import com.ai.common.exception.RestException;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;
import com.ai.sys.bean.IdsBean;
import com.ai.sys.bean.PasswordBean;
import com.ai.sys.entity.Role;
import com.ai.sys.entity.User;
import com.ai.sys.repository.RoleRepository;
import com.ai.sys.repository.UserRepository;
import com.ai.sys.security.UserAuthorizingRealm;
import com.ai.sys.service.UserService;

@Controller
@RequestMapping(value = { "/system/user" })
public class UserController extends AbstractController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@RequiresPermissions("system:user:view")
	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		Page<User> page = find(request, pageInfo, userService);
		model.addAttribute("page", page);
		return "/system/user/list";
	}

	@RequiresPermissions("system:user:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd() {
		return "/system/user/edit";
	}

	@OperationLogAnnotation(module = "系统管理", subModule = "用户管理", action = "增加", message = "增加用户")
	@RequiresPermissions("system:user:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody IdsBean<User> bean) {
		return edit(null, bean);
	}

	@RequiresPermissions("system:user:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(@PathVariable("id") Long id, Model model) {
		User user = userService.findById(id);
		model.addAttribute("user", user);
		return "/system/user/edit";
	}

	@OperationLogAnnotation(module = "系统管理", subModule = "用户管理", action = "修改", message = "修改用户")
	@RequiresPermissions("system:user:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@PathVariable("id") Long id,
			@RequestBody IdsBean<User> bean) {
		User paramUser = bean.getData();
		Long[] roleIds = bean.getIds();
		boolean exist = checkLoginName(id, paramUser.getLoginName());
		if (exist) {
			throw new RestException("帐号"
					+ StringUtils.trim(paramUser.getLoginName()) + "已使用!");
		}
		User user = null;
		if (id == null) {
			user = paramUser;
			user.setPassword(UserAuthorizingRealm.entryptPassword(paramUser
					.getPassword()));
		} else {
			user = userService.findById(id);
			BeanInfoUtil.bean2bean(paramUser, user,
					"loginName,name,no,email,phone,mobile,status");
		}

		List<Role> roleList = new ArrayList<Role>();
		if (null != roleIds) {
			for (Long roleId : roleIds) {
				if (null != roleId && roleId > 0) {
					Role role = new Role();
					role.setId(roleId);
					roleList.add(role);
				}
			}
		}
		user.setRoleList(roleList);

		beanValidator(user);
		userService.save(user);

		return new BaseResult();
	}

	@OperationLogAnnotation(module = "系统管理", subModule = "用户管理", action = "删除", message = "删除用户")
	@RequiresPermissions("system:user:delete")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		userService.deleteById(id);
		return new BaseResult();
	}

	@RequiresPermissions("system:user:editPassword")
	@RequestMapping(value = { "{id}/editPassword" }, method = RequestMethod.GET)
	public String toEditPassword(@PathVariable("id") Long id, Model model) {
		User user = userRepository.findOne(id);
		model.addAttribute("user", user);
		return "system/user/editPassword";
	}

	@OperationLogAnnotation(module = "系统管理", subModule = "用户管理", action = "修改", message = "修改密码")
	@RequiresPermissions("system:user:editPassword")
	@RequestMapping(value = { "{id}/editPassword" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult editPassword(@PathVariable("id") Long id,
			@RequestBody PasswordBean bean) {
		User user = userRepository.findOne(id);
		user.setPassword(UserAuthorizingRealm.entryptPassword(bean
				.getPassword()));

		beanValidator(user);
		userRepository.save(user);
		return new BaseResult();
	}

	private boolean checkLoginName(Long id, String loginName) {
		boolean exist = false;
		User user = null;
		if (StringUtils.isNotEmpty(loginName)) {
			user = userRepository.findOneByLoginName(loginName);
		}
		if (user != null) {
			if (id == null || id == -1 || user.getId().longValue() != id) {
				exist = true;
			}
		}
		return exist;
	}

	@RequestMapping(value = { "check" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Object[] check(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "fieldId") String fieldId,
			@RequestParam(value = "fieldValue") String fieldValue) {
		boolean exist = checkLoginName(id, fieldValue);

		Object[] jsonValidateReturn = new Object[3];
		jsonValidateReturn[0] = fieldId;
		jsonValidateReturn[1] = !exist;
		if (!exist) {
			jsonValidateReturn[2] = "可以使用!";
		} else {
			jsonValidateReturn[2] = "帐号" + StringUtils.trim(fieldValue)
					+ "已使用!";
		}
		return jsonValidateReturn;
	}

	@RequestMapping(value = { "/roles" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public JsTreeBean findAllRoles(
			@RequestParam(value = "userId", required = false) Long userId) {
		List<Role> roles = new ArrayList<Role>();
		if (userId != null && userId > 0) {
			User user = userRepository.findOne(userId);
			roles = user.getRoleList();
		}

		List<JsTreeBean> jsTreeBeans = new ArrayList<JsTreeBean>();
		Iterable<Role> iterable = roleRepository.findAll();
		Iterator<Role> iterator = iterable.iterator();
		while (iterator.hasNext()) {
			Role role = iterator.next();
			JsTreeBean jsTreeBean = new JsTreeBean();
			jsTreeBean.setId("" + role.getId());
			jsTreeBean.setText(role.getName());
			if (roles.contains(role)) {
				jsTreeBean.getState().setSelected(true);
			}
			jsTreeBeans.add(jsTreeBean);
		}
		JsTreeBean jsTreeBean = new JsTreeBean();
		jsTreeBean.setId(JsTreeBean.ROOT_NODE_ID);
		jsTreeBean.setText("角色列表");

		jsTreeBean.getState().setOpened(true);
		jsTreeBean.setChildren(jsTreeBeans);
		return jsTreeBean;
	}

}
