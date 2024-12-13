package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.api.model.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.base.api.model.security.Action;
import com.dropchop.recyclone.model.entity.jpa.base.JpaCode;
import com.dropchop.recyclone.model.entity.jpa.base.JpaTitleDescriptionTranslationHelper;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaLanguage;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaTitleDescriptionTranslation;
import com.dropchop.recyclone.model.entity.jpa.marker.HasJpaLanguage;
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
@Table(name = "security_action")
public class JpaAction extends JpaCode
  implements Action<JpaTitleDescriptionTranslation>, JpaTitleDescriptionTranslationHelper,
  HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon, HasJpaLanguage {

  @Column(name="title")
  private String title;

  @Column(name="description")
  private String description;

  @Column(name = "lang", insertable = false, updatable = false)
  private String lang;

  @OneToOne(targetEntity = JpaLanguage.class)
  @JoinColumn(name = "lang", referencedColumnName = "code", foreignKey = @ForeignKey(name = "security_action_fk_language_code"))
  private JpaLanguage language;

  @ElementCollection
  @CollectionTable(
    name="security_action_l",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_security_action_l_fk_language_code_lang", columnNames = {"fk_security_action_code", "lang"}),
    foreignKey = @ForeignKey(name = "security_action_l_fk_security_action_code"),
    joinColumns = @JoinColumn(name="fk_security_action_code")
  )
  private Set<JpaTitleDescriptionTranslation> translations;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;

  public JpaAction(@NonNull String code) {
    super(code);
  }
}
