package com.dropchop.shiro.filter;

import jakarta.ws.rs.container.ContainerRequestContext;

import java.util.Base64;

/**
 * Modeled and copied from Shiro Web.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 01. 22.
 */
@SuppressWarnings("unused")
public class BasicHttpAuthenticationFilter extends HttpAuthenticationFilter {

  private static final String BASIC = "Basic" ;

  public BasicHttpAuthenticationFilter() {
    setAuthcScheme(BASIC);
    setAuthzScheme(BASIC);
  }

  public BasicHttpAuthenticationFilter(String loginUrl) {
    super(loginUrl);
    setAuthcScheme(BASIC);
    setAuthzScheme(BASIC);
  }

  public BasicHttpAuthenticationFilter(boolean permissive) {
    super(permissive);
    setAuthcScheme(BASIC);
    setAuthzScheme(BASIC);
  }

  public BasicHttpAuthenticationFilter(boolean permissive, String loginUrl) {
    super(permissive, loginUrl);
    setAuthcScheme(BASIC);
    setAuthzScheme(BASIC);
  }

  /**
   * Returns the username and password pair based on the specified <code>encoded</code> String obtained from
   * the request's authorization header.
   * <p/>
   * Per RFC 2617, the default implementation first Base64 decodes the string and then splits the resulting decoded
   * string into two based on the ":" character.  That is:
   * <p/>
   * <code>String decoded = Base64.decodeToString(encoded);<br/>
   * return decoded.split(":");</code>
   *
   * @param scheme  the {@link #getAuthcScheme() authcScheme} found in the request
   *                {@link #getAuthzHeader(ContainerRequestContext) authzHeader}.  It is ignored by this implementation,
   *                but available to overriding implementations should they find it useful.
   * @param encoded the Base64-encoded username:password value found after the scheme in the header
   * @return the username (index 0)/password (index 1) pair obtained from the encoded header data.
   */
  protected String[] getPrincipalsAndCredentials(String scheme, String encoded) {
    byte[] decodedBytes = Base64.getDecoder().decode(encoded);
    String decodedString = new String(decodedBytes);
    return decodedString.split(":", 2);
  }
}
