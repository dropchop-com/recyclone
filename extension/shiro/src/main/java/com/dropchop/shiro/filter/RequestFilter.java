package com.dropchop.shiro.filter;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 06. 23.
 */
public interface RequestFilter extends ShiroFilter {

  boolean onFilterRequest(ContainerRequestContext requestContext);
}
