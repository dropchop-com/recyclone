package com.dropchop.recyclone.model.entity.jpa.tag;

import com.dropchop.recyclone.model.api.utils.Uuid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 06. 22.
 */
class ENamedTagTest {

  @Test
  void setName() {
    ENamedTag group = new ENamedTag();
    group.setName("some_name");
    String id = group.getUuid().toString();
    assertEquals(Uuid.getNameBasedV3(ENamedTag.class, "some_name").toString(), id);
    assertEquals(id, group.getUuid().toString());
  }
}