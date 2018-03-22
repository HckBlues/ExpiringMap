package com.task;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTimeUtils;

/**
 * Expiring map is a simple map wrapper, that based on HashMap.
 * This wrapper allows specifying a time to live of the key-value and
 * implements lazy eviction of expired entries,
 */
public class ExpiringMap {

    private Map<Integer, ExpiringValue> map = new HashMap<>();

    /**
     * Stores key-value pair in a map, with specific time to die.
     * When durationMs will be exceeded, key-value will be not accessible,
     * and will be removed during the 'get' operation.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @param durationMs life time duration of the key-value entry
     */
    public void put(int key, int value, long durationMs) {
        map.put(key, new ExpiringValue(value, durationMs));
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key
     * or key-value is expired.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     * {@code null} if this map contains no mapping for the key or key-value is expired
     */
    public Integer get(int key) {
        ExpiringValue expiringValue = map.get(key);

        if (expiringValue.expirationTime < DateTimeUtils.currentTimeMillis()) {
            map.remove(key);

            return null;
        } else {
            return expiringValue.value;
        }
    }

    private class ExpiringValue {
        private final long expirationTime;
        private final int value;

        ExpiringValue(int value, long durationMs) {
            this.value = value;
            this.expirationTime = DateTimeUtils.currentTimeMillis() + durationMs;
        }
    }

}
