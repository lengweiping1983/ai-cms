package com.ai.cms.media.repository;

import java.util.Date;
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
	
	MediaFile findOneByCode(String code);
	
	@Cacheable
	MediaFile findOne(Long id);

	@Modifying
	@Query(" delete from MediaFile t where t.programId in (select id from Program p where p.seriesId = :seriesId) or t.seriesId = :seriesId ")
	void deleteBySeriesId(@Param("seriesId") Long seriesId);

	@Modifying
	@Query(" delete from MediaFile t where t.programId = :programId")
	void deleteByProgramId(@Param("programId") Long programId);
	
    @Modifying
    @Query(" update MediaFile t set t.cpId = :toCpId where t.programId = :programId ")
    void updateCpIdByProgramId(@Param("programId") Long programId, @Param("toCpId") Long toCpId);

    @Modifying
    @Query(" update MediaFile t set t.cpId = :toCpId where (t.programId in (select id from Program p where p.seriesId = :seriesId) or t.seriesId = :seriesId )")
    void updateCpIdBySeriesId(@Param("seriesId") Long seriesId, @Param("toCpId") Long toCpId);
    
	@Cacheable
	List<MediaFile> findByProgramId(Long programId);

	@Cacheable
	List<MediaFile> findByProgramIdAndType(Long programId, Integer type);

	@Cacheable
	@Query(" select f from MediaFile f where f.programId in :programIdList ")
	List<MediaFile> findByProgramIdListIn(
			@Param("programIdList") List<Long> programIdList);
	
	@Query(" select f from MediaFile f where f.filePath = :filePath ")
	List<MediaFile> findByFilePath(@Param("filePath") String filePath);

	/******************************** 注入相关 begin ********************************/

	List<MediaFile> findByCloudId(String cloudId);

	List<MediaFile> findByCloudCode(String cloudCode);

	@Query(" select t from MediaFile t where t.programId = :programId and (t.injectionStatus = 1 or t.injectionStatus = 2 or t.injectionStatus = 5 or t.injectionStatus = 7) ")
	List<MediaFile> findByProgramIdAndWaitInjection(
			@Param("programId") Long programId);

	@Query(" select t from MediaFile t where t.programId = :programId and (t.injectionStatus = 3 or t.injectionStatus = 8) ")
	List<MediaFile> findByProgramIdAndInjected(
			@Param("programId") Long programId);

	@Modifying
	@Query(" update MediaFile t set t.injectionStatus = :injectionStatus, t.injectionTime = :injectionTime, t.cdnFileCode = :partnerItemCode where t.programId = :programId and type = 1 ")
	void updateInjectionStatusByProgramId(@Param("programId") Long programId,
			@Param("injectionStatus") Integer injectionStatus,
			@Param("injectionTime") Date injectionTime,
			@Param("partnerItemCode") String partnerItemCode);
	
	@Modifying
	@Query(" update MediaFile t set t.injectionStatus = :injectionStatus, t.injectionTime = :injectionTime, t.partnerItemCode = :partnerItemCode where t.id = :id ")
	void updateInjectionStatus(@Param("id") Long id,
			@Param("injectionStatus") Integer injectionStatus,
			@Param("injectionTime") Date injectionTime,
			@Param("partnerItemCode") String partnerItemCode);
	
	@Modifying
	@Query(" update MediaFile t set t.injectionStatus = :injectionStatus, t.injectionTime = :injectionTime, t.partnerItemCode = :partnerItemCode, t.cdnFileCode = :partnerItemCode where t.id = :id ")
	void updateInjectionStatus2(@Param("id") Long id,
			@Param("injectionStatus") Integer injectionStatus,
			@Param("injectionTime") Date injectionTime,
			@Param("partnerItemCode") String partnerItemCode);

	/******************************** 注入相关 end ********************************/

}
