package com.ai.epg.album.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.album.entity.Album;

@Repository
@CacheConfig(cacheNames = "AlbumRepository")
public interface AlbumRepository extends AbstractRepository<Album, Long> {

	@Cacheable
	Album findOne(Long id);
	
	@Cacheable
	List<Album> findAll(Iterable<Long> ids);
	
	@Cacheable
    Album findOneByAppCodeAndCode(String appCode, String code);
	
	@Cacheable
    @Query(" select t from Album t where t.status = 1 ")
    List<Album> findAllAlbum();

	@Cacheable
    @Query(" select t from Album t where t.code = :code and (t.appCode is null or t.appCode = '' ) ")
    List<Album> findCode(@Param("code") String code);

	@Query(" select t from Album t where t.id= :id or t.code = :code ")
	Album getAlbumByIdOrCode(@Param("id") Long id, @Param("code") String code);
	
    @Query(" select t from Album t where t.code = :code ")
    List<Album> findAllCode(@Param("code") String code);
}
