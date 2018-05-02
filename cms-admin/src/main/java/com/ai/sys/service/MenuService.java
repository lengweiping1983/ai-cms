package com.ai.sys.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.common.bean.jstree.JsTreeBean;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;
import com.ai.sys.entity.Menu;
import com.ai.sys.entity.Role;
import com.ai.sys.entity.User;
import com.ai.sys.repository.MenuRepository;
import com.ai.sys.repository.UserRepository;

@Service
@Transactional(value = "sysTransactionManager", readOnly = true)
public class MenuService extends AbstractService<Menu, Long> {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AbstractRepository<Menu, Long> getRepository() {
        return menuRepository;
    }

    /**
     * 获取用户拥有的菜单与权限
     * 
     * @return
     */
    public List<Menu> findMenuFromUser(User sessionUser) {
        List<Long> roleIdList = new ArrayList<Long>();
        User user = userRepository.findOne(sessionUser.getId());
        if (user.getRoleList() != null) {
            for (Role role : user.getRoleList()) {
                roleIdList.add(role.getId());
            }
        }
        if (roleIdList.size() > 0) {
            List<Menu> menuList = menuRepository.findByRoleIdIn(roleIdList);
            Collections.sort(menuList);
            return menuList;
        }
        return new ArrayList<Menu>();
    }

    /**
     * 获取用户拥有的菜单与权限,并获取相应的父节点
     *
     * @return
     */
    public List<Menu> findMenuTreeFromUser(User user) {
		if (user == null) {
			return new ArrayList<Menu>();
		}
        if (user.isAdmin()) {
            return menuRepository.findAllShowMenu();
        }
        List<Menu> sourceList = findMenuFromUser(user);
        return findMenuTree(sourceList);
    }

    public List<Menu> findMenuTree(List<Menu> sourceList) {
        Map<Long, Menu> menuMap = new HashMap<Long, Menu>();

        List<Menu> menuList = sourceList;

        while (true) {
            if (menuList == null || menuList.isEmpty()) {
                break;
            }
            for (Menu menu : menuList) {
                menuMap.put(menu.getId(), menu);
            }
            List<Long> idList = new ArrayList<Long>();
            for (Menu menu : menuList) {
                if (menu.getParentId() != null && !menuMap.keySet().contains(menu.getParentId()) && !idList.contains(menu.getParentId())) {
                    idList.add(menu.getParentId());
                }
            }
            if (idList.size() > 0) {
                menuList = menuRepository.findByIdIn(idList);
                continue;
            }
            break;
        }
        List<Menu> resultList = new ArrayList<Menu>(menuMap.values());
        Collections.sort(resultList);
        return resultList;
    }

    public List<Menu> findTreeMenu(User user) {
        List<Menu> allList = findMenuTreeFromUser(user);
        findChildMenu(allList);
        List<Menu> menuList = new ArrayList<Menu>();
        for (Menu menu : allList) {
            if (menu.getParent() == null) {
                menuList.add(menu);
            }

            if (StringUtils.isNotEmpty(menu.getHref())) {
                String symbol = "?";
                if (menu.getHref().contains("?")) {
                    symbol = "&";
                }
                menu.setRequestUrl(menu.getHref() + symbol + "navigation_menu_id=" + menu.getId());
            }
        }
        return menuList;
    }

    public List<Menu> findAllSortMenu() {
        List<Menu> allList = menuRepository.findAllMenu();
        List<Menu> menuList = new ArrayList<Menu>();
        for (Menu menu : allList) {
            if (menu.getParent() == null) {
                menuList.add(menu);
                sortList(menuList, allList, menu.getId());
            }
        }
        return menuList;
    }

    private void findChildMenu(List<Menu> allList) {
        for (Menu e : allList) {
            for (Menu childe : allList) {
                if (childe.getParent() != null && childe.getParent().getId() != null && childe.getParent().getId() == e.getId()
                        && Menu.TYPE_MENU == childe.getType()) {
                    e.getChildList().add(childe);
                }
            }
        }
    }

