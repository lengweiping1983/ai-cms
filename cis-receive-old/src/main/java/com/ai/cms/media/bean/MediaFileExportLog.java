package com.ai.cms.media.bean;

import java.io.Serializable;

import com.ai.common.excel.annotation.ExcelField;

public class MediaFileExportLog implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT = 1;

	@ExcelField(title = "媒体内容ID", align = 2, groups = { DEFAULT }, sort = 1)
	private Long id;// 媒体内容Id

	@ExcelField(title = "节目名称", align = 1, groups = { DEFAULT }, sort = 11)
	private String name;// 节目名称

	@ExcelField(title = "标题", align = 1, groups = { DEFAULT }, sort = 12)
	private String title;// 标题

	@ExcelField(title = "内容类型", align = 2, groups = { DEFAULT }, sort = 13)
	private String contentType;// 内容类型

	@ExcelField(title = "第几集", align = 1, groups = { DEFAULT }, sort = 21)
	private String episodeIndex;// 第几集

	@ExcelField(title = "媒体内容类型(正片/片花)", align = 1, groups = { DEFAULT }, sort = 31)
	private String mediaFileType;// 媒体内容类型

	@ExcelField(title = "播放时长(单位为秒)", align = 1, groups = { DEFAULT }, sort = 41)
	private String duration;// 播放时长，单位为秒

	@ExcelField(title = "文件路径", align = 1, groups = { DEFAULT }, sort = 51)
	private String filePath;// 文件路径

	@ExcelField(title = "文件MD5值", align = 1, groups = { DEFAULT }, sort = 61)
	private String fileMd5;// 文件MD5值

	@ExcelField(title = "文件大小(单位为B)", align = 1, groups = { DEFAULT }, sort = 71)
	private String fileSize;// 文件大小

	@ExcelField(title = "码率(单位为K)", align = 1, groups = { DEFAULT }, sort = 81)
	private String bitrate; // 码率，单位为K

	@ExcelField(title = "运营商侧节目代码", align = 1, groups = { DEFAULT }, sort = 91)
	private String programPlayCode;// 运营商侧节目代码

	@ExcelField(title = "运营商侧媒体内容代码", align = 1, groups = { DEFAULT }, sort = 101)
	private String playCode;// 运营商侧媒体内容代码

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getEpisodeIndex() {
		return episodeIndex;
	}

	public void setEpisodeIndex(String episodeIndex) {
		this.episodeIndex = episodeIndex;
	}

	public String getMediaFileType() {
		return mediaFileType;
	}

	public void setMediaFileType(String mediaFileType) {
		this.mediaFileType = mediaFileType;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getBitrate() {
		return bitrate;
	}

	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}

	public String getProgramPlayCode() {
		return programPlayCode;
	}

	public void setProgramPlayCode(String programPlayCode) {
		this.programPlayCode = programPlayCode;
	}

	public String getPlayCode() {
		return playCode;
	}

	public void setPlayCode(String playCode) {
		this.playCode = playCode;
	}

}
