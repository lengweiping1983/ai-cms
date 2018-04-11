package com.ai.epg.subscriber.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.ItemTypeEnum;
import com.ai.common.enums.OrderRecordTypeEnum;
import com.ai.common.enums.PaymentStatusEnum;
import com.ai.common.enums.ValidStatusEnum;
import com.ai.common.excel.ExportExcel;
import com.ai.common.exception.ServiceException;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.common.utils.DateUtils;
import com.ai.epg.config.entity.App;
import com.ai.epg.config.repository.AppRepository;
import com.ai.epg.product.entity.Charge;
import com.ai.epg.product.repository.ChargeRepository;
import com.ai.epg.subscriber.entity.OrderRecord;
import com.ai.epg.subscriber.entity.OrderRecordExportLog;
import com.ai.epg.subscriber.repository.OrderRecordRepository;
import com.ai.epg.subscriber.repository.SubscriberGroupRepository;
import com.ai.epg.subscriber.repository.SubscriberRepository;

@Controller
@RequestMapping(value = { "/orderRecord/orderRecord" })
public class OrderRecordController extends AbstractImageController {

	@Autowired
	private OrderRecordRepository orderRecordRepository;

	@Autowired
	private AppRepository appRepository;

	@Autowired
	private ChargeRepository chargeRepository;

	@Autowired
	private SubscriberGroupRepository subscriberGroupRepository;
	
	@Autowired
	private SubscriberRepository subscriberRepository;
	
	private void setModel(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		model.addAttribute("statusEnum", ValidStatusEnum.values());
		model.addAttribute("typeEnum", OrderRecordTypeEnum.values());
		model.addAttribute("itemTypeEnum", ItemTypeEnum.values());
		
		List<App> appList = appRepository.findAll();
		model.addAttribute("appList", appList);

		List<Charge> chargeList = chargeRepository.findAll();
		model.addAttribute("chargeList", chargeList);

	}

	private void setContentPage(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}
		List<PropertyFilter> filters = getPropertyFilters(request);

