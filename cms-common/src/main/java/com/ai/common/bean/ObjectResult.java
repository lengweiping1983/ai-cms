package com.ai.common.bean;

public class ObjectResult<T> extends BaseResult {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ObjectResult<T> newObjectResult(final T data) {
        ObjectResult<T> result = new ObjectResult<T>();
        result.setData(data);
        return result;
    }

}
