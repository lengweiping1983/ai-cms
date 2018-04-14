package com.ai.cms.media.bean;

import org.apache.commons.lang3.StringUtils;

public class PlayBean {
	public static String PLAY_TYPE_MP4 = "video/mp4";
	public static String PLAY_TYPE_WEBM = "video/webm";
	public static String PLAY_TYPE_OGG = "video/ogg";
	public static String PLAY_TYPE_TS = "video/mp4";
	public static String PLAY_TYPE_M3U8 = "application/x-mpegURL";

	private String name;
	private String description;
	private Integer duration = 0;
	private String playUrl;
	private String type;
	private Integer bitrate;// 码率，单位为K

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
		parserTypeFromPlayUrl();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getBitrate() {
		return bitrate;
	}

	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}

	public void parserNameFromPlayUrl() {
		if (StringUtils.isNotEmpty(playUrl)) {
			name = StringUtils.substringAfterLast(playUrl, "/");
		}
	}

	public void parserTypeFromPlayUrl() {
		if (StringUtils.isNotEmpty(playUrl)) {
			String suffix = StringUtils.substringAfterLast(playUrl, ".");
			if ("mp4".equalsIgnoreCase(suffix)) {
				type = PLAY_TYPE_MP4;
			} else if ("ogg".equalsIgnoreCase(suffix)) {
				type = PLAY_TYPE_OGG;
			} else if ("webm".equalsIgnoreCase(suffix)) {
				type = PLAY_TYPE_WEBM;
			} else if ("ts".equalsIgnoreCase(suffix)) {
				type = PLAY_TYPE_TS;
			} else if ("m3u8".equalsIgnoreCase(suffix)) {
				type = PLAY_TYPE_M3U8;
			}
		}
	}

}
