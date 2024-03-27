package com.dropchop.shiro.jaxrs;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 12. 21.
 */
public class ShiroSecurityContext implements SecurityContext {

  final private ContainerRequestContext containerRequestContext;
  final private SecurityContext originalSecurityContext;

  public ShiroSecurityContext(ContainerRequestContext containerRequestContext) {
    this.containerRequestContext = containerRequestContext;
    this.originalSecurityContext = containerRequestContext.getSecurityContext();
  }

  @Override
  public Principal getUserPrincipal() {

    Principal result;

    Subject subject = getSubject();
    PrincipalCollection shiroPrincipals = subject.getPrincipals();
    if (shiroPrincipals != null) {
      result = shiroPrincipals.oneByType(Principal.class);

      if (result == null) {
        result = new ObjectPrincipal(shiroPrincipals.getPrimaryPrincipal());
      }
    }
    else {
      result = originalSecurityContext.getUserPrincipal();
    }

    return result;
  }

  @Override
  public boolean isUserInRole(String role) {
    return getSubject().hasRole(role);
  }

  @Override
  public boolean isSecure() {
    return containerRequestContext.getSecurityContext().isSecure();
  }

  @Override
  public String getAuthenticationScheme() {
    return containerRequestContext.getSecurityContext().getAuthenticationScheme();
  }

  private Subject getSubject() {
    return SecurityUtils.getSubject();
  }


  /**
   * Java Principal wrapper around any Shiro Principal object.s
   */
  @SuppressWarnings("ClassCanBeRecord")
  private static class ObjectPrincipal implements Principal {
    private final Object object;

    public ObjectPrincipal(Object object) {
      this.object = object;
    }

    public Object getObject() {
      return object;
    }

    public String getName() {
      return getObject().toString();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      ObjectPrincipal that = (ObjectPrincipal) o;

      return object.equals(that.object);

    }

    public int hashCode() {
      return object.hashCode();
    }

    public String toString() {
      return object.toString();
    }
  }
}
