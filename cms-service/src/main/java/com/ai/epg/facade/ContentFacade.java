package com.ai.epg.facade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.ai.AppGlobal;
import com.ai.cms.media.bean.SearchResult;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.Series;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.cms.media.repository.SeriesRepository;
import com.ai.cms.star.entity.Club;
import com.ai.cms.star.entity.Star;
import com.ai.cms.star.repository.ClubRepository;
import com.ai.cms.star.repository.StarRepository;
import com.ai.common.bean.PageInfo;
import com.ai.common.enums.ChargeTypeEnum;
import com.ai.common.enums.ContentTypeEnum;
import com.ai.common.enums.ItemTypeEnum;
import com.ai.common.jpa.PropertyFilter;
import com.ai.common.jpa.SpecificationUtils;
import com.ai.common.utils.PathUtils;
import com.ai.epg.category.entity.CategoryItemView;
import com.ai.epg.product.entity.Charge;
import com.ai.epg.product.repository.ChargeRepository;
import com.ai.epg.subscriber.entity.UserFavorite;
import com.ai.epg.subscriber.entity.UserFavoriteView;
import com.ai.epg.subscriber.entity.UserPlayList;
import com.ai.epg.subscriber.repository.UserFavoriteRepository;
import com.ai.epg.subscriber.repository.UserFavoriteViewRepository;
import com.ai.epg.subscriber.repository.UserPlayListRepository;
import com.ai.epg.widget.entity.WidgetItemView;

/**
 * 内容相关的接口
 *
 */
@Component
public class ContentFacade {

	private static SeriesRepository seriesRepository;

	private static ProgramRepository programRepository;

	private static StarRepository starRepository;

	private static ClubRepository clubRepository;

	private static ChargeRepository chargeRepository;

	private static UserFavoriteRepository userFavoriteRepository;

	private static UserFavoriteViewRepository userFavoriteViewRepository;

	private static UserPlayListRepository userPlayListRepository;
	
	@Autowired
	public void setSeriesRepository(SeriesRepository seriesRepository) {
		ContentFacade.seriesRepository = seriesRepository;
	}

	@Autowired
	public void setProgramRepository(ProgramRepository programRepository) {
		ContentFacade.programRepository = programRepository;
	}

	@Autowired
	public void setStarRepository(StarRepository starRepository) {
		ContentFacade.starRepository = starRepository;
	}

	@Autowired
	public void setClubRepository(ClubRepository clubRepository) {
		ContentFacade.clubRepository = clubRepository;
	}

	@Autowired
	public void setChargeRepository(ChargeRepository chargeRepository) {
		ContentFacade.chargeRepository = chargeRepository;
	}

	@Autowired
	public void setUserFavoriteRepository(
			UserFavoriteRepository userFavoriteRepository) {
		ContentFacade.userFavoriteRepository = userFavoriteRepository;
	}

	@Autowired
	public void setUserFavoriteViewRepository(
			UserFavoriteViewRepository userFavoriteViewRepository) {
		ContentFacade.userFavoriteViewRepository = userFavoriteViewRepository;
	}

	@Autowired
	public void setUserPlayListRepository(
			UserPlayListRepository userPlayListRepository) {
		ContentFacade.userPlayListRepository = userPlayListRepository;
	}

	/**
	 * 根据节目id获取节目对象
	 * 
	 * @param programId
	 * @return
	 */
	public static Program getProgram(final String appCode, final Long programId) {
		
		return programRepository.findOne(programId);
	}

	/**
	 * 根据club id 获取俱乐部对象
	 * 
	 * @param programId
	 * @return
	 */
	public static Club getClubbyId(final String appCode, final Long clubId) {
		
		if (clubId == null) {
			return new Club();
		}

		return clubRepository.findOne(clubId);
	}

	/**
	 * 根据明星id获取明星对象
	 * 
	 * @param starId
	 * @return
	 */
	public static Star getStar(final String appCode, final Long starId) {
		

		return starRepository.findOne(starId);
	}

	/**
	 * 根据剧头id获取节目列表
	 * 
	 * @param seriesId
	 * @return
	 */
	public static List<Program> getProgramListBySeries(final String appCode, final Long seriesId) {
		
		return programRepository.findBySeriesIdAndOnline(seriesId);
	}

	/**
	 * 获取最热剧头的指定数量
	 * 
	 * @param code
	 * @param itemType
	 * @param maxWidth
	 * @return
	 */
	public static List<Series> getSeriesMaxList(final String appCode, final int maxWidth) {
		
		PageInfo pageInfo = new PageInfo(0, maxWidth);
		Page<Series> page = seriesRepository.findRecommBySeries(pageInfo.getPageRequest());
		if (page == null) {
			return new ArrayList<Series>();
		}
		return page.getContent();
	}

	/**
	 * 根据剧头id获取节目列表并按照时间排序
	 * 
	 * @param seriesId
	 * @return
	 */
	public static List<Program> getSeriesByTime(final String appCode, final Long seriesId) {
		
		return programRepository.findSeriesByTime(seriesId);
	}

	public static Page<Program> getProgramPageBySeries(final String appCode, final Long seriesId, int pageNum,
			int maxWidth) {
		
		PageInfo pageInfo = new PageInfo(pageNum, maxWidth);
		return programRepository.findBySeriesIdPage(seriesId, pageInfo.getPageRequest());
	}

