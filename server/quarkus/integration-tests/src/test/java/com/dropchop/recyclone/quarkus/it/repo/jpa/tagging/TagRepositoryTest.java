package com.dropchop.recyclone.quarkus.it.repo.jpa.tagging;

import com.dropchop.recyclone.base.api.model.attr.*;
import com.dropchop.recyclone.base.jpa.model.tagging.JpaLanguageGroup;
import com.dropchop.recyclone.base.jpa.repo.tagging.TagRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static com.dropchop.recyclone.base.api.model.marker.HasAttributes.getAttributeValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 07. 22.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TagRepositoryTest {

  @Inject
  TagRepository repository;

  @Transactional
  @SuppressWarnings("UnusedReturnValue")
  public <T, R> R transact(Function<T, R> function, T t) {
    return function.apply(t);
  }

  @Test
  public void testStorJpaLanguageGroup1() {
    JpaLanguageGroup languageGroup1 = new JpaLanguageGroup();
    languageGroup1.setName("celtic");
    languageGroup1.setTitle("Celtic");
    languageGroup1.setLang("en");
    languageGroup1.addAttribute(
      AttributeBool.builder().name("test1Bool1").value(Boolean.TRUE).build()
    );
    languageGroup1.addAttribute(
      AttributeBool.builder().name("test1Bool2").value(Boolean.FALSE).build()
    );
    languageGroup1.addAttribute(
      AttributeValueList.builder()
        .name("test1List").value(
          List.of(
            "test1",
            "test2",
            "test3"
          )
        )
        .build()
    );
    languageGroup1.addAttribute(
      AttributeSet.builder()
        .name("test1Set").value(Set.of(
          AttributeBool.builder().name("test1Bool").value(Boolean.FALSE).build(),
          AttributeString.builder().name("test1Color").value("#AFAFAF").build(),
          AttributeString.builder().name("test1Keyword").value("L'Oreal").build()
        ))
        .build()
    );

    JpaLanguageGroup languageGroup2 = new JpaLanguageGroup();
    languageGroup2.setName("germanic");
    languageGroup2.setTitle("Germanic");
    languageGroup2.setLang("en");
    languageGroup2.addAttribute(
      AttributeBool.builder().name("test2Bool1").value(Boolean.TRUE).build()
    );
    languageGroup2.addAttribute(
      AttributeBool.builder().name("test2Bool2").value(Boolean.FALSE).build()
    );
    languageGroup2.addAttribute(
      AttributeValueList.builder()
        .name("test2List").value(
          List.of(
            "test1",
            "test2",
            "test3"
          )
        )
        .build()
    );
    languageGroup2.addAttribute(
      AttributeSet.builder()
        .name("test2Set").value(Set.of(
          AttributeBool.builder().name("test2Bool").value(Boolean.FALSE).build(),
          AttributeString.builder().name("test2Color").value("#ADADAD").build(),
          AttributeString.builder().name("test2Keyword").value("H &\" M").build()
        ))
        .build()
    );

    transact(lg -> repository.save(lg), languageGroup1);
    transact(lg -> repository.save(lg), languageGroup2);
    JpaLanguageGroup group1 = repository.findById(languageGroup1.getUuid());
    assertNotNull(group1);
    assertEquals(Boolean.TRUE, group1.getAttributeValue("test1Bool1"));
    assertEquals(Boolean.FALSE, group1.getAttributeValue("test1Bool2"));
    assertEquals(List.of(
      "test1",
      "test2",
      "test3"
    ), group1.getAttributeValue("test1List"));
    Set<Attribute<?>> values1 = group1.getAttributeValue("test1Set");
    assertNotNull(values1);
    assertEquals(Boolean.FALSE, getAttributeValue(values1, "test1Bool"));
    assertEquals("#AFAFAF", getAttributeValue(values1, "test1Color"));
    assertEquals("L'Oreal", getAttributeValue(values1, "test1Keyword"));

    Attribute<?> attributeSet2 = group1.getAttribute("test1Set");
    if (attributeSet2 instanceof AttributeSet set2) {
      assertEquals(Boolean.FALSE, set2.getAttributeValue("test1Bool"));
      assertEquals("#AFAFAF", set2.getAttributeValue("test1Color"));
      assertEquals("L'Oreal", set2.getAttributeValue("test1Keyword"));
    }


    JpaLanguageGroup group2 = repository.findById(languageGroup2.getUuid());
    assertNotNull(group2);
    assertEquals(Boolean.TRUE, group2.getAttributeValue("test2Bool1"));
    assertEquals(Boolean.FALSE, group2.getAttributeValue("test2Bool2"));
    assertEquals(List.of(
      "test1",
      "test2",
      "test3"
    ), group2.getAttributeValue("test2List"));
    Set<Attribute<?>> values2 = group2.getAttributeValue("test2Set");
    assertEquals(Boolean.FALSE, getAttributeValue(values2, "test2Bool"));
    assertEquals("#ADADAD", getAttributeValue(values2, "test2Color"));
    assertEquals("H &\" M", getAttributeValue(values2, "test2Keyword"));
  }
}
