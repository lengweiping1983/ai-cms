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

import com.ai.cms.media.entity.Series;
import com.ai.common.repository.AbstractRepository;

@Repository
@CacheConfig(cacheNames = "SeriesRepository")
public interface SeriesRepository extends AbstractRepository<Series, Long> {

	Series findOneById(Long id);
	
	@Query(" select p from Series p where p.code = :code ")
	Series findOneByCode(@Param("code") String code);

	@Query(" select p from Series p where p.code = :code and p.status = 1 ")
	Series findOneByCodeAndOnline(@Param("code") String code);
	
	@Cacheable
	Series findOne(Long id);
	
	@Cacheable
	List<Series> findAll(Iterable<Long> ids);
	
	@Query(" select p from Series p where p.title = :title ")
	List<Series> findByTitle(@Param("title") String title);

	@Query(" select p from Series p where p.cpId = :cpId")
	List<Series> findByCpId(@Param("cpId") Long cpId);
	
	@Query(" select p from Series p where p.cpId = :cpId")
	Page<Series> findByCpIdPage(@Param("cpId") Long cpId,Pageable pageable);
	
	@Query(" select p from Series p where p.cpId = :cpId and p.createTime<= :createTime ")
	Page<Series> findByCpIdPageByCreateTime(@Param("createTime") Date createTime,@Param("cpId") Long cpId,Pageable pageable);
	
	@Query(" select p from Series p where p.createTime<= :createTime ")
	Page<Series> findByCreateTime(@Param("createTime") Date createTime,Pageable pageable);
	
	@Query(" select p from Series p where p.cpId = :cpId and ( p.createTime>= :startTime and p.createTime<= :endTime or p.updateTime>= :startTime and p.updateTime<= :endTime )")
	Page<Series> findByTime(@Param("cpId") Long cpId,@Param("startTime") Date startTime,@Param("endTime") Date endTime,Pageable pageable);
	 
	@Query(" select p from Series p where p.cpId = :cpId and p.onlineTime>= :startTime and p.onlineTime<= :endTime")
	Page<Series> findByTimeAtOnLine(@Param("cpId") Long cpId,@Param("startTime") Date startTime,@Param("endTime") Date endTime,Pageable pageable);
	
	@Query(" select p from Series p where p.id in :idList ")
    List<Series> findByListIn(@Param("idList") List<Long> idList);
	
	@Query(" select p from Series p where p.status =1 and p.id = :id")
	Series findOneByIdAndStatus(@Param("id") Long id);

	@Query(" select p from Series p where p.contentType = :contentType and p.status = 1 ")
	Page<Series> findByContentTypeAndOnline(Integer contentType, Pageable pageable);
	
	@Cacheable
	@Query(" select p from Series p where p.status = 1 and p.contentType = 3 order by rating DESC ")
	Page<Series> findRecommBySeries(Pageable pageable);

	// 陕西EPG筛选相关方法
	// 按照“最热”进行排序
	@Cacheable
	@Query(" select p from Series p where p.contentType = 2 and p.status = 1 and p.tags like :type_two and p.area like :type_three and p.year like :type_four ORDER BY p.loveNum DESC")
	Page<Series> findByHeat(@Param("type_two") String type_two, @Param("type_three") String type_three,
			@Param("type_four") String type_four, Pageable pageable);

	// 按照“评分”进行排序
	@Cacheable
	@Query(" select p from Series p where p.contentType = 2 and p.status = 1 and p.tags like :type_two and p.area like :type_three and p.year like :type_four ORDER BY p.rating DESC")
	Page<Series> findByScore(@Param("type_two") String type_two, @Param("type_three") String type_three,
			@Param("type_four") String type_four, Pageable pageable);

	// 按照“最新”进行排序
	@Cacheable
	@Query(" select p from Series p where p.contentType = 2 and p.status = 1 and p.tags like :type_two and p.area like :type_three and p.year like :type_four ORDER BY p.orgAirDate DESC")
	Page<Series> findByDate(@Param("type_two") String type_two, @Param("type_three") String type_three,
			@Param("type_four") String type_four, Pageable pageable);

	/******************************** 查找所有的剧头 ********************************/
	@Query(" select p from  Series p")
	List<Series> fandalld();

	/********************************
	 * 注入相关 begin
	 ********************************/

	List<Series> findByCloudId(String cloudId);

	List<Series> findByCloudCode(String cloudCode);

	@Modifying
	@Query(" update Series t set t.injectionStatus = :injectionStatus, t.injectionTime = :injectionTime, t.partnerItemCode = :partnerItemCode where t.id = :id ")
	void updateInjectionStatus(@Param("id") Long id, @Param("injectionStatus") Integer injectionStatus,
			@Param("injectionTime") Date injectionTime,
			@Param("partnerItemCode") String partnerItemCode);

	/******************************** 注入相关 end ********************************/

	/******************************** 修改剧头中的tags ********************************/
	@Modifying
	@Query(" update Series t set t.tags = :tags where t.code= :code")
	void updateTagsInfo(@Param("code") String code, @Param("tags") String tags);

	@Cacheable
	@Query(" select p from Series p where p.status = 1 and p.searchName like :searchName ORDER BY p.updateTime DESC,p.rating DESC")
	Page<Series> findBySearchName(@Param("searchName") String searchName, Pageable pageable);

	@Cacheable
	@Query(" select p from Series p where p.status = 1 and p.contentType = :contentType and p.searchName like :searchName ORDER BY p.updateTime DESC,p.rating DESC")
	Page<Series> findBySearchNameAndContentType(@Param("searchName") String searchName,
			@Param("contentType") Integer contentType, Pageable pageable);

	@Cacheable
	@Query(" select p from Series p where p.status = 1 and p.searchName like :searchName ORDER BY p.updateTime DESC,p.rating DESC")
	Page<Series> findBySearchNameAndContentType(@Param("searchName") String searchName, Pageable pageable);


	/******************************** 福建搜索相关 start ********************************/

	@Query(" select p from Series p where p.partnerItemCode = :partnerItemCode")
	Series findByPartnerItemCode(@Param("partnerItemCode") String partnerItemCode);

	/******************************** 福建搜索相关 end ********************************/
}