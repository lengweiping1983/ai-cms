package com.ai.epg.config.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.ai.common.enums.ValidStatusEnum;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.epg.config.entity.Entry;
import com.ai.epg.config.repository.EntryRepository;

@Controller
@RequestMapping(value = { "/config/entry" })
public class EntryController extends AbstractController {

	@Autowired
	private EntryRepository entryRepository;

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		Page<Entry> page = find(request, pageInfo, entryRepository);
		model.addAttribute("page", page);

		model.addAttribute("statusEnum", ValidStatusEnum.values());

		return "config/entry/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model) {
		model.addAttribute("statusEnum", ValidStatusEnum.values());

		return "config/entry/edit";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody Entry entry) {
		return edit(entry, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		Entry entry = entryRepository.findOne(id);
		model.addAttribute("entry", entry);

		model.addAttribute("statusEnum", ValidStatusEnum.values());
		return "config/entry/edit";
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody Entry entry, @PathVariable("id") Long id) {
		if (id == null) {
			entryRepository.save(entry);
		} else {
			Entry entryInfo = entryRepository.findOne(entry.getId());
			BeanInfoUtil.bean2bean(entry, entryInfo, "code,name,serviceUrl");
			entryRepository.save(entryInfo);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		entryRepository.delete(id);
		return new BaseResult();
	}

	@RequestMapping(value = { "check" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Object[] check(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "fieldId") String fieldId,
			@RequestParam(value = "fieldValue") String fieldValue) {
		boolean exist = checkCode(id, fieldValue);

		Object[] jsonValidateReturn = new Object[3];
		jsonValidateReturn[0] = fieldId;
		jsonValidateReturn[1] = !exist;
		if (!exist) {
			jsonValidateReturn[2] = "可以使用!";
		} else {
			jsonValidateReturn[2] = "代码" + StringUtils.trim(fieldValue)
					+ "已使用!";
		}
		return jsonValidateReturn;
	}

	private boolean checkCode(Long id, String code) {
		boolean exist = false;
		Entry entry = null;
		if (StringUtils.isNotEmpty(code)) {
			entry = entryRepository.findOneByCode(code);
		}
		if (entry != null) {
			if (id == null || id == -1 || entry.getId().longValue() != id) {
				exist = true;
			}
		}
		return exist;
	}

}
