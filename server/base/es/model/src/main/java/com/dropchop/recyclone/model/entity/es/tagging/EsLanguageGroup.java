package com.dropchop.recyclone.model.entity.es.tagging;

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
public class EsLanguageGroup extends EsNamedTag
  implements com.dropchop.recyclone.model.api.tagging.LanguageGroup<EsTag, EsTitleDescriptionTranslation>{

  public EsLanguageGroup(@NonNull String name) {
    super(name);
  }
}
