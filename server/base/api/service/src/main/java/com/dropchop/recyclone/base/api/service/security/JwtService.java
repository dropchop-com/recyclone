package com.dropchop.recyclone.base.api.service.security;

import com.dropchop.recyclone.base.api.config.JwtConfig;
import io.jsonwebtoken.Claims;

import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 14. 08. 2025
 */
public interface JwtService {

  String encode(JwtConfig config, long timeout, String subject, Map<String, Object> claims);

  default String encode(JwtConfig config, long timeout, String subject) {
    return encode(config, timeout, subject, null);
  }

  Claims decode(JwtConfig config, String token);
}
