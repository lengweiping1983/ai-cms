package com.ai.cms.live.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.live.entity.Channel;
import com.ai.common.repository.AbstractRepository;

@Repository
@CacheConfig(cacheNames = "ChannelRepository")
public interface ChannelRepository extends AbstractRepository<Channel, Long> {

	@Cacheable
	Channel findOne(Long id);

	@Cacheable
	List<Channel> findAll(Iterable<Long> ids);
	
	@Cacheable
	Channel findOneByCode(String code);

	@Cacheable
	@Query(" select t from Channel t where t.status = 1 and t.type = 1 order by t.sortIndex ")
	List<Channel> findLiveChannelList();
	
	@Cacheable
	@Query(" select t.id from Channel t where t.status = 1 and t.type = 1 and t.code like :code order by t.sortIndex ")
	List<Long> findIdListByCode(@Param("code") String code);

	@Cacheable
	@Query(" select t from Channel t where t.status = 1 order by t.sortIndex ")
	Page<Channel> findPageList(Pageable pageable);
}
