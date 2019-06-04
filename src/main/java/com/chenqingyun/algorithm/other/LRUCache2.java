package com.chenqingyun.algorithm.other;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chenqingyun
 * @date 2019-06-04
 * <p>
 * LRU 算法的 Java 实现，基于 LinkedHashMap
 */
public class LRUCache2<K, V> extends LinkedHashMap<K, V> {

    private int size;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private LRUCache2(int size) {
        super(size, DEFAULT_LOAD_FACTOR, true);
        this.size = size;
    }

    public static <K, V> LRUCache2<K, V> newInstance(int size) {
        return new LRUCache2(size);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > size;
    }


}
