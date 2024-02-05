package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.localization.TitleDescriptionTranslation;

import java.util.UUID;

@SuppressWarnings("unused")
public interface PermissionTemplate<
  P extends Permission<TDT, A, D>,
  TDT extends TitleDescriptionTranslation,
  A extends Action<TDT>,
  D extends Domain<TDT, A>>
  extends PermissionInstance<P, TDT, A, D> {

  String getSubSubject();
  void setSubSubject(String subSubject);

  UUID getSubSubjectId();
  void setSubSubjectId(UUID subSubjectId);

}
