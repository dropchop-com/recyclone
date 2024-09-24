package com.dropchop.recyclone.model.dto;

import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.invoke.IdentifierParams;
import com.dropchop.recyclone.model.dto.tagging.LanguageGroup;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 12. 21.
 */
class ConstructorTest {

  @Test
  @SuppressWarnings("unused")
  public void constructor() {
    new Language("sl", Language.Script.Latn);
    new Language("sr", Language.Script.Cyrl, new Country("ME"));
    new Language();
    new Country();
    new Country("SI");
    new LanguageGroup();

    IdentifierParams params = IdentifierParams.builder().identifier("a").identifier("b").build();
    assertEquals(List.of("a", "b"), params.getIdentifiers());
  }
}