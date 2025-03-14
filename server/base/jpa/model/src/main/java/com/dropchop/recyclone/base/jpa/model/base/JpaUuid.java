package com.dropchop.recyclone.base.jpa.model.base;

import com.dropchop.recyclone.base.api.model.marker.HasUuid;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@MappedSuperclass
public abstract class JpaUuid implements JpaEntity, HasUuid, Comparable<JpaUuid> {

  @Id
  @NonNull
  private UUID uuid;

  @Override
  public int compareTo(JpaUuid o) {
    return this.getUuid().compareTo(o.getUuid());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof JpaUuid oUuid)) {
      return false;
    }
    return Objects.equals(this.getUuid(), oUuid.getUuid());
  }

  @Override
  public int hashCode() {
    return this.getUuid().hashCode();
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ":" + getUuid();
  }
}
