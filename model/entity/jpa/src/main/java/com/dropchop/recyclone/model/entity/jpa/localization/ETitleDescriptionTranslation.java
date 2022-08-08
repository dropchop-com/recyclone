package com.dropchop.recyclone.model.entity.jpa.localization;

import com.dropchop.recyclone.model.api.localization.TitleDescriptionTranslation;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
@Getter
@Setter
@RequiredArgsConstructor
@MappedSuperclass
@Embeddable
public class ETitleDescriptionTranslation extends ETitleTranslation
  implements TitleDescriptionTranslation {

  public ETitleDescriptionTranslation(@NonNull String lang, @NonNull String title) {
    super(lang, title);
  }

  @Column(name = "description")
  @EqualsAndHashCode.Exclude
  private String description;
}
