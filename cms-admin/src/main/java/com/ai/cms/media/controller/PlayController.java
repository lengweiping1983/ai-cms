package com.ai.cms.media.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ai.AdminGlobal;
import com.ai.common.controller.AbstractImageController;

@Controller
@RequestMapping(value = { "/media/play" })
public class PlayController extends AbstractImageController {

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
			@RequestParam(value = "path", required = false) String sourcePath) {
		logger.info("preview path {" + sourcePath + "}");

		try {
			String playUrl = AdminGlobal.getVideoWebPath(sourcePath);
			model.addAttribute("playUrl", playUrl);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			model.addAttribute("status", "错误!");
		}
	}
}