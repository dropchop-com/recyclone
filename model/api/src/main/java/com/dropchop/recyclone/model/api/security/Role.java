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
public interface Role<
  TT extends TitleTranslation,
  A extends Action<TT>,
  D extends Domain<TT, A>,
  P extends Permission<TT, A, D>>
  extends Model, HasCode, HasEmbeddedTitleTranslation, HasTitleTranslation<TT> {

  SortedSet<P> getPermissions();
  void setPermissions(SortedSet<P> permissions);
}
