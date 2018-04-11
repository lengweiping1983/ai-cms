package com.ai.cms.transcode.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.transcode.entity.TranscodeRequestFile;
import com.ai.common.repository.AbstractRepository;

@Repository
public interface TranscodeRequestFileRepository extends
		AbstractRepository<TranscodeRequestFile, Long> {

	@Modifying
	@Query(" delete from TranscodeRequestFile t where t.requestId = :requestId ")
	void deleteByRequestId(@Param("requestId") Long requestId);
}
