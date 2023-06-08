package com.dropchop.recyclone.repo.jpa.blaze.security;


import com.dropchop.recyclone.model.api.security.UserAccount;
import com.dropchop.recyclone.model.entity.jpa.base.EUuid;
import com.dropchop.recyclone.model.entity.jpa.localization.ECountry;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.model.entity.jpa.security.ELoginAccount;
import com.dropchop.recyclone.model.entity.jpa.security.ETokenAccount;
import com.dropchop.recyclone.model.entity.jpa.security.EUser;
import com.dropchop.recyclone.model.entity.jpa.security.EUserAccount;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.localization.CountryRepository;
import com.dropchop.recyclone.repo.jpa.blaze.localization.LanguageRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.hibernate.Session;
import org.junit.BeforeClass;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

  private static UUID USER_UUID = UUID.randomUUID();;
  private static ELanguage lngEn = new ELanguage("en");
  private static ECountry countryUk = new ECountry("uk");

  @Inject
  @RepositoryType(RCYN_DEFAULT)
  LanguageRepository lngRepository;


  @Inject
  @RepositoryType(RCYN_DEFAULT)
  CountryRepository countryRepository;


  @Inject
  @RepositoryType(RCYN_DEFAULT)
  UserRepository userRespository;


  @Inject
  EntityManager em;

  @Transactional
  @SuppressWarnings("UnusedReturnValue")
  public <T, R> R transact(Function<T, R> function, T t) {
    return function.apply(t);
  }


  @Test
  @Order(1)
  public void testStoreUserWithAccounts() {
    EUser<EUuid> user = new EUser<>();
    user.setUuid(USER_UUID);
    user.setCreated(ZonedDateTime.now());
    user.setModified(ZonedDateTime.now());
    user.setFirstName("Testko");
    user.setLastName("Testic");
    user.setLanguage(lngEn);
    user.setCountry(countryUk);

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

    this.transact(c -> this.countryRepository.save(countryUk), countryUk);
    this.transact(u -> this.userRespository.save(user), user);


    EUser tmpUser = this.userRespository.findById(USER_UUID);
    assertNotNull(tmpUser);
    assertNotNull(tmpUser.getAccounts());
    assertEquals("Testko", tmpUser.getFirstName());
    assertEquals("Testic", tmpUser.getLastName());
    assertEquals(2, tmpUser.getAccounts().size());

  }


}
