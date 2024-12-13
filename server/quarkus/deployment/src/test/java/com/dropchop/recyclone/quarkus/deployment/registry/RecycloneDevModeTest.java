package com.dropchop.recyclone.quarkus.deployment.registry;

import com.dropchop.recyclone.base.jpa.mapper.security.UserAccountToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.security.UserAccountToDtoMapperImpl;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusDevModeTest;

public class RecycloneDevModeTest {

    // Start hot reload (DevMode) test with your extension loaded
    @RegisterExtension
    static final QuarkusDevModeTest devModeTest = new QuarkusDevModeTest()
        .setArchiveProducer(
            () -> ShrinkWrap.create(JavaArchive.class)
                .addClasses(
                    UserAccountToDtoMapper.class,
                    UserAccountToDtoMapperImpl.class,
                    TestShiroEnvironmentProvider.class
                )
        );

    @Test
    public void writeYourOwnDevModeTest() {
        // Write your dev mode tests here - see the testing extension guide https://quarkus.io/guides/writing-extensions#testing-hot-reload for more information
        Assertions.assertTrue(true, "Add dev mode assertions to " + getClass().getName());
    }
}
