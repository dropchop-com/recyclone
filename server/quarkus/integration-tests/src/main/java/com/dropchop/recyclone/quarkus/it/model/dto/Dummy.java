package com.dropchop.recyclone.quarkus.it.model.dto;

import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.model.dto.base.DtoCode;
import com.dropchop.recyclone.model.dto.localization.TitleDescriptionTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 03. 24.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class Dummy extends DtoCode implements HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon {
    /*implements com.dropchop.recyclone.quarkus.it.model.api.Dummy<TitleDescriptionTranslation>,
    HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon {*/

  private String title;

  private String description;

  private String lang;

  //@JsonInclude(NON_EMPTY)
  //private Set<TitleDescriptionTranslation> translations;

  private ZonedDateTime created;

  private ZonedDateTime modified;

  private ZonedDateTime deactivated;
}
