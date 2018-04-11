package com.ai.cms.league.repository;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.league.entity.LeagueSeasonClub;
import com.ai.cms.league.entity.LeagueSeasonClubView;
import com.ai.common.repository.AbstractRepository;

@Repository
@CacheConfig(cacheNames = "LeagueSeasonClubRepository")
public interface LeagueSeasonClubRepository extends
		AbstractRepository<LeagueSeasonClub, Long> {

	@Cacheable
	LeagueSeasonClub findOne(Long id);

	@Cacheable
	LeagueSeasonClub findOneByLeagueSeasonIdAndItemTypeAndItemId(
			Long leagueSeasonId, Integer itemType, Long itemId);

	@Modifying
	@Query(" delete from LeagueSeasonClub t where t.leagueSeasonId = :leagueSeasonId")
	void deleteByLeagueSeasonId(@Param("leagueSeasonId") Long leagueSeasonId);

	@Cacheable
	@Query(" select t from LeagueSeasonClubView t where t.leagueSeasonId = :leagueSeasonId and t.status = 1 and t.itemStatus = 1 order by t.creditNum DESC")
	Page<LeagueSeasonClubView> findPageListByLeagueSeasonIdClub(
			@Param("leagueSeasonId") Long leagueSeasonId, Pageable pageable);
}
