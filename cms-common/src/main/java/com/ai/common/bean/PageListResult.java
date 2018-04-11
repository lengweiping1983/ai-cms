package com.ai.common.bean;

import org.springframework.data.domain.Page;

public class PageListResult<T> extends ListResult<T> {

    private PageInfo pageInfo = new PageInfo();

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public static <T> PageListResult<T> newPageListResult(final Page<T> pageList) {
        PageListResult<T> result = new PageListResult<T>();

        // 封装分页数据
        result.setData(pageList.getContent());

        // 封装分页信息
        result.setPageInfo(PageInfo.newPageInfo(pageList));

        return result;
    }

}
