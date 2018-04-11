package com.ai.epg.product.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;
import com.ai.common.utils.DateUtils;
import com.ai.epg.product.entity.Charge;
import com.ai.epg.product.repository.ChargeRepository;
import com.ai.epg.seq.repository.SequenceRepository;
import com.ai.epg.subscriber.repository.OrderRecordRepository;
import com.ai.epg.subscriber.repository.SubscriberRepository;

@Service
@Transactional(readOnly = true)
public class OrderService extends AbstractService<Charge, Long> {

	@Autowired
	protected SequenceRepository sequenceRepository;

	@Autowired
	protected OrderRecordRepository orderRecordRepository;

	@Autowired
	protected ChargeRepository chargeRepository;

	@Autowired
	protected SubscriberRepository subscriberRepository;

	@Override
	public AbstractRepository<Charge, Long> getRepository() {
		return chargeRepository;
	}
	
	public Date getTime(Charge charge, Date currentDate) {
		Integer timeUnit = charge.getTimeUnit();
		Integer duration = charge.getDuration();
		if (timeUnit == null) {
			timeUnit = 1;
		}
		if (duration == null) {
			duration = 1;
		}
		switch (timeUnit) {
		case 1:
			return DateUtils.addHours(currentDate, duration);
		case 2:
			return DateUtils.addDays(currentDate, duration);
		case 3:
			return DateUtils.addMonths(currentDate, duration);
		case 4:
			return DateUtils.addYears(currentDate, duration);
		}
		return currentDate;
	}

	public Date getExpiredTime(Charge charge, Date currentDate) {
		Date expiredTime = currentDate;
		switch (charge.getType()) {
		case 1:
			expiredTime = getTime(charge, currentDate);
			break;
		case 2:
			expiredTime = DateUtils.addYears(currentDate, 20);// 过期时间设为20年
			break;
		case 3:
			expiredTime = getTime(charge, currentDate);
			break;
		case 4:
			expiredTime = DateUtils.addYears(currentDate, 20);// 过期时间设为20年
			break;
		case 5:
			expiredTime = getTime(charge, currentDate);
			break;
		default:
			break;
		}
		return expiredTime;
	}

	
	public Charge getCharge(String appCode, String productId) {
		List<Charge> chargeList = chargeRepository
				.findByPartnerProductCode(StringUtils.trimToEmpty(productId));
		if (chargeList != null && chargeList.size() > 0) {
			return chargeList.get(0);
		}
		return null;
	}
	
	public String getOrderCodeSeq() {
		return getNowDateString()
				+ fillSeq(sequenceRepository.findOrderCodeSeq());
	}

	public String getTransactionCode() {
		return getNowDateString()
				+ fillSeq(sequenceRepository.findTransactionIDSeq());
	}

	public static String getNowDateString() {
		return DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
	}

	public static String fillSeq(long seq) {
		String str = "000000000000000000" + seq;
		return str.substring(str.length() - 18, str.length());
	}

}