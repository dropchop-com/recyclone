package com.dropchop.recyclone.model.entity.jpa.base;

import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.marker.HasCode;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.util.Objects;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@MappedSuperclass
public abstract class JpaCode implements Entity, HasCode, Comparable<JpaCode> {

  @Id
  @NonNull
  private String code;

  @Override
  public int compareTo(JpaCode o) {
    String _code = this.getCode();
    return _code.compareTo(o.getCode());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof JpaCode jpaCode)) {
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
