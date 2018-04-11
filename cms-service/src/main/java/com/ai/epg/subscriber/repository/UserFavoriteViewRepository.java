package com.ai.epg.subscriber.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.subscriber.entity.UserFavoriteView;

@Repository
public interface UserFavoriteViewRepository extends AbstractRepository<UserFavoriteView, Long> {

    @Query(" select t from UserFavoriteView t where t.userId = :userId and t.appCode = :appCode and t.itemStatus = 1 order by t.id desc ")
    Page<UserFavoriteView> findPageListByUserId(@Param("userId") Long userId, @Param("appCode") String appCode,Pageable pageable);

}
