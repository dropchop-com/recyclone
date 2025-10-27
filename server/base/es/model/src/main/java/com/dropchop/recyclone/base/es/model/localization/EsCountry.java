package com.dropchop.recyclone.base.es.model.localization;

import com.dropchop.recyclone.base.api.model.localization.Country;
import com.dropchop.recyclone.base.api.model.marker.HasTags;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.api.model.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.base.es.model.base.EsCode;
import com.dropchop.recyclone.base.es.model.base.EsTitleTranslationHelper;
import com.dropchop.recyclone.base.es.model.marker.HasEsLanguage;
import com.dropchop.recyclone.base.es.model.tagging.EsTag;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * Country with ISO 3166 2-letter code.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 12. 21.
 */
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("unused")
@JsonInclude(NON_EMPTY)
public class EsCountry extends EsCode
  implements Country<EsTitleTranslation>, EsTitleTranslationHelper,
  HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon, HasEsLanguage,
  HasTags<EsTag, EsTitleDescriptionTranslation> {

  private String title;

  private String lang;

  private EsLanguage language;

  private Set<EsTitleTranslation> translations;

  private List<EsTag> tags;

  private ZonedDateTime created;

  private ZonedDateTime modified;

  private ZonedDateTime deactivated;

  public EsCountry(@NonNull String code) {
    super(code);
  }
}
