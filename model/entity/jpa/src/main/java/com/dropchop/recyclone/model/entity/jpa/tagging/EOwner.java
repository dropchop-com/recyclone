package com.dropchop.recyclone.model.entity.jpa.tagging;

import com.dropchop.recyclone.model.entity.jpa.localization.ETitleDescriptionTranslation;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 06. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("Owner")
public class EOwner extends ENamedTag
  implements com.dropchop.recyclone.model.api.tagging.Owner<ETag, ETitleDescriptionTranslation> {
  public EOwner(@NonNull String name) {
    super(name);
  }
}
