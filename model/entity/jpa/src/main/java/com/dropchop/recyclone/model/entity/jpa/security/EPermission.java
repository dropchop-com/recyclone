package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.security.Permission;
import com.dropchop.recyclone.model.entity.jpa.base.EUuid;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleDescriptionTranslation;
import com.dropchop.recyclone.model.entity.jpa.marker.HasELanguage;
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
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "security_permission")
public class EPermission extends EUuid
  implements Permission<ETitleDescriptionTranslation, EAction, EDomain>,
  HasELanguage, HasCreated, HasModified, HasDeactivated {

  @ManyToOne(targetEntity = EDomain.class)
  @JoinColumn(name = "fk_security_domain_code",
    foreignKey = @ForeignKey(name = "security_permission_fk_security_domain_code"))
  private EDomain domain;

  @ManyToOne(targetEntity = EAction.class)
  @JoinColumn(name = "fk_security_action_code",
    foreignKey = @ForeignKey(name = "security_permission_fk_security_action_code"))
  private EAction action;

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

  @OneToOne(targetEntity = ELanguage.class)
  @JoinColumn(name = "lang", referencedColumnName = "code",
    foreignKey = @ForeignKey(name = "security_permission_fk_language_code"))
  private ELanguage language;

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
  private Set<ETitleDescriptionTranslation> translations = new HashSet<>();

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;
}
