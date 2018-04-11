package com.ai.epg.market.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.market.entity.ActivityUser;

@Repository
public interface ActivityUserRepository extends
		AbstractRepository<ActivityUser, Long> {
	
	List<ActivityUser> findByPhone(String phone);
	
}
