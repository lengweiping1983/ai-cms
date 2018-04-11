package com.ai.epg.category.repository;

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
import org.springframework.transaction.annotation.Transactional;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.category.entity.CategoryItem;

@Repository
@CacheConfig(cacheNames = "CategoryItemRepository")
public interface CategoryItemRepository extends
		AbstractRepository<CategoryItem, Long> {

	@Cacheable
	CategoryItem findOne(Long id);

	@Cacheable
	List<CategoryItem> findByCategoryIdAndItemTypeAndItemId(Long categoryId,
			Integer itemType, Long itemId);

	@Modifying
	@Query(" delete from CategoryItem t where t.categoryId = :categoryId")
	void deleteByCategoryId(@Param("categoryId") Long categoryId);

	@Modifying
	@Query(" update CategoryItem t set t.sortIndex = (t.sortIndex + :step) where t.categoryId = :categoryId and t.sortIndex > 0 and t.sortIndex >= :first ")
	void updateSortIndexByCategoryId(@Param("categoryId") Long categoryId,
			@Param("step") Integer step, @Param("first") Integer first);

	/******************************** 分发相关 begin ********************************/

	@Modifying
	@Query(" update CategoryItem t set t.injectionStatus = :injectionStatus, t.injectionTime = :injectionTime where t.id = :id ")
	void updateInjectionStatus(@Param("id") Long id,
			@Param("injectionStatus") Integer injectionStatus,
			@Param("injectionTime") Date injectionTime);
	
	@Modifying
	@Query(" update CategoryItem t set t.platformId = :platformId, t.injectionStatus = :injectionStatus, t.injectionTime = :injectionTime where t.id = :id ")
	void updateInjectionStatus(@Param("id") Long id,
			@Param("platformId") Long platformId,
			@Param("injectionStatus") Integer injectionStatus,
			@Param("injectionTime") Date injectionTime);

	/******************************** 分发相关 end ********************************/
	
	@Cacheable
	@Query(" select t from CategoryItem t where t.categoryId in :categoryIdList and t.status = 1 and t.itemStatus = 1 "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	List<CategoryItem> findListByCategoryIdList(
			@Param("categoryIdList") List<Long> categoryIdList,
			@Param("groupCodes") String groupCodes,
			@Param("currentTime") Date currentTime);

	@Cacheable
	@Query(" select t from CategoryItem t where t.categoryId = :categoryId and t.status = 1 and t.itemStatus = 1 "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	Page<CategoryItem> findPageListByCategoryId(@Param("categoryId") Long categoryId,
			@Param("groupCodes") String groupCodes,
			@Param("currentTime") Date currentTime, Pageable pageable);
	
	@Cacheable
	@Query(" select t from CategoryItem t where t.categoryId = :categoryId and t.itemType = :itemType and t.status = 1 and t.itemStatus = 1 "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	Page<CategoryItem> findPageListByCategoryIdAndItemType(
			@Param("categoryId") Long categoryId,
			@Param("itemType") Integer itemType,
			@Param("groupCodes") String groupCodes, @Param("currentTime") Date currentTime, Pageable pageable);
	
	@Modifying
	@Transactional
	@Query(value = " update epg_category_item t set t.sort_index = (select epg.nextval('SortIndex')) where t.category_id = :categoryId order by sort_index asc" , nativeQuery = true)
	void updateSeqSortIndexByCategoryId(@Param("categoryId") Long categoryId);
	
	@Modifying
    @Transactional
    @Query(value = " update epg.sequence t set t.current_value = 0 where t.name = :name ", nativeQuery = true)
    void updateSortIndex(@Param("name") String name);
}
