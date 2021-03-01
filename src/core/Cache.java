package core;

import java.util.concurrent.*;

public class Cache<K, V> implements core.def.Cache<K, V> {
    private final int maxSize;
    private final ConcurrentHashMap<K, V> map;
    private final ConcurrentLinkedQueue<K> queue;
    private final ScheduledExecutorService scheduledExecutorService;

    public Cache(final int maxSize) {
        this.maxSize = maxSize;
        map = new ConcurrentHashMap<>(maxSize);
        queue = new ConcurrentLinkedQueue<>();
        scheduledExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    }

    public void put(final K key, final V value, long expireTime) {
        if (map.containsKey(key)) {
            queue.remove(key);
        }
        while (queue.size() >= maxSize) {
            K oldestKey = queue.poll();
            if (null != oldestKey) {
                map.remove(oldestKey);
            }
        }
        queue.add(key);
        map.put(key, value);
        if (expireTime > 0) {
            scheduledExecutorService.schedule(() -> {
                queue.remove(key);
                map.remove(key);
            }, expireTime, TimeUnit.MILLISECONDS);
        }
    }

    public void put(final K key, final V value) {
        put(key, value, 0);
    }

    public V get(final K key) {
        if (map.containsKey(key)) {
            queue.remove(key);
            queue.add(key);
        }
        return map.get(key);
    }
}