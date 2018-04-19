package com.ai.cms.media.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.media.entity.MediaImage;
import com.ai.common.repository.AbstractRepository;

@Repository
public interface MediaImageRepository extends
		AbstractRepository<MediaImage, Long> {

	@Query(" select t from MediaImage t where t.programId = :programId and t.type = :type ")
	List<MediaImage> findByProgramIdAndType(@Param("programId") Long programId,
			@Param("type") Integer type);

	@Query(" select max(t.sortIndex) from MediaImage t where t.programId = :programId and t.type = :type ")
	Integer findMaxSortIndexByProgramIdAndType(
			@Param("programId") Long programId, @Param("type") Integer type);

	@Query(" select t from MediaImage t where t.seriesId = :seriesId and t.type = :type ")
	List<MediaImage> findBySeriesIdAndType(@Param("seriesId") Long seriesId,
			@Param("type") Integer type);

	@Query(" select max(t.sortIndex) from MediaImage t where t.seriesId = :seriesId and t.type = :type ")
	Integer findMaxSortIndexBySeriesIdAndType(@Param("seriesId") Long seriesId,
			@Param("type") Integer type);

	@Modifying
	@Query(" delete from MediaImage t where t.programId = :programId ")
	void deleteByProgramId(@Param("programId") Long programId);

	@Modifying
	@Query(" delete from MediaImage t where t.seriesId = :seriesId ")
	void deleteBySeriesId(@Param("seriesId") Long seriesId);
	
	/******************************** 分发/同步相关 begin ********************************/

	List<MediaImage> findByCloudId(String cloudId);

	List<MediaImage> findByCloudCode(String cloudCode);

	/******************************** 分发/同步相关 end ********************************/

}
