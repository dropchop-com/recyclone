package com.dropchop.recyclone.base.api.model.security;

import lombok.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 04. 08. 2025
 */
public class ClientKeyConfigs {

  private final Map<String, ClientKeyConfig> configs = new LinkedHashMap<>();

  public int size() {
    return configs.size();
  }

  public boolean isEmpty() {
    return configs.isEmpty();
  }

  public @NonNull Set<Map.Entry<String, ClientKeyConfig>> entrySet() {
    return configs.entrySet();
  }

  public ClientKeyConfig put(String s, ClientKeyConfig clientKeyConfig) {
    return configs.put(s, clientKeyConfig);
  }

  public ClientKeyConfig get(String o) {
    return configs.get(o);
  }
}
