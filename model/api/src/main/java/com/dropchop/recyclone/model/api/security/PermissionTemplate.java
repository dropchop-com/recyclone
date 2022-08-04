package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.localization.TitleTranslation;

import java.util.UUID;

public interface PermissionTemplate<TT extends TitleTranslation, A extends Action<TT>, D extends Domain<TT, A>>
  extends PermissionInstance<TT, A, D>{

  String getSubSubject();
  void setSubSubject(String subSubject);

  UUID getSubSubjectId();
  void setSubSubjectId(UUID subSubjectId);

}
