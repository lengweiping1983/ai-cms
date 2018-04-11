package com.ai.epg.template.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.template.entity.TemplateDefault;

@Repository
@CacheConfig(cacheNames = "TemplateDefaultRepository")
public interface TemplateDefaultRepository extends
		AbstractRepository<TemplateDefault, Long> {

	@Cacheable
	List<TemplateDefault> findByEnvAndType(String env, Integer type);

}
