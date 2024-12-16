package com.dropchop.recyclone.base.api.model.marker;

import com.dropchop.recyclone.base.api.model.localization.TitleDescriptionTranslation;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 01. 22.
 */
public interface HasTitleDescriptionTranslation<TDT extends TitleDescriptionTranslation>
  extends HasTitleTranslation<TDT> {

  @Override
  void setTranslations(Set<TDT> translations);

}
