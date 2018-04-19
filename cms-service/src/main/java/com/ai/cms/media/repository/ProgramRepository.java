package com.ai.cms.media.repository;

import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.media.entity.Program;
import com.ai.common.repository.AbstractRepository;

@Repository
@CacheConfig(cacheNames = "ProgramRepository")
public interface ProgramRepository extends AbstractRepository<Program, Long> {
	
	Program findOneById(Long id);
	
	@Cacheable
	Program findOne(Long id);
	
	@Cacheable
	List<Program> findAll(Iterable<Long> ids);
	
	@Cacheable
	List<Program> findByIdIn(List<Long> idList);
	
	@Modifying
	@Query(" delete from Program p where p.seriesId = :seriesId ")
	void deleteBySeriesId(@Param("seriesId") Long seriesId);
	
	@Modifying
	@Query(" update Program t set t.cpId = :toCpId where t.seriesId = :seriesId and t.cpId != :toCpId ")
	void updateCpIdBySeriesId(@Param("seriesId") Long seriesId, @Param("toCpId") String toCpId);
	
	@Modifying
	@Query(" update Program t set t.templateId = :templateId where t.id = :id and t.templateId != :templateId ")
	void updateTemplateIdById(@Param("id") Long id, @Param("templateId") String templateId);
	
	@Modifying
	@Query(" update Program t set t.auditStatus = :auditStatus, t.auditUser = :auditUser,"
			+ " t.auditTime = :auditTime, t.storageTime = :storageTime where t.seriesId = :seriesId ")
	void updateAuditStatusBySeriesId(@Param("seriesId") Long seriesId,
			@Param("auditStatus") Integer auditStatus,
			@Param("auditUser") String auditUser,
			@Param("auditTime") Date auditTime,
			@Param("storageTime") Date storageTime);
	
	@Modifying
	@Query(" update Program t set t.auditStatus = :auditStatus, t.auditUser = :auditUser,"
			+ " t.auditTime = :auditTime where t.seriesId = :seriesId ")
	void updateAuditStatusBySeriesId(@Param("seriesId") Long seriesId,
			@Param("auditStatus") Integer auditStatus,
			@Param("auditUser") String auditUser,
			@Param("auditTime") Date auditTime);
	
	@Cacheable
    @Query(" select t from Program t where t.status = 1 and t.tag != '' ")
    List<Program> findAllProgram();

	
	Program findOneBySeriesIdAndEpisodeIndex(Long seriesId, Integer episodeIndex);
	
	@Query(" select count(p) from Program p ")
	Integer findMaxSize();

	@Query(" select p from Program p where (p.updateTime>= :startTime and p.updateTime<= :endTime )")
	Page<Program> findByStartTimeAndEndTime(@Param("startTime") Date startTime,@Param("endTime") Date endTime,Pageable pageable);

	@Query(" select p from Program p where p.type = 2 and p.seriesId in :seriesIdList order by seriesId, episodeIndex ")
	List<Program> findBySeriesIdIn(@Param("seriesIdList") List<Long> seriesIdList);
	
	@Query(" select p from Program p where p.name = :name and p.episodeIndex = :episodeIndex ")
	List<Program> findByNameAndEpisodeIndex(@Param("name") String name,
			@Param("episodeIndex") Integer episodeIndex);

	@Query(" select p from Program p where p.contentType = :contentType and p.name = :name and p.episodeIndex = :episodeIndex ")
	List<Program> findByContentTypeAndNameAndEpisodeIndex(
			@Param("contentType") Integer contentType,
			@Param("name") String name,
			@Param("episodeIndex") Integer episodeIndex);

	@Cacheable
	@Query(" select p from Program p where p.id in :idList and p.status = 1 ")
	List<Program> findByIdInAndStatus(@Param("idList") List<Long> idList);
	
