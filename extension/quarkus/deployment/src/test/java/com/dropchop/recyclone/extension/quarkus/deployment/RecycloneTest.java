package com.dropchop.recyclone.extension.quarkus.deployment;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.model.api.filtering.RecycloneClassRegistryService;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.dto.security.LoginAccount;
import com.dropchop.recyclone.model.dto.security.TokenAccount;
import com.dropchop.recyclone.model.dto.security.UserAccount;
import com.dropchop.recyclone.model.entity.jpa.security.ELoginAccount;
import com.dropchop.recyclone.model.entity.jpa.security.ETokenAccount;
import com.dropchop.recyclone.service.api.mapping.ToDtoManipulator;
import com.dropchop.recyclone.service.api.mapping.ToDtoManipulatorImpl;
import com.dropchop.recyclone.service.jpa.blaze.security.UserAccountToDtoMapper;
import com.dropchop.recyclone.service.jpa.blaze.security.UserAccountToDtoMapperImpl;
import io.quarkus.test.QuarkusUnitTest;
import jakarta.inject.Inject;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Map;

public class RecycloneTest {

    // Start unit test with your extension loaded
    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
        .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
            .addClasses(ToDtoManipulator.class)
            .addClasses(ToDtoManipulatorImpl.class)
            .addClasses(UserAccountToDtoMapper.class)
            .addClasses(UserAccountToDtoMapperImpl.class)
            .addClasses(UserAccount.class)
            .addClasses(ELoginAccount.class)
            .addClasses(LoginAccount.class)
            .addClasses(ETokenAccount.class)
            .addClasses(TokenAccount.class)
        );

    @Inject
    RecycloneClassRegistryService service;

    @Inject
    MapperSubTypeConfig mapperSubTypeConfig;

    @Inject
    JsonSerializationTypeConfig jsonSerializationTypeConfig;

    @Test
    public void getJsonSerializationTypeConfigService() {

        Map<String, Class<?>> expected = Map.of(
            LoginAccount.class.getSimpleName(), LoginAccount.class,
            TokenAccount.class.getSimpleName(), TokenAccount.class
        );
        Map<String, Class<?>> got = service.getJsonSerializationTypeConfig().getSubTypeMap();
        Assertions.assertEquals(expected, got);
    }

    @Test
    public void getJsonSerializationTypeConfig() {

        Map<String, Class<?>> expected = Map.of(
            LoginAccount.class.getSimpleName(), LoginAccount.class,
            TokenAccount.class.getSimpleName(), TokenAccount.class
        );
        Map<String, Class<?>> got = jsonSerializationTypeConfig.getSubTypeMap();
        Assertions.assertEquals(expected, got);
    }

    @Test
    public void getMapperSubTypeConfigService() {
        MapperSubTypeConfig config = service.getMapperTypeConfig();
        Assertions.assertEquals(ELoginAccount.class, config.mapsTo(LoginAccount.class));
        Assertions.assertEquals(LoginAccount.class, config.mapsTo(ELoginAccount.class));
        Assertions.assertEquals(ETokenAccount.class, config.mapsTo(TokenAccount.class));
        Assertions.assertEquals(TokenAccount.class, config.mapsTo(ETokenAccount.class));
    }

    @Test
    public void getMapperSubTypeConfig() {
        Assertions.assertEquals(ELoginAccount.class, mapperSubTypeConfig.mapsTo(LoginAccount.class));
        Assertions.assertEquals(LoginAccount.class, mapperSubTypeConfig.mapsTo(ELoginAccount.class));
        Assertions.assertEquals(ETokenAccount.class, mapperSubTypeConfig.mapsTo(TokenAccount.class));
        Assertions.assertEquals(TokenAccount.class, mapperSubTypeConfig.mapsTo(ETokenAccount.class));
    }
}
