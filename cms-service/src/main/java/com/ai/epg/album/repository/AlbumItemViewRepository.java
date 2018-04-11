package com.ai.epg.album.repository;

import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.album.entity.AlbumItemView;

@Repository
@CacheConfig(cacheNames = "AlbumItemViewRepository")
public interface AlbumItemViewRepository extends
		AbstractRepository<AlbumItemView, Long> {

	@Cacheable
	AlbumItemView findOne(Long id);

	List<AlbumItemView> findByIdIn(List<Long> idList);

	@Query(" select t from AlbumItemView t where t.albumId = :albumId order by t.sortIndex ")
	List<AlbumItemView> findAllByAlbumId(@Param("albumId") Long albumId);
	
	@Cacheable
	@Query(" select t from AlbumItemView t where t.albumId = :albumId and t.status = 1 and t.itemStatus = 1 "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	List<AlbumItemView> findByAlbumId(@Param("albumId") Long albumId,
			@Param("groupCodes") String groupCodes,
			@Param("currentTime") Date currentTime);

	@Cacheable
	@Query(" select t from AlbumItemView t where t.albumId = :albumId and t.status = 1 and t.itemStatus = 1 "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	Page<AlbumItemView> findPageListByAlbumId(@Param("albumId") Long albumId,
			@Param("groupCodes") String groupCodes,
			@Param("currentTime") Date currentTime, Pageable pageable);

	@Cacheable
	@Query(" select t from AlbumItemView t where t.albumId = :albumId and t.status = 1 and (t.itemStatus = 1 or t.itemStatus = 0) "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	Page<AlbumItemView> findPageListByStatusAlbumId(
			@Param("albumId") Long albumId,
			@Param("groupCodes") String groupCodes,
			@Param("currentTime") Date currentTime, Pageable pageable);

	AlbumItemView findOneByAlbumIdAndPositionId(Long albumId, String positionId);

}
