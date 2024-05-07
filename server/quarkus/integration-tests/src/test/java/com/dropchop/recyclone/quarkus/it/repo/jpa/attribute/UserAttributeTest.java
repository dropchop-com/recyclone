package com.dropchop.recyclone.quarkus.it.repo.jpa.attribute;

import com.dropchop.recyclone.model.api.attr.AttributeBool;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaCountry;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaLanguage;
import com.dropchop.recyclone.model.entity.jpa.security.JpaUser;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.localization.CountryRepository;
import com.dropchop.recyclone.repo.jpa.blaze.localization.LanguageRepository;
import com.dropchop.recyclone.repo.jpa.blaze.security.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.time.ZonedDateTime;
import java.util.UUID;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;
import static com.dropchop.recyclone.quarkus.it.repo.jpa.localization.CountryRepositoryTest.coUkCode;
import static com.dropchop.recyclone.quarkus.it.repo.jpa.localization.CountryRepositoryTest.generateCountry;
import static com.dropchop.recyclone.quarkus.it.repo.jpa.localization.LanguageRepositoryTest.lngEnCode;
import static com.dropchop.recyclone.quarkus.it.repo.jpa.localization.LanguageRepositoryTest.lngSlCode;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Kristijan Sečan <kristijan.secan@dropchop.com> on 4. 07. 23.
 */
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserAttributeTest {
    private static final UUID USER_UUID = UUID.fromString("2e1d057d-527c-4128-a0aa-d203ffe8073d");
    //private static final String TOKEN = "random-token";
    //private static final String LOGIN_NAME = "random-login-name";

    @Inject
    @RepositoryType(RECYCLONE_JPA_DEFAULT)
    LanguageRepository languageRepository;

    @Inject
    @RepositoryType(RECYCLONE_JPA_DEFAULT)
    CountryRepository countryRepository;

    @Inject
    @RepositoryType(RECYCLONE_JPA_DEFAULT)
    UserRepository userRespository;

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

        user.setAttributeValue("ChangeMeWithAdd", Boolean.FALSE);
        user.setAttributeValue("ChangeMeWithSet", Boolean.FALSE);

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
        assertEquals(Boolean.FALSE, user.getAttributeValue("ChangeMeWithAdd"));
        assertEquals(Boolean.FALSE, user.getAttributeValue("ChangeMeWithSet"));
    }

    @Test
    @Order(2)
    @Transactional
    public void testUpdateUserAttributeWithAdd() {
        JpaUser user = this.userRespository.findById(USER_UUID);
        assertEquals(Boolean.FALSE, user.getAttributeValue("ChangeMeWithAdd"));
        user.addAttribute(new AttributeBool("ChangeMeWithAdd", Boolean.TRUE));
        this.userRespository.save(user);
        JpaUser changedUser = this.userRespository.findById(USER_UUID);
        assertEquals(Boolean.TRUE, changedUser.getAttributeValue("ChangeMeWithAdd"));
    }

    @Test
    @Order(3)
    @Transactional
    public void testUpdateUserAttributeWithSetValue() {
        JpaUser user = this.userRespository.findById(USER_UUID);
        assertEquals(Boolean.FALSE, user.getAttributeValue("ChangeMeWithSet"));
        user.setAttributeValue("ChangeMeWithSet", Boolean.TRUE);
        this.userRespository.save(user);
        JpaUser changedUser = this.userRespository.findById(USER_UUID);
        assertEquals(Boolean.TRUE, changedUser.getAttributeValue("ChangeMeWithSet"));
    }


}
