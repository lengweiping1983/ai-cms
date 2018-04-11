package com.ai.epg.facade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ai.AppGlobal;
import com.ai.cms.live.entity.Channel;
import com.ai.cms.live.entity.ChannelConfig;
import com.ai.cms.live.entity.Schedule;
import com.ai.cms.live.repository.ChannelConfigRepository;
import com.ai.cms.live.repository.ChannelRepository;
import com.ai.cms.live.repository.ScheduleRepository;
import com.ai.common.bean.PageInfo;

/**
 * 直播相关的接口
 *
 */
@Component
public class LiveFacade {

	private static ScheduleRepository scheduleRepository;

	private static ChannelRepository channelRepository;

	private static ChannelConfigRepository channelConfigRepository;

	@Autowired
	public void setScheduleRepository(ScheduleRepository scheduleRepository) {
		LiveFacade.scheduleRepository = scheduleRepository;
	}

	@Autowired
	public void setChannelRepository(ChannelRepository channelRepository) {
		LiveFacade.channelRepository = channelRepository;
	}

	@Autowired
	public void setChannelConfigRepository(
			ChannelConfigRepository channelConfigRepository) {
		LiveFacade.channelConfigRepository = channelConfigRepository;
	}

	/**
	 * 根据频道id获取频道对象
	 * 
	 * @param channelId
	 * @return
	 */
	public static Channel getChannel(final String appCode, final Long channelId) {
		

		return channelRepository.findOne(channelId);
	}

	/**
	 * 获取频道显示名称
	 * 
	 * @param channel
	 * @param maxWidth
	 * @return
	 */
	public static String getChannelTitle(final String appCode, final Channel channel, final int maxWidth) {
		if (channel == null) {
			return "";
		}
		String title = "";
		if (StringUtils.isNotEmpty(channel.getName())) {
			title = channel.getName();
		}
		return AppGlobal.abbreviate(StringUtils.defaultString(title, ""), maxWidth);
	}

	/**
	 * 获取频道横海报
	 * 
	 * @param channel
	 * @param defaultStr
	 * @return
	 */
	public static String getChannelImage1(final String appCode, final Channel channel, final String defaultStr) {
		if (channel == null) {
			return "";
		}
		return AppGlobal.getImagePath(StringUtils.defaultString(channel.getImage1(), defaultStr));
	}

	/**
	 * 获取频道竖海报
	 * 
	 * @param channel
	 * @param defaultStr
	 * @return
	 */
	public static String getChannelImage2(final String appCode, final Channel channel, final String defaultStr) {
		if (channel == null) {
			return "";
		}
		return AppGlobal.getImagePath(StringUtils.defaultString(channel.getImage2(), defaultStr));
	}

	/**
	 * 分页获取频道列表
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public static Page<Channel> getChannelPageList(final String appCode, final int currentPage, final int pageSize) {
		

		PageInfo pageInfo = new PageInfo(currentPage, pageSize);
		Page<Channel> page = channelRepository.findPageList(pageInfo.getPageRequest());
		return page;
	}

	/**
	 * 获取指定数量的频道列表
	 * 
	 * @param maxWidth
	 * @return
	 */
	public static List<Channel> getChannelMaxList(final String appCode, final int maxWidth) {
		

		Page<Channel> page = getChannelPageList(appCode, 0, maxWidth);
		return page.getContent();
	}

	/**
	 * 获取相关的频道列表
	 * 
	 * @param channel
	 * @return
	 */
	public static List<Channel> getRelateChannelList(final String appCode, final Channel channel, final int maxWidth) {
		

		Page<Channel> page = getChannelPageList(appCode, 0, 10);
		return page.getContent();
	}

	/**
	 * 根据回看节目id获取回看节目对象
	 * 
	 * @param scheduleId
	 * @return
	 */
	public static Schedule getSchedule(final String appCode, final Long scheduleId) {
		

		return scheduleRepository.findOne(scheduleId);
	}

	/**
	 * 获取回看节目名称
	 * 
	 * @param schedule
	 * @param maxWidth
	 * @return
	 */
	public static String getScheduleProgramName(final String appCode, final Schedule schedule, final int maxWidth) {
		if (schedule == null) {
			return "";
		}
		String title = "";
		if (StringUtils.isNotEmpty(schedule.getProgramName())) {
			title = schedule.getProgramName();
		}
		return AppGlobal.abbreviate(StringUtils.defaultString(title, ""), maxWidth);
	}

	/**
	 * 根据频道id获取回看节目列表
	 * 
	 * @param channelId
	 * @return
	 */
	public static List<Schedule> getScheduleListByChannelId(final String appCode, final Long channelId) {
		

		return scheduleRepository.findLatestByChannelIdAndOnline(channelId, new Date());
	}

	/**
	 * 根据频道id分页获取频道下某特定日期指定数量的节目单列表
	 * 
	 * @param channelId
	 * @param days
	 *            当前日期的前后天数，-1表示昨天， 1表示明天，0表示今天
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public static Page<Schedule> getChannelItemPageList(final String appCode, final Long channelId, final int days,
			final int currentPage, final int pageSize) {
		
		PageInfo pageInfo = new PageInfo(currentPage, pageSize);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Date().getTime() + days * 24 * 60 * 60 * 1000);
		Date startTime = null;
		Date endTime = null;
		try {
			startTime = sdf2.parse(date + " 00:00:00");
			endTime = sdf2.parse(date + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Page<Schedule> page = scheduleRepository.findProgramByChannelIdAndDate(channelId, startTime, endTime,
				pageInfo.getPageRequest());
		return page;
	}

	/**
	 * 根据频道id获取频道下某特定日期指定数量的节目单列表
	 * 
	 * @param channelId
	 * @return
	 */
	public static List<Schedule> getChannelItemMaxList(final String appCode, final Long channelId, final int days,
			final int maxNum) {
		
		Page<Schedule> page = getChannelItemPageList(appCode, channelId, days, 0, maxNum);
		return page.getContent();
	}

	public static String getChannelNumber(final String appCode, final Long channelId) {
		
		String channelNumber = "";
		List<ChannelConfig> channelConfigList = channelConfigRepository.findByChannelId(channelId);
		if (channelConfigList.size() > 0 && StringUtils.isNotEmpty(channelConfigList.get(0).getPartnerChannelNumber())) {
			channelNumber = channelConfigList.get(0).getPartnerChannelNumber();
		} else {
			Channel channel = channelRepository.findOne(channelId);
			if (channel != null) {
				channelNumber = channel.getNumber();
			}
		}
		return channelNumber;
	}
}