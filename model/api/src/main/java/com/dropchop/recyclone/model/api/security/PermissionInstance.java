package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.localization.TitleDescriptionTranslation;

import java.util.UUID;

public interface PermissionInstance<
  TDT extends TitleDescriptionTranslation,
  A extends Action<TDT>,
  D extends Domain<TDT, A>> extends Permission<TDT, A, D>{

  UUID getPermissionId();
  void setPermissionId(UUID permissionId);

  String getSubject();
  void setSubject(String subject);

  UUID getSubjectId();
  void setSubjectId(UUID subjectId);


  Boolean getAllowed();
  void setAllowed(Boolean allowed);
}
