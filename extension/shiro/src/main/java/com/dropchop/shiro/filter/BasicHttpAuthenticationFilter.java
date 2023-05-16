package com.dropchop.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.codec.Base64;

import jakarta.ws.rs.container.ContainerRequestContext;

/**
 * Modeled and copied from Shiro Web.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
@Slf4j
public class BasicHttpAuthenticationFilter extends HttpAuthenticationFilter {

  public BasicHttpAuthenticationFilter() {
    setAuthcScheme("Basic");
    setAuthzScheme("Basic");
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
    String decoded = Base64.decodeToString(encoded);
    return decoded.split(":", 2);
  }
}
