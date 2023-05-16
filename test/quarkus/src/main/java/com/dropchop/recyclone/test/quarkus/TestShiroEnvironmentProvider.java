package com.dropchop.recyclone.test.quarkus;

import com.dropchop.shiro.cdi.ShiroEnvironmentProvider;
import com.dropchop.shiro.filter.AccessControlFilter;
import com.dropchop.shiro.filter.ApiKeyHttpAuthenticationFilter;
import com.dropchop.shiro.filter.BasicHttpAuthenticationFilter;
import com.dropchop.shiro.filter.BearerHttpAuthenticationFilter;
import com.dropchop.shiro.realm.ShiroMapRealm;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.event.EventBus;
import org.apache.shiro.event.support.DefaultEventBus;
import org.apache.shiro.realm.Realm;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import java.util.List;
import java.util.Map;

import static com.dropchop.shiro.filter.ApiKeyHttpAuthenticationFilter.DEFAULT_API_KEY_HEADER;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 01. 22.
 */
@ApplicationScoped
public class TestShiroEnvironmentProvider extends ShiroEnvironmentProvider {

  @Produces
  public CacheManager getCacheManager() {
    return null;
  }

  @Produces
  public RolePermissionResolver getRolePermissionResolver() {
    return null;
  }

  @Produces
  public PermissionResolver getPermissionResolver() {
    return new WildcardPermissionResolver();
  }

  @Produces
  public EventBus getEventBus() {
    return new DefaultEventBus();
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
          "admin", "*",
          "staff", "localization.language:*," +
            "security.action:*," +
            "security.domain:*," +
            "security.permission:*," +
            "security.role:*",
          "user", "localization.language:view"
        )
      )
    );
  }

  @Produces
  public List<AccessControlFilter> getFilters() {
    return List.of(
      new BasicHttpAuthenticationFilter(),
      new BearerHttpAuthenticationFilter(),
      new ApiKeyHttpAuthenticationFilter(DEFAULT_API_KEY_HEADER, "api_key")
    );
  }
}
