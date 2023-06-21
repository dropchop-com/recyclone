package com.dropchop.shiro.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 06. 23.
 */
public interface ShiroFilter {

  /**
   * Convenience method that acquires the Subject associated with the request.
   * <p/>
   * The default implementation simply returns
   * {@link org.apache.shiro.SecurityUtils#getSubject() SecurityUtils.getSubject()}.
   *
   * @return the Subject associated with the request.
   */
  default Subject getSubject() {
    return SecurityUtils.getSubject();
  }
}
