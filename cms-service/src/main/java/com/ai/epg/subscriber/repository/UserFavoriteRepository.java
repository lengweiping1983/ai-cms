package com.ai.epg.subscriber.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.subscriber.entity.UserFavorite;

@Repository
public interface UserFavoriteRepository extends
		AbstractRepository<UserFavorite, Long> {

	UserFavorite findOneByUserIdAndItemTypeAndItemIdAndAppCode(Long userId,
			Integer itemType, Long itemId, String appCode);

	@Query(" select t from UserFavorite t where t.userId = :userId and t.appCode = :appCode order by t.id desc ")
	Page<UserFavorite> findPageListByUserId(@Param("userId") Long userId,
			@Param("appCode") String appCode, Pageable pageable);

}
