package com.dropchop.recyclone.base.jpa.model.security;

import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.api.model.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.base.api.model.security.User;
import com.dropchop.recyclone.base.jpa.model.attr.JpaAttribute;
import com.dropchop.recyclone.base.jpa.model.common.JpaPerson;
import com.dropchop.recyclone.base.jpa.model.localization.JpaCountry;
import com.dropchop.recyclone.base.jpa.model.localization.JpaLanguage;
import com.dropchop.recyclone.base.jpa.model.localization.JpaTitleDescriptionTranslation;
import com.dropchop.recyclone.base.jpa.model.localization.JpaTitleTranslation;
import com.dropchop.recyclone.base.jpa.model.marker.HasJpaAttributes;
import com.dropchop.recyclone.base.jpa.model.marker.HasJpaLanguage;
import com.dropchop.recyclone.base.jpa.model.tagging.JpaTag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 01. 22.
 * @author Armando Ota <armando.ota@dropchop.org>
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "security_user")
@SuppressWarnings("unused")
public class JpaUser extends JpaPerson
    implements User<JpaUserAccount, JpaTitleDescriptionTranslation, JpaTitleTranslation, JpaAction, JpaDomain, JpaPermission, JpaRole, JpaCountry, JpaLanguage, JpaTag>,
    HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon, HasJpaLanguage, HasJpaAttributes {

  @Column(name = "created")
  private ZonedDateTime created;

  @Column(name = "modified")
  private ZonedDateTime modified;

  @Column(name = "deactivated")
  private ZonedDateTime deactivated;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  Set<JpaUserAccount> accounts = new LinkedHashSet<>();

  public void addAccount(JpaUserAccount account) {
    if (account == null) return;
    this.accounts.add(account);
    account.setUser(this);
  }

  public void removeAccount(JpaUserAccount account) {
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
  private List<JpaTag> tags = new LinkedList<>();

  @Transient
  private Set<JpaPermission> permissions = new LinkedHashSet<>();

  @ManyToMany(targetEntity = JpaRole.class)
  @JoinTable(name = "security_user_role",
      joinColumns = {@JoinColumn(name = "fk_user_uuid", foreignKey = @ForeignKey(name = "security_user_role_fk_user_uuid"))},
      inverseJoinColumns = {@JoinColumn(name = "fk_role_code", foreignKey = @ForeignKey(name = "security_user_role_fk_role_code"))}
  )
  @OrderBy("code ASC")
  private Set<JpaRole> roles = new LinkedHashSet<>();

  @ElementCollection
  @CollectionTable(
      name = "security_user_a",
      uniqueConstraints = @UniqueConstraint(
          name = "uq_security_user_a_fk_user_uuid_name", columnNames = {"fk_security_user_uuid", "name"}
      ),
      foreignKey = @ForeignKey(name = "security_user_a_fk_security_user_uuid"),
      joinColumns = @JoinColumn(name = "fk_security_user_uuid")
  )
  private Set<JpaAttribute<?>> jpaAttributes = new HashSet<>();
}
