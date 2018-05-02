package com.ai.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.common.bean.BaseResult;
import com.ai.common.bean.OperationObject;
import com.ai.common.bean.jstree.JsTreeBean;
import com.ai.common.controller.AbstractController;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.env.handler.OperationLogAnnotation;
import com.ai.sys.entity.Menu;
import com.ai.sys.entity.Role;
import com.ai.sys.repository.MenuRepository;
import com.ai.sys.repository.RoleRepository;
import com.ai.sys.service.MenuService;

@Controller
@RequestMapping(value = { "/system/menu/" })
public class MenuController extends AbstractController {

	@Autowired
	private MenuService menuService;

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private RoleRepository roleRepository;

	@RequiresPermissions("system:menu:view")
	@RequestMapping(value = { "" })
	public String list(Model model) {
		List<Menu> menuList = menuService.findAllSortMenu();
		model.addAttribute("menuList", menuList);
		model.addAttribute("yesNoEnum", YesNoEnum.values());
		return "system/menu/list";
	}

	@RequiresPermissions("system:menu:add")
	@RequestMapping(value = { "add/{type}" }, method = RequestMethod.GET)
	public String toAdd(Model model, @PathVariable("type") Integer type) {
		Menu menu = new Menu();
		menu.setType(type);
		model.addAttribute("menu", menu);
		model.addAttribute("yesNoEnum", YesNoEnum.values());
		return "system/menu/edit";
	}

	@OperationLogAnnotation(module = "系统管理", subModule = "菜单管理", action = "增加", message = "增加菜单")
	@RequiresPermissions("system:menu:add")
	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody Menu paramMenu) {
		return edit(null, paramMenu);
	}

	@RequiresPermissions("system:menu:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		Menu menu = menuRepository.findOne(id);
		model.addAttribute("menu", menu);
		model.addAttribute("yesNoEnum", YesNoEnum.values());
		return "system/menu/edit";
	}

	@OperationLogAnnotation(module = "系统管理", subModule = "菜单管理", action = "修改", message = "修改菜单")
	@RequiresPermissions("system:menu:edit")
	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@PathVariable("id") Long id,
			@RequestBody Menu paramMenu) {
		String message = "";
		Menu operationObjectList = null;

		Menu menu = null;
		if (null == id) {
			menu = paramMenu;
		} else {
			menu = menuRepository.findOne(id);
			BeanInfoUtil.bean2bean(paramMenu, menu,
					"type,parentId,name,href,icon,sort,isShow,permission");
		}
		menuRepository.save(menu);
		operationObjectList = menu;
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@OperationLogAnnotation(module = "系统管理", subModule = "菜单管理", action = "删除", message = "删除菜单")
	@RequiresPermissions("system:menu:delete")
	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		String message = "";
		Menu operationObjectList = null;

		Menu menu = menuRepository.findOne(id);
		if (menu != null) {
			menuService.delete(menu);
			operationObjectList = menu;
		}
		return new BaseResult().setMessage(message).addOperationObject(
				transformOperationObject(operationObjectList));
	}

	@RequiresPermissions("system:menu:view")
	@RequestMapping(value = "{id}/")
	public String view(Model model, @PathVariable("id") Long id) {
		Menu menu = menuRepository.findOne(id);
		model.addAttribute("menu", menu);
		model.addAttribute("yesNoEnum", YesNoEnum.values());
		return "system/menu/view";
	}

	private List<JsTreeBean> getNodeList(boolean showPermission, Long roleId,
			Long filterMenuId) {
		List<Menu> menuList = null;
		if (roleId != null && roleId > 0) {
			Role role = roleRepository.findOne(roleId);
			if (role != null) {
				menuList = role.getMenuList();
			}
		}

		List<JsTreeBean> nodeList = menuService.findJsTreeMenu(showPermission,
				filterMenuId, menuList);
		return nodeList;
	}

	@RequestMapping(value = { "getTree" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<JsTreeBean> getTree(
			@RequestParam(value = "showPermission", required = false) boolean showPermission,
			@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "filterMenuId", required = false) Long filterMenuId) {
		return getNodeList(showPermission, roleId, filterMenuId);
	}

	@RequestMapping(value = { "getTreeWithRoot" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JsTreeBean getTreeWithRoot(
			@RequestParam(value = "showPermission", required = false) boolean showPermission,
			@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "filterMenuId", required = false) Long filterMenuId) {
		List<JsTreeBean> nodeList = getNodeList(showPermission, roleId,
				filterMenuId);

		JsTreeBean root = new JsTreeBean();
		root.setId(JsTreeBean.ROOT_NODE_ID);
		root.setText("菜单树");
		root.getState().setOpened(true);

		root.setChildren(nodeList);

		return root;
	}

	/**
	 * 校验是否可删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "{id}/validate" }, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> validate(@PathVariable("id") Long id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("result", "true");
		Menu menu = menuRepository.findOne(id);
		// 是否拥有子菜单, 如果有则不能删除
		List<Menu> menus = menuRepository.findByParentId(id);
		if (null != menus && !menus.isEmpty()) {
			map.put("result", "false");
			map.put("description", "[" + menu.getName() + "]菜单拥有子节点,无法删除!");
		}
		return map;
	}

	@RequestMapping(value = { "{id}/findChild" }, method = RequestMethod.GET)
	@ResponseBody
	public List<Menu> findChild(@PathVariable("id") Long id) {
		List<Menu> menuList = menuRepository.findByParentId(id);
		return menuList;
	}

	public OperationObject transformOperationObject(Menu menu) {
		if (menu == null) {
			return null;
		}
		OperationObject operationObject = new OperationObject();
		operationObject.setId(menu.getId());
		operationObject.setName(menu.getName());
		return operationObject;
	}

}
