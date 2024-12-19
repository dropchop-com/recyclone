package com.dropchop.recyclone.base.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeSerializer extends JsonSerializer<ZonedDateTime> {

  @Override
  public void serialize(
    ZonedDateTime zonedDateTime,
    JsonGenerator jsonGenerator,
    SerializerProvider serializerProvider) throws IOException {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    jsonGenerator.writeString(formatter.format(zonedDateTime));
  }
}
