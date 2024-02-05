package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.model.api.marker.HasUuid;

import java.util.UUID;

@SuppressWarnings("unused")
public interface PermissionInstance<
  P extends Permission<TDT, A, D>,
  TDT extends TitleDescriptionTranslation,
  A extends Action<TDT>,
  D extends Domain<TDT, A>
  > extends Model, HasUuid {

  P getPermission();
  void setPermission(P permission);

  String getSubject();
  void setSubject(String subject);

  UUID getSubjectId();
  void setSubjectId(UUID subjectId);


  Boolean getAllowed();
  void setAllowed(Boolean allowed);
}
