package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.security.Action;
import com.dropchop.recyclone.model.entity.jpa.base.ECode;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleTranslation;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
@Getter
@Setter
@Entity
@Table(name = "security_action")
@NoArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class EAction extends ECode implements Action<ETitleTranslation> {

  @Column(name="title")
  private String title;

  @Column(name = "lang", insertable = false, updatable = false)
  private String lang;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, targetEntity = ELanguage.class)
  @JoinColumn(name = "lang", referencedColumnName = "code", foreignKey = @ForeignKey(name = "security_action_fk_language_code"))
  private ELanguage language;

  @ElementCollection
  @CollectionTable(
    name="security_action_l",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_security_action_l_fk_language_code_lang", columnNames = {"fk_security_action_code", "lang"}),
    foreignKey = @ForeignKey(name = "security_action_l_fk_security_action_code"),
    joinColumns = @JoinColumn(name="fk_security_action_code")
  )
  private Set<ETitleTranslation> translations;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;
}
