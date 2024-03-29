package com.dropchop.recyclone.model.dto.invoke;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class PermissionDetailsParams extends IdentifierParams {

  private String subject;
  private String subjectID;
  private String subSubject;
  private String subSubjectId;
  private Boolean allowed;
  private String permissionId;
}


