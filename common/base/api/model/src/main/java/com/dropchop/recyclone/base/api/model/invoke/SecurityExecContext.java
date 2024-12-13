package com.dropchop.recyclone.base.api.model.invoke;

import com.dropchop.recyclone.base.api.model.security.annotations.Logical;

import java.util.List;

import static com.dropchop.recyclone.base.api.model.security.Constants.Permission.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 1. 06. 22.
 */
@SuppressWarnings("unused")
public interface SecurityExecContext {

  default String getFirstRequiredPermission() {
    List<String> permissions = getRequiredPermissions();
    if (permissions == null || permissions.isEmpty()) {
      return null;
    }
    return permissions.getFirst();
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
  void setRequiredPermissions(List<String> requiredPermissions);
  Logical getRequiredPermissionsOp();
  void setRequiredPermissionsOp(Logical op);

  List<String> getRequiredRoles();
  void setRequiredRoles(List<String> requiredRoles);
  Logical getRequiredRolesOp();
  void setRequiredRolesOp(Logical op);

  Boolean getRequiredGuest();
  void setRequiredGuest(Boolean requiredGuest);

  Boolean getRequiredAuthenticated();
  void setRequiredAuthenticated(Boolean requiredAuthenticated);

  default String getSecurityDomainAction() {
    return compose(getSecurityDomain(), getSecurityAction());
  }

  default String getSecurityDomainAction(String identifiers) {
    return compose(getSecurityDomain(), getSecurityAction(), identifiers);
  }
}
