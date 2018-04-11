package com.ai.epg.template.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;
import com.ai.epg.template.entity.Template;
import com.ai.epg.template.entity.TemplateParam;
import com.ai.epg.template.repository.TemplateParamRepository;
import com.ai.epg.template.repository.TemplateRepository;



@Service
@Transactional(readOnly = true)
public class TemplateService extends AbstractService<Template, Long> {

	@Autowired
	TemplateParamRepository templateParamRepository;

	@Autowired
	TemplateRepository templateRepository;

	@Override
	public AbstractRepository<Template, Long> getRepository() {
		return templateRepository;
	}

	public List<TemplateParam> getTempalteParamList(String templateCode) {
		if (StringUtils.isEmpty(templateCode)) {
			return null;
		}

		List<Template> templateList = templateRepository
				.findByCode(templateCode);
		if (templateList != null && templateList.size() > 0) {
			return templateParamRepository.findAllByTemplateId(templateList
					.get(0).getId());
		}
		return null;
	}
	
	
	@Transactional(readOnly = false)
	public void deleteTemplateById(Long id) {
		if (id != null) {
			templateParamRepository.deleteByTemplateId(id);
			templateRepository.delete(id);
		}
	}
	
	@Transactional(readOnly = false)
	public void deleteTemplate(Template template) {
		if (template != null) {
			templateParamRepository.deleteByTemplateId(template.getId());
			templateRepository.delete(template);
		}
	}
	
	public Template getTemplate(String appCode, String code) {
		String targetAppCode = appCode;
		String targetCode = code;
		if (StringUtils.isNotEmpty(code) && code.contains("@")) {
			targetAppCode = StringUtils.upperCase(StringUtils
					.substringAfterLast(code, "@"));
			targetCode = StringUtils.upperCase(StringUtils.substringBefore(
					code, "@"));
		}
		Template template = templateRepository.findOneByAppCodeAndCode(
				targetAppCode, targetCode);
		return template;
	}
}