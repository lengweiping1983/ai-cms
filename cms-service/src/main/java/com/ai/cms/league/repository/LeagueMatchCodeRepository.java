package com.ai.cms.league.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.ai.cms.league.entity.LeagueMatchCode;
import com.ai.common.repository.AbstractRepository;

@Repository
@CacheConfig(cacheNames = "LeagueMatchCodeRepository")
public interface LeagueMatchCodeRepository extends
		AbstractRepository<LeagueMatchCode, Long> {

	@Cacheable
	LeagueMatchCode findOne(Long id);

	@Cacheable
	LeagueMatchCode findByItemTypeAndItemId(Integer itemType, Long itemId);

}
