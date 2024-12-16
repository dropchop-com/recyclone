package com.dropchop.recyclone.quarkus.deployment.registry;

import com.dropchop.recyclone.base.api.model.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.base.api.model.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.base.dto.model.security.LoginAccount;
import com.dropchop.recyclone.base.dto.model.security.TokenAccount;
import com.dropchop.recyclone.base.dto.model.tagging.LanguageGroup;
import com.dropchop.recyclone.base.es.mapper.tagging.TagToDtoMapper;
import com.dropchop.recyclone.base.es.mapper.tagging.TagToDtoMapperImpl;
import com.dropchop.recyclone.base.es.model.tagging.EsLanguageGroup;
import com.dropchop.recyclone.base.jpa.model.security.JpaLoginAccount;
import com.dropchop.recyclone.base.jpa.model.security.JpaTokenAccount;
import com.dropchop.recyclone.base.jpa.model.tagging.JpaLanguageGroup;
import com.dropchop.recyclone.quarkus.runtime.rest.RestMapping;
import com.dropchop.recyclone.quarkus.runtime.app.RecycloneApplicationImpl;
import com.dropchop.recyclone.base.jpa.mapper.security.UserAccountToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.security.UserAccountToDtoMapperImpl;
import io.quarkus.test.QuarkusUnitTest;
import jakarta.inject.Inject;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class RegistryTest {

    // Start unit test with your extension loaded
    @RegisterExtension
    static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
        .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
            .addClasses(
                UserAccountToDtoMapper.class,
                UserAccountToDtoMapperImpl.class,
                com.dropchop.recyclone.base.jpa.mapper.tagging.TagToDtoMapper.class,
                com.dropchop.recyclone.base.jpa.mapper.tagging.TagToDtoMapperImpl.class,
                TagToDtoMapper.class,
                TagToDtoMapperImpl.class,
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
        Assertions.assertEquals(Set.of(JpaLoginAccount.class), config.mapsTo(LoginAccount.class));
        Assertions.assertEquals(Set.of(LoginAccount.class), config.mapsTo(JpaLoginAccount.class));
        Assertions.assertEquals(Set.of(JpaTokenAccount.class), config.mapsTo(TokenAccount.class));
        Assertions.assertEquals(Set.of(TokenAccount.class), config.mapsTo(JpaTokenAccount.class));
    }

    @Test
    public void getMapperSubTypeConfig() {
        Assertions.assertEquals(Set.of(JpaLoginAccount.class), mapperSubTypeConfig.mapsTo(LoginAccount.class));
        Assertions.assertEquals(Set.of(LoginAccount.class), mapperSubTypeConfig.mapsTo(JpaLoginAccount.class));
        Assertions.assertEquals(Set.of(JpaTokenAccount.class), mapperSubTypeConfig.mapsTo(TokenAccount.class));
        Assertions.assertEquals(Set.of(TokenAccount.class), mapperSubTypeConfig.mapsTo(JpaTokenAccount.class));
    }

    @Test
    public void getMultiMapperSubTypeConfig() {
        Assertions.assertEquals(
            Set.of(JpaLanguageGroup.class, EsLanguageGroup.class),
            mapperSubTypeConfig.mapsTo(LanguageGroup.class)
        );
    }
}
