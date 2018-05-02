package com.ai.cms.injection.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.injection.entity.SendTask;
import com.ai.common.repository.AbstractRepository;

@Repository
public interface SendTaskRepository extends AbstractRepository<SendTask, Long> {

	Page<SendTask> findByStatus(Integer status, Pageable pageable);

	@Query(" select t from SendTask t where (t.status = 0) "
			+ " and (t.preTaskId is null or t.preTaskStatus = 1) "
			+ " and (t.nextCheckTime is null or t.nextCheckTime < :nextCheckTime) "
			+ " order by priority desc, createTime ")
	Page<SendTask> findNeedGenSendTask(
			@Param("nextCheckTime") Date nextCheckTime, Pageable pageable);

	@Query(" select t from SendTask t where (t.status = 1 or (t.status = 5 and t.requestTimes < :requestTimes)) "
			+ " and (t.preTaskId is null or t.preTaskStatus = 1) order by priority desc, createTime ")
	Page<SendTask> findByRequestTimes(
			@Param("requestTimes") Integer requestTimes, Pageable pageable);

	@Query(" select t from SendTask t where t.platformId = :platformId "
			+ " and t.category = :category " + " and t.type = :type "
			+ " and t.itemType = :itemType " + " and t.itemId = :itemId "
			+ " order by createTime desc ")
	Page<SendTask> findByPlatformIdAndCategoryAndTypeAndItemTypeAndItemId(
			@Param("platformId") Long platformId,
			@Param("category") String category, @Param("type") Integer type,
			@Param("itemType") Integer itemType, @Param("itemId") Long itemId,
			Pageable pageable);

	List<SendTask> findByCorrelateId(String correlateId);

	@Query(" select count(t) from SendTask t where t.lastRequestTime >= :lastRequestTime ")
	long countByLastRequestTime(@Param("lastRequestTime") Date lastRequestTime);

	@Modifying
	@Query(" update SendTask t set t.preTaskStatus = :preTaskStatus where t.preTaskId = :preTaskId ")
	void updatePreTaskStatusByPreTaskId(
			@Param("preTaskStatus") Integer preTaskStatus,
			@Param("preTaskId") Long preTaskId);

}