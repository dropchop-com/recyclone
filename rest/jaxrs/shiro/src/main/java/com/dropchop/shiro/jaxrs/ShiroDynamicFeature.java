package com.dropchop.shiro.jaxrs;

import com.dropchop.shiro.filter.AccessControlFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.*;

import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 12. 21.
 */
@Slf4j
@Provider
public class ShiroDynamicFeature implements DynamicFeature {

  private static final List<Class<? extends Annotation>> shiroAnnotations =
    List.of(
      RequiresPermissions.class,
      RequiresRoles.class,
      RequiresAuthentication.class,
      RequiresUser.class,
      RequiresGuest.class
    );

  @Inject
  org.apache.shiro.mgt.SecurityManager securityManager;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  List<AccessControlFilter> accessControlFilters;

  @Override
  public void configure(ResourceInfo ri, FeatureContext context) {
    List<Annotation> authzSpecs = new ArrayList<>();
    for (Class<? extends Annotation> annotationClass : shiroAnnotations) {
      // XXX What is the performance of getAnnotation vs getAnnotations?
      Annotation classAuthzSpec = ri.getResourceClass().getAnnotation(annotationClass);
      Annotation methodAuthzSpec = ri.getResourceMethod().getAnnotation(annotationClass);

      if (classAuthzSpec != null) {
        authzSpecs.add(classAuthzSpec);
        log.debug("Registering {} [{}::{}]", classAuthzSpec.getClass().getSimpleName(),
          ri.getResourceClass().getSimpleName(), ri.getResourceMethod());
      }
      if (methodAuthzSpec != null) {
        authzSpecs.add(methodAuthzSpec);
        log.debug("Registering {} [{}::{}]", methodAuthzSpec.getClass().getSimpleName(),
          ri.getResourceClass().getSimpleName(), ri.getResourceMethod());
      }
    }

    if (!authzSpecs.isEmpty()) {
      log.debug("Registering ShiroFilter.");
      context.register(
        new ShiroContainerFilter(
          accessControlFilters,
          securityManager,
          authzSpecs),
        Priorities.AUTHORIZATION
      );
    }
  }
}
