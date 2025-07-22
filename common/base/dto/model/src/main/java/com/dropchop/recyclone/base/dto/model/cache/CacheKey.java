package com.dropchop.recyclone.base.dto.model.cache;

import com.dropchop.recyclone.base.dto.model.base.DtoCode;
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
@SuppressWarnings("unused")
public class CacheKey extends DtoCode implements com.dropchop.recyclone.base.api.model.cache.CacheKey {

  private String lang;

  private String searchLang;
  private String searchCode;

  public CacheKey(@NonNull String code) {
    super(code);
  }

  public CacheKey(@NonNull String code, @NonNull String lang) {
    super(code);
    this.lang = lang;
  }

  public CacheKey forSearch(String identifier, String searchLang) {
    this.setSearchCode(identifier);
    this.setSearchLang(searchLang);
    return this;
  }
}
