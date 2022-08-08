package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasTitleDescriptionTranslation;
import com.dropchop.recyclone.model.api.marker.HasTranslationInlinedTitleDescription;

public interface RoleGroup<
  TDT extends TitleDescriptionTranslation,
  A extends Action<TDT>,
  D extends Domain<TDT, A>,
  P extends Permission<TDT, A, D>,
  R extends Role<TDT, A, D, P>>
    extends Model, HasCode, HasTranslationInlinedTitleDescription, HasTitleDescriptionTranslation<TDT> {
}
