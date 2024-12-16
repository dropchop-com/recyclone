package com.dropchop.recyclone.base.api.model.security;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.base.api.model.marker.HasCode;
import com.dropchop.recyclone.base.api.model.marker.HasTitleTranslation;
import com.dropchop.recyclone.base.api.model.marker.HasTranslationInlinedTitleDescription;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 11. 01. 22.
 */
public interface Domain<
  TDT extends TitleDescriptionTranslation,
  A extends Action<TDT>>
  extends Model, HasCode, HasTranslationInlinedTitleDescription, HasTitleTranslation<TDT> {

  Set<A> getActions();
  void setActions(Set<A> actions);
}
