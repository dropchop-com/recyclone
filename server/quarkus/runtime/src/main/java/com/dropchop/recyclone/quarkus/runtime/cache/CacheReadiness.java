package com.dropchop.recyclone.quarkus.runtime.cache;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 10. 2025.
 */
@Readiness
@ApplicationScoped
public class CacheReadiness implements HealthCheck {

  private volatile boolean ready;

  public void markReady() { this.ready = true; }

  @Override
  public HealthCheckResponse call() {
    return ready ? HealthCheckResponse.up("cache") : HealthCheckResponse.down("cache");
  }
}
