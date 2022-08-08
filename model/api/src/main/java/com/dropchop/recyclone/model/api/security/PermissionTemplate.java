package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.localization.TitleDescriptionTranslation;

import java.util.UUID;

public interface PermissionTemplate<TDT extends TitleDescriptionTranslation, A extends Action<TDT>, D extends Domain<TDT, A>>
  extends PermissionInstance<TDT, A, D>{

  String getSubSubject();
  void setSubSubject(String subSubject);

  UUID getSubSubjectId();
  void setSubSubjectId(UUID subSubjectId);

}
