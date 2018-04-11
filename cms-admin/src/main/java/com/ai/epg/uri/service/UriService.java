package com.ai.epg.uri.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.AppGlobal;
import com.ai.common.enums.OnlineStatusEnum;
import com.ai.common.enums.TemplateParamTypeEnum;
import com.ai.common.exception.ServiceException;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;
import com.ai.epg.category.entity.Category;
import com.ai.epg.category.repository.CategoryRepository;
import com.ai.epg.config.entity.App;
import com.ai.epg.config.repository.AppRepository;
import com.ai.epg.template.entity.TemplateParam;
import com.ai.epg.template.repository.TemplateParamRepository;
import com.ai.epg.template.repository.TemplateRepository;
import com.ai.epg.uri.bean.UriParamBean;
import com.ai.epg.uri.entity.Uri;
import com.ai.epg.uri.entity.UriParam;
import com.ai.epg.uri.repository.UriParamRepository;
import com.ai.epg.uri.repository.UriRepository;
import com.ai.epg.widget.entity.Widget;
import com.ai.epg.widget.repository.WidgetRepository;

@Service
@Transactional(value = "slaveTransactionManager", readOnly = true)
public class UriService extends AbstractService<Uri, Long> {

	@Autowired
	private AppRepository appRepository;

	@Autowired
	private UriRepository uriRepository;

	@Autowired
	private UriParamRepository uriParamRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private WidgetRepository widgetRepository;

	@Autowired
	private TemplateRepository templateRepository;

	@Autowired
	private TemplateParamRepository templateParamRepository;

