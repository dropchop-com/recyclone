package com.dropchop.recyclone.repo.es.mapper;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 01. 24.
 */
@SuppressWarnings("unused")
public class QueryNodeObject extends QueryNode implements IQueryNodeObject {

  private final Map<String, Object> delegate = new LinkedHashMap<>();

  public QueryNodeObject() {
  }

  public QueryNodeObject(IQueryNode parent) {
    super(parent);
  }

  @Override
  public int size() {
    return delegate.size();
  }

  @Override
  public boolean isEmpty() {
    return delegate.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return delegate.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return delegate.containsValue(value);
  }

  @Override
  public Object get(Object key) {
    return delegate.get(key);
  }

  @Override
  public Object put(String key, Object value) {
    return delegate.put(key, value);
  }

  @Override
  public Object remove(Object key) {
    return delegate.remove(key);
  }

  @Override
  public void putAll(Map<? extends String, ?> m) {
    delegate.putAll(m);
  }

  @Override
  public void clear() {
    delegate.clear();
  }

  @Override
  public Set<String> keySet() {
    return delegate.keySet();
  }

  @Override
  public Collection<Object> values() {
    return delegate.values();
  }

  @Override
  public Set<Map.Entry<String, Object>> entrySet() {
    return delegate.entrySet();
  }

  @Override
  public boolean equals(Object o) {
    return delegate.equals(o);
  }

  @Override
  public int hashCode() {
    return delegate.hashCode();
  }

  @Override
  public Object getOrDefault(Object key, Object defaultValue) {
    return delegate.getOrDefault(key, defaultValue);
  }

  @Override
  public void forEach(BiConsumer<? super String, ? super Object> action) {
    delegate.forEach(action);
  }

  @Override
  public void replaceAll(BiFunction<? super String, ? super Object, ?> function) {
    delegate.replaceAll(function);
  }

  @Override
  public Object putIfAbsent(String key, Object value) {
    return delegate.putIfAbsent(key, value);
  }

  @Override
  public boolean remove(Object key, Object value) {
    return delegate.remove(key, value);
  }

  @Override
  public boolean replace(String key, Object oldValue, Object newValue) {
    return delegate.replace(key, oldValue, newValue);
  }

  @Override
  public Object replace(String key, Object value) {
    return delegate.replace(key, value);
  }

  @Override
  public Object computeIfAbsent(String key, Function<? super String, ?> mappingFunction) {
    return delegate.computeIfAbsent(key, mappingFunction);
  }

  @Override
  public Object computeIfPresent(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
    return delegate.computeIfPresent(key, remappingFunction);
  }

  @Override
  public Object compute(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
    return delegate.compute(key, remappingFunction);
  }

  @Override
  public Object merge(String key, Object value, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
    return delegate.merge(key, value, remappingFunction);
  }


}