		Specification<OrderRecord> specification = SpecificationUtils
				.getSpecification(filters);
		Page<OrderRecord> page = find(specification, pageInfo,
				orderRecordRepository);
		model.addAttribute("page", page);
	}

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {

		setContentPage(model, request, pageInfo);

		setModel(model, request, pageInfo);

		return "orderRecord/orderRecord/list";
	}

	@RequestMapping(value = { "{id}/unsubscribe" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult unsubscribe(@PathVariable("id") Long id) {
		OrderRecord orderRecord = orderRecordRepository.findOne(id);
		Date currentTime = new Date();
		if (orderRecord.getUnsubscribeTime() == null
				|| (orderRecord.getExpiredTime() != null && orderRecord
						.getExpiredTime().getTime() > currentTime.getTime())) {
			orderRecord.setExpiredTime(currentTime);
			orderRecord.setUnsubscribeTime(currentTime);
			orderRecordRepository.save(orderRecord);
		}

		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/detail" }, method = RequestMethod.GET)
	public String detail(Model model, HttpServletRequest request,
			@PathVariable("id") Long id, PageInfo pageInfo) {
		OrderRecord orderRecord = orderRecordRepository.findOne(id);
		model.addAttribute("orderRecord", orderRecord);

		setModel(model, request, pageInfo);

		return "orderRecord/orderRecord/detail";
	}
	
	private boolean getExportExcel(List<OrderRecord> orderRecords,
			HttpServletResponse response) throws IOException {
		List<String> groupCodes = subscriberGroupRepository.findAllNotGeneralCode();
		List<String> subscribers = null;
		if (groupCodes != null && groupCodes.size()>0) {
			subscribers = subscriberRepository.findInGroupCodes(groupCodes);
		}
		List<OrderRecordExportLog> logs = new ArrayList<OrderRecordExportLog>();
		for (OrderRecord orderRecord : orderRecords) {
			// 过滤测试，体验帐号的订购记录
			if (orderRecord.getType() != null
					&& orderRecord.getType() != OrderRecordTypeEnum.GENERAL
							.getKey()) {
				continue;
			}
			if (StringUtils.isEmpty(orderRecord.getPartnerUserId())) {
				continue;
			}
			if (subscribers != null
					&& subscribers.contains(orderRecord.getPartnerUserId())) {
				continue;
			}
			OrderRecordExportLog log = new OrderRecordExportLog();
			log.setPartnerUserId(orderRecord.getPartnerUserId());
			log.setOrderCode(orderRecord.getOrderCode());
			log.setProductCode(orderRecord.getProductCode());
			log.setContentCode(orderRecord.getContentCode());
			if (orderRecord.getItemType() != null) {
				log.setItemType(ItemTypeEnum.getEnumByKey(
						orderRecord.getItemType()).getValue());
			}
			if (orderRecord.getItemId() != null) {
				log.setItemId(String.valueOf(orderRecord.getItemId()));
			}
			log.setItemName(orderRecord.getItemName());
			log.setPlayMode(orderRecord.getPlayMode());
			log.setFromEntrance(orderRecord.getFromEntrance());
			if (orderRecord.getSubscriptionTime() != null) {
				log.setSubscriptionTime(DateUtils.formatDate(
						orderRecord.getSubscriptionTime(),
						"yyyy-MM-dd HH:mm:ss"));
			}
			if (orderRecord.getUnsubscribeTime() != null) {
				log.setUnsubscribeTime(DateUtils.formatDate(
						orderRecord.getUnsubscribeTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			log.setPaymentStatus(PaymentStatusEnum.getEnumByKey(
					orderRecord.getPaymentStatus()).getValue());
			if (orderRecord.getValidTime() != null) {
				log.setValidTime(DateUtils.formatDate(
						orderRecord.getValidTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			if (orderRecord.getExpiredTime() != null) {
				log.setExpiredTime(DateUtils.formatDate(
						orderRecord.getExpiredTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			log.setClientIp(orderRecord.getClientIp());
			logs.add(log);
		}
		String fileName = "订购记录.xlsx";
		new ExportExcel("订购记录", "订购记录", OrderRecordExportLog.class, 2,
				OrderRecordExportLog.DEFAULT).setDataList(logs)
				.write(response, fileName).dispose();
		return true;
	}
	
	@RequestMapping(value = { "exportAll" }, method = RequestMethod.GET)
	public BaseResult exportAll(Model model, HttpServletRequest request, PageInfo pageInfo,
			HttpServletResponse response) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}
		try {
			pageInfo.setSize(100000);
			Page<OrderRecord> page = find(request, pageInfo, orderRecordRepository);
			getExportExcel(page.getContent(), response);
			return null;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "batchExport", method = RequestMethod.GET)
	public String batchExport(Model model, @RequestParam(value = "itemType") Integer itemType,
			@RequestParam(value = "itemIds") String itemIds, HttpServletResponse response) {
		try {
			if (itemType == null || StringUtils.isEmpty(itemIds)) {
				return null;
			}

			List<Long> ids = new ArrayList<Long>();
			String[] itemIdArr = itemIds.split(",");
			if (null != itemIdArr) {
				for (String itemIdStr : itemIdArr) {
					Long itemId = Long.valueOf(itemIdStr);
					ids.add(itemId);
				}
			}
			List<OrderRecord> orderRecords = orderRecordRepository.findAll(ids);
			getExportExcel(orderRecords, response);
			return null;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = { "experienceAccount" }, method = RequestMethod.GET)
	public String toAdd(Model model, HttpServletRequest request,
									  PageInfo pageInfo) {
		OrderRecord orderRecord = new OrderRecord();
		model.addAttribute("orderRecord", orderRecord);

		setModel(model,request,pageInfo);

		return "orderRecord/orderRecord/experienceAccount";
	}

	@RequestMapping(value = { "saveAccount" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody OrderRecord orderRecord) {
		OrderRecord orderRecords =null;
		String test = orderRecord.getPartnerUserId();
		String[] partnerUserIdArray = test.split("\r\n");
		String chargeList = orderRecord.getProductCode();
		String[] chargeArray = chargeList.split(",");
		for(int i=0;i<partnerUserIdArray.length;i++){
			if (StringUtils.isNotBlank(partnerUserIdArray[i])) {
				orderRecords = new OrderRecord();
				orderRecords.setType(OrderRecordTypeEnum.EXPERIENCE.getKey());
				orderRecords.setPartnerUserId(partnerUserIdArray[i]);
				orderRecords.setAppCode(orderRecord.getAppCode());//所属app
				orderRecords.setExpiredTime(orderRecord.getExpiredTime());//过期时间
				orderRecords.setSubscriptionTime(orderRecord.getSubscriptionTime());//订购时间
				orderRecords.setUpdateTime(new Date());
				orderRecords.setValidTime(orderRecord.getValidTime());//生效时间
				orderRecords.setProductCode(chargeArray[0]);//产品代码
				orderRecords.setType(OrderRecordTypeEnum.EXPERIENCE.getKey());//为体验中户
				if(chargeArray[1]!=null){
					orderRecords.setPurchaseType(Integer.parseInt(chargeArray[1]));
				}
				if(StringUtils.isNotEmpty(chargeArray[2])){
					orderRecords.setPrice(Integer.parseInt(chargeArray[2]));
				}
				if(StringUtils.isNotEmpty(chargeArray[3])){
					orderRecords.setOriginPrice(Integer.parseInt(chargeArray[3]));
				}
				orderRecords.setOrderCode(orderRecord.getOrderCode());//订单号
				orderRecords.setPaymentStatus(1);
				orderRecordRepository.saveAndFlush(orderRecords);
			}
		}
		return new BaseResult();
	}
}
