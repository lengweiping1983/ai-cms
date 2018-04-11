package com.ai.cms.config.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.ai.cms.config.entity.Site;
import com.ai.common.repository.AbstractRepository;

@Repository
@CacheConfig(cacheNames = "SiteRepository")
public interface SiteRepository extends AbstractRepository<Site, Long> {

	@Cacheable
	Site findOne(Long id);

	@Cacheable
	Site findOneByCode(String code);

}
