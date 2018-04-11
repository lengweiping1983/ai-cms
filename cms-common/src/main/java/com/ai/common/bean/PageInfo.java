package com.ai.common.bean;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PageInfo {
    public static final int DEFAULT_PAGE_SIZE = 10;// 默认每页显示数量
    public static final int DEFAULT_PAGE_NUM = 0;
    
    private int page = DEFAULT_PAGE_NUM;// 页码
    private int size = DEFAULT_PAGE_SIZE; // 每页显示数量

    private int totalPages;// 总页数
    private long totalElements; // 总数量

    @JsonIgnore
    private String order; // 排序 升序为:"userName" 降序为:"-userName"

    public PageInfo() {
    }

    public PageInfo(final int page, final int size) {
        this.page = page;
        this.size = size;
    }

    public int getPage() {
        return page < 0 ? 0 : page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size < 1 ? DEFAULT_PAGE_SIZE : size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * 获取分页对象
     *
     * @param pageInfo
     * @return
     */
    public static PageRequest getPageRequest(final PageInfo pageInfo) {
        Sort sort = null;
        if (StringUtils.isNotEmpty(pageInfo.getOrder())) {
            if (StringUtils.startsWith(pageInfo.getOrder(), "-")) {
                sort = new Sort(Sort.Direction.DESC, StringUtils.substringAfter(pageInfo.getOrder(), "-"));
            } else {
                sort = new Sort(Sort.Direction.ASC, pageInfo.getOrder());
            }
        }
        return new PageRequest(pageInfo.getPage(), pageInfo.getSize(), sort);
    }
    
    @JsonIgnore
    public PageRequest getPageRequest() {
        return getPageRequest(this);
    }

    public static <T> PageInfo newPageInfo(final Page<T> pageList) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(pageList.getNumber());
        pageInfo.setSize(pageList.getSize());
        pageInfo.setTotalElements(pageList.getTotalElements());
        pageInfo.setTotalPages(pageList.getTotalPages());
        return pageInfo;
    }

}
