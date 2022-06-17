package com.dropchop.recyclone.test.quarkus;

import com.dropchop.recyclone.service.api.mapping.ContextAwarePolymorphicRegistry;
import com.dropchop.recyclone.service.api.mapping.DefaultContextAwarePolymorphicRegistry;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 06. 22.
 */
@ApplicationScoped
public class TestApplicationConfiguration {

  @Produces
  ContextAwarePolymorphicRegistry getContextAwarePolymorphicRegistry() {
    return new DefaultContextAwarePolymorphicRegistry();
  }
}
