package com.ai.common.utils;

import java.util.HashMap;
import java.util.Map;

public class HashMapBuilder<K, V> {
    Map<K, V> data = new HashMap<K, V>();

    public HashMapBuilder<K, V> put(K k, V v) {
        data.put(k, v);
        return this;
    }

    public Map<K, V> build() {
        return data;
    }
}
