package com.dropchop.shiro.filter;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 01. 22.
 */
@ApplicationScoped
@SuppressWarnings("unused")
public class ApiKeyHttpAuthenticationFilter extends BearerHttpAuthenticationFilter {

  public static final String DEFAULT_API_KEY_NAME = "X-API-KEY";
  public static final String DEFAULT_API_KEY_LOC = "header";

  private static final Logger log = LoggerFactory.getLogger(ApiKeyHttpAuthenticationFilter.class);

  @ConfigProperty(name = "quarkus.recyclone.security.rest.api-key.in", defaultValue = DEFAULT_API_KEY_LOC)
  String apiKeyIn;

  @ConfigProperty(name = "quarkus.recyclone.rest.security.api-key.name", defaultValue = DEFAULT_API_KEY_NAME)
  String apiKeyName;

  @PostConstruct
  public void init() {
    setAuthcScheme(this.apiKeyName);
    setAuthzScheme("");
  }

  protected String[] getPrincipalsAndCredentials(String authorizationHeader, ContainerRequestContext requestContext) {
    if (authorizationHeader == null) {
      return null;
    }
    return getPrincipalsAndCredentials(this.apiKeyName, authorizationHeader);
  }

  protected String getAuthzHeader(ContainerRequestContext requestContext) {
    if (this.apiKeyName != null && !this.apiKeyName.isBlank()) {
      String apiKey = requestContext.getUriInfo().getQueryParameters().getFirst(this.apiKeyName);
      if (apiKey != null && !apiKey.isBlank()) {
        return apiKey;
      }
    }
    return requestContext.getHeaderString(this.apiKeyName);
  }

  protected boolean isLoginAttempt(ContainerRequestContext requestContext) {
    if (this.apiKeyName != null && !this.apiKeyName.isBlank()) {
      String apiKey = requestContext.getUriInfo().getQueryParameters().getFirst(this.apiKeyName);
      if (apiKey != null && !apiKey.isBlank()) {
        return true;
      }
    }
    String authzHeader = getAuthzHeader(requestContext);
    return authzHeader != null && isLoginAttempt(authzHeader);
  }
}
