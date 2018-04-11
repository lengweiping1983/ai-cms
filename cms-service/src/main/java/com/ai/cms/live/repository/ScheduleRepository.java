package com.ai.cms.live.repository;

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

import com.ai.cms.live.entity.Schedule;
import com.ai.cms.media.entity.Program;
import com.ai.common.repository.AbstractRepository;

@Repository
@CacheConfig(cacheNames = "ScheduleRepository")
public interface ScheduleRepository extends AbstractRepository<Schedule, Long> {

	@Cacheable
	Schedule findOne(Long id);
	
	@Cacheable
	List<Schedule> findAll(Iterable<Long> ids);

	@Modifying
	@Query(" delete from Schedule t where t.channelId = :channelId")
	void deleteByChannelId(@Param("channelId") Long channelId);

	@Cacheable
	@Query(" select t from Schedule t where t.channelId = :channelId and t.status = 1 and t.beginTime > :beginTime order by t.beginTime ")
	List<Schedule> findLatestByChannelIdAndOnline(@Param("channelId") Long channelId,
			@Param("beginTime") Date beginTime);

//	@Cacheable
//	@Query(" select t from Schedule t, Program p where t.channelId = :channelId and t.status = 1 and p.status = 1 "
//			+ " and t.splitProgram = 1 and (t.programId = p.id or (t.mediaId = p.mediaId and t.mediaEpisode = p.episodeIndex)) order by t.beginTime desc ")
//	List<Schedule> findLatestSchedule(@Param("channelId") Long channelId);
//
//	@Cacheable
//	@Query(" select p from Schedule t, Program p where t.channelId = :channelId and t.id = :scheduleId and t.status = 1 and p.status = 1 "
//			+ " and t.splitProgram = 1 and (t.programId = p.id or (t.mediaId = p.mediaId and t.mediaEpisode = p.episodeIndex)) ")
//	List<Program> findLatestProgram(@Param("channelId") Long channelId, @Param("scheduleId") Long scheduleId);

	@Cacheable
	@Query(" select t from Schedule t where t.channelId in :channelId and t.status = 1 and t.beginTime >= :beginTime and t.beginTime < :endTime order by t.beginTime ")
	Page<Schedule> findProgramByChannelIdAndDate(@Param("channelId") Long channelId,
			@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, Pageable pageable);
	
	
	/******************************** 直播加点播 ****************************** begin ***/
	@Cacheable
	@Query(" select t from Schedule t where t.id in :scheduleIdList and t.status = 1 and t.beginTime <= :beginTime and t.endTime > :endTime order by t.beginTime ")
	List<Schedule> findLatestByScheduleIdListAndNotEnd(
			@Param("scheduleIdList") List<Long> scheduleIdList,
			@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

	@Cacheable
	@Query(" select t from Schedule t where t.id in :scheduleIdList and t.status = 1 and t.beginTime <= :beginTime and "
			+ " t.endTime > :endTime and t.id != :scheduleId order by t.beginTime ")
	List<Schedule> findLatestByScheduleIdListAndNotEnd(
			@Param("scheduleIdList") List<Long> scheduleIdList,
			@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,
			@Param("scheduleId") Long scheduleId);
	
	/******************************** 直播加点播 ****************************** end ***/

	@Cacheable
	@Query(" select t from Schedule t where t.channelId in :channelId and t.status = 1 order by t.beginTime ")
	List<Schedule> findSchedulesByChannelId(@Param("channelId") Long channelId);
	
	@Cacheable
	@Query(" select t from Schedule t where t.channelId = :channelId and t.beginTime = :beginTime and t.status = 1 ")
	List<Schedule> findByChannelIdAndBeginTime(@Param("channelId") Long channelId, @Param("beginTime") Date beginTime);
}
