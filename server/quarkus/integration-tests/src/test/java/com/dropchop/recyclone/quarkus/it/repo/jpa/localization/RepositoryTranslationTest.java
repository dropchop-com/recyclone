package com.dropchop.recyclone.quarkus.it.repo.jpa.localization;

import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.jpa.model.localization.JpaTitleDescriptionTranslation;
import com.dropchop.recyclone.base.jpa.model.security.JpaAction;
import com.dropchop.recyclone.base.jpa.repo.security.ActionRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 05. 22.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RepositoryTranslationTest {

  @Inject
  ActionRepository actionRepository;

  @Test
  @Order(1)
  @Transactional
  public void translationsRemove() {
    JpaAction action = actionRepository.findById(Constants.Actions.VIEW);
    if (action != null) {
      action.getTranslations().remove(new JpaTitleDescriptionTranslation("sl", "Karkoli"));
      assertEquals(0, action.getTranslations().size());
    }
  }

  @Test
  @Order(2)
  @Transactional
  public void testTranslationsRemovedAndAddBack() {
    JpaAction action = actionRepository.findById(Constants.Actions.VIEW);
    if (action != null) {
      assertEquals(0, action.getTranslations().size());
      action.getTranslations().add(new JpaTitleDescriptionTranslation("sl", "Ogled"));
      assertEquals(1, action.getTranslations().size());
    }
  }

  @Test
  @Order(3)
  @Transactional
  public void testTranslationsRemovedAddedTranslationBack() {
    JpaAction action = actionRepository.findById(Constants.Actions.VIEW);
    if (action != null) {
      assertEquals(1, action.getTranslations().size());
    }
  }

  @Test
  @Order(4)
  @Transactional
  public void removeTranslation() {
    JpaAction action = actionRepository.findById(Constants.Actions.VIEW);
    if (action != null) {
      action.removeTranslation("sl");
      assertEquals(0, action.getTranslations().size());
    }
  }

  @Test
  @Order(5)
  @Transactional
  public void testRemovedTranslationAndAddBack() {
    testTranslationsRemovedAndAddBack();
  }

  @Test
  @Order(6)
  @Transactional
  public void testAddedTranslationAddedTranslationBack() {
    testTranslationsRemovedAddedTranslationBack();
  }
}
