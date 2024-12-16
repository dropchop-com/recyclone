package com.dropchop.recyclone.base.jpa.model.security;

import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.api.model.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.base.api.model.security.Role;
import com.dropchop.recyclone.base.jpa.model.base.JpaCode;
import com.dropchop.recyclone.base.jpa.model.base.JpaTitleDescriptionTranslationHelper;
import com.dropchop.recyclone.base.jpa.model.localization.JpaLanguage;
import com.dropchop.recyclone.base.jpa.model.localization.JpaTitleDescriptionTranslation;
import com.dropchop.recyclone.base.jpa.model.marker.HasJpaLanguage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "security_role")
@SuppressWarnings("unused")
public class JpaRole extends JpaCode
  implements Role<JpaTitleDescriptionTranslation, JpaAction, JpaDomain, JpaPermission>, JpaTitleDescriptionTranslationHelper,
  HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon, HasJpaLanguage {

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, targetEntity = JpaPermission.class)
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
  private Set<JpaPermission> permissions;

  @Column(name="title")
  private String title;

  @Column(name="description")
  private String description;

  @Column(name = "lang", insertable = false, updatable = false)
  private String lang;

  @OneToOne(targetEntity = JpaLanguage.class)
  @JoinColumn(name = "lang", referencedColumnName = "code", foreignKey = @ForeignKey(name = "security_role_fk_language_code"))
  private JpaLanguage language;

  @ElementCollection
  @CollectionTable(
    name="security_role_l",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_security_role_l_fk_language_code_lang", columnNames = {"fk_security_role_code", "lang"}),
    foreignKey = @ForeignKey(name = "security_role_l_fk_security_role_code"),
    joinColumns=@JoinColumn(name="fk_security_role_code")
  )
  private Set<JpaTitleDescriptionTranslation> translations;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;

  public JpaRole(@NonNull String code) {
    super(code);
  }
}
