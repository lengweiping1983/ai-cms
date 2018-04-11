package com.ai.epg.product.repository;

import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.product.entity.ServiceItem;

@Repository
public interface ServiceItemRepository extends
		AbstractRepository<ServiceItem, Long> {

	ServiceItem findOne(Long id);

	ServiceItem findOneByServiceIdAndItemTypeAndItemId(Long serviceId,
			Integer itemType, Long itemId);

	@Modifying
	@Query(" delete from ServiceItem t where t.serviceId = :serviceId")
	void deleteByServiceId(@Param("serviceId") Long serviceId);

	@Modifying
	@Query(" update ServiceItem t set t.sortIndex = (t.sortIndex + :num) where t.serviceId = :serviceId and t.sortIndex > 0 and t.sortIndex >= :first ")
	void updateSortIndexByServiceId(@Param("serviceId") Long serviceId,
			@Param("num") Integer num, @Param("first") Integer first);

	/******************************** 分发相关 begin ********************************/

	@Modifying
	@Query(" update ServiceItem t set t.injectionStatus = :injectionStatus, t.injectionTime = :injectionTime where t.id = :id ")
	void updateInjectionStatus(@Param("id") Long id,
			@Param("injectionStatus") Integer injectionStatus,
			@Param("injectionTime") Date injectionTime);
	
	@Modifying
	@Query(" update ServiceItem t set t.platformId = :platformId, t.injectionStatus = :injectionStatus, t.injectionTime = :injectionTime where t.id = :id ")
	void updateInjectionStatus(@Param("id") Long id,
			@Param("platformId") Long platformId,
			@Param("injectionStatus") Integer injectionStatus,
			@Param("injectionTime") Date injectionTime);

//	@Query(" select t from ServiceItem t where t.itemId = itemId and t.itemType = itemType")
	ServiceItem findOneByItemIdAndItemType(Long itemId, Integer itemType);

    ServiceItem findOneByItemIdAndItemTypeAndServiceId(Long itemId, Integer itemType, Long serviceId);

    /******************************** 分发相关 end ********************************/
}
