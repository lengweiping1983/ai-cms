package com.ai.epg.product.controller;

import java.util.Date;
import java.util.List;

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

import com.ai.AdminGlobal;
import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.cms.injection.enums.InjectionStatusEnum;
import com.ai.cms.injection.repository.InjectionPlatformRepository;
import com.ai.cms.injection.service.InjectionService;
import com.ai.cms.media.bean.BatchBean;
import com.ai.cms.media.bean.BatchInjectionBean;
import com.ai.cms.media.bean.BatchStatusBean;
import com.ai.cms.media.bean.ImageBean;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.bean.ResultCode;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.ServiceItemTypeEnum;
import com.ai.common.exception.ServiceException;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.epg.product.entity.Service;
import com.ai.epg.product.entity.ServiceItem;
import com.ai.epg.product.entity.ServiceItemView;
import com.ai.epg.product.repository.ServiceItemRepository;
import com.ai.epg.product.repository.ServiceItemViewRepository;
import com.ai.epg.product.repository.ServiceRepository;
import com.ai.epg.product.service.ProductService;
import com.ai.epg.subscriber.entity.SubscriberGroup;
import com.ai.epg.subscriber.repository.SubscriberGroupRepository;

@Controller
@RequestMapping(value = { "/service/{serviceId}/serviceItem" })
public class ServiceItemController extends AbstractImageController {

	@Autowired
	private ServiceRepository serviceRepository;

	@Autowired
	private ServiceItemRepository serviceItemRepository;

	@Autowired
	private ServiceItemViewRepository serviceItemViewRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private InjectionPlatformRepository injectionPlatformRepository;
	@Autowired
	private InjectionService injectionService;

	@Autowired
	private SubscriberGroupRepository subscriberGroupRepository;

	private void setModel(Model model) {
		model.addAttribute("typeEnum", ServiceItemTypeEnum.values());
		model.addAttribute("statusEnum", OnlineStatusEnum.values());
		model.addAttribute("injectionStatusEnum", InjectionStatusEnum.values());

		List<SubscriberGroup> subscriberGroupList = subscriberGroupRepository
				.findAll();
		model.addAttribute("subscriberGroupList", subscriberGroupList);
	}

	@RequestMapping(value = { "" })
	public String list(@PathVariable("serviceId") Long serviceId, Model model,
			HttpServletRequest request, PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("sortIndex");
		}

		Service service = serviceRepository.findOne(serviceId);
		model.addAttribute("service", service);

