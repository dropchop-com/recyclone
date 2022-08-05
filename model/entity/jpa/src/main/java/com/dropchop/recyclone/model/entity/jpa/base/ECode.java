package com.dropchop.recyclone.model.entity.jpa.base;

import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.marker.HasCode;
import lombok.*;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@MappedSuperclass
public abstract class ECode implements Entity, HasCode, Comparable<ECode> {

  @Id
  @NonNull
  private String code;

  @Override
  public int compareTo(ECode o) {
    String _code = this.getCode();
    return _code.compareTo(o.getCode());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ECode eCode)) {
      return false;
    }
    return Objects.equals(this.getCode(), eCode.getCode());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return "code=" + this.getCode();
  }
}
