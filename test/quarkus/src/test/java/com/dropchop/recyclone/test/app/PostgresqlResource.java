package com.dropchop.recyclone.test.app;

import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 01. 22.
 */
@Slf4j
@SuppressWarnings({"OptionalGetWithoutIsPresent", "RedundantSuppression", "unused", "resource", "OptionalUsedAsFieldOrParameterType", "OptionalIsPresent"})
public class PostgresqlResource implements QuarkusTestResourceLifecycleManager, DevServicesContext.ContextAware {

  PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:11.3")
    .withDatabaseName("foo")
    .withUsername("foo")
    .withPassword("secret")
    //.withExposedPorts(5432)
    .withInitScript("struct.sql")
    ;

  private Optional<String> containerNetworkId;

  @Override
  public void setIntegrationTestContext(DevServicesContext context) {
    log.info("Dev services [{}]", context.devServicesProperties());
    containerNetworkId = context.containerNetworkId();
  }

  @Override
  public Map<String, String> start() {
    log.error("Wil start postgres container with network id [{}]", containerNetworkId.isPresent() ? containerNetworkId.get() : null);
    if (containerNetworkId.isPresent()) {
      postgresqlContainer.withNetworkMode(containerNetworkId.get()).start();
    } else {
      postgresqlContainer.start();
    }
    return Collections.singletonMap(
      "quarkus.datasource.url", postgresqlContainer.getJdbcUrl()
    );
  }

  @Override
  public void stop() {
    postgresqlContainer.stop();
  }
}
