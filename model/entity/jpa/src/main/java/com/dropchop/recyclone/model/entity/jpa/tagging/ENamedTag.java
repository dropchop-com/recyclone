package com.dropchop.recyclone.model.entity.jpa.tagging;

import com.dropchop.recyclone.model.api.tagging.NamedTag;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleDescriptionTranslation;
import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 06. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@SuppressWarnings("JpaDataSourceORMInspection")
public class ENamedTag extends ETag
  implements NamedTag<ETag, ETitleDescriptionTranslation> {

  @NonNull
  @Column(name="name")
  private String name;

  @Override
  public String toString() {
    return super.toString() + ",n" + ":" + getName();
  }
}
