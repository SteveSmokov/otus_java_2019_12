package ru.otus.db.cachehw;

public interface HwListener<K, V> {
  void notify(K key, V value, String action);
}
