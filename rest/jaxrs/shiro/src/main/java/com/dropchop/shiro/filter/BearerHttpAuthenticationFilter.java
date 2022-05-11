package com.dropchop.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * Modeled and copied from Shiro Web.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
@Slf4j
public class BearerHttpAuthenticationFilter extends HttpAuthenticationFilter {

  private static final String BEARER = "Bearer";

  public BearerHttpAuthenticationFilter() {
    setAuthcScheme(BEARER);
    setAuthzScheme(BEARER);
  }

  protected AuthenticationToken createBearerToken(String token, ContainerRequestContext requestContext) {
    return new BearerToken(token, "" /*requestContext.getRemoteHost()*/);
  }

  /**
   * Creates an AuthenticationToken for use during login attempt with the provided credentials in the http header.
   * <p/>
   * This implementation:
   * <ol><li>acquires the username and password based on the request's
   * {@link #getAuthzHeader(ContainerRequestContext) authorization header} via the
   * {@link #getPrincipalsAndCredentials(String, ContainerRequestContext) getPrincipalsAndCredentials} method</li>
   * <li>The return value of that method is converted to an <code>AuthenticationToken</code> via the
   * {@link #createToken(String, String, ContainerRequestContext) createToken} method</li>
   * <li>The created <code>AuthenticationToken</code> is returned.</li>
   * </ol>
   *
   * @param requestContext  incoming ServletRequest
   * @return the AuthenticationToken used to execute the login attempt
   */
  protected AuthenticationToken createToken(ContainerRequestContext requestContext) {
    String authorizationHeader = getAuthzHeader(requestContext);
    if (authorizationHeader == null || authorizationHeader.length() == 0) {
      // Create an empty authentication token since there is no
      // Authorization header.
      return createBearerToken("", requestContext);
    }

    log.debug("Attempting to execute login with auth header");

    String[] prinCred = getPrincipalsAndCredentials(authorizationHeader, requestContext);
    if (prinCred == null || prinCred.length < 1) {
      // Create an authentication token with an empty password,
      // since one hasn't been provided in the request.
      return createBearerToken("", requestContext);
    }

    String token = prinCred[0] != null ? prinCred[0] : "";
    return createBearerToken(token, requestContext);
  }

  @Override
  protected String[] getPrincipalsAndCredentials(String scheme, String token) {
    return new String[] {token};
  }
}
