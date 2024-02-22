package com.dropchop.recyclone.model.dto.security;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.common.Person;
import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class User extends Person
  implements com.dropchop.recyclone.model.api.security.User<
  UserAccount, TitleDescriptionTranslation, TitleTranslation,
  Action, Domain, Permission, Role, Country, Language, Tag>,
  com.dropchop.recyclone.model.api.common.Person<
    Country, Language, TitleTranslation>{

  @JsonInclude(NON_EMPTY)
  private Set<Role> roles;

  @JsonInclude(NON_EMPTY)
  private Set<Permission> permissions;

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


  @JsonInclude(NON_EMPTY)
  private List<Tag> tags;

  @JsonInclude(NON_EMPTY)
  private Set<Attribute<?>> attributes;
}
