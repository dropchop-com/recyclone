package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.model.api.security.Role;
import com.dropchop.recyclone.model.entity.jpa.base.ECode;
import com.dropchop.recyclone.model.entity.jpa.base.ETitleDescriptionTranslationHelper;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleDescriptionTranslation;
import com.dropchop.recyclone.model.entity.jpa.marker.HasELanguage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "security_role")
@SuppressWarnings("unused")
public class ERole extends ECode
  implements Role<ETitleDescriptionTranslation, EAction, EDomain, EPermission>, ETitleDescriptionTranslationHelper,
  HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon, HasELanguage {

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, targetEntity = EPermission.class)
  @JoinTable(name = "security_role_security_permission",
    joinColumns = {
      @JoinColumn(name = "fk_security_role_code",
        foreignKey = @ForeignKey(name = "security_role_permission_fk_security_role_code")
      )
    },
    inverseJoinColumns = {
      @JoinColumn(name = "fk_security_permission_uuid",
        foreignKey = @ForeignKey(name = "security_role_permission_fk_security_permission_uuid")
      )
    }
  )
  @OrderBy("domain.code ASC, action.code ASC")
  private Set<EPermission> permissions;

  @Column(name="title")
  private String title;

  @Column(name="description")
  private String description;

  @Column(name = "lang", insertable = false, updatable = false)
  private String lang;

  @OneToOne(targetEntity = ELanguage.class)
  @JoinColumn(name = "lang", referencedColumnName = "code", foreignKey = @ForeignKey(name = "security_role_fk_language_code"))
  private ELanguage language;

  @ElementCollection
  @CollectionTable(
    name="security_role_l",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_security_role_l_fk_language_code_lang", columnNames = {"fk_security_role_code", "lang"}),
    foreignKey = @ForeignKey(name = "security_role_l_fk_security_role_code"),
    joinColumns=@JoinColumn(name="fk_security_role_code")
  )
  private Set<ETitleDescriptionTranslation> translations;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;

  public ERole(@NonNull String code) {
    super(code);
  }
}
