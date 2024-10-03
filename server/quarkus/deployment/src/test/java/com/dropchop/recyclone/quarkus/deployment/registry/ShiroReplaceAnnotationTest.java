package com.dropchop.recyclone.quarkus.deployment.registry;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.rest.api.localization.LanguageResource;
import io.quarkus.test.QuarkusUnitTest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 30. 09. 24.
 */
public class ShiroReplaceAnnotationTest {
  // Start unit test with your extension loaded
  @RegisterExtension
  static final QuarkusUnitTest unitTest = new QuarkusUnitTest()
      .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
          .addClasses(
              com.dropchop.recyclone.rest.api.localization.LanguageResource.class
          )
      );

  @ApplicationScoped
  @Named("languageResource")
  public static class LanguageResourceImpl implements LanguageResource {
    @Override
    public Result<Language> get() {
      return null;
    }

    @Override
    public List<Language> getRest() {
      return List.of();
    }

    @Override
    public Result<Language> getByCode(String code) {
      return null;
    }

    @Override
    public List<Language> getByCodeRest(String code) {
      return List.of();
    }

    @Override
    public Result<Language> search(CodeParams parameters) {
      return null;
    }

    @Override
    public List<Language> searchRest(CodeParams parameters) {
      return List.of();
    }
  }

  @Inject
  @Named("languageResource")
  LanguageResource resource;

  @Test
  public void test() {
    resource.get();
  }
}
