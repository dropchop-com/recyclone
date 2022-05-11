package com.dropchop.recyclone.test.quarkus;

import com.dropchop.recyclone.rest.jaxrs.server.CommonDynamicFeatures;
import com.dropchop.recyclone.rest.jaxrs.server.DefaultContentTypeFilter;
import com.dropchop.recyclone.rest.jaxrs.server.ObjectMapperContextResolver;
import com.dropchop.recyclone.rest.jaxrs.server.ServiceErrorExceptionMapper;
import com.dropchop.recyclone.rest.jaxrs.server.localization.intern.LanguageResource;
import com.dropchop.recyclone.rest.jaxrs.server.security.intern.ActionResource;
import com.dropchop.recyclone.rest.jaxrs.server.security.intern.DomainResource;
import com.dropchop.recyclone.rest.jaxrs.server.security.intern.RoleResource;
import com.dropchop.recyclone.rest.jaxrs.server.security.intern.UserResource;
import com.dropchop.shiro.jaxrs.ShiroDynamicFeature;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Components;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 12. 21.
 */
@Slf4j
@OpenAPIDefinition(
  /*tags = {
    @Tag(name = "core", description = "Core functionality."),
  },*/
  info = @Info(
    title="Example API",
    version = "1.0.1",
    contact = @Contact(
      name = "Example API Support",
      url = "http://exampleurl.com/contact",
      email = "techsupport@example.com"),
    license = @License(
      name = "Apache 2.0",
      url = "https://www.apache.org/licenses/LICENSE-2.0.html")),
  security = {
    @SecurityRequirement(name = "apiKey"),
    @SecurityRequirement(name = "BasicAuth"),
    @SecurityRequirement(name = "ApiKeyAuth"),
    @SecurityRequirement(name = "OpenID"),
    @SecurityRequirement(name = "JWT")
  },
  components = @Components(
    securitySchemes = {
        @SecurityScheme(securitySchemeName = "apiKey",
          type = SecuritySchemeType.HTTP,
          scheme = "Bearer"),
        @SecurityScheme(securitySchemeName = "BasicAuth",
          type = SecuritySchemeType.HTTP,
          scheme = "basic"),
        @SecurityScheme(securitySchemeName = "ApiKeyAuth",
          type = SecuritySchemeType.APIKEY,
          in = SecuritySchemeIn.HEADER,
          apiKeyName = "X-API-Key"),
        @SecurityScheme(securitySchemeName = "OpenID",
          type = SecuritySchemeType.OPENIDCONNECT,
          openIdConnectUrl = "https://example.com/.well-known/openid-configuration")
      }
  )
)
@SuppressWarnings("unused")
@ApplicationPath("/api")
public class TestApplication extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    return Set.of(
      ServiceErrorExceptionMapper.class
      , ObjectMapperContextResolver.class
      , ShiroDynamicFeature.class
      , DefaultContentTypeFilter.class
      , CommonDynamicFeatures.class
      , ActionResource.class
      , DomainResource.class
      , RoleResource.class
      , UserResource.class
      , com.dropchop.recyclone.rest.jaxrs.server.localization.LanguageResource.class
      , LanguageResource.class
    );
  }
}
