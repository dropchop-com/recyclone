package com.dropchop.recyclone.base.dto.model.cache;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 21. 07. 2025
 */
@Getter
@Setter
@NoArgsConstructor
public class TitleCacheKey extends CacheKey implements com.dropchop.recyclone.base.api.model.cache.TitleCacheKey {
  private String lang;

  public TitleCacheKey(@NonNull String code) {
    super(code);
  }

  public TitleCacheKey(@NonNull String code, String lang) {
    super(code);
    this.lang = lang;
  }
}
