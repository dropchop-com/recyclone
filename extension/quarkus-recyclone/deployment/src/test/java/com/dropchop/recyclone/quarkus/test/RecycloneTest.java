package com.dropchop.recyclone.quarkus.test;

import com.dropchop.recyclone.quarkus.runtime.RecylconeRegistryService;
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
            //.addClasses(RecylconeRegistryService.class)
            //.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
        );


    @Inject
    RecylconeRegistryService service;

    @Test
    public void writeYourOwnUnitTest() {
        Map<String, String> classNames = service.getMappingClassNames();
        //TODO: write tests
        Assertions.assertTrue(true, "Add some assertions to " + classNames);
    }
}
