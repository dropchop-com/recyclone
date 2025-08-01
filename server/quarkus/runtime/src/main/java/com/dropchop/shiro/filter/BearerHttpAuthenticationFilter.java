package com.dropchop.shiro.filter;

import com.dropchop.recyclone.base.api.config.BearerConfig;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Modeled and copied from Shiro Web.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 01. 22.
 */
@ApplicationScoped
@SuppressWarnings("unused")
public class BearerHttpAuthenticationFilter extends HeaderHttpAuthenticationFilter {

  private static final Logger log = LoggerFactory.getLogger(BearerHttpAuthenticationFilter.class);


  /*public BearerHttpAuthenticationFilter() {
    setAuthcScheme(BEARER);
    setAuthzScheme(BEARER);
  }*/

  public BearerHttpAuthenticationFilter(BearerConfig bearerConfig) {
    super(bearerConfig.permissive);
    setAuthcScheme(BEARER);
    setAuthzScheme(BEARER);
  }
}
