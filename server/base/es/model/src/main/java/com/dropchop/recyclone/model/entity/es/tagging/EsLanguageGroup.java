package com.dropchop.recyclone.model.entity.es.tagging;

import com.dropchop.recyclone.base.api.model.tagging.LanguageGroup;
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
public class EsLanguageGroup extends EsNamedTag implements LanguageGroup<EsTag, EsTitleDescriptionTranslation> {

  public EsLanguageGroup(@NonNull String name) {
    super(name);
  }
}
