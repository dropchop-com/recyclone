package com.dropchop.recyclone.quarkus.runtime.elasticsearch;

import io.quarkus.runtime.StartupEvent;
import io.vertx.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import static com.dropchop.recyclone.base.api.model.invoke.Constants.Messages.CACHE_STORAGE_INIT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 5/14/25.
 */
@ApplicationScoped
public class DefaultInitializerSignaler {

  @Inject
  EventBus eventBus;

  public void onStart(@Observes StartupEvent event) {
    eventBus.send(CACHE_STORAGE_INIT, "skipped");
  }
}
