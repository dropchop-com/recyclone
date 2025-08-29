package com.dropchop.recyclone.base.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class ZoneDateTimeSerializerTest {

  @Test
  public void serialize() throws Exception {
    ZonedDateTime zonedDateTime = ZonedDateTime.now();

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput1 = mapper.writeValueAsString(zonedDateTime);
    String jsonOutput2 = mapper.writeValueAsString(zonedDateTime);

    assertEquals(jsonOutput1, jsonOutput2);

    log.info("zonedDateTime: [{}]", zonedDateTime);
    log.info("Serialized ZonedDateTime: [{}]", jsonOutput1);
  }
}
