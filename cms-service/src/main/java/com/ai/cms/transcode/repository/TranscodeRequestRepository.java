package com.ai.cms.transcode.repository;

import org.springframework.stereotype.Repository;

import com.ai.cms.transcode.entity.TranscodeRequest;
import com.ai.common.repository.AbstractRepository;

@Repository
public interface TranscodeRequestRepository extends
		AbstractRepository<TranscodeRequest, Long> {

}
