package com.dropchop.recyclone.rest.jackson.client;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.marker.HasAttributes;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 02. 22.
 */
class AttributeDeserializerTest {

  @Test
  void deserialize() throws Exception {
    String json = """
      {
        "version": "v1_0",
        "from": 0,
        "size": 0,
        "attributes": [
          {
            "name": "test",
            "value": "2022-02-10T10:30:44.123+01:00"
          },
          {
            "name": "test1",
            "value": [
              {
                "name": "test",
                "value": "2022-02-10T10:30:44.123+01:00"
              },
              {
                "name": "test_bool",
                "value": true
              },
              {
                "name": "test_long",
                "value": 2132142335443553
              },
              {
                "name": "test_f",
                "value": 1.2132142335443553
              }
            ]
          }
        ],
        "lang": "en",
        "requestId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
        "codes": [
          "sl"
        ]
      }""";

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();
    CodeParams params = mapper.readValue(json, CodeParams.class);

    assertNotNull(params.getAttributes());

    ZonedDateTime dateTime = params.getAttributeValue("test");
    assertNotNull(dateTime);
    Set<Attribute<?>> setAttribute = params.getAttributeValue("test1", null);
    dateTime = HasAttributes.getAttributeValue(setAttribute, "test", null);
    assertNotNull(dateTime);
    Boolean bool = HasAttributes.getAttributeValue(setAttribute, "test_bool", null);
    assertNotNull(bool);
    assertTrue(bool);

    BigDecimal dec = HasAttributes.getAttributeValue(setAttribute, "test_long", null);
    assertNotNull(dec);
    assertEquals(2132142335443553L, dec.longValue());

    dec = HasAttributes.getAttributeValue(setAttribute, "test_f", null);
    assertNotNull(dec);
    assertEquals(1.2132142335443553, dec.doubleValue());
  }
}