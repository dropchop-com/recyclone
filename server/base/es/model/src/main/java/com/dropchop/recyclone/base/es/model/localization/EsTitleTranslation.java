package com.dropchop.recyclone.base.es.model.localization;

import com.dropchop.recyclone.base.api.model.localization.TitleTranslation;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.es.model.marker.HasEsLanguage;
import lombok.*;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 12. 21.
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressWarnings("unused")
public class EsTitleTranslation
  implements HasEsLanguage, TitleTranslation, HasCreated, HasModified {

  @NonNull
  @EqualsAndHashCode.Include
  private String lang;

  private EsLanguage language;

  @NonNull
  private String title;

  private ZonedDateTime created;

  private ZonedDateTime modified;

  transient Boolean base;
}
