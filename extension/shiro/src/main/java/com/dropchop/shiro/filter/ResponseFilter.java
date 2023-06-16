package com.dropchop.shiro.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 06. 23.
 */
public interface ResponseFilter extends ShiroFilter {
  boolean onFilterResponse(ContainerRequestContext requestContext, ContainerResponseContext responseContext);
}
