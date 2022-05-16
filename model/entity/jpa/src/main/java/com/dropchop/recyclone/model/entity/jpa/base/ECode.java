package com.dropchop.recyclone.model.entity.jpa.base;

import com.dropchop.recyclone.model.api.Entity;
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
    return this.code.compareTo(o.code);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ECode)) return false;
    ECode eCode = (ECode) o;
    return Objects.equals(code, eCode.code);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("code=").append(code);
    return sb.toString();
  }
}
