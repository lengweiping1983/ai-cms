package com.ai.cms.live.service;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;






import com.ai.cms.injection.repository.InjectionObjectRepository;
import com.ai.cms.live.entity.Channel;
import com.ai.cms.live.entity.ChannelConfig;
import com.ai.cms.live.entity.Schedule;
import com.ai.cms.live.repository.ChannelConfigRepository;
import com.ai.cms.live.repository.ChannelRepository;
import com.ai.cms.live.repository.ScheduleRepository;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;

@Service
@Transactional(value = "slaveTransactionManager", readOnly = true)
public class LiveService extends AbstractService<Channel, Long> {

	@Autowired
	private ChannelRepository channelRepository;

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private ChannelConfigRepository channelConfigRepository;

	@Autowired
	private InjectionObjectRepository injectionObjectRepository;

	@Override
	public AbstractRepository<Channel, Long> getRepository() {
		return channelRepository;
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void deleteChannel(Channel channel) {
		if (channel != null) {
			scheduleRepository.deleteByChannelId(channel.getId());
			channelRepository.delete(channel);
		}
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void saveSchedule(Schedule schedule) {
		if (schedule.getBeginTime() != null && schedule.getDuration() != null) {
			schedule.setEndTime(DateUtils.addMinutes(schedule.getBeginTime(),
					schedule.getDuration()));
		}
		scheduleRepository.save(schedule);

//		InjectionObject injectionObject = injectionObjectRepository
//				.findOneByItemTypeAndItemId(
//						InjectionItemTypeEnum.SCHEDULE.getKey(),
//						schedule.getId());
//
//		if (StringUtils.isNotEmpty(schedule.getPartnerItemCode())) {
//			if (injectionObject == null) {
//				injectionObject = new InjectionObject();
//				injectionObject.setItemType(InjectionItemTypeEnum.SCHEDULE
//						.getKey());
//				injectionObject.setItemId(schedule.getId());
//			}
//			if (!StringUtils.trimToEmpty(schedule.getPartnerItemCode()).equals(
//					injectionObject.getPartnerItemCode())) {
//				injectionObject.setInjectionStatus(InjectionStatusEnum.INJECTED
//						.getKey());
//				injectionObject.setInjectionTime(new Date());
//				injectionObject.setPartnerItemCode(schedule
//						.getPartnerItemCode());
//				injectionObjectRepository.save(injectionObject);
//			}
//		} else {
//			if (injectionObject != null) {
//				injectionObjectRepository.delete(injectionObject);
//			}
//		}
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void deleteSchedule(Schedule schedule) {
		if (schedule != null) {
			scheduleRepository.delete(schedule);

//			InjectionObject injectionObject = injectionObjectRepository
//					.findOneByItemTypeAndItemId(
//							InjectionItemTypeEnum.SCHEDULE.getKey(),
//							schedule.getId());
//			if (injectionObject != null) {
//				injectionObjectRepository.delete(injectionObject);
//			}
		}
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void saveChannelConfig(ChannelConfig channelConfig) {
		channelConfigRepository.save(channelConfig);
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void deleteChannelConfig(ChannelConfig channelConfig) {
		if (channelConfig != null) {
			channelConfigRepository.delete(channelConfig);
		}
	}

}