package com.dropchop.recyclone.base.dto.model.security;

import com.dropchop.recyclone.base.dto.model.base.DtoId;
import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class RoleNode extends DtoId implements com.dropchop.recyclone.base.api.model.security.RoleNode<RoleNode> {

    //target group info
    private String target;
    private String targetId;

    //parent node
    private RoleNode parent;

    //db entity instance info
    private String entity;
    private String entityId;
    private String entityName;

    //Defines how many levels up on hierarchy
    private Integer maxParentInstanceLevel;

    private ZonedDateTime created;
    private ZonedDateTime modified;


    //permissions set for role node
    private List<RoleNodePermission> roleNodePermissions;


    public boolean isInstance() {
        return this.entity != null && this.entityId != null;
    }

}
