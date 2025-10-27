package com.dropchop.recyclone.base.es.model.base;

import com.dropchop.recyclone.base.api.model.marker.HasCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@JsonInclude(NON_EMPTY)
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
