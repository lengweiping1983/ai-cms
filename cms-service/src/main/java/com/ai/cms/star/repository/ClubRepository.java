package com.ai.cms.star.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.ai.cms.star.entity.Club;
import com.ai.common.repository.AbstractRepository;

@Repository
@CacheConfig(cacheNames = "ClubRepository")
public interface ClubRepository extends AbstractRepository<Club, Long> {

	@Cacheable
	Club findOne(Long id);
	
	@Cacheable
	List<Club> findAll(Iterable<Long> ids);
	
}
