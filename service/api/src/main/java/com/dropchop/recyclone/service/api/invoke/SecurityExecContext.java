package com.dropchop.recyclone.service.api.invoke;

import com.dropchop.recyclone.model.api.security.annotations.Logical;

import java.util.List;

import static com.dropchop.recyclone.model.api.security.Constants.Permission.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 06. 22.
 */
public interface SecurityExecContext {

  default String getFirstRequiredPermission() {
    List<String> permissions = getRequiredPermissions();
    if (permissions == null || permissions.isEmpty()) {
      return null;
    }
    return permissions.get(0);
  }

  /**
   * Default security domain used in current execution
   */
  default String getSecurityDomain() {
    String permission = getFirstRequiredPermission();
    return permission == null ? null : decomposeDomain(permission);
  }

  /**
   * Default security action used in current execution
   */
  default String getSecurityAction() {
    String permission = getFirstRequiredPermission();
    return permission == null ? null : decomposeAction(permission);
  }

  List<String> getRequiredPermissions();
  void setRequiredPermissions(List<String> permissions);
  Logical getRequiredPermissionsOp();
  void setRequiredPermissionsOp(Logical op);

  default String getSecurityDomainAction() {
    return compose(getSecurityDomain(), getSecurityAction());
  }

  default String getSecurityDomainAction(String identifiers) {
    return compose(getSecurityDomain(), getSecurityAction(), identifiers);
  }
}
