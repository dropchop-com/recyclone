package com.dropchop.shiro.filter;

import com.dropchop.recyclone.base.api.config.ApiKeyConfig;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 01. 22.
 */
@ApplicationScoped
@SuppressWarnings("unused")
public class ApiKeyHttpAuthenticationFilter extends CustomKeyHttpAuthenticationFilter {

  public ApiKeyHttpAuthenticationFilter(ApiKeyConfig apiKeyConfig) {
    super(apiKeyConfig);
  }
}
