package com.dropchop.recyclone.base.api.service.security;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.api.config.JwtConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 14. 08. 2025
 */
@Slf4j
@ApplicationScoped
public class JwtDefaultService implements JwtService {

  @Inject
  @RecycloneType(RECYCLONE_DEFAULT)
  @SuppressWarnings({"CdiInjectionPointsInspection", "RedundantSuppression"})
  ObjectMapper mapper;

  public String encode(JwtConfig config, String subject, Map<String, Object> claims) {
    if (config.getSecret() == null || config.getSecret().isEmpty()) {
      log.error("No security secret available");
      return null;
    }
    byte[] keyBytes = Decoders.BASE64.decode(config.getSecret());
    SecretKey key = Keys.hmacShaKeyFor(keyBytes);
    JwtBuilder builder = Jwts.builder()
        .id(UUID.randomUUID().toString())
        .issuer(config.getIssuer())
        .subject(subject)
        .header()
        .add("typ", "jwt")
        .and()
        .expiration(Date.from(ZonedDateTime.now().plusSeconds(config.getTimeoutSeconds()).toInstant()))
        .signWith(key, Jwts.SIG.HS512);
    if (claims != null && !claims.isEmpty()) {
      builder.claims(claims);
    }
    if (mapper != null) {
      builder.json(new JacksonSerializer<>(mapper));
    }
    return builder.compact();
  }

  public String decodeSubject(JwtConfig config, String token) {
    byte[] keyBytes = Decoders.BASE64.decode(config.getSecret());
    SecretKey key = Keys.hmacShaKeyFor(keyBytes);
    Jws<Claims> jws;
    try {
      JwtParserBuilder builder = Jwts.parser();
      if (mapper != null) {
        builder.json(new JacksonDeserializer<>(mapper));
      }
      jws = builder
          .verifyWith(key)
          .requireIssuer(config.getIssuer())
          .build()
          .parseSignedClaims(token);
    } catch (JwtException e) {
      log.warn("Invalid JWT token!", e);
      return null;
    }
    Claims claims = jws.getPayload();
    if (claims == null) {
      log.warn("Invalid JWT token! Missing claims!");
      return null;
    }
    return claims.getSubject();
  }
}
