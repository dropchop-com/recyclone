package com.dropchop.recyclone.model.api.security;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 03. 22.
 */
public interface Constants {

  String PERM_DELIM = ":";
  String ALL = "*";

  interface Permission {

    static String compose(String domain, String action) {
      if (domain == null || action.isBlank()) {
        domain = Domains.ALL;
      }
      if (action == null || action.isBlank()) {
        action = Actions.ALL;
      }
      return domain + PERM_DELIM + action;
    }

    static String compose(String domain, String action, String instances) {
      if (domain == null || action.isBlank()) {
        domain = Domains.ALL;
      }
      if (action == null || action.isBlank()) {
        action = Actions.ALL;
      }
      if (instances == null || instances.isBlank()) {
        instances = Constants.ALL;
      }
      return domain + PERM_DELIM + action + PERM_DELIM + instances;
    }

    static String decomposeDomain(String permission) {
      if (permission == null) {
        return null;
      }
      int idx = permission.indexOf(PERM_DELIM);
      if (idx < 0) {
        return permission;
      } else {
        return permission.substring(0, idx);
      }
    }

    static String decomposeAction(String permission) {
      if (permission == null) {
        return null;
      }
      String[] parts = permission.split(PERM_DELIM);
      if (parts.length <= 1) {
        return null;
      } else {
        return parts[1];
      }
    }
  }

  interface Domains {
    String ALL = "*";
    interface Localization {
      String LANGUAGE = "localization.language";
      String COUNTRY  = "localization.country";
    }

    interface Security {
      String ACTION     = "security.action";
      String DOMAIN     = "security.domain";
      String ROLE       = "security.role";
      String PERMISSION = "security.permission";
      String USER       = "security.user";
    }
  }

  interface Actions {
    String ALL        = "*";
    String VIEW       = "view";
    String CREATE     = "create";
    String DELETE     = "delete";
    String UPDATE     = "update";
    String DEACTIVATE = "deactivate"; //on tuesdays it's covered by update on fridays a see it as a separate action ... a možda i nije
  }
}
