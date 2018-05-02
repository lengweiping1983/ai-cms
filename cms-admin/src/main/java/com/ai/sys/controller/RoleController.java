package com.ai.sys.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.common.bean.BaseResult;
import com.ai.common.bean.OperationObject;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractController;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;
import com.ai.sys.bean.IdsBean;
import com.ai.sys.entity.Menu;
import com.ai.sys.entity.Role;
import com.ai.sys.repository.MenuRepository;
import com.ai.sys.repository.RoleRepository;

@Controller
@RequestMapping(value = { "/system/role/" })
public class RoleController extends AbstractController {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private MenuRepository menuRepository;

	@RequiresPermissions("system:role:view")
	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		Page<Role> page = find(request, pageInfo, roleRepository);
		model.addAttribute("page", page);
		return "system/role/list";
	}

	@RequiresPermissions("system:role:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd() {
		return "system/role/edit";
	}

	@OperationLogAnnotation(module = "系统管理", subModule = "角色管理", action = "增加", message = "增加角色")
	@RequiresPermissions("system:role:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody IdsBean<Role> bean) {
		return edit(null, bean);
	}

	@RequiresPermissions("system:role:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		Role role = roleRepository.findOne(id);
		model.addAttribute("role", role);
		return "system/role/edit";
	}

	@OperationLogAnnotation(module = "系统管理", subModule = "角色管理", action = "修改", message = "修改角色")
	@RequiresPermissions("system:role:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@PathVariable("id") Long id,
			@RequestBody IdsBean<Role> bean) {
		String message = "";
		Role operationObjectList = null;

		Role paramRole = bean.getData();
		Long[] menuIds = bean.getIds();

		Role role = null;
		if (null == id) {
			role = paramRole;
		} else {
			role = roleRepository.findOne(id);
			BeanInfoUtil.bean2bean(paramRole, role, "name");
		}

		List<Menu> menuList = new ArrayList<Menu>();
		if (null != menuIds) {
			for (Long menuId : menuIds) {
				if (menuId != null && menuId > 0) {
					Menu menu = new Menu();
					menu.setId(menuId);
					menuList.add(menu);
				}
			}
		}
		role.setMenuList(menuList);

		roleRepository.save(role);
		operationObjectList = role;
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "系统管理", subModule = "角色管理", action = "删除", message = "删除角色")
	@RequiresPermissions("system:role:delete")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		String message = "";
		Role operationObjectList = null;

		Role role = roleRepository.findOne(id);
		if (role != null) {
			roleRepository.delete(role);
			operationObjectList = role;
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@RequestMapping(value = { "{roleId}/menu/{menuId}" }, method = { RequestMethod.GET })
	@ResponseBody
	public List<Menu> findRolePermissionMenu(
			@PathVariable("roleId") Long roleId,
			@PathVariable("menuId") Long menuId) {
		List<Menu> menuList = menuRepository.findMenusByRoleId(roleId, menuId);

		return menuList;
	}

	public OperationObject transformOperationObject(Role role) {
		if (role == null) {
			return null;
		}
		OperationObject operationObject = new OperationObject();
		operationObject.setId(role.getId());
		operationObject.setName(role.getName());
		return operationObject;
	}

}
