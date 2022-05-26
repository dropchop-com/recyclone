package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasEmbeddedTitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasUuid;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 01. 22.
 */
public interface Permission<T extends TitleTranslation, A extends Action<T>, D extends Domain<T, A>>
  extends Model, HasUuid, HasEmbeddedTitleTranslation, HasTitleTranslation<T> {
  D getDomain();
  void setDomain(D domain);

  A getAction();
  void setAction(A action);

  List<String> getInstances();
  void setInstances(List<String> instances);
}
