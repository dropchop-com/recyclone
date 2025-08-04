package com.dropchop.recyclone.base.dto.model.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class OauthLikeRequest extends AuthorizationRequest {

  @Override
  @JsonProperty("client_id")
  public String getClientId() {
    return super.getClientId();
  }

  @Override
  @JsonProperty("client_id")
  public void setClientId(String clientId) {
    super.setClientId(clientId);
  }

  @Override
  @JsonProperty("client_secret")
  public String getClientSecret() {
    return super.getClientSecret();
  }

  @Override
  @JsonProperty("client_secret")
  public void setClientSecret(String clientSecret) {
    super.setClientSecret(clientSecret);
  }

  @Override
  @JsonProperty("redirect_uri")
  public String getRedirectUri() {
    return super.getRedirectUri();
  }

  @Override
  @JsonProperty("redirect_uri")
  public void setRedirectUri(String redirectUri) {
    super.setRedirectUri(redirectUri);
  }

  @Override
  @JsonProperty("grant_type")
  public String getGrantType() {
    return super.getGrantType();
  }

  @Override
  @JsonProperty("grant_type")
  public void setGrantType(String grantType) {
    super.setGrantType(grantType);
  }
}
