package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasTitleDescriptionTranslation;
import com.dropchop.recyclone.model.api.marker.HasTranslationInlinedTitleDescription;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 11. 01. 22.
 */
public interface Role<
  TDT extends TitleDescriptionTranslation,
  A extends Action<TDT>,
  D extends Domain<TDT, A>,
  P extends Permission<TDT, A, D>>
  extends Model, HasCode, HasTranslationInlinedTitleDescription, HasTitleDescriptionTranslation<TDT> {

  Set<P> getPermissions();
  void setPermissions(Set<P> permissions);
}
