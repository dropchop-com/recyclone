package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.model.api.marker.*;

import java.util.SortedSet;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 01. 22.
 */
public interface Role<
  TDT extends TitleDescriptionTranslation,
  A extends Action<TDT>,
  D extends Domain<TDT, A>,
  P extends Permission<TDT, A, D>>
  extends Model, HasCode, HasTranslationInlinedTitleDescription, HasTitleDescriptionTranslation<TDT> {

  SortedSet<P> getPermissions();
  void setPermissions(SortedSet<P> permissions);
}
