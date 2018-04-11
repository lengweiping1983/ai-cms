package com.ai.cms.live.repository;

import com.ai.cms.live.entity.ScheduleImportUpdate;
import com.ai.common.repository.AbstractRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleImportUpdateRepository extends
		AbstractRepository<ScheduleImportUpdate, Long> {

}
