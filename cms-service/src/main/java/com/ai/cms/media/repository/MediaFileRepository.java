package com.ai.cms.media.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.media.entity.MediaFile;
import com.ai.common.repository.AbstractRepository;

@Repository
@CacheConfig(cacheNames = "MediaFileRepository")
public interface MediaFileRepository extends
		AbstractRepository<MediaFile, Long> {

	MediaFile findOneById(Long id);

	@Cacheable
	MediaFile findOne(Long id);

	@Query(" select t from MediaFile t where t.programId = :programId order by t.bitrate desc, t.templateId ")
	List<MediaFile> findByProgramId(@Param("programId") Long programId);

	@Query(" select t from MediaFile t where t.seriesId = :seriesId order by t.episodeIndex, t.bitrate desc, t.templateId ")
	List<MediaFile> findBySeriesId(@Param("seriesId") Long seriesId);

	@Query(" select t from MediaFile t where t.programId = :programId and t.type = :type order by t.bitrate desc, t.templateId ")
	List<MediaFile> findByProgramId(@Param("programId") Long programId,
			@Param("type") Integer type);

	@Query(" select t from MediaFile t where t.seriesId = :seriesId and t.type = :type order by t.episodeIndex, t.bitrate desc, t.templateId ")
	List<MediaFile> findBySeriesId(@Param("seriesId") Long seriesId,
			@Param("type") Integer type);

	@Cacheable
	List<MediaFile> findByProgramIdAndTemplateIdAndType(Long programId,
			Long templateId, Integer type);

	@Cacheable
	List<MediaFile> findByProgramIdAndTemplateIdIn(Long programId,
			List<Long> templateIdList);

	@Cacheable
	@Query(" select t from MediaFile t where t.programId in :programIdList ")
	List<MediaFile> findByProgramIdListIn(
			@Param("programIdList") List<Long> programIdList);

	@Query(" select t from MediaFile t where t.filePath = :filePath ")
	List<MediaFile> findByFilePath(@Param("filePath") String filePath);

	@Modifying
	@Query(" delete from MediaFile t where t.programId = :programId ")
	void deleteByProgramId(@Param("programId") Long programId);

	@Modifying
	@Query(" delete from MediaFile t where t.programId = :programId and t.templateId = :templateId ")
	void deleteByProgramId(@Param("programId") Long programId,
			@Param("templateId") Long templateId);

	@Modifying
	@Query(" delete from MediaFile t where t.programId = :programId and t.templateId = :templateId and t.type = :type ")
	void deleteByProgramId(@Param("programId") Long programId,
			@Param("templateId") Long templateId, @Param("type") Integer type);

	@Modifying
	@Query(" delete from MediaFile t where t.programId in (select id from Program p where p.seriesId = :seriesId) or t.seriesId = :seriesId ")
	void deleteBySeriesId(@Param("seriesId") Long seriesId);

	/******************************** 分发/同步相关 begin ********************************/

	List<MediaFile> findByCloudId(String cloudId);

	List<MediaFile> findByCloudCode(String cloudCode);

	/******************************** 分发/同步相关 end ********************************/

}
