package com.ai.epg.subscriber.repository;

import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.subscriber.entity.UserLike;

@Repository
public interface UserLikeRepository extends AbstractRepository<UserLike, Long> {

	UserLike findOneByUserIdAndItemTypeAndItemIdAndAppCode(Long userId,
			Integer itemType, Long itemId, String appCode);

}
