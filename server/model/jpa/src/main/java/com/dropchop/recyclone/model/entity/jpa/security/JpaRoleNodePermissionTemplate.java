package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.security.RoleNodePermission;
import com.dropchop.recyclone.model.api.security.RoleNodePermissionTemplate;
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
@DiscriminatorValue("Template")
public class JpaRoleNodePermissionTemplate extends JpaRoleNodePermission implements RoleNodePermissionTemplate<
        JpaAction, JpaDomain, JpaPermission, JpaRoleNode, JpaRoleNodePermission, JpaTitleDescriptionTranslation> {

    @Column(name="target")
    private String target;

    @Column(name="target_id")
    private String targetId;

}
