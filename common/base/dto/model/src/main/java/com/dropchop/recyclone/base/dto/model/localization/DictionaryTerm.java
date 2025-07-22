package com.dropchop.recyclone.base.dto.model.localization;

import com.dropchop.recyclone.base.api.model.marker.HasTags;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.api.model.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.base.dto.model.base.DtoCode;
import com.dropchop.recyclone.base.dto.model.tagging.Tag;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuppressWarnings("unused")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class DictionaryTerm  extends DtoCode
    implements com.dropchop.recyclone.base.api.model.localization.DictionaryTerm<Tag, TitleTranslation, TitleDescriptionTranslation>,
    HasCreated, HasModified, HasTags<Tag, TitleDescriptionTranslation>
{

  private String title;
  private String lang;

  private ZonedDateTime created;
  private ZonedDateTime modified;

  @JsonInclude(NON_EMPTY)
  private Set<TitleTranslation> translations;

  @JsonInclude(NON_EMPTY)
  private List<Tag> tags;
}
