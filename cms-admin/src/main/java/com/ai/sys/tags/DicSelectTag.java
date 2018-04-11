package com.ai.sys.tags;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ai.sys.entity.Dic;
import com.ai.sys.entity.DicItem;
import com.ai.sys.repository.DicRepository;

public class DicSelectTag extends TagSupport {
    private static final long serialVersionUID = 1L;

    private static Map<String, Dic> dicMap = new HashMap<String, Dic>();

    private String id = "";
    private String code = "";
    private String name = "";
    private String value = "";
    private String onChange = "";
    private String css = "";
    private String style = "";

    @Autowired
    private DicRepository dicRepository;

    public int doStartTag() throws JspException {
        try {
            code = code.trim();
            Dic dic = dicMap.get(code);
            if (null == dic || dic.getDicItemList().isEmpty()) {
                if (null == dicRepository) {
                    dicRepository = (DicRepository) WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext()).getBean(
                            "dicRepository");
                }
                dic = dicRepository.findByCode(code);
                dicMap.put(code, dic);
            }

            if (null != dic) {
                StringBuffer sb = new StringBuffer();

                List<DicItem> dicItems = dic.getDicItemList();
                if (null == dicItems || dicItems.isEmpty()) {
                    sb.append("没有:").append(code).append(" 对应的数据!");
                    pageContext.getOut().write(sb.toString());
                } else {
                    sb.append("<select name='").append(name).append("'");
                    sb.append(" id='").append(id).append("'");
                    sb.append(" onchange='").append(onChange).append("'");
                    sb.append(" class='form-control ").append(css).append("'");
                    sb.append(" style='").append(style).append("'>");
                    for (DicItem dicItem : dicItems) {
                        sb.append("<option").append(" value='").append(dicItem.getValue()).append("'");
                        if (value.equals(dicItem.getValue())) {
                            sb.append(" selected='selected'");
                        }
                        sb.append(">");
                        sb.append(dicItem.getName());
                        sb.append("</option>");
                    }
                    sb.append("</select>");
                    pageContext.getOut().write(sb.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.doStartTag();
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOnChange() {
        return onChange;
    }

    public void setOnChange(String onChange) {
        this.onChange = onChange;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
