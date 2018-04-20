package com.ai.cms.media.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ai.AdminGlobal;
import com.ai.cms.media.bean.M3U8Bean;
import com.ai.cms.media.bean.PlayBean;
import com.ai.cms.media.entity.MediaFile;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.Series;
import com.ai.cms.media.repository.MediaFileRepository;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.cms.media.repository.SeriesRepository;
import com.ai.common.controller.AbstractImageController;
import com.ai.common.utils.IOUtils;

@Controller
@RequestMapping(value = { "/media/play" })
public class PlayController extends AbstractImageController {
	public static Map<String, String> filePathMap = new HashMap<String, String>();

	@Autowired
	protected SeriesRepository seriesRepository;

	@Autowired
	protected ProgramRepository programRepository;

	@Autowired
	protected MediaFileRepository mediaFileRepository;

	public static String genM3U8(String path, Long fileSize, Integer duration)
			throws Exception {
		String filePath = AdminGlobal.getVideoWebPath(path);
		StringBuffer sb = new StringBuffer();
		sb.append("#EXTM3U").append("\n");
		sb.append("#EXT-X-VERSION:4").append("\n");
		sb.append("#EXT-X-PLAYLIST-TYPE:VOD").append("\n");
		sb.append("#EXT-X-MEDIA-SEQUENCE:0").append("\n");
		if (fileSize != null && duration != null) {
			List<M3U8Bean> m3u8BeanList = new ArrayList<M3U8Bean>();

			int sqlitDuration = 60;
			long secondFileSize = fileSize / duration;
			int sqlitTotal = duration / sqlitDuration;

			for (int i = 0; i < sqlitTotal; i++) {
				M3U8Bean m3u8Bean = new M3U8Bean();
				long byterangeStart = secondFileSize * sqlitDuration * i;
				long byterangeNum = secondFileSize * sqlitDuration;
				m3u8Bean.setDuration(sqlitDuration);
				m3u8Bean.setByterangeStart(byterangeStart);
				m3u8Bean.setByterangeNum(byterangeNum);
				m3u8BeanList.add(m3u8Bean);
			}
			if (duration % sqlitDuration != 0) {
				M3U8Bean m3u8Bean = new M3U8Bean();
				int lastSqlitDuration = duration % sqlitDuration;
				long byterangeStart = secondFileSize * sqlitDuration
						* sqlitTotal;
				long byterangeNum = fileSize - byterangeStart;

				m3u8Bean.setDuration(lastSqlitDuration);
				m3u8Bean.setByterangeStart(byterangeStart);
				m3u8Bean.setByterangeNum(byterangeNum);
				m3u8BeanList.add(m3u8Bean);
			}
			sb.append("#EXT-X-TARGETDURATION").append(sqlitDuration)
					.append("\n");

			for (M3U8Bean m3u8Bean : m3u8BeanList) {
				sb.append("#EXTINF:").append(m3u8Bean.getDuration())
						.append("\n");
				sb.append("#EXT-X-BYTERANGE:")
						.append(m3u8Bean.getByterangeNum()).append("@")
						.append(m3u8Bean.getByterangeStart()).append("\n");
				sb.append(filePath).append("\n");
			}
			sb.append("#ZEN-TOTAL-DURATION:").append(duration).append("\n");
		} else if (fileSize != null) {
			List<M3U8Bean> m3u8BeanList = new ArrayList<M3U8Bean>();

			long sqlitSize = 30 * 1024 * 1024l;
			long sqlitTotal = fileSize / sqlitSize;

			for (int i = 0; i < sqlitTotal; i++) {
				M3U8Bean m3u8Bean = new M3U8Bean();
				long byterangeStart = sqlitSize * i;
				long byterangeNum = sqlitSize;
				m3u8Bean.setDuration(0);
				m3u8Bean.setByterangeStart(byterangeStart);
				m3u8Bean.setByterangeNum(byterangeNum);
				m3u8BeanList.add(m3u8Bean);
			}
			if (fileSize % sqlitSize != 0) {
				M3U8Bean m3u8Bean = new M3U8Bean();
				long byterangeStart = sqlitSize * sqlitTotal;
				long byterangeNum = fileSize - byterangeStart;

				m3u8Bean.setDuration(0);
				m3u8Bean.setByterangeStart(byterangeStart);
				m3u8Bean.setByterangeNum(byterangeNum);
				m3u8BeanList.add(m3u8Bean);
			}
			for (M3U8Bean m3u8Bean : m3u8BeanList) {
				sb.append("#EXTINF:").append(m3u8Bean.getDuration())
						.append("\n");
				sb.append("#EXT-X-BYTERANGE:")
						.append(m3u8Bean.getByterangeNum()).append("@")
						.append(m3u8Bean.getByterangeStart()).append("\n");
				sb.append(filePath).append("\n");
			}
		} else {
			sb.append("#EXTINF:").append(0).append("\n");
			sb.append(filePath).append("\n");
		}
		sb.append("#EXT-X-ENDLIST").append("\n");
		return sb.toString();
	}

