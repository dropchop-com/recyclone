package com.dropchop.shiro.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * Modeled and copied from Shiro Web.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 01. 22.
 */
@SuppressWarnings("unused")
public abstract class HttpAuthenticationFilter extends AuthenticatingFilter {

  /**
   * HTTP Authorization header, equal to <code>Authorization</code>
   */
  protected static final String AUTHORIZATION_HEADER = "Authorization";

  /**
   * HTTP Authentication header, equal to <code>WWW-Authenticate</code>
   */
  protected static final String AUTHENTICATE_HEADER = "WWW-Authenticate";

  private static final Logger log = LoggerFactory.getLogger(HttpAuthenticationFilter.class);

  /**
   * The name which is displayed during the challenge process of authentication, defaults to <code>application</code>
   * and can be overridden by the {@link #setApplicationName(String) setApplicationName} method.
   */
  private String applicationName = "application";

  /**
   * The authcScheme to look for in the <code>Authorization</code> header, defaults to <code>BASIC</code>
   */
  private String authcScheme;

  /**
   * The authzScheme value to look for in the <code>Authorization</code> header, defaults to <code>BASIC</code>
   */
  private String authzScheme;


  public HttpAuthenticationFilter() {
    super();
  }

  public HttpAuthenticationFilter(String loginUrl) {
    super(loginUrl);
  }

  public HttpAuthenticationFilter(boolean permissive) {
    super(permissive);
  }

  public HttpAuthenticationFilter(boolean permissive, String loginUrl) {
    super(permissive, loginUrl);
  }

  /**
   * Returns the name to use in the ServletResponse's <b><code>WWW-Authenticate</code></b> header.
   * <p/>
   * Per RFC 2617, this name name is displayed to the end user when they are asked to authenticate.  Unless overridden
   * by the {@link #setApplicationName(String) setApplicationName(String)} method, the default value is 'application'.
   * <p/>
   * Please see {@link #setApplicationName(String) setApplicationName(String)} for an example of how this functions.
   *
   * @return the name to use in the ServletResponse's 'WWW-Authenticate' header.
   */
  public String getApplicationName() {
    return applicationName;
  }

  /**
   * Sets the name to use in the ServletResponse's <b><code>WWW-Authenticate</code></b> header.
   * <p/>
   * Per RFC 2617, this name name is displayed to the end user when they are asked to authenticate.  Unless overridden
   * by this method, the default value is &quot;application&quot;
   * <p/>
   * For example, setting this property to the value <b><code>Awesome Webapp</code></b> will result in the
   * following header:
   * <p/>
   * <code>WWW-Authenticate: Basic realm=&quot;<b>Awesome Webapp</b>&quot;</code>
   * <p/>
   * Side note: As you can see from the header text, the HTTP Basic specification calls
   * this the authentication 'realm', but we call this the 'applicationName' instead to avoid confusion with
   * Shiro's Realm constructs.
   *
   * @param applicationName the name to use in the ServletResponse's 'WWW-Authenticate' header.
   */
  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  /**
   * Returns the HTTP <b><code>Authorization</code></b> header value that this filter will respond to as indicating
   * a login request.
   * <p/>
   * Unless overridden by the {@link #setAuthzScheme(String) setAuthzScheme(String)} method, the
   * default value is <code>BASIC</code>.
   *
   * @return the Http 'Authorization' header value that this filter will respond to as indicating a login request
   */
  public String getAuthzScheme() {
    return authzScheme;
  }

  /**
   * Sets the HTTP <b><code>Authorization</code></b> header value that this filter will respond to as indicating a
   * login request.
   * <p/>
   * Unless overridden by this method, the default value is <code>BASIC</code>
   *
   * @param authzScheme the HTTP <code>Authorization</code> header value that this filter will respond to as
   *                    indicating a login request.
   */
  public void setAuthzScheme(String authzScheme) {
    this.authzScheme = authzScheme;
  }

  /**
   * Returns the HTTP <b><code>WWW-Authenticate</code></b> header scheme that this filter will use when sending
   * the HTTP Basic challenge response.  The default value is <code>BASIC</code>.
   *
   * @return the HTTP <code>WWW-Authenticate</code> header scheme that this filter will use when sending the HTTP
   *         Basic challenge response.
   * @see #sendChallenge
   */
  public String getAuthcScheme() {
    return authcScheme;
  }

  /**
   * Sets the HTTP <b><code>WWW-Authenticate</code></b> header scheme that this filter will use when sending the
   * HTTP Basic challenge response.  The default value is <code>BASIC</code>.
   *
   * @param authcScheme the HTTP <code>WWW-Authenticate</code> header scheme that this filter will use when
   *                    sending the Http Basic challenge response.
   * @see #sendChallenge
   */
  public void setAuthcScheme(String authcScheme) {
    this.authcScheme = authcScheme;
  }


  /**
   * Returns the {@link #AUTHORIZATION_HEADER AUTHORIZATION_HEADER} from the specified ContainerRequestContext.
   *
   * @param requestContext the incoming <code>ContainerRequestContext</code>
   * @return the <code>Authorization</code> header's value.
   */
  protected String getAuthzHeader(ContainerRequestContext requestContext) {
    return requestContext.getHeaderString(AUTHORIZATION_HEADER);
  }

