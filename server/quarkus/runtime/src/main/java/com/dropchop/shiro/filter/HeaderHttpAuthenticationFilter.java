package com.dropchop.shiro.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 01. 22.
 */
@SuppressWarnings("unused")
public abstract class HeaderHttpAuthenticationFilter extends HttpAuthenticationFilter {

  private static final Logger log = LoggerFactory.getLogger(HeaderHttpAuthenticationFilter.class);
  public static final String BEARER = "Bearer";

  public HeaderHttpAuthenticationFilter() {
  }

  public HeaderHttpAuthenticationFilter(String loginUrl) {
    super(loginUrl);
  }

  public HeaderHttpAuthenticationFilter(boolean permissive) {
    super(permissive);
  }

  public HeaderHttpAuthenticationFilter(boolean permissive, String loginUrl) {
    super(permissive, loginUrl);
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
  @Override
  protected AuthenticationToken createToken(ContainerRequestContext requestContext) {
    String authorizationHeader = getAuthzHeader(requestContext);
    if (authorizationHeader == null || authorizationHeader.isEmpty()) {
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
