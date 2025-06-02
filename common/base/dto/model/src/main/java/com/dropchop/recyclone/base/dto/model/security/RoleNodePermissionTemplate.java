package com.dropchop.recyclone.base.dto.model.security;

import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
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
public class RoleNodePermissionTemplate extends RoleNodePermission
        implements com.dropchop.recyclone.base.api.model.security.RoleNodePermissionTemplate<
        Action, Domain, Permission, RoleNode, TitleDescriptionTranslation> {

   private String target;
   private String targetId;
}
