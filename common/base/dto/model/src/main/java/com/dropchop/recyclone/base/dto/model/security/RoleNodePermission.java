package com.dropchop.recyclone.base.dto.model.security;

import com.dropchop.recyclone.base.dto.model.base.DtoId;
import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

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
@SuppressWarnings("unused")
public class RoleNodePermission extends DtoId implements com.dropchop.recyclone.base.api.model.security.RoleNodePermission<
        Action, Domain, Permission, RoleNode, RoleNodePermission, TitleDescriptionTranslation> {

  @Getter
  private final String type = this.getClass().getSimpleName();

  private RoleNode roleNode;

  private Permission permission;

  private Boolean allowed;

  private ZonedDateTime created;

  private ZonedDateTime modified;

  public void setType(String type) {}


}
