package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasEmbeddedTitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;

import java.util.SortedSet;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 01. 22.
 */
public interface Domain<TT extends TitleTranslation, A extends Action<TT>>
  extends Model, HasCode, HasEmbeddedTitleTranslation, HasTitleTranslation<TT> {

  SortedSet<A> getActions();
  void setActions(SortedSet<A> actions);
}
