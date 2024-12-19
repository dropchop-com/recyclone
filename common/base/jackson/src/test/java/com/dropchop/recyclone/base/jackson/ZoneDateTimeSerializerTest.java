package com.dropchop.recyclone.base.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZoneDateTimeSerializerTest {

  @Test
  public void serialize() throws Exception {
    ZonedDateTime zonedDateTime = ZonedDateTime.now();

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput1 = mapper.writeValueAsString(zonedDateTime);
    String jsonOutput2 = mapper.writeValueAsString(zonedDateTime);

    assertEquals(jsonOutput1, jsonOutput2);

    System.out.println("zonedDateTime: " + zonedDateTime);
    System.out.println("Serialized ZonedDateTime: " + jsonOutput1);
  }
}
