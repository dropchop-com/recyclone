package com.dropchop.recyclone.model.entity.jpa.tagging;

import com.dropchop.recyclone.model.entity.jpa.localization.ETitleDescriptionTranslation;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 06. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@DiscriminatorValue("LanguageGroup")
public class ELanguageGroup extends ENamedTag
  implements com.dropchop.recyclone.model.api.tagging.LanguageGroup<ETag, ETitleDescriptionTranslation>{

  public ELanguageGroup(@NonNull String name) {
    super(name);
  }
}
