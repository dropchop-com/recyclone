package com.dropchop.recyclone.base.dto.model.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(NON_NULL)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class OauthLikeResponse extends AuthorizationResponse {

  @Override
  @JsonProperty("access_token")
  public String getAccessToken() {
    return super.getAccessToken();
  }

  @Override
  @JsonProperty("access_token")
  public void setAccessToken(String accessToken) {
    super.setAccessToken(accessToken);
  }

  @Override
  @JsonProperty("refresh_token")
  public String getRefreshToken() {
    return super.getRefreshToken();
  }

  @Override
  @JsonProperty("refresh_token")
  public void setRefreshToken(String refreshToken) {
    super.setRefreshToken(refreshToken);
  }

  @Override
  @JsonProperty("id_token")
  public String getIdToken() {
    return super.getIdToken();
  }

  @Override
  @JsonProperty("id_token")
  public void setIdToken(String idToken) {
    super.setIdToken(idToken);
  }

  @Override
  @JsonProperty("token_type")
  public String getTokenType() {
    return super.getTokenType();
  }

  @Override
  @JsonProperty("token_type")
  public void setTokenType(String tokenType) {
    super.setTokenType(tokenType);
  }

  @Override
  @JsonProperty("expires_in")
  public Integer getExpiresIn() {
    return super.getExpiresIn();
  }

  @Override
  @JsonProperty("expires_in")
  public void setExpiresIn(Integer expiresIn) {
    super.setExpiresIn(expiresIn);
  }

  @JsonInclude(NON_NULL)
  @ToString.Include
  String error;

  @JsonInclude(NON_NULL)
  @ToString.Include
  @JsonProperty("error_description")
  String errorDescription;
}
