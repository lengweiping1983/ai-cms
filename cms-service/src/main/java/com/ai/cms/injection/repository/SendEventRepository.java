//package com.ai.cms.injection.repository;
//
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import com.ai.cms.injection.entity.SendEvent;
//import com.ai.common.repository.AbstractRepository;
//
//@Repository
//public interface SendEventRepository extends
//		AbstractRepository<SendEvent, Long> {
//
//	@Query("select t from SendEvent t where t.status = 0 "
//			+ " and ( t.nextCheckTime is null or t.nextCheckTime < :nextCheckTime) order by priority desc, fireTime, parentItemId, itemId ")
//	Page<SendEvent> findByNextCheckTime(
//			@Param("nextCheckTime") Date nextCheckTime, Pageable pageable);
//
//	@Query("select t from SendEvent t where t.status = 0 "
//			+ " and t.platformId = :platformId and t.category = :category "
//			+ " and ((t.itemType = :itemType and t.itemId = :itemId) or (t.parentItemType = :itemType and t.parentItemId = :itemId)) ")
//	List<SendEvent> findAllRelatedByItemTypeAndItemId(
//			@Param("platformId") Long platformId,
//			@Param("category") String category,
//			@Param("itemType") Integer itemType, @Param("itemId") Long itemId);
//
//	@Query("select t from SendEvent t where t.status = 0 "
//			+ " and t.platformId = :platformId and t.category = :category "
//			+ " and t.parentItemType = :parentItemType and t.parentItemId = :parentItemId ")
//	List<SendEvent> findByParentItemTypeAndParentItemId(
//			@Param("platformId") Long platformId,
//			@Param("category") String category,
//			@Param("parentItemType") Integer parentItemType,
//			@Param("parentItemId") Long parentItemId);
//
//	@Query("select t from SendEvent t where t.status = 0 "
//			+ " and t.platformId = :platformId and t.category = :category "
//			+ " and t.type = :type and t.parentItemType = :parentItemType and t.parentItemId = :parentItemId ")
//	List<SendEvent> findByTypeAndParentItemTypeAndParentItemId(
//			@Param("platformId") Long platformId,
//			@Param("category") String category, @Param("type") Integer type,
//			@Param("parentItemType") Integer parentItemType,
//			@Param("parentItemId") Long parentItemId);
//
//	@Query("select t from SendEvent t where t.status = 0 "
//			+ " and t.platformId = :platformId and t.category = :category "
//			+ " and t.type = :type and t.action = :action and t.itemType = :itemType and t.itemId = :itemId ")
//	List<SendEvent> findByTypeAndActionAndItemTypeAndItemId(
//			@Param("platformId") Long platformId,
//			@Param("category") String category, @Param("type") Integer type,
//			@Param("action") Integer action,
//			@Param("itemType") Integer itemType, @Param("itemId") Long itemId);
//
//	@Modifying
//	@Query(" update SendEvent t set t.nextCheckTime = :nextCheckTime where t.status = 0 "
//			+ " and t.platformId = :platformId and t.category = :category "
//			+ " and t.itemType = :itemType and t.itemId = :itemId ")
//	void updateNextCheckTime(@Param("platformId") Long platformId,
//			@Param("category") String category,
//			@Param("nextCheckTime") Date nextCheckTime,
//			@Param("itemType") Integer itemType, @Param("itemId") Long itemId);
//}
