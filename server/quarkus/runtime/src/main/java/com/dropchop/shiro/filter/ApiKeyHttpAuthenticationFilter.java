package com.dropchop.shiro.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 01. 22.
 */
@SuppressWarnings("unused")
public class ApiKeyHttpAuthenticationFilter extends BearerHttpAuthenticationFilter {

  public static final String DEFAULT_API_KEY_NAME = "X-API-Key";
  public static final String DEFAULT_API_KEY_LOC = "header";

  private static final Logger log = LoggerFactory.getLogger(ApiKeyHttpAuthenticationFilter.class);

  private final String headerKey;
  private final String queryParamKey;

  public ApiKeyHttpAuthenticationFilter(String headerKey, String queryParamKey) {
    this.headerKey = headerKey == null || headerKey.isBlank() ? DEFAULT_API_KEY_NAME : headerKey;
    this.queryParamKey = queryParamKey;
    setAuthcScheme(this.headerKey);
    setAuthzScheme("");
  }

  public ApiKeyHttpAuthenticationFilter(String headerKey) {
    this(headerKey, null);
  }

  public ApiKeyHttpAuthenticationFilter() {
    this(null, null);
  }

  protected String[] getPrincipalsAndCredentials(String authorizationHeader, ContainerRequestContext requestContext) {
    if (authorizationHeader == null) {
      return null;
    }
    return getPrincipalsAndCredentials(this.headerKey, authorizationHeader);
  }

  protected String getAuthzHeader(ContainerRequestContext requestContext) {
    if (this.queryParamKey != null && !this.queryParamKey.isBlank()) {
      String apiKey = requestContext.getUriInfo().getQueryParameters().getFirst(this.queryParamKey);
      if (apiKey != null && !apiKey.isBlank()) {
        return apiKey;
      }
    }
    return requestContext.getHeaderString(this.headerKey);
  }

  protected boolean isLoginAttempt(ContainerRequestContext requestContext) {
    if (this.queryParamKey != null && !this.queryParamKey.isBlank()) {
      String apiKey = requestContext.getUriInfo().getQueryParameters().getFirst(this.queryParamKey);
      if (apiKey != null && !apiKey.isBlank()) {
        return true;
      }
    }
    String authzHeader = getAuthzHeader(requestContext);
    return authzHeader != null && isLoginAttempt(authzHeader);
  }
}
