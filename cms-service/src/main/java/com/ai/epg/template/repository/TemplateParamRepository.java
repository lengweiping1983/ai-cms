package com.ai.epg.template.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.template.entity.TemplateParam;

@Repository
@CacheConfig(cacheNames = "TemplateParamRepository")
public interface TemplateParamRepository extends
		AbstractRepository<TemplateParam, Long> {

	@Cacheable
	@Query(" select t from TemplateParam t where t.templateId = :templateId order by sortIndex, number ")
	List<TemplateParam> findAllByTemplateId(
			@Param("templateId") Long templateId);
	
	@Cacheable
	@Query(" select t from TemplateParam t where t.templateId = :templateId and t.internalParamCategory = :internalParamCategory order by sortIndex, number ")
	List<TemplateParam> findAllByTemplateIdAndInternalParamCategory(
			@Param("templateId") Long templateId,
			@Param("internalParamCategory") Integer internalParamCategory);

	@Cacheable
	TemplateParam findOneByTemplateIdAndPositionId(Long templateId, String positionId);
	
	@Cacheable
	TemplateParam findOneByTemplateIdAndNumber(Long templateId, String number);
	
	@Cacheable
	TemplateParam findOneByTemplateIdAndTypeAndCode(Long templateId, Integer type, String code);
		
	@Modifying
	@Transactional
	@Query(" delete from TemplateParam t where t.templateId = :templateId ")
	void deleteByTemplateId(@Param("templateId") Long templateId);

}
