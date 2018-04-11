package com.ai.injection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.common.bean.BaseResult;
import com.ai.injection.job.ReceiveJob;

@Controller
@RequestMapping(value = { "/injection/job/" })
public class InjectionController {

	@Autowired
	private ReceiveJob receiveJob;

	@RequestMapping(value = "start", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult start() {
		receiveJob.execute();
		return new BaseResult();
	}

}
