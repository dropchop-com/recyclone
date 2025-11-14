package com.dropchop.recyclone.quarkus.deployment.registry;

import com.dropchop.recyclone.base.es.mapper.tagging.TagToDtoMapper;
import com.dropchop.recyclone.base.es.mapper.tagging.TagToDtoMapperImpl;
import com.dropchop.recyclone.base.jpa.mapper.localization.CountryToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.localization.CountryToDtoMapperImpl;
import com.dropchop.recyclone.base.jpa.mapper.localization.LanguageToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.localization.LanguageToDtoMapperImpl;
import com.dropchop.recyclone.base.jpa.mapper.security.*;
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
                    ActionToDtoMapper.class,
                    ActionToDtoMapperImpl.class,
                    DomainToDtoMapper.class,
                    DomainToDtoMapperImpl.class,
                    CountryToDtoMapper.class,
                    CountryToDtoMapperImpl.class,
                    LanguageToDtoMapper.class,
                    LanguageToDtoMapperImpl.class,
                    PermissionToDtoMapper.class,
                    PermissionToDtoMapperImpl.class,
                    UserToDtoMapper.class,
                    UserToDtoMapperImpl.class,
                    UserAccountToDtoMapper.class,
                    UserAccountToDtoMapperImpl.class,
                    TagToDtoMapper.class,
                    TagToDtoMapperImpl.class,
                    com.dropchop.recyclone.base.jpa.mapper.tagging.TagToDtoMapper.class,
                    com.dropchop.recyclone.base.jpa.mapper.tagging.TagToDtoMapperImpl.class,
                    TestShiroEnvironmentProvider.class
                )
        );

    @Test
    public void writeYourOwnDevModeTest() {
        // Write your dev mode tests here - see the testing extension guide https://quarkus.io/guides/writing-extensions#testing-hot-reload for more information
        Assertions.assertTrue(true, "Add dev mode assertions to " + getClass().getName());
    }
}
