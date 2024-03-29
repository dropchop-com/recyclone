package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.model.api.security.User;
import com.dropchop.recyclone.model.entity.jpa.attr.EAttribute;
import com.dropchop.recyclone.model.entity.jpa.base.EUuid;
import com.dropchop.recyclone.model.entity.jpa.common.EPerson;
import com.dropchop.recyclone.model.entity.jpa.localization.ECountry;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleDescriptionTranslation;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleTranslation;
import com.dropchop.recyclone.model.entity.jpa.marker.HasEAttributes;
import com.dropchop.recyclone.model.entity.jpa.marker.HasELanguage;
import com.dropchop.recyclone.model.entity.jpa.tagging.ETag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 * @author Armando Ota <armando.ota@dropchop.org>
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "security_user")
@SuppressWarnings("unused")
public class EUser extends EPerson
    implements User<EUserAccount, ETitleDescriptionTranslation, ETitleTranslation, EAction, EDomain, EPermission, ERole, ECountry, ELanguage, ETag>,
    HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon, HasELanguage, HasEAttributes {

  @Column(name = "created")
  private ZonedDateTime created;

  @Column(name = "modified")
  private ZonedDateTime modified;

  @Column(name = "deactivated")
  private ZonedDateTime deactivated;

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  Set<EUserAccount> accounts = new LinkedHashSet<>();

  public void addAccount(EUserAccount account) {
    if (account == null) return;
    this.accounts.add(account);
    account.setUser(this);
  }

  public void removeAccount(EUserAccount account) {
    account.setUser(null);
    this.accounts.remove(account);
  }

  @ManyToMany
  @JoinTable(
      name = "security_user_t",
      uniqueConstraints = @UniqueConstraint(
          name = "uq_security_user_t_fk_security_user_uuid_fk_tag_uuid", columnNames = {"fk_security_user_uuid", "fk_tag_uuid"}),
      joinColumns = @JoinColumn(name = "fk_security_user_uuid", foreignKey = @ForeignKey(name = "security_user_t_fk_security_user_uuid")),
      inverseJoinColumns = @JoinColumn(name = "fk_tag_uuid", foreignKey = @ForeignKey(name = "security_user_t_fk_tag_uuid"))
  )
  @OrderColumn(name = "idx")
  private List<ETag> tags = new LinkedList<>();

  @Transient
  private Set<EPermission> permissions = new LinkedHashSet<>();

  @ManyToMany(targetEntity = ERole.class)
  @JoinTable(name = "security_user_role",
      joinColumns = {@JoinColumn(name = "fk_user_uuid", foreignKey = @ForeignKey(name = "security_user_role_fk_user_uuid"))},
      inverseJoinColumns = {@JoinColumn(name = "fk_role_code", foreignKey = @ForeignKey(name = "security_user_role_fk_role_code"))}
  )
  @OrderBy("code ASC")
  private Set<ERole> roles = new LinkedHashSet<>();

  @ElementCollection
  @CollectionTable(
      name = "security_user_a",
      uniqueConstraints = @UniqueConstraint(
          name = "uq_security_user_a_fk_user_uuid_name", columnNames = {"fk_security_user_uuid", "name"}
      ),
      foreignKey = @ForeignKey(name = "security_user_a_fk_security_user_uuid"),
      joinColumns = @JoinColumn(name = "fk_security_user_uuid")
  )
  private Set<EAttribute<?>> eAttributes = new HashSet<>();
}
