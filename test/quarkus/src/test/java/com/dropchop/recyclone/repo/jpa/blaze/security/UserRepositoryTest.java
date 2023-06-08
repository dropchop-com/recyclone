package com.dropchop.recyclone.repo.jpa.blaze.security;


import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.model.entity.jpa.base.EUuid;
import com.dropchop.recyclone.model.entity.jpa.localization.ECountry;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.model.entity.jpa.security.ELoginAccount;
import com.dropchop.recyclone.model.entity.jpa.security.ETokenAccount;
import com.dropchop.recyclone.model.entity.jpa.security.EUser;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.api.TransactionHelper;
import com.dropchop.recyclone.repo.jpa.blaze.localization.CountryRepository;
import com.dropchop.recyclone.repo.jpa.blaze.localization.LanguageRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;
import static com.dropchop.recyclone.repo.jpa.blaze.localization.CountryRepositoryTest.coUkCode;
import static com.dropchop.recyclone.repo.jpa.blaze.localization.CountryRepositoryTest.generateCountry;
import static com.dropchop.recyclone.repo.jpa.blaze.localization.LanguageRepositoryTest.lngEnCode;
import static com.dropchop.recyclone.repo.jpa.blaze.localization.LanguageRepositoryTest.lngSlCode;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

  private static final UUID USER_UUID = UUID.fromString("2e1d057d-527c-4128-a0aa-d203ffe8073d");

  @Inject
  @RepositoryType(Constants.Implementation.RCYN_DEFAULT)
  LanguageRepository languageRepository;

  @Inject
  @RepositoryType(RCYN_DEFAULT)
  CountryRepository countryRepository;

  @Inject
  @RepositoryType(RCYN_DEFAULT)
  UserRepository userRespository;

  @Inject
  TransactionHelper th;


  @BeforeAll
  @Transactional
  public void setup() {
    ELanguage lngSl = languageRepository.findById(lngSlCode);
    ELanguage lngEn = languageRepository.findById(lngEnCode);
    ECountry countryUk = generateCountry(coUkCode);
    countryUk.setTitle(lngSlCode, "Združeno kraljestvo");
    countryUk.setLanguage(lngSl);
    countryUk.setCreated(ZonedDateTime.now());
    countryUk.setModified(ZonedDateTime.now());
    countryRepository.save(countryUk);

    EUser<EUuid> user = new EUser<>();
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
    EUser<?> user = this.userRespository.findById(USER_UUID);
    userRespository.delete(user);
    ECountry countryUk = countryRepository.findById(coUkCode);
    countryRepository.delete(countryUk);
  }

  @Test
  @Order(1)
  public void testStoredUser() {
    EUser<?> user = this.userRespository.findById(USER_UUID);
    assertEquals("Testko", user.getFirstName());
    assertEquals("Testic", user.getLastName());
  }

  @Test
  @Order(2)
  public void testStoreUserAccounts() {
    th.transact(() -> {
      EUser<?> user = this.userRespository.findById(USER_UUID);

      ELoginAccount loginAccount = new ELoginAccount();
      loginAccount.setUuid(UUID.randomUUID());
      loginAccount.setCreated(ZonedDateTime.now());
      loginAccount.setModified(ZonedDateTime.now());
      loginAccount.setLoginName("test");
      loginAccount.setPassword("test");
      loginAccount.setTitle("Login account");

      ETokenAccount tokenAccount = new ETokenAccount();
      tokenAccount.setUuid(UUID.randomUUID());
      tokenAccount.setCreated(ZonedDateTime.now());
      tokenAccount.setModified(ZonedDateTime.now());
      tokenAccount.setToken("token");
      loginAccount.setTitle("Token account");
      user.addAccount(loginAccount);
      user.addAccount(tokenAccount);

      this.userRespository.save(user);

    });

    th.transact(() -> {
      EUser<?> tmpUser = this.userRespository.findById(USER_UUID);
      assertEquals(2, tmpUser.getAccounts().size());
    });
  }
}
