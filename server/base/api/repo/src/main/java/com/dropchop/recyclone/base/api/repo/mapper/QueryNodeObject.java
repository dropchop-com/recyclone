package com.dropchop.recyclone.base.api.repo.mapper;

import lombok.NonNull;

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
  public void putAll(@NonNull Map<? extends String, ?> m) {
    delegate.putAll(m);
  }

  @Override
  public void clear() {
    delegate.clear();
  }

  @Override
  public @NonNull Set<String> keySet() {
    return delegate.keySet();
  }

  @Override
  public @NonNull Collection<Object> values() {
    return delegate.values();
  }

  @Override
  public @NonNull Set<Map.Entry<String, Object>> entrySet() {
    return delegate.entrySet();
  }

  @Override
  @SuppressWarnings({"EqualsWhichDoesntCheckParameterClass", "EqualsDoesntCheckParameterClass"})
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
  public Object computeIfAbsent(String key, @NonNull Function<? super String, ?> mappingFunction) {
    return delegate.computeIfAbsent(key, mappingFunction);
  }

  @Override
  public Object computeIfPresent(String key, @NonNull BiFunction<? super String, ? super Object, ?> remappingFunction) {
    return delegate.computeIfPresent(key, remappingFunction);
  }

  @Override
  public Object compute(String key, @NonNull BiFunction<? super String, ? super Object, ?> remappingFunction) {
    return delegate.compute(key, remappingFunction);
  }

  @Override
  public Object merge(String key, @NonNull Object value,
                      @NonNull BiFunction<? super Object, ? super Object, ?> remappingFunction) {
    return delegate.merge(key, value, remappingFunction);
  }

  private Object getNestedValue(String keys) {
    return getNestedValue(this, keys);
  }

  private static Object getNestedValue(QueryNodeObject node, String targetKey) {
    if (node == null) return null;

    if (node.containsKey(targetKey)) {
      if(node.get(targetKey) instanceof QueryNodeObject) {
        return ((QueryNodeObject)node.get(targetKey)).values().iterator().next();
      }
      return node.get(targetKey);
    }

    for (Object value : node.values()) {
      if (value instanceof QueryNodeObject) {
        Object result = getNestedValue((QueryNodeObject) value, targetKey);
        if (result != null) return result;
      } else if (value instanceof QueryNodeList) {
        for (Object item : (QueryNodeList) value) {
          if (item instanceof QueryNodeObject) {
            Object result = getNestedValue((QueryNodeObject) item, targetKey);
            if (result != null) return result;
          }
        }
      }
    }
    return null;
  }


  @SuppressWarnings("unchecked")
  public <T> T getNestedValue(Class<T> type, String keys) {
    Object value = getNestedValue(keys);
    if(type.equals(UUID.class)) {
      if(value instanceof String) {
        return (T) UUID.fromString((String) value);
      } else if(value != null) {
        return null;
      }

      return null;
    }
    return type.isInstance(value) ? (T) value : null;
  }
}