	/**
	 * @param appCode
	 * @param program
	 * @param tagItems
	 *            用于查询的tag数组
	 * @param queryTagNum
	 *            用于查询的tag数量
	 * @param flag
	 *            查询方式，0：and查询 1：or查询
	 * @param maxWidth
	 *            分页的页面大小
	 * @return
	 */
	public static List<Program> getRelatedPrograms(final String appCode, final Program program, final String[] tagItems,
			final int queryTagNum, final int flag, final int maxWidth) {
		PageInfo pageInfo = new PageInfo(0, maxWidth);
		List<Program> programList = new ArrayList<Program>();
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("id__NQ_L", String.valueOf(program.getId())));
		filters.add(new PropertyFilter("contentType__EQ_I", String.valueOf(program.getContentType())));
		filters.add(new PropertyFilter("status__EQ_I", "1"));

		PropertyFilter tagFilter = new PropertyFilter();
		List<PropertyFilter> filterSubList = new ArrayList<PropertyFilter>();
		if (flag == 1) {// flag:1 or
			filters.add(tagFilter);
			tagFilter.setSubList(filterSubList);
		}

		for (int i = 0; i < tagItems.length; i++) {
			if (i < queryTagNum) {
				if (flag == 1) { // flag:1 or
					filterSubList.add(new PropertyFilter("tag__LIKE_S", String.valueOf(tagItems[i])));
				} else { // flag:0 and
					filters.add(new PropertyFilter("tag__LIKE_S", String.valueOf(tagItems[i])));
				}
			} else {
				break;
			}
		}
		Specification<Program> specification = SpecificationUtils.getSpecification(filters);
		Page<Program> pageContent = programRepository.findAll(specification, pageInfo.getPageRequest());
		programList = pageContent.getContent();
		return programList;
	}

	/**
	 * 获取相关的节目列表
	 * 
	 * @param program
	 * @return
	 */
	public static List<Program> getRelatedProgramList(final String appCode, final Program program, final int maxWidth) {
		
		List<Program> programList = new ArrayList<Program>();
		int maxTagNum = 3;
		if (program != null) {
			String tag = program.getTag();
			if (StringUtils.isNotEmpty(tag)) {
				String[] tagItems = replaceOtherCharsToBlank(appCode, tag.trim()).split(" ");
				List<Program> tempProgramList = new ArrayList<Program>();
				tempProgramList = getRelatedPrograms(appCode, program, tagItems, maxTagNum, 0, maxWidth);
				programList.addAll(tempProgramList);

				if (programList.size() < maxWidth && tagItems.length >= maxTagNum) { // 大于允许处理的关键字个数时，对关键字内进行AND的组合查询
					int tempNum = maxTagNum - 1;
					while (tempNum > 0) {
						tempProgramList = getRelatedPrograms(appCode, program, tagItems, tempNum, 0, maxWidth);
						programList.addAll(tempProgramList);
						Map<Long, Program> map = new HashMap<Long, Program>();
						for (Program p : programList) {
							map.put(p.getId(), p);
						}
						programList.clear();
						programList.addAll(map.values());

						if (programList.size() >= maxWidth) {
							programList = programList.subList(0, maxWidth);
							break;
						}
						tempNum--;
					}
				}
				if (programList.size() < maxWidth && tagItems.length > 1) {// 大于允许处理的关键字个数时，对关键字内进行OR查询
					tempProgramList = getRelatedPrograms(appCode, program, tagItems, tagItems.length, 1, maxWidth);
					programList.addAll(tempProgramList);
					Map<Long, Program> map = new HashMap<Long, Program>();
					for (Program p : programList) {
						map.put(p.getId(), p);
					}
					programList.clear();
					programList.addAll(map.values());
					if (programList.size() > maxWidth) {
						programList = programList.subList(0, maxWidth);
					}
				}
			}
		}
		if (programList.size() < maxWidth && maxWidth - programList.size() > 0) {
			PageInfo pageInfo = new PageInfo(0, maxWidth - programList.size());
			List<Integer> contentTypes = new ArrayList<Integer>();
			contentTypes.add(ContentTypeEnum.MOVIE.getKey());
			Page<Program> programs = programRepository.findByNewestProgram(
					contentTypes, pageInfo.getPageRequest());
			if (programs != null && programs.getContent() != null) {
				programList.addAll(programs.getContent());
			}
		}
		return programList;
	}

	/**
	 * @param appCode
	 * @param series
	 * @param tagItems
	 *            用于查询的tag数组
	 * @param queryTagNum
	 *            用于查询的tag数量
	 * @param flag
	 *            查询方式，0：and查询 1：or查询
	 * @param maxWidth
	 *            分页的页面大小
	 * @return
	 */
	public static List<Series> getRelatedSeries(final String appCode, final Series series, final String[] tagItems,
			final int queryTagNum, final int flag, final int maxWidth) {
		PageInfo pageInfo = new PageInfo(0, maxWidth);
		List<Series> seriesList = new ArrayList<Series>();
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("id__NQ_L", String.valueOf(series.getId())));
		filters.add(new PropertyFilter("contentType__EQ_I", String.valueOf(series.getContentType())));
		filters.add(new PropertyFilter("status__EQ_I", "1"));

		PropertyFilter tagFilter = new PropertyFilter();
		List<PropertyFilter> filterSubList = new ArrayList<PropertyFilter>();
		if (flag == 1) {// flag:1 or
			filters.add(tagFilter);
			tagFilter.setSubList(filterSubList);
		}

		for (int i = 0; i < tagItems.length; i++) {
			if (i < queryTagNum) {
				if (flag == 1) { // flag:1 or
					filterSubList.add(new PropertyFilter("tag__LIKE_S", String.valueOf(tagItems[i])));
				} else { // flag:0 and
					filters.add(new PropertyFilter("tag__LIKE_S", String.valueOf(tagItems[i])));
				}
			} else {
				break;
			}
		}
		Specification<Series> specification = SpecificationUtils.getSpecification(filters);
		Page<Series> pageContent = seriesRepository.findAll(specification, pageInfo.getPageRequest());
		seriesList = pageContent.getContent();
		return seriesList;
	}

	/**
	 * 获取相关的剧头列表
	 * 
	 * @param appCode
	 * @param series
	 * @param maxWidth
	 * @return
	 */
	public static List<Series> getRelatedSeriesList(final String appCode, final Series series, final int maxWidth) {
		
		List<Series> seriesList = new ArrayList<Series>();
		int maxTagNum = 3;
		if (series != null) {
			String tag = series.getTag();
			if (StringUtils.isNotEmpty(tag)) {
				String[] tagItems = replaceOtherCharsToBlank(appCode, tag.trim()).split(" ");
				List<Series> tempSeriesList = new ArrayList<Series>();
				tempSeriesList = getRelatedSeries(appCode, series, tagItems, maxTagNum, 0, maxWidth);
				seriesList.addAll(tempSeriesList);

				if (seriesList.size() < maxWidth && tagItems.length >= maxTagNum) { // 大于允许处理的关键字个数时，对关键字内进行AND的组合查询
					int tempNum = maxTagNum - 1;
					while (tempNum > 0) {
						tempSeriesList = getRelatedSeries(appCode, series, tagItems, tempNum, 0, maxWidth);
						seriesList.addAll(tempSeriesList);
						Map<Long, Series> map = new HashMap<Long, Series>();
						for (Series s : seriesList) {
							map.put(s.getId(), s);
						}
						seriesList.clear();
						seriesList.addAll(map.values());

						if (seriesList.size() >= maxWidth) {
							break;
						}
						tempNum--;
					}
				}
				if (seriesList.size() < maxWidth && tagItems.length > 1) {// 大于允许处理的关键字个数时，对关键字内进行OR查询
					tempSeriesList = getRelatedSeries(appCode, series, tagItems, tagItems.length, 1, maxWidth);
					seriesList.addAll(tempSeriesList);
					Map<Long, Series> map = new HashMap<Long, Series>();
					for (Series s : seriesList) {
						map.put(s.getId(), s);
					}
					seriesList.clear();
					seriesList.addAll(map.values());
					if (seriesList.size() > maxWidth) {
						seriesList = seriesList.subList(0, maxWidth);
					}
				}
			}
		}
		if (seriesList.size() < maxWidth && maxWidth - seriesList.size() > 0) {
			PageInfo pageInfo = new PageInfo(0, maxWidth - seriesList.size());
			List<Integer> contentTypes = new ArrayList<Integer>();
			contentTypes.add(ContentTypeEnum.MOVIE.getKey());
			contentTypes.add(ContentTypeEnum.TV.getKey());
			contentTypes.add(ContentTypeEnum.CHILDREN.getKey());
			contentTypes.add(ContentTypeEnum.EDUCATION.getKey());
			contentTypes.add(ContentTypeEnum.MUSIC.getKey());
			Page<Series> seriess = seriesRepository.findByNewestSeries(
					contentTypes, pageInfo.getPageRequest());
			if (seriess != null && seriess.getContent() != null) {
				seriesList.addAll(seriess.getContent());
			}
		}
		return seriesList;
	}

	/**
	 * 获取明星的相关作品
	 * 
	 * @param String
	 *            start
	 * @return
	 */
	public static List<Program> getRelateStarProgramList(final String appCode, final Star star) {
		

		PageInfo pageInfo = new PageInfo(0, 500);
		Page<Program> page = programRepository.findStarProgramByNameList("%" + star.getName() + "%",
				pageInfo.getPageRequest());
		return page.getContent();
	}

	/**
	 * 获取明星的相关作品并按年份分组
	 * 
	 * @param String
	 *            start
	 * @return
	 */
	public static List<Program> getRelateStarProgramListByYear(final String appCode, final String year,
			final Star star) {
		

		PageInfo pageInfo = new PageInfo(0, 500);
		Page<Program> page = programRepository.findStarProgramByYearList(year, "%" + star.getName() + "%",
				pageInfo.getPageRequest());
		return page.getContent();
	}

	/**
	 * 根据club id 获取俱乐部对象
	 * 
	 * @param programId
	 * @return
	 */
	public static Page<UserFavoriteView> getRelateUserFavoriteViewList(final String appCode, final Long userId,
			int pageNum, int maxWidth) {
		
		PageInfo pageInfo = new PageInfo(pageNum, maxWidth);
		Page<UserFavoriteView> uFavorite = userFavoriteViewRepository.findPageListByUserId(userId, appCode,
				pageInfo.getPageRequest());
		return uFavorite;
	}

	public static Page<UserPlayList> getRelateUserPlayList(final String appCode, final Long userId, int pageNum,
			int maxWidth) {
		
		PageInfo pageInfo = new PageInfo(pageNum, maxWidth);
		Page<UserPlayList> userPlaylist = userPlayListRepository.findPageListByUserId(userId, appCode,
				ItemTypeEnum.CHANNEL.getKey(), pageInfo.getPageRequest());
		return userPlaylist;
	}

	public static List<UserPlayList> getUserPlayList(final String appCode, List<UserPlayList> userPlaylist) {
		List<UserPlayList> resultList = new ArrayList<UserPlayList>();
		List<Long> seriesList = new ArrayList<Long>();
		for (int i = 0; i < userPlaylist.size(); i++) {
			Long seriesId = userPlaylist.get(i).getSeriesId();
			if (seriesId != null) {
				if (!seriesList.contains(seriesId)) {
					seriesList.add(seriesId);
					resultList.add(userPlaylist.get(i));
				}
			} else {
				resultList.add(userPlaylist.get(i));
			}
		}
		return resultList;
	}

	// 获取上次看到的电视剧某一集
	public static Page<UserPlayList> getLastPlayTv(final String appCode, int itemType, int pageNum, int maxWidth) {
		
		PageInfo pageInfo = new PageInfo(pageNum, maxWidth);
		Page<UserPlayList> userPlaylist = userPlayListRepository.findLastPlayTv(appCode, itemType,
				pageInfo.getPageRequest());
		return userPlaylist;
	}

	public static String getUserPlayListImage1(final String appCode, final UserPlayList userPlayList,
			final String defaultStr) {
		if (userPlayList == null) {
			return "";
		}
		Program Program = programRepository.findOne(userPlayList.getItemId());
		return AppGlobal.getImagePath(StringUtils.defaultString(Program.getImage1(), defaultStr));
	}

	/**
	 * 根据club id 获取俱乐部对象
	 * 
	 * @param programId
	 * @return
	 */
	public static Page<UserFavorite> getRelateUserFavoriteList(final String appCode, final Long userId, int pageNum,
			int maxWidth) {
		
		PageInfo pageInfo = new PageInfo(pageNum, maxWidth);
		Page<UserFavorite> uFavorite = userFavoriteRepository.findPageListByUserId(userId, appCode,
				pageInfo.getPageRequest());
		return uFavorite;
	}

	/**
	 * 获取节目类型标签列表
	 * 
	 * @param program
	 * @return
	 */
	public static List<String> getProgramTagList(final String appCode, final Program program) {
		
		List<String> list = new ArrayList<String>();
		String tag = program.getTag();
		if (StringUtils.isNotBlank(tag)) {
			String regex = ",|，|\\s+";
			String tagArr[] = tag.split(regex);
			for (int i = 0; i < tagArr.length; i++) {
				list.add(i, tagArr[i]);
			}
		}

		return list;
	}

	/**
	 * 获取节目语言标签列表
	 * 
	 * @param program
	 * @param maxWidth
	 * @return
	 */
	public static List<String> getProgramLanguageList(final String appCode, final Program program) {
		
		List<String> list = new ArrayList<String>();
		String languages = program.getLanguage();
		if (StringUtils.isNotBlank(languages)) {
			String regex = ",|，|\\s+";
			String languagesArr[] = languages.split(regex);
			for (int i = 0; i < languagesArr.length; i++) {
				list.add(i, languagesArr[i]);
			}
		}
		return list;
	}

	/**
	 * 获取节目演员标签列表
	 * 
	 * @param program
	 * @param maxWidth
	 * @return
	 */
	public static List<String> getProgramActorList(final String appCode, final Program program) {
		
		List<String> list = new ArrayList<String>();
		String actor = program.getActor();
		if (StringUtils.isNotEmpty(actor)) {
			String regex = ",|，|\\s+";
			String languagesArr[] = actor.split(regex);
			for (int i = 0; i < languagesArr.length; i++) {
				list.add(i, languagesArr[i]);
			}
		}
		return list;
	}

	/**
	 * 根据剧头id获取剧头对象
	 * 
	 * @param seriesId
	 * @return
	 */
	public static Series getSeries(final String appCode, final Long seriesId) {
		

		return seriesRepository.findOne(seriesId);
	}

	/**
	 * 获取节目显示名称
	 * 
	 * @param program
	 * @param maxWidth
	 * @return
	 */
	public static String getProgramTitle(final String appCode, final Program program, final int maxWidth) {
		if (program == null) {
			return "";
		}
		String title = "";
		if (StringUtils.isNotEmpty(program.getTitle())) {
			title = program.getTitle();
		}
		return AppGlobal.abbreviate(StringUtils.defaultString(title, ""), maxWidth);
	}

	/**
	 * 获取明星横海报
	 * 
	 * @param star
	 * @param defaultStr
	 * @return
	 */
	public static String getStarImage1(final String appCode, final Star star, final String defaultStr) {
		if (star == null) {
			return "";
		}
		return AppGlobal.getImagePath(StringUtils.defaultString(star.getImage1(), defaultStr));
	}

	/**
	 * 获取明星竖海报
	 * 
	 * @param star
	 * @param defaultStr
	 * @return
	 */
	public static String getStarImage2(final String appCode, final Star star, final String defaultStr) {
		if (star == null) {
			return "";
		}
		return AppGlobal.getImagePath(StringUtils.defaultString(star.getImage2(), defaultStr));
	}

	/**
	 * 获取节目横海报
	 * 
	 * @param program
	 * @param defaultStr
	 * @return
	 */
	public static String getProgramImage1(final String appCode, final Program program, final String defaultStr) {
		if (program == null) {
			return "";
		}
		if (program.getImage1() == null || program.getImage1().equals("") || program.getImage1() == "null"
				|| program.getImage1().equals(" ")) {
			return defaultStr;
		}
		return AppGlobal.getImagePath(StringUtils.defaultString(program.getImage1(), defaultStr));
	}

	/**
	 * 获取节目竖海报
	 * 
	 * @param program
	 * @param defaultStr
	 * @return
	 */
	public static String getProgramImage2(final String appCode, final Program program, final String defaultStr) {
		if (program == null) {
			return "";
		}
		return AppGlobal.getImagePath(StringUtils.defaultString(program.getImage2(), defaultStr));
	}

	/**
	 * 获取剧头显示名称
	 * 
	 * @param series
	 * @param maxWidth
	 * @return
	 */
	public static String getSeriesTitle(final String appCode, final Series series, final int maxWidth) {
		if (series == null) {
			return "";
		}
		String title = "";
		if (StringUtils.isNotEmpty(series.getTitle())) {
			title = series.getTitle();
		}
		return AppGlobal.abbreviate(StringUtils.defaultString(title, ""), maxWidth);
	}

	/**
	 * 获取剧头演员标签列表
	 * 
	 * @param Series
	 * @param maxWidth
	 * @return
	 */
	public static List<String> getSeriesActorList(final String appCode, final Series series) {
		
		List<String> list = new ArrayList<String>();
		String actor = series.getActor();
		if (StringUtils.isNotBlank(actor)) {
			String regex = ",|，|\\s+";
			String actorArr[] = actor.split(regex);
			for (int i = 0; i < actorArr.length; i++) {
				list.add(i, actorArr[i]);
			}
		}
		return list;
	}

	public static List<Program> getProgramListInWidgetview(final String appCode, List<WidgetItemView> widvewList) {
		
		List<Program> list = new ArrayList<Program>();
		List<Long> idlist = new ArrayList<Long>();
		if (widvewList != null && widvewList.size() > 0) {
			for (int i = 0; i < widvewList.size(); i++) {
				idlist.add(widvewList.get(i).getItemId());
			}
			list = programRepository.findByIdIn(idlist);

		}
		return list;
	}

	/**
	 * 通过栏目项id查询所有的节目
	 */

	public static List<Program> getProgramListInCategoryview(final String appCode,
			List<CategoryItemView> categoryItemViewList) {
		
		List<Program> list = new ArrayList<Program>();
		List<Long> idlist = new ArrayList<Long>();
		if (categoryItemViewList != null && categoryItemViewList.size() > 0) {
			for (int i = 0; i < categoryItemViewList.size(); i++) {
				idlist.add(categoryItemViewList.get(i).getItemId());
			}
			list = programRepository.findByIdIn(idlist);
		}
		return list;
	}

	/**
	 * 获取剧头语言标签列表
	 * 
	 * @param Series
	 * @param maxWidth
	 * @return
	 */
	public static List<String> getSeriesLanguageList(final String appCode, final Series series) {
		
		List<String> list = new ArrayList<String>();
		String languages = series.getLanguage();
		if (StringUtils.isNotBlank(languages)) {
			String regex = ",|，|\\s+";
			String languagesArr[] = languages.split(regex);
			for (int i = 0; i < languagesArr.length; i++) {
				list.add(i, languagesArr[i]);
			}
		}
		return list;
	}

	/**
	 * 获取剧头导演标签列表
	 * 
	 * @param Series
	 * @param maxWidth
	 * @return
	 */
	public static List<String> getSeriesDirectorList(final String appCode, final Series series) {
		
		List<String> list = new ArrayList<String>();
		String Director = series.getDirector();
		if (StringUtils.isNotBlank(Director)) {
			String regex = ",|，|\\s+";
			String DirectorArr[] = Director.split(regex);
			for (int i = 0; i < DirectorArr.length; i++) {
				list.add(i, DirectorArr[i]);
			}
		}
		return list;
	}

	public static List<String> getSplitStringList(final String appCode, String strs) {
		
		List<String> list = new ArrayList<String>();
		if (StringUtils.isNotBlank(strs)) {
			String regex = ",|，|\\s+";
			String StringArr[] = strs.split(regex);
			for (int i = 0; i < StringArr.length; i++) {
				list.add(i, StringArr[i]);
			}
		}
		return list;
	}

	/**
	 * 获取剧头横海报
	 * 
	 * @param series
	 * @param defaultStr
	 * @return
	 */
	public static String getSeriesImage1(final String appCode, final Series series, final String defaultStr) {
		if (series == null) {
			return "";
		}
		return AppGlobal.getImagePath(StringUtils.defaultString(series.getImage1(), defaultStr));
	}

	/**
	 * 获取剧头竖海报
	 * 
	 * @param series
	 * @param defaultStr
	 * @return
	 */
	public static String getSeriesImage2(final String appCode, final Series series, final String defaultStr) {
		if (series == null) {
			return "";
		}
		return AppGlobal.getImagePath(StringUtils.defaultString(series.getImage2(), defaultStr));

	}

	public static List<Program> getProgramLastNextProgram(final String appCode, Program program) {
		
		List<Program> programList = new ArrayList<Program>();
		Series series = seriesRepository.findOne(program.getSeriesId());
		Long seriesId = series.getId();
		PageInfo pageInfo = new PageInfo(0, 10);
		int episodeIndex = program.getEpisodeIndex();
		int lastProgram = 0;
		int nextProgram = 1;
		if (episodeIndex == 1) {
			lastProgram = series.getEpisodeTotal();
		} else if (episodeIndex > 1) {
			lastProgram = episodeIndex - 1;
		}
		if (episodeIndex < series.getEpisodeTotal()) {
			nextProgram = episodeIndex + 1;
		}

		Page<Program> page = programRepository.getLastNextProgram(lastProgram, nextProgram, seriesId,
				pageInfo.getPageRequest());
		for (Program pro : page.getContent()) {
			programList.add(pro);
		}
		if (episodeIndex == 1) {
			Collections.reverse(programList);
		}
		return programList;

	}

	public static String getUserFavoriteImage1(final String appCode, final UserFavorite userFavorite,
			final String defaultStr) {
		if (userFavorite == null) {
			return "";
		}
		Program Program = programRepository.findOne(userFavorite.getItemId());
		Series series = seriesRepository.findOne(userFavorite.getItemId());
		if (null != series) {
			return AppGlobal.getImagePath(StringUtils.defaultString(series.getImage1(), defaultStr));
		} else {
			return AppGlobal.getImagePath(StringUtils.defaultString(Program.getImage1(), defaultStr));
		}
	}

	public static Map<Long, Program> getByWidgetItemViewListProgram(final String appCode,
			List<WidgetItemView> widgetItemViewList) {
		

		Map<Long, Program> result = new HashMap<Long, Program>();
		List<Long> itemIdList = new ArrayList<Long>();
		if (widgetItemViewList != null && widgetItemViewList.size() > 0) {
			for (int i = 0; i < widgetItemViewList.size(); i++) {
				if (widgetItemViewList.get(i).getItemId() != null && widgetItemViewList.get(i).getItemType() == 1) {
					itemIdList.add(widgetItemViewList.get(i).getItemId());
				}

			}
		}
		if (itemIdList.size() > 0) {
			List<Program> programList = programRepository.findByIdIn(itemIdList);
			for (Program program : programList) {
				result.put(program.getId(), program);
			}
		}
		return result;
	}

	/**
	 * 根据查询条件获取影视列表
	 * 
	 * 
	 * @param
	 * @param searchType
	 * @return searchText
	 */
	public static Page<Program> getMoviePageListByTypeAndText(final String appCode, final int searchType,
			final String searchText, int pageNum, int maxWidth) {
		
		PageInfo pageInfo = new PageInfo(pageNum, maxWidth);
		if (searchType == 0) {
			return programRepository.findBySearchNameAndContentType("%" + searchText + "%", pageInfo.getPageRequest());
		} else if (searchType == 1) {
			return programRepository.findByActorPinyinAndContentType("%" + searchText + "%",
					pageInfo.getPageRequest());
		}
		return null;
	}

	/**
	 * 根据keyword获取默认影视列表
	 * 
	 * 
	 * @param
	 * @param searchType
	 * @return searchText
	 */
	public static Page<Program> getMoviePageListByKeyword(final String appCode, String keyword, int pageNum,
			int maxWidth) {
		
		PageInfo pageInfo = new PageInfo(pageNum, maxWidth);
		if (keyword != null && keyword.trim() != "") {
			return programRepository.findByKeyword("%" + keyword.trim() + "%", pageInfo.getPageRequest());
		}
		return null;
	}

	/**
	 * 获取推荐位项显示看点
	 * 
	 * @param widgetItemView
	 * @param maxWidth
	 * @return
	 */
	public static String getWidgetItemViewport(final String appCode, final WidgetItemView widgetItemView,
			final int maxWidth) {
		
		if (widgetItemView == null) {
			return "";
		}
		Program program = programRepository.findOne(widgetItemView.getItemId());
		return AppGlobal.abbreviate(StringUtils.defaultString(program.getViewpoint(), ""), maxWidth);
	}

	/**
	 * 替换间隔符为空格
	 * 
	 * @param origin
	 * @return
	 */
	public static String replaceOtherCharsToBlank(final String appCode, String origin) {
		String result = "";
		if (StringUtils.isNotEmpty(origin)) {
			origin = origin.trim();
			origin = origin.replace("，", " ");
			origin = origin.replace(",", " ");
			origin = origin.replace("；", " ");
			origin = origin.replace(";", " ");
			origin = origin.replace("。", " ");
			origin = origin.replace(".", " ");
			result = origin;
		}
		return result;
	}

	/**
	 * 替换间隔符为斜杠
	 * 
	 * @param origin
	 * @return
	 */
	public static String replaceOtherCharsToSlash(final String appCode, String origin) {
		String result = "";
		if (StringUtils.isNotEmpty(origin)) {
			origin = origin.trim();
			origin = origin.replace("，", " / ");
			origin = origin.replace(",", " / ");
			origin = origin.replace("；", " / ");
			origin = origin.replace(";", " / ");
			origin = origin.replace("。", " / ");
			origin = origin.replace(".", " / ");
			result = origin;
		}
		return result;
	}

	// 陕西EPG获取筛选后的节目信息
	public static Page<Program> getScreenMoviePageListByType(final String appCode, String type_one, String type_two,
			String type_three, String type_four, int pageNum, int maxWidth) {
		
		PageInfo pageInfo = new PageInfo(pageNum, maxWidth);
		Page<Program> page = null;
		if (type_two.equals("全部")) {
			type_two = "";
		}
		if (type_three.equals("全部")) {
			type_three = "";
		}
		if (type_four.equals("全部")) {
			type_four = "";
		}
		if (type_four.equals("今年")) {
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			type_four = String.valueOf(year);
		}
		if (type_one.equals("最热") || type_one.equals("")) {
			page = programRepository.findByHeat("%" + type_two + "%", "%" + type_three + "%", "%" + type_four + "%",
					pageInfo.getPageRequest());
		} else if (type_one.equals("最新")) {
			page = programRepository.findByDate("%" + type_two + "%", "%" + type_three + "%", "%" + type_four + "%",
					pageInfo.getPageRequest());
		} else if (type_one.equals("评分")) {
			page = programRepository.findByScore("%" + type_two + "%", "%" + type_three + "%", "%" + type_four + "%",
					pageInfo.getPageRequest());
		}

		return page;
	}

	// 陕西EPG获取筛选后的节目信息
	public static Page<Series> getSeriesPageListByType(final String appCode, String type_one, String type_two,
			String type_three, String type_four, int pageNum, int maxWidth) {
		
		PageInfo pageInfo = new PageInfo(pageNum, maxWidth);
		Page<Series> page = null;
		if (type_two.equals("全部")) {
			type_two = "";
		}
		if (type_three.equals("全部")) {
			type_three = "";
		}
		if (type_four.equals("全部")) {
			type_four = "";
		}
		if (type_four.equals("今年")) {
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			type_four = String.valueOf(year);
		}
		if (type_one.equals("最热") || type_one.equals("")) {
			page = seriesRepository.findByHeat("%" + type_two + "%", "%" + type_three + "%", "%" + type_four + "%",
					pageInfo.getPageRequest());
		} else if (type_one.equals("最新")) {
			page = seriesRepository.findByDate("%" + type_two + "%", "%" + type_three + "%", "%" + type_four + "%",
					pageInfo.getPageRequest());
		} else if (type_one.equals("评分")) {
			page = seriesRepository.findByScore("%" + type_two + "%", "%" + type_three + "%", "%" + type_four + "%",
					pageInfo.getPageRequest());
		}

		return page;
	}

	/**
	 * 根据搜索字符串搜索剧头和节目
	 * 
	 * @param appCode
	 * @param searchText
	 * @param pageNUm
	 * @param maxWidth
	 * @return
	 */
	public static List<SearchResult> getSearchResultPageListByText(final String appCode, final String searchText,
			final int contentType, final int maxWidth) {
		
		int initSize = 100;
		PageInfo pageInfo = new PageInfo(0, initSize);
		List<SearchResult> searchResultList = new ArrayList<SearchResult>();
		List<Program> programList = new ArrayList<Program>();
		List<Series> seriesList = new ArrayList<Series>();

		List<Program> tempProgramList = programRepository
				.findBySearchName("%" + searchText + "%", pageInfo.getPageRequest()).getContent();
		List<Series> tempSeriesList = seriesRepository
				.findBySearchName("%" + searchText + "%", pageInfo.getPageRequest()).getContent();

		if (contentType != 0) {
			for (int i = 0; i < tempProgramList.size(); i++) {
				if (tempProgramList.get(i).getContentType() == contentType) {
					programList.add(tempProgramList.get(i));
				}
			}

			for (int i = 0; i < tempSeriesList.size(); i++) {
				if (tempSeriesList.get(i).getContentType() == contentType) {
					seriesList.add(tempSeriesList.get(i));
				}
			}
		} else {
			programList.addAll(tempProgramList);
			seriesList.addAll(tempSeriesList);
		}

		// 单集节目
		for (int i = 0; i < programList.size(); i++) {
			SearchResult searchResult = new SearchResult();
			searchResult.setId(programList.get(i).getId());
			searchResult.setCreateTime(programList.get(i).getCreateTime());
			searchResult.setName(programList.get(i).getName());
			searchResult.setTitle(programList.get(i).getTitle());
			searchResult.setCode("" + programList.get(i).getId());
			searchResult.setContentType(programList.get(i).getContentType());
			searchResult.setYear(programList.get(i).getYear());
			searchResult.setArea(programList.get(i).getArea());
			searchResult.setLanguage(programList.get(i).getLanguage());
			searchResult.setDuration(programList.get(i).getDuration());
			searchResult.setDirector(programList.get(i).getDirector());
			searchResult.setActor(programList.get(i).getActor());
			searchResult.setGenres(programList.get(i).getTag());
			searchResult.setImage1(getProgramImage1(appCode, programList.get(i), ""));
			searchResult.setImage2(getProgramImage2(appCode, programList.get(i), ""));
			searchResultList.add(searchResult);
		}
		// 剧头
		for (int i = 0; i < seriesList.size(); i++) {
			SearchResult searchResult = new SearchResult();
			searchResult.setId(seriesList.get(i).getId());
			searchResult.setCreateTime(seriesList.get(i).getCreateTime());
			searchResult.setName(seriesList.get(i).getName());
			searchResult.setTitle(seriesList.get(i).getTitle());
			searchResult.setCode("" + seriesList.get(i).getId());
			searchResult.setContentType(seriesList.get(i).getContentType());
			searchResult.setYear(seriesList.get(i).getYear());
			searchResult.setArea(seriesList.get(i).getArea());
			searchResult.setLanguage(seriesList.get(i).getLanguage());
			searchResult.setDuration(seriesList.get(i).getDuration());
			searchResult.setDirector(seriesList.get(i).getDirector());
			searchResult.setActor(seriesList.get(i).getActor());
			searchResult.setGenres(seriesList.get(i).getTag());
			searchResult.setImage1(getSeriesImage1(appCode, seriesList.get(i), ""));
			searchResult.setImage2(getSeriesImage2(appCode, seriesList.get(i), ""));
			searchResultList.add(searchResult);
		}
		Collections.sort(searchResultList, new Comparator<SearchResult>() {
			// 重写排序规则
			@Override
			public int compare(SearchResult o1, SearchResult o2) {
				return -o1.getCreateTime().compareTo(o2.getCreateTime());
			}
		});
		if (searchResultList.size() > maxWidth) {
			return searchResultList.subList(0, maxWidth);
		} else
			return searchResultList;
	}

	/**
	 * 根据收费类型ID获取收费类型描述
	 * 
	 * @param type
	 * @return
	 */
	public static String getChargeTypeDesc(final String appCode, final int type) {
		return ChargeTypeEnum.getEnumByKey(type).getValue();
	}

	/**
	 * 根据产品ID获取计费点
	 * 
	 * @param appCode
	 * @param partnerProductCode
	 * @return
	 */
	public static Charge getChargeByProductId(final String appCode, final String partnerProductCode) {
		List<Charge> chargeList = chargeRepository
				.findByPartnerProductCode(partnerProductCode);
		if (chargeList != null && chargeList.size() > 0) {
			return chargeList.get(0);
		}
		return null;
	}

	/**
	 * 根据内容类型获取内容类型描述
	 * 
	 * @param appCode
	 * @param type
	 * @return
	 */
	public static String getContentTypeDescription(final String appCode, final int type) {
		return ContentTypeEnum.getEnumByKey(type).getValue();
	}

	public static String isChargedProgram(final String appCode, final Long id) {
		Program program = getProgram(appCode, id);
		if (program != null) {
			String feeKeyword = ContentFacade.getFeeKeyword(appCode,
					program.getKeyword());
			if (StringUtils.isNotEmpty(feeKeyword)) {
				return "1";
			}
		}
		return "0";
	}
	
	public static String reviateWrapSpacesd(String source, int maxWidth) {
		if (source == null) {
			return "";
		}
		String result = abbreviate(source, maxWidth).replace("\r\n", "<br/>");
		return result;
	}
	
	/**
	 * 缩进字符串
	 * 
	 * @param str
	 * @param maxWidth
	 * @return
	 */
	public static String abbreviate(String str, int maxWidth) {
		if (str == null) {
			return "";
		}
		if (str.length() <= maxWidth) {
			return str;
		}
		final String abrevMarker = "...";
		return str.substring(0, maxWidth - 1) + abrevMarker;
	}
	
	/**
	 * 获取节目播放地址
	 * 
	 * @param appCode
	 * @param program
	 * @param paramStr
	 * @return
	 */
	public static String getProgramPlayUrlAddParam(final String appCode,
			final Program program, final String paramStr) {
		if (program == null) {
			return "";
		}
		String url = PathUtils.joinUrl(ItemTypeEnum.PROGRAM.getKey() + "/"
				+ program.getId() + "/play",
				StringUtils.trimToEmpty(paramStr));
		url = addProgramExtParam(appCode, program, url);
		return url;
	}

	public static String addProgramExtParam(final String appCode,
			final Program program, final String url) {
		String cpCode = AppGlobal.cpIdToCodeMap.get("" + program.getCpId());
		return PathUtils.joinUrl(url,
				"cpCode=" + StringUtils.trimToEmpty(cpCode));
	}
	
	/**
	 * 获取剧头播放地址
	 * 
	 * @param appCode
	 * @param series
	 * @param paramStr
	 * @return
	 */
	public static String getSeriesPlayUrlAddParam(final String appCode,
			final Series series, final String paramStr) {
		if (series == null) {
			return "";
		}
		String url = PathUtils.joinUrl(ItemTypeEnum.SERIES.getKey() + "/"
				+ series.getId() + "/play",
				StringUtils.trimToEmpty(paramStr));
		url = addSeriesExtParam(appCode, series, url);
		return url;
	}

	public static String addSeriesExtParam(final String appCode,
			final Series series, final String url) {
		String cpCode = AppGlobal.cpIdToCodeMap.get("" + series.getCpId());
		return PathUtils.joinUrl(url,
				"cpCode=" + StringUtils.trimToEmpty(cpCode));
	}
	
	/**
	 * 获取计费点列表
	 * 
	 * @param appCode
	 * @return
	 */
	public static List<Charge> getChargeList(String appCode) {
		List<Charge> result = new ArrayList<Charge>();
		if (StringUtils.isEmpty(appCode)) {
			return result;
		}
		result = AppGlobal.appToChargeListMap.get(appCode);
		return result;
	}
	
	/**
	 * 获取计费点列表
	 * 
	 * @param appCode
	 * @param keyword
	 * @return
	 */
	public static List<Charge> getChargeList(String appCode, String keyword) {
		List<Charge> result = new ArrayList<Charge>();
		if (StringUtils.isEmpty(appCode) || StringUtils.isEmpty(keyword)
				|| keyword.contains(AppGlobal.FEE_FREE)) {
			return result;
		}
		List<Charge> appChargeList = AppGlobal.appToChargeListMap.get(appCode);
		if (appChargeList == null || appChargeList.size() == 0) {
			return result;
		}

		String keywordStr = keyword.replaceAll("，", ",").replaceAll(" ", ",");
		String[] codeArray = keywordStr.split(",");
		List<Integer> index = new ArrayList<Integer>();
		
		for (int i = 0; i < appChargeList.size(); i++) {
			for (int j = 0; j < codeArray.length; j++){
				if (codeArray[j].equals(appChargeList.get(i).getCode())) {
					index.add(i);
				}
			}
		}
		// 对下标排序
		Collections.sort(index);
		// 根据下标的大小对result进行添加操作
		for (Integer _i: index) {
			result.add(appChargeList.get(_i));
		}
	
		return result;
	}
	
	/**
	 * 获取计费关键字
	 * 
	 * @param appCode
	 * @param keyword
	 * @return
	 */
	public static String getFeeKeyword(String appCode, String keyword) {
		List<Charge> chargeList = getChargeList(appCode, keyword);
		StringBuffer result = new StringBuffer("");
		for (Charge charge : chargeList) {
			result.append(charge.getCode()).append(",");
		}
		return result.toString();
	}

}