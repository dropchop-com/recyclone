package com.dropchop.recyclone.quarkus.runtime.registry;

import com.dropchop.recyclone.base.api.model.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.base.api.model.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.quarkus.runtime.app.JsonSerializationTypeConfigImpl;
import com.dropchop.recyclone.quarkus.runtime.app.MapperSubTypeConfigImpl;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

import java.util.Collection;
import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 03. 24.
 */
@Recorder
public class RegistryRecorder {
  public RuntimeValue<JsonSerializationTypeConfig> createJsonSerializationTypeConfig(Collection<String> jsonSerConf) {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    JsonSerializationTypeConfig config = new JsonSerializationTypeConfigImpl();
    for (String entry : jsonSerConf) {
      try {
        Class<?> cls = cl.loadClass(entry);
        config.addSubType(cls.getSimpleName(), cls);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }

    return new RuntimeValue<>(config);
  }

  public RuntimeValue<MapperSubTypeConfig> createMapperSubTypeConfig(Map<String, String> mapperConf,
                                                                     Collection<String> entityRootInterfaces) {
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
    for (String entityInterface : entityRootInterfaces) {
      try {
        mapperSubTypeConfig.addEntityRootMarker(cl.loadClass(entityInterface));
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }

    return new RuntimeValue<>(mapperSubTypeConfig);
  }
}
