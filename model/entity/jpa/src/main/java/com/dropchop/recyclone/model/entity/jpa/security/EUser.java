package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.model.api.security.User;
import com.dropchop.recyclone.model.entity.jpa.base.EUuid;
import com.dropchop.recyclone.model.entity.jpa.common.EPerson;
import com.dropchop.recyclone.model.entity.jpa.localization.ECountry;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleDescriptionTranslation;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleTranslation;
import com.dropchop.recyclone.model.entity.jpa.marker.HasELanguage;
import com.dropchop.recyclone.model.entity.jpa.tagging.ETag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "security_user")
@SuppressWarnings("JpaDataSourceORMInspection")
public class EUser<O extends EUuid> extends EPerson
  implements User<EUserAccount, ETitleDescriptionTranslation, ETitleTranslation, EAction, EDomain, EPermission, ERole, O, ECountry, ELanguage, ETag>,
  HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon, HasELanguage {


  @Column(name="default_email")
  private String defaultEmail;

  @Column(name="default_phone")
  private String defaultPhone;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;

  @Column(name="owner_uuid")
  private UUID ownerUuid;

  @Column(name="owner_type")
  private String ownerType;

  @Transient
  private O owner;

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = true)
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
    name="security_user_t",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_security_user_t_fk_security_user_uuid_fk_tag_uuid", columnNames = {"fk_security_user_uuid", "fk_tag_uuid"}),
    joinColumns = @JoinColumn( name="fk_security_user_uuid", foreignKey = @ForeignKey(name = "security_user_t_fk_security_user_uuid")),
    inverseJoinColumns = @JoinColumn( name="fk_tag_uuid", foreignKey = @ForeignKey(name = "security_user_t_fk_tag_uuid"))
  )
  @OrderColumn(name = "idx")
  private List<ETag> tags = new LinkedList<>();

  @Transient
  SortedSet<EPermission> permissions = new TreeSet<>();

  @ManyToMany(targetEntity = ERole.class)
  @JoinTable(name = "security_user_role",
      joinColumns = {@JoinColumn(name = "fk_user_uuid", foreignKey = @ForeignKey(name = "security_user_role_fk_user_uuid"))},
      inverseJoinColumns = {@JoinColumn(name = "fk_role_code", foreignKey = @ForeignKey(name = "security_user_role_fk_role_code"))}
  )
  @OrderBy("code ASC")
  SortedSet<ERole> roles = new TreeSet<>();
}