  /**
   * Default implementation that returns <code>true</code> if the specified <code>authzHeader</code>
   * starts with the same (case-insensitive) characters specified by the
   * {@link #getAuthzScheme() authzScheme}, <code>false</code> otherwise.
   * <p/>
   * That is:
   * <p/>
   * <code>String authzScheme = getAuthzScheme().toLowerCase();<br/>
   * return authzHeader.toLowerCase().startsWith(authzScheme);</code>
   *
   * @param authzHeader the 'Authorization' header value (guaranteed to be non-null if the
   *                    {@link #isLoginAttempt(ContainerRequestContext)} method is not overridden).
   * @return <code>true</code> if the authzHeader value matches that configured as defined by
   *         the {@link #getAuthzScheme() authzScheme}.
   */
  protected boolean isLoginAttempt(String authzHeader) {
    //SHIRO-415: use English Locale:
    String authzScheme = getAuthzScheme().toLowerCase(Locale.ENGLISH);
    return authzHeader.toLowerCase(Locale.ENGLISH).startsWith(authzScheme);
  }

  /**
   * Determines whether the incoming request is an attempt to log in.
   * <p/>
   * The default implementation obtains the value of the request's
   * {@link #AUTHORIZATION_HEADER AUTHORIZATION_HEADER}, and if it is not <code>null</code>, delegates
   * to {@link #isLoginAttempt(String) isLoginAttempt(authzHeaderValue)}. If the header is <code>null</code>,
   * <code>false</code> is returned.
   *
   * @param requestContext  incoming ContainerRequestContext
   * @return true if the incoming request is an attempt to log in based, false otherwise
   */
  protected boolean isLoginAttempt(ContainerRequestContext requestContext) {
    String authzHeader = getAuthzHeader(requestContext);
    return authzHeader != null && isLoginAttempt(authzHeader);
  }

  /**
   * Builds the challenge for authorization by setting a HTTP <code>401</code> (Unauthorized) status as well as the
   * response's {@link #AUTHENTICATE_HEADER AUTHENTICATE_HEADER}.
   * <p/>
   * The header value constructed is equal to:
   * <p/>
   * <code>{@link #getAuthcScheme() getAuthcScheme()} + " realm=\"" + {@link #getApplicationName() getApplicationName()} + "\"";</code>
   *
   * @param requestContext  incoming ContainerRequestContext, ignored by this implementation
   * @return false - this sends the challenge to be sent back
   */
  protected boolean sendChallenge(ContainerRequestContext requestContext) {
    log.info("Authentication required: sending 401 Authentication challenge response.");
    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).header(
      AUTHENTICATE_HEADER, getAuthcScheme() + " realm=\"" + getApplicationName() + "\""
    ).build());
    return false;
  }

  /**
   * Returns a String[] containing credential parts parsed fom the "Authorization" header.
   *
   * @param scheme  the {@link #getAuthcScheme() authcScheme} found in the request
   *                {@link #getAuthzHeader(ContainerRequestContext) authzHeader}.  It is ignored by this implementation,
   *                but available to overriding implementations should they find it useful.
   * @param value the raw string value from the "Authorization" header.
   */
  abstract String[] getPrincipalsAndCredentials(String scheme, String value);

  /**
   * Returns the username obtained from the
   * {@link #getAuthzHeader(ContainerRequestContext) authorizationHeader}.
   * <p/>
   * Once the {@code authzHeader} is split per the RFC (based on the space character ' '), the resulting split tokens
   * are translated into the username/password pair by the
   * {@link #getPrincipalsAndCredentials(String, String) getPrincipalsAndCredentials(scheme,encoded)} method.
   *
   * @param authorizationHeader the authorization header obtained from the request.
   * @param requestContext      the incoming ContainerRequestContext
   * @return the username (index 0)/password pair (index 1) submitted by the user for the given header value and request.
   * @see #getAuthzHeader(ContainerRequestContext)
   */
  protected String[] getPrincipalsAndCredentials(String authorizationHeader, ContainerRequestContext requestContext) {
    if (authorizationHeader == null) {
      return null;
    }
    String[] authTokens = authorizationHeader.split(" ");
    if (authTokens.length < 2) {
      return null;
    }
    return getPrincipalsAndCredentials(authTokens[0], authTokens[1]);
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
   * @param requestContext  incoming ContainerRequestContext
   * @return the AuthenticationToken used to execute the login attempt
   */
  protected AuthenticationToken createToken(ContainerRequestContext requestContext) {
    String authorizationHeader = getAuthzHeader(requestContext);
    if (authorizationHeader == null || authorizationHeader.isEmpty()) {
      // Create an empty authentication token since there is no
      // Authorization header.
      return createToken("", "", requestContext);
    }

    log.trace("Creating login token with auth header");

    String[] prinCred = getPrincipalsAndCredentials(authorizationHeader, requestContext);
    if (prinCred == null || prinCred.length < 2) {
      // Create an authentication token with an empty password,
      // since one hasn't been provided in the request.
      String username = prinCred == null || prinCred.length == 0 ? "" : prinCred[0];
      return createToken(username, "", requestContext);
    }

    String username = prinCred[0];
    String password = prinCred[1];

    return createToken(username, password, requestContext);
  }


  /**
   * Processes unauthenticated requests. It handles the two-stage request/challenge authentication protocol.
   *
   * @param requestContext  incoming ContainerRequestContext
   * @return true if the request should be processed; false if the request should not continue to be processed
   */
  @Override
  public boolean onAccessDenied(ContainerRequestContext requestContext) {
    boolean loggedIn = false; //false by default or we wouldn't be in this method
    if (isLoginAttempt(requestContext)) {
      loggedIn = executeLogin(requestContext);
    }
    if (!loggedIn) {
      sendChallenge(requestContext);
    }
    return loggedIn;
  }

  /**
   * Delegates to {@link #isLoginAttempt(ContainerRequestContext) isLoginAttempt}.
   */
  @Override
  protected final boolean isLoginRequest(ContainerRequestContext requestContext) {
    return this.isLoginAttempt(requestContext);
  }
}
