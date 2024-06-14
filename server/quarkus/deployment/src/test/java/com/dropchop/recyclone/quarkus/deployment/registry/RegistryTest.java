package com.dropchop.recyclone.quarkus.deployment.registry;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.dto.security.LoginAccount;
import com.dropchop.recyclone.model.dto.security.TokenAccount;
import com.dropchop.recyclone.model.entity.jpa.security.JpaLoginAccount;
import com.dropchop.recyclone.model.entity.jpa.security.JpaTokenAccount;
import com.dropchop.recyclone.quarkus.runtime.rest.RestMapping;
import com.dropchop.recyclone.quarkus.runtime.app.RecycloneApplicationImpl;
import com.dropchop.recyclone.mapper.jpa.security.UserAccountToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.security.UserAccountToDtoMapperImpl;
import io.quarkus.test.QuarkusUnitTest;
import jakarta.inject.Inject;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Map;

@SuppressWarnings("unused")
public class RegistryTest {

    // Start unit test with your extension loaded
    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
        .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
            .addClasses(
                UserAccountToDtoMapper.class,
                UserAccountToDtoMapperImpl.class,
                TestShiroEnvironmentProvider.class
            )
        );

    @Inject
    RecycloneApplicationImpl service;

    @Inject
    MapperSubTypeConfig mapperSubTypeConfig;

    @Inject
    JsonSerializationTypeConfig jsonSerializationTypeConfig;

    @Inject
    @SuppressWarnings("CdiInjectionPointsInspection")
    RestMapping restMapping;

    @Test
    public void getJsonSerializationTypeConfigService() {

        Map<String, Class<?>> expected = Map.of(
            LoginAccount.class.getSimpleName(), LoginAccount.class,
            TokenAccount.class.getSimpleName(), TokenAccount.class
        );
        Map<String, Class<?>> got = service.getJsonSerializationTypeConfig().getSubTypeMap();
        //Assertions.assertEquals(expected, got);
        Assertions.assertTrue(got.entrySet().containsAll(expected.entrySet()));
    }

    @Test
    public void getJsonSerializationTypeConfig() {
        Map<String, Class<?>> expected = Map.of(
            LoginAccount.class.getSimpleName(), LoginAccount.class,
            TokenAccount.class.getSimpleName(), TokenAccount.class
        );
        Map<String, Class<?>> got = jsonSerializationTypeConfig.getSubTypeMap();
        Assertions.assertTrue(got.entrySet().containsAll(expected.entrySet()));
    }

    @Test
    public void getMapperSubTypeConfigService() {
        MapperSubTypeConfig config = service.getMapperSubTypeConfig();
        Assertions.assertEquals(JpaLoginAccount.class, config.mapsTo(LoginAccount.class));
        Assertions.assertEquals(LoginAccount.class, config.mapsTo(JpaLoginAccount.class));
        Assertions.assertEquals(JpaTokenAccount.class, config.mapsTo(TokenAccount.class));
        Assertions.assertEquals(TokenAccount.class, config.mapsTo(JpaTokenAccount.class));
    }

    @Test
    public void getMapperSubTypeConfig() {
        Assertions.assertEquals(JpaLoginAccount.class, mapperSubTypeConfig.mapsTo(LoginAccount.class));
        Assertions.assertEquals(LoginAccount.class, mapperSubTypeConfig.mapsTo(JpaLoginAccount.class));
        Assertions.assertEquals(JpaTokenAccount.class, mapperSubTypeConfig.mapsTo(TokenAccount.class));
        Assertions.assertEquals(TokenAccount.class, mapperSubTypeConfig.mapsTo(JpaTokenAccount.class));
    }
}
