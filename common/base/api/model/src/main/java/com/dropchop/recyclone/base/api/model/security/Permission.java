package com.dropchop.recyclone.base.api.model.security;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.base.api.model.marker.HasTitleDescriptionTranslation;
import com.dropchop.recyclone.base.api.model.marker.HasTranslationInlinedTitleDescription;
import com.dropchop.recyclone.base.api.model.marker.HasUuid;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 11. 01. 22.
 */
public interface Permission<
  TDT extends TitleDescriptionTranslation,
  A extends Action<TDT>,
  D extends Domain<TDT, A>>
  extends Model, HasUuid, HasTranslationInlinedTitleDescription, HasTitleDescriptionTranslation<TDT> {
  D getDomain();
  void setDomain(D domain);

  A getAction();
  void setAction(A action);

  List<String> getInstances();
  void setInstances(List<String> instances);
}
