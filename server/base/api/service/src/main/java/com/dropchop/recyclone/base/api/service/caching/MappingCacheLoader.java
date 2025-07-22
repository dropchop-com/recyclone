package com.dropchop.recyclone.base.api.service.caching;

import java.util.Map;

/**
 * A specialized {@link CacheLoader} interface designed to work with mappings of type {@code Map<String, Object>}.
 * The {@code MappingCacheLoader} provides a method to retrieve a {@link CacheMapper} that facilitates the transformation
 * or mapping of the cached entries into a specific target type.
 *
 * @param <T> the target type to which the cached data will be mapped
 */
public interface MappingCacheLoader<T> extends CacheLoader<Map<String, Object>> {

  CacheMapper<Map<String, Object>, T> getMapper();
}