	@Override
	public AbstractRepository<Uri, Long> getRepository() {
		return uriRepository;
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public String getInternalParam(Long uriId, List<UriParamBean> newParams) {
		if (uriId == null) {
			return null;
		}
		String internalParam = "";
		List<UriParam> oldParams = uriParamRepository.findAllByUriId(uriId);
		for (UriParamBean newParam : newParams) {
			UriParam param = null;
			if (oldParams != null) {
				for (UriParam oldParam : oldParams) {
					if (oldParam.getId() != null
							&& newParam.getId() != null
							&& oldParam.getId().longValue() == newParam.getId()
									.longValue()) {
						param = oldParam;
						break;
					}
				}
			}
			if (StringUtils.isNotEmpty(newParam.getCode())
					&& (param == null || !StringUtils.trimToEmpty(
							param.getStyle()).equals(
							StringUtils.trimToEmpty(newParam.getStyle())))) {
				if (StringUtils.isNotEmpty(internalParam)) {
					internalParam += "&";
				}
				internalParam += StringUtils.trimToEmpty(newParam.getNumber())
						+ "$" + newParam.getType() + "$"
						+ StringUtils.trimToEmpty(newParam.getCode()) + "$"
						+ StringUtils.trimToEmpty(newParam.getStyle());
			}
		}
		return internalParam;
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void saveWidgetAndCategory(Long uriId, List<UriParamBean> newParams,
			String appCode) {
		if (uriId == null) {
			return;
		}
		Uri uri = uriRepository.findOne(uriId);
		saveWidgetAndCategory(uri, newParams, appCode, false);
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void saveWidgetAndCategory(Uri uri, List<UriParamBean> newParams,
			String appCode, boolean updateUriParam) {
		if (uri == null) {
			return;
		}
		if (uri.getTemplateId() != null) {
			List<UriParam> oldParams = uriParamRepository.findAllByUriId(uri
					.getId());
			List<TemplateParam> templateParamList = templateParamRepository
					.findAllByTemplateId(uri.getTemplateId());
			for (UriParamBean newParam : newParams) {
				boolean exist = false;

				UriParam param = null;
				if (oldParams != null) {
					for (UriParam oldParam : oldParams) {
						if (oldParam.getId() != null
								&& newParam.getId() != null
								&& oldParam.getId().longValue() == newParam
										.getId().longValue()) {
							param = oldParam;
							exist = true;
							break;
						}
					}
				}

				if (!exist) {
					Long itemId = null;
					if (newParam.getItemId() != null) {
						itemId = newParam.getItemId();
					} else if (newParam.getAutoCreateItem() != null
							&& newParam.getAutoCreateItem() == 1
							&& StringUtils.isNotEmpty(newParam.getCode())
							&& StringUtils.isNotEmpty(newParam.getName())) {
						String targetAppCode = appCode;
						String targetCode = StringUtils.upperCase(StringUtils
								.trimToEmpty(newParam.getCode()));
						if (StringUtils.isNotEmpty(targetCode)
								&& targetCode.contains("@")) {
							targetAppCode = StringUtils.upperCase(StringUtils
									.substringAfterLast(targetCode, "@"));
							targetCode = StringUtils.upperCase(StringUtils
									.substringBefore(targetCode, "@"));
						}
						App app = null;
						if (StringUtils.isNotEmpty(targetAppCode)) {
							app = appRepository.findOneByCode(targetAppCode);
						}
						if (app == null) {
							throw new ServiceException("应用[" + targetAppCode
									+ "]不存在！");
						}

						TemplateParam templateParam = null;
						if (templateParamList != null) {
							for (TemplateParam tParam : templateParamList) {
								if (tParam.getNumber().equals(
										newParam.getNumber())) {
									templateParam = tParam;
									break;
								}
							}
						}
						if (newParam.getType() == TemplateParamTypeEnum.WIDGET
								.getKey() && templateParam != null) {
							Widget widget = widgetRepository
									.findOneByAppCodeAndCode(targetAppCode,
											targetCode);
							if (widget == null) {
								if (StringUtils.isEmpty(targetCode)) {
									throw new ServiceException("推荐位代码不能为空!");
								} else if (StringUtils.upperCase(targetCode)
										.indexOf(AppGlobal.WIDGET_CODE_PRE) != 0) {
									throw new ServiceException("推荐位代码["
											+ targetCode + "]需要以"
											+ AppGlobal.WIDGET_CODE_PRE + "开头!");
								} else if (AppGlobal.WIDGET_CODE_PRE
										.equalsIgnoreCase(targetCode)) {
									throw new ServiceException("推荐位代码["
											+ targetCode + "]不正确!");
								}
								widget = new Widget();
								widget.setAppCode(appCode);
								widget.setCode(targetCode);
								widget.setName(newParam.getName());
								widget.setTitle(newParam.getName());
								widget.setDescription(templateParam
										.getDescription());// 从模板中复制
								widget.setStatus(OnlineStatusEnum.ONLINE
										.getKey());
								widget.setOnlineTime(new Date());
								widget.setType(templateParam.getWidgetType());// 从模板中复制
								widget.setItemNum(templateParam
										.getWidgetItemNum());// 从模板中复制

								widget.setConfigItemTypes(templateParam
										.getConfigItemTypes());
								widget.setConfigImage1(templateParam
										.getConfigImage1());
								widget.setConfigImage1Width(templateParam
										.getConfigImage1Width());
								widget.setConfigImage1Height(templateParam
										.getConfigImage1Height());
								widget.setConfigImage2(templateParam
										.getConfigImage2());
								widget.setConfigImage2Width(templateParam
										.getConfigImage2Width());
								widget.setConfigImage2Height(templateParam
										.getConfigImage2Height());
								widgetRepository.save(widget);
							}
							itemId = widget.getId();
						} else if (newParam.getType() == TemplateParamTypeEnum.CATEGORY
								.getKey() && templateParam != null) {
							Category category = categoryRepository
									.findOneByAppCodeAndCode(targetAppCode,
											targetCode);
							if (category == null) {
								if (StringUtils.isEmpty(targetCode)) {
									throw new ServiceException("栏目代码不能为空!");
								} else if (StringUtils.upperCase(targetCode)
										.indexOf(AppGlobal.CATEGORY_CODE_PRE) != 0) {
									throw new ServiceException("栏目代码["
											+ targetCode + "]需要以"
											+ AppGlobal.CATEGORY_CODE_PRE
											+ "开头!");
								} else if (AppGlobal.CATEGORY_CODE_PRE
										.equalsIgnoreCase(targetCode)) {
									throw new ServiceException("栏目代码["
											+ targetCode + "]不正确!");
								}
								category = new Category();
								category.setAppCode(targetAppCode);
								category.setCode(targetCode);
								category.setName(newParam.getName());
								category.setTitle(newParam.getName());
								category.setDescription(templateParam
										.getDescription());// 从模板中复制
								category.setStatus(OnlineStatusEnum.ONLINE
										.getKey());
								category.setOnlineTime(new Date());

								category.setConfigItemTypes(templateParam
										.getConfigItemTypes());
								category.setConfigImage1(templateParam
										.getConfigImage1());
								category.setConfigImage1Width(templateParam
										.getConfigImage1Width());
								category.setConfigImage1Height(templateParam
										.getConfigImage1Height());
								category.setConfigImage2(templateParam
										.getConfigImage2());
								category.setConfigImage2Width(templateParam
										.getConfigImage2Width());
								category.setConfigImage2Height(templateParam
										.getConfigImage2Height());

								categoryRepository.save(category);
							}
							itemId = category.getId();
						}
					}
					if (updateUriParam && itemId != null) {
						param = new UriParam();
						param.setUriId(uri.getId());
						param.setNumber(newParam.getNumber());
						param.setStyle(StringUtils.trimToEmpty(newParam
								.getStyle()));
						param.setItemType(newParam.getType());
						param.setItemId(itemId);
						uriParamRepository.save(param);
					}
				} else {
					if (updateUriParam
							&& !StringUtils.trimToEmpty(param.getStyle())
									.equals(StringUtils.trimToEmpty(newParam
											.getStyle()))) {
						param.setStyle(StringUtils.trimToEmpty(newParam
								.getStyle()));
						uriParamRepository.save(param);
					}
				}
			}
		}
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void saveUri(Uri uri, List<UriParamBean> newParams, String appCode) {
		List<UriParam> oldParams = uriParamRepository.findAllByUriId(uri
				.getId());
		if (oldParams != null) {
			for (UriParam oldParam : oldParams) {
				boolean exist = false;
				for (UriParamBean newParam : newParams) {
					if (newParam.getId() != null
							&& oldParam.getId() != null
							&& newParam.getId().longValue() == oldParam.getId()
									.longValue()) {
						exist = true;
						break;
					}
				}
				if (!exist) {
					uriParamRepository.delete(oldParam);
					uriParamRepository.flush();
				}
			}
		}

		if (uri != null) {
			uriRepository.save(uri);
		}
		saveWidgetAndCategory(uri, newParams, appCode, true);
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void saveUri(Uri uri) {
		if (uri != null) {
			uriRepository.save(uri);
		}
	}

	@Transactional(value = "slaveTransactionManager", readOnly = false)
	public void deleteUri(Uri uri) {
		if (uri != null) {
			uriParamRepository.deleteByUriId(uri.getId());
			uriRepository.delete(uri);
		}
	}

}