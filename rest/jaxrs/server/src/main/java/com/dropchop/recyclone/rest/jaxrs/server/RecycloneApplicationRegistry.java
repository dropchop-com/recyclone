package com.dropchop.recyclone.rest.jaxrs.server;

import com.dropchop.recyclone.rest.jaxrs.provider.*;
import com.dropchop.recyclone.rest.jaxrs.server.localization.intern.CountryResource;
import com.dropchop.recyclone.rest.jaxrs.server.localization.intern.LanguageResource;
import com.dropchop.recyclone.rest.jaxrs.server.security.intern.*;
import com.dropchop.recyclone.rest.jaxrs.server.tagging.intern.TagResource;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 06. 22.
 */
public class RecycloneApplicationRegistry {

  public static Set<Class<?>> getRestLayerRegistrationClasses() {
    return Set.of(
      ServiceErrorExceptionMapper.class
      , ObjectMapperContextResolver.class
      , DefaultContentTypeFilter.class
      , CommonDynamicFeatures.class
      , ExecContextInitInterceptor.class
      , ExecContextWriteInterceptor.class
    );
  }

  public static Set<Class<?>> getRestLocalizationResourceClasses() {
    return Set.of(
      com.dropchop.recyclone.rest.jaxrs.server.localization.LanguageResource.class
      , LanguageResource.class
      , com.dropchop.recyclone.rest.jaxrs.server.localization.CountryResource.class
      , CountryResource.class
    );
  }

  public static Set<Class<?>> getRestTaggingResourceClasses() {
    return Set.of(
      com.dropchop.recyclone.rest.jaxrs.server.tagging.TagResource.class
      , TagResource.class
    );
  }

  public static Set<Class<?>> getRestSecurityResourceClasses() {
    return Set.of(
      ActionResource.class
      , DomainResource.class
      , PermissionResource.class
      , RoleResource.class
      , UserResource.class
    );
  }
}
