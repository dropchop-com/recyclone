package com.dropchop.recyclone.model.entity.jpa.tagging;

import com.dropchop.recyclone.model.entity.jpa.localization.JpaTitleDescriptionTranslation;
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
@DiscriminatorValue("CountryGroup")
@SuppressWarnings("unused")
public class JpaCountryGroup extends JpaNamedTag
  implements com.dropchop.recyclone.model.api.tagging.CountryGroup<JpaTag, JpaTitleDescriptionTranslation> {
  public JpaCountryGroup(@NonNull String name) {
    super(name);
  }
}
