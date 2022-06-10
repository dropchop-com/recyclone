package com.dropchop.recyclone.service.api.invoke;

import org.apache.shiro.subject.Subject;

import static com.dropchop.recyclone.model.api.security.Constants.Permission.compose;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 06. 22.
 */
public interface SecurityExecContext {

  Subject getSubject();
  String getSecurityAction();
  String getSecurityDomain();

  default String getSecurityDomainAction() {
    return compose(getSecurityDomain(), getSecurityAction());
  }

  default String getSecurityDomainAction(String identifiers) {
    return compose(getSecurityDomain(), getSecurityAction(), identifiers);
  }
}
