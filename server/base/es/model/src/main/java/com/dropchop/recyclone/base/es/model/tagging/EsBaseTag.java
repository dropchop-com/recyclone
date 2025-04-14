package com.dropchop.recyclone.base.es.model.tagging;

import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.api.model.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.base.api.model.tagging.Tag;
import com.dropchop.recyclone.base.es.model.attr.EsAttribute;
import com.dropchop.recyclone.base.es.model.base.EsUuid;
import com.dropchop.recyclone.base.es.model.localization.EsLanguage;
import com.dropchop.recyclone.base.es.model.localization.EsTitleDescriptionTranslation;
import com.dropchop.recyclone.base.es.model.marker.HasEsAttributes;
import com.dropchop.recyclone.base.es.model.marker.HasEsLanguage;
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
public class EsBaseTag<T extends EsBaseTag<T, TT>, TT extends EsTitleDescriptionTranslation> extends EsUuid
  implements Tag<T, TT>,
  HasCreated, HasDeactivated, HasModified, HasStateInlinedCommon, HasEsLanguage, HasEsAttributes {

  private String type = this.getClass().getSimpleName().substring(2);

  private String title;

  private String description;

  private String lang;

  private EsLanguage language;

  private Set<TT> translations;

  private Set<EsAttribute<?>> esAttributes = new HashSet<>();

  private List<T> tags;

  private ZonedDateTime created;

  private ZonedDateTime modified;

  private ZonedDateTime deactivated;
}
