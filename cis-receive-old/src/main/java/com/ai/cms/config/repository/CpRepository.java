package com.ai.cms.config.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.ai.cms.config.entity.Cp;
import com.ai.common.repository.AbstractRepository;

@Repository
@CacheConfig(cacheNames = "CpRepository")
public interface CpRepository extends AbstractRepository<Cp, Long> {

	@Cacheable
	Cp findOne(Long id);

	@Cacheable
	Cp findOneByCode(String code);

}
