package com.ai.epg.subscriber.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.ValidStatusEnum;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.epg.config.entity.App;
import com.ai.epg.config.repository.AppRepository;
import com.ai.epg.subscriber.entity.Subscriber;
import com.ai.epg.subscriber.entity.SubscriberGroup;
import com.ai.epg.subscriber.repository.SubscriberGroupRepository;
import com.ai.epg.subscriber.repository.SubscriberRepository;

@Controller
@RequestMapping(value = { "/subscriber/subscriber" })
public class SubscriberController extends AbstractImageController {

	@Autowired
	private SubscriberGroupRepository subscriberGroupRepository;

	@Autowired
	private SubscriberRepository subscriberRepository;

	@Autowired
	private AppRepository appRepository;

	private void setModel(Model model, HttpServletRequest request) {
		model.addAttribute("statusEnum", ValidStatusEnum.values());

		List<App> appList = appRepository.findAll();
		model.addAttribute("appList", appList);

		List<SubscriberGroup> subscriberGroupList = subscriberGroupRepository
				.findAll();
		model.addAttribute("subscriberGroupList", subscriberGroupList);
	}

	private void setContentPage(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}
		List<PropertyFilter> filters = getPropertyFilters(request);

		Specification<Subscriber> specification = SpecificationUtils
				.getSpecification(filters);
		Page<Subscriber> page = find(specification, pageInfo,
				subscriberRepository);
		model.addAttribute("page", page);
	}

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {

		setContentPage(model, request, pageInfo);

		setModel(model, request);

		return "subscriber/subscriber/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model, HttpServletRequest request) {
		Subscriber subscriber = new Subscriber();
		model.addAttribute("subscriber", subscriber);

		setModel(model, request);

		return "subscriber/subscriber/edit";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody Subscriber subscriber) {
		return edit(subscriber, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, HttpServletRequest request,
			@PathVariable("id") Long id) {
		Subscriber subscriber = subscriberRepository.findOne(id);
		model.addAttribute("subscriber", subscriber);
		setModel(model, request);

		return "subscriber/subscriber/edit";
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody Subscriber subscriber,
			@PathVariable("id") Long id) {
		if (id == null) {
		} else {
			Subscriber subscriberInfo = subscriberRepository.findOne(id);
			BeanInfoUtil.bean2bean(subscriber, subscriberInfo, "groupCode");
			subscriberRepository.save(subscriberInfo);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		Subscriber subscriber = subscriberRepository.findOne(id);
		subscriberRepository.delete(subscriber.getId());

		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, HttpServletRequest request,
			@PathVariable("id") Long id) {
		Subscriber subscriber = subscriberRepository.findOne(id);
		model.addAttribute("subscriber", subscriber);

		setModel(model, request);

		return "subscriber/subscriber/detail";
	}

}
