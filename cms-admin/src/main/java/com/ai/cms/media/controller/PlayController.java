package com.ai.cms.media.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ai.AdminGlobal;
import com.ai.cms.media.bean.PlayBean;
import com.ai.cms.media.entity.MediaFile;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.Series;
import com.ai.cms.media.repository.MediaFileRepository;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.cms.media.repository.SeriesRepository;
import com.ai.common.controller.AbstractImageController;

@Controller
@RequestMapping(value = { "/media/play" })
public class PlayController extends AbstractImageController {

	@Autowired
	protected SeriesRepository seriesRepository;

	@Autowired
	protected ProgramRepository programRepository;

	@Autowired
	protected MediaFileRepository mediaFileRepository;

	@RequestMapping(value = { "previewMediaFile" })
	public String previewMediaFile(Model model, HttpServletRequest request,
			@RequestParam(value = "id") Long id) {
		List<PlayBean> playBeanList = new ArrayList<PlayBean>();
		model.addAttribute("playBeanList", playBeanList);

		MediaFile mediaFile = mediaFileRepository.findOne(id);
		if (mediaFile != null) {
			PlayBean playBean = getVideoWebPath(null, mediaFile);
			playBeanList.add(playBean);
		}
		return "media/play/preview";
	}

	@RequestMapping(value = { "previewProgram" })
	public String previewProgram(Model model, HttpServletRequest request,
			@RequestParam(value = "id") Long id) {
		List<PlayBean> playBeanList = new ArrayList<PlayBean>();
		model.addAttribute("playBeanList", playBeanList);

		Program program = programRepository.findOne(id);
		if (program != null) {
			List<MediaFile> mediaFileList = mediaFileRepository
					.findByProgramId(program.getId());
			for (MediaFile mediaFile : mediaFileList) {
				PlayBean playBean = getVideoWebPath(program, mediaFile);
				playBeanList.add(playBean);
			}
		}
		return "media/play/preview";
	}

	@RequestMapping(value = { "previewSeries" })
	public String previewSeries(Model model, HttpServletRequest request,
			@RequestParam(value = "id") Long id) {
		List<PlayBean> playBeanList = new ArrayList<PlayBean>();
		model.addAttribute("playBeanList", playBeanList);

		Series series = seriesRepository.findOne(id);
		if (series != null) {
			Map<Long, Program> programMap = new HashMap<Long, Program>();
			List<Program> programList = programRepository.findBySeriesId(series
					.getId());
			for (Program program : programList) {
				programMap.put(program.getId(), program);
			}

			List<MediaFile> mediaFileList = mediaFileRepository
					.findBySeriesId(series.getId());

			for (MediaFile mediaFile : mediaFileList) {
				PlayBean playBean = getVideoWebPath(
						programMap.get(mediaFile.getProgramId()), mediaFile);
				playBeanList.add(playBean);
			}
		}
		return "media/play/preview";
	}

	public PlayBean getVideoWebPath(Program program, MediaFile mediaFile) {
		PlayBean playBean = new PlayBean();
		String playUrl = StringUtils.trimToEmpty(mediaFile.getFilePath());
		if (playUrl.indexOf("ftp") == 0) {
		} else if (playUrl.indexOf("http") == 0) {
		} else {
			playUrl = AdminGlobal.joinPath(AdminGlobal.getVideoWebPath(),
					playUrl);
		}
		if (program != null) {
			playBean.setName(program.getName());
		} else {
			playBean.parserNameFromPlayUrl();
		}
		if (mediaFile.getDuration() != null) {
			playBean.setDuration(mediaFile.getDuration());
		} else if (program != null && program.getDuration() != null) {
			playBean.setDuration(program.getDuration() * 60);
		}
		playBean.setPlayUrl(playUrl);
		playBean.setBitrate(mediaFile.getBitrate());
		return playBean;
	}

	@RequestMapping(value = { "preview" })
	public String preview(Model model, HttpServletRequest request,
			@RequestParam(value = "path", required = false) String path) {
		getPlayUrl(model, request, path);
		return "media/play/preview";
	}

	@RequestMapping(value = { "previewNewWindow" })
	public String previewNewWindow(Model model, HttpServletRequest request,
			@RequestParam(value = "path", required = false) String path) {
		getPlayUrl(model, request, path);
		return "media/play/previewNewWindow";
	}

	public void getPlayUrl(Model model, HttpServletRequest request,
			@RequestParam(value = "path", required = false) String path) {
		logger.info("preview path {" + path + "}");
		List<PlayBean> playBeanList = new ArrayList<PlayBean>();
		model.addAttribute("playBeanList", playBeanList);

		try {
			String playUrl = AdminGlobal.getVideoWebPath(path);
			PlayBean playBean = new PlayBean();
			playBean.setPlayUrl(playUrl);
			playBean.parserNameFromPlayUrl();
			playBeanList.add(playBean);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			model.addAttribute("status", "错误!");
		}
	}
}