	@Query(" select p from Program p where p.status =1 and p.id = :id")
	Program findOneByIdAndStatus(@Param("id") Long id);
	
//	@Query(" select p from Program p where p.cpId = :cpId")
//	List<Program> findByCpId(@Param("cpId") Long cpId);
//	
//	@Query(" select p from Program p where p.cpId = :cpId")
//	Page<Program> findByCpIdPage(@Param("cpId") Long cpId,Pageable pageable);
//	
//	@Query(" select p from Program p where p.cpId = :cpId and p.createTime<= :createTime ")
//	Page<Program> findByCpIdPageByCreateTime(@Param("createTime") Date createTime,@Param("cpId") Long cpId,Pageable pageable);
//	
//	@Query(" select p from Program p where p.createTime<= :createTime ")
//	Page<Program> findByCreateTime(@Param("createTime") Date createTime,Pageable pageable);
//	
//	@Query(" select p from Program p where p.cpId = :cpId and ( p.createTime>= :startTime and p.createTime<= :endTime or p.updateTime>= :startTime and p.updateTime<= :endTime )")
//	Page<Program> findByTime(@Param("cpId") Long cpId,@Param("startTime") Date startTime,@Param("endTime") Date endTime,Pageable pageable);
//
//	@Query(" select p from Program p where p.cpId = :cpId and p.onlineTime>= :startTime and p.onlineTime<= :endTime")
//	Page<Program> findByTimeAtOnLine(@Param("cpId") Long cpId,@Param("startTime") Date startTime,@Param("endTime") Date endTime,Pageable pageable);
//	
//	@Query(" select p from Program p where p.cpId = :cpId and p.createTime >= :startTime and p.createTime <= :endTime")
//	List<Program> findByCpIdAndPeriod(@Param("cpId") Long cpId, @Param("startTime") Date startTime,
//			@Param("endTime") Date endTime);
//	
//	@Query(" select p from Program p where p.status = 1 and p.cpId = :cpId and p.createTime >= :startTime and p.createTime <= :endTime")
//	List<Program> findByCpIdAndPeriodAndOnline(@Param("cpId") Long cpId, @Param("startTime") Date startTime,
//			@Param("endTime") Date endTime);



	@Cacheable
	@Query(" select p from Program p where p.seriesId = :seriesId and  p.status =1 order by episodeIndex ")
	Page<Program> findBySeriesIdPage(@Param("seriesId") Long seriesId, Pageable pageable);

	@Cacheable
	@Query(" select p from Program p where p.seriesId = :seriesId order by episodeIndex ")
	List<Program> findBySeriesId(@Param("seriesId") Long seriesId);

	@Cacheable
	@Query(" select p from Program p where p.seriesId = :seriesId and p.status = 1 order by episodeIndex ")
	List<Program> findBySeriesIdAndOnline(@Param("seriesId") Long seriesId);

	@Cacheable
	@Query(" select p from Program p where p.seriesId = :seriesId and p.status = 1 order by orgAirDate DESC")
	List<Program> findSeriesByTime(@Param("seriesId") Long seriesId);

	@Cacheable
	@Query(" select p from Program p where p.contentType =:contentType and p.status = 1")
	Page<Program> findByContentTypeAndOnline(@Param("contentType") Integer contentType, Pageable pageable);

	@Cacheable
	@Query(" select p from Program p where p.status = 1 and (p.actor LIKE  :actor  OR p.director LIKE  :actor)  ORDER BY p.year DESC ")
	Page<Program> findStarProgramByNameList(@Param("actor") String name, Pageable pageable);
		
	@Cacheable
	@Query(" select p from Program p where p.status = 1 and p.year =  :year and (p.actor LIKE  :actor  OR p.director LIKE  :actor) ORDER BY p.year DESC ")
	Page<Program> findStarProgramByYearList(@Param("year") String year,@Param("actor") String actor, Pageable pageable);
	
	@Cacheable
	@Query(" select p from Program p where p.searchName like :searchName and p.contentType = 1 and p.status = 1 ORDER BY p.searchName")
	Page<Program> findBySearchNameAndContentType(@Param("searchName") String searchName, Pageable pageable);

	@Cacheable
	@Query(" select p from Program p where p.status = 1 and p.type = 1 and p.searchName like :searchName ORDER BY p.updateTime DESC,p.rating DESC")
	Page<Program> findBySearchName(@Param("searchName") String searchName, Pageable pageable);

	@Cacheable
	@Query(" select p from Program p where p.status = 1 and p.type = 1 and p.contentType = :contentType and p.searchName like :searchName ORDER BY p.updateTime DESC,p.rating DESC")
	Page<Program> findBySearchNameAndContentType(@Param("searchName") String searchName,
			@Param("contentType") Integer contentType, Pageable pageable);

