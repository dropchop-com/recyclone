package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.security.RoleNode;
import com.dropchop.recyclone.model.entity.jpa.base.JpaUuid;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaTitleDescriptionTranslation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "security_role_node")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType = DiscriminatorType.STRING)
public class JpaRoleNode extends JpaUuid implements RoleNode<
        JpaAction, JpaDomain, JpaPermission, JpaRoleNode, JpaRoleNodePermission, JpaTitleDescriptionTranslation> {

    @ManyToOne
    @JoinColumn(name = "fk_role_node_uuid", referencedColumnName = "uuid", foreignKey = @ForeignKey(name = "security_role_node_parent_role_node_fk"))
    private JpaRoleNode parent;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "roleNode")
    private List<JpaRoleNodePermission> roleNodePermissions;

    private String entity;

    private String entityUuid;


}
