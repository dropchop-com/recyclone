package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.quarkus.runtime.invoke.ParamsSelector;
import com.dropchop.recyclone.quarkus.runtime.rest.RestMapping;
import com.dropchop.recyclone.quarkus.runtime.rest.RestMethod;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Deserializes JSON into Params obtained from CDI if applicable.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 3. 05. 24.
 */
@ApplicationScoped
public class ParamsFactoryDeserializerModifier extends BeanDeserializerModifier {

  private final Set<Class<? extends Params>> paramsClasses = new LinkedHashSet<>();
  private final ParamsSelector paramsSelector;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  public ParamsFactoryDeserializerModifier(RestMapping restMapping, ParamsSelector paramsSelector) {
    this.paramsSelector = paramsSelector;
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    for (Map.Entry<String, RestMethod> restMethodEntry : restMapping.getApiMethods().entrySet()) {
      RestMethod restMethod = restMethodEntry.getValue();
      if (restMethod.isExcluded()) {
        continue;
      }
      String paramsClass = restMethod.getParamClass();
      if (paramsClass == null) {
        continue;
      }
      try {
        //noinspection unchecked
        paramsClasses.add((Class<? extends Params>) cl.loadClass(paramsClass));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config,
                                                BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
    for (Class<? extends Params> clazz : paramsClasses) {
      if (clazz.equals(beanDesc.getBeanClass())) {
        return new StdDeserializer<>(clazz) {
          @Override
          public Params deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            // Load instance using CDI
            Params instance = paramsSelector.select(clazz);

            // Deserialize using the default deserializer
            //noinspection unchecked
            return ((JsonDeserializer<Params>) deserializer).deserialize(p, ctxt, instance);
          }
        };
      }
    }
    return deserializer; // return the original deserializer for other classes
  }
}
