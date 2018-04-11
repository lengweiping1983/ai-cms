package com.ai.epg.category.repository;

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
import com.ai.epg.category.entity.CategoryItemView;

@Repository
@CacheConfig(cacheNames = "CategoryItemViewRepository")
public interface CategoryItemViewRepository extends
		AbstractRepository<CategoryItemView, Long> {

	@Cacheable
	CategoryItemView findOne(Long id);

	List<CategoryItemView> findByIdIn(List<Long> idList);

	@Cacheable
	@Query(" select t from CategoryItemView t where t.categoryId = :categoryId and t.status = 1 and t.itemStatus = 1 "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	List<CategoryItemView> findByCategoryId(
			@Param("categoryId") Long categoryId,
			@Param("groupCodes") String groupCodes,
			@Param("currentTime") Date currentTime);

	@Cacheable
	@Query(" select t from CategoryItemView t where t.categoryId = :categoryId and t.status = 1 and t.itemStatus = 1 "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	Page<CategoryItemView> findPageListByCategoryId(
			@Param("categoryId") Long categoryId,
			@Param("groupCodes") String groupCodes,
			@Param("currentTime") Date currentTime, Pageable pageable);

	@Cacheable
	@Query(" select t from CategoryItemView t where t.categoryId = :categoryId and t.status = 1 and t.itemStatus = 1  and t.tag like :tag "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	Page<CategoryItemView> findPageListByCategoryIdAndTag(
			@Param("categoryId") Long categoryId, @Param("tag") String tag,
			@Param("groupCodes") String groupCodes,
			@Param("currentTime") Date currentTime, Pageable pageable);

	@Cacheable
	@Query(" select t from CategoryItemView t where t.categoryId = :categoryId and t.status = 1 and t.itemStatus = 1  and t.tag not like :tag "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	Page<CategoryItemView> findPageListByCategoryIdExceptTag(
			@Param("categoryId") Long categoryId, @Param("tag") String tag,
			@Param("groupCodes") String groupCodes,
			@Param("currentTime") Date currentTime, Pageable pageable);
}
