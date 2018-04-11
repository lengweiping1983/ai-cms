package com.ai.cms.league.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.league.entity.LeagueSeasonStar;
import com.ai.common.repository.AbstractRepository;

@Repository
@CacheConfig(cacheNames = "LeagueSeasonStarRepository")
public interface LeagueSeasonStarRepository extends
		AbstractRepository<LeagueSeasonStar, Long> {

	@Cacheable
	LeagueSeasonStar findOne(Long id);

	@Cacheable
	LeagueSeasonStar findOneByLeagueSeasonIdAndItemTypeAndItemId(
			Long leagueSeasonId, Integer itemType, Long itemId);

	@Modifying
	@Query(" delete from LeagueSeasonStar t where t.leagueSeasonId = :leagueSeasonId")
	void deleteByLeagueSeasonId(@Param("leagueSeasonId") Long leagueSeasonId);

}
