package com.ai.epg.widget.repository;

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
import com.ai.epg.widget.entity.WidgetItemView;

@Repository
@CacheConfig(cacheNames = "WidgetItemViewRepository")
public interface WidgetItemViewRepository extends
		AbstractRepository<WidgetItemView, Long> {

	@Cacheable
	WidgetItemView findOne(Long id);

	List<WidgetItemView> findByIdIn(List<Long> idList);
	
	@Cacheable
	@Query(" select t from WidgetItemView t where t.widgetId in :widgetIdList and t.status = 1 and t.itemStatus = 1 "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	List<WidgetItemView> findByWidgetIdList(@Param("widgetIdList") List<Long> widgetIdList,
			@Param("groupCodes") String groupCodes, @Param("currentTime") Date currentTime);
	
	@Cacheable
	@Query(" select t from WidgetItemView t where t.widgetId = :widgetId and t.status = 1 and t.itemStatus = 1 "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	List<WidgetItemView> findByWidgetId(@Param("widgetId") Long widgetId,
			@Param("groupCodes") String groupCodes, @Param("currentTime") Date currentTime);

	@Cacheable
	@Query(" select t from WidgetItemView t where t.widgetId = :widgetId and t.itemType = :itemType and t.status = 1 and t.itemStatus = 1 "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	List<WidgetItemView> findByWidgetIdAndItemType(
			@Param("widgetId") Long widgetId,
			@Param("itemType") Integer itemType,
			@Param("groupCodes") String groupCodes, @Param("currentTime") Date currentTime);

	@Cacheable
	@Query(" select t from WidgetItemView t where t.widgetId = :widgetId and t.status = 1 and t.itemStatus = 1 "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	Page<WidgetItemView> findPageListByWidgetId(
			@Param("widgetId") Long widgetId,
			@Param("groupCodes") String groupCodes, @Param("currentTime") Date currentTime, Pageable pageable);

	@Cacheable
	@Query(" select t from WidgetItemView t where t.widgetId = :widgetId and t.itemType = :itemType and t.status = 1 and t.itemStatus = 1 "
			+ " and (t.groupCodes is null or t.groupCodes = '' or t.groupCodes like :groupCodes) "
			+ " and (t.validTime <= :currentTime and t.expiredTime >= :currentTime) order by t.sortIndex ")
	Page<WidgetItemView> findPageListByWidgetIdAndItemType(
			@Param("widgetId") Long widgetId,
			@Param("itemType") Integer itemType,
			@Param("groupCodes") String groupCodes, @Param("currentTime") Date currentTime, Pageable pageable);
}
