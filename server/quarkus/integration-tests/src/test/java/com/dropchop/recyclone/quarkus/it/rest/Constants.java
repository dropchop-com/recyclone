package com.dropchop.recyclone.quarkus.it.rest;

import static com.dropchop.recyclone.base.api.model.rest.Constants.Paths.Event.EVENTS;
import static com.dropchop.recyclone.base.api.model.rest.Constants.Paths.INTERNAL_SEGMENT;
import static com.dropchop.recyclone.base.api.model.rest.Constants.Paths.Localization.LANGUAGE;
import static com.dropchop.recyclone.base.api.model.rest.Constants.Paths.PUBLIC_SEGMENT;
import static com.dropchop.recyclone.base.api.model.rest.Constants.Paths.Security.*;
import static com.dropchop.recyclone.base.api.model.rest.Constants.Paths.Tagging.TAG;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 27. 08. 2025
 */
public class Constants {
  public static final String ADMIN_USER = "admin1";
  public static final String EDITOR_USER = "editor1";
  public static final String USER_USER = "user1";
  public static final String TEST_PASSWORD = "password";
  public static final String LANG_ENDPOINT = "/api" + PUBLIC_SEGMENT + LANGUAGE;
  public static final String TAG_ENDPOINT = "/api" + INTERNAL_SEGMENT + TAG;
  public static final String EVENTS_ENDPOINT = "/api" + INTERNAL_SEGMENT + EVENTS;
  public static final String USER_ENDPOINT = "/api" + INTERNAL_SEGMENT + USER;
  public static final String PERM_ENDPOINT = "/api" + INTERNAL_SEGMENT + PERMISSION;
  public static final String ROLE_NODE_PERM_ENDPOINT = "/api" + INTERNAL_SEGMENT + ROLE_NODE_PERMISSION;
  public static final String ROLE_NODE_ENDPOINT = "/api" + INTERNAL_SEGMENT + ROLE_NODE;
  public static final String ROLE_ENDPOINT = "/api" + INTERNAL_SEGMENT + ROLE;
  public static final String ROLE_PERM_ENDPOINT = ROLE_ENDPOINT + PERMISSION_SEGMENT;
}
