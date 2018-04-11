package com.ai.epg.template.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.template.entity.Template;

@Repository
@CacheConfig(cacheNames = "TemplateRepository")
public interface TemplateRepository extends AbstractRepository<Template, Long> {

	@Cacheable
	Template findOne(Long id);

	@Cacheable
	Template findOneByAppCodeAndCode(String appCode, String code);
	
	@Cacheable
	@Query(" select t from Template t where t.code = :code ")
	List<Template> findByCode(@Param("code") String code);
	
	@Cacheable
	@Query(" select t from Template t where t.appCode = :appCode order by sortIndex")
	List<Template> findByAppCode(@Param("appCode") String appCode);
	
	@Cacheable
	@Query(" select t from Template t where t.type = :type order by sortIndex")
	List<Template> findByType(@Param("type") Integer type);

	@Cacheable
	@Query(" select t from Template t where (t.appCode = :appCode or t.share = '1') order by appCode, sortIndex")
	List<Template> findByAppCodeIncludeShare(@Param("appCode") String appCode);
	
	@Cacheable
	@Query(" select t from Template t where (t.appCode = :appCode or t.share = '1') and t.type = :type order by appCode, sortIndex")
	List<Template> findByAppCodeAndTypeIncludeShare(@Param("appCode") String appCode,
			@Param("type") Integer type);
}
