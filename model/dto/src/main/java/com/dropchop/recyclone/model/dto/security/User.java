package com.dropchop.recyclone.model.dto.security;

import com.dropchop.recyclone.model.dto.DtoId;
import com.dropchop.recyclone.model.dto.Person;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.SortedSet;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class User<O extends DtoId> extends Person
  implements com.dropchop.recyclone.model.api.security.User<UserAccount, TitleTranslation, Action, Domain, Permission, Role, O> {

  private SortedSet<Role> roles;
  private SortedSet<Permission> permissions;

  private List<? extends UserAccount> accounts;

  private ZonedDateTime created;
  private String createdBy;

  private ZonedDateTime modified;
  private String modifiedBy;

  private ZonedDateTime deleted;
  private String deletedBy;

  private ZonedDateTime deactivated;
  private String deactivatedBy;

  O owner;
}
