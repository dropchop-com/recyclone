package com.dropchop.recyclone.base.dto.model.security;

import com.dropchop.recyclone.base.dto.model.base.DtoId;
import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@JsonInclude(NON_NULL)
public class PermissionInstance extends DtoId
  implements com.dropchop.recyclone.base.api.model.security.PermissionInstance<Permission, TitleDescriptionTranslation, Action, Domain> {

  private Permission permission;
  private String subject;
  private UUID subjectId;
  private Boolean allowed;
  private ZonedDateTime created;
  private ZonedDateTime modified;

}
