package com.dropchop.recyclone.base.jpa.model.tagging;

import com.dropchop.recyclone.base.api.model.tagging.CountryGroup;
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
@DiscriminatorValue("CountryGroup")
@SuppressWarnings("unused")
public class JpaCountryGroup extends JpaNamedTag
  implements CountryGroup<JpaTag, JpaTitleDescriptionTranslation> {
  public JpaCountryGroup(@NonNull String name) {
    super(name);
  }
}
