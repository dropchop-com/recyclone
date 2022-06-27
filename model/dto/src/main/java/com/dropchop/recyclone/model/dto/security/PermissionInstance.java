package com.dropchop.recyclone.model.dto.security;

import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@JsonInclude(NON_NULL)
public class PermissionInstance extends Permission implements com.dropchop.recyclone.model.api.security.PermissionInstance<TitleTranslation, Action, Domain> {

  private UUID permissionId;
  private String subject;
  private UUID subjectId;
  private Boolean allowed;


}
