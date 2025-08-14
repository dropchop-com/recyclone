package com.dropchop.recyclone.base.api.model.security;

import lombok.Getter;

import java.net.URI;
import java.util.Objects;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 04. 08. 2025
 */
@Getter
public class ClientKeyConfig {
  private final String clientId;
  private final String queryName;
  private final String headerName;
  private final int expiresAfterSeconds;
  private final URI uri;
  private final String secret;
  private final String salt;

  public ClientKeyConfig(String clientId, String queryName, String headerName, int expiresAfterSeconds,
                         URI uri, String secret, String salt) {
    this.clientId = clientId;
    this.queryName = queryName;
    this.headerName = headerName;
    this.expiresAfterSeconds = expiresAfterSeconds;
    this.uri = uri;
    this.secret = secret;
    this.salt = salt;
  }

  public ClientKeyConfig(String clientId, URI uri, String secret, String salt) {
    this(clientId, null, null, 3600, uri, secret, salt);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ClientKeyConfig that)) return false;
    return Objects.equals(getClientId(), that.getClientId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getClientId());
  }
}
