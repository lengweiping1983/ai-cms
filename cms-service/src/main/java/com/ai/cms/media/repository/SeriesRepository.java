package com.ai.cms.media.repository;

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
	
	@Cacheable
	Series findOne(Long id);
	
	@Cacheable
	List<Series> findAll(Iterable<Long> ids);
	
	@Query(" select count(p) from Series p where p.name = :name ")
	long countByName(@Param("name") String name);

	@Query(" select count(p) from Series p where p.name = :name and p.id != :id ")
	long countByNameAndNotId(@Param("name") String name, @Param("id") Long id);
	
	@Modifying
	@Query(" update Series t set t.templateId = :templateId where t.id = :id and t.templateId != :templateId")
	void updateTemplateIdById(@Param("id") Long id, @Param("templateId") String templateId);
	
	
	
	
	@Cacheable
    @Query(" select t from Series t where t.status = 1 and t.tag != '' ")
    List<Series> findAllSeries();

	
	
	
	@Query(" select p from Series p where p.name = :name ")
	List<Series> findByName(@Param("name") String name);
	
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
	@Query(" select p from Series p where p.contentType = 2 and p.status = 1 and p.tag like :type_two and p.area like :type_three and p.year like :type_four ORDER BY p.loveNum DESC")
	Page<Series> findByHeat(@Param("type_two") String type_two, @Param("type_three") String type_three,
			@Param("type_four") String type_four, Pageable pageable);

	// 按照“评分”进行排序
	@Cacheable
	@Query(" select p from Series p where p.contentType = 2 and p.status = 1 and p.tag like :type_two and p.area like :type_three and p.year like :type_four ORDER BY p.rating DESC")
	Page<Series> findByScore(@Param("type_two") String type_two, @Param("type_three") String type_three,
			@Param("type_four") String type_four, Pageable pageable);

	// 按照“最新”进行排序
	@Cacheable
	@Query(" select p from Series p where p.contentType = 2 and p.status = 1 and p.tag like :type_two and p.area like :type_three and p.year like :type_four ORDER BY p.orgAirDate DESC")
	Page<Series> findByDate(@Param("type_two") String type_two, @Param("type_three") String type_three,
			@Param("type_four") String type_four, Pageable pageable);

	/******************************** 查找所有的剧头 ********************************/
	@Query(" select p from  Series p")
	List<Series> fandalld();

	/******************************** 分发/同步相关 begin ******************************/

	List<Series> findByCloudId(String cloudId);

	List<Series> findByCloudCode(String cloudCode);
	/******************************** 分发/同步相关 end ********************************/


	@Cacheable
	@Query(" select p from Series p where p.status = 1 and p.searchName like :searchName ORDER BY p.updateTime DESC,p.rating DESC")
	Page<Series> findBySearchName(@Param("searchName") String searchName, Pageable pageable);

	@Cacheable
	@Query(" select p from Series p where p.status = 1 and p.contentType = :contentType and p.searchName like :searchName ORDER BY p.updateTime DESC,p.rating DESC")
	Page<Series> findBySearchNameAndContentType(@Param("searchName") String searchName,
			@Param("contentType") Integer contentType, Pageable pageable);
	
	@Cacheable
	@Query(" select p from Series p where p.status = 1 and p.contentType in :contentTypes order by onlineTime desc ")
	Page<Series> findByNewestSeries(
			@Param("contentTypes") List<Integer> contentTypes, Pageable pageable);


	/******************************** 相关推荐 start ********************************/
	@Query(" select s from Series s where s.contentType = 1 and s.status = 1 and s.tag like CONCAT('%',:tagName,'%') ORDER BY s.orgAirDate DESC ")
	Page<Series> findByTag(@Param("tagName") String tagName, Pageable pageable);

	/******************************** 相关推荐 end ********************************/
}