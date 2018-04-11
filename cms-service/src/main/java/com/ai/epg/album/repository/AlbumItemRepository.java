package com.ai.epg.album.repository;

import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.album.entity.AlbumItem;

@Repository
@CacheConfig(cacheNames = "AlbumItemRepository")
public interface AlbumItemRepository extends AbstractRepository<AlbumItem, Long> {

	@Cacheable
	AlbumItem findOne(Long id);
	
	AlbumItem findOneByAlbumIdAndPositionId(Long albumId, String positionId);
	
	@Cacheable
    List<AlbumItem> findByAlbumIdAndItemTypeAndItemId(Long albumId, Integer itemType, Long itemId);

	@Modifying
	@Query(" delete from AlbumItem t where t.albumId = :albumId")
	void deleteByAlbumId(@Param("albumId") Long albumId);

	@Modifying
	@Query(" update AlbumItem t set t.sortIndex = (t.sortIndex + :step) where t.albumId = :albumId and t.sortIndex > 0 and t.sortIndex >= :first ")
	void updateSortIndexByAlbumId(@Param("albumId") Long albumId,
			@Param("step") Integer step, @Param("first") Integer first);

	@Cacheable
	@Query(" select t from AlbumItem t where t.albumId in :albumIdList and t.status = 1 and t.itemStatus = 1 "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	List<AlbumItem> findListByAlbumIdList(
			@Param("albumIdList") List<Long> albumIdList,
			@Param("groupCodes") String groupCodes,
			@Param("currentTime") Date currentTime);

	@Cacheable
	@Query(" select t from AlbumItem t where t.albumId = :albumId and t.status = 1 and t.itemStatus = 1 "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	Page<AlbumItem> findPageListByAlbumId(@Param("albumId") Long albumId,
			@Param("groupCodes") String groupCodes,
			@Param("currentTime") Date currentTime, Pageable pageable);
	
	@Cacheable
	@Query(" select t from AlbumItem t where t.albumId = :albumId and t.itemType = :itemType and t.status = 1 and t.itemStatus = 1 "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	Page<AlbumItem> findPageListByAlbumIdAndItemType(
			@Param("albumId") Long albumId,
			@Param("itemType") Integer itemType,
			@Param("groupCodes") String groupCodes, @Param("currentTime") Date currentTime, Pageable pageable);
}
