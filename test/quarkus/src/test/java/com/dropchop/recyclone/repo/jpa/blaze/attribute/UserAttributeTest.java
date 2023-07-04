package com.dropchop.recyclone.repo.jpa.blaze.attribute;

import com.dropchop.recyclone.model.api.attr.AttributeBool;
import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.model.entity.jpa.base.EUuid;
import com.dropchop.recyclone.model.entity.jpa.localization.ECountry;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.model.entity.jpa.security.EUser;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.api.TransactionHelper;
import com.dropchop.recyclone.repo.jpa.blaze.localization.CountryRepository;
import com.dropchop.recyclone.repo.jpa.blaze.localization.LanguageRepository;
import com.dropchop.recyclone.repo.jpa.blaze.security.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;
import static com.dropchop.recyclone.repo.jpa.blaze.localization.CountryRepositoryTest.coUkCode;
import static com.dropchop.recyclone.repo.jpa.blaze.localization.CountryRepositoryTest.generateCountry;
import static com.dropchop.recyclone.repo.jpa.blaze.localization.LanguageRepositoryTest.lngEnCode;
import static com.dropchop.recyclone.repo.jpa.blaze.localization.LanguageRepositoryTest.lngSlCode;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Kristijan Sečan <kristijan.secan@dropchop.com> on 4. 07. 23.
 */
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserAttributeTest {
    private static final UUID USER_UUID = UUID.fromString("2e1d057d-527c-4128-a0aa-d203ffe8073d");
    private static final String TOKEN = "random-token";
    private static final String LOGIN_NAME = "random-login-name";

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

        user.setAttributeValue("ChangeMeWithAdd", Boolean.FALSE);
        user.setAttributeValue("ChangeMeWithSet", Boolean.FALSE);

        this.userRespository.save(user);
    }

    @AfterAll
    @Transactional
    public void tearDown() {
        EUser<EUuid> user = this.userRespository.findById(USER_UUID);
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
        assertEquals(Boolean.FALSE, user.getAttributeValue("ChangeMeWithAdd"));
        assertEquals(Boolean.FALSE, user.getAttributeValue("ChangeMeWithSet"));
    }

    @Test
    @Order(2)
    @Transactional
    public void testUpdateUserAttributeWithAdd() {
        EUser<EUuid> user = this.userRespository.findById(USER_UUID);
        assertEquals(Boolean.FALSE, user.getAttributeValue("ChangeMeWithAdd"));
        user.addAttribute(new AttributeBool("ChangeMeWithAdd", Boolean.TRUE));
        this.userRespository.save(user);
        EUser<EUuid> changedUser = this.userRespository.findById(USER_UUID);
        assertEquals(Boolean.TRUE, changedUser.getAttributeValue("ChangeMeWithAdd"));
    }

    @Test
    @Order(3)
    @Transactional
    public void testUpdateUserAttributeWithSetValue() {
        EUser<EUuid> user = this.userRespository.findById(USER_UUID);
        assertEquals(Boolean.FALSE, user.getAttributeValue("ChangeMeWithSet"));
        user.setAttributeValue("ChangeMeWithSet", Boolean.TRUE);
        this.userRespository.save(user);
        EUser<EUuid> changedUser = this.userRespository.findById(USER_UUID);
        assertEquals(Boolean.TRUE, changedUser.getAttributeValue("ChangeMeWithSet"));
    }


}
