package com.dropchop.recyclone.model.dto.security;

import com.dropchop.recyclone.model.dto.localization.TitleDescriptionTranslation;
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
@SuppressWarnings("unused")
public class PermissionTemplate extends PermissionInstance
  implements com.dropchop.recyclone.base.api.model.security.PermissionTemplate<
    Permission, TitleDescriptionTranslation, Action, Domain> {

  private String subSubject;
  private UUID subSubjectId;
}
