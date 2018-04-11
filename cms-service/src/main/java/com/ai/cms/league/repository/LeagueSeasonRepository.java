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

import com.ai.cms.league.entity.LeagueSeason;
import com.ai.common.repository.AbstractRepository;

@Repository
@CacheConfig(cacheNames = "LeagueSeasonRepository")
public interface LeagueSeasonRepository extends
		AbstractRepository<LeagueSeason, Long> {

	@Cacheable
	LeagueSeason findOne(Long id);
	
	@Cacheable
	List<LeagueSeason> findAll(Iterable<Long> ids);
	
	@Cacheable
	List<LeagueSeason> findByIdIn(List<Long> idList);
	
	@Cacheable
	@Query(" select p from LeagueSeason p where p.id = :idList and p.status = 1")
	List<LeagueSeason> findByIdInAndOnline(List<Long> idList);
	
	@Query(" select p from LeagueSeason p where p.cpId = :cpId")
	List<LeagueSeason> findByCpId(@Param("cpId") Long cpId);
	
	@Query(" select p from LeagueSeason p where p.cpId = :cpId")
	Page<LeagueSeason> findByCpIdPage(@Param("cpId") Long cpId,Pageable pageable);
	
	@Query(" select p from LeagueSeason p where p.cpId = :cpId and p.createTime<= :createTime ")
	Page<LeagueSeason> findByCpIdPageByCreateTime(@Param("createTime") Date createTime,@Param("cpId") Long cpId,Pageable pageable);
	
	@Query(" select p from LeagueSeason p where p.createTime<= :createTime ")
	Page<LeagueSeason> findByCreateTime(@Param("createTime") Date createTime,Pageable pageable);
	
	@Query(" select p from LeagueSeason p where p.cpId = :cpId and ( p.createTime>= :startTime and p.createTime<= :endTime or p.updateTime>= :startTime and p.updateTime<= :endTime )")
	Page<LeagueSeason> findByTime(@Param("cpId") Long cpId,@Param("startTime") Date startTime,@Param("endTime") Date endTime,Pageable pageable);
	
	@Query(" select p from LeagueSeason p where p.status =1 and p.id = :id")
	LeagueSeason findOneByIdAndStatus(@Param("id") Long id);

	@Cacheable
	// 通过联赛获取当前赛季的对象
	@Query(" select t from LeagueSeason t where t.leagueId=:leagueId ORDER BY t.beginTime DESC")
	List<LeagueSeason> findLeagueSeason(@Param("leagueId") Long leagueId);

}