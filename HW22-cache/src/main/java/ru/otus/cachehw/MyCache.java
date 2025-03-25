package ru.otus.cachehw;

import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);
    private final Map<Key, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(new Key(key), value);
        notifyListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        var removed = cache.remove(new Key(key));
        notifyListeners(key, removed, "remove");
    }

    @Override
    public V get(K key) {
        var value = cache.get(new Key(key));
        notifyListeners(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String action) {
        listeners.forEach(it -> {
            try {
                it.notify(key, value, "put");
            } catch (Exception e) {
                logger.error(
                        "Error while notifying listener {}: {}", it.getClass().getSimpleName(), e.toString());
            }
        });
    }

    @AllArgsConstructor
    @Value
    class Key {
        K key;
    }
}
