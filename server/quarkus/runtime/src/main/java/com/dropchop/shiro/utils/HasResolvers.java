package com.dropchop.shiro.utils;

import com.dropchop.recyclone.model.api.base.Model;

/**
 * Mark subject mapper with resolver capabilities.
 */
public interface HasResolvers {

  Resolver getResolver(Class<? extends Model> subject);
}
