package com.ai.epg.template.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.template.entity.Marker;

@Repository
public interface MarkerRepository extends AbstractRepository<Marker, Long> {

	Marker findOne(Long id);

}
