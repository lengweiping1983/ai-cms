package com.ai.cms.config.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ai.cms.config.entity.MediaTemplate;
import com.ai.common.repository.AbstractRepository;

@Repository
public interface MediaTemplateRepository extends
		AbstractRepository<MediaTemplate, Long> {

	MediaTemplate findOneByCode(String code);

	List<MediaTemplate> findByIdIn(List<Long> idList);

	List<MediaTemplate> findByCategory(String category);

	List<MediaTemplate> findByMediaSpec(String mediaSpec);

}
