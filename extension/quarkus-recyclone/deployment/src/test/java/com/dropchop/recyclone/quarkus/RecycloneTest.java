package com.dropchop.recyclone.quarkus;

import com.dropchop.recyclone.model.api.filtering.PolymorphicRegistry;
import com.dropchop.recyclone.model.api.filtering.PolymorphicRegistry.SerializationConfig;
import com.dropchop.recyclone.model.api.filtering.PolymorphicRegistryConfig;
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
            /*.addClass(PolymorphicRegistry.class)
            .addClass(SerializationConfig.class)
            .addClass(PolymorphicRegistryConfig.class)*/
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
    RecylconeRegistryService service;

    @Test
    public void writeYourOwnUnitTest() {

        Map<String, Class<?>> expected = Map.of(
            LoginAccount.class.getSimpleName(), LoginAccount.class,
            TokenAccount.class.getSimpleName(), TokenAccount.class
        );
        Map<String, Class<?>> got = service.getSerializationConfigs()
            .stream().findFirst().orElseThrow().getSubTypeMap();
        //Assertions.assertTrue(true, "Add some assertions to " + classMap);
        //Assertions.assertTrue(true, "Add some assertions to " + typeMap);
        Assertions.assertEquals(expected, got);
    }
}
