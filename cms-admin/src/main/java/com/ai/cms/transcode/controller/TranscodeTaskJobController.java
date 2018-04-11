package com.ai.cms.transcode.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.cms.transcode.entity.TranscodeRequest;
import com.ai.cms.transcode.entity.TranscodeTask;
import com.ai.cms.transcode.job.TranscodeTaskJob;
import com.ai.cms.transcode.repository.TranscodeRequestRepository;
import com.ai.cms.transcode.repository.TranscodeTaskRepository;
import com.ai.cms.transcode.service.TranscodeService;
import com.ai.common.bean.BaseResult;
import com.ai.common.controller.AbstractImageController;

@Controller
@RequestMapping(value = { "/transcode/job/" })
public class TranscodeTaskJobController extends AbstractImageController {

	@Autowired
	private TranscodeTaskJob transcodeTaskJob;

	@Autowired
	private TranscodeRequestRepository transcodeRequestRepository;

	@Autowired
	private TranscodeTaskRepository transcodeTaskRepository;

	@Autowired
	private TranscodeService transcodeService;

	@RequestMapping(value = { "start" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult start() {
		transcodeTaskJob.execute();
		return new BaseResult();
	}

	@RequestMapping(value = { "all" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult all() {
		List<TranscodeRequest> transcodeRequests = transcodeRequestRepository
				.findAll();
		for (TranscodeRequest transcodeRequest : transcodeRequests) {
			transcodeService.updateTranscodeRequest(transcodeRequest, true);
		}
		return new BaseResult();
	}

	@RequestMapping(value = { "taskAll" }, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult taskAll() {
		List<TranscodeTask> transcodeTasks = transcodeTaskRepository.findAll();
		int result = 0; // 0=成功,1=失败,2=取消
		String message = "";
		for (TranscodeTask transcodeTask : transcodeTasks) {
			if (StringUtils.isNotEmpty(transcodeTask.getCloudTaskId())) {
				transcodeService.updateTranscodeTaskFromCallback(
						transcodeTask.getCloudTaskId(), result, message);
			}
		}
		return new BaseResult();
	}

}
