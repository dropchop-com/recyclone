package com.dropchop.recyclone.base.api.model.cache;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 21. 07. 2025
 */
@SuppressWarnings("unused")
public interface TextCacheKey extends CacheKey {
  String getSearchText();
  void setSearchText(String searchText);
}
