package com.dropchop.recyclone.model.dto.tagging;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 13. 07. 23.
 */
public class TagTest {

  @Test
  void constructWithBuilder() {
    String uuid = "f56dcafe-4e75-4aee-a8b7-11d7726c5901";
    Tag t = Tag.builder()
        .uuid(UUID.fromString(uuid))
        .id(uuid)
        .build();
    assertNotNull(t.getUuid());
    assertEquals(UUID.fromString(uuid), t.getUuid());
  }
}
