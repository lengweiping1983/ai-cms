package com.ai.epg.category.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.cms.league.entity.LeagueMatch;
import com.ai.cms.league.repository.LeagueMatchRepository;
import com.ai.cms.media.entity.Program;
import com.ai.cms.media.entity.Series;
import com.ai.cms.media.repository.ProgramRepository;
import com.ai.cms.media.repository.SeriesRepository;
import com.ai.common.bean.jstree.JsTreeBean;
import com.ai.common.enums.ItemTypeEnum;
import com.ai.common.repository.AbstractRepository;
import com.ai.common.service.AbstractService;
import com.ai.epg.category.entity.Category;
import com.ai.epg.category.entity.CategoryItem;
import com.ai.epg.category.repository.CategoryItemRepository;
import com.ai.epg.category.repository.CategoryRepository;

@Service
@Transactional(readOnly = true)
public class CategoryService extends AbstractService<Category, Long> {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryItemRepository categoryItemRepository;

	@Autowired
	private ProgramRepository programRepository;
	
	@Autowired
	private SeriesRepository seriesRepository;
	
	@Autowired
	private LeagueMatchRepository leagueMatchRepository;
	
	@Override
	public AbstractRepository<Category, Long> getRepository() {
		return categoryRepository;
	}

	@Transactional(readOnly = false)
	public void deleteCategory(Category category) {
		if (category != null) {
			categoryItemRepository.deleteByCategoryId(category.getId());
			categoryRepository.delete(category);
		}
	}

	/**
	 * 获取Category树
	 *
	 * @param filterId
	 *            过滤节点Id
	 * @param selectList
	 *            已选节点
	 * @return
	 */
	public List<JsTreeBean> findJsTreeCategory(String appCode, Long filterId,
			List<Category> selectList) {
		List<Category> allList = categoryRepository.findByAppCode(appCode);
		List<JsTreeBean> beanList = findJsTreeNode(allList,
				JsTreeBean.ROOT_NODE_ID, filterId, selectList);

		return beanList;
	}

	/**
	 * 加载节点
	 *
	 * @param allList
	 *            所有节点
	 * @param parentId
	 *            节点Id
	 * @param filterId
	 *            过滤节点Id
	 * @param selectList
	 *            已选节点
	 * @return
	 */
	private List<JsTreeBean> findJsTreeNode(List<Category> allList,
			String parentId, Long filterId, List<Category> selectList) {
		List<JsTreeBean> beanList = findChildJsTreeNode(allList, parentId,
				filterId);
		if (beanList != null) {
			for (JsTreeBean bean : beanList) {
				bean.setChildren(findJsTreeNode(allList, bean.getId(),
						filterId, selectList));
				if (selectList != null) {
					for (Category category : selectList) {
						if (bean.getId().equals(
								String.valueOf(category.getId()))) {
							bean.getState().setSelected(true);
						}
					}
				}
			}
		}
		return beanList;
	}

	/**
	 * 根据节点Id加载子节点列表
	 *
	 * @param allList
	 *            所有节点
	 * @param parentId
	 *            节点Id
	 * @param filterId
	 *            过滤节点Id
	 * @return
	 */
	private List<JsTreeBean> findChildJsTreeNode(List<Category> allList,
			String parentId, Long filterId) {
		List<JsTreeBean> childJsTreeBeans = new ArrayList<JsTreeBean>();
		for (Category e : allList) {
			if (filterId != null && filterId == e.getId()) {
				continue;
			}
			if (e.getParent() == null
					&& JsTreeBean.ROOT_NODE_ID.equals(parentId)) {
				JsTreeBean child = new JsTreeBean();
				child.setId(String.valueOf(e.getId()));
				child.setText(e.getName());
				child.getA_attr().setHref("/category/category/");
				child.getA_attr().setTitle(e.getStatus().toString());
				child.getState().setOpened(true);

				childJsTreeBeans.add(child);

			} else if (e.getParent() != null
					&& parentId.equals(String.valueOf(e.getParent().getId()))) {

				JsTreeBean child = new JsTreeBean();
				child.setId(String.valueOf(e.getId()));
				child.setText(e.getName());
				child.getA_attr().setHref("/category/category/");
				child.getA_attr().setTitle(e.getStatus().toString());
				
				childJsTreeBeans.add(child);
			}
		}
		return childJsTreeBeans;
	}

	public Category findRootNodeByAppCode(String appCode) {
		if (StringUtils.isEmpty(appCode)) {
			return null;
		}
		List<Category> categorys = categoryRepository
				.findRootNodeByAppCode(appCode);
		if (categorys != null && categorys.size() > 0) {
			return categorys.get(0);
		}
		return null;
	}

