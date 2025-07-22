package com.dropchop.recyclone.base.dto.model.cache;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 27. 04. 2025.
 */
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("unused")
public class GroupCacheKey extends CacheKey implements com.dropchop.recyclone.base.api.model.cache.GroupCacheKey {

  private String searchGroup;

  public GroupCacheKey(@NonNull String code) {
    super(code);
  }

  public GroupCacheKey(@NonNull String code, @NonNull String lang) {
    super(code, lang);
  }

  public GroupCacheKey forSearch(String searchGroup, String searchLang) {
    this.setSearchGroup(searchGroup);
    this.setSearchLang(searchLang);
    return this;
  }
}
