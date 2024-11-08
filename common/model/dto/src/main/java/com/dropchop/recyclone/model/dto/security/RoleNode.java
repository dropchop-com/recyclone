package com.dropchop.recyclone.model.dto.security;

import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.localization.TitleDescriptionTranslation;
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
public class RoleNode extends DtoId implements com.dropchop.recyclone.model.api.security.RoleNode<
        Action, Domain, Permission, RoleNode, RoleNodePermission, TitleDescriptionTranslation> {

    private String target;
    private String targetId;
    private RoleNode parent;
    private List<RoleNodePermission> roleNodePermissions;
    private String entity;
    private String entityUuid;

}
