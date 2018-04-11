package com.ai.cms.transcode.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.cms.config.service.ConfigService;
import com.ai.cms.transcode.entity.TranscodeRequest;
import com.ai.cms.transcode.entity.TranscodeTask;
import com.ai.cms.transcode.enums.TranscodeRequestTypeEnum;
import com.ai.cms.transcode.enums.TranscodeTaskStatusEnum;
import com.ai.cms.transcode.enums.TranscodeTaskTypeEnum;
import com.ai.cms.transcode.repository.TranscodeRequestRepository;
import com.ai.cms.transcode.repository.TranscodeTaskRepository;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;

@Controller
@RequestMapping(value = { "/transcode/{transcodeRequestId}/transcodeTask" })
public class TranscodeRequestTaskController extends AbstractImageController {

	@Autowired
	private TranscodeRequestRepository transcodeRequestRepository;

	@Autowired
	private TranscodeTaskRepository transcodeTaskRepository;

	@Autowired
	private ConfigService configService;

	private void setModel(Model model) {
		model.addAttribute("statusEnum", TranscodeTaskStatusEnum.values());
		model.addAttribute("typeEnum", TranscodeTaskTypeEnum.values());

		model.addAttribute("transcodeRequestTypeEnum",
				TranscodeRequestTypeEnum.values());

		model.addAttribute("mediaTemplateList",
				configService.findAllMediaTemplate());
	}

	@RequestMapping(value = { "" })
	public String list(
			@PathVariable("transcodeRequestId") Long transcodeRequestId,
			Model model, HttpServletRequest request, PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-priority");
		}

		TranscodeRequest transcodeRequest = transcodeRequestRepository
				.findOne(transcodeRequestId);
		model.addAttribute("transcodeRequest", transcodeRequest);

		Page<TranscodeTask> page = find(request, pageInfo,
				transcodeTaskRepository);
		model.addAttribute("page", page);

		setModel(model);

		return "transcode/transcodeRequestTask/list";
	}

}
