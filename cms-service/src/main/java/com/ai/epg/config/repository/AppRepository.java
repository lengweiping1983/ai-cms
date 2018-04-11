package com.ai.epg.config.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.config.entity.App;

@Repository
@CacheConfig(cacheNames = "AppRepository")
public interface AppRepository extends AbstractRepository<App, Long> {

	@Cacheable
	App findOne(Long id);

	@Cacheable
	App findOneByCode(String code);

	@Cacheable
	@Query(" select t from App t where t.status = 1 ")
	List<App> findAllOnlineApp();
}
