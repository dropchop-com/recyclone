package com.dropchop.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleRole;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.util.PermissionUtils;
import org.apache.shiro.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
public class ShiroMapRealm extends SimpleAccountRealm {

  public ShiroMapRealm(Map<String, String> userDefinitions, Map<String, String> roleDefinitions) {
    this.processRoleDefinitions(roleDefinitions);
    this.processUserDefinitions(userDefinitions);
    super.onInit();
  }

  protected void processRoleDefinitions(Map<String, String> roleDefs) {
    if (roleDefs == null || roleDefs.isEmpty()) {
      return;
    }
    for (String rolename : roleDefs.keySet()) {
      String value = roleDefs.get(rolename);

      SimpleRole role = getRole(rolename);
      if (role == null) {
        role = new SimpleRole(rolename);
        add(role);
      }

      Set<Permission> permissions = PermissionUtils.resolveDelimitedPermissions(value, getPermissionResolver());
      role.setPermissions(permissions);
    }
  }

  protected void processUserDefinitions(Map<String, String> userDefs) {
    if (userDefs == null || userDefs.isEmpty()) {
      return;
    }
    for (String username : userDefs.keySet()) {

      String value = userDefs.get(username);

      String[] passwordAndRolesArray = StringUtils.split(value);

      String password = passwordAndRolesArray[0];

      SimpleAccount account = getUser(username);
      if (account == null) {
        account = new SimpleAccount(username, password, getName());
        add(account);
      }
      account.setCredentials(password);

      if (passwordAndRolesArray.length > 1) {
        for (int i = 1; i < passwordAndRolesArray.length; i++) {
          String rolename = passwordAndRolesArray[i];
          account.addRole(rolename);

          SimpleRole role = getRole(rolename);
          if (role != null) {
            account.addObjectPermissions(role.getPermissions());
          }
        }
      } else {
        account.setRoles(null);
      }
    }
  }

  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
    throws AuthenticationException {

    String username;
    if (token instanceof BearerToken) {
      username = ((BearerToken)token).getToken();
    } else if (token instanceof UsernamePasswordToken) {
      username = ((UsernamePasswordToken) token).getUsername();
    } else {
      username = null;
    }

    SimpleAccount account = getUser(username);

    if (account != null) {

      if (account.isLocked()) {
        throw new LockedAccountException("Account [" + account + "] is locked.");
      }
      if (account.isCredentialsExpired()) {
        String msg = "The credentials for account [" + account + "] are expired";
        throw new ExpiredCredentialsException(msg);
      }

    }

    return account;
  }

  @Override
  public boolean supports(AuthenticationToken token) {
    if (token instanceof BearerToken) {
      return true;
    }
    return super.supports(token);
  }
}
