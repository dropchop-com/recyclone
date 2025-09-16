package com.dropchop.recyclone.base.dto.model.security;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.dropchop.recyclone.base.dto.model.common.Person;
import com.dropchop.recyclone.base.dto.model.localization.Country;
import com.dropchop.recyclone.base.dto.model.localization.Language;
import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.base.dto.model.localization.TitleTranslation;
import com.dropchop.recyclone.base.dto.model.tagging.Tag;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class User extends Person
  implements com.dropchop.recyclone.base.api.model.security.User<
      UserAccount, TitleDescriptionTranslation, TitleTranslation,
      Action, Domain, Permission, Role, Country, Language, Tag>,
    com.dropchop.recyclone.base.api.model.common.Person<Country, Language, TitleTranslation> {

  public enum Fields {
    accounts, roles, permissions, tags, attributes, owner
  }

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

  public User cloneSimplified(EnumSet<Fields> fieldsToExclude) {
    User newUser;
    try {
      newUser = (User) super.clone();
      if (fieldsToExclude.contains(Fields.attributes)) {
        newUser.setAttributes(null);
      }
      if (fieldsToExclude.contains(Fields.tags)) {
        newUser.setTags(null);
      }
      if (fieldsToExclude.contains(Fields.accounts)) {
        newUser.setAccounts(null);
      }
      if (fieldsToExclude.contains(Fields.roles)) {
        newUser.setRoles(null);
      }
      if (fieldsToExclude.contains(Fields.permissions)) {
        newUser.setPermissions(null);
      }
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
    return newUser;
  }

  public User cloneSimplified() {
    return cloneSimplified(
        EnumSet.of(Fields.accounts, Fields.roles, Fields.permissions)
    );
  }
}
