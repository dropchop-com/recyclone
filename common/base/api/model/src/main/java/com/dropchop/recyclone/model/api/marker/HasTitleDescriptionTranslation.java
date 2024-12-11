package com.dropchop.recyclone.model.api.marker;

import com.dropchop.recyclone.model.api.localization.TitleDescriptionTranslation;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 01. 22.
 */
public interface HasTitleDescriptionTranslation<TDT extends TitleDescriptionTranslation>
  extends HasTitleTranslation<TDT> {

  @Override
  void setTranslations(Set<TDT> translations);

}
