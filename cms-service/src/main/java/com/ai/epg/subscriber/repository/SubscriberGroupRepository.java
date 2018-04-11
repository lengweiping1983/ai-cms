package com.ai.epg.subscriber.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.subscriber.entity.SubscriberGroup;

@Repository
public interface SubscriberGroupRepository extends AbstractRepository<SubscriberGroup, Long> {

	SubscriberGroup findOneByCode(String code);
	
	@Query(" select s.code from SubscriberGroup s where s.groupType != 1")
	List<String> findAllNotGeneralCode();
}
