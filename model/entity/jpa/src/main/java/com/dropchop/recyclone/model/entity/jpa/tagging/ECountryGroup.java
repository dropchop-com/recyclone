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
@DiscriminatorValue("CountryGroup")
public class ECountryGroup extends ENamedTag
  implements com.dropchop.recyclone.model.api.tagging.CountryGroup<ETag, ETitleDescriptionTranslation> {
  public ECountryGroup(@NonNull String name) {
    super(name);
  }
}
