package com.ai.cms.injection.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.injection.entity.InjectionPlatform;
import com.ai.common.repository.AbstractRepository;

@Repository
public interface InjectionPlatformRepository extends
		AbstractRepository<InjectionPlatform, Long> {

	@Query(" select t from InjectionPlatform t where t.status = 1 "
			+ " and t.cspId = :cspId and t.lspId = :lspId and t.direction = :direction ")
	List<InjectionPlatform> findByCspIdAndLspIdAndDirection(
			@Param("cspId") String cspId, @Param("lspId") String lspId,
			@Param("direction") Integer direction);

	@Query(" select t from InjectionPlatform t where t.status = 1 "
			+ " and t.direction = :direction ")
	List<InjectionPlatform> findAllByDirection(
			@Param("direction") Integer direction);

	@Query(" select t from InjectionPlatform t where t.status = 1 ")
	List<InjectionPlatform> findAllByValid();

	@Query(" select t from InjectionPlatform t where t.status = 1 "
			+ " and t.type = :type ")
	List<InjectionPlatform> findAllByType(@Param("type") Integer type);

}
