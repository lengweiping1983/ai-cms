package com.ai.cms.live.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ai.cms.live.entity.ChannelConfig;
import com.ai.common.repository.AbstractRepository;

@Repository
@CacheConfig(cacheNames = "ChannelConfigRepository")
public interface ChannelConfigRepository extends
		AbstractRepository<ChannelConfig, Long> {

	@Cacheable
	ChannelConfig findOne(Long id);
	
	@Cacheable
	List<ChannelConfig> findByChannelIdIn(List<Long> channelConfigIdList);
	
	@Modifying
	@Query(" delete from ChannelConfig t where t.channelId = :channelId ")
	void deleteByChannelId(@Param("channelId") Long channelId);

	@Cacheable
	@Query(" select t from ChannelConfig t where t.channelId = :channelId ")
	List<ChannelConfig> findByChannelId(@Param("channelId") Long channelId);

	@Query(" select t from ChannelConfig t where t.channelId = :channelId and t.providerType = :providerType ")
	ChannelConfig findByChannelIdAndProviderType(
			@Param("channelId") Long channelId,
			@Param("providerType") Integer providerType);

	@Cacheable
	@Query(" select t from ChannelConfig t where t.status = 1 order by t.sortIndex ")
	List<ChannelConfig> findAllChannelConfig();

	@Cacheable
	@Query(" select t from ChannelConfig t where t.status = 1 and t.channelId = :channelId order by t.sortIndex ")
	ChannelConfig findOneByChannelId(@Param("channelId") Long channelId);

}
