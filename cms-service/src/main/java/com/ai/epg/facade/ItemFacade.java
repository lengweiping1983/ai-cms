package com.ai.epg.facade;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.ai.cms.live.entity.Channel;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.Series;
import com.ai.common.enums.ItemTypeEnum;
import com.ai.common.utils.PathUtils;
import com.ai.epg.album.entity.Album;

/**
 * 元素相关的接口
 *
 */
@Component
public class ItemFacade {

	/**
	 * 获取节目详情地址
	 * 
	 * @param program
	 * @return
	 */
	public static String getProgramDetailUrl(final String appCode,
			final Program program) {
		return getProgramDetailUrlAddParam(appCode, program, null);
	}

	/**
	 * 获取节目详情地址
	 * 
	 * @param program
	 * @param paramStr
	 * @return
	 */
	public static String getProgramDetailUrlAddParam(final String appCode,
			final Program program, final String paramStr) {
		if (program == null) {
			return "";
		}
		String path = "";
		path = ItemTypeEnum.PROGRAM.getKey() + "/" + program.getId()
				+ "/detail";
		String url = PathUtils.joinUrl(path, StringUtils.trimToEmpty(paramStr));
		return url;
	}

	/**
	 * 获取剧头详情地址
	 * 
	 * @param series
	 * @return
	 */
	public static String getSeriesDetailUrl(final String appCode,
			final Series series) {
		return getSeriesDetailUrlAddParam(appCode, series, null);
	}

	/**
	 * 获取剧头详情地址
	 * 
	 * @param series
	 * @param paramStr
	 * @return
	 */
	public static String getSeriesDetailUrlAddParam(final String appCode,
			final Series series, final String paramStr) {
		if (series == null) {
			return "";
		}
		String path = "";
		path = ItemTypeEnum.SERIES.getKey() + "/" + series.getId() + "/detail";
		String url = PathUtils.joinUrl(path, StringUtils.trimToEmpty(paramStr));
		return url;
	}

	/**
	 * 获取专题详情地址
	 * 
	 * @param album
	 * @return
	 */
	public static String getAlbumDetailUrl(final String appCode,
			final Album album) {
		return getAlbumDetailUrlAddParam(appCode, album, null);
	}

	/**
	 * 获取专题详情地址
	 * 
	 * @param album
	 * @param paramStr
	 * @return
	 */
	public static String getAlbumDetailUrlAddParam(final String appCode,
			final Album album, final String paramStr) {
		if (album == null) {
			return "";
		}
		String path = "";
		path = ItemTypeEnum.ALBUM.getKey() + "/" + album.getId() + "/detail";
		String url = PathUtils.joinUrl(path, StringUtils.trimToEmpty(paramStr));
		return url;
	}

	/**
	 * 获取频道详情地址
	 * 
	 * @param channel
	 * @return
	 */
	public static String getChannelDetailUrl(final String appCode,
			final Channel channel) {
		return getChannelDetailUrlAddParam(appCode, channel, null);
	}

	/**
	 * 获取频道详情地址
	 * 
	 * @param channel
	 * @param paramStr
	 * @return
	 */
	public static String getChannelDetailUrlAddParam(final String appCode,
			final Channel channel, final String paramStr) {
		if (channel == null) {
			return "";
		}
		String path = "";
		path = ItemTypeEnum.CHANNEL.getKey() + "/" + channel.getId() + "/detail";
		String url = PathUtils.joinUrl(path, StringUtils.trimToEmpty(paramStr));
		return url;
	}

}
