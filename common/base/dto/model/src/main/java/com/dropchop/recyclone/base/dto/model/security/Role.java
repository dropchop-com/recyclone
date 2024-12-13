package com.dropchop.recyclone.base.dto.model.security;

import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.api.model.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.base.dto.model.base.DtoCode;
import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(NON_NULL)
@SuppressWarnings("unused")
public class Role extends DtoCode
  implements com.dropchop.recyclone.base.api.model.security.Role<TitleDescriptionTranslation, Action, Domain, Permission>,
  HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon {

  @JsonInclude(NON_EMPTY)
  Set<Permission> permissions = new LinkedHashSet<>();

  private String title;

  private String description;

  private String lang;

  @JsonInclude(NON_EMPTY)
  private Set<TitleDescriptionTranslation> translations;

  private ZonedDateTime created;

  private ZonedDateTime modified;

  private ZonedDateTime deactivated;
}