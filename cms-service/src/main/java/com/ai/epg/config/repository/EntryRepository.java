package com.ai.epg.config.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.config.entity.Entry;

@Repository
@CacheConfig(cacheNames = "EntryRepository")
public interface EntryRepository extends AbstractRepository<Entry, Long> {

	@Cacheable
	Entry findOne(Long id);

	@Cacheable
	Entry findOneByCode(String code);

}
