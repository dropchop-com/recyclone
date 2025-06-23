package com.dropchop.recyclone.base.es.model.tagging;

import com.dropchop.recyclone.base.api.model.tagging.Shared;
import com.dropchop.recyclone.base.es.model.localization.EsTitleDescriptionTranslation;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 06. 22.
 */
@Getter
@Setter
@SuppressWarnings("unused")
public class EsShared extends EsNamedTag
  implements Shared<EsTag, EsTitleDescriptionTranslation> {

  public EsShared(@NonNull String name) {
    super(name);
  }
}
