package com.ai.cms.live.controller;

import com.ai.AdminGlobal;
import com.ai.cms.config.entity.Cp;
import com.ai.cms.config.repository.CpRepository;
import com.ai.cms.live.entity.ScheduleImportUpdate;
import com.ai.cms.live.entity.ScheduleImportUpdateLog;
import com.ai.cms.live.repository.ScheduleImportUpdateRepository;
import com.ai.cms.live.service.ScheduleImportUpdateService;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.MediaImportTypeEnum;
import com.ai.common.enums.ItemTypeEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.excel.ImportExcel;
import com.ai.common.exception.ServiceException;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.env.handler.OperationLogAnnotation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

@Controller
@RequestMapping(value = {"/scheduleimport/scheduleImportUpdate"})
public class ScheduleImportUpdateController extends AbstractImageController {

	@Autowired
	private CpRepository cpRepository;

	@Autowired
	private ScheduleImportUpdateRepository scheduleRepository;

	@Autowired
	private ScheduleImportUpdateService scheduleImportUpdateService;

	@RequestMapping(value = {""})
	public String list(Model model, HttpServletRequest request,
					   PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}
		List<PropertyFilter> filters = getPropertyFilters(request);
		PropertyFilter pf = new PropertyFilter("siteCode__EQ_S",
				AdminGlobal.getSiteCode());
		filters.add(pf);
		Specification<ScheduleImportUpdate> specification = SpecificationUtils
				.getSpecification(filters);
		Page<ScheduleImportUpdate> page = find(specification, pageInfo,
				scheduleRepository);
		model.addAttribute("page", page);

		List<Cp> cpList = cpRepository.findAll();
		model.addAttribute("cpList", cpList);

		model.addAttribute("typeEnum", MediaImportTypeEnum.values());
		model.addAttribute("yesNoEnum", YesNoEnum.values());

		return "scheduleimport/scheduleImportUpdate/list";
	}

	@RequestMapping(value = {"import/{type}"}, method = RequestMethod.GET)
	public String toImport(Model model, @PathVariable("type") Integer type) {
		ScheduleImportUpdate scheduleImportUpdate = new ScheduleImportUpdate();
		model.addAttribute("scheduleImportUpdate", scheduleImportUpdate);

		model.addAttribute("yesNoEnum", YesNoEnum.values());

		return "scheduleimport/scheduleImportUpdate/edit";
	}

	@OperationLogAnnotation(module = "直播管理", subModule = "导入节目单", action = "增加", message = "增加节目单")
	@RequestMapping(value = {"import"}, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult importProgram(MultipartFile file,
									ScheduleImportUpdate scheduleImportUpdate) {
		try {
			String fileName = file.getOriginalFilename();
			scheduleImportUpdate.setFileName(fileName);
			scheduleImportUpdate.setSiteCode(AdminGlobal.getSiteCode());

			int headerRowNum = 1;
			ImportExcel importExcel = new ImportExcel(file, headerRowNum, 0);

			List<ScheduleImportUpdateLog> scheduleImportUpdateLogList = importExcel
					.getDataListByTitle(ScheduleImportUpdateLog.class, 0,
							false, ScheduleImportUpdateLog.DEFAULT);
			scheduleImportUpdateService.importSchedule(scheduleImportUpdate,
					scheduleImportUpdateLogList, headerRowNum);
		} catch (Exception e) {
			AdminGlobal.operationLogActionResult.set("0");
			throw new ServiceException("解析失败！" + e.getMessage());
		}
		return new BaseResult();
	}

}