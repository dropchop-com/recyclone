package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.security.RoleNode;
import com.dropchop.recyclone.model.entity.jpa.base.JpaUuid;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaTitleDescriptionTranslation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Armando Ota <armando.ota@dropchop.com>
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "security_role_node")
public class JpaRoleNode extends JpaUuid implements RoleNode<
        JpaAction, JpaDomain, JpaPermission, JpaRoleNode, JpaRoleNodePermission, JpaTitleDescriptionTranslation> {

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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "roleNode")
    private List<JpaRoleNodePermission> roleNodePermissions;

    @Column(name = "entity")
    private String entity;

    @Column(name = "entity_id")
    private String entityId;

}
