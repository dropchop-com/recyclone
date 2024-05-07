package com.dropchop.recyclone.model.entity.jpa.tagging;

import com.dropchop.recyclone.model.entity.jpa.localization.JpaTitleDescriptionTranslation;
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
@DiscriminatorValue("LanguageGroup")
@SuppressWarnings("unused")
public class JpaLanguageGroup extends JpaNamedTag
  implements com.dropchop.recyclone.model.api.tagging.LanguageGroup<JpaTag, JpaTitleDescriptionTranslation>{

  public JpaLanguageGroup(@NonNull String name) {
    super(name);
  }
}
