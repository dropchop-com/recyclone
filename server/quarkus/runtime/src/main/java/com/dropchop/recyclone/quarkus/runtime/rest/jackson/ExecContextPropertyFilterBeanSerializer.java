package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.base.api.model.filtering.FieldFilter;
import com.dropchop.recyclone.base.api.model.invoke.ExecContextContainer;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializer;

import java.io.IOException;

/**
 * Bean serializer that delegates serialization to a {@link JsonSerializer}
 * and applies filtering using {@link FieldFilter} from {@link Params} object
 * obtained from {@link ExecContextContainer} object.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 08. 25.
 */
public class ExecContextPropertyFilterBeanSerializer extends ParamsPropertyFilterBeanSerializer
    implements ExecContextFilteringDelegateSerializer {

  private final ExecContextContainer execContextContainer;

  public ExecContextPropertyFilterBeanSerializer(BeanSerializer delegate, ExecContextContainer execContextContainer) {
    super(delegate, null);
    this.execContextContainer = execContextContainer;
  }

  @Override
  public void serialize(Object o, JsonGenerator generator, SerializerProvider provider) throws IOException {
    this.serialize(this.execContextContainer, o, generator, provider);
  }
}