    private void sortList(List<Menu> destList, List<Menu> allList, Long parentId) {
        for (Menu e : allList) {
            if (e.getParent() != null && e.getParent().getId() != null && e.getParent().getId() == parentId) {
                destList.add(e);
                for (Menu childe : allList) {
                    if (childe.getParent() != null && childe.getParent().getId() != null && childe.getParent().getId() == e.getId()) {
                        sortList(destList, allList, e.getId());
                        break;
                    }
                }
            }
        }
    }

    /**
     * 获取Menu树
     *
     * @param showPermission
     *            是否需要显示权限节点,如 false 则不显示权限节点
     * @param filterId
     *            过滤节点Id
     * @param selectList
     *            已选节点
     * @return
     */
    public List<JsTreeBean> findJsTreeMenu(boolean showPermission, Long filterId, List<Menu> selectList) {
        List<Menu> allList = menuRepository.findAllShowMenu();
        List<JsTreeBean> beanList = findJsTreeNode(allList, JsTreeBean.ROOT_NODE_ID, showPermission, filterId, selectList);

        return beanList;
    }

    /**
     * 加载节点
     *
     * @param allList
     *            所有节点
     * @param parentId
     *            节点Id
     * @param showPermission
     *            是否需要显示权限节点,如 false 则不显示权限节点
     * @param filterId
     *            过滤节点Id
     * @param selectList
     *            已选节点
     * @return
     */
    private List<JsTreeBean> findJsTreeNode(List<Menu> allList, String parentId, boolean showPermission, Long filterId, List<Menu> selectList) {
        List<JsTreeBean> beanList = findChildJsTreeNode(allList, parentId, showPermission, filterId);
        if (beanList != null) {
            for (JsTreeBean bean : beanList) {
                bean.setChildren(findJsTreeNode(allList, bean.getId(), showPermission, filterId, selectList));
                if (selectList != null) {
                    for (Menu menu : selectList) {
                        if (bean.getId().equals(String.valueOf(menu.getId()))) {
                            bean.getState().setSelected(true);
                        }
                    }
                }
            }
        }
        return beanList;
    }

    /**
     * 根据节点Id加载子节点列表
     *
     * @param allList
     *            所有节点
     * @param parentId
     *            节点Id
     * @param showPermission
     *            是否需要显示权限节点,如 false 则不显示权限节点
     * @param filterId
     *            过滤节点Id
     * @return
     */
    private List<JsTreeBean> findChildJsTreeNode(List<Menu> allList, String parentId, boolean showPermission, Long filterId) {
        List<JsTreeBean> childJsTreeBeans = new ArrayList<JsTreeBean>();
        for (Menu e : allList) {
            if (filterId != null && filterId == e.getId()) {
                continue;
            }
            if (e.getParent() == null && JsTreeBean.ROOT_NODE_ID.equals(parentId)) {
                JsTreeBean child = new JsTreeBean();
                child.setId(String.valueOf(e.getId()));
                child.setText(e.getName());
                child.getState().setOpened(true);

                childJsTreeBeans.add(child);

            } else if (e.getParent() != null && parentId.equals("" + e.getParent().getId())) {
                if (Menu.TYPE_PERMISSION == e.getType() && !showPermission) {
                    continue;
                }

                JsTreeBean child = new JsTreeBean();
                child.setId(String.valueOf(e.getId()));
                child.setText(e.getName());

                // 如果类型是‘权限’则设置为文件图标；如果类型是‘菜单’不需要设置，默认是文件夹图标
                if (Menu.TYPE_PERMISSION == e.getType()) {
                    child.setIcon("fa fa-file icon-state-warning icon-lg");
                }

                childJsTreeBeans.add(child);
            }
        }
        return childJsTreeBeans;
    }
}