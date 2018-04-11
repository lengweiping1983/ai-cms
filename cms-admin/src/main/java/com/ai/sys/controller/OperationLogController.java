package com.ai.sys.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractController;
import com.ai.common.utils.DateUtils;
import com.ai.sys.entity.OperationLog;
import com.ai.sys.repository.OperationLogRepository;

@Controller
@RequestMapping(value = { "/system/operationlog" })
public class OperationLogController extends AbstractController {

	@Autowired
	private OperationLogRepository operationLogRepository;

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}
		Page<OperationLog> page = find(request, pageInfo,
				operationLogRepository);
		model.addAttribute("page", page);
		return "system/operationlog/list";
	}

	@RequestMapping(value = { "clear" }, method = RequestMethod.GET)
	public String toClear(Model model) {
		return "system/operationlog/clear";
	}

	@RequestMapping(value = { "clear/{createTime}" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult clear(@PathVariable("createTime") String createTime) {
		if (createTime != null && !createTime.equals("")) {
			Date d = DateUtils.parseDate(createTime + " 23:59:59");
			operationLogRepository.deleteByCreateTime(d);
		}
		return new BaseResult();
	}

}