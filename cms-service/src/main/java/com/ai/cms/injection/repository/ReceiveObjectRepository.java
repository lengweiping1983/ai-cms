package com.ai.cms.injection.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.injection.entity.ReceiveObject;
import com.ai.common.repository.AbstractRepository;

@Repository
public interface ReceiveObjectRepository extends
		AbstractRepository<ReceiveObject, Long> {

	List<ReceiveObject> findByModuleAndPid(Integer module, Long pid);

	List<ReceiveObject> findByModuleAndPidAndStatus(Integer module, Long pid,
			Integer status);

	ReceiveObject findOneByModuleAndPidAndItemTypeAndItemId(Integer module,
			Long pid, Integer itemType, Long itemId);

	@Modifying
	@Query(" delete from ReceiveObject t where t.pid = :pid ")
	void deleteByPid(@Param("pid") Long pid);

}
