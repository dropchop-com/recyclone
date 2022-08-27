package com.dropchop.recyclone.model.dto.security;

import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.common.Person;
import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(NON_NULL)
public class User<O extends DtoId> extends Person
  implements com.dropchop.recyclone.model.api.security.User<
  UserAccount, TitleDescriptionTranslation, TitleTranslation,
  Action, Domain, Permission, Role, O, Country, Language, Tag>,
  com.dropchop.recyclone.model.api.common.Person<
    Country, Language, TitleTranslation>{

  @Singular
  @JsonInclude(NON_EMPTY)
  private SortedSet<Role> roles;

  @Singular
  @JsonInclude(NON_EMPTY)
  private SortedSet<Permission> permissions;

  @Singular
  @JsonInclude(NON_EMPTY)
  private Set<UserAccount> accounts = new LinkedHashSet<>();

  private ZonedDateTime created;
  private String createdBy;

  private ZonedDateTime modified;
  private String modifiedBy;

  private ZonedDateTime deleted;
  private String deletedBy;

  private ZonedDateTime deactivated;
  private String deactivatedBy;

  O owner;

  @Singular
  @JsonInclude(NON_EMPTY)
  private List<Tag> tags;
}
