package com.ai.epg.category.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.common.bean.jstree.JsTreeBean;
import com.ai.common.controller.AbstractController;
import com.ai.epg.category.service.CategoryService;

@Controller
@RequestMapping(value = {"/category/"})
public class CategoryIndexController extends AbstractController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = {""})
    public String index(HttpServletRequest request) {
        String currentAppCode = request.getParameter("appCode");
        if (StringUtils.isNotEmpty(currentAppCode)) {
            request.getSession().setAttribute("currentAppCode", currentAppCode);
        }
        return "category/category/index";
    }

    @RequestMapping(value = {"left"})
    public String left() {
        return "category/category/left";
    }

    private List<JsTreeBean> getNodeList(HttpServletRequest request, Long filterCategoryId, String appCode) {
        String currentAppCode = appCode;
        if (StringUtils.isEmpty(currentAppCode)) {
            currentAppCode = (String) request.getSession().getAttribute("currentAppCode");
        }
        List<JsTreeBean> nodeList = categoryService.findJsTreeCategory(currentAppCode, filterCategoryId, null);
        return nodeList;
    }

    @RequestMapping(value = {"getTree"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<JsTreeBean> getTree(HttpServletRequest request, @RequestParam(value = "filterCategoryId", required = false) Long filterCategoryId,
            @RequestParam(value = "appCode", required = false) String appCode) {
        return getNodeList(request, filterCategoryId, appCode);
    }

    @RequestMapping(value = {"getTreeWithRoot"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JsTreeBean getTreeWithRoot(HttpServletRequest request, @RequestParam(value = "filterCategoryId", required = false) Long filterCategoryId,
            @RequestParam(value = "appCode", required = false) String appCode) {
        List<JsTreeBean> nodeList = getNodeList(request, filterCategoryId, appCode);

        JsTreeBean root = new JsTreeBean();
        root.setId(JsTreeBean.ROOT_NODE_ID);
        root.setText("栏目树");
        root.getA_attr().setHref("/category/category/");
        root.getState().setOpened(true);

        root.setChildren(nodeList);

        return root;
    }

}
