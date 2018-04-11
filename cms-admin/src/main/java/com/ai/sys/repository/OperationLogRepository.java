package com.ai.sys.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ai.common.repository.AbstractRepository;
import com.ai.sys.entity.OperationLog;

@Repository
public interface OperationLogRepository extends
		AbstractRepository<OperationLog, Long> {

	@Modifying
	@Transactional
	@Query(" delete from OperationLog t where t.createTime <= :createTime")
	void deleteByCreateTime(@Param("createTime") Date createTime);
}
