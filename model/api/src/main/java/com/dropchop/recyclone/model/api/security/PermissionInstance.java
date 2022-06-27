package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.localization.TitleTranslation;

import java.util.UUID;

public interface PermissionInstance<T extends TitleTranslation, A extends Action<T>, D extends Domain<T, A>> extends Permission<T, A, D>{

  UUID getPermissionId();
  void setPermissionId(UUID permissionId);

  String getSubject();
  void setSubject(String subject);

  UUID getSubjectId();
  void setSubjectId(UUID subjectId);


  Boolean getAllowed();
  void setAllowed(Boolean allowed);
}
