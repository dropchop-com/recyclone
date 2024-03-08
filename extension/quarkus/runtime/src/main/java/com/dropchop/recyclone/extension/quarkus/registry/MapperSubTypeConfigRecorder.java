package com.dropchop.recyclone.extension.quarkus.registry;

import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 03. 24.
 */
@Recorder
public class MapperSubTypeConfigRecorder {
  public RuntimeValue<MapperSubTypeConfig> createConfig(Map<String, String> mapperConf) {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    MapperSubTypeConfig mapperSubTypeConfig = new MapperSubTypeConfigImpl();
    for (Map.Entry<String, String> entry : mapperConf.entrySet()) {
      try {
        mapperSubTypeConfig.addBidiMapping(
            cl.loadClass(entry.getKey()), cl.loadClass(entry.getValue())
        );
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }

    return new RuntimeValue<>(mapperSubTypeConfig);
  }
}
