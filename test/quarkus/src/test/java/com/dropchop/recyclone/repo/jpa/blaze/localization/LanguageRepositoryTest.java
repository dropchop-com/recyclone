package com.dropchop.recyclone.repo.jpa.blaze.localization;

import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.repo.api.RepositoryType;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import jakarta.inject.Inject;
import java.time.ZonedDateTime;
@Disabled
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LanguageRepositoryTest {

  @Inject
  @RepositoryType(com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT)
  LanguageRepository languageRepository;

  public static String lngEnCode = "en";
  public static String lngSlCode = "sl";


  public static ELanguage generateLanguage(String code) {
    ELanguage language = new ELanguage();
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
