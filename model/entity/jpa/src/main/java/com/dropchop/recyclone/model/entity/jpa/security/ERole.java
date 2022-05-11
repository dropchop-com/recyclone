package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.security.Role;
import com.dropchop.recyclone.model.entity.jpa.ECode;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleTranslation;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.SortedSet;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 01. 22.
 */
@Getter
@Setter
@Entity
@Table(name = "security_role")
@NoArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class ERole extends ECode implements Role<ETitleTranslation, EAction, EDomain, EPermission> {

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
  private SortedSet<EPermission> permissions;

  @Column(name="title")
  private String title;

  @Column(name = "lang", insertable = false, updatable = false)
  private String lang;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, targetEntity = ELanguage.class)
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
  private Set<ETitleTranslation> translations;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;
}
