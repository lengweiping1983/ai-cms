package com.ai.cms.league.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.league.entity.LeagueSeasonClubView;
import com.ai.common.repository.AbstractRepository;

@Repository
@CacheConfig(cacheNames = "LeagueSeasonClubViewRepository")
public interface LeagueSeasonClubViewRepository extends
		AbstractRepository<LeagueSeasonClubView, Long> {

	@Cacheable
	LeagueSeasonClubView findOne(Long id);
	
	@Cacheable
	@Query(" select t from LeagueSeasonClubView t where t.leagueSeasonId = :leagueSeasonId and t.status = 1 and t.itemStatus = 1 order by t.sortIndex ")
	List<LeagueSeasonClubView> findByLeagueSeasonId(
			@Param("leagueSeasonId") Long leagueSeasonId);

	@Cacheable
	@Query(" select t from LeagueSeasonClubView t where t.leagueSeasonId = :leagueSeasonId and t.status = 1 and t.itemStatus = 1 order by t.sortIndex ")
	Page<LeagueSeasonClubView> findPageListByLeagueSeasonId(
			@Param("leagueSeasonId") Long leagueSeasonId, Pageable pageable);

}
