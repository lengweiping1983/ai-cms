package com.ai.epg.uri.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.uri.entity.UriParam;

@Repository
@CacheConfig(cacheNames = "UriParamRepository")
public interface UriParamRepository extends AbstractRepository<UriParam, Long> {

	@Cacheable
	UriParam findOne(Long id);

	@Cacheable
	@Query(" select t from UriParam t where t.uriId = :uriId ")
	List<UriParam> findAllByUriId(@Param("uriId") Long uriId);

	@Cacheable
	UriParam findOneByUriIdAndItemTypeAndNumber(Long uriId, Integer itemType,
			String number);

	@Cacheable
	UriParam findOneByUriIdAndItemTypeAndItemId(Long uriId, Integer itemType,
			Long itemId);

	@Modifying
	@Transactional
	@Query(" delete from UriParam t where t.uriId = :uriId ")
	void deleteByUriId(@Param("uriId") Long uriId);
}