	@Transactional(readOnly = false)
	public void batchUpdate(String selectCategoryIds, Integer status,
			Integer itemType, String itemIds, Integer sortIndex) {
		if (StringUtils.isEmpty(selectCategoryIds)) {
			return;
		}
		String[] selectCategoryIdArr = selectCategoryIds.split(",");
		String[] itemIdArr = itemIds.split(",");
		if (null != itemIdArr && itemIdArr.length > 0) {
			List<Long> idList = new ArrayList<Long>();
			for (String itemId : itemIdArr) {
				idList.add(Long.valueOf(itemId));
			}
			if (itemType == ItemTypeEnum.PROGRAM.getKey()) {
				List<Program> programList = programRepository.findAll(idList);

				for (int index = 0; index < selectCategoryIdArr.length; index++) {
					Long selectCategoryId = Long
							.valueOf(selectCategoryIdArr[index]);

					categoryItemRepository.updateSortIndexByCategoryId(
							selectCategoryId, itemIdArr.length, sortIndex);

					for (int i = 0; i < programList.size(); i++) {
						Long itemId = programList.get(i).getId();
						CategoryItem categoryItem = getCategoryItem(
										selectCategoryId, itemType, itemId);
						if (categoryItem == null) {
							categoryItem = new CategoryItem();
							categoryItem.setCategoryId(selectCategoryId);
							categoryItem.setItemType(itemType);
							categoryItem.setItemId(itemId);
						}
						categoryItem.setStatus(status);
						categoryItem
								.setItemStatus(programList.get(i).getStatus());
						categoryItem.setSortIndex(i + 1);
						categoryItemRepository.save(categoryItem);
					}
				}
			} else if (itemType == ItemTypeEnum.SERIES.getKey()) {
				List<Series> seriesList = seriesRepository.findAll(idList);
				for (int index = 0; index < selectCategoryIdArr.length; index++) {
					Long selectCategoryId = Long
							.valueOf(selectCategoryIdArr[index]);

					categoryItemRepository.updateSortIndexByCategoryId(
							selectCategoryId, itemIdArr.length, sortIndex);

					for (int i = 0; i < seriesList.size(); i++) {
						Long itemId = seriesList.get(i).getId();
						CategoryItem categoryItem = getCategoryItem(
										selectCategoryId, itemType, itemId);
						if (categoryItem == null) {
							categoryItem = new CategoryItem();
							categoryItem.setCategoryId(selectCategoryId);
							categoryItem.setItemType(itemType);
							categoryItem.setItemId(itemId);
						}
						categoryItem.setStatus(status);
						categoryItem.setItemStatus(seriesList.get(i).getStatus());
						categoryItem.setSortIndex(i + 1);
						categoryItemRepository.save(categoryItem);
					}
				}
			} else if (itemType == ItemTypeEnum.LEAGUE_MATCH.getKey()) {
				List<LeagueMatch> leagueMatchList = leagueMatchRepository
						.findAll(idList);
				for (int index = 0; index < selectCategoryIdArr.length; index++) {
					Long selectCategoryId = Long
							.valueOf(selectCategoryIdArr[index]);

					categoryItemRepository.updateSortIndexByCategoryId(
							selectCategoryId, itemIdArr.length, sortIndex);

					for (int i = 0; i < leagueMatchList.size(); i++) {
						Long itemId = leagueMatchList.get(i).getId();
						CategoryItem categoryItem = getCategoryItem(selectCategoryId,
								itemType, itemId);
						if (categoryItem == null) {
							categoryItem = new CategoryItem();
							categoryItem.setCategoryId(selectCategoryId);
							categoryItem.setItemType(itemType);
							categoryItem.setItemId(itemId);
						}
						categoryItem.setStatus(status);
						categoryItem.setItemStatus(leagueMatchList.get(i)
								.getStatus());
						categoryItem.setSortIndex(i + 1);
						categoryItemRepository.save(categoryItem);
					}
				}
			}
		}
	}
	
	public CategoryItem getCategoryItem(Long categoryId, Integer itemType, Long itemId) {
		List<CategoryItem> categoryItemList = categoryItemRepository
				.findByCategoryIdAndItemTypeAndItemId(categoryId, itemType, itemId);
		if (categoryItemList != null && categoryItemList.size() > 0) {
			return categoryItemList.get(0);
		}
		return null;
	}
	
	@Transactional(readOnly = false)
	public void batchUpdateMoveByAfter(Long selectCategoryId, Integer step,
			Integer sortIndex) {
		categoryItemRepository.updateSortIndexByCategoryId(selectCategoryId,
				step, sortIndex);
	}

	@Transactional(readOnly = false)
	public void saveCategoryItem(CategoryItem categoryItem) {
		if (categoryItem != null) {
			if (categoryItem.getId() == null
					&& categoryItem.getCategoryId() != null
					&& categoryItem.getSortIndex() != null) {
				batchUpdateMoveByAfter(categoryItem.getCategoryId(), 1,
						categoryItem.getSortIndex());
			}
			categoryItemRepository.save(categoryItem);
		}
	}
	
	/**
	 * 根据栏目代码获取栏目对象
	 * 
	 * @param appCode
	 * @param code
	 * @return
	 */
	public Category getCategory(final String appCode, final String code) {
		String targetAppCode = appCode;
		String targetCode = code;
		if (StringUtils.isNotEmpty(code) && code.contains("@")) {
			targetAppCode = StringUtils.upperCase(StringUtils
					.substringAfterLast(code, "@"));
			targetCode = StringUtils.upperCase(StringUtils.substringBefore(
					code, "@"));
		}
		return categoryRepository.findOneByAppCodeAndCode(targetAppCode,
				targetCode);
	}
}