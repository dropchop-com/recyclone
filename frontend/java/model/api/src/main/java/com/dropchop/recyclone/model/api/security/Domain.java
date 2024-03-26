package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasTranslationInlinedTitleDescription;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 01. 22.
 */
public interface Domain<
  TDT extends TitleDescriptionTranslation,
  A extends Action<TDT>>
  extends Model, HasCode, HasTranslationInlinedTitleDescription, HasTitleTranslation<TDT> {

  Set<A> getActions();
  void setActions(Set<A> actions);
}
