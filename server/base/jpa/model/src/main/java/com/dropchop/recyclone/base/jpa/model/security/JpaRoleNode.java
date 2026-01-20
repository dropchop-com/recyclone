package com.dropchop.recyclone.base.jpa.model.security;

import com.dropchop.recyclone.base.api.model.security.RoleNode;
import com.dropchop.recyclone.base.jpa.model.base.JpaUuid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "security_role_node")
public class JpaRoleNode extends JpaUuid implements RoleNode<JpaRoleNode> {

  @Column(name = "target", nullable = false)
  private String target;

  @Column(name = "target_id")
  private String targetId;

  @ManyToOne
  @JoinColumn(name = "fk_role_node_uuid",
      referencedColumnName = "uuid",
      foreignKey = @ForeignKey(name = "security_role_node_parent_role_node_fk")
  )
  private JpaRoleNode parent;

  @Column(name = "entity")
  private String entity;

  @Column(name = "entity_id")
  private String entityId;

  @Column(name = "entity_name")
  private String entityName;

  @Column(name = "max_parent_instance_level")
  private Integer maxParentInstanceLevel;

  @Column(name = "created")
  private ZonedDateTime created;

  @Column(name = "modified")
  private ZonedDateTime modified;
}
