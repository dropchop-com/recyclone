package com.dropchop.recyclone.quarkus.runtime;

import jakarta.inject.Singleton;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 02. 24.
 */
@Singleton
@SuppressWarnings("unused")
public class RecylconeRegistryService {

  private final Map<String, String> mappingClassNames = new LinkedHashMap<>();
  private final Set<String> serializationClassNames = new LinkedHashSet<>();

  public RecylconeRegistryService() {
    // Constructor for MyService.
  }

  public void addSerializationClassName(String className) {
    serializationClassNames.add(className);
  }

  public void addMappingClassNames(String fromClassName, String toClassName) {
    mappingClassNames.put(toClassName, fromClassName);
  }

  public Map<String, String> getMappingClassNames() {
    return mappingClassNames;
  }

  public Set<String> getSerializationClassNames() {
    return serializationClassNames;
  }
}
