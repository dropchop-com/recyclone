package com.dropchop.recyclone.base.api.model.security;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.base.api.model.marker.HasUuid;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;

import java.util.UUID;

@SuppressWarnings("unused")
public interface PermissionInstance<
  P extends Permission<TDT, A, D>,
  TDT extends TitleDescriptionTranslation,
  A extends Action<TDT>,
  D extends Domain<TDT, A>
  > extends Model, HasUuid, HasCreated, HasModified {

  P getPermission();
  void setPermission(P permission);

  String getSubject();
  void setSubject(String subject);

  UUID getSubjectId();
  void setSubjectId(UUID subjectId);


  Boolean getAllowed();
  void setAllowed(Boolean allowed);
}
