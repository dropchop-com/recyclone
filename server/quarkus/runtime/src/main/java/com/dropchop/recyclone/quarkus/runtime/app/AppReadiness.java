package com.dropchop.recyclone.quarkus.runtime.app;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 10. 2025.
 */
@Readiness
@ApplicationScoped
public class AppReadiness implements HealthCheck {

  private volatile boolean ready;

  public void markReady(String value) { this.ready = true; }

  @Override
  public HealthCheckResponse call() {
    return ready ? HealthCheckResponse.up("app") : HealthCheckResponse.down("app");
  }
}
