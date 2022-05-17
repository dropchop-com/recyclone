package com.dropchop.recyclone.model.api.localization;

import com.dropchop.recyclone.model.api.marker.HasEmbeddedTitleTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 01. 22.
 */
public interface TitleTranslation extends Translation, HasEmbeddedTitleTranslation {
  String getTitle();
  void setTitle(String title);
}
