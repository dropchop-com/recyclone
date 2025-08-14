package com.dropchop.shiro.filter;

import com.dropchop.recyclone.base.api.service.security.AuthenticationService;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.dropchop.shiro.cdi.ShiroAuthenticationService.SHIRO_REQ_INTERNAL_SERVICE;

/**
 * Modeled and copied from Shiro Web.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 01. 22.
 */
@SuppressWarnings("unused")
public abstract class AuthenticatingFilter implements AccessControlFilter {

  public static final String DEFAULT_PATH_SEPARATOR = "/";

  private static final Logger log = LoggerFactory.getLogger(AuthenticatingFilter.class);

  private final PatternMatcher pathMatcher = new AntPathMatcher();

  private boolean permissive = true;
  private String loginUrl = "/api/security/login/**";

  public AuthenticatingFilter() {
  }

  public AuthenticatingFilter(String loginUrl) {
    this.loginUrl = loginUrl;
  }

  public AuthenticatingFilter(boolean permissive) {
    this.permissive = permissive;
  }

  public AuthenticatingFilter(boolean permissive, String loginUrl) {
    this.permissive = permissive;
    this.loginUrl = loginUrl;
  }

  /**
   * Returns <code>true</code> if this filter should be permissive.
   *
   * @return <code>true</code> if this filter should be permissive
   */
  protected boolean isPermissive() {
    return permissive;
  }

  public String getLoginUrl() {
    return loginUrl;
  }

  /**
   * Returns <code>true</code> if &quot;rememberMe&quot; should be enabled for the login attempt associated with the
   * current <code>request</code>, <code>false</code> otherwise.
   * <p/>
   * This implementation always returns <code>false</code> and is provided as a template hook to subclasses that
   * support <code>rememberMe</code> logins and wish to determine <code>rememberMe</code> in a custom mannner
   * based on the current <code>request</code>.
   *
   * @param requestContext the incoming ContainerRequestContext
   * @return <code>true</code> if &quot;rememberMe&quot; should be enabled for the login attempt associated with the
   *         current <code>request</code>, <code>false</code> otherwise.
   */
  protected boolean isRememberMe(ContainerRequestContext requestContext) {
    return false;
  }

  protected boolean executeLogin(ContainerRequestContext requestContext) {
    AuthenticationToken token = createToken(requestContext);
    if (token == null) {
      String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
          "must be created in order to execute a login attempt.";
      throw new IllegalStateException(msg);
    }
    Object oAuthService = requestContext.getProperty(SHIRO_REQ_INTERNAL_SERVICE);
    if (oAuthService instanceof AuthenticationService authenticationService) {
      try {
        Subject subject = authenticationService.login(token);
        return onLoginSuccess(token, subject, requestContext);
      } catch (AuthenticationException e) {
        return onLoginFailure(token, e, requestContext);
      }
    } else {
      throw new IllegalStateException("Missing " + AuthenticationService.class.getName() + " in request context!");
    }
  }

  protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                   ContainerRequestContext requestContext) {
    return true;
  }

  protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
                                   ContainerRequestContext requestContext) {
    return false;
  }

  protected abstract AuthenticationToken createToken(ContainerRequestContext request);

  protected AuthenticationToken createToken(String username, String password,
                                            ContainerRequestContext request) {
    boolean rememberMe = isRememberMe(request);
    return createToken(username, password, rememberMe, "host");
  }

  @SuppressWarnings("SameParameterValue")
  protected AuthenticationToken createToken(String username, String password, boolean rememberMe, String host) {
    return new UsernamePasswordToken(username, password, rememberMe, host);
  }

  protected boolean isLoginRequest(ContainerRequestContext requestContext) {
    String requestURI = requestContext.getUriInfo().getPath();
    String path = getLoginUrl();
    log.trace("Attempting to match pattern '{}' with current requestURI '{}'...", path, requestURI);
    boolean match = pathMatcher.matches(path, requestURI);

    if (!match) {
      if (requestURI != null && !DEFAULT_PATH_SEPARATOR.equals(requestURI)
        && requestURI.endsWith(DEFAULT_PATH_SEPARATOR)) {
        requestURI = requestURI.substring(0, requestURI.length() - 1);
      }
      if (path != null && !DEFAULT_PATH_SEPARATOR.equals(path)
        && path.endsWith(DEFAULT_PATH_SEPARATOR)) {
        path = path.substring(0, path.length() - 1);
      }
      log.trace("Attempting to match pattern '{}' with current requestURI '{}'...", path, requestURI);
      match = pathMatcher.matches(path, requestURI);
    }

    return match;
  }

  /**
   * Determines whether the current subject should be allowed to make the current request.
   * <p/>
   * The default implementation returns <code>true</code> if the user is authenticated.  Will also return
   * <code>true</code> if the {@link #isLoginRequest} returns false and the &quot;permissive&quot; flag is set.
   *
   * @return <code>true</code> if request should be allowed access
   */
  @Override
  public boolean isAccessAllowed(ContainerRequestContext requestContext) {
    Subject subject = getSubject();
    return (subject.isAuthenticated() && subject.getPrincipal() != null) ||
      (!isLoginRequest(requestContext) && isPermissive());
  }
}
