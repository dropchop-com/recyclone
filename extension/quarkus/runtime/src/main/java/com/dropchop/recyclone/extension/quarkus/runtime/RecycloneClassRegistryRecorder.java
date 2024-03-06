package com.dropchop.recyclone.extension.quarkus.runtime;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.api.filtering.RecycloneClassRegistryService;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

import java.util.Collection;
import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 03. 24.
 */
@Recorder
public class RecycloneClassRegistryRecorder {
  public RuntimeValue<RecycloneClassRegistryService> createClassRegistry(Collection<String> jsonSerConf,
                                                                         Map<String, String> mapperConf) {
    RecycloneClassRegistryService service = new RecycloneClassRegistryServiceImpl();
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    JsonSerializationTypeConfig config = service.getJsonSerializationTypeConfig();
    for (String entry : jsonSerConf) {
      try {
        Class<?> cls = cl.loadClass(entry);
        config.addSubType(cls.getSimpleName(), cls);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    MapperSubTypeConfig mapperSubTypeConfig = service.getMapperTypeConfig();
    for (Map.Entry<String, String> entry : mapperConf.entrySet()) {
      try {
        mapperSubTypeConfig.addBidiMapping(
            cl.loadClass(entry.getKey()), cl.loadClass(entry.getValue())
        );
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }

    return new RuntimeValue<>(service);
  }
}
