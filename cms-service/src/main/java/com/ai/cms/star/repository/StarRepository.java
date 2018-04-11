package com.ai.cms.star.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.ai.cms.star.entity.Star;
import com.ai.common.repository.AbstractRepository;

@Repository
@CacheConfig(cacheNames = "StarRepository")
public interface StarRepository extends AbstractRepository<Star, Long> {

	@Cacheable
	Star findOne(Long id);

	@Cacheable
	List<Star> findAll(Iterable<Long> ids);
	
}
