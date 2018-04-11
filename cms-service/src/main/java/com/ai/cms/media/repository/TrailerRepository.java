package com.ai.cms.media.repository;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.media.entity.Trailer;
import com.ai.common.repository.AbstractRepository;

@Repository
public interface TrailerRepository extends AbstractRepository<Trailer, Long> {

	@Cacheable
	Trailer findOne(Long id);

	@Cacheable
	Trailer findOneByProgramIdAndItemTypeAndItemId(Long programId,
			Integer itemType, Long itemId);

	@Cacheable
	@Query(" select p from Trailer p where p.itemId = :itemId and p.status= 1 ")
	List<Trailer> fintrailerlist(@Param("itemId") Long itemId);

	@Modifying
	@Query(" delete from Trailer t where t.programId = :programId")
	void deleteByProgramId(@Param("programId") Long programId);
	
}
