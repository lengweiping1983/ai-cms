package com.ai.epg.product.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ai.common.repository.AbstractRepository;
import com.ai.epg.product.entity.ServiceItemView;

@Repository
public interface ServiceItemViewRepository extends
		AbstractRepository<ServiceItemView, Long> {

	ServiceItemView findOne(Long id);

	List<ServiceItemView> findByIdIn(List<Long> idList);

}
