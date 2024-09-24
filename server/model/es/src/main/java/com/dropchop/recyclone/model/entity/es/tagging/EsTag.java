package com.dropchop.recyclone.model.entity.es.tagging;

import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.model.api.tagging.Tag;
import com.dropchop.recyclone.model.entity.es.attr.EsAttribute;
import com.dropchop.recyclone.model.entity.es.base.EsUuid;
import com.dropchop.recyclone.model.entity.es.localization.EsLanguage;
import com.dropchop.recyclone.model.entity.es.localization.EsTitleDescriptionTranslation;
import com.dropchop.recyclone.model.entity.es.marker.HasEsAttributes;
import com.dropchop.recyclone.model.entity.es.marker.HasEsLanguage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 1. 06. 22.
 */
@Getter
@Setter
@NoArgsConstructor
public class EsTag extends EsUuid
  implements Tag<EsTag, EsTitleDescriptionTranslation>,
  HasCreated, HasDeactivated, HasModified, HasStateInlinedCommon, HasEsLanguage, HasEsAttributes {

  private String type = this.getClass().getSimpleName().substring(1);

  private String title;

  private String description;

  private String lang;

  private EsLanguage language;

  private Set<EsTitleDescriptionTranslation> translations;

  private Set<EsAttribute<?>> esAttributes = new HashSet<>();

  private List<EsTag> tags;

  private ZonedDateTime created;

  private ZonedDateTime modified;

  private ZonedDateTime deactivated;
}