		Page<ServiceItemView> page = find(request, pageInfo,
				serviceItemViewRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "service/serviceItem/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model, @PathVariable("serviceId") Long serviceId) {
		ServiceItemView serviceItemView = new ServiceItemView();
		model.addAttribute("serviceItem", serviceItemView);

		Service service = serviceRepository.findOne(serviceId);
		model.addAttribute("service", service);

		setModel(model);

		return "service/serviceItem/edit";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@PathVariable("serviceId") Long serviceId,
			@RequestBody ImageBean<ServiceItem> imageBean) {
		return edit(serviceId, imageBean, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model,
			@PathVariable("serviceId") Long serviceId,
			@PathVariable("id") Long id) {
		ServiceItemView serviceItemView = serviceItemViewRepository.findOne(id);
		model.addAttribute("serviceItem", serviceItemView);

		Service service = serviceRepository.findOne(serviceId);
		model.addAttribute("service", service);

		setModel(model);

		return "service/serviceItem/edit";
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@PathVariable("serviceId") Long serviceId,
			@RequestBody ImageBean<ServiceItem> imageBean,
			@PathVariable("id") Long id) {
		ServiceItem serviceItem = imageBean.getData();

		ServiceItem serviceItemInfo = null;
		if (id == null) {
			serviceItemInfo = serviceItem;
			serviceItemInfo.setSiteCode(AdminGlobal.getSiteCode());

			ServiceItem existServiceItem = serviceItemRepository
					.findOneByServiceIdAndItemTypeAndItemId(serviceId,
							serviceItem.getItemType(), serviceItem.getItemId());
			if (existServiceItem != null) {
				throw new ServiceException("该元素已存在！");
			}
		} else {
			serviceItemInfo = serviceItemRepository.findOne(id);
			BeanInfoUtil.bean2bean(serviceItem, serviceItemInfo,
					"sortIndex,status,itemStatus,validTime,expiredTime");
		}

		productService.saveServiceItem(serviceItemInfo);

		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("serviceId") Long serviceId,
			@PathVariable("id") Long id) {
		ServiceItem serviceItemInfo = serviceItemRepository.findOne(id);
		if (serviceItemInfo != null) {
//			if (serviceItemInfo != null) {
//				Date currentTime = new Date();
//				injectionService.outInjection(serviceItemInfo,
//						serviceItemInfo.getPlatformId(), 1, currentTime,
//						currentTime);
//			}
			serviceItemRepository.delete(serviceItemInfo);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/status" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult status(@PathVariable("id") Long id) {
		ServiceItem serviceItemInfo = serviceItemRepository.findOne(id);
		if (serviceItemInfo.getStatus() == OnlineStatusEnum.ONLINE.getKey()) {
			serviceItemInfo.setStatus(OnlineStatusEnum.OFFLINE.getKey());
		} else {
			serviceItemInfo.setStatus(OnlineStatusEnum.ONLINE.getKey());
		}
		serviceItemRepository.save(serviceItemInfo);
		return new BaseResult();
	}

	@RequestMapping(value = { "batchChangeStatus" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchChangeStatus(@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String[] itemIdArr = itemIds.split(",");
		if (null != itemIdArr) {
			for (String itemIdStr : itemIdArr) {
				Long itemId = Long.valueOf(itemIdStr);
				ServiceItem serviceItemInfo = serviceItemRepository
						.findOne(itemId);
				serviceItemInfo.setStatus(batchBean.getStatus());
				serviceItemRepository.save(serviceItemInfo);
			}
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "batchDelete" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchDelete(@RequestBody BatchBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		Date currentTime = new Date();
		String[] itemIdArr = itemIds.split(",");
		if (null != itemIdArr) {
			for (String itemIdStr : itemIdArr) {
				Long itemId = Long.valueOf(itemIdStr);
				ServiceItem serviceItemInfo = serviceItemRepository
						.findOne(itemId);
				if (serviceItemInfo != null
						&& serviceItemInfo.getStatus() == OnlineStatusEnum.OFFLINE
								.getKey()) {

//					injectionService.outInjection(serviceItemInfo,
//							serviceItemInfo.getPlatformId(), 1, currentTime,
//							currentTime);

					serviceItemRepository.delete(serviceItemInfo);
				}
			}
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "batchInjection" }, method = RequestMethod.GET)
	public String tobatchInjection(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		List<InjectionPlatform> injectionPlatformList = injectionPlatformRepository
				.findAll();
		model.addAttribute("injectionPlatformList", injectionPlatformList);

		return "service/serviceItem/batchInjection";
	}

	@RequestMapping(value = { "batchInjection" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchInjection(@RequestBody BatchInjectionBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		Date currentTime = new Date();
		String[] itemIdArr = itemIds.split(",");
		if (null != itemIdArr) {
			for (String itemIdStr : itemIdArr) {
				Long itemId = Long.valueOf(itemIdStr);
				ServiceItem serviceItemInfo = serviceItemRepository
						.findOne(itemId);
//				if (serviceItemInfo != null) {
//					injectionService.inInjection(serviceItemInfo,
//							batchBean.getPlatformId(), batchBean.getPriority(),
//							currentTime, currentTime);
//				}
			}
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "batchOutInjection" }, method = RequestMethod.GET)
	public String batchOutInjection(Model model,
			@RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds) {

		model.addAttribute("itemType", itemType);
		model.addAttribute("itemIds", itemIds);

		return "service/serviceItem/batchOutInjection";
	}

	@RequestMapping(value = { "batchOutInjection" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult batchOutInjection(
			@RequestBody BatchInjectionBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		Date currentTime = new Date();
		String[] itemIdArr = itemIds.split(",");
		if (null != itemIdArr) {
			for (String itemIdStr : itemIdArr) {
				Long itemId = Long.valueOf(itemIdStr);
				ServiceItem serviceItemInfo = serviceItemRepository
						.findOne(itemId);
//				if (serviceItemInfo != null) {
//					injectionService.outInjection(serviceItemInfo,
//							serviceItemInfo.getPlatformId(),
//							batchBean.getPriority(), currentTime, currentTime);
//				}
			}
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "resetInjectionStatus" }, method = { RequestMethod.POST }, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult resetInjectionStatus(
			@RequestBody BatchStatusBean batchBean) {
		Integer itemType = batchBean.getItemType();
		String itemIds = batchBean.getItemIds();
		if (itemType == null || StringUtils.isEmpty(itemIds)) {
			return new BaseResult(ResultCode.ILLEGAL_ARGUMENT.value());
		}
		String[] itemIdArr = itemIds.split(",");
		if (null != itemIdArr) {
			for (String itemIdStr : itemIdArr) {
				Long itemId = Long.valueOf(itemIdStr);
				ServiceItem serviceItem = serviceItemRepository.findOne(itemId);
				if (serviceItem != null) {
					serviceItem.setInjectionStatus(InjectionStatusEnum.DEFAULT
							.getKey());
					serviceItemRepository.save(serviceItem);
				}
			}
		}
		return new BaseResult();
	}
	
}
