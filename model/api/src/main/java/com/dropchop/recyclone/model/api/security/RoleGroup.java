package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasEmbeddedTitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;

public interface RoleGroup<
  TT extends TitleTranslation,
  A extends Action<TT>,
  D extends Domain<TT, A>,
  P extends Permission<TT, A, D>,
  R extends Role<TT, A, D, P>>
    extends Model, HasCode, HasEmbeddedTitleTranslation, HasTitleTranslation<TT> {
}
