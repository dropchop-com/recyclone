package com.dropchop.shiro.utils;

import com.dropchop.recyclone.base.api.model.base.Model;

/**
 * Mark subject mapper with resolver capabilities.
 */
@SuppressWarnings("unused")
public interface HasResolvers {

  Resolver getResolver(Class<? extends Model> subject);
}
