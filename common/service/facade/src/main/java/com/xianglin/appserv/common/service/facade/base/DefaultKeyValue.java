package com.xianglin.appserv.common.service.facade.base;

import java.util.Map;

/**
 * @author Yungyu
 * @description Created by Yungyu on 2018/4/17 11:40.
 */

public class DefaultKeyValue<K, V> implements Map.Entry<K, V> {

    private K key;

    private V value;

    public DefaultKeyValue(K key, V value) {

        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {

        return key;
    }

    @Override
    public V getValue() {

        return value;
    }

    @Override
    public V setValue(V value) {

        this.value = value;
        return value;
    }
}
