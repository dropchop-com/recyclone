package com.dropchop.recyclone.model.entity.jpa.tagging;

import com.dropchop.recyclone.model.api.utils.Uuid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 06. 22.
 */
class JpaNamedTagTest {

  @Test
  void setName() {
    JpaNamedTag group = new JpaNamedTag();
    group.setName("some_name");
    String id = group.getUuid().toString();
    assertEquals(Uuid.getNameBasedV3("NamedTag.some_name").toString(), id);
    assertEquals(id, group.getUuid().toString());

    /*ENamedTag group1 = ENamedTag.builder().name("some_name").build();
    id = group1.getUuid().toString();
    assertEquals(Uuid.getNameBasedV3("NamedTag.some_name").toString(), id);
    assertEquals(id, group.getUuid().toString());*/
  }
}