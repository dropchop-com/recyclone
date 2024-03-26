package com.dropchop.recyclone.model.entity.jpa.base;

import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.marker.HasUuid;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@MappedSuperclass
public abstract class EUuid implements Entity, HasUuid, Comparable<EUuid> {

  @Id
  @NonNull
  private UUID uuid;

  @Override
  public int compareTo(EUuid o) {
    return this.getUuid().compareTo(o.getUuid());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EUuid oUuid)) {
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
