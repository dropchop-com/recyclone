package com.dropchop.recyclone.quarkus.it.app;

import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.dto.tagging.LanguageGroup;
import com.dropchop.recyclone.model.entity.jpa.tagging.JpaLanguageGroup;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import com.dropchop.recyclone.quarkus.runtime.rest.RestClass;
import com.dropchop.recyclone.quarkus.runtime.rest.RestMapping;
import com.dropchop.recyclone.quarkus.runtime.app.RecycloneApplication;
import com.dropchop.recyclone.quarkus.runtime.rest.jackson.ObjectMapperFactory;
import com.dropchop.shiro.cdi.DefaultShiroEnvironmentProvider;
import com.dropchop.shiro.filter.ApiKeyHttpAuthenticationFilter;
import com.dropchop.shiro.filter.ShiroFilter;
import com.dropchop.shiro.realm.ShiroMapRealm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.dropchop.shiro.filter.ApiKeyHttpAuthenticationFilter.DEFAULT_API_KEY_HEADER;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 06. 22.
 */
@SuppressWarnings("unused")
@Alternative
@Priority(1)
@ApplicationScoped
public class ApplicationConfiguration extends DefaultShiroEnvironmentProvider {

  @Inject
  ObjectMapperFactory objectMapperFactory;

  @Inject
  MapperSubTypeConfig mapperConfig;

  @Inject
  RecycloneApplication application;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  RestMapping restMapping;

  /**
   * ObjectMapper customization/extension point. Add own polymorphic mappings here.
   * This is needed because of polymorphism if you want to use existing To[XY]TagMapper.
   * If provide your own To[XY]MyCustomTagMapper then you (probably) don't need this.
   */
  @Produces
  @ApplicationScoped
  ObjectMapper getObjectMapper() {
    // this is just for demo
    RecycloneBuildConfig buildConfig = application.getBuildConfig();

    // this is just for demo
    Map<String, RestClass> restClassMap = restMapping.getApiClasses();

    //mapping is already there collected from the @SubclassMapping annotation of the TagToDtoMapper,
    //but we add it again here just for demonstration
    mapperConfig.addBidiMapping(LanguageGroup.class, JpaLanguageGroup.class);
    return objectMapperFactory.createObjectMapper();
  }

  @Produces
  public List<Realm> getRealms() {
    return List.of(
        new ShiroMapRealm(
            Map.of(
                "user1", "password,user",
                "editor1", "password,staff",
                "admin1", "password,admin",
                "usertoken1", "usertoken1,user",
                "editortoken1", "editortoken1,staff",
                "admintoken1", "admintoken1,admin"
            ),
            Map.of(
                "admin",
                "*",
                "staff",
                "localization.language:*," +
                    "security.action:*," +
                    "security.domain:*," +
                    "security.permission:*," +
                    "security.role:*",
                "user",
                "localization.language:view," +
                    "test.dummy:view"
            )
        )
    );
  }

  @Produces
  public List<ShiroFilter> getFilters() {
    List<ShiroFilter> filters = new ArrayList<>(super.getFilters());
    filters.add(new ApiKeyHttpAuthenticationFilter(DEFAULT_API_KEY_HEADER, "api_key"));
    return filters;
  }
}
