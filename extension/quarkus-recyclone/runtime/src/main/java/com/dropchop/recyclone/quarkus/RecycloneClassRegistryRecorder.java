package com.dropchop.recyclone.quarkus;

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
    RecycloneClassRegistryService service = new RecycloneClassRegistryService();
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    for (String entry : jsonSerConf) {
      try {
        Class<?> cls = cl.loadClass(entry);
        service.getJsonSerializationTypeConfig().addSubType(cls.getSimpleName(), cls);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    for (Map.Entry<String, String> entry : mapperConf.entrySet()) {
      try {
        service.getMapperTypeConfig().addBidiMapping(
            cl.loadClass(entry.getKey()), cl.loadClass(entry.getValue())
        );
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }

    return new RuntimeValue<>(service);
  }
}
