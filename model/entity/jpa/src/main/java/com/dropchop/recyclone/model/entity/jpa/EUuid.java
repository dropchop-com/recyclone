package com.dropchop.recyclone.model.entity.jpa;

import com.dropchop.recyclone.model.api.Entity;
import com.dropchop.recyclone.model.api.marker.HasUuid;
import lombok.*;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 01. 22.
 */
@Getter
@Setter
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
    if (this == o) return true;
    if (!(o instanceof EUuid)) return false;
    EUuid eUuid = (EUuid) o;
    return Objects.equals(uuid, eUuid.uuid);
  }

  @Override
  public int hashCode() {
    return uuid.hashCode();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("");
    sb.append("uuid=").append(uuid);
    return sb.toString();
  }
}
