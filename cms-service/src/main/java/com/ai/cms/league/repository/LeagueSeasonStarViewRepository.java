package com.ai.cms.league.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.league.entity.LeagueSeasonStarView;
import com.ai.common.repository.AbstractRepository;

@Repository
@CacheConfig(cacheNames = "LeagueSeasonStarViewRepository")
public interface LeagueSeasonStarViewRepository extends
		AbstractRepository<LeagueSeasonStarView, Long> {

	@Cacheable
	LeagueSeasonStarView findOne(Long id);
	
	@Cacheable
	@Query(" select t from LeagueSeasonStarView t where t.leagueSeasonId = :leagueSeasonId and t.status = 1 and t.itemStatus = 1 order by t.sortIndex ")
	List<LeagueSeasonStarView> findByLeagueSeasonId(
			@Param("leagueSeasonId") Long leagueSeasonId);

	@Cacheable
	@Query(" select t from LeagueSeasonStarView t where t.leagueSeasonId = :leagueSeasonId and t.status = 1 and t.itemStatus = 1 order by t.sortIndex ")
	Page<LeagueSeasonStarView> findPageListByLeagueSeasonId(
			@Param("leagueSeasonId") Long leagueSeasonId, Pageable pageable);

	@Cacheable
	@Query(" select t from LeagueSeasonStarView t where t.leagueSeasonId = :leagueSeasonId and t.status = 1 and t.itemStatus = 1 order by t.enterNum DESC")
	Page<LeagueSeasonStarView> findPageListByLeagueSeasonIdStartSS(
			@Param("leagueSeasonId") Long leagueSeasonId, Pageable pageable);

	@Cacheable
	@Query(" select t from LeagueSeasonStarView t where t.leagueSeasonId = :leagueSeasonId and t.status = 1 and t.itemStatus = 1 order by t.assistNum DESC")
	Page<LeagueSeasonStarView> findPageListByLeagueSeasonIdStartZG(
			@Param("leagueSeasonId") Long leagueSeasonId, Pageable pageable);

}
