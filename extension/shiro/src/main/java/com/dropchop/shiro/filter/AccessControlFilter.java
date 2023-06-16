package com.dropchop.shiro.filter;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * Modeled and copied from Shiro Web.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
public interface AccessControlFilter extends RequestFilter {

  /**
   * Returns <code>true</code> if the request is allowed to proceed through the filter normally, or <code>false</code>
   * if the request should be handled by the
   * {@link #onAccessDenied(ContainerRequestContext) onAccessDenied(requestContext)}
   * method instead.
   *
   * @param requestContext     the incoming <code>ContainerRequestContext</code>
   * @return <code>true</code> if the request should proceed through the filter normally, <code>false</code> if the
   *         request should be processed by this filter's
   *         {@link #onAccessDenied(ContainerRequestContext)} method instead.
   */
  boolean isAccessAllowed(ContainerRequestContext requestContext);

  /**
   * Processes requests where the subject was denied access as determined by the
   * {@link #isAccessAllowed(ContainerRequestContext) isAccessAllowed}
   * method.
   *
   * @param requestContext  the incoming <code>ContainerRequestContext</code>
   * @return <code>true</code> if the request should continue to be processed; false if the subclass will
   *         handle/render the response directly.
   */
  boolean onAccessDenied(ContainerRequestContext requestContext);


  @Override
  default boolean onFilterRequest(ContainerRequestContext requestContext) {
    return this.isAccessAllowed(requestContext) || this.onAccessDenied(requestContext);
  }
}
