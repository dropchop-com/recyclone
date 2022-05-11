package com.dropchop.recyclone.model.dto.localization;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static com.dropchop.recyclone.model.api.localization.Language.Variant.nonstandard;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 27. 12. 21.
 */
class LanguageTest {

  @Test
  void getCode() {
    Language language;
    language = new Language("sr");
    assertEquals("sr", language.getCode());
    language = new Language("sr", null, (String)null, nonstandard.name());
    assertEquals("sr-nonstandard", language.getCode());
    language = new Language("sr", Language.Script.Cyrl);
    assertEquals("sr-Cyrl", language.getCode());
    language = new Language("sr", Language.Script.Cyrl, new Country("ME"));
    assertEquals("sr-Cyrl-ME", language.getCode());
    language = new Language("sr", Language.Script.Cyrl, new Country("ME"), nonstandard.name());
    assertEquals("sr-Cyrl-ME-nonstandard", language.getCode());
  }

  @Test
  void translations() {
    Language language;
    language = new Language("sr");
    language.setTitle("Serbian");
    language.addTranslation(new TitleTranslation("sl", "Srbščina"));
    language.addTranslation(new TitleTranslation("sl", "Srbščina1"));
    assertEquals(1, language.getTranslations().size());
    assertEquals("Srbščina1", language.getTranslationOrTitle("sl"));
  }

  @Test
  void toLocale() {
    Language language = new Language("sr", Language.Script.Cyrl, new Country("ME"));
    Locale locale = Locale.forLanguageTag("sr-Cyrl-ME");
    assertEquals(locale, language.toLocale());
  }

  @Test
  void paramsBuilder() {
    CodeParams params = CodeParams.builder().lang("sl").build();
  }
}