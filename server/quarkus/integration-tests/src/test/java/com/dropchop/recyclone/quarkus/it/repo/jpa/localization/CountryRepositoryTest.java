package com.dropchop.recyclone.quarkus.it.repo.jpa.localization;

import com.dropchop.recyclone.model.entity.jpa.localization.JpaCountry;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaLanguage;
import com.dropchop.recyclone.base.api.repo.TransactionHelper;
import com.dropchop.recyclone.base.jpa.repo.localization.CountryRepository;
import com.dropchop.recyclone.base.jpa.repo.localization.LanguageRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;
import java.util.Locale;

import static com.dropchop.recyclone.quarkus.it.repo.jpa.localization.LanguageRepositoryTest.lngEnCode;
import static com.dropchop.recyclone.quarkus.it.repo.jpa.localization.LanguageRepositoryTest.lngSlCode;

@Slf4j
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CountryRepositoryTest {

  public static final String coUkCode = "uk";
  public static final String coSiCode = "si";

  @Inject
  CountryRepository countryRepository;

  @Inject
  LanguageRepository languageRepository;

  @Inject
  TransactionHelper th;

  JpaLanguage lngEn;
  JpaLanguage lngSl;

  ZonedDateTime testStart;


  public static JpaCountry generateCountry(String code) {
    JpaCountry country = new JpaCountry();
    country.setCode(code);
    country.setModified(ZonedDateTime.now());
    country.setCreated(ZonedDateTime.now());
    return country;
  }

  @BeforeAll
  @Transactional
  public void setup() {
    lngEn = languageRepository.findById(LanguageRepositoryTest.lngEnCode);
    lngSl = languageRepository.findById(lngSlCode);

    JpaCountry countryUk = generateCountry(coUkCode);
    countryUk.setTitle(lngSlCode, "Združeno kraljestvo");
    countryUk.setLanguage(lngSl);

    JpaCountry countrySi = generateCountry(coSiCode);
    countrySi.setTitle(lngSlCode, "Slovenija");
    countrySi.setLanguage(lngSl);

    countryRepository.save(countryUk);
    countryRepository.save(countrySi);

    testStart = ZonedDateTime.now();
  }

  @AfterAll
  @Transactional
  public void tearDown() {
    JpaCountry countryUk = this.countryRepository.findById(coUkCode);
    countryRepository.delete(countryUk);
    JpaCountry countrySi = this.countryRepository.findById(coSiCode);
    countryRepository.delete(countrySi);
  }

  @Test
  @Order(1)
  public void addRemoveTranslations() {
    th.transact(() -> {
      JpaCountry country = this.countryRepository.findById(coUkCode);
      Assertions.assertEquals(0, country.getTranslations().size());
      country.setTitleTranslation(lngEnCode, "United Kingdom");
      this.countryRepository.save(country);
    });
    th.transact(() -> {
      JpaCountry country = this.countryRepository.findById(coUkCode);
      Assertions.assertEquals(1, country.getTranslations().size());
      Assertions.assertTrue(country.removeTranslation(Locale.ENGLISH));
      this.countryRepository.save(country);
    });
    th.transact(() -> {
      JpaCountry country = this.countryRepository.findById(coUkCode);
      Assertions.assertEquals(0, country.getTranslations().size());
    });
  }

  @Test
  @Transactional
  @Order(2)
  public void changeInlineTranslation() {
    th.transact(() -> {
      JpaCountry tmpCountry = this.countryRepository.findById(coUkCode);
      tmpCountry.setTitle(lngSlCode, "Burek");
      this.countryRepository.save(tmpCountry);
    });
    th.transact(() -> {
      JpaCountry tmpCountry = this.countryRepository.findById(coUkCode);
      Assertions.assertEquals("Burek", tmpCountry.getTranslationOrTitle(lngSlCode));
      tmpCountry.setTitle(lngSlCode, "Združeno kraljestvo");
      this.countryRepository.save(tmpCountry);
    });
  }
}
