package com.dropchop.recyclone.repo.jpa.blaze.localization;

import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleDescriptionTranslation;
import com.dropchop.recyclone.model.entity.jpa.security.EAction;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.security.ActionRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 05. 22.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RepositoryTranslationTest {

  @Inject
  @RepositoryType(RCYN_DEFAULT)
  ActionRepository actionRepository;

  @Test
  @Order(1)
  @Transactional
  public void translationsRemove() {
    EAction action = actionRepository.findById(Constants.Actions.VIEW);
    if (action != null) {
      action.getTranslations().remove(new ETitleDescriptionTranslation("sl", "Karkoli"));
      assertEquals(0, action.getTranslations().size());
    }
  }

  @Test
  @Order(2)
  @Transactional
  public void testTranslationsRemovedAndAddBack() {
    EAction action = actionRepository.findById(Constants.Actions.VIEW);
    if (action != null) {
      assertEquals(0, action.getTranslations().size());
      action.getTranslations().add(new ETitleDescriptionTranslation("sl", "Ogled"));
      assertEquals(1, action.getTranslations().size());
    }
  }

  @Test
  @Order(3)
  @Transactional
  public void testTranslationsRemovedAddedTranslationBack() {
    EAction action = actionRepository.findById(Constants.Actions.VIEW);
    if (action != null) {
      assertEquals(1, action.getTranslations().size());
    }
  }

  @Test
  @Order(4)
  @Transactional
  public void removeTranslation() {
    EAction action = actionRepository.findById(Constants.Actions.VIEW);
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
