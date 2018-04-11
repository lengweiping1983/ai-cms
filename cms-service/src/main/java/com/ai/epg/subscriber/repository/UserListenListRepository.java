package com.ai.epg.subscriber.repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.subscriber.entity.UserListenList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserListenListRepository extends AbstractRepository<UserListenList, Long> {

	@Query(" select t from UserListenList t where t.userId = :userId and t.itemType = :itemType and " +
			" t.itemId = :itemId and t.appCode = :appCode order by t.updateTime desc ")
	List<UserListenList> findByUserIdAndItemTypeAndItemIdAndAppCode(@Param("userId") Long userId, @Param("itemType") Integer itemType, @Param("itemId") Long itemId, @Param("appCode") String appCode);

	@Modifying
	@Query(" update UserListenList t set t.playTime = :playTime where t.userId = :userId and t.itemType = :itemType and t.itemId = :itemId and t.appCode = :appCode ")
	void updatePlayTime(@Param("playTime") Integer playTime, @Param("userId") Long userId,
                        @Param("itemType") Integer itemType, @Param("itemId") Long itemId, @Param("appCode") String appCode);

	@Query(" select t from UserListenList t where t.userId = :userId and t.appCode = :appCode order by t.updateTime desc ")
	Page<UserListenList> findPageListByUserId(@Param("userId") Long userId, @Param("appCode") String appCode, Pageable pageable);

	@Query(" select t from UserListenList t where t.userId = :userId and t.appCode = :appCode and t.itemType != :itemType order by t.updateTime desc ")
	Page<UserListenList> findPageListByUserId(@Param("userId") Long userId, @Param("appCode") String appCode,
                                            @Param("itemType") Integer itemType, Pageable pageable);

	@Query(" select t from UserListenList t where t.appCode = :appCode and t.itemType = :itemType order by t.updateTime desc ")
	Page<UserListenList> findLastPlayTv(@Param("appCode") String appCode, @Param("itemType") int itemType,
                                      Pageable pageable);

	UserListenList findOneByUserIdAndItemTypeAndAppCode(Long userId, Integer itemType, String appCode);

	UserListenList findOneByUserIdAndItemIdAndAppCode(Long userId, Long itemId, String appCode);


	@Query(" select t from UserListenList t where t.appCode = :appCode and t.userId = :userId and t.seriesId = :seriesId  order by t.updateTime desc ")
	List<UserListenList> findOneByAppCodeAndUserIdAndSeriesId(@Param("appCode") String appCode, @Param("userId") Long userId, @Param("seriesId") Long seriesId);

	@Query(" select t from UserListenList t where t.appCode = :appCode and t.userId = :userId and itemType = :itemType order by t.updateTime  ")
	List<UserListenList> findByAppCodeAndUserIdAndItemType(@Param("appCode") String appCode, @Param("userId") Long userId, @Param("itemType") Integer itemType);

	@Query(" select t from UserListenList t where t.appCode = :appCode and t.userId = :userId  order by t.updateTime  ")
	List<UserListenList> findOneByAppCodeAndUserId(@Param("appCode") String appCode, @Param("userId") Long userId);

	@Query(" select t from UserListenList t where t.appCode = :appCode and t.userId = :userId and t.updateTime>=current_date order by t.updateTime  ")
	List<UserListenList> findOneByAppCodeAndUserIdAndToday(@Param("appCode") String appCode, @Param("userId") Long userId);

	@Query(" select t from UserListenList t where t.appCode = :appCode and t.userId = :userId  order by t.updateTime asc ")
	List<UserListenList> findByAppCodeAndUserIdAsc(@Param("appCode") String appCode, @Param("userId") Long userId);

	@Query(" select t from UserListenList t where t.appCode = :appCode and t.userId = :userId  order by t.updateTime desc ")
	List<UserListenList> findByAppCodeAndUserIdDesc(@Param("appCode") String appCode, @Param("userId") Long userId);
}
