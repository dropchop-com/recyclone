package com.dropchop.shiro.token;

import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig.Rest.Security.JwtConfig;
import com.dropchop.shiro.filter.JwtAuthenticationFilter;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class JwtHelper {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  public static String encode(JwtConfig config, User user, Map<String, Object> claims) {
    if (config.secret == null || config.secret.isEmpty()) {
      log.error("No security secret available");
      return null;
    }
    byte[] keyBytes = Decoders.BASE64.decode(config.secret);
    SecretKey key = Keys.hmacShaKeyFor(keyBytes);
    JwtBuilder builder = Jwts.builder()
        .id(UUID.randomUUID().toString())
        .issuer(config.issuer)
        .subject(user.getUuid().toString())
        .header()
        .add("typ", "jwt")
        .and()
        .expiration(Date.from(ZonedDateTime.now().plusSeconds(config.timeoutSeconds).toInstant()))
        .signWith(key, Jwts.SIG.HS512);
    if (claims != null && !claims.isEmpty()) {
      builder.claims(claims);
    }
    return builder.compact();
  }

  public static String encode(JwtConfig config, User user) {
    return encode(config, user, null);
  }

  public static String decodeSubject(JwtConfig config, String token) {
    byte[] keyBytes = Decoders.BASE64.decode(config.secret);
    SecretKey key = Keys.hmacShaKeyFor(keyBytes);
    Jws<Claims> jws;
    try {
      jws = Jwts.parser()
          .verifyWith(key)
          .requireIssuer(config.issuer)
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
