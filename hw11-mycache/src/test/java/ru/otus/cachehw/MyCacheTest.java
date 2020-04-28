package ru.otus.cachehw;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MyCacheTest {
    private static final Logger logger = LoggerFactory.getLogger(MyCacheTest.class);
    private final HwCache<Integer, Integer> cache = new MyCache<>(5);
    private HwListener<Integer, Integer> listener;
    @BeforeEach
    void init(){
        listener = new HwListener<Integer, Integer>() {
            @Override
            public void notify(Integer key, Integer value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };
    }

    @Test
    void putAndGet() {
        cache.addListener(listener);
        cache.put(1, 11);
        Assertions.assertEquals(11, cache.get(1));
        cache.removeListener(listener);
    }

    @Test
    void remove() {
        cache.addListener(listener);
        cache.put(1, 11);
        cache.remove(1);
        Assertions.assertNull(cache.get(1));
        cache.removeListener(listener);
    }

    @Test
    void getNull() {
        cache.addListener(listener);
        for(int i=0; i<10; i++) {
            cache.put(i, i+1);
        }
        Assertions.assertNull(cache.get(1));
        cache.removeListener(listener);
    }
}