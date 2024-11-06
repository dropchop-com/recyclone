package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.security.RoleNodePermission;
import com.dropchop.recyclone.model.entity.jpa.base.JpaUuid;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaTitleDescriptionTranslation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "security_role_node_permission")
public class JpaRoleNodePermission extends JpaUuid implements RoleNodePermission<
        JpaAction, JpaDomain, JpaPermission, JpaRoleNode, JpaRoleNodePermission, JpaTitleDescriptionTranslation> {

    @ManyToOne
    @JoinColumn(name = "fk_role_node_uuid", referencedColumnName = "uuid", foreignKey = @ForeignKey(name = "security_role_node_permission_role_node_fk"))
    private JpaRoleNode roleNode;

    @ManyToOne
    @JoinColumn(name = "fk_permission_uuid", referencedColumnName = "uuid", foreignKey = @ForeignKey(name = "security_role_node_permission_permission_fk"))
    private JpaPermission permission;

    @Column(name="allowed")
    private Boolean allowed;

}
