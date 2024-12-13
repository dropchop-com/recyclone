package com.dropchop.recyclone.quarkus.it.repo.jpa.security;


import com.dropchop.recyclone.model.entity.jpa.localization.JpaCountry;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaLanguage;
import com.dropchop.recyclone.model.entity.jpa.security.JpaLoginAccount;
import com.dropchop.recyclone.model.entity.jpa.security.JpaTokenAccount;
import com.dropchop.recyclone.model.entity.jpa.security.JpaUser;
import com.dropchop.recyclone.base.api.repo.TransactionHelper;
import com.dropchop.recyclone.repo.jpa.blaze.localization.CountryRepository;
import com.dropchop.recyclone.repo.jpa.blaze.localization.LanguageRepository;
import com.dropchop.recyclone.repo.jpa.blaze.security.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;
import java.util.UUID;

import static com.dropchop.recyclone.quarkus.it.repo.jpa.localization.CountryRepositoryTest.coUkCode;
import static com.dropchop.recyclone.quarkus.it.repo.jpa.localization.CountryRepositoryTest.generateCountry;
import static com.dropchop.recyclone.quarkus.it.repo.jpa.localization.LanguageRepositoryTest.lngEnCode;
import static com.dropchop.recyclone.quarkus.it.repo.jpa.localization.LanguageRepositoryTest.lngSlCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

  private static final UUID USER_UUID = UUID.fromString("2e1d057d-527c-4128-a0aa-d203ffe8073d");
  private static final String TOKEN = "random-token";
  private static final String LOGIN_NAME = "random-login-name";

  @Inject
  LanguageRepository languageRepository;

  @Inject
  CountryRepository countryRepository;

  @Inject
  UserRepository userRespository;

  @Inject
  TransactionHelper th;

  @BeforeAll
  @Transactional
  public void setup() {
    JpaLanguage lngSl = languageRepository.findById(lngSlCode);
    JpaLanguage lngEn = languageRepository.findById(lngEnCode);
    JpaCountry countryUk = generateCountry(coUkCode);
    countryUk.setTitle(lngSlCode, "Združeno kraljestvo");
    countryUk.setLanguage(lngSl);
    countryUk.setCreated(ZonedDateTime.now());
    countryUk.setModified(ZonedDateTime.now());
    countryRepository.save(countryUk);

    JpaUser user = new JpaUser();
    user.setUuid(USER_UUID);
    user.setCreated(ZonedDateTime.now());
    user.setModified(ZonedDateTime.now());
    user.setFirstName("Testko");
    user.setLastName("Testic");
    user.setLanguage(lngEn);
    user.setCountry(countryUk);
    this.userRespository.save(user);
  }

  @AfterAll
  @Transactional
  public void tearDown() {
    JpaUser user = this.userRespository.findById(USER_UUID);
    userRespository.delete(user);
    JpaCountry countryUk = countryRepository.findById(coUkCode);
    countryRepository.delete(countryUk);
  }

  @Test
  @Order(1)
  public void testStoredUser() {
    JpaUser user = this.userRespository.findById(USER_UUID);
    assertEquals("Testko", user.getFirstName());
    assertEquals("Testic", user.getLastName());
  }

  @Test
  @Order(2)
  public void testStoreUserAccounts() {
    th.transact(() -> {
      JpaUser user = this.userRespository.findById(USER_UUID);

      JpaLoginAccount loginAccount = new JpaLoginAccount();
      loginAccount.setUuid(UUID.randomUUID());
      loginAccount.setCreated(ZonedDateTime.now());
      loginAccount.setModified(ZonedDateTime.now());
      loginAccount.setLoginName(LOGIN_NAME);
      loginAccount.setPassword("test");
      loginAccount.setTitle("Login account");

      JpaTokenAccount tokenAccount = new JpaTokenAccount();
      tokenAccount.setUuid(UUID.randomUUID());
      tokenAccount.setCreated(ZonedDateTime.now());
      tokenAccount.setModified(ZonedDateTime.now());
      tokenAccount.setToken(TOKEN);
      loginAccount.setTitle("Token account");
      user.addAccount(loginAccount);
      user.addAccount(tokenAccount);

      this.userRespository.save(user);

    });

    th.transact(() -> {
      JpaUser tmpUser = this.userRespository.findById(USER_UUID);
      assertEquals(2, tmpUser.getAccounts().size());
    });
  }


  @Test
  @Order(3)
  public void testFindByLoginName() {
    JpaUser tmpUser = this.userRespository.findByLoginName(LOGIN_NAME);
    assertNotNull(tmpUser);
  }


  @Test
  @Order(4)
  public void testFindByToken() {
    JpaUser tmpUser = this.userRespository.findByToken(TOKEN);
    assertNotNull(tmpUser);
  }
}
