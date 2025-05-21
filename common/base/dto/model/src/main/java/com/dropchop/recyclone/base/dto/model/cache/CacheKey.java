package com.dropchop.recyclone.base.dto.model.cache;

import com.dropchop.recyclone.base.dto.model.base.DtoCode;
import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 27. 04. 25.
 */
@Getter
@Setter
@NoArgsConstructor
public class CacheKey extends DtoCode implements com.dropchop.recyclone.base.api.model.cache.CacheKey {
  public CacheKey(@NonNull String code) {
    super(code);
  }
}
