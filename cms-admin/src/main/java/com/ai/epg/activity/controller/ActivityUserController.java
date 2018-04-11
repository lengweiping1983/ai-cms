package com.ai.epg.activity.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractController;
import com.ai.common.enums.ValidStatusEnum;
import com.ai.common.excel.ExportExcel;
import com.ai.common.exception.ServiceException;
import com.ai.common.utils.BeanInfoUtil;
import com.ai.common.utils.DateUtils;
import com.ai.epg.market.entity.Activity;
import com.ai.epg.market.entity.ActivityUser;
import com.ai.epg.market.entity.ActivityUserLog;
import com.ai.epg.market.repository.ActivityRepository;
import com.ai.epg.market.repository.ActivityUserRepository;

@Controller
@RequestMapping(value = { "/activity/activityUser" })
public class ActivityUserController extends AbstractController {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private ActivityUserRepository activityUserRepository;

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		Page<ActivityUser> page = find(request, pageInfo,
				activityUserRepository);
		model.addAttribute("page", page);

		model.addAttribute("statusEnum", ValidStatusEnum.values());
		
		String search_activityId__EQ_I = request
				.getParameter("search_activityId__EQ_I");
		if (StringUtils.isNotEmpty(search_activityId__EQ_I)) {
			try {
				Long id = Long.valueOf(search_activityId__EQ_I);
				if (id != null && id > 0) {
					Activity activity = activityRepository.findOne(id);
					if (activity != null) {
						model.addAttribute("activity", activity);
					}
				}
			} catch (Exception e) {

			}
		}

		return "activity/activityUser/list";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.GET)
	public String toAdd(Model model) {
		model.addAttribute("statusEnum", ValidStatusEnum.values());

		return "activity/activityUser/edit";
	}

	@RequestMapping(value = { "add" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult add(@RequestBody ActivityUser activityUser) {
		return edit(activityUser, null);
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.GET)
	public String toEdit(Model model, @PathVariable("id") Long id) {
		ActivityUser activityUser = activityUserRepository.findOne(id);
		model.addAttribute("activityUser", activityUser);

		model.addAttribute("statusEnum", ValidStatusEnum.values());
		return "activity/activityUser/edit";
	}

	@RequestMapping(value = { "{id}/edit" }, method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult edit(@RequestBody ActivityUser activityUser,
			@PathVariable("id") Long id) {
		if (id == null) {
			activityUserRepository.save(activityUser);
		} else {
			ActivityUser activityUserInfo = activityUserRepository
					.findOne(activityUser.getId());
			BeanInfoUtil.bean2bean(activityUser, activityUserInfo,
					"phone,reserved1,reserved2,reserved3,reserved4,reserved5");
			activityUserRepository.save(activityUserInfo);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "{id}/delete" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long id) {
		activityUserRepository.delete(id);
		return new BaseResult();
	}

	@RequestMapping(value = { "export" }, method = RequestMethod.GET)
	public BaseResult export(Model model, HttpServletRequest request,
			PageInfo pageInfo, HttpServletResponse response) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}
		try {
			pageInfo.setSize(100000);
			Page<ActivityUser> page = find(request, pageInfo,
					activityUserRepository);
			getExportExcel(page.getContent(), response);
			return null;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	private boolean getExportExcel(List<ActivityUser> activityUsers,
			HttpServletResponse response) throws IOException {
		Map<Long, Activity> activityMap = new HashMap<Long, Activity>();

		List<Activity> activityList = activityRepository.findAll();
		for (Activity activity : activityList) {
			activityMap.put(activity.getId(), activity);
		}

		List<ActivityUserLog> logs = new ArrayList<ActivityUserLog>();

		for (ActivityUser activityUser : activityUsers) {

			ActivityUserLog log = new ActivityUserLog();

			if (activityUser.getActivityId() != null) {
				Activity activity = activityMap.get(activityUser
						.getActivityId());
				if (activity != null) {
					log.setActivityName(activity.getName());
				}
			}
			if (activityUser.getCreateTime() != null) {
				log.setCreateTime(DateUtils.formatDate(
						activityUser.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			if (activityUser.getPartnerUserId() != null) {
				log.setPartnerUserId(StringUtils.trimToEmpty(activityUser
						.getPartnerUserId()));
			}
			if (activityUser.getPhone() != null) {
				log.setPhone(StringUtils.trimToEmpty(activityUser.getPhone()));
			}

			logs.add(log);
		}

		String fileName = "参与用户.xlsx";
		new ExportExcel("参与用户", "参与用户", ActivityUserLog.class, 2,
				ActivityUserLog.DEFAULT).setDataList(logs)
				.write(response, fileName).dispose();
		return true;
	}
}
