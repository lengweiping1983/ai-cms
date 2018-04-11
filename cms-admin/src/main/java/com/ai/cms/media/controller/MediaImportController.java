package com.ai.cms.media.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import com.ai.AdminGlobal;
import com.ai.cms.config.service.ConfigService;
import com.ai.cms.media.bean.MediaFileExportLog;
import com.ai.cms.media.bean.ProgramImportLog;
import com.ai.cms.media.bean.SeriesImportLog;
import com.ai.cms.media.entity.MediaImport;
import com.ai.cms.media.repository.MediaImportRepository;
import com.ai.cms.media.service.MediaFileImportService;
import com.ai.cms.media.service.ProgramImportService;
import com.ai.cms.media.service.SeriesImportService;
import com.ai.common.bean.BaseResult;
import com.ai.common.bean.PageInfo;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.enums.MediaImportTypeEnum;
import com.ai.common.enums.YesNoEnum;
import com.ai.common.excel.ImportExcel;
import com.ai.common.exception.ServiceException;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.env.handler.OperationLogAnnotation;

@Controller
@RequestMapping(value = { "/media/mediaImport" })
public class MediaImportController extends AbstractImageController {

	@Autowired
	private ConfigService configService;

	@Autowired
	private MediaImportRepository mediaImportRepository;

	@Autowired
	private SeriesImportService seriesImportService;

	@Autowired
	private ProgramImportService programImportService;

	@Autowired
	private MediaFileImportService mediaFileImportService;

	@RequestMapping(value = { "" })
	public String list(Model model, HttpServletRequest request,
			PageInfo pageInfo) {
		if (StringUtils.isEmpty(pageInfo.getOrder())) {
			pageInfo.setOrder("-createTime");
		}
		List<PropertyFilter> filters = getPropertyFilters(request);

		PropertyFilter pf = new PropertyFilter("siteCode__EQ_S",
				AdminGlobal.getSiteCode());
		filters.add(pf);

		Specification<MediaImport> specification = SpecificationUtils
				.getSpecification(filters);
		Page<MediaImport> page = find(specification, pageInfo,
				mediaImportRepository);
		model.addAttribute("page", page);

		model.addAttribute("cpList", configService.findAllCp());

		model.addAttribute("typeEnum", MediaImportTypeEnum.values());
		model.addAttribute("yesNoEnum", YesNoEnum.values());

		return "media/mediaImport/list";
	}

	@RequiresPermissions("media:media:batchImport")
	@RequestMapping(value = { "import/{type}" }, method = RequestMethod.GET)
	public String toImport(Model model, @PathVariable("type") Integer type) {
		MediaImport mediaImport = new MediaImport();
		mediaImport.setType(type);
		model.addAttribute("mediaImport", mediaImport);

		model.addAttribute("typeEnum", MediaImportTypeEnum.values());
		model.addAttribute("yesNoEnum", YesNoEnum.values());

		return "media/mediaImport/edit";
	}

	@OperationLogAnnotation(module = "媒资管理", subModule = "媒资管理", action = "批量导入", message = "批量导入")
	@RequiresPermissions("media:media:batchImport")
	@RequestMapping(value = { "import" }, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public BaseResult importMediaData(MultipartFile file,
			MediaImport mediaImport) {
		try {
			String fileName = file.getOriginalFilename();
			mediaImport.setFileName(fileName);
			mediaImport.setSiteCode(AdminGlobal.getSiteCode());

			int headerRowNum = 2;
			ImportExcel importExcel = new ImportExcel(file, headerRowNum, 0);

			if (mediaImport.getType() == MediaImportTypeEnum.SERIES_META_DATA
					.getKey()) {

				List<SeriesImportLog> seriesImportLogList = importExcel
						.getDataListByTitle(SeriesImportLog.class, 1, false,
								SeriesImportLog.DEFAULT);

				seriesImportService.importMediaData(mediaImport,
						seriesImportLogList, headerRowNum);
			} else if (mediaImport.getType() == MediaImportTypeEnum.PROGRAM_META_DATA
					.getKey()) {

				List<ProgramImportLog> programImportLogList = importExcel
						.getDataListByTitle(ProgramImportLog.class, 1, false,
								ProgramImportLog.DEFAULT);

				programImportService.importMediaData(mediaImport,
						programImportLogList, headerRowNum);
			} else if (mediaImport.getType() == MediaImportTypeEnum.MEDIA_FILE_META_DATA
					.getKey()) {

				List<MediaFileExportLog> mediaFileLogList = importExcel
						.getDataListByTitle(MediaFileExportLog.class, 1, false,
								MediaFileExportLog.DEFAULT);

				mediaFileImportService.importMediaFile(mediaImport,
						mediaFileLogList, headerRowNum);
			}
		} catch (Exception e) {
			throw new ServiceException("解析失败！" + e.getMessage());
		}

		return new BaseResult();
	}

}