package com.ai.cms.media.repository;

import org.springframework.stereotype.Repository;

import com.ai.cms.media.entity.MediaImport;
import com.ai.common.repository.AbstractRepository;

@Repository
public interface MediaImportRepository extends
		AbstractRepository<MediaImport, Long> {

}
