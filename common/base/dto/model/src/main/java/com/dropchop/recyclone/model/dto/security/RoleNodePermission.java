package com.dropchop.recyclone.model.dto.security;

import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.localization.TitleDescriptionTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(NON_NULL)
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "type"
)
public class RoleNodePermission extends DtoId implements com.dropchop.recyclone.model.api.security.RoleNodePermission<
        Action, Domain, Permission, RoleNode, RoleNodePermission, TitleDescriptionTranslation> {

    private final String type = this.getClass().getSimpleName();

    private RoleNode roleNode;
    private Permission permission;
    private Boolean allowed;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {}
}
