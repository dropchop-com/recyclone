package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.model.api.security.Domain;
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
@Table(name = "security_domain")
public class EDomain extends ECode
  implements Domain<ETitleDescriptionTranslation, EAction>, ETitleDescriptionTranslationHelper,
  HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon, HasELanguage {

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, targetEntity = EAction.class)
  @JoinTable(name = "security_domain_security_action",
    joinColumns = {
      @JoinColumn(name = "fk_security_domain_code",
        foreignKey = @ForeignKey(name = "security_domain_action_fk_security_domain_code")
      )
    },
    inverseJoinColumns = {
      @JoinColumn(name = "fk_security_action_code",
        foreignKey = @ForeignKey(name = "security_domain_action_fk_security_action_code")
      )
    }
  )
  @OrderBy("code ASC")
  Set<EAction> actions;

  @Column(name="title")
  private String title;

  @Column(name="description")
  private String description;

  @Column(name = "lang", insertable = false, updatable = false)
  private String lang;

  @OneToOne(targetEntity = ELanguage.class)
  @JoinColumn(name = "lang", referencedColumnName = "code", foreignKey = @ForeignKey(name = "security_domain_fk_language_code"))
  private ELanguage language;

  @ElementCollection
  @CollectionTable(
    name="security_domain_l",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_security_domain_l_fk_language_code_lang",
      columnNames = {"fk_security_domain_code", "lang"}
    ),
    foreignKey = @ForeignKey(name = "security_domain_l_fk_security_domain_code"),
    joinColumns = @JoinColumn(name="fk_security_domain_code")
  )
  private Set<ETitleDescriptionTranslation> translations;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;

  public EDomain(@NonNull String code) {
    super(code);
  }
}
