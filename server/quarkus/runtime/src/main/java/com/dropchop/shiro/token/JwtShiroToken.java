package com.dropchop.shiro.token;

import com.dropchop.recyclone.base.dto.model.security.User;
import org.apache.shiro.authc.BearerToken;

@SuppressWarnings("unused")
public class JwtShiroToken extends BearerToken {

  private final User subject;

  private final String host;
  private final String rawToken;

  /**
   *  Returns {@code true} if this token was validated on creation, {@code false} otherwise.
   *  if this token was validated upon creation.
   */
  private final boolean validated;

  public JwtShiroToken(User subject, String host, String rawToken, boolean isValidated) {
    super(rawToken);
    this.host = host;
    this.subject = subject;
    this.rawToken = rawToken;
    this.validated = isValidated;
  }


  @Override
  public String getHost() {
    return host;
  }

  @Override
  public Object getPrincipal() {
    return this.subject;
  }

  public String getCredentials() {
    return rawToken;
  }

  public User getSubject() {
    return subject;
  }

  public boolean isValidated() {
    return validated;
  }
}
