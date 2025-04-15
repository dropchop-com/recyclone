package com.dropchop.recyclone.base.es.model.tagging;

import com.dropchop.recyclone.base.api.model.tagging.Group;
import com.dropchop.recyclone.base.es.model.localization.EsTitleDescriptionTranslation;
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
public class EsGroup extends EsNamedTag
    implements Group<EsTag, EsTitleDescriptionTranslation> {
  public EsGroup(@NonNull String name) {
    super(name);
  }
}
