package com.ai.cms.config.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.config.entity.CpFtp;
import com.ai.common.repository.AbstractRepository;

@Repository
public interface CpFtpRepository extends AbstractRepository<CpFtp, Long> {

	CpFtp findOne(Long id);

	CpFtp findOneByCpCode(String cpCode);

	@Modifying
	@Query(" delete from CpFtp t where t.cpCode = :cpCode ")
	void deleteByCpCode(@Param("cpCode") String cpCode);

}
