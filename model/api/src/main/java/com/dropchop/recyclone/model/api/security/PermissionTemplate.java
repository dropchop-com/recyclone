package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.localization.TitleTranslation;

import java.util.UUID;

public interface PermissionTemplate<T extends TitleTranslation, A extends Action<T>, D extends Domain<T, A>> extends PermissionInstance<T, A, D>{

  String getSubSubject();
  void setSubSubject(String subSubject);

  UUID getSubSubjectId();
  void setSubSubjectId(UUID subSubjectId);

}
