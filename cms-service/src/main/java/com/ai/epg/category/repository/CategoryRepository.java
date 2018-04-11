package com.ai.epg.category.repository;

import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.category.entity.Category;

@Repository
@CacheConfig(cacheNames = "CategoryRepository")
public interface CategoryRepository extends AbstractRepository<Category, Long> {

	@Cacheable
	Category findOne(Long id);

	@Cacheable
	List<Category> findAll(Iterable<Long> ids);
	
	@Cacheable
	Category findOneByAppCodeAndCode(String appCode, String code);
	
	@Cacheable
	List<Category> findByAppCodeAndCodeIn(String appCode, List<String> codeList);
	
	@Cacheable
    @Query(" select t from Category t where t.status = 1 ")
    List<Category> findAllCategory();

	@Query(" select c from Category c where c.parentId = :parentId order by c.sortIndex ")
	List<Category> findAllByParentId(@Param("parentId") Long parentId);

	@Cacheable
	@Query(" select c from Category c where c.parentId = :parentId and c.status = 1 order by c.sortIndex ")
	List<Category> findByParentId(@Param("parentId") Long parentId);

    @Cacheable
    @Query(" select c from Category c where c.parentId = :parentId and c.status = 1 order by c.sortIndex desc ")
    List<Category> findByParentIdDesc(@Param("parentId") Long parentId);

	@Cacheable
	@Query(" select c from Category c where c.appCode = :appCode order by c.parent, c.sortIndex ")
	List<Category> findByAppCode(@Param("appCode") String appCode);

	@Cacheable
	@Query(" select c from Category c where c.appCode = :appCode and c.parentId is null order by c.sortIndex ")
	List<Category> findRootNodeByAppCode(@Param("appCode") String appCode);

	@Cacheable
	@Query(" select t from Category t where t.parentId in :parentIdList and t.status = 1 "
			+ " order by t.sortIndex ")
	List<Category> findSubListByParentIdList(@Param("parentIdList") List<Long> parentIdList);
		
	/******************************** 分发相关 begin ********************************/

	@Modifying
	@Query(" update Category t set t.injectionStatus = :injectionStatus, t.injectionTime = :injectionTime, t.partnerItemCode = :partnerItemCode where t.id = :id ")
	void updateInjectionStatus(@Param("id") Long id,
			@Param("injectionStatus") Integer injectionStatus,
			@Param("injectionTime") Date injectionTime, 
			@Param("partnerItemCode") String partnerItemCode);

	/******************************** 分发相关 end ********************************/
}
