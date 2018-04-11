package com.ai.epg.uri.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.uri.entity.Uri;

@Repository
@CacheConfig(cacheNames = "UriRepository")
public interface UriRepository extends AbstractRepository<Uri, Long> {

	@Cacheable
	Uri findOne(Long id);
	
	@Cacheable
	List<Uri> findAll(Iterable<Long> ids);

	@Cacheable
	Uri findOneByAppCodeAndCode(String appCode, String code);

	@Cacheable
	@Query(" select t from Uri t where t.appCode = :appCode and t.type = :type and t.status = 1 order by t.sortIndex ")
	Page<Uri> findByAppCodeAndTypePageList(@Param("appCode") String appCode,
			@Param("type") Integer type, Pageable pageable);

}
