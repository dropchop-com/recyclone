package com.dropchop.recyclone.base.jpa.model.tagging;

import com.dropchop.recyclone.base.api.model.tagging.Owner;
import com.dropchop.recyclone.base.jpa.model.localization.JpaTitleDescriptionTranslation;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 06. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("Owner")
@SuppressWarnings("unused")
public class JpaOwner extends JpaNamedTag
  implements Owner<JpaTag, JpaTitleDescriptionTranslation> {
  public JpaOwner(@NonNull String name) {
    super(name);
  }
}
