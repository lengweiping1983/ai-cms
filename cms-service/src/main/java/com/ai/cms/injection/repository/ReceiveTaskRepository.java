package com.ai.cms.injection.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.injection.entity.ReceiveTask;
import com.ai.common.repository.AbstractRepository;

@Repository
public interface ReceiveTaskRepository extends
		AbstractRepository<ReceiveTask, Long> {

	@Query(" select t from ReceiveTask t where t.status = 1 "
			+ " and t.downloadTimes < :downloadTimes order by priority desc, createTime ")
	Page<ReceiveTask> findByDownloadTimes(
			@Param("downloadTimes") Integer downloadTimes, Pageable pageable);

	@Query(" select t from ReceiveTask t where t.responseStatus != 3 and (t.status = 3 or t.status = 4) "
			+ " and t.responseTimes < :responseTimes order by priority desc, createTime ")
	Page<ReceiveTask> findByResponseTimes(
			@Param("responseTimes") Integer responseTimes, Pageable pageable);

	List<ReceiveTask> findByPlatformIdAndCorrelateId(
			@Param("platformId") Long platformId,
			@Param("correlateId") String correlateId);

	@Query(" select t from ReceiveTask t where (t.status = 6) "
			+ " and (t.nextCheckTime is null or t.nextCheckTime < :nextCheckTime) "
			+ " order by priority desc, createTime ")
	Page<ReceiveTask> findByNextCheckTime(
			@Param("nextCheckTime") Date nextCheckTime, Pageable pageable);
}
