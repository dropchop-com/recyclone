package com.dropchop.recyclone.quarkus.runtime.registry;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

import java.util.Collection;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 03. 24.
 */
@Recorder
public class JsonSerializationTypeConfigRecorder {
  public RuntimeValue<JsonSerializationTypeConfig> createConfig(Collection<String> jsonSerConf) {
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
}
