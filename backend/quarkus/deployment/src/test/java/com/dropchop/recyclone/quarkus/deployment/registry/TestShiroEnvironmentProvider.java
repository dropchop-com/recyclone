package com.dropchop.recyclone.quarkus.deployment.registry;

import com.dropchop.shiro.cdi.ShiroEnvironmentProvider;
import com.dropchop.shiro.filter.ShiroFilter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.event.EventBus;
import org.apache.shiro.realm.Realm;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 01. 22.
 */
@ApplicationScoped
public class TestShiroEnvironmentProvider extends ShiroEnvironmentProvider {

  @Produces
  public CacheManager getCacheManager() {
    return super.getCacheManager();
  }

  @Produces
  public RolePermissionResolver getRolePermissionResolver() {
    return super.getRolePermissionResolver();
  }

  @Produces
  public PermissionResolver getPermissionResolver() {
    return super.getPermissionResolver();
  }

  @Produces
  public EventBus getEventBus() {
    return super.getEventBus();
  }

  @Produces
  public List<Realm> getRealms() {
    return super.getRealms();
  }

  @Produces
  public List<ShiroFilter> getFilters() {
    return super.getFilters();
  }
}
