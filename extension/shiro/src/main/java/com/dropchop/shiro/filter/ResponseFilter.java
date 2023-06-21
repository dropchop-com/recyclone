package com.dropchop.shiro.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 06. 23.
 */
public interface ResponseFilter extends ShiroFilter {
  boolean onFilterResponse(ContainerRequestContext requestContext, ContainerResponseContext responseContext);
}
