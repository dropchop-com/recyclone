package com.dropchop.recyclone.quarkus.it.app;

import com.dropchop.shiro.cdi.DefaultShiroEnvironmentProvider;
import com.dropchop.shiro.filter.*;
import com.dropchop.shiro.realm.ShiroMapRealm;
import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Alternative;
import org.apache.shiro.realm.Realm;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.dropchop.shiro.filter.ApiKeyHttpAuthenticationFilter.DEFAULT_API_KEY_HEADER;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 01. 22.
 */
@Alternative
@Priority(1)
@ApplicationScoped
public class ShiroEnvironmentProvider extends DefaultShiroEnvironmentProvider {

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