	@RequestMapping(value = "/mediaFile/{id}.m3u8")
	public void mediaFileM3U8(Model model, HttpServletRequest request,
			HttpServletResponse response, @PathVariable(value = "id") Long id)
			throws Exception {
		MediaFile mediaFile = mediaFileRepository.findOne(id);
		mediaFileM3U8(model, request, response, mediaFile);
	}

	@RequestMapping(value = "/uuid/{uuid}.m3u8")
	public void uuidM3U8(Model model, HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable(value = "uuid") String uuid) throws Exception {
		String path = filePathMap.get(uuid);
		if (StringUtils.isNotEmpty(path)) {
			File file = new File(AdminGlobal.getVideoUploadPath(path));
			if (file.exists()) {
				String playUrl = AdminGlobal.getVideoWebPath(path);
				String m3u8Text = genM3U8(playUrl, file.length(), null);
				logger.info(m3u8Text);
				response.getWriter().print(m3u8Text);
			}
		}
		response.getWriter().flush();
		response.getWriter().close();
	}

	public void mediaFileM3U8(Model model, HttpServletRequest request,
			HttpServletResponse response, MediaFile mediaFile) throws Exception {
		if (mediaFile != null) {
			String m3u8Text;
			if (StringUtils.isNotEmpty(mediaFile.getM3u8Url())) {
				m3u8Text = IOUtils.readTxtFile(AdminGlobal
						.getM3U8UploadPath(mediaFile.getM3u8Url()));
			} else {
				String filePath = mediaFile.getFilePath();
				Long fileSize = mediaFile.getFileSize();
				Integer duration = mediaFile.getDuration();
				m3u8Text = genM3U8(filePath, fileSize, duration);
			}
			logger.info(m3u8Text);
			response.getWriter().print(m3u8Text);
		}
		response.getWriter().flush();
		response.getWriter().close();
	}

	@RequestMapping(value = { "previewMediaFile" })
	public String previewMediaFile(Model model, HttpServletRequest request,
			@RequestParam(value = "id") Long id) {
		List<PlayBean> playBeanList = new ArrayList<PlayBean>();
		model.addAttribute("playBeanList", playBeanList);

		MediaFile mediaFile = mediaFileRepository.findOne(id);
		if (mediaFile != null) {
			Program program = programRepository.findOne(mediaFile
					.getProgramId());
			PlayBean playBean = getVideoWebPath(program, mediaFile);
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
		String suffix = StringUtils.substringAfterLast(playUrl, ".");
		if ("ts".equalsIgnoreCase(suffix)) {
			playUrl = AdminGlobal.getWebAccessUrl() + "/media/play/mediaFile/"
					+ mediaFile.getId() + ".m3u8";
		}
		playBean.setPlayUrl(playUrl);
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
		playBean.setBitrate(mediaFile.getBitrate());
		return playBean;
	}

	@RequestMapping(value = { "preview" })
	public String preview(Model model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "path", required = false) String path)
			throws Exception {
		getPlayUrl(model, request, response, path);
		return "media/play/preview";
	}

	@RequestMapping(value = { "previewNewWindow" })
	public String previewNewWindow(Model model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "path", required = false) String path)
			throws Exception {
		getPlayUrl(model, request, response, path);
		return "media/play/previewNewWindow";
	}

	public void getPlayUrl(Model model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "path", required = false) String path)
			throws Exception {
		logger.info("preview path {" + path + "}");

		List<MediaFile> mediaFileList = mediaFileRepository
				.findByFilePath(path);
		if (mediaFileList != null && mediaFileList.size() > 0) {
			previewMediaFile(model, request, mediaFileList.get(0).getId());
			return;
		}
		String playUrl = AdminGlobal.getVideoWebPath(path);

		logger.info("playUrl=" + playUrl);
		List<PlayBean> playBeanList = new ArrayList<PlayBean>();
		model.addAttribute("playBeanList", playBeanList);

		PlayBean playBean = new PlayBean();
		playBean.setPlayUrl(playUrl);
		playBean.parserNameFromPlayUrl();
		playBeanList.add(playBean);

		String suffix = StringUtils.substringAfterLast(playUrl, ".");
		File file = new File(AdminGlobal.getVideoUploadPath(path));
		if ("ts".equalsIgnoreCase(suffix) && file.exists()) {
			String uuid = UUID.randomUUID().toString();
			filePathMap.put(uuid, path);
			playUrl = AdminGlobal.getWebAccessUrl() + "/media/play/uuid/"
					+ uuid + ".m3u8";
			playBean.setPlayUrl(playUrl);
		}
		model.addAttribute("playUrl", playUrl);
	}

	public static void main(String[] args) throws Exception {
		String filePath = "http://127.0.0.1:8080/video/xx.ts";
		Long fileSize = 5413246056l;
		Integer duration = 6720;
		String m3u8Text = genM3U8(filePath, fileSize, duration);
		System.out.println(m3u8Text);
		String uuid = UUID.randomUUID().toString();
		System.out.println(uuid);
	}
}