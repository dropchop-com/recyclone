package com.dropchop.shiro.filter;

import com.dropchop.recyclone.base.api.model.utils.Uuid;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig.Rest.Security.ApiKeyConfig;
import com.dropchop.shiro.token.UuidApiKeyToken;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@SuppressWarnings("unused")
public class UuidAuthenticationFilter extends ApiKeyHttpAuthenticationFilter {

  private static final Logger log = LoggerFactory.getLogger(UuidAuthenticationFilter.class);

  public UuidAuthenticationFilter(ApiKeyConfig apiKeyConfig) {
    super(apiKeyConfig);
  }

  @Override
  protected AuthenticationToken createToken(ContainerRequestContext requestContext) {
    String receivedToken = getAuthzHeader(requestContext);
    if (receivedToken == null || receivedToken.isEmpty()) {
      return createToken(UUID.randomUUID().toString(), UUID.randomUUID().toString(), requestContext);
    }
    if (!Uuid.isUuid(receivedToken)) {
      log.warn("Invalid UUID [{}]", receivedToken);
      return createToken(UUID.randomUUID().toString(), UUID.randomUUID().toString(), requestContext);
    }
    return new UuidApiKeyToken(receivedToken);
  }

  @Override
  protected boolean isLoginAttempt(ContainerRequestContext requestContext) {
    String receivedToken = getAuthzHeader(requestContext);
    return receivedToken != null && !receivedToken.isEmpty();
  }
}
