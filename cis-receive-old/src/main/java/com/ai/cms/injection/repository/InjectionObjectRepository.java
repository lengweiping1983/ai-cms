package com.ai.cms.injection.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.injection.entity.InjectionObject;
import com.ai.common.repository.AbstractRepository;

@Repository
public interface InjectionObjectRepository extends
		AbstractRepository<InjectionObject, Long> {

	InjectionObject findOneByPlatformIdAndCategoryAndItemTypeAndItemId(
			Long platformId, String category, Integer itemType, Long itemId);

	List<InjectionObject> findByPlatformIdAndItemTypeAndPartnerItemCode(
			Long platformId, Integer itemType, String partnerItemCode);

	List<InjectionObject> findByItemTypeAndItemId(Integer itemType, Long itemId);

	List<InjectionObject> findByPlatformIdAndCategoryAndItemTypeAndItemParentId(
			Long platformId, String category, Integer itemType,
			Long itemParentId);

	List<InjectionObject> findByItemTypeAndItemIdIn(Integer itemType,
			List<Long> itemIdList);

	@Modifying
	@Query(" delete from InjectionObject t where t.itemType = :itemType and t.itemId = :itemId ")
	void deleteByItemTypeAndItemId(@Param("itemType") Integer itemType,
			@Param("itemId") Long itemId);

	@Modifying
	@Query(" delete from InjectionObject t where t.itemType = :itemType and t.itemId in :itemIdList ")
	void deleteByItemTypeAndItemIdIn(@Param("itemType") Integer itemType,
			@Param("itemIdList") List<Long> itemIdList);

	/******************************** 分发相关 begin ********************************/

	@Modifying
	@Query(" update InjectionObject t set t.injectionStatus = :injectionStatus, t.injectionTime = :injectionTime, t.partnerItemCode = :partnerItemCode "
			+ " where t.platformId = :platformId and t.category = :category and t.itemType = :itemType and t.itemId = :itemId ")
	void updateInjectionStatus(@Param("platformId") Long platformId,
			@Param("category") String category,
			@Param("itemType") Integer itemType, @Param("itemId") Long itemId,
			@Param("injectionStatus") Integer injectionStatus,
			@Param("injectionTime") Date injectionTime,
			@Param("partnerItemCode") String partnerItemCode);

	@Modifying
	@Query(" update InjectionObject t set t.injectionTime = :injectionTime, t.partnerItemCode = :partnerItemCode "
			+ " where t.platformId = :platformId and t.category = :category and t.itemType = :itemType and t.itemId = :itemId ")
	void updateInjectionCode(@Param("platformId") Long platformId,
			@Param("category") String category,
			@Param("itemType") Integer itemType, @Param("itemId") Long itemId,
			@Param("injectionTime") Date injectionTime,
			@Param("partnerItemCode") String partnerItemCode);

	/******************************** 分发相关 end ********************************/
}
