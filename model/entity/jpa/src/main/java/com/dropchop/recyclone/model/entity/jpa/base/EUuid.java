package com.dropchop.recyclone.model.entity.jpa.base;

import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.marker.HasUuid;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 01. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@RequiredArgsConstructor
@MappedSuperclass
public abstract class EUuid implements Entity, HasUuid, Comparable<EUuid> {

  @Id
  @NonNull
  private UUID uuid;

  @Override
  public int compareTo(EUuid o) {
    return this.uuid.compareTo(o.uuid);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EUuid eUuid)) {
      return false;
    }
    return Objects.equals(uuid, eUuid.uuid);
  }

  @Override
  public int hashCode() {
    return uuid.hashCode();
  }

  @Override
  public String toString() {
    return "uuid=" + uuid;
  }
}
