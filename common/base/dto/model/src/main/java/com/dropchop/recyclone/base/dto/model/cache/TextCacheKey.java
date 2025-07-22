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
@SuppressWarnings("unused")
public class TextCacheKey extends CacheKey implements com.dropchop.recyclone.base.api.model.cache.TextCacheKey {

  private String searchText;

  public TextCacheKey(@NonNull String code) {
    super(code);
  }

  public TextCacheKey(@NonNull String code, @NonNull String lang) {
    super(code, lang);
  }

  public TextCacheKey forSearch(String searchText, String searchLang) {
    this.setSearchText(searchText);
    this.setSearchLang(searchLang);
    return this;
  }
}