	@Cacheable
	@Query(" select p from Program p where p.actorPinyin like :actorPinyin and p.contentType = 1 and p.status = 1 ORDER BY p.actorPinyin")
	Page<Program> findByActorPinyinAndContentType(@Param("actorPinyin") String actorPinyin, Pageable pageable);

	@Cacheable
	@Query(" select p from Program p where p.keyword like :keyword and p.contentType = 1 and p.status = 1 ORDER BY p.searchName")
	Page<Program> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

	@Cacheable
	@Query(" select p from Program p where p.id != :id and p.tag like :tag and p.contentType =:contentType and p.status = 1")
	Page<Program> findByContentByOneTag(@Param("id") Long id, @Param("tag") String tag,
			@Param("contentType") Integer contentType, Pageable pageable);

	@Cacheable
	@Query(" select p from Program p where p.id != :id and (p.tag like :tag1 or p.tag like :tag2) and p.contentType =:contentType and p.status = 1")
	Page<Program> findByContentByTwoTag(@Param("id") Long id, @Param("tag1") String tag1,
			@Param("tag2") String tag2, @Param("contentType") Integer contentType, Pageable pageable);

	@Cacheable
	@Query(" select p from Program p where    p.seriesId=:seriesId  and  ( p.episodeIndex=:lastProgram or  p.episodeIndex=:nextProgram)   and p.type =2 and p.status = 1")
	Page<Program> getLastNextProgram(@Param("lastProgram") Integer lastProgram,
			@Param("nextProgram") Integer nextProgram, @Param("seriesId") Long seriesId, Pageable pageable);

	// 陕西EPG筛选相关方法
	// 按照“最热”进行排序
	@Cacheable
	@Query(" select p from Program p where p.contentType = 1 and p.status = 1 and p.tag like :type_two and p.area like :type_three and p.year like :type_four ORDER BY p.loveNum DESC")
	Page<Program> findByHeat(@Param("type_two") String type_two, @Param("type_three") String type_three,
			@Param("type_four") String type_four, Pageable pageable);

	// 按照“评分”进行排序
	@Cacheable
	@Query(" select p from Program p where p.contentType = 1 and p.status = 1 and p.tag like :type_two and p.area like :type_three and p.year like :type_four ORDER BY p.rating DESC")
	Page<Program> findByScore(@Param("type_two") String type_two, @Param("type_three") String type_three,
			@Param("type_four") String type_four, Pageable pageable);

	// 按照“最新”进行排序
	@Cacheable
	@Query(" select p from Program p where p.contentType = 1 and p.status = 1 and p.tag like :type_two and p.area like :type_three and p.year like :type_four ORDER BY p.orgAirDate DESC")
	Page<Program> findByDate(@Param("type_two") String type_two, @Param("type_three") String type_three,
			@Param("type_four") String type_four, Pageable pageable);

	/******************************** 分发/同步相关 begin ******************************/

	List<Program> findByCloudId(String cloudId);

	List<Program> findByCloudCode(String cloudCode);
	/******************************** 分发/同步相关 end ********************************/
	
	/**
	 *  Add by jnchen   
	 */
	@Query(" select p from Program p where p.contentType = 1 and p.status = 1 and p.tag like CONCAT('%',:tagName,'%') ORDER BY p.orgAirDate DESC ")
	Page<Program> findByTag(@Param("tagName") String tagName, Pageable pageable);

	@Query(" select p from Program p where p.contentType = 1 and p.status = 1 and p.actor like CONCAT('%',:starName,'%') ORDER BY p.orgAirDate DESC ")
	Page<Program> findByActor(@Param("starName") String starName,
			Pageable pageable);

	@Query(" select p from Program p where p.contentType = 1 and p.status = 1 and p.director like CONCAT('%',:starName,'%') ORDER BY p.orgAirDate DESC ")
	Page<Program> findByDirector(@Param("starName") String starName,
			Pageable pageable);
	
	@Cacheable
	@Query(" select p from Program p where p.status = 1 and p.contentType in :contentTypes order by onlineTime desc ")
	Page<Program> findByNewestProgram(
			@Param("contentTypes") List<Integer> contentTypes, Pageable pageable);

	/******************************** 相关推荐 start ********************************/
	@Cacheable
	@Query(" select p from Program p where p.status = 1 order by orgAirDate desc ")
	Page<Program> findByOrgAirDate(Pageable pageable);
	/******************************** 相关推荐 end ********************************/
	
	
	
}