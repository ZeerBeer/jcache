package core.def;

public interface Cache<K, V> {
    public void put(final K key, final V value, long expireTime);
    public void put(final K key, final V value);
    public V get(final K key);
}
