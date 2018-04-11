package com.ai.epg.product.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.product.entity.Charge;

@Repository
@CacheConfig(cacheNames = "ChargeRepository")
public interface ChargeRepository extends AbstractRepository<Charge, Long> {

	@Cacheable
	Charge findOne(Long id);

	@Cacheable
	Charge findOneByAppCodeAndCode(String appCode, String code);
	
	@Cacheable
	@Query(" select t from Charge t where t.partnerProductCode = :partnerProductCode order by t.sortIndex ")
	List<Charge> findByPartnerProductCode(@Param("partnerProductCode") String partnerProductCode);

	@Cacheable
	@Query(" select t from Charge t where t.appCode = :appCode and t.type = :type and t.status = :status order by t.sortIndex ")
	List<Charge> findByAppCodeAndTypeAndStatus(@Param("appCode") String appCode, @Param("type") Integer type,
			@Param("status") Integer status);
	
	@Cacheable
	@Query(" select t from Charge t where t.appCode = :appCode and t.status = 1 and t.partnerProductCode in :productList order by t.sortIndex ")
	List<Charge> findByAppCodeAndproducIn(@Param("appCode") String appCode,
			@Param("productList") List<String> productList);

	@Cacheable
	@Query(" select t from Charge t where t.appCode = :appCode and t.status = 1 order by t.sortIndex ")
	List<Charge> findByAppCode(@Param("appCode") String appCode);
	
	@Cacheable
	@Query(" select t from Charge t where t.appCodes like :appCode and t.status = 1 order by t.sortIndex ")
	List<Charge> findByAppCodes(@Param("appCode") String appCode);
	
	@Cacheable
	@Query(" select t from Charge t where t.partnerProductCode = :partnerProductCode and t.status = 1 order by t.sortIndex ")
	Charge findPartnerProductCode(@Param("partnerProductCode") String partnerProductCode);
	
	@Cacheable
	@Query(" select t from Charge t where t.appCode = :appCode order by t.sortIndex ")
	List<Charge> findAllByAppCode(@Param("appCode") String appCode);

	@Cacheable
	@Query(" select t from Charge t where t.status = 1 order by t.sortIndex ")
	List<Charge> findAllChargeByOnline();// 获取所有生效的计费点
}
