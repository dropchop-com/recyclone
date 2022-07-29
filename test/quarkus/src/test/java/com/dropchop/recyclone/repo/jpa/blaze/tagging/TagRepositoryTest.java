package com.dropchop.recyclone.repo.jpa.blaze.tagging;

import com.dropchop.recyclone.model.api.attr.AttributeBool;
import com.dropchop.recyclone.model.entity.jpa.tagging.ELanguageGroup;
import com.dropchop.recyclone.repo.api.RepositoryType;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.function.Function;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 07. 22.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TagRepositoryTest {

  @Inject
  @RepositoryType(RCYN_DEFAULT)
  TagRepository repository;

  @Transactional
  @SuppressWarnings("UnusedReturnValue")
  public <T, R> R transact(Function<T, R> function, T t) {
    return function.apply(t);
  }

  @Test
  public void testStoreLanguageGroup1() {
    ELanguageGroup languageGroup = new ELanguageGroup();
    languageGroup.setName("ex-yugoslav");
    languageGroup.setTitle("Ex Yugoslavic");
    languageGroup.setLang("en");
    languageGroup.addAttribute(
      AttributeBool.builder().name("testBool1").value(Boolean.TRUE).build()
    );
    languageGroup.addAttribute(
      AttributeBool.builder().name("testBool1").value(Boolean.FALSE).build()
    );
    transact(lg -> repository.save(lg), languageGroup);
  }
}
