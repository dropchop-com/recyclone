package com.dropchop.recyclone.quarkus.it.repo.jpa.localization;

import com.dropchop.recyclone.base.jpa.model.localization.JpaLanguage;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.ZonedDateTime;

@Disabled
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SuppressWarnings("unused")
public class LanguageRepositoryTest {

  public static String lngEnCode = "en";
  public static String lngSlCode = "sl";


  public static JpaLanguage generateLanguage(String code) {
    JpaLanguage language = new JpaLanguage();
    language.setCode(code);
    language.setModified(ZonedDateTime.now());
    language.setCreated(ZonedDateTime.now());
    return language;
  }


  /*@BeforeAll
  public static void setup() {

  }


  @Test
  @Transactional
  @Order(1)
  public void createCountry() {
    ELanguage language = generateLanguage(lngEnCode);
    this.languageRepository.save(language);
  }


  @Test
  @Transactional
  @Order(2)
  public void addRemoveTranslations() {
    ELanguage tmpLng = this.languageRepository.findById(lngEnCode);

    Assertions.assertNotNull(tmpLng);
    Assertions.assertEquals(lngEnCode, tmpLng.getCode());
    Assertions.assertEquals(0, tmpLng.getTranslations().size());

    tmpLng.setTitle(lngEnCode, "English");
    tmpLng.setTitle(lngSlCode, "Anglešlko");
    tmpLng.setModified(ZonedDateTime.now());

    this.languageRepository.save(tmpLng);

    tmpLng = this.languageRepository.findById(lngEnCode);

    Assertions.assertNotNull(tmpLng);
    Assertions.assertEquals(lngEnCode, tmpLng.getCode());
    Assertions.assertEquals(2, tmpLng.getTranslations().size());

    tmpLng.setTitle(lngEnCode, "Burek");
    this.languageRepository.save(tmpLng);

    tmpLng = this.languageRepository.findById(lngEnCode);
    Assertions.assertNotNull(tmpLng);
    Assertions.assertEquals(lngEnCode, tmpLng.getCode());
    Assertions.assertEquals("Burek", tmpLng.getTranslationOrTitle(Locale.ENGLISH, null));

    tmpLng.removeTranslation(Locale.ENGLISH);

    this.languageRepository.save(tmpLng);

    tmpLng = this.languageRepository.findById(lngEnCode);
    Assertions.assertNotNull(tmpLng);
    Assertions.assertEquals(lngEnCode, tmpLng.getCode());
    Assertions.assertEquals(1, tmpLng.getTranslations().size());
    Assertions.assertNull(tmpLng.getTranslation(Locale.ENGLISH));
    Assertions.assertNotNull(tmpLng.getTranslation(lngSlCode));
  }


  @Test
  @Transactional
  @Order(3)
  public void deleteCountry() {
    ELanguage tmpLng = this.languageRepository.findById(lngEnCode);
    this.languageRepository.delete(tmpLng);

    tmpLng = this.languageRepository.findById(lngEnCode);
    Assertions.assertNull(tmpLng);
  }*/
}
