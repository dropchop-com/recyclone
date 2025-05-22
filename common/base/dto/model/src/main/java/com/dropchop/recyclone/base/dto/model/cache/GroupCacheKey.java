package com.dropchop.recyclone.base.dto.model.cache;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 27. 04. 25.
 */
@Getter
@Setter
@NoArgsConstructor
public class GroupCacheKey extends CacheKey implements com.dropchop.recyclone.base.api.model.cache.GroupCacheKey {
  public GroupCacheKey(@NonNull String code) {
    super(code);
  }
}
