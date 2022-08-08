package com.dropchop.recyclone.model.entity.jpa.tagging;

import com.dropchop.recyclone.model.api.tagging.NamedTag;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleDescriptionTranslation;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 06. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@SuppressWarnings("JpaDataSourceORMInspection")
public class ENamedTag extends ETag
  implements NamedTag<ETag, ETitleDescriptionTranslation> {

  @NonNull
  @Column(name="name")
  private String name;
}
