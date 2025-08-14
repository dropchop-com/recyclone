package com.dropchop.shiro.filter;

import com.dropchop.recyclone.base.api.config.ApiKeyConfig;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 01. 22.
 */
@SuppressWarnings("unused")
public abstract class CustomKeyHttpAuthenticationFilter extends HeaderHttpAuthenticationFilter {

  public static final String DEFAULT_API_KEY_LOC = "header";

  private static final Logger log = LoggerFactory.getLogger(CustomKeyHttpAuthenticationFilter.class);

  private final String headerKey;
  private final String queryParamKey;

  protected CustomKeyHttpAuthenticationFilter() {
    this.headerKey = null;
    this.queryParamKey = null;
  }

  public CustomKeyHttpAuthenticationFilter(ApiKeyConfig apiKeyConfig) {
    super(apiKeyConfig.isPermissive());
    this.setApplicationName(apiKeyConfig.getApplicationName());
    String headerKey = apiKeyConfig.getHeaderName();
    String queryParamKey =
    this.headerKey = apiKeyConfig.getHeaderName();
    this.queryParamKey = apiKeyConfig.getQueryName();
    setAuthcScheme(this.headerKey);
    setAuthzScheme("");
  }

  @Override
  protected String[] getPrincipalsAndCredentials(String authorizationHeader, ContainerRequestContext requestContext) {
    if (authorizationHeader == null) {
      return null;
    }
    return getPrincipalsAndCredentials(this.headerKey, authorizationHeader);
  }

  @Override
  protected String getAuthzHeader(ContainerRequestContext requestContext) {
    if (this.queryParamKey != null && !this.queryParamKey.isBlank()) {
      String apiKey = requestContext.getUriInfo().getQueryParameters().getFirst(this.queryParamKey);
      if (apiKey != null && !apiKey.isBlank()) {
        return apiKey;
      }
    }
    return requestContext.getHeaderString(this.headerKey);
  }

  @Override
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
