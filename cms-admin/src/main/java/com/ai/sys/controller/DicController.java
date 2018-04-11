package com.ai.sys.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.ai.common.controller.AbstractController;
import com.ai.sys.entity.Dic;
import com.ai.sys.entity.DicItem;
import com.ai.sys.repository.DicItemRepository;
import com.ai.sys.repository.DicRepository;

@Controller
@RequestMapping(value = {"/system/dic/"})
public class DicController extends AbstractController {

    @Autowired
    private DicRepository dicRepository;

    @Autowired
    private DicItemRepository dicItemRepository;

    @RequestMapping(value = {""})
    public String list(Model model, HttpServletRequest request, PageInfo pageInfo) {
        Page<Dic> page = find(request, pageInfo, dicRepository);
        model.addAttribute("page", page);
        return "system/dic/list";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String toAdd() {
        return "system/dic/edit";
    }

    @RequestMapping(value = "{id}/edit", method = RequestMethod.GET)
    public String toEdit(@PathVariable(value = "id") Long id, Model model) {
        Dic dic = dicRepository.findOne(id);
        model.addAttribute("dic", dic);
        return "system/dic/edit";
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Dic edit(@RequestBody Dic dic) {
        if (null != dic) {
            dicRepository.save(dic);
        }
        return dic;
    }

    @RequestMapping(value = "{id}/item/edit", method = RequestMethod.GET)
    public String value(@PathVariable(value = "id") Long id, Model model) {
        Dic dic = dicRepository.findOne(id);
        model.addAttribute("dic", dic);
        return "system/dic/editDicItem";
    }

    @RequestMapping(value = "{id}/item/edit", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<DicItem> value(@PathVariable(value = "id") Long id, @RequestBody DicItem dicItem) {
        beanValidator(dicItem);
        Dic dic = dicRepository.findOne(id);
        dicItem.setId(null);
        dicItem.setDicId(id);
        dic.getDicItemList().add(dicItem);
        Collections.sort(dic.getDicItemList());
        dicRepository.save(dic);
        return dic.getDicItemList();
    }

    @RequestMapping(value = "{id}/delete", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult delete(@PathVariable(value = "id") Long id) {
        Dic dic = dicRepository.findOne(id);
        dicRepository.delete(dic);
        return new BaseResult();
    }

    @RequestMapping(value = "value/{id}/delete", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseResult valueDelete(@PathVariable(value = "id") Long id) {
        dicItemRepository.delete(id);
        return new BaseResult();
    }

    @RequestMapping(value = {"check"}, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Object[] check(@RequestParam(value = "id") long id, @RequestParam(value = "fieldId") String fieldId,
            @RequestParam(value = "fieldValue") String fieldValue) {
        Dic dic = dicRepository.findOne(id);
        List<DicItem> dicItems = dic.getDicItemList();
        Object[] jsonValidateReturn = new Object[3];
        jsonValidateReturn[0] = fieldId;
        jsonValidateReturn[1] = true;
        jsonValidateReturn[2] = "可以使用!";

        if (fieldId.equalsIgnoreCase("dic_item_code")) {
            for (DicItem dicItem : dicItems) {
                if (dicItem.getCode().equals(fieldValue)) {
                    jsonValidateReturn[1] = false;
                    jsonValidateReturn[2] = "项代码已存在!";
                    break;
                }
            }
        }

        if (fieldId.equalsIgnoreCase("dic_item_name")) {
            for (DicItem dicItem : dicItems) {
                if (dicItem.getName().equals(fieldValue)) {
                    jsonValidateReturn[1] = false;
                    jsonValidateReturn[2] = "项名称已存在!";
                    break;
                }
            }
        }

        if (fieldId.equalsIgnoreCase("dic_item_value")) {
            for (DicItem dicItem : dicItems) {
                if (dicItem.getValue().equals(fieldValue)) {
                    jsonValidateReturn[1] = false;
                    jsonValidateReturn[2] = "值已存在!";
                    break;
                }
            }
        }

        return jsonValidateReturn;
    }

}
