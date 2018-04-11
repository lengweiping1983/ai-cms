package com.ai.cms.media.repository;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.media.entity.SplitRProgram;
import com.ai.common.repository.AbstractRepository;

@Repository
public interface SplitRProgramRepository extends AbstractRepository<SplitRProgram, Long> {

	@Cacheable
	SplitRProgram findOneByItemTypeAndItemId(Integer itemType, Long itemId);
	
	@Cacheable
	List<SplitRProgram> findByItemTypeAndItemIdIn(Integer itemType,List<Long> itemIdList);
	
	@Modifying
	@Query(" delete SplitRProgram t where t.itemType = :itemType and t.itemId = :itemId ")
	void deleteByItemTypeAndItemId(@Param("itemType") Integer itemType,
			@Param("itemId") Long itemId);
}