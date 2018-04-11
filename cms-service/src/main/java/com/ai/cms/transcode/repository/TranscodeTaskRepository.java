package com.ai.cms.transcode.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.transcode.entity.TranscodeTask;
import com.ai.common.repository.AbstractRepository;

@Repository
public interface TranscodeTaskRepository extends
		AbstractRepository<TranscodeTask, Long> {

	Page<TranscodeTask> findByIsCallback(Integer isCallback, Pageable pageable);
	
	Page<TranscodeTask> findByTypeAndStatus(Integer type, Integer status,
			Pageable pageable);

	@Query(" select t from TranscodeTask t where (t.status = 1 or ((t.status = 4 or t.status = 5) "
			+ " and t.requestTimes < :requestTimes)) and ( t.preTaskId is null or t.preTaskId = 0 or t.preTaskStatus = 1 ) "
			+ " order by priority desc, createTime")
	Page<TranscodeTask> findByRequestTimes(
			@Param("requestTimes") Integer requestTimes, Pageable pageable);

	List<TranscodeTask> findByCloudTaskId(
			@Param("cloudTaskId") String cloudTaskId);

	List<TranscodeTask> findByResponseCode(
			@Param("responseCode") String responseCode);

	List<TranscodeTask> findByPreTaskId(@Param("preTaskId") Long preTaskId);

	List<TranscodeTask> findByRequestId(Long requestId);

	long countByRequestId(Long requestId);

	long countByRequestIdAndStatus(Long requestId, Integer status);

	@Query(" select count(t) from TranscodeTask t where t.requestId = :requestId "
			+ " and (t.status = 4 or t.status = 5) ")
	long countTaskFailNumByRequestId(@Param("requestId") Long requestId);

	@Modifying
	@Query(" delete from TranscodeTask t where t.requestId = :requestId ")
	void deleteByRequestId(@Param("requestId") Long requestId);

}