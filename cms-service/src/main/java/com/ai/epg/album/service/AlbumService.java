package com.ai.epg.album.service;

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
import com.ai.epg.album.entity.Album;
import com.ai.epg.album.entity.AlbumItem;
import com.ai.epg.album.repository.AlbumItemRepository;
import com.ai.epg.album.repository.AlbumRepository;

@Service
@Transactional(readOnly = true)
public class AlbumService extends AbstractService<Album, Long> {

	@Autowired
	private AlbumRepository albumRepository;

	@Autowired
	private AlbumItemRepository albumItemRepository;

	@Autowired
	private ProgramRepository programRepository;
	
	@Autowired
	private SeriesRepository seriesRepository;
	
	@Autowired
	private LeagueMatchRepository leagueMatchRepository;
	
	@Override
	public AbstractRepository<Album, Long> getRepository() {
		return albumRepository;
	}

	@Transactional(readOnly = false)
	public void deleteAlbum(Album album) {
		if (album != null) {
			albumItemRepository.deleteByAlbumId(album.getId());
			albumRepository.delete(album);
		}
	}

	@Transactional(readOnly = false)
	public void batchUpdate(String selectAlbumIds, Integer status,
			Integer itemType, String itemIds, Integer sortIndex) {
		if (StringUtils.isEmpty(selectAlbumIds)) {
			return;
		}
		String[] selectAlbumIdArr = selectAlbumIds.split(",");
		String[] itemIdArr = itemIds.split(",");
		if (null != itemIdArr && itemIdArr.length > 0) {
			List<Long> idList = new ArrayList<Long>();
			for (String itemId : itemIdArr) {
				idList.add(Long.valueOf(itemId));
			}
			if (itemType == ItemTypeEnum.PROGRAM.getKey()) {
				List<Program> programList = programRepository.findAll(idList);

				for (int index = 0; index < selectAlbumIdArr.length; index++) {
					Long selectAlbumId = Long
							.valueOf(selectAlbumIdArr[index]);

					albumItemRepository.updateSortIndexByAlbumId(
							selectAlbumId, itemIdArr.length, sortIndex);

					for (int i = 0; i < programList.size(); i++) {
						Long itemId = programList.get(i).getId();
						AlbumItem albumItem = getAlbumItem(
										selectAlbumId, itemType, itemId);
						if (albumItem == null) {
							albumItem = new AlbumItem();
							albumItem.setAlbumId(selectAlbumId);
							albumItem.setItemType(itemType);
							albumItem.setItemId(itemId);
						}
						albumItem.setStatus(status);
						albumItem
								.setItemStatus(programList.get(i).getStatus());
						albumItem.setSortIndex(i + 1);
						albumItemRepository.save(albumItem);
					}
				}
			} else if (itemType == ItemTypeEnum.SERIES.getKey()) {
				List<Series> seriesList = seriesRepository.findAll(idList);
				for (int index = 0; index < selectAlbumIdArr.length; index++) {
					Long selectAlbumId = Long
							.valueOf(selectAlbumIdArr[index]);

					albumItemRepository.updateSortIndexByAlbumId(
							selectAlbumId, itemIdArr.length, sortIndex);

					for (int i = 0; i < seriesList.size(); i++) {
						Long itemId = seriesList.get(i).getId();
						AlbumItem albumItem = getAlbumItem(
										selectAlbumId, itemType, itemId);
						if (albumItem == null) {
							albumItem = new AlbumItem();
							albumItem.setAlbumId(selectAlbumId);
							albumItem.setItemType(itemType);
							albumItem.setItemId(itemId);
						}
						albumItem.setStatus(status);
						albumItem.setItemStatus(seriesList.get(i).getStatus());
						albumItem.setSortIndex(i + 1);
						albumItemRepository.save(albumItem);
					}
				}
			} else if (itemType == ItemTypeEnum.LEAGUE_MATCH.getKey()) {
				List<LeagueMatch> leagueMatchList = leagueMatchRepository
						.findAll(idList);
				for (int index = 0; index < selectAlbumIdArr.length; index++) {
					Long selectAlbumId = Long
							.valueOf(selectAlbumIdArr[index]);

					albumItemRepository.updateSortIndexByAlbumId(
							selectAlbumId, itemIdArr.length, sortIndex);

					for (int i = 0; i < leagueMatchList.size(); i++) {
						Long itemId = leagueMatchList.get(i).getId();
						AlbumItem albumItem = getAlbumItem(selectAlbumId,
								itemType, itemId);
						if (albumItem == null) {
							albumItem = new AlbumItem();
							albumItem.setAlbumId(selectAlbumId);
							albumItem.setItemType(itemType);
							albumItem.setItemId(itemId);
						}
						albumItem.setStatus(status);
						albumItem.setItemStatus(leagueMatchList.get(i)
								.getStatus());
						albumItem.setSortIndex(i + 1);
						albumItemRepository.save(albumItem);
					}
				}
			}
		}
	}
	
	public AlbumItem getAlbumItem(Long albumId, Integer itemType, Long itemId) {
		List<AlbumItem> albumItemList = albumItemRepository
				.findByAlbumIdAndItemTypeAndItemId(albumId, itemType, itemId);
		if (albumItemList != null && albumItemList.size() > 0) {
			return albumItemList.get(0);
		}
		return null;
	}

	@Transactional(readOnly = false)
	public void batchUpdateMoveByAfter(Long selectAlbumId, Integer step,
			Integer sortIndex) {
		albumItemRepository.updateSortIndexByAlbumId(selectAlbumId, step,
				sortIndex);
	}

	@Transactional(readOnly = false)
	public void saveAlbumItem(AlbumItem albumItem) {
		if (albumItem != null) {
			if (albumItem.getId() == null && albumItem.getAlbumId() != null
					&& albumItem.getSortIndex() != null) {
				batchUpdateMoveByAfter(albumItem.getAlbumId(), 1,
						albumItem.getSortIndex());
			}
			albumItemRepository.save(albumItem);
		}
	}

	/**
	 * 根据专题代码获取专题对象
	 * 
	 * @param appCode
	 * @param code
	 * @return
	 */
	public Album getAlbum(final String appCode, final String code) {
		String targetAppCode = appCode;
		String targetCode = code;
		if (StringUtils.isNotEmpty(code) && code.contains("@")) {
			targetAppCode = StringUtils.upperCase(StringUtils
					.substringAfterLast(code, "@"));
			targetCode = StringUtils.upperCase(StringUtils.substringBefore(
					code, "@"));
		}
		return albumRepository.findOneByAppCodeAndCode(targetAppCode,
				targetCode);
	}
}