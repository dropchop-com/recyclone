package com.dropchop.recyclone.base.api.model.rest;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 03. 22.
 */
public interface Constants {

  @SuppressWarnings("unused")
  interface Tags {
    String LOCALIZATION = "localization";
    String SECURITY     = "security";
    String TAGGING      = "tagging";

    String DYNAMIC_PREFIX  = "dyn-";
    String DYNAMIC_DELIM   = ":";
    String DYNAMIC_PARAMS  = DYNAMIC_PREFIX + "params";
    String DYNAMIC_CONTEXT = DYNAMIC_PREFIX + "context";

    interface DynamicContext {
      String INTERNAL = DYNAMIC_CONTEXT + DYNAMIC_DELIM + "internal";
      String PUBLIC   = DYNAMIC_CONTEXT + DYNAMIC_DELIM + "public";
    }
  }

  interface Paths {
    String SEARCH_SEGMENT = "/search";
    String QUERY_SEGMENT = "/query";
    String LOCALIZATION_SEGMENT = "/localization";
    String SECURITY_SEGMENT = "/security";
    String TAGGING_SEGMENT = "/tagging";

    String INTERNAL_SEGMENT = "/internal";
    String PUBLIC_SEGMENT = "/public";

    interface Localization {
      String LANGUAGE_SEGMENT = "/language";
      String LANGUAGE = LOCALIZATION_SEGMENT + LANGUAGE_SEGMENT;
      String COUNTRY_SEGMENT = "/country";
      String COUNTRY = LOCALIZATION_SEGMENT + COUNTRY_SEGMENT;
      String DICTIONARY_TERM_SEGMENT = "/dictionaryTerm";
      String DICTIONARY_TERM = LOCALIZATION_SEGMENT + DICTIONARY_TERM_SEGMENT;
    }

    interface Security {
      String ACTION_SEGMENT = "/action";
      String ACTION = SECURITY_SEGMENT + ACTION_SEGMENT;
      String DOMAIN_SEGMENT = "/domain";
      String DOMAIN = SECURITY_SEGMENT + DOMAIN_SEGMENT;
      String ROLE_SEGMENT = "/role";
      String ROLE = SECURITY_SEGMENT + ROLE_SEGMENT;
      String ROLE_NODE_SEGMENT = "/roleNode";
      String ROLE_NODE = SECURITY_SEGMENT + ROLE_NODE_SEGMENT;
      String ROLE_NODE_PERMISSION_SEGMENT = "/roleNodePermission";
      String ROLE_NODE_PERMISSION = SECURITY_SEGMENT + ROLE_NODE_PERMISSION_SEGMENT;
      String PERMISSION_SEGMENT = "/permission";
      String PERMISSION = SECURITY_SEGMENT + PERMISSION_SEGMENT;
      String PERMISSIONS_SEGMENT = "/permissions";
      String PERMISSIONS = SECURITY_SEGMENT + PERMISSIONS_SEGMENT;
      String PERMISSIONS_LIST_SEGMENT = "/list";
      String PERMISSIONS_UPDATE_SEGMENT = "/update";
      String USER_SEGMENT = "/user";
      String USER = SECURITY_SEGMENT + USER_SEGMENT;
      String USER_ACCOUNTS_SEGMENT = "/accounts";
      String LOGIN_SEGMENT = "/login";
      String LOGIN = SECURITY_SEGMENT + LOGIN_SEGMENT;
      String JWT_SEGMENT = "/jwt";
      String LOGIN_JWT = LOGIN + JWT_SEGMENT;
      String PLAIN_SEGMENT = "/plain";
      String LOGIN_PLAIN = LOGIN + PLAIN_SEGMENT;
    }

    interface Tagging {
      String TAG_SEGMENT = "/tag";
      String TAG = TAGGING_SEGMENT + TAG_SEGMENT;
    }

    interface Event {
      String EVENTS = "/events";
    }
  }

  interface Params {
    interface Header {
      String CFIELDS = "X-Content-Fields";
      String CLEVEL = "X-Content-Level";
      String VERSION = "X-Content-Version";
      String LANG = "Accept-Language";
      String MODIFY_POLICY = "X-Modify-Policy";
    }

    interface Query {
      String CFIELDS = "c_fields";
      String CLEVEL = "c_level";
      String LANG = "lang";
      String FROM = "from";
      String SIZE = "size";
      String STATE = "state";
      String SORT = "sort";
      String MODIFY_POLICY = "modify_policy";
    }
  }

  interface ContentDetail {
    String ALL_PREFIX = "all";
    String NESTED_PREFIX = "nest";
    String TITLE_SUFIX = "_title";
    String TRANS_SUFIX = "_trans";
    String ALL_OBJS_IDCODE = ALL_PREFIX + "_id";
    String ALL_OBJS_IDCODE_TITLE = ALL_PREFIX + "_title";
    String ALL_OBJS_IDCODE_TITLE_TRANS = ALL_PREFIX + "_trans";
    String NESTED_OBJS_IDCODE = NESTED_PREFIX + "_id";
    String NESTED_OBJS_IDCODE_TITLE = NESTED_PREFIX + "_title";
    String NESTED_OBJS_IDCODE_TITLE_TRANS = NESTED_PREFIX + "_trans";
  }
}
