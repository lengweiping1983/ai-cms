package com.ai.common.bean;

import java.util.ArrayList;
import java.util.List;

public class ListResult<T> extends BaseResult {

    private List<T> data = new ArrayList<T>();

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public static <T> ListResult<T> newListResult(final List<T> data) {
        ListResult<T> result = new ListResult<T>();
        result.setData(data);
        return result;
    }
}
