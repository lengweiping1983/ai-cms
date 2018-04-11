package com.ai.epg.template.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;
import com.ai.epg.template.entity.Marker;
import com.ai.epg.template.repository.MarkerRepository;

@Service
public class MarkerService extends AbstractService<Marker, Long> {

	@Autowired
	private MarkerRepository markerRepository;

	@Override
	public AbstractRepository<Marker, Long> getRepository() {
		return markerRepository;
	}

	// @Cacheable
	public Map<String, String> getMarkerMap() {
		List<Marker> markerList = markerRepository.findAll();
		Map<String, String> markerMap = new HashMap<String, String>();
		for (Marker m : markerList) {
			markerMap.put(m.getCode(), m.getUrl());
		}
		return markerMap;
	}
}
