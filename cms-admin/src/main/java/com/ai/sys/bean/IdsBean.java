package com.ai.sys.bean;

public class IdsBean<T> {

    private T data;

    private Long[] ids;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long[] getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
    }

}
