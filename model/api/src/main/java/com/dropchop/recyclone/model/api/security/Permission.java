package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.model.api.marker.HasTitleDescriptionTranslation;
import com.dropchop.recyclone.model.api.marker.HasTranslationInlinedTitleDescription;
import com.dropchop.recyclone.model.api.marker.HasUuid;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 01. 22.
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
