package com.dropchop.recyclone.rest.jaxrs.serialization;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.marker.HasAttributes;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.rest.jaxrs.server.ObjectMapperContextResolver;
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
    String json = "{\n" +
      "  \"version\": \"v1_0\",\n" +
      "  \"from\": 0,\n" +
      "  \"size\": 0,\n" +
      "  \"attributes\": [\n" +
      "    {\n" +
      "      \"name\": \"test\",\n" +
      "      \"value\": \"2022-02-10T10:30:44.123+01:00\"\n" +
      "    },\n" +
      "    {\n" +
      "      \"name\": \"test1\",\n" +
      "      \"value\": [\n" +
      "        {\n" +
      "          \"name\": \"test\",\n" +
      "          \"value\": \"2022-02-10T10:30:44.123+01:00\"\n" +
      "        },\n" +
      "        {\n" +
      "          \"name\": \"test_bool\",\n" +
      "          \"value\": true\n" +
      "        },\n" +
      "        {\n" +
      "          \"name\": \"test_long\",\n" +
      "          \"value\": 2132142335443553\n" +
      "        },\n" +
      "        {\n" +
      "          \"name\": \"test_f\",\n" +
      "          \"value\": 1.2132142335443553\n" +
      "        }\n" +
      "      ]\n" +
      "    }\n" +
      "  ],\n" +
      "  \"lang\": \"en\",\n" +
      "  \"requestId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\n" +
      "  \"codes\": [\n" +
      "    \"sl\"\n" +
      "  ]\n" +
      "}";

    ObjectMapper mapper = ObjectMapperContextResolver.createObjectMapper();
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