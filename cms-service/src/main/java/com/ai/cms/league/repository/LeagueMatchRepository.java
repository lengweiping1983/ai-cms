package com.ai.cms.league.repository;

import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.league.entity.LeagueMatch;
import com.ai.common.repository.AbstractRepository;

@Repository
@CacheConfig(cacheNames = "LeagueMatchRepository")
public interface LeagueMatchRepository extends
		AbstractRepository<LeagueMatch, Long> {

	@Cacheable
	LeagueMatch findOne(Long id);
	
	@Cacheable
	List<LeagueMatch> findAll(Iterable<Long> ids);
	
	@Cacheable
	@Query("select  p from LeagueMatch p where p.id in :idList and p.status = 1 ")
	List<LeagueMatch> findByIdInAndOnline(@Param("idList") List<Long> idList);
	
	@Cacheable
	@Query("select  p from LeagueMatch p where  p.id in (:idList) order by FIELD (p.id,:idList) ")
	List<LeagueMatch> findByIdIn(@Param("idList") List<Long> idList);

	@Query(" select p from LeagueMatch p where p.cpId = :cpId")
	List<LeagueMatch> findByCpId(@Param("cpId") Long cpId);
	
	@Query(" select p from LeagueMatch p where p.cpId = :cpId")
	Page<LeagueMatch> findByCpIdPage(@Param("cpId") Long cpId,Pageable pageable);
	
	@Query(" select p from LeagueMatch p where p.cpId = :cpId and p.createTime<= :createTime ")
	Page<LeagueMatch> findByCpIdPageByCreateTime(@Param("createTime") Date createTime,@Param("cpId") Long cpId,Pageable pageable);
	
	@Query(" select p from LeagueMatch p where p.createTime<= :createTime ")
	Page<LeagueMatch> findByCreateTime(@Param("createTime") Date createTime,Pageable pageable);
	
	@Query(" select p from LeagueMatch p where p.cpId = :cpId and ( p.createTime>= :startTime and p.createTime<= :endTime or p.updateTime>= :startTime and p.updateTime<= :endTime )")
	Page<LeagueMatch> findByTime(@Param("cpId") Long cpId,@Param("startTime") Date startTime,@Param("endTime") Date endTime,Pageable pageable);
	
	@Cacheable
	@Query(" select p from LeagueMatch p where p.leagueSeasonId = :leagueSeasonId and p.status = 1 ORDER BY p.beginTime ASC")
	List<LeagueMatch> findByLeagueSeasonId(
			@Param("leagueSeasonId") Long leagueSeasonId);

	@Cacheable
	// 获取当前赛季前后天数的赛事
	@Query(" select p from LeagueMatch p where p.leagueSeasonId = :leagueSeasonId and p.status = 1 and p.beginTime >= :beginTime and p.endTime <= :endTime ORDER BY p.beginTime ASC")
	Page<LeagueMatch> findLeagueMatchByDate(
			@Param("leagueSeasonId") Long leagueSeasonId,
			@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,
			Pageable pageable);

	@Cacheable
	// 根据相关赛事赛程获取
	@Query(" select t from LeagueMatch t where t.leagueSeasonId=:leagueSeasonId and t.status = 1 ORDER BY t.beginTime ASC")
	Page<LeagueMatch> findByLeagueSeasonId(
			@Param("leagueSeasonId") Long leagueSeasonId,
			Pageable pageable);
	
	@Cacheable
	// 根据相关赛事赛程获取
	@Query(" select t from LeagueMatch t where t.leagueSeasonId=:leagueSeasonId and t.status = 1 and t.leagueIndex=:leagueIndex ORDER BY t.beginTime ASC")
	Page<LeagueMatch> findLeagueSeasonByLeagueIndex(
			@Param("leagueSeasonId") Long leagueSeasonId,
			@Param("leagueIndex") int leagueIndex, Pageable pageable);
	
	@Cacheable
	// 根据相关赛事赛程获取
	@Query(" select t from LeagueMatch t where t.leagueSeasonId=:leagueSeasonId and t.status = 1 and t.leagueIndex in :leagueIndexList ORDER BY t.beginTime ASC")
	Page<LeagueMatch> findLeagueSeasonByLeagueIndex(
			@Param("leagueSeasonId") Long leagueSeasonId,
			@Param("leagueIndexList") List<Integer> leagueIndexList, Pageable pageable);

	@Cacheable
	// 获取近期赛事
	@Query(" select t from LeagueMatch t where t.sportContentType=:sportContentType and t.status = 1 ORDER BY t.beginTime ASC")
	Page<LeagueMatch> getLeagueMatchListByNow(
			@Param("sportContentType") int sportContentType, Pageable pageable);

	@Cacheable
	// 获取前3天后7天的比赛
	@Query(" select t from LeagueMatch t where t.sportContentType=:sportContentType and t.status = 1 and t.beginTime>=:beginTime and t.endTime<=:endTime ORDER BY t.beginTime ASC")
	Page<LeagueMatch> getLeagueMatchListByNow(
			@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,
			@Param("sportContentType") int sportContentType, Pageable pageable);

	@Cacheable
	// 获取前后小时正在直播的赛事
	@Query(" select p from LeagueMatch p where p.sportContentType = :sportContentType and p.status = 1 and p.beginTime>=:beginTime and p.endTime<=:endTime ORDER BY p.beginTime DESC")
	List<LeagueMatch> getLeagueMatchListByNow(
			@Param("sportContentType") int sportContentType,
			@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,
			Pageable pageable);

	@Cacheable
	// 根据明星ID和赛事开始时间分页获取明星相关赛事
	@Query(" select p from LeagueMatch p where (p.homeId = :starId or p.guestId = :starId ) and p.status = 1 and p.beginTime<= :beginTime ORDER BY p.beginTime DESC")
	Page<LeagueMatch> getRelatedMatchPageListByStarId(
			@Param("starId") Long starId, @Param("beginTime") Date beginTime,
			Pageable pageable);

	@Cacheable
	// 根据明星ID和赛事TAG分页获取明星相关赛事
	@Query(" select p from LeagueMatch p where p.keyword = :keyword and p.status = 1 ORDER BY p.beginTime DESC")
	Page<LeagueMatch> findPageListByStarIdAndTag(
			@Param("keyword") String keyword, Pageable pageable);
}
