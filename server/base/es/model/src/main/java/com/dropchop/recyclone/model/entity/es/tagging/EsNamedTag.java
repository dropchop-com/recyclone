package com.dropchop.recyclone.model.entity.es.tagging;

import com.dropchop.recyclone.base.api.model.tagging.NamedTag;
import com.dropchop.recyclone.model.entity.es.localization.EsTitleDescriptionTranslation;
import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 1. 06. 22.
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class EsNamedTag extends EsTag
  implements NamedTag<EsTag, EsTitleDescriptionTranslation> {

  @NonNull
  private String name;

  @Override
  public String toString() {
    return super.toString() + ",n" + ":" + getName();
  }
}
