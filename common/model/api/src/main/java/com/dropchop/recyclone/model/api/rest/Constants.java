package com.dropchop.recyclone.model.api.rest;

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
    String ELASTICSEARCH_SEARCH_SEGMENT = "/es_search";
    String ELASTICSEARCH_SAVE_SEGMENT = "/es_save";
    String ELASTICSEARCH_DELETE_SEGMENT = "/es_delete";

    String INTERNAL_SEGMENT = "/internal";
    String PUBLIC_SEGMENT = "/public";

    interface Localization {
      String LANGUAGE_SEGMENT = "/language";
      String LANGUAGE = LOCALIZATION_SEGMENT + LANGUAGE_SEGMENT;
      String COUNTRY_SEGMENT = "/country";
      String COUNTRY = LOCALIZATION_SEGMENT + COUNTRY_SEGMENT;
    }

    interface Security {
      String ACTION_SEGMENT = "/action";
      String ACTION = SECURITY_SEGMENT + ACTION_SEGMENT;
      String DOMAIN_SEGMENT = "/domain";
      String DOMAIN = SECURITY_SEGMENT + DOMAIN_SEGMENT;
      String ROLE_SEGMENT = "/role";
      String ROLE = SECURITY_SEGMENT + ROLE_SEGMENT;
      String PERMISSION_SEGMENT = "/permission";
      String PERMISSION = SECURITY_SEGMENT + PERMISSION_SEGMENT;
      String USER_SEGMENT = "/user";
      String USER = SECURITY_SEGMENT + USER_SEGMENT;
    }

    interface Tagging {
      String TAG_SEGMENT = "/tag";
      String TAG = TAGGING_SEGMENT + TAG_SEGMENT;
    }
  }

  interface Params {
    interface Header {
      String CFIELDS = "X-Content-Fields";
      String CLEVEL = "X-Content-Level";
      String VERSION = "X-Content-Version";
      String LANG = "Accept-Language";
    }

    interface Query {
      String CFIELDS = "c_fields";
      String CLEVEL = "c_level";
      String LANG = "lang";
      String FROM = "from";
      String SIZE = "size";
      String STATE = "state";
      String SORT = "sort";
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
