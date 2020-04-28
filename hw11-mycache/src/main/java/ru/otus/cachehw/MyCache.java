package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
  private final int maxElements;
  private final Map<K,V> dataMap = new WeakHashMap<>();
  private Set<HwListener<K,V>> listeners = new HashSet<>();

  public MyCache(int maxElements) {
    this.maxElements = maxElements;
  }

  @Override
  public void put(K key, V value) {
    if(maxElements>0 && dataMap.size()==maxElements){
      K oldKey = dataMap.keySet().stream().findFirst().get();
      listeners.forEach(listener -> listener.notify(oldKey, get(oldKey), "Delete element for new element"));
      dataMap.remove(oldKey);
    }
    dataMap.put(key,value);
    listeners.forEach(listener -> listener.notify(key, value, "Put new element"));
  }

  @Override
  public void remove(K key) {
    dataMap.remove(key);
    listeners.forEach(listener -> listener.notify(key, get(key), "Delete element by key"));
  }

  @Override
  public V get(K key) {
    V element = dataMap.get(key);
    listeners.forEach(listener -> listener.notify(key, element, "Get value by key"));
    return element;
  }

  @Override
  public void addListener(HwListener<K, V> listener) {
    listeners.add(listener);
  }

  @Override
  public void removeListener(HwListener<K, V> listener) {
    listeners.remove(listener);
  }
}
