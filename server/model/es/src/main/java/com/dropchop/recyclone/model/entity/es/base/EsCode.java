package com.dropchop.recyclone.model.entity.es.base;

import com.dropchop.recyclone.model.api.marker.HasCode;
import lombok.*;

import java.util.Objects;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public abstract class EsCode implements EsEntity, HasCode, Comparable<EsCode> {

  @NonNull
  private String code;

  @Override
  public int compareTo(EsCode o) {
    String _code = this.getCode();
    return _code.compareTo(o.getCode());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EsCode jpaCode)) {
      return false;
    }
    return Objects.equals(this.getCode(), jpaCode.getCode());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ":" + this.getCode();
  }
}
