package com.dropchop.recyclone.model.dto.localization;

import com.dropchop.recyclone.model.api.marker.HasTags;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.model.dto.base.DtoCode;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Country with ISO 3166 2-letter code.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 12. 21.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class Country extends DtoCode
  implements com.dropchop.recyclone.model.api.localization.Country<TitleTranslation>,
  HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon,
  HasTags<Tag, TitleDescriptionTranslation> {

  public Country(@NonNull String code) {
    super(code);
  }

  private String title;

  private String lang;

  @JsonInclude(NON_EMPTY)
  private Set<TitleTranslation> translations;

  @JsonInclude(NON_EMPTY)
  private List<Tag> tags;

  private ZonedDateTime created;

  private ZonedDateTime modified;

  private ZonedDateTime deactivated;
}
