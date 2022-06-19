package com.dropchop.recyclone.test.quarkus;

import com.dropchop.recyclone.rest.jaxrs.server.RecycloneApplicationRegistry;
import com.dropchop.shiro.RecycloneShiroExtension;
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
import java.util.LinkedHashSet;
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
    title="Recyclone API",
    version = "1.0.1",
    contact = @Contact(
      name = "Recyclone API Support",
      url = "https://exampleurl.com/contact",
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
    Set<Class<?>> container = new LinkedHashSet<>();
    container.addAll(RecycloneShiroExtension.getRestLayerRegistrationClasses());
    container.addAll(RecycloneApplicationRegistry.getRestLayerRegistrationClasses());
    container.addAll(RecycloneApplicationRegistry.getRestLocalizationResourceClasses());
    container.addAll(RecycloneApplicationRegistry.getRestSecurityResourceClasses());
    container.addAll(RecycloneApplicationRegistry.getRestTaggingResourceClasses());
    return container;
  }
}
