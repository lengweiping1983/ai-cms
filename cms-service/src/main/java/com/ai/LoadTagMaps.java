package com.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.Series;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.cms.media.repository.SeriesRepository;

@Component
@CacheConfig(cacheNames = "LoadTagMaps")
public class LoadTagMaps {

	private static final Logger logger = LoggerFactory.getLogger(AppGlobal.class);

	private static ProgramRepository pRepository;
	
	private static SeriesRepository sRepository;
	
	@Autowired
	public void setpRepository(ProgramRepository pRepository) {
		LoadTagMaps.pRepository = pRepository;
	}

	@Autowired
	public void setsRepository(SeriesRepository sRepository) {
		LoadTagMaps.sRepository = sRepository;
	}

	public static Map<String, List<String>> pMaps = new HashMap<String, List<String>>();
	public static Map<String, List<String>> sMaps = new HashMap<String, List<String>>();

	public static String[] DEFAULT_SEPARATE_CHAR_ARR = { ";", ",", "，", "\\|", "｜", " " };

	public static String replaceSep(String source, String[] sepArr, String sep) {
		if (StringUtils.isEmpty(source)) {
			return source;
		}
		String result = source;
		for (String str : sepArr) {
			result = result.replace(str, ",");
		}
		return result;
	}

	public static void putPragramData(String tag, String id) {
		for (String str : tag.split(",")) {
			if (pMaps.get(str) == null) {
				List<String> ids = new ArrayList<String>();
				ids.add(id);
				pMaps.put(StringUtils.trimToEmpty(tag), ids);
				ids = null;
			} else {
				pMaps.get(str).add(id);
			}
		}
	}

	public static void putSeriesData(String tag, String id) {
		for (String str : tag.split(",")) {
			if (sMaps.get(str) == null) {
				List<String> programs = new ArrayList<String>();
				programs.add(id);
				sMaps.put(StringUtils.trimToEmpty(tag), programs);
				programs = null;
			} else {
				sMaps.get(str).add(id);
			}
		}
	}

	@Cacheable
	public static void LoadPrograms() {
		List<Program> pLists = pRepository.findAllProgram();
		if (pLists != null) {
			logger.info("load programTagMap start...");
			for (int i = 0; i < pLists.size(); i++) {
				Program program = pLists.get(i);
				if (program.getStatus() == 1 && program.getTag() != null && program.getTag() != "") {
					String tag = program.getTag();
					replaceSep(tag, DEFAULT_SEPARATE_CHAR_ARR, ",");
					putPragramData(tag, program.getId().toString());
				}
			}
		}
		pLists = null;
	}

	@Cacheable
	public static void LoadSeries() {
		List<Series> sLists = sRepository.findAllSeries();
		if (sLists != null) {
			logger.info("load seriesTagMap start...");
			for (int i = 0; i < sLists.size(); i++) {
				Series series = sLists.get(i);
				if (series.getStatus() == 1 && series.getTag() != null && series.getTag() != "") {
					String tag = series.getTag();
					replaceSep(tag, DEFAULT_SEPARATE_CHAR_ARR, ",");
					putSeriesData(tag, series.getId().toString());
				}
			}
		}
		sLists = null;
	}

}
