package com.ai.epg.market.repository;

import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.market.entity.Activity;

@Repository
public interface ActivityRepository extends AbstractRepository<Activity, Long> {

	Activity findOne(Long id);

	Activity findOneByCode(String code);
}
