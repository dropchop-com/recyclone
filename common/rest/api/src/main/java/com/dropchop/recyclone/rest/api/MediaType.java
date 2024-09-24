package com.dropchop.recyclone.rest.api;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 12. 21.
 */
@SuppressWarnings("unused")
public class MediaType extends jakarta.ws.rs.core.MediaType {

  public static final String APPLICATION_JSON_DROPCHOP_RESULT
    = "application/vnd.dropchop.result+json";
  public static final jakarta.ws.rs.core.MediaType APPLICATION_JSON_DROPCHOP_RESULT_TYPE
    = new jakarta.ws.rs.core.MediaType("application", "vnd.dropchop.result+json");

  public static final String APPLICATION_JSON_DROPCHOP_PARAMS
    = "application/vnd.dropchop.params+json";
  public static final jakarta.ws.rs.core.MediaType APPLICATION_JSON_DROPCHOP_PARAMS_TYPE
    = new jakarta.ws.rs.core.MediaType("application", "vnd.dropchop.params+json");
}
