package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.api.model.security.Permission;
import com.dropchop.recyclone.model.entity.jpa.base.JpaUuid;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaLanguage;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaTitleDescriptionTranslation;
import com.dropchop.recyclone.model.entity.jpa.marker.HasJpaLanguage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "security_permission")
public class JpaPermission extends JpaUuid
  implements Permission<JpaTitleDescriptionTranslation, JpaAction, JpaDomain>,
    HasJpaLanguage, HasCreated, HasModified, HasDeactivated {

  @ManyToOne(targetEntity = JpaDomain.class)
  @JoinColumn(name = "fk_security_domain_code",
    foreignKey = @ForeignKey(name = "security_permission_fk_security_domain_code"))
  private JpaDomain domain;

  @ManyToOne(targetEntity = JpaAction.class)
  @JoinColumn(name = "fk_security_action_code",
    foreignKey = @ForeignKey(name = "security_permission_fk_security_action_code"))
  private JpaAction action;

  @ElementCollection
  @CollectionTable(
    name="security_permission_instances",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_security_permission_instances_instance",
      columnNames = {"fk_security_permission_uuid", "instance"}
    ),
    foreignKey = @ForeignKey(name = "security_permission_instances_fk_security_permission_uuid"),
    joinColumns=@JoinColumn(name="fk_security_permission_uuid")
  )
  @Column(name="instance")
  private List<String> instances = new ArrayList<>();

  @Column(name="title")
  private String title;

  @Column(name="description")
  private String description;

  @Column(name = "lang", insertable = false, updatable = false)
  private String lang;

  @OneToOne(targetEntity = JpaLanguage.class)
  @JoinColumn(name = "lang", referencedColumnName = "code",
    foreignKey = @ForeignKey(name = "security_permission_fk_language_code"))
  private JpaLanguage language;

  @ElementCollection
  @CollectionTable(
    name="security_permission_l",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_security_permission_l_fk_language_code_lang",
      columnNames = {"fk_security_permission_uuid", "lang"}
    ),
    foreignKey = @ForeignKey(name = "ssecurity_permission_l_fk_security_permission_uuid"),
    joinColumns = @JoinColumn(name="fk_security_permission_uuid")
  )
  private Set<JpaTitleDescriptionTranslation> translations = new HashSet<>();

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;
}
