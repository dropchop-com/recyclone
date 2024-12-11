package com.dropchop.recyclone.model.entity.es.localization;

import com.dropchop.recyclone.model.api.localization.TitleDescriptionTranslation;
import lombok.*;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 12. 21.
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EsTitleDescriptionTranslation
  implements TitleDescriptionTranslation {

  @NonNull
  @EqualsAndHashCode.Include
  private String lang;

  private EsLanguage language;

  @NonNull
  private String title;

  private String description;

  private ZonedDateTime created;

  private ZonedDateTime modified;

  transient Boolean base;
}
