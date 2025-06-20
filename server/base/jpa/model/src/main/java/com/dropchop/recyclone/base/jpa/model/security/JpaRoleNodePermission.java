package com.dropchop.recyclone.base.jpa.model.security;

import com.dropchop.recyclone.base.api.model.security.RoleNodePermission;
import com.dropchop.recyclone.base.jpa.model.base.JpaUuid;
import com.dropchop.recyclone.base.jpa.model.localization.JpaTitleDescriptionTranslation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "security_role_node_permission")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Instance")
public class JpaRoleNodePermission extends JpaUuid implements RoleNodePermission<
        JpaAction, JpaDomain, JpaPermission, JpaRoleNode, JpaTitleDescriptionTranslation> {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_role_node_uuid",
    referencedColumnName = "uuid",
    foreignKey = @ForeignKey(name = "security_role_node_permission_security_role_node_fk"),
    nullable = false)
  private JpaRoleNode roleNode;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fk_permission_uuid",
    referencedColumnName = "uuid",
    foreignKey = @ForeignKey(name = "security_role_node_permission_security_permission_fk"),
    nullable = false)
  private JpaPermission permission;

  @Column(name = "allowed", nullable = false)
  private Boolean allowed;

  @Column(name = "created")
  private ZonedDateTime created;

  @Column(name = "modified")
  private ZonedDateTime modified;
}
