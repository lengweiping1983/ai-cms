package com.ai.cms.injection.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.injection.entity.DownloadTask;
import com.ai.common.repository.AbstractRepository;

@Repository
public interface DownloadTaskRepository extends
		AbstractRepository<DownloadTask, Long> {

	Page<DownloadTask> findByStatus(Integer status, Pageable pageable);

	@Query(" select t from DownloadTask t where (t.status = 1 or t.status = 6 "
			+ " or ((t.status = 4 or t.status = 5) and t.requestTimes < :requestTimes)) "
			+ " order by priority desc, createTime ")
	Page<DownloadTask> findByRequestTimes(
			@Param("requestTimes") Integer requestTimes, Pageable pageable);

	@Query(" select t from DownloadTask t where t.inputFilePath is not null and t.inputFilePath != '' "
			+ " and (t.sourceFileMd5 is null or t.sourceFileMd5 = '') ")
	Page<DownloadTask> findByNotFileMd5(Pageable pageable);

	@Query(" select count(t) from DownloadTask t where (t.status = 4 or t.status = 5 or t.status = 8 or t.status = 9) ")
	long countTaskFailNum();

	long countByModuleAndPid(Integer module, Long pid);

	long countByModuleAndPidAndStatus(Integer module, Long pid, Integer status);
	
	@Query(" select count(t) from DownloadTask t where t.module = :module and t.pid = :pid "
			+ " and (t.status = 4 or t.status = 5 or t.status = 8 or t.status = 9) ")
	long countFailNumByModuleAndPid(@Param("module") Integer module,
			@Param("pid") Long pid);

	List<DownloadTask> findByResponseCode(
			@Param("responseCode") String responseCode);

	List<DownloadTask> findByProgramId(@Param("programId") Long programId);

	List<DownloadTask> findByMediaFileId(@Param("mediaFileId") Long mediaFileId);

	List<DownloadTask> findByInputFilePath(
			@Param("inputFilePath") String inputFilePath);

	List<DownloadTask> findByOutputFilePath(
			@Param("outputFilePath") String outputFilePath);

	List<DownloadTask> findByInputFilePathAndOutputFilePath(
			@Param("inputFilePath") String inputFilePath,
			@Param("outputFilePath") String outputFilePath);

	List<DownloadTask> findByModuleAndPidAndStatusAndMediaFileIdAndInputFilePath(
			Integer module, Long pid, Integer status, Long mediaFileId,
			String inputFilePath);

	@Modifying
	@Query(" delete from DownloadTask t where t.programId = :programId ")
	void deleteByProgramId(@Param("programId") Long programId);

	@Modifying
	@Query(" delete from DownloadTask t where t.mediaFileId = :mediaFileId ")
	void deleteByMediaFileId(@Param("mediaFileId") Long mediaFileId);

	@Modifying
	@Query(" update DownloadTask t set t.status = 1 where t.status = 2 ")
	void resetDownloadingStatus();

}