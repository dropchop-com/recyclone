package com.dropchop.recyclone.base.es.model.tagging;

import com.dropchop.recyclone.base.api.model.tagging.NamedTag;
import com.dropchop.recyclone.base.es.model.localization.EsTitleDescriptionTranslation;
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