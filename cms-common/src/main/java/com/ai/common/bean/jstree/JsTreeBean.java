package com.ai.common.bean.jstree;

import java.util.ArrayList;
import java.util.List;

public class JsTreeBean {
    public static final String ROOT_NODE_ID = "-1";

    private String id;
    private String text;
    private String icon;
    private JsTreeState state = new JsTreeState();
    private JsTreeAAttr a_attr = new JsTreeAAttr();
    private List<JsTreeBean> children = new ArrayList<JsTreeBean>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public JsTreeState getState() {
        return state;
    }

    public void setState(JsTreeState state) {
        this.state = state;
    }

    public JsTreeAAttr getA_attr() {
        return a_attr;
    }

    public void setA_attr(JsTreeAAttr a_attr) {
        this.a_attr = a_attr;
    }

    public List<JsTreeBean> getChildren() {
        return children;
    }

    public void setChildren(List<JsTreeBean> children) {
        this.children = children;
    }

}
