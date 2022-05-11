package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.Model;
import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;

import java.util.SortedSet;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 01. 22.
 */
public interface Role<T extends TitleTranslation, A extends Action<T>, D extends Domain<T, A>, P extends Permission<T, A, D>>
  extends Model, HasCode, HasTitleTranslation<T> {

  SortedSet<P> getPermissions();
  void setPermissions(SortedSet<P> permissions);
}
