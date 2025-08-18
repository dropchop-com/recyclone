package com.dropchop.shiro.filter;

import com.dropchop.recyclone.base.api.config.ApiKeyConfig;
import com.dropchop.recyclone.base.api.model.utils.Uuid;
import com.dropchop.shiro.token.UuidApiKeyToken;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@ApplicationScoped
@SuppressWarnings("unused")
public class UuidAuthenticationFilter extends CustomKeyHttpAuthenticationFilter {

  private static final Logger log = LoggerFactory.getLogger(UuidAuthenticationFilter.class);

  @Inject
  Subject subject;

  @Inject
  public UuidAuthenticationFilter(ApiKeyConfig apiKeyConfig) {
    super(apiKeyConfig);
  }

  @Override
  public Subject getSubject() {
    return subject;
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
}
