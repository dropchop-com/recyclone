package com.dropchop.recyclone.model.dto.security;

import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.model.dto.base.DtoCode;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(NON_NULL)
public class Role extends DtoCode
  implements com.dropchop.recyclone.model.api.security.Role<TitleTranslation, Action, Domain, Permission>,
  HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon {

  @JsonInclude(NON_EMPTY)
  SortedSet<Permission> permissions = new TreeSet<>();

  private String title;

  private String lang;

  @JsonInclude(NON_EMPTY)
  private Set<TitleTranslation> translations;

  private ZonedDateTime created;

  private ZonedDateTime modified;

  private ZonedDateTime deactivated;
}
