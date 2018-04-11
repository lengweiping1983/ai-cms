package com.ai.epg.subscriber.controller;

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
import com.ai.common.enums.SubscriberGroupTypeEnum;
import com.ai.common.enums.ValidStatusEnum;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.epg.subscriber.entity.SubscriberGroup;
import com.ai.epg.subscriber.repository.SubscriberGroupRepository;

@Controller
@RequestMapping(value = { "/subscriber/subscriberGroup" })
public class SubscriberGroupController extends AbstractController {

	@Autowired
	private SubscriberGroupRepository subscriberGroupRepository;
	
	private void setModel(Model model, HttpServletRequest request) {
		model.addAttribute("statusEnum", ValidStatusEnum.values());
		model.addAttribute("typeEnum", SubscriberGroupTypeEnum.values());
	}

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		Page<SubscriberGroup> page = find(request, pageInfo,
				subscriberGroupRepository);
		model.addAttribute("page", page);

		setModel(model, request);

		return "subscriber/subscriberGroup/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model, HttpServletRequest request) {
		setModel(model, request);

		return "subscriber/subscriberGroup/edit";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody SubscriberGroup subscriberGroup) {
		return edit(subscriberGroup, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, HttpServletRequest request, @PathVariable("id") Long id) {
		SubscriberGroup subscriberGroup = subscriberGroupRepository.findOne(id);
		model.addAttribute("subscriberGroup", subscriberGroup);

		setModel(model, request);
		return "subscriber/subscriberGroup/edit";
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody SubscriberGroup subscriberGroup,
			@PathVariable("id") Long id) {
		if (id == null) {
			subscriberGroupRepository.save(subscriberGroup);
		} else {
			SubscriberGroup subscriberGroupInfo = subscriberGroupRepository
					.findOne(subscriberGroup.getId());
			BeanInfoUtil.bean2bean(subscriberGroup, subscriberGroupInfo,
					"groupType,name,code,status,description");
			subscriberGroupRepository.save(subscriberGroupInfo);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		subscriberGroupRepository.delete(id);
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
		SubscriberGroup subscriberGroup = null;
		if (StringUtils.isNotEmpty(code)) {
			subscriberGroup = subscriberGroupRepository.findOneByCode(code);
		}
		if (subscriberGroup != null) {
			if (id == null || id == -1
					|| subscriberGroup.getId().longValue() != id) {
				exist = true;
			}
		}
		return exist;
	}

}
