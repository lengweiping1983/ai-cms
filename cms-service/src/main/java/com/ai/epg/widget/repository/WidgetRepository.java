package com.ai.epg.widget.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.widget.entity.Widget;

@Repository
@CacheConfig(cacheNames = "WidgetRepository")
public interface WidgetRepository extends AbstractRepository<Widget, Long> {

	@Cacheable
	Widget findOne(Long id);
	
	@Cacheable
	List<Widget> findAll(Iterable<Long> ids);
	
	@Cacheable
    Widget findOneByAppCodeAndCode(String appCode, String code);
	
	@Cacheable
	List<Widget> findByAppCodeAndCodeIn(String appCode, List<String> codeList);
	
	@Cacheable
	@Query(" select t from Widget t where t.appCode = :appCode and t.code in :codeList ")
	List<Widget> findByAppCodeAndCodeList(@Param("appCode") String appCode,
			@Param("codeList") List<String> codeList);
	
	@Cacheable
    List<Widget> findByCode(String code);
	
	@Cacheable
    @Query(" select t from Widget t ")
    List<Widget> findAllWidget();
}
