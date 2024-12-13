package com.dropchop.recyclone.base.dto.model.security;

import com.dropchop.recyclone.base.dto.model.base.DtoId;
import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class RoleNode extends DtoId implements com.dropchop.recyclone.base.api.model.security.RoleNode<
        Action, Domain, Permission, RoleNode, RoleNodePermission, TitleDescriptionTranslation> {

    //target group info
    private String target;
    private String targetId;

    //parent node
    private RoleNode parent;

    //permissions set for role node
    private List<RoleNodePermission> roleNodePermissions;

    //db entity instance info
    private String entity;
    private String entityId;

    //Defines how many levels up on hierarchy
    private Integer maxParentInstanceLevel;

}
