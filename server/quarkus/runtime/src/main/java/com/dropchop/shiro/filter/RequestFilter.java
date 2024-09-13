package com.dropchop.shiro.filter;

import jakarta.ws.rs.container.ContainerRequestContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 16. 06. 23.
 */
public interface RequestFilter extends ShiroFilter {

  boolean onFilterRequest(ContainerRequestContext requestContext);
}
