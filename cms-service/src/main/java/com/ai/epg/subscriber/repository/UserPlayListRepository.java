package com.ai.epg.subscriber.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.subscriber.entity.UserPlayList;

@Repository
public interface UserPlayListRepository extends AbstractRepository<UserPlayList, Long> {

	@Query(" select t from UserPlayList t where t.userId = :userId and t.itemType = :itemType and " +
			" t.itemId = :itemId and t.appCode = :appCode order by t.updateTime desc ")
	List<UserPlayList> findByUserIdAndItemTypeAndItemIdAndAppCode(@Param("userId")Long userId, @Param("itemType")Integer itemType, @Param("itemId")Long itemId, @Param("appCode")String appCode);

	@Modifying
	@Query(" update UserPlayList t set t.playTime = :playTime where t.userId = :userId and t.itemType = :itemType and t.itemId = :itemId and t.appCode = :appCode ")
	void updatePlayTime(@Param("playTime") Integer playTime, @Param("userId") Long userId,
			@Param("itemType") Integer itemType, @Param("itemId") Long itemId, @Param("appCode") String appCode);

	@Query(" select t from UserPlayList t where t.userId = :userId and t.appCode = :appCode order by t.updateTime desc ")
	Page<UserPlayList> findPageListByUserId(@Param("userId") Long userId, @Param("appCode") String appCode, Pageable pageable);
	
	@Query(" select t from UserPlayList t where t.userId = :userId and t.appCode = :appCode and t.itemType != :itemType order by t.updateTime desc ")
	Page<UserPlayList> findPageListByUserId(@Param("userId") Long userId, @Param("appCode") String appCode,
			@Param("itemType") Integer itemType, Pageable pageable);

	@Query(" select t from UserPlayList t where t.appCode = :appCode and t.itemType = :itemType order by t.updateTime desc ")
	Page<UserPlayList> findLastPlayTv(@Param("appCode") String appCode, @Param("itemType") int itemType,
			Pageable pageable);

	UserPlayList findOneByUserIdAndItemTypeAndAppCode(Long userId, Integer itemType, String appCode);
	
	@Query(" select t from UserPlayList t where t.appCode = :appCode and t.userId = :userId and t.seriesId = :seriesId  order by t.updateTime desc ")
	List<UserPlayList> findOneByAppCodeAndUserIdAndSeriesId(@Param("appCode") String appCode,@Param("userId") Long userId,@Param("seriesId") Long seriesId);
	
	@Query(" select t from UserPlayList t where t.appCode = :appCode and t.userId = :userId and itemType = :itemType order by t.updateTime  ")
	List<UserPlayList> findByAppCodeAndUserIdAndItemType(@Param("appCode") String appCode,@Param("userId") Long userId,@Param("itemType") Integer itemType);
	
	@Query(" select t from UserPlayList t where t.appCode = :appCode and t.userId = :userId  order by t.updateTime  ")
	List<UserPlayList> findOneByAppCodeAndUserId(@Param("appCode") String appCode,@Param("userId") Long userId);

	@Query(" select t from UserPlayList t where t.appCode = :appCode and t.userId = :userId and t.updateTime>=current_date order by t.updateTime  ")
	List<UserPlayList> findOneByAppCodeAndUserIdAndToday(@Param("appCode") String appCode,@Param("userId") Long userId);

	@Query(" select t from UserPlayList t where t.appCode = :appCode and t.userId = :userId  ")
	List<UserPlayList> findByAppCodeAndUserId(@Param("appCode") String appCode,@Param("userId") Long userId);
}
