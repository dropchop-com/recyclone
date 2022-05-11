package com.dropchop.recyclone.model.api.rest;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 03. 22.
 */
public interface Constants {

  interface Tags {
    String LOCALIZATION = "localization";
    String SECURITY     = "security";

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
    String SEARCH = "/search";

    interface Localization {
      String LANGUAGE = "/localization/language";
    }

    interface Security {
      String ACTION     = "/security/action";
      String DOMAIN     = "/security/domain";
      String ROLE       = "/security/role";
      String PERMISSION = "/security/permission";
      String USER       = "/security/user";
    }

    String INTERNAL = "/internal";
    String PUBLIC   = "/public";
  }

  interface ContentDetail {
    String ALL_PREFIX = "all";
    String NESTED_PREFIX = "nest";
    String ALL_OBJS_IDCODE = ALL_PREFIX + "_id";
    String ALL_OBJS_IDCODE_TITLE = ALL_PREFIX + "_title";
    String ALL_OBJS_IDCODE_TITLE_TRANS = ALL_PREFIX + "_trans";
    String NESTED_OBJS_IDCODE = NESTED_PREFIX + "_id";
    String NESTED_OBJS_IDCODE_TITLE = NESTED_PREFIX + "_title";
    String NESTED_OBJS_IDCODE_TITLE_TRANS = NESTED_PREFIX + "_trans";
  }
}
