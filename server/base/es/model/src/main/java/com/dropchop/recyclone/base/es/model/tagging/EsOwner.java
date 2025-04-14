package com.dropchop.recyclone.base.es.model.tagging;

import com.dropchop.recyclone.base.api.model.tagging.Owner;
import com.dropchop.recyclone.base.es.model.localization.EsTitleDescriptionTranslation;
import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 06. 22.
 */
@Getter
@Setter
@SuppressWarnings("unused")
public class EsOwner extends EsNamedTag
  implements Owner<EsTag, EsTitleDescriptionTranslation> {

  public EsOwner(@NonNull String name) {
    super(name);
  }
}
