package com.dropchop.recyclone.model.entity.es.tagging;

import com.dropchop.recyclone.base.api.model.utils.Uuid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 1. 06. 22.
 */
class EsNamedTagTest {

  @Test
  void setName() {
    EsNamedTag group = new EsNamedTag();
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