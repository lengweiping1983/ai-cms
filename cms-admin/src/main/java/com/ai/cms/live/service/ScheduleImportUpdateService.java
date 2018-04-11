package com.ai.cms.live.service;

import com.ai.AdminGlobal;
import com.ai.cms.config.repository.CpRepository;
import com.ai.cms.live.entity.Channel;
import com.ai.cms.live.entity.Schedule;
import com.ai.cms.live.entity.ScheduleImportUpdate;
import com.ai.cms.live.entity.ScheduleImportUpdateLog;
import com.ai.cms.live.repository.ChannelRepository;
import com.ai.cms.live.repository.ScheduleImportUpdateRepository;
import com.ai.cms.live.repository.ScheduleRepository;
import com.ai.common.exception.ServiceException;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;
import com.ai.common.utils.DateUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(value = "slaveTransactionManager", readOnly = true)
public class ScheduleImportUpdateService extends
		AbstractService<Schedule, Long> {
	private static final Log log = LogFactory
			.getLog(ScheduleImportUpdateService.class);

	@Autowired
	private ScheduleImportUpdateRepository scheduleImportUpdateRepository;

	@Autowired
	private CpRepository cpRepository;

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private ChannelRepository channelRepository;

	@Override
	public AbstractRepository<Schedule, Long> getRepository() {
		return scheduleRepository;
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public synchronized void importSchedule(
			ScheduleImportUpdate scheduleImportUpdate,
			List<ScheduleImportUpdateLog> logList, int startRow) {
		if (logList == null || logList.size() <= 1) {
			throw new ServiceException("内容为空!");
		}

		log.info("importSchedule totalNum=" + logList.size() + " begin...");
		Map<String, Long> cMap = new HashMap<String, Long>();
		List<Channel> cList = channelRepository.findAll();
		for (Channel c : cList) {
			cMap.put(StringUtils.trimToEmpty(c.getName()), c.getId());
		}

		// 检查文件内容
		for (int i = 0; i < logList.size(); i++) {
			int row = startRow + i + 1;
			ScheduleImportUpdateLog scheduleImportUpdateLog = logList.get(i);

			if (StringUtils.isEmpty(scheduleImportUpdateLog.getChannelName())) {
				throw new ServiceException("第" + row + "行，频道名称为空!");
			}
			if (StringUtils.isEmpty(scheduleImportUpdateLog.getContentName())) {
				throw new ServiceException("第" + row + "行，节目名称为空!");
			}

			if (StringUtils.isNotEmpty(scheduleImportUpdateLog.getTimeLength())) {
				try {
					Integer.valueOf(scheduleImportUpdateLog.getTimeLength());
				} catch (Exception e) {
					throw new ServiceException("第" + row + "行，时长["
							+ scheduleImportUpdateLog.getTimeLength() + "]不正确!");
				}
			}
			if (StringUtils.isNotEmpty(scheduleImportUpdateLog.getTime())) {
				if (scheduleImportUpdateLog.getTime().split(":").length <= 1) {
					throw new ServiceException("第" + row + "行，时间["
							+ scheduleImportUpdateLog.getTime() + "]不正确!");
				}
			}

			if (StringUtils.isNotEmpty(scheduleImportUpdateLog.getAirDate())) {
				if (scheduleImportUpdateLog.getAirDate().split("-").length <= 2) {
					throw new ServiceException("第" + row + "行，日期["
							+ scheduleImportUpdateLog.getTime() + "]不正确!");
				}
			}
		}

		int successCount = 0;
		int errorCount = 0;
		String rows = "";
		for (int i = 0; i < logList.size(); i++) {
			String row = startRow + i + 1 + "";
			ScheduleImportUpdateLog scheduleImportUpdateLog = logList.get(i);
			Schedule schedule = new Schedule();
			String start = scheduleImportUpdateLog.getAirDate() + " "
					+ scheduleImportUpdateLog.getTime() + ":00";
			schedule.setBeginTime(DateUtils.parseDate(start));
			schedule.setDuration(Integer.parseInt(scheduleImportUpdateLog
					.getTimeLength()));
			schedule.setEndTime(new Date(DateUtils.parseDate(start).getTime()
					+ Integer.parseInt(scheduleImportUpdateLog.getTimeLength())
					* 60 * 1000));
			schedule.setProgramName(scheduleImportUpdateLog.getContentName());
			schedule.setChannelId(cMap.get(scheduleImportUpdateLog
					.getChannelName().trim()));
			List<Schedule> schedulelist = scheduleRepository
					.findByChannelIdAndBeginTime(schedule.getChannelId(),
							schedule.getBeginTime());
			if (schedulelist.size() == 0) {
				scheduleRepository.save(schedule);
				successCount++;
			} else {
				errorCount++;
				rows += row + ",";
			}
		}
		scheduleImportUpdate.setImportTime(new Date());
		scheduleImportUpdate.setSuccess(successCount);
		scheduleImportUpdate.setFailure(errorCount);
		if (errorCount > 0) {
			scheduleImportUpdate.setErrorMessage("第"
					+ rows.substring(0, rows.length() - 1) + "行已存在，不能重复导入！");
		} else {
			scheduleImportUpdate.setErrorMessage("导入成功！");
		}
		scheduleImportUpdateRepository.save(scheduleImportUpdate);
		log.info("importSchedule totalNum=" + logList.size() + " end.");
		AdminGlobal.operationLogTypeAndId.set("102$"
				+ scheduleImportUpdate.getId());
		AdminGlobal.operationLogMessage.set("增加节目单");
		AdminGlobal.operationLogActionResult.set("1");

	}

}