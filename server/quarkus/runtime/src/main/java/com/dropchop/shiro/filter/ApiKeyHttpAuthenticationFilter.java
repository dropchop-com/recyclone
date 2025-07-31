package com.dropchop.shiro.filter;

import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig.Rest.Security.ApiKeyConfig;
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

  public ApiKeyHttpAuthenticationFilter(ApiKeyConfig apiKeyConfig) {
    String headerKey = apiKeyConfig.headerName;
    String queryParamKey =
    this.headerKey = apiKeyConfig.headerName == null || apiKeyConfig.headerName.isBlank()
        ? DEFAULT_API_KEY_NAME : headerKey;
    this.queryParamKey = apiKeyConfig.queryName == null || apiKeyConfig.queryName.isBlank()
        ? this.headerKey.toLowerCase() : apiKeyConfig.queryName;
    setAuthcScheme(this.headerKey);
    setAuthzScheme("");
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
