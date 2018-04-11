package com.ai.sys.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.sys.entity.Menu;
import com.ai.sys.entity.User;
import com.ai.sys.security.SecurityUtils;
import com.ai.sys.service.MenuService;

@Controller
@RequestMapping(value = {""})
public class IndexController {

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = {"/index", ""})
    public String index(Model model) {
        return "index/index";
    }

    @RequestMapping(value = {"/header"})
    public String header(Model model) {
        User user = SecurityUtils.getUser();
        model.addAttribute("user", user);

        return "index/header";
    }

    @RequestMapping(value = {"/leftMenu"})
    public String leftMenu(Model model) {
        User user = SecurityUtils.getUser();
        List<Menu> menuList = menuService.findTreeMenu(user);
        model.addAttribute("menuList", menuList);

        return "index/leftMenu";
    }

    private List<Menu> findMenuIncludeParentById(Long id) {
        List<Menu> menuList = new ArrayList<Menu>();
        if (id != null && id >= 0) {
            Menu menu = menuService.findById(id);
            do {
                menuList.add(0, menu);
                menu = menu.getParent();
            } while (menu != null);
        }
        return menuList;
    }

    @RequestMapping(value = {"/navigation/{id}"})
    public String navigation(Model model, @PathVariable("id") Long id) {
        List<Menu> menuList = findMenuIncludeParentById(id);
        model.addAttribute("menuList", menuList);

        return "index/navigation";
    }

    @RequestMapping(value = {"/footer"})
    public String footer(Model model) {
        return "index/footer";
    }

}
