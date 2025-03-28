package com.dropchop.recyclone.quarkus.it.model.entity.es;

import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.api.model.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.base.es.model.base.EsCode;
import com.dropchop.recyclone.base.es.model.base.EsTitleDescriptionTranslationHelper;
import com.dropchop.recyclone.base.es.model.localization.EsLanguage;
import com.dropchop.recyclone.base.es.model.localization.EsTitleDescriptionTranslation;
import com.dropchop.recyclone.base.es.model.marker.HasEsLanguage;
import com.dropchop.recyclone.quarkus.it.model.api.Dummy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 12. 24.
 */
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("unused")
public class EsDummy extends EsCode
    implements Dummy<EsTitleDescriptionTranslation>, EsTitleDescriptionTranslationHelper,
    HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon, HasEsLanguage {

  private String title;

  private String description;

  private String lang;

  private EsLanguage language;

  private Set<EsTitleDescriptionTranslation> translations;

  private ZonedDateTime created;

  private ZonedDateTime modified;

  private ZonedDateTime deactivated;

  public EsDummy(@NonNull String code) {
    super(code);
  }
}
