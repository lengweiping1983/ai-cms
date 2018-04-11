package com.ai.epg.widget.repository;

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
import com.ai.epg.widget.entity.WidgetItem;

@Repository
@CacheConfig(cacheNames = "WidgetItemRepository")
public interface WidgetItemRepository extends
		AbstractRepository<WidgetItem, Long> {

	@Cacheable
	WidgetItem findOne(Long id);
	
	@Cacheable
	List<WidgetItem> findAll(Iterable<Long> ids);

	@Cacheable
	List<WidgetItem> findByWidgetIdAndItemTypeAndItemId(Long widgetId,
			Integer itemType, Long itemId);

	@Modifying
	@Query(" delete from WidgetItem t where t.widgetId = :widgetId")
	void deleteByWidgetId(@Param("widgetId") Long widgetId);

	@Modifying
	@Query(" update WidgetItem t set t.sortIndex = (t.sortIndex + :step) where t.widgetId = :widgetId and t.sortIndex > 0 and t.sortIndex >= :first ")
	void updateSortIndexByWidgetId(@Param("widgetId") Long widgetId,
			@Param("step") Integer step, @Param("first") Integer first);
	
	@Cacheable
	@Query(" select t from WidgetItem t where t.widgetId in :widgetIdList and t.status = 1 and t.itemStatus = 1 "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	List<WidgetItem> findListByWidgetIdList(
			@Param("widgetIdList") List<Long> widgetIdList,
			@Param("groupCodes") String groupCodes,
			@Param("currentTime") Date currentTime);

	@Cacheable
	@Query(" select t from WidgetItem t where t.widgetId = :widgetId and t.status = 1 and t.itemStatus = 1 "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	Page<WidgetItem> findPageListByWidgetId(@Param("widgetId") Long widgetId,
			@Param("groupCodes") String groupCodes,
			@Param("currentTime") Date currentTime, Pageable pageable);
	
	@Cacheable
	@Query(" select t from WidgetItem t where t.widgetId = :widgetId and t.itemType = :itemType and t.status = 1 and t.itemStatus = 1 "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	Page<WidgetItem> findPageListByWidgetIdAndItemType(
			@Param("widgetId") Long widgetId,
			@Param("itemType") Integer itemType,
			@Param("groupCodes") String groupCodes, @Param("currentTime") Date currentTime, Pageable pageable);
}
