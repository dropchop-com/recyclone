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
    LanguageGroup group1 = new LanguageGroup("slavic");
    String id1 = group1.getId();
    assertEquals(Uuid.getNameBasedV3(LanguageGroup.class, "slavic").toString(), id1);
    assertEquals(id1, group1.getUuid().toString());

    LanguageGroup group2 = new LanguageGroup("ex_yu");
    String id2 = group2.getId();
    assertEquals(Uuid.getNameBasedV3(LanguageGroup.class, "ex_yu").toString(), id2);
    assertEquals(id2, group2.getUuid().toString());
  }
}
