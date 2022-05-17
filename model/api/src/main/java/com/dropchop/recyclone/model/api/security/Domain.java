package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasEmbededTitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;

import java.util.SortedSet;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 01. 22.
 */
public interface Domain<T extends TitleTranslation, A extends Action<T>>
  extends Model, HasCode, HasEmbededTitleTranslation, HasTitleTranslation<T> {

  SortedSet<A> getActions();
  void setActions(SortedSet<A> actions);
}
