package com.ai.cms.league.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.league.entity.League;
import com.ai.common.repository.AbstractRepository;

@Repository
@CacheConfig(cacheNames = "LeagueRepository")
public interface LeagueRepository extends AbstractRepository<League, Long> {

	@Cacheable
	League findOne(Long id);

	@Cacheable
	List<League> findAll(Iterable<Long> ids);
	
	@Cacheable
	League findOneByCode(String code);

	@Query(" select p from League p where p.status =1 and p.id = :id")
	League findOneByIdAndStatus(@Param("id") Long id);
}
