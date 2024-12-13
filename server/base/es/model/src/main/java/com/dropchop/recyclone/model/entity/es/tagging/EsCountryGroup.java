package com.dropchop.recyclone.model.entity.es.tagging;

import com.dropchop.recyclone.base.api.model.tagging.CountryGroup;
import com.dropchop.recyclone.model.entity.es.localization.EsTitleDescriptionTranslation;
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
@SuppressWarnings("unused")
public class EsCountryGroup extends EsNamedTag implements CountryGroup<EsTag, EsTitleDescriptionTranslation> {
  public EsCountryGroup(@NonNull String name) {
    super(name);
  }
}
