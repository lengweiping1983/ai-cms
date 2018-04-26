package com.ai.cms.config.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.cms.config.entity.Cp;
import com.ai.cms.config.entity.CpFtp;
import com.ai.cms.config.entity.MediaTemplate;
import com.ai.cms.config.entity.Site;
import com.ai.cms.config.repository.CpFtpRepository;
import com.ai.cms.config.repository.CpRepository;
import com.ai.cms.config.repository.MediaTemplateRepository;
import com.ai.cms.config.repository.SiteRepository;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;

@Service
@Transactional(value = "slaveTransactionManager", readOnly = true)
public class ConfigService extends AbstractService<Cp, Long> {

	@Autowired
	private SiteRepository siteRepository;

	@Autowired
	private CpRepository cpRepository;

	@Autowired
	private CpFtpRepository cpFtpRepository;

	@Autowired
	private MediaTemplateRepository mediaTemplateRepository;

	@Override
	public AbstractRepository<Cp, Long> getRepository() {
		return cpRepository;
	}

	public List<Site> findAllSite() {
		return siteRepository.findAll();
	}

	public Cp findOneCpByCode(String code) {
		return cpRepository.findOneByCode(code);
	}

	public List<Cp> findAllCp() {
		return cpRepository.findAll();
	}

	public List<MediaTemplate> findAllMediaTemplate() {
		return mediaTemplateRepository.findAll();
	}

	public Map<Long, Cp> findAllCpMap() {
		Map<Long, Cp> cpMap = new HashMap<Long, Cp>();
		List<Cp> cpList = cpRepository.findAll();
		for (Cp cp : cpList) {
			cpMap.put(cp.getId(), cp);
		}
		return cpMap;
	}

	public Map<String, Cp> findAllCpNameMap() {
		Map<String, Cp> cpMap = new HashMap<String, Cp>();
		List<Cp> cpList = cpRepository.findAll();
		for (Cp cp : cpList) {
			cpMap.put(cp.getShortName(), cp);
		}
		for (Cp cp : cpList) {
			cpMap.put(cp.getName(), cp);
		}
		return cpMap;
	}

	public String getCpShortNameByCpId(Map<Long, Cp> cpMap, String cpIds) {
		StringBuffer result = new StringBuffer();
		if (StringUtils.isNotEmpty(cpIds)) {
			String[] cpIdArr = cpIds.split(",");
			for (String cpId : cpIdArr) {
				Cp cp = cpMap.get(Long.valueOf(cpId));
				if (cp != null) {
					if (result.toString().length() > 0) {
						result.append(",");
					}
					result.append(cp.getShortName());
				}
			}
		}
		return result.toString();
	}

	public String getCpNameByCpId(Map<Long, Cp> cpMap, String cpIds) {
		StringBuffer result = new StringBuffer();
		if (StringUtils.isNotEmpty(cpIds)) {
			String[] cpIdArr = cpIds.split(",");
			for (String cpId : cpIdArr) {
				Cp cp = cpMap.get(Long.valueOf(cpId));
				if (cp != null) {
					if (result.toString().length() > 0) {
						result.append(",");
					}
					result.append(cp.getName());
				}
			}
		}
		return result.toString();
	}

	public String getCpIdByCpName(Map<String, Cp> cpMap, String cpNames) {
		StringBuffer result = new StringBuffer();
		if (StringUtils.isNotEmpty(cpNames)) {
			String[] cpNameArr = cpNames.split(",");
			for (String cpName : cpNameArr) {
				Cp cp = cpMap.get(cpName);
				if (cp != null) {
					if (result.toString().length() > 0) {
						result.append(",");
					}
					result.append(cp.getId());
				}
			}
		}
		return result.toString();
	}

	public Map<Long, MediaTemplate> findAllMediaTemplateMap() {
		Map<Long, MediaTemplate> mediaTemplateMap = new HashMap<Long, MediaTemplate>();
		List<MediaTemplate> mediaTemplateList = mediaTemplateRepository
				.findAll();
		for (MediaTemplate mediaTemplate : mediaTemplateList) {
			mediaTemplateMap.put(mediaTemplate.getId(), mediaTemplate);
		}
		return mediaTemplateMap;
	}

	public String getTemplateCodeByTemplateId(
			Map<Long, MediaTemplate> mediaTemplateMap, String templateIds) {
		StringBuffer result = new StringBuffer();
		if (StringUtils.isNotEmpty(templateIds)) {
			String[] mediaTemplateIdArr = templateIds.split(",");
			for (String mediaTemplateId : mediaTemplateIdArr) {
				MediaTemplate mediaTemplate = mediaTemplateMap.get(Long
						.valueOf(mediaTemplateId));
				if (mediaTemplate != null) {
					if (result.toString().length() > 0) {
						result.append(",");
					}
					result.append(mediaTemplate.getCode());
				}
			}
		}
		return result.toString();
	}

	public String getTemplateTitleByTemplateId(
			Map<Long, MediaTemplate> mediaTemplateMap, String templateIds) {
		StringBuffer result = new StringBuffer();
		if (StringUtils.isNotEmpty(templateIds)) {
			String[] mediaTemplateIdArr = templateIds.split(",");
			for (String mediaTemplateId : mediaTemplateIdArr) {
				MediaTemplate mediaTemplate = mediaTemplateMap.get(Long
						.valueOf(mediaTemplateId));
				if (mediaTemplate != null) {
					if (result.toString().length() > 0) {
						result.append(",");
					}
					result.append(mediaTemplate.getTitle());
				}
			}
		}
		return result.toString();
	}

	public CpFtp findCpFtpByCpId(Long cpId) {
		return cpFtpRepository.findOneByCpId(cpId);
	}

}