package com.ai.epg.subscriber.repository;

import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.subscriber.entity.PlayRecord;

@Repository
public interface PlayRecordRepository extends
		AbstractRepository<PlayRecord, Long> {

	PlayRecord findOneByUserIdAndItemTypeAndItemIdAndAppCode(Long userId,
			Integer itemType, Long itemId, String appCode);

}
