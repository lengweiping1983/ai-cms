package com.ai.epg.widget.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.cms.league.entity.LeagueMatch;
import com.ai.cms.league.repository.LeagueMatchRepository;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.Series;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.cms.media.repository.SeriesRepository;
import com.ai.common.enums.ItemTypeEnum;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;
import com.ai.epg.widget.entity.Widget;
import com.ai.epg.widget.entity.WidgetItem;
import com.ai.epg.widget.repository.WidgetItemRepository;
import com.ai.epg.widget.repository.WidgetRepository;

@Service
@Transactional(readOnly = true)
public class WidgetService extends AbstractService<Widget, Long> {

	@Autowired
	private WidgetRepository widgetRepository;

	@Autowired
	private WidgetItemRepository widgetItemRepository;

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private SeriesRepository seriesRepository;

	@Autowired
	private LeagueMatchRepository leagueMatchRepository;

	@Override
	public AbstractRepository<Widget, Long> getRepository() {
		return widgetRepository;
	}

	@Transactional(readOnly = false)
	public void deleteWidget(Widget widget) {
		if (widget != null) {
			widgetItemRepository.deleteByWidgetId(widget.getId());
			widgetRepository.delete(widget);
		}
	}

	@Transactional(readOnly = false)
	public void batchUpdate(String selectWidgetIds, Integer status,
			Integer itemType, String itemIds, Integer sortIndex) {
		if (StringUtils.isEmpty(selectWidgetIds)) {
			return;
		}
		String[] selectWidgetIdArr = selectWidgetIds.split(",");
		String[] itemIdArr = itemIds.split(",");
		if (null != itemIdArr && itemIdArr.length > 0) {
			List<Long> idList = new ArrayList<Long>();
			for (String itemId : itemIdArr) {
				idList.add(Long.valueOf(itemId));
			}
			if (itemType == ItemTypeEnum.PROGRAM.getKey()) {
				List<Program> programList = programRepository.findAll(idList);

				for (int index = 0; index < selectWidgetIdArr.length; index++) {
					Long selectWidgetId = Long
							.valueOf(selectWidgetIdArr[index]);

					widgetItemRepository.updateSortIndexByWidgetId(
							selectWidgetId, itemIdArr.length, sortIndex);

					for (int i = 0; i < programList.size(); i++) {
						Long itemId = programList.get(i).getId();
						WidgetItem widgetItem = getWidgetItem(selectWidgetId,
								itemType, itemId);
						if (widgetItem == null) {
							widgetItem = new WidgetItem();
							widgetItem.setWidgetId(selectWidgetId);
							widgetItem.setItemType(itemType);
							widgetItem.setItemId(itemId);
						}
						widgetItem.setStatus(status);
						widgetItem
								.setItemStatus(programList.get(i).getStatus());
						widgetItem.setSortIndex(i + 1);
						widgetItemRepository.save(widgetItem);
					}
				}
			} else if (itemType == ItemTypeEnum.SERIES.getKey()) {
				List<Series> seriesList = seriesRepository.findAll(idList);
				for (int index = 0; index < selectWidgetIdArr.length; index++) {
					Long selectWidgetId = Long
							.valueOf(selectWidgetIdArr[index]);

					widgetItemRepository.updateSortIndexByWidgetId(
							selectWidgetId, itemIdArr.length, sortIndex);

					for (int i = 0; i < seriesList.size(); i++) {
						Long itemId = seriesList.get(i).getId();
						WidgetItem widgetItem = getWidgetItem(selectWidgetId,
								itemType, itemId);
						if (widgetItem == null) {
							widgetItem = new WidgetItem();
							widgetItem.setWidgetId(selectWidgetId);
							widgetItem.setItemType(itemType);
							widgetItem.setItemId(itemId);
						}
						widgetItem.setStatus(status);
						widgetItem.setItemStatus(seriesList.get(i).getStatus());
						widgetItem.setSortIndex(i + 1);
						widgetItemRepository.save(widgetItem);
					}
				}
			} else if (itemType == ItemTypeEnum.LEAGUE_MATCH.getKey()) {
				List<LeagueMatch> leagueMatchList = leagueMatchRepository
						.findAll(idList);
				for (int index = 0; index < selectWidgetIdArr.length; index++) {
					Long selectWidgetId = Long
							.valueOf(selectWidgetIdArr[index]);

					widgetItemRepository.updateSortIndexByWidgetId(
							selectWidgetId, itemIdArr.length, sortIndex);

					for (int i = 0; i < leagueMatchList.size(); i++) {
						Long itemId = leagueMatchList.get(i).getId();
						WidgetItem widgetItem = getWidgetItem(selectWidgetId,
								itemType, itemId);
						if (widgetItem == null) {
							widgetItem = new WidgetItem();
							widgetItem.setWidgetId(selectWidgetId);
							widgetItem.setItemType(itemType);
							widgetItem.setItemId(itemId);
						}
						widgetItem.setStatus(status);
						widgetItem.setItemStatus(leagueMatchList.get(i)
								.getStatus());
						widgetItem.setSortIndex(i + 1);
						widgetItemRepository.save(widgetItem);
					}
				}
			}
		}
	}

	public WidgetItem getWidgetItem(Long widgetId, Integer itemType, Long itemId) {
		List<WidgetItem> widgetItemList = widgetItemRepository
				.findByWidgetIdAndItemTypeAndItemId(widgetId, itemType, itemId);
		if (widgetItemList != null && widgetItemList.size() > 0) {
			return widgetItemList.get(0);
		}
		return null;
	}

	@Transactional(readOnly = false)
	public void batchUpdateMoveByAfter(Long selectWidgetId, Integer step,
			Integer sortIndex) {
		widgetItemRepository.updateSortIndexByWidgetId(selectWidgetId, step,
				sortIndex);
	}

	@Transactional(readOnly = false)
	public void saveWidgetItem(WidgetItem widgetItem) {
		if (widgetItem != null) {
			if (widgetItem.getId() == null && widgetItem.getWidgetId() != null
					&& widgetItem.getSortIndex() != null) {
				batchUpdateMoveByAfter(widgetItem.getWidgetId(), 1,
						widgetItem.getSortIndex());
			}
			widgetItemRepository.save(widgetItem);
		}
	}

	/**
	 * 根据推荐位代码获取推荐位对象
	 * 
	 * @param appCode
	 * @param code
	 * @return
	 */
	public Widget getWidget(final String appCode, final String code) {
		String targetAppCode = appCode;
		String targetCode = code;
		if (StringUtils.isNotEmpty(code) && code.contains("@")) {
			targetAppCode = StringUtils.upperCase(StringUtils
					.substringAfterLast(code, "@"));
			targetCode = StringUtils.upperCase(StringUtils.substringBefore(
					code, "@"));
		}
		return widgetRepository.findOneByAppCodeAndCode(targetAppCode,
				targetCode);
	}
}