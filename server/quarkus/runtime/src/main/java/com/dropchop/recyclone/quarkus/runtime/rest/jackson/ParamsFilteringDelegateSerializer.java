package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.base.api.model.filtering.FieldFilter;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Specialization of {@link FilteringDelegateSerializer} that applies filtering
 * using {@link FieldFilter} from {@link Params} object.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 08. 06. 25
 */
public interface ParamsFilteringDelegateSerializer extends FilteringDelegateSerializer {

  default void serialize(Params params, Object o, JsonGenerator generator, SerializerProvider provider)
      throws IOException {
    FilteringDelegateSerializer.super.serialize(
        params != null ? FieldFilter.fromParams(params) : null, o, generator, provider
    );
  }
}
