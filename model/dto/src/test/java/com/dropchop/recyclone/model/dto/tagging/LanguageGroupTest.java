package com.dropchop.recyclone.model.dto.tagging;

import com.dropchop.recyclone.model.api.utils.Uuid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 01. 22.
 */
public class LanguageGroupTest {

  @Test
  void construct() {
    LanguageGroup group = new LanguageGroup("slavic");
    String id = group.getId();
    assertEquals(Uuid.getNameBasedV3(LanguageGroup.class, "slavic").toString(), id);
    assertEquals(id, group.getUuid().toString());
  }
}
