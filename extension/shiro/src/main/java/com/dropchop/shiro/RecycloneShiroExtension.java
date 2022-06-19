package com.dropchop.shiro;

import com.dropchop.shiro.jaxrs.ShiroDynamicFeature;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 06. 22.
 */
public class RecycloneShiroExtension {
  public static Set<Class<?>> getRestLayerRegistrationClasses() {
    return Set.of(ShiroDynamicFeature.class);
  }
}
