package com.ai.sys.tags;

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

public class DicItemNameByItemValueTag extends TagSupport {
    private static final long serialVersionUID = 1L;

    private String code = "";

    private String itemValue = "";

    private static Map<String, Dic> dicMap = new HashMap<String, Dic>();

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
                List<DicItem> dicItems = dic.getDicItemList();
                for (DicItem dicItem : dicItems) {
                    if (dicItem.getValue().equals(itemValue)) {
                        pageContext.getOut().write(dicItem.getName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.doStartTag();
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

}
