package com.dropchop.shiro.cdi;

import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import com.dropchop.shiro.filter.*;
import com.dropchop.shiro.realm.ShiroMapRealm;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.event.EventBus;
import org.apache.shiro.event.support.DefaultEventBus;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig.Rest.Security.Mechanism.*;

/**
 * Modeled and copied from Shiro Spring.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 01. 22.
 */
@ApplicationScoped
public class DefaultShiroEnvironmentProvider {
  private static final Logger log = LoggerFactory.getLogger(DefaultShiroEnvironmentProvider.class);

  @Produces
  public CacheManager getCacheManager() {
    return null;
  }

  @Produces
  public RolePermissionResolver getRolePermissionResolver() {
    return null;
  }

  @Produces
  public PermissionResolver getPermissionResolver() {
    return new WildcardPermissionResolver();
  }

  @Produces
  public EventBus getEventBus() {
    return new DefaultEventBus();
  }

  @Produces
  public List<Realm> getRealms() {
    return List.of(new ShiroMapRealm(Collections.emptyMap(), Collections.emptyMap()));
  }

  @Inject
  RecycloneBuildConfig recycloneConfig;

  @Produces
  public ApiKeyConfig getApiKeyConfig() {
    if (!recycloneConfig.rest().security().isEmpty()) {
      for (RecycloneBuildConfig.Rest.Security security : recycloneConfig.rest().security()) {
        if (security.mechanism() == API_KEY) {
          return new ApiKeyConfig(
              recycloneConfig.rest().info().title().orElse(null),
              security.permissive(),
              security.headerName(),
              security.queryName()
          );
        }
      }
    }
    ShiroEnabledFilters filterClasses = getEnabledFilters();
    if (filterClasses.contains(ApiKeyHttpAuthenticationFilter.class)
        || filterClasses.contains(UuidAuthenticationFilter.class)) {
      log.warn("Api Key authentication mechanism is not configured!");
    }
    return new ApiKeyConfig(
        recycloneConfig.rest().info().title().orElse(null), Boolean.TRUE, "X-API-Key", "api-key"
    );
  }

  @Produces
  public JwtConfig getJwtConfig() {
    if (!recycloneConfig.rest().security().isEmpty()) {
      for (RecycloneBuildConfig.Rest.Security security : recycloneConfig.rest().security()) {
        if (security.mechanism() == JWT) {
          return new JwtConfig(
              recycloneConfig.rest().info().title().orElse(null),
              security.loginPath(),
              security.permissive(),
              security.issuer().orElse(
                  recycloneConfig.rest().info().contact().name().orElse("Jwt Issuer")
              ),
              security.secret().orElse(null),
              security.timeoutSeconds().orElse(600)
          );
        }
      }
    }
    ShiroEnabledFilters filterClasses = getEnabledFilters();
    if (filterClasses.contains(JwtAuthenticationFilter.class)
        || filterClasses.contains(JwtEveryResponseFilter.class)) {
      log.warn("JWT authentication mechanism is not configured. " +
          "JWT filters will use unsecured JWT tokens for authentication and authorization.");
    }
    return new JwtConfig(
        recycloneConfig.rest().info().title().orElse(null), "/api/security/login/jwt", Boolean.TRUE,
        recycloneConfig.rest().info().contact().name().orElse("Jwt Issuer"), "no-need-for-secrecy", 600
    );
  }

  @Produces
  public BasicConfig getBasicConfig() {
    if (!recycloneConfig.rest().security().isEmpty()) {
      for (RecycloneBuildConfig.Rest.Security security : recycloneConfig.rest().security()) {
        if (security.mechanism() == BASIC_AUTH) {
          return new BasicConfig(
              recycloneConfig.rest().info().title().orElse(null),
              security.permissive()
          );
        }
      }
    }
    ShiroEnabledFilters filterClasses = getEnabledFilters();
    if (filterClasses.contains(BasicHttpAuthenticationFilter.class)) {
      log.info("Basic authentication mechanism is not configured. Using sensible defaults.");
    }
    return new BasicConfig(
        recycloneConfig.rest().info().title().orElse(null), Boolean.TRUE
    );
  }

  @Produces
  public BearerConfig getBearerConfig() {
    if (!recycloneConfig.rest().security().isEmpty()) {
      for (RecycloneBuildConfig.Rest.Security security : recycloneConfig.rest().security()) {
        if (security.mechanism() == BEARER_TOKEN) {
          return new BearerConfig(
              recycloneConfig.rest().info().title().orElse(null),
              security.permissive()
          );
        }
      }
    }
    ShiroEnabledFilters filterClasses = getEnabledFilters();
    if (filterClasses.contains(BearerHttpAuthenticationFilter.class)) {
      log.info("Bearer authentication mechanism is not configured. Using sensible defaults.");
    }
    return new BearerConfig(
        recycloneConfig.rest().info().title().orElse(null), Boolean.TRUE
    );
  }

  @Produces
  public ShiroEnabledFilters getEnabledFilters() {
    return ShiroEnabledFilters.of(
        BasicHttpAuthenticationFilter.class,
        BearerHttpAuthenticationFilter.class
    );
  }
}
