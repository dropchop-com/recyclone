package com.dropchop.recyclone.model.dto.security;

import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.common.Person;
import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@JsonInclude(NON_NULL)
public class User<O extends DtoId> extends Person
  implements com.dropchop.recyclone.model.api.security.User<
  UserAccount, TitleDescriptionTranslation, TitleTranslation,
  Action, Domain, Permission, Role, O, Country, Language, Tag> {

  private SortedSet<Role> roles;
  private SortedSet<Permission> permissions;

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

  private List<Tag> tags;
}
