package com.ai.cms.config.controller;

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

import com.ai.cms.config.entity.Cp;
import com.ai.cms.config.repository.CpRepository;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractController;
import com.ai.common.enums.CpTypeEnum;
import com.ai.common.enums.ValidStatusEnum;
import com.ai.common.utils.BeanInfoUtil;

@Controller
@RequestMapping(value = { "/config/cp" })
public class CpController extends AbstractController {

	@Autowired
	private CpRepository cpRepository;

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		Page<Cp> page = find(request, pageInfo, cpRepository);
		model.addAttribute("page", page);

		model.addAttribute("typeEnum", CpTypeEnum.values());
		model.addAttribute("statusEnum", ValidStatusEnum.values());

		return "config/cp/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model) {
		Cp cp = new Cp();
		model.addAttribute("cp", cp);
		
		model.addAttribute("typeEnum", CpTypeEnum.values());
		model.addAttribute("statusEnum", ValidStatusEnum.values());

		return "config/cp/edit";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody Cp cp) {
		return edit(cp, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		Cp cp = cpRepository.findOne(id);
		model.addAttribute("cp", cp);

		model.addAttribute("typeEnum", CpTypeEnum.values());
		model.addAttribute("statusEnum", ValidStatusEnum.values());
		return "config/cp/edit";
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody Cp cp, @PathVariable("id") Long id) {
		if (id == null) {
			cpRepository.save(cp);
		} else {
			Cp cpInfo = cpRepository.findOne(cp.getId());
			BeanInfoUtil.bean2bean(cp, cpInfo,
					"type,code,name,shortName,status,description");
			cpRepository.save(cpInfo);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		cpRepository.delete(id);
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
		Cp cp = null;
		if (StringUtils.isNotEmpty(code)) {
			cp = cpRepository.findOneByCode(code);
		}
		if (cp != null) {
			if (id == null || id == -1 || cp.getId().longValue() != id) {
				exist = true;
			}
		}
		return exist;
	}

}
