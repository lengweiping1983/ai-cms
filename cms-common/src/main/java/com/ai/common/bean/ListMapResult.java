package com.ai.common.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListMapResult extends BaseResult {

    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

}
