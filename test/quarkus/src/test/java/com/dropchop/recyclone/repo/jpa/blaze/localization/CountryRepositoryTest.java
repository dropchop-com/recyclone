package com.dropchop.recyclone.repo.jpa.blaze.localization;

import com.dropchop.recyclone.model.entity.jpa.localization.ECountry;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.api.TransactionHelper;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.Locale;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation;
import static com.dropchop.recyclone.repo.jpa.blaze.localization.LanguageRepositoryTest.lngEnCode;
import static com.dropchop.recyclone.repo.jpa.blaze.localization.LanguageRepositoryTest.lngSlCode;

@Slf4j
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CountryRepositoryTest {

  public static final String coUkCode = "uk";
  public static final String coSiCode = "si";

  @Inject
  @RepositoryType(Implementation.RCYN_DEFAULT)
  CountryRepository countryRepository;

  @Inject
  @RepositoryType(Implementation.RCYN_DEFAULT)
  LanguageRepository languageRepository;

  @Inject
  TransactionHelper th;

  ELanguage lngEn;
  ELanguage lngSl;

  ZonedDateTime testStart;


  public static ECountry generateCountry(String code) {
    ECountry country = new ECountry();
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

    ECountry countryUk = generateCountry(coUkCode);
    countryUk.setTitle(lngSlCode, "Združeno kraljestvo");
    countryUk.setLanguage(lngSl);

    ECountry countrySi = generateCountry(coSiCode);
    countrySi.setTitle(lngSlCode, "Slovenija");
    countrySi.setLanguage(lngSl);

    countryRepository.save(countryUk);
    countryRepository.save(countrySi);

    testStart = ZonedDateTime.now();
  }

  @AfterAll
  @Transactional
  public void tearDown() {
    ECountry countryUk = this.countryRepository.findById(coUkCode);
    countryRepository.delete(countryUk);
    ECountry countrySi = this.countryRepository.findById(coSiCode);
    countryRepository.delete(countrySi);
  }

  @Test
  @Order(1)
  public void addRemoveTranslations() {
    th.transact(() -> {
      ECountry country = this.countryRepository.findById(coUkCode);
      Assertions.assertEquals(0, country.getTranslations().size());
      country.setTitleTranslation(lngEnCode, "United Kingdom");
      this.countryRepository.save(country);
    });
    th.transact(() -> {
      ECountry country = this.countryRepository.findById(coUkCode);
      Assertions.assertEquals(1, country.getTranslations().size());
      Assertions.assertTrue(country.removeTranslation(Locale.ENGLISH));
      this.countryRepository.save(country);
    });
    th.transact(() -> {
      ECountry country = this.countryRepository.findById(coUkCode);
      Assertions.assertEquals(0, country.getTranslations().size());
    });
  }

  @Test
  @Transactional
  @Order(2)
  public void changeInlineTranslation() {
    th.transact(() -> {
      ECountry tmpCountry = this.countryRepository.findById(coUkCode);
      tmpCountry.setTitle(lngSlCode, "Burek");
      this.countryRepository.save(tmpCountry);
    });
    th.transact(() -> {
      ECountry tmpCountry = this.countryRepository.findById(coUkCode);
      Assertions.assertEquals("Burek", tmpCountry.getTranslationOrTitle(lngSlCode));
      tmpCountry.setTitle(lngSlCode, "Združeno kraljestvo");
      this.countryRepository.save(tmpCountry);
    });
  }
}
