package com.ai.epg.product.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.product.entity.Service;

@Repository
public interface ServiceRepository extends AbstractRepository<Service, Long> {

	Service findOne(Long id);

	Service findOneByCode(String code);

	@Query(" select t from Service t where t.status = 1 ")
	List<Service> findAllService();
	
	/******************************** 分发相关 begin ********************************/

	@Modifying
	@Query(" update Service t set t.injectionStatus = :injectionStatus, t.injectionTime = :injectionTime, t.partnerItemCode = :partnerItemCode where t.id = :id ")
	void updateInjectionStatus(@Param("id") Long id,
			@Param("injectionStatus") Integer injectionStatus,
			@Param("injectionTime") Date injectionTime, 
			@Param("partnerItemCode") String partnerItemCode);

	/******************************** 分发相关 end ********************************/
}
