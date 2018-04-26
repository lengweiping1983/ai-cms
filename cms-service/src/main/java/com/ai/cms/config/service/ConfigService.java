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

	public Map<String, Cp> findAllCpMap() {
		Map<String, Cp> cpMap = new HashMap<String, Cp>();
		List<Cp> cpList = cpRepository.findAll();
		for (Cp cp : cpList) {
			cpMap.put(cp.getCode(), cp);
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

	public String getCpShortNameByCpCode(Map<String, Cp> cpMap, String cpCodes) {
		StringBuffer result = new StringBuffer();
		if (StringUtils.isNotEmpty(cpCodes)) {
			String[] cpCodeArr = cpCodes.split(",");
			for (String cpCode : cpCodeArr) {
				Cp cp = cpMap.get(cpCode);
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

	public String getCpNameByCpCode(Map<String, Cp> cpMap, String cpCodes) {
		StringBuffer result = new StringBuffer();
		if (StringUtils.isNotEmpty(cpCodes)) {
			String[] cpCodeArr = cpCodes.split(",");
			for (String cpCode : cpCodeArr) {
				Cp cp = cpMap.get(cpCode);
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

	public String getCpCodeByCpName(Map<String, Cp> cpMap, String cpNames) {
		StringBuffer result = new StringBuffer();
		if (StringUtils.isNotEmpty(cpNames)) {
			String[] cpNameArr = cpNames.split(",");
			for (String cpName : cpNameArr) {
				Cp cp = cpMap.get(cpName);
				if (cp != null) {
					if (result.toString().length() > 0) {
						result.append(",");
					}
					result.append(cp.getCode());
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

	public CpFtp findCpFtpByCpCode(String cpCode) {
		if (StringUtils.isNotEmpty(cpCode)) {
			return cpFtpRepository.findOneByCpCode(cpCode);
		}
		return null;
	}
	
	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void deleteCp(Cp cp) {
		if (cp != null) {
			cpFtpRepository.deleteByCpCode(cp.getCode());
			cpRepository.delete(cp);
		}
	}

}