package com.dropchop.recyclone.base.api.model.utils;

import org.slf4j.MDC;

import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 02. 07. 2026.
 */
@SuppressWarnings("unused")
public interface Logging {

  static void resetMdcValue(String key, String value) {
    if (value == null || value.isBlank()) {
      MDC.remove(key);
      return;
    }
    MDC.put(key, value);
  }

  @SafeVarargs
  static void resetMdcValues(Map.Entry<String, String>... entries) {
    for (Map.Entry<String, String> entry : entries) {
      resetMdcValue(entry.getKey(), entry.getValue());
    }
  }

  static void clearMdcValues(String... keys) {
    for (String k : keys) {
      resetMdcValue(k, null);
    }
  }
